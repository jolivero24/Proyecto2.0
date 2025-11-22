package modelo;

import java.io.Serializable;
import estructuras.listas.Lista;
import estructuras.listas.ListaDinamica;

/**
 * Clase que representa un autor de investigaciones científicas.
 * Mantiene una lista de todas las investigaciones asociadas al autor.
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class Autor implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Nombre completo del autor.
     */
    private String nombre;
    
    /**
     * Lista de investigaciones asociadas a este autor.
     */
    private Lista<Resumen> investigaciones;
    
    /**
     * Constructor que crea un nuevo autor.
     * 
     * @param nombre Nombre del autor (no puede ser null o vacío)
     * @throws IllegalArgumentException si el nombre es inválido
     */
    public Autor(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del autor no puede estar vacío");
        }
        this.nombre = nombre.trim();
        this.investigaciones = new ListaDinamica<>();
    }
    
    /**
     * Obtiene el nombre del autor.
     * 
     * @return Nombre del autor
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Agrega una investigación a la lista del autor.
     * 
     * @param resumen Resumen de la investigación a agregar
     * @throws IllegalArgumentException si el resumen es null
     */
    public void agregarInvestigacion(Resumen resumen) {
        if (resumen == null) {
            throw new IllegalArgumentException("El resumen no puede ser null");
        }
        if (!investigaciones.contiene(resumen)) {
            investigaciones.agregar(resumen);
        }
    }
    
    /**
     * Obtiene la lista de investigaciones del autor.
     * 
     * @return Lista de resúmenes asociados al autor
     */
    public Lista<Resumen> getInvestigaciones() {
        return new ListaDinamica<>(investigaciones);
    }
    
    /**
     * Representación en cadena del autor.
     * 
     * @return Nombre del autor
     */
    @Override
    public String toString() {
        return nombre;
    }
}

