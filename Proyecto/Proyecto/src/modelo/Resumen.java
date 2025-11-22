package modelo;

import java.io.Serializable;
import estructuras.listas.Lista;
import estructuras.listas.ListaDinamica;

/**
 * Clase que representa un resumen de investigación científica.
 * Contiene información sobre el título, autores, cuerpo del resumen
 * y palabras clave asociadas.
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class Resumen implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Título de la investigación.
     * Es usado como clave en la tabla de dispersión.
     */
    private String titulo;
    
    /**
     * Lista de nombres de autores de la investigación.
     */
    private Lista<String> autores;
    
    /**
     * Cuerpo completo del resumen de la investigación.
     */
    private String cuerpo;
    
    /**
     * Lista de palabras clave asociadas a la investigación.
     */
    private Lista<String> palabrasClave;
    
    /**
     * Constructor completo de un resumen.
     * 
     * @param titulo Título de la investigación (no puede ser null o vacío)
     * @param autores Lista de nombres de autores (mínimo 1)
     * @param cuerpo Texto del resumen (no puede ser null o vacío)
     * @param palabrasClave Lista de palabras clave (puede estar vacía)
     * @throws IllegalArgumentException si algún parámetro requerido es inválido
     */
    public Resumen(String titulo, Lista<String> autores, 
                   String cuerpo, Lista<String> palabrasClave) {
        // Validaciones
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        if (autores == null || autores.estaVacia()) {
            throw new IllegalArgumentException("Debe haber al menos un autor");
        }
        
        this.titulo = titulo.trim();
        this.autores = new ListaDinamica<>(autores);
        this.cuerpo = cuerpo != null ? cuerpo.trim() : "";
        this.palabrasClave = palabrasClave != null ? 
                             new ListaDinamica<>(palabrasClave) : new ListaDinamica<>();
    }
    
    /**
     * Obtiene el título del resumen.
     * 
     * @return Título de la investigación
     */
    public String getTitulo() {
        return titulo;
    }
    
    /**
     * Obtiene la lista de autores.
     * 
     * @return Lista de nombres de autores
     */
    public Lista<String> getAutores() {
        return new ListaDinamica<>(autores);
    }
    
    /**
     * Obtiene el cuerpo del resumen.
     * 
     * @return Texto completo del resumen
     */
    public String getCuerpo() {
        return cuerpo;
    }
    
    /**
     * Obtiene la lista de palabras clave.
     * 
     * @return Lista de palabras clave
     */
    public Lista<String> getPalabrasClave() {
        return new ListaDinamica<>(palabrasClave);
    }
    
    /**
     * Representación en cadena del resumen.
     * 
     * @return Cadena con el título del resumen
     */
    @Override
    public String toString() {
        return titulo;
    }
}

