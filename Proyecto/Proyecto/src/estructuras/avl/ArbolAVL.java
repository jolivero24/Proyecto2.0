package estructuras.avl;

import estructuras.listas.Lista;
import estructuras.listas.ListaDinamica;

/**
 * Implementación propia de un árbol AVL (Adelson-Velsky y Landis) para
 * mantener elementos ordenados alfabéticamente. El árbol se balancea
 * automáticamente después de cada inserción para mantener complejidad O(log n).
 * 
 * Complejidad temporal:
 * - Inserción: O(log n)
 * - Búsqueda: O(log n)
 * - Recorrido in-orden: O(n)
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class ArbolAVL {
    
    /**
     * Raíz del árbol AVL.
     */
    private NodoAVL raiz;
    
    /**
     * Comparador de texto para ordenamiento alfabético en español.
     */
    private ComparadorTexto comparador;
    
    /**
     * Constructor que crea un nuevo árbol AVL vacío.
     */
    public ArbolAVL() {
        this.raiz = null;
        this.comparador = new ComparadorTexto();
    }
    
    /**
     * Inserta un nuevo elemento en el árbol. Si la clave ya existe,
     * actualiza el valor. El árbol se balancea automáticamente.
     * 
     * @param clave Clave del elemento (String)
     * @param valor Valor a almacenar (puede ser Autor o PalabraClave)
     * @throws IllegalArgumentException si la clave es null
     */
    public void insertar(String clave, Object valor) {
        if (clave == null) {
            throw new IllegalArgumentException("La clave no puede ser null");
        }
        raiz = insertarRecursivo(raiz, clave, valor);
    }
    
    /**
     * Método recursivo que inserta un nodo en el árbol y balancea si es necesario.
     * 
     * @param nodo Nodo actual en la recursión
     * @param clave Clave a insertar
     * @param valor Valor a almacenar
     * @return Nodo raíz del subárbol (posiblemente balanceado)
     */
    private NodoAVL insertarRecursivo(NodoAVL nodo, String clave, Object valor) {
        // Caso base: crear nuevo nodo
        if (nodo == null) {
            return new NodoAVL(clave, valor);
        }
        
        // Comparar claves
        int comparacion = comparador.comparar(clave, nodo.getClave());
        
        // Inserción BST estándar
        if (comparacion < 0) {
            nodo.setIzquierdo(insertarRecursivo(nodo.getIzquierdo(), clave, valor));
        } else if (comparacion > 0) {
            nodo.setDerecho(insertarRecursivo(nodo.getDerecho(), clave, valor));
        } else {
            // Clave duplicada: actualizar valor
            nodo.setValor(valor);
            return nodo;
        }
        
        // Actualizar altura del nodo actual
        nodo.setAltura(1 + Math.max(obtenerAltura(nodo.getIzquierdo()),
                                    obtenerAltura(nodo.getDerecho())));
        
        // Obtener factor de balance
        int balance = obtenerBalance(nodo);
        
        // Aplicar rotaciones según el caso
        
        // Caso LL (Rotación Simple Derecha)
        if (balance > 1 && comparador.comparar(clave, nodo.getIzquierdo().getClave()) < 0) {
            return rotacionDerecha(nodo);
        }
        
        // Caso RR (Rotación Simple Izquierda)
        if (balance < -1 && comparador.comparar(clave, nodo.getDerecho().getClave()) > 0) {
            return rotacionIzquierda(nodo);
        }
        
        // Caso LR (Rotación Doble: Izquierda-Derecha)
        if (balance > 1 && comparador.comparar(clave, nodo.getIzquierdo().getClave()) > 0) {
            nodo.setIzquierdo(rotacionIzquierda(nodo.getIzquierdo()));
            return rotacionDerecha(nodo);
        }
        
        // Caso RL (Rotación Doble: Derecha-Izquierda)
        if (balance < -1 && comparador.comparar(clave, nodo.getDerecho().getClave()) < 0) {
            nodo.setDerecho(rotacionDerecha(nodo.getDerecho()));
            return rotacionIzquierda(nodo);
        }
        
        return nodo; // Sin cambios necesarios
    }
    
    /**
     * Busca un elemento en el árbol por su clave.
     * 
     * @param clave Clave a buscar
     * @return Valor asociado a la clave, o null si no existe
     */
    public Object buscar(String clave) {
        if (clave == null) {
            return null;
        }
        return buscarRecursivo(raiz, clave);
    }
    
    /**
     * Método recursivo que busca un nodo en el árbol.
     * 
     * @param nodo Nodo actual en la recursión
     * @param clave Clave a buscar
     * @return Valor del nodo encontrado, o null
     */
    private Object buscarRecursivo(NodoAVL nodo, String clave) {
        if (nodo == null) {
            return null;
        }
        
        int comparacion = comparador.comparar(clave, nodo.getClave());
        
        if (comparacion < 0) {
            return buscarRecursivo(nodo.getIzquierdo(), clave);
        } else if (comparacion > 0) {
            return buscarRecursivo(nodo.getDerecho(), clave);
        } else {
            return nodo.getValor(); // Encontrado
        }
    }
    
    /**
     * Verifica si existe un elemento con la clave dada.
     * 
     * @param clave Clave a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existe(String clave) {
        return buscar(clave) != null;
    }
    
    /**
     * Realiza un recorrido in-orden del árbol, retornando los valores
     * en orden alfabético.
     * 
     * @return Lista de valores ordenados alfabéticamente
     */
    public Lista<Object> recorridoInOrden() {
        Lista<Object> resultado = new ListaDinamica<>();
        recorridoInOrdenRecursivo(raiz, resultado);
        return resultado;
    }
    
    /**
     * Método recursivo que realiza el recorrido in-orden.
     * 
     * @param nodo Nodo actual
     * @param resultado Lista donde se acumulan los resultados
     */
    private void recorridoInOrdenRecursivo(NodoAVL nodo, Lista<Object> resultado) {
        if (nodo != null) {
            // Recorrer subárbol izquierdo
            recorridoInOrdenRecursivo(nodo.getIzquierdo(), resultado);
            
            // Procesar nodo actual
            resultado.agregar(nodo.getValor());
            
            // Recorrer subárbol derecho
            recorridoInOrdenRecursivo(nodo.getDerecho(), resultado);
        }
    }
    
    /**
     * Obtiene una lista de todas las claves en orden alfabético.
     * 
     * @return Lista de claves ordenadas
     */
    public Lista<String> obtenerClaves() {
        Lista<String> resultado = new ListaDinamica<>();
        obtenerClavesRecursivo(raiz, resultado);
        return resultado;
    }
    
    /**
     * Método recursivo que obtiene todas las claves en orden.
     * 
     * @param nodo Nodo actual
     * @param resultado Lista donde se acumulan las claves
     */
    private void obtenerClavesRecursivo(NodoAVL nodo, Lista<String> resultado) {
        if (nodo != null) {
            obtenerClavesRecursivo(nodo.getIzquierdo(), resultado);
            resultado.agregar(nodo.getClave());
            obtenerClavesRecursivo(nodo.getDerecho(), resultado);
        }
    }
    
    /**
     * Obtiene la altura de un nodo (0 si es null).
     * 
     * @param nodo Nodo del cual obtener la altura
     * @return Altura del nodo, 0 si es null
     */
    private int obtenerAltura(NodoAVL nodo) {
        return (nodo == null) ? 0 : nodo.getAltura();
    }
    
    /**
     * Calcula el factor de balance de un nodo.
     * Balance = altura(izquierdo) - altura(derecho)
     * 
     * @param nodo Nodo del cual calcular el balance
     * @return Factor de balance (debe estar en {-1, 0, 1} para árbol balanceado)
     */
    private int obtenerBalance(NodoAVL nodo) {
        if (nodo == null) {
            return 0;
        }
        return obtenerAltura(nodo.getIzquierdo()) - obtenerAltura(nodo.getDerecho());
    }
    
    /**
     * Realiza una rotación simple a la derecha (caso LL).
     * 
     *        y                x
     *       / \              / \
     *      x   C    -->     A   y
     *     / \                  / \
     *    A   B                B   C
     * 
     * @param y Nodo desbalanceado
     * @return Nueva raíz del subárbol
     */
    private NodoAVL rotacionDerecha(NodoAVL y) {
        NodoAVL x = y.getIzquierdo();
        NodoAVL T2 = x.getDerecho();
        
        // Realizar rotación
        x.setDerecho(y);
        y.setIzquierdo(T2);
        
        // Actualizar alturas
        y.setAltura(Math.max(obtenerAltura(y.getIzquierdo()),
                             obtenerAltura(y.getDerecho())) + 1);
        x.setAltura(Math.max(obtenerAltura(x.getIzquierdo()),
                             obtenerAltura(x.getDerecho())) + 1);
        
        return x; // Nueva raíz
    }
    
    /**
     * Realiza una rotación simple a la izquierda (caso RR).
     * 
     *    x                  y
     *   / \                / \
     *  A   y      -->     x   C
     *     / \            / \
     *    B   C          A   B
     * 
     * @param x Nodo desbalanceado
     * @return Nueva raíz del subárbol
     */
    private NodoAVL rotacionIzquierda(NodoAVL x) {
        NodoAVL y = x.getDerecho();
        NodoAVL T2 = y.getIzquierdo();
        
        // Realizar rotación
        y.setIzquierdo(x);
        x.setDerecho(T2);
        
        // Actualizar alturas
        x.setAltura(Math.max(obtenerAltura(x.getIzquierdo()),
                             obtenerAltura(x.getDerecho())) + 1);
        y.setAltura(Math.max(obtenerAltura(y.getIzquierdo()),
                             obtenerAltura(y.getDerecho())) + 1);
        
        return y; // Nueva raíz
    }
    
    /**
     * Obtiene la altura total del árbol.
     * 
     * @return Altura del árbol
     */
    public int obtenerAlturaArbol() {
        return obtenerAltura(raiz);
    }
    
    /**
     * Verifica si el árbol está vacío.
     * 
     * @return true si está vacío, false en caso contrario
     */
    public boolean estaVacio() {
        return raiz == null;
    }
}

