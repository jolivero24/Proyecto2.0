package estructuras.avl;

/**
 * Nodo para el árbol AVL.
 * Cada nodo contiene una clave (String), un valor (Object), referencias
 * a los nodos izquierdo y derecho, y la altura del subárbol.
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class NodoAVL {
    
    /**
     * Clave del nodo (String para ordenamiento).
     */
    private String clave;
    
    /**
     * Valor almacenado en el nodo (puede ser Autor o PalabraClave).
     */
    private Object valor;
    
    /**
     * Referencia al nodo hijo izquierdo.
     */
    private NodoAVL izquierdo;
    
    /**
     * Referencia al nodo hijo derecho.
     */
    private NodoAVL derecho;
    
    /**
     * Altura del subárbol con raíz en este nodo.
     */
    private int altura;
    
    /**
     * Constructor que crea un nuevo nodo AVL.
     * 
     * @param clave Clave del nodo (no puede ser null)
     * @param valor Valor a almacenar
     * @throws IllegalArgumentException si la clave es null
     */
    public NodoAVL(String clave, Object valor) {
        if (clave == null) {
            throw new IllegalArgumentException("La clave no puede ser null");
        }
        this.clave = clave;
        this.valor = valor;
        this.izquierdo = null;
        this.derecho = null;
        this.altura = 1; // Nuevo nodo es hoja, altura = 1
    }
    
    /**
     * Obtiene la clave del nodo.
     * 
     * @return Clave del nodo
     */
    public String getClave() {
        return clave;
    }
    
    /**
     * Obtiene el valor almacenado.
     * 
     * @return Valor del nodo
     */
    public Object getValor() {
        return valor;
    }
    
    /**
     * Establece el valor del nodo.
     * 
     * @param valor Nuevo valor a almacenar
     */
    public void setValor(Object valor) {
        this.valor = valor;
    }
    
    /**
     * Obtiene el nodo hijo izquierdo.
     * 
     * @return Nodo izquierdo, o null si no existe
     */
    public NodoAVL getIzquierdo() {
        return izquierdo;
    }
    
    /**
     * Establece el nodo hijo izquierdo.
     * 
     * @param izquierdo Nuevo nodo izquierdo
     */
    public void setIzquierdo(NodoAVL izquierdo) {
        this.izquierdo = izquierdo;
    }
    
    /**
     * Obtiene el nodo hijo derecho.
     * 
     * @return Nodo derecho, o null si no existe
     */
    public NodoAVL getDerecho() {
        return derecho;
    }
    
    /**
     * Establece el nodo hijo derecho.
     * 
     * @param derecho Nuevo nodo derecho
     */
    public void setDerecho(NodoAVL derecho) {
        this.derecho = derecho;
    }
    
    /**
     * Obtiene la altura del subárbol.
     * 
     * @return Altura del nodo
     */
    public int getAltura() {
        return altura;
    }
    
    /**
     * Establece la altura del subárbol.
     * 
     * @param altura Nueva altura
     */
    public void setAltura(int altura) {
        this.altura = altura;
    }
}

