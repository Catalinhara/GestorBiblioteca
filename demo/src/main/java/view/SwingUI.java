package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import controller.AutorController;
import controller.LibroController;
import model.Autor;
import model.Libro;

// Clase SwingUI que extiende JFrame para crear la interfaz de usuario de la aplicación
public class SwingUI extends JFrame {
    private LibroController libroController = new LibroController();
    private AutorController autorController = new AutorController();
// Componentes para la tabla de visualización
    private DefaultTableModel tableModel;
    private JTable table;
// Campos de entrada para el formulario
    private JTextField tituloField = new JTextField(15);
    private JTextField autorField = new JTextField(15);
    private JTextField anioField = new JTextField(10);
    private JComboBox<String> estadoComboBox = new JComboBox<>(new String[]{"Disponible", "Prestado"});
// Botones para las diferentes acciones
    private JButton addAutorButton = new JButton("Añadir Autor");
    private JButton addLibroButton = new JButton("Añadir Libro");
    private JButton readButton = new JButton("Listar Datos");
    private JButton updateButton = new JButton("Actualizar Libro");
    private JButton deleteButton = new JButton("Eliminar");
    private JButton clearButton = new JButton("Limpiar");
// Constructor que configura la interfaz
    public SwingUI() {
        super("Gestión de Librería");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

 // Panel de entrada de datos
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(new Color(173, 216, 230));
// Añade etiquetas y campos de entrada al panel de entrada
        inputPanel.add(crearLabel("Título:", Color.DARK_GRAY));
        inputPanel.add(tituloField);
        inputPanel.add(crearLabel("Autor:", Color.DARK_GRAY));
        inputPanel.add(autorField);
        inputPanel.add(crearLabel("Año de Publicación:", Color.DARK_GRAY));
        inputPanel.add(anioField);
        inputPanel.add(crearLabel("Estado:", Color.DARK_GRAY));
        inputPanel.add(estadoComboBox);
// Personaliza y añade botones al panel de botones
        personalizarBoton(addAutorButton, new Color(34, 139, 34), Color.WHITE, 16);
        personalizarBoton(addLibroButton, new Color(0, 102, 204), Color.WHITE, 16);
        personalizarBoton(readButton, new Color(0, 153, 255), Color.WHITE, 16);
        personalizarBoton(updateButton, new Color(0, 102, 204), Color.WHITE, 16);
        personalizarBoton(deleteButton, new Color(255, 51, 51), Color.WHITE, 16);
        personalizarBoton(clearButton, new Color(102, 102, 255), Color.WHITE, 16);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(173, 216, 230));
        buttonPanel.add(addAutorButton);
        buttonPanel.add(addLibroButton);
        buttonPanel.add(readButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
// Configura el borde de los campos de entrada
        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        tituloField.setBorder(border);
        autorField.setBorder(border);
        anioField.setBorder(border);
        estadoComboBox.setBorder(border);
        //Configura la tabla para mostrar datos de libros
        tableModel = new DefaultTableModel(new Object[]{"ID", "Título", "Autor", "Año", "Estado"}, 0);
        table = new JTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
// Carga de datos inicial
        cargarLibros();
// Asigna acciones a los botones
        addAutorButton.addActionListener(e -> mostrarPopupNuevoAutor());
        addLibroButton.addActionListener(e -> agregarLibro());
        readButton.addActionListener(e -> cargarLibros());
        updateButton.addActionListener(e -> actualizarLibro());
        deleteButton.addActionListener(e -> eliminarLibro());
        clearButton.addActionListener(e -> limpiarCampos());
// Deshabilita botones de actualizar y eliminar si no hay selección
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
// Listener para habilitar botones al seleccionar una fila de la tabla
        table.getSelectionModel().addListSelectionListener(e -> {
            boolean seleccion = table.getSelectedRow() != -1;
            updateButton.setEnabled(seleccion);
            deleteButton.setEnabled(seleccion);

            if (seleccion) {
                int selectedRow = table.getSelectedRow();
                tituloField.setText((String) tableModel.getValueAt(selectedRow, 1));
                autorField.setText((String) tableModel.getValueAt(selectedRow, 2));
                anioField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
                estadoComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 4));
            }
        });
