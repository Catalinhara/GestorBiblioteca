package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Libro> libros = new ArrayList<>();

    // Constructor por defecto
    public Autor() {}

    // Constructor con parámetros
    public Autor(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<Libro> getLibros() { return libros; }
    public void setLibros(List<Libro> libros) { this.libros = libros; }

    // Método para agregar un libro a la lista
    public void agregarLibro(Libro libro) {
        libros.add(libro);
        libro.setAutor(this);
    }

    // Método para eliminar un libro de la lista
    public void eliminarLibro(Libro libro) {
        libros.remove(libro);
        libro.setAutor(null);
    }
}
