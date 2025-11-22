package estructuras.hashtable;

import modelo.Resumen;

/**
 * Nodo para la tabla de dispersión que utiliza encadenamiento separado.
 * Cada nodo contiene una clave (título), un valor (resumen) y una referencia
 * al siguiente nodo en la cadena de colisiones.
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class NodoHash {
    
    /**
     * Clave del nodo (título del resumen).
     */
    private String clave;
    
    /**
     * Valor almacenado (objeto Resumen).
     */
    private Resumen valor;
    
    /**
     * Referencia al siguiente nodo en la cadena de colisiones.
     */
    private NodoHash siguiente;
    
    /**
     * Constructor que crea un nuevo nodo.
     * 
     * @param clave Título del resumen (clave de hash)
     * @param valor Objeto Resumen a almacenar
     * @throws IllegalArgumentException si clave o valor son null
     */
    public NodoHash(String clave, Resumen valor) {
        if (clave == null || valor == null) {
            throw new IllegalArgumentException("Clave y valor no pueden ser null");
        }
        this.clave = clave;
        this.valor = valor;
        this.siguiente = null;
    }
    
    /**
     * Obtiene la clave del nodo.
     * 
     * @return Clave (título del resumen)
     */
    public String getClave() {
        return clave;
    }
    
    /**
     * Obtiene el valor almacenado.
     * 
     * @return Objeto Resumen
     */
    public Resumen getValor() {
        return valor;
    }
    
    /**
     * Obtiene el siguiente nodo en la cadena.
     * 
     * @return Siguiente nodo, o null si no existe
     */
    public NodoHash getSiguiente() {
        return siguiente;
    }
    
    /**
     * Establece el siguiente nodo en la cadena.
     * 
     * @param siguiente Nodo siguiente en la cadena de colisiones
     */
    public void setSiguiente(NodoHash siguiente) {
        this.siguiente = siguiente;
    }
}