// Configura el diseño general de la ventana
        setLayout(new BorderLayout(10, 10));
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
// Limita la entrada de solo números en el campo de año
        restringirEntradaNumerica(anioField);
    }
// Método para mostrar un popup de agregar autor
    private void mostrarPopupNuevoAutor() {
        JTextField nuevoAutorField = new JTextField(15);
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.add(new JLabel("Nombre del Nuevo Autor:"));
        panel.add(nuevoAutorField);

        int result = JOptionPane.showConfirmDialog(
                this, 
                panel, 
                "Añadir Nuevo Autor", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String nombre = nuevoAutorField.getText().trim();
            if (nombre.isEmpty()) {
                mostrarMensajeError("El nombre del autor no puede estar vacío.");
                return;
            }
            autorController.agregarAutor(nombre);
            mostrarMensajeExito("Autor agregado correctamente.");
            cargarLibros(); // Actualizar tabla para mostrar el nuevo autor
        }
    }
// Método para cargar los datos de libros y autores en la tabla
    private void cargarLibros() {
        tableModel.setRowCount(0); // Limpia la tabla
        List<Autor> autores = autorController.obtenerAutores(); // Obtiene todos los autores
    
        for (Autor autor : autores) {
            List<Libro> libros = autor.getLibros(); // Libros asociados al autor
    
            if (libros.isEmpty()) {
                // Si el autor no tiene libros, muestra solo su información con el ID del autor
                tableModel.addRow(new Object[]{
                    autor.getId(),   // ID del autor
                    "Sin libro",     // Mensaje indicando que no hay libro
                    autor.getNombre(),
                    null,            // Sin año de publicación
                    null             // Sin estado
                });
            } else {
                // Si el autor tiene libros, muestra cada uno de ellos
                for (Libro libro : libros) {
                    tableModel.addRow(new Object[]{
                        libro.getId(),
                        libro.getTitulo(),
                        autor.getNombre(),
                        libro.getAnioPublicacion(),
                        libro.getEstado()
                    });
                }
            }
        }
    }
    
 // Método para añadir un libro
    private void agregarLibro() {
        if (!validarCampos()) return;
        String titulo = tituloField.getText().trim();
        String nombreAutor = autorField.getText().trim();
        int anio = Integer.parseInt(anioField.getText().trim());
        String estado = (String) estadoComboBox.getSelectedItem();

        Autor autor = autorController.obtenerAutores()
                .stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(nombreAutor))
                .findFirst()
                .orElseGet(() -> {
                    autorController.agregarAutor(nombreAutor);
                    return autorController.obtenerAutores()
                            .stream()
                            .filter(a -> a.getNombre().equalsIgnoreCase(nombreAutor))
                            .findFirst()
                            .orElse(null);
                });

        if (autor != null) {
            libroController.agregarLibro(titulo, anio, estado, autor.getId());
            mostrarMensajeExito("Libro agregado correctamente.");
            cargarLibros();
            limpiarCampos();
        } else {
            mostrarMensajeError("Error al agregar el libro. Autor no encontrado.");
        }
    }
