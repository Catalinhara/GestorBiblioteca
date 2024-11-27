package controller;

import java.util.List;

import model.Autor;
import model.AutorDAO;

public class AutorController {
    private AutorDAO autorDAO = new AutorDAO();

    public void agregarAutor(String nombre) {
        Autor autor = new Autor(nombre);
        autorDAO.guardar(autor);
    }

    public List<Autor> obtenerAutores() {
        return autorDAO.obtenerTodos();
    }
    public Autor buscarPorId(Long id) {
        return autorDAO.buscarPorId(id);
    }

    public void eliminarAutor(Long id) {
        autorDAO.eliminar(id);
    }

    public void guardar(Autor autor) {
        autorDAO.guardar(autor);
    }

    
}
