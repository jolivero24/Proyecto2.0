package modelo;

import java.io.Serializable;
import estructuras.listas.Lista;
import estructuras.listas.ListaDinamica;

/**
 * Clase que representa una palabra clave en el sistema.
 * Mantiene información sobre la frecuencia total de aparición
 * y los resúmenes asociados.
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class PalabraClave implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Texto de la palabra clave.
     */
    private String texto;
    
    /**
     * Frecuencia total de aparición de la palabra clave en todos los resúmenes.
     */
    private int frecuenciaTotal;
    
    /**
     * Lista de resúmenes que contienen esta palabra clave.
     */
    private Lista<Resumen> resumenes;
    
    /**
     * Constructor que crea una nueva palabra clave.
     * 
     * @param texto Texto de la palabra clave (no puede ser null o vacío)
     * @throws IllegalArgumentException si el texto es inválido
     */
    public PalabraClave(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El texto de la palabra clave no puede estar vacío");
        }
        this.texto = texto.trim().toLowerCase();
        this.frecuenciaTotal = 0;
        this.resumenes = new ListaDinamica<>();
    }
    
    /**
     * Obtiene el texto de la palabra clave.
     * 
     * @return Texto de la palabra clave
     */
    public String getTexto() {
        return texto;
    }
    
    /**
     * Obtiene la frecuencia total de aparición.
     * 
     * @return Frecuencia total
     */
    public int getFrecuenciaTotal() {
        return frecuenciaTotal;
    }
    
    /**
     * Incrementa la frecuencia total en una cantidad específica.
     * 
     * @param cantidad Cantidad a incrementar (debe ser positiva)
     */
    public void incrementarFrecuencia(int cantidad) {
        if (cantidad > 0) {
            frecuenciaTotal += cantidad;
        }
    }
    
    /**
     * Agrega un resumen a la lista de resúmenes asociados.
     * 
     * @param resumen Resumen a agregar
     * @throws IllegalArgumentException si el resumen es null
     */
    public void agregarResumen(Resumen resumen) {
        if (resumen == null) {
            throw new IllegalArgumentException("El resumen no puede ser null");
        }
        if (!resumenes.contiene(resumen)) {
            resumenes.agregar(resumen);
        }
    }
    
    /**
     * Obtiene la lista de resúmenes asociados.
     * 
     * @return Lista de resúmenes que contienen esta palabra clave
     */
    public Lista<Resumen> getResumenes() {
        return new ListaDinamica<>(resumenes);
    }
    
    /**
     * Representación en cadena de la palabra clave.
     * 
     * @return Texto de la palabra clave
     */
    @Override
    public String toString() {
        return texto;
    }
}

