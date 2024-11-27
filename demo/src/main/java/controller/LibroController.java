package controller;

import java.util.List;

import model.Autor;
import model.AutorDAO;
import model.Libro;
import model.LibroDAO;

public class LibroController {
    private LibroDAO libroDAO = new LibroDAO();
    private AutorDAO autorDAO = new AutorDAO();

    public void agregarLibro(String titulo, int anioPublicacion, String estado, Long autorId) {
        Autor autor = autorDAO.buscarPorId(autorId);
        if (autor != null) {
            Libro libro = new Libro(titulo, anioPublicacion, estado, autor);
            libroDAO.guardar(libro);
        }
    }

    public List<Libro> obtenerLibros() {
        return libroDAO.obtenerTodos();
    }

    public void eliminarLibro(Long id) {
        libroDAO.eliminar(id);
    }
    
    public Libro buscarPorId(Long id) {
        return libroDAO.buscarPorId(id);
    }

    public void actualizarLibro(Long id, String nuevoTitulo, String nuevoEstado) {
        // Buscar el libro por su ID
        Libro libro = libroDAO.buscarPorId(id);
        if (libro != null) {
            // Actualizar el título y el estado del libro
            libro.setTitulo(nuevoTitulo);
            libro.setEstado(nuevoEstado);
            // Guardar el libro actualizado
            libroDAO.guardar(libro);
        }
    }
    
}
