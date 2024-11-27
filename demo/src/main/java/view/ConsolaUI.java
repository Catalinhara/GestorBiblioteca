package view;

import java.util.List;
import java.util.Scanner;

import controller.AutorController;
import controller.LibroController;
import model.Autor;
import model.Libro;

public class ConsolaUI {
    // Instancia de controladores para manejar la lógica de Autor y Libro
    private AutorController autorController = new AutorController();
    private LibroController libroController = new LibroController();
    private Scanner scanner = new Scanner(System.in);

    // Método principal para iniciar la interfaz de usuario de consola
    public void iniciar() {
        System.out.println("Bienvenido al sistema de gestión de biblioteca.");

        while (true) {
            System.out.println("\nOpciones:");
            System.out.println("1) Añadir Autor");
            System.out.println("2) Añadir Libro");
            System.out.println("3) Mostrar Autores y Libros");
            System.out.println("4) Actualizar Libro");
            System.out.println("5) Eliminar Autor o Libro");
            System.out.println("6) Salir");
            System.out.print("Seleccione una opción: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consume el salto de línea

            // Selección de la opción basada en el input del usuario
            switch (opcion) {
                case 1 -> agregarAutor();
                case 2 -> agregarLibro();
                case 3 -> mostrarAutoresYLibros();
                case 4 -> actualizarLibro();
                case 5 -> eliminarAutorOLibro();
                case 6 -> {
                    System.out.println("Saliendo...");
                    return; // Salir del bucle y finalizar el programa
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    // Método para agregar un nuevo autor
    private void agregarAutor() {
        System.out.print("Ingrese el nombre del autor: ");
        String nombre = scanner.nextLine();
        autorController.agregarAutor(nombre);
        System.out.println("Autor agregado correctamente.");
    }

    // Método para agregar un nuevo libro
    private void agregarLibro() {
        System.out.print("Ingrese el título del libro: ");
        String titulo = scanner.nextLine();
        System.out.print("Ingrese el año de publicación: ");
        int año = scanner.nextInt();
        scanner.nextLine(); // Consume el salto de línea
        System.out.print("Ingrese el estado del libro (Disponible/Prestado): ");
        String estado = scanner.nextLine();

        System.out.print("Ingrese el ID del autor: ");
        Long autorId = scanner.nextLong();
        scanner.nextLine(); // Consume el salto de línea

        // Verificación de existencia del autor antes de asociarlo al libro
        Autor autor = autorController.buscarPorId(autorId);
        if (autor == null) {
            System.out.println("Autor no encontrado.");
            return;
        }

        libroController.agregarLibro(titulo, año, estado, autorId);
        System.out.println("Libro agregado correctamente.");
    }

    // Método para mostrar todos los autores y sus libros
    private void mostrarAutoresYLibros() {
        List<Autor> autores = autorController.obtenerAutores();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
            return;
        }

        // Iteración y visualización de cada autor y sus libros
        for (Autor autor : autores) {
            System.out.println("Autor ID: " + autor.getId() + ", Nombre: " + autor.getNombre());
            List<Libro> libros = autor.getLibros();

            if (libros.isEmpty()) {
                System.out.println("  * Este autor no tiene libros.");
            } else {
                for (Libro libro : libros) {
                    System.out.println("  * Libro ID: " + libro.getId() + ", Título: " + libro.getTitulo() +
                                       ", Año: " + libro.getAnioPublicacion() + ", Estado: " + libro.getEstado());
                }
            }
        }
    }

    // Método para actualizar los datos de un libro existente
    private void actualizarLibro() {
        System.out.print("Ingrese el ID del libro a actualizar: ");
        Long libroId = scanner.nextLong();
        scanner.nextLine(); // Consume el salto de línea
        Libro libro = libroController.buscarPorId(libroId);

        if (libro == null) {
            System.out.println("Libro no encontrado.");
            return;
        }

        // Pedir nuevos valores al usuario, permitiendo dejar en blanco para no cambiar
        System.out.print("Ingrese el nuevo título del libro (deje vacío para no cambiar): ");
        String nuevoTitulo = scanner.nextLine();
        System.out.print("Ingrese el nuevo estado del libro (deje vacío para no cambiar): ");
        String nuevoEstado = scanner.nextLine();

        if (!nuevoTitulo.isEmpty()) {
            libro.setTitulo(nuevoTitulo);
        }
        if (!nuevoEstado.isEmpty()) {
            libro.setEstado(nuevoEstado);
        }

        libroController.actualizarLibro(libro.getId(), libro.getTitulo(), libro.getEstado());
        System.out.println("Libro actualizado correctamente.");
    }

    // Método para eliminar un autor o un libro basado en la selección del usuario
    private void eliminarAutorOLibro() {
        System.out.print("¿Desea eliminar un Autor o un Libro? (Ingrese 'Autor' o 'Libro'): ");
        String tipo = scanner.nextLine().trim().toLowerCase();

        if (tipo.equals("autor")) {
            System.out.print("Ingrese el ID del autor a eliminar: ");
            Long autorId = scanner.nextLong();
            scanner.nextLine(); // Consume el salto de línea

            // Verificación de existencia del autor antes de eliminar
            Autor autor = autorController.buscarPorId(autorId);
            if (autor != null) {
                autorController.eliminarAutor(autorId);
                System.out.println("Autor y sus libros eliminados correctamente.");
            } else {
                System.out.println("Autor no encontrado.");
            }
        } else if (tipo.equals("libro")) {
            System.out.print("Ingrese el ID del libro a eliminar: ");
            Long libroId = scanner.nextLong();
            scanner.nextLine(); // Consume el salto de línea

            // Verificación de existencia del libro antes de eliminar
            Libro libro = libroController.buscarPorId(libroId);
            if (libro != null) {
                libroController.eliminarLibro(libroId);
                System.out.println("Libro eliminado correctamente.");
            } else {
                System.out.println("Libro no encontrado.");
            }
        } else {
            System.out.println("Opción no válida.");
        }
    }
}
