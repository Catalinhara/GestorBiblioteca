package model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AutorDAO {
    // Se crea una fábrica de EntityManager utilizando la unidad de persistencia "bibliotecaPU"
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("bibliotecaPU");

    // Método para guardar un autor (insertar o actualizar)
    public void guardar(Autor autor) {
        // Se crea un EntityManager para interactuar con la base de datos
        EntityManager em = emf.createEntityManager();
        try {
            // Inicia una transacción para asegurar que las operaciones sean atómicas
            em.getTransaction().begin();
            
            // Si el autor no tiene un ID (es nuevo) o el autor no existe en la base de datos, se persiste
            if (autor.getId() == null || em.find(Autor.class, autor.getId()) == null) {
                em.persist(autor);  // Persistir autor nuevo
            } else {
                em.merge(autor);    // Si el autor ya existe, se actualiza
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

    // Método para obtener todos los autores registrados en la base de datos
    public List<Autor> obtenerTodos() {
        // Se crea un EntityManager para interactuar con la base de datos
        EntityManager em = emf.createEntityManager();
        
        // Se ejecuta la consulta JPQL para obtener todos los autores
        List<Autor> autores = em.createQuery("FROM Autor", Autor.class).getResultList();
        
        // Se cierra el EntityManager
        em.close();
        
        // Se devuelve la lista de autores
        return autores;
    }

    // Método para buscar un autor por su ID
    public Autor buscarPorId(Long id) {
        // Se crea un EntityManager para interactuar con la base de datos
        EntityManager em = emf.createEntityManager();
        
        // Se busca el autor en la base de datos por su ID
        Autor autor = em.find(Autor.class, id);
        
        // Se cierra el EntityManager
        em.close();
        
        // Se devuelve el autor encontrado (o null si no se encuentra)
        return autor;
    }

    // Método para eliminar un autor por su ID
    public void eliminar(Long id) {
        // Se crea un EntityManager para interactuar con la base de datos
        EntityManager em = emf.createEntityManager();
        
        // Se busca el autor en la base de datos por su ID
        Autor autor = em.find(Autor.class, id);
        
        // Si el autor existe, se procede con la eliminación
        if (autor != null) {
            // Inicia una transacción
            em.getTransaction().begin();
            
            // Elimina el autor de la base de datos
            em.remove(autor);
            
            // Confirma la transacción
            em.getTransaction().commit();
        }
        
        // Se cierra el EntityManager
        em.close();
    }
    
}