// Método para actualizar un libro
    private void actualizarLibro() {
        if (!validarCampos()) return;
    
        int selectedRow = table.getSelectedRow();
        Long id = (Long) tableModel.getValueAt(selectedRow, 0);
        String nuevoTitulo = tituloField.getText().trim();
        String estado = (String) estadoComboBox.getSelectedItem();
    
        // Llamada al controlador con el título y el estado
        libroController.actualizarLibro(id, nuevoTitulo, estado);
        mostrarMensajeExito("Libro actualizado correctamente.");
        cargarLibros();
        limpiarCampos();
    }
      // Método para eliminar un libro
    private void eliminarLibro() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1 && confirmarAccion("¿Seguro que deseas eliminar los datos?")) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0); // ID de la entidad seleccionada
            String titulo = (String) tableModel.getValueAt(selectedRow, 1); // Título del libro o "Sin libro"
            String nombreAutor = (String) tableModel.getValueAt(selectedRow, 2); // Nombre del autor
    
            // Verifica si la fila representa un autor sin libros
            if (id != null && "Sin libro".equals(titulo)) {
                // Si la fila representa un autor sin libros, elimina solo al autor
                autorController.eliminarAutor(id);
                mostrarMensajeExito("Autor eliminado correctamente.");
            } else {
                // Si la fila representa un libro específico, elimina el libro
                libroController.eliminarLibro(id);
    
                // Busca el autor por nombre para verificar si tiene otros libros
                Autor autor = autorController.obtenerAutores().stream()
                        .filter(a -> a.getNombre().equals(nombreAutor))
                        .findFirst()
                        .orElse(null);
    
                if (autor != null) {
                    // Remover el libro eliminado de la lista de libros del autor
                    autor.getLibros().removeIf(libro -> libro.getId().equals(id));
                    autorController.guardar(autor); // Guardar cambios en el autor
    
                    // Si el autor no tiene más libros, eliminar también al autor
                    if (autor.getLibros().isEmpty()) {
                        autorController.eliminarAutor(autor.getId());
                        mostrarMensajeExito("Autor y su libro eliminado correctamente.");
                    } else {
                        mostrarMensajeExito("Libro eliminado correctamente.");
                    }
                }
            }
    
            cargarLibros(); // Recarga la tabla para reflejar los cambios
            limpiarCampos();
        }
    }
    
    
// Método para personalizar botones
    private void personalizarBoton(JButton boton, Color colorFondo, Color colorTexto, int tamañoFuente) {
        boton.setBackground(colorFondo);
        boton.setForeground(colorTexto);
        boton.setFont(new Font("Arial", Font.BOLD, tamañoFuente));
        boton.setPreferredSize(new Dimension(120, 40));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
// Método para crear etiquetas estilizadas
    private JLabel crearLabel(String texto, Color color) {
        JLabel label = new JLabel(texto);
        label.setForeground(color);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }
 // Método para restringir la entrada numérica en un campo de texto
    private void restringirEntradaNumerica(JTextField textField) {
        textField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JTextField source = (JTextField) input;
                String text = source.getText().trim();
                try {
                    if (text.matches("\\d{4}") || text.equals("")) {
                        return true;
                    } else {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    mostrarMensajeError("El año debe ser un número válido de 4 dígitos.");
                    return false;
                }
            }
        });
    }
// Validación de campos antes de realizar acciones CRUD
    private boolean validarCampos() { 
        String titulo = tituloField.getText().trim();
        String autor = autorField.getText().trim();
        String anioTexto = anioField.getText().trim();
    
        if (titulo.isEmpty() || autor.isEmpty()) {
            mostrarMensajeError("El título y el autor no pueden estar vacíos.");
            return false;
        }
    
        // Validar que el título no sea "Sin libro" para evitar un bug de borrar datos
        if ("Sin libro".equalsIgnoreCase(titulo)) {
            mostrarMensajeError("El título no puede ser 'Sin libro'. Introduzca un título válido.");
            return false;
        }
    
        try {
            int anio = Integer.parseInt(anioTexto);
            if (anio < 0 || anio > 9999) {
                mostrarMensajeError("El año debe ser un número válido entre 0 y 9999.");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarMensajeError("El año debe ser un número válido.");
            return false;
        }
        return true;
    }
    
// Métodos de utilidad para mostrar mensajes
    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean confirmarAccion(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(this, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION);
        return respuesta == JOptionPane.YES_OPTION;
    }
// Método para limpiar campos de entrada
    private void limpiarCampos() {
        tituloField.setText("");
        autorField.setText("");
        anioField.setText("");
        estadoComboBox.setSelectedIndex(0);
        table.clearSelection();
    }
// Método principal para iniciar la interfaz
    public void start() {
        setVisible(true);
    }
}
