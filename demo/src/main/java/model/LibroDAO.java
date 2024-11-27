package model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class LibroDAO {
    // Se crea una fábrica de EntityManager utilizando la unidad de persistencia "bibliotecaPU"
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("bibliotecaPU");

    // Método para guardar un libro (insertar o actualizar)
    public void guardar(Libro libro) {
        // Se crea un EntityManager para interactuar con la base de datos
        EntityManager em = emf.createEntityManager();
        try {
            // Inicia una transacción para asegurar que las operaciones sean atómicas
            em.getTransaction().begin();
            
            // Si el libro no tiene un ID (es nuevo) o el libro no existe en la base de datos, se persiste
            if (libro.getId() == null || em.find(Libro.class, libro.getId()) == null) {
                em.persist(libro);  // Persistir libro nuevo
            } else {
                em.merge(libro);    // Si el libro ya existe, se actualiza
            }
            
            // Se confirma la transacción
            em.getTransaction().commit();
        } catch (Exception e) {
            // Si ocurre algún error, se revierte la transacción
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // Se lanza la excepción para que sea manejada más arriba en la pila de llamadas
            throw e;
        } finally {
            // Finalmente, se cierra el EntityManager para liberar recursos
            em.close();
        }
    }

    // Método para obtener todos los libros registrados en la base de datos
    public List<Libro> obtenerTodos() {
        // Se crea un EntityManager para interactuar con la base de datos
        EntityManager em = emf.createEntityManager();
        
        // Se ejecuta la consulta JPQL para obtener todos los libros
        List<Libro> libros = em.createQuery("FROM Libro", Libro.class).getResultList();
        
        // Se cierra el EntityManager
        em.close();
        
        // Se devuelve la lista de libros
        return libros;
    }

    // Método para buscar un libro por su ID
    public Libro buscarPorId(Long id) {
        // Se crea un EntityManager para interactuar con la base de datos
        EntityManager em = emf.createEntityManager();
        
        // Se busca el libro en la base de datos por su ID
        Libro libro = em.find(Libro.class, id);
        
        // Se cierra el EntityManager
        em.close();
        
        // Se devuelve el libro encontrado (o null si no se encuentra)
        return libro;
    }

    // Método para eliminar un libro por su ID
    public void eliminar(Long id) {
        // Se crea un EntityManager para interactuar con la base de datos
        EntityManager em = emf.createEntityManager();
        try {
            // Inicia una transacción
            em.getTransaction().begin();
            
            // Se busca el libro en la base de datos por su ID
            Libro libro = em.find(Libro.class, id);
            
            // Si el libro existe, se procede con la eliminación
            if (libro != null) {
                em.remove(libro);  // Elimina el libro de la base de datos
            }
            
            // Confirma la transacción
            em.getTransaction().commit();
        } catch (Exception e) {
            // Si ocurre un error, se revierte la transacción
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // Se lanza la excepción para que sea manejada más arriba en la pila de llamadas
            throw e;
        } finally {
            // Se cierra el EntityManager
            em.close();
        }
    }
}
