package estructuras.listas;

import java.io.Serializable;

/**
 * Implementación manual de una lista dinámica que reemplaza ArrayList.
 * Utiliza un arreglo interno que se redimensiona automáticamente
 * cuando es necesario.
 * 
 * Complejidad temporal:
 * - Agregar: O(1) amortizado
 * - Obtener: O(1)
 * - Eliminar: O(n)
 * - Contiene: O(n)
 * 
 * @param <T> Tipo de elementos almacenados en la lista
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class ListaDinamica<T> implements Lista<T>, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Capacidad inicial del arreglo interno.
     */
    private static final int CAPACIDAD_INICIAL = 10;
    
    /**
     * Factor de crecimiento del arreglo (se duplica cuando se llena).
     */
    private static final int FACTOR_CRECIMIENTO = 2;
    
    /**
     * Arreglo interno que almacena los elementos.
     */
    private Object[] elementos;
    
    /**
     * Número de elementos almacenados actualmente.
     */
    private int tamaño;
    
    /**
     * Constructor que crea una lista vacía.
     */
    public ListaDinamica() {
        this.elementos = new Object[CAPACIDAD_INICIAL];
        this.tamaño = 0;
    }
    
    /**
     * Constructor que crea una lista copiando los elementos de otra lista.
     * 
     * @param otraLista Lista de la cual copiar elementos
     */
    public ListaDinamica(Lista<T> otraLista) {
        if (otraLista == null) {
            this.elementos = new Object[CAPACIDAD_INICIAL];
            this.tamaño = 0;
        } else {
            this.elementos = new Object[Math.max(otraLista.tamaño(), CAPACIDAD_INICIAL)];
            this.tamaño = 0;
            for (int i = 0; i < otraLista.tamaño(); i++) {
                this.agregar(otraLista.obtener(i));
            }
        }
    }
    
    @Override
    public boolean agregar(T elemento) {
        if (tamaño >= elementos.length) {
            redimensionar();
        }
        elementos[tamaño] = elemento;
        tamaño++;
        return true;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamaño) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
        return (T) elementos[indice];
    }
    
    @Override
    public int tamaño() {
        return tamaño;
    }
    
    @Override
    public boolean estaVacia() {
        return tamaño == 0;
    }
    
    @Override
    public boolean contiene(T elemento) {
        for (int i = 0; i < tamaño; i++) {
            if (elementos[i] == null && elemento == null) {
                return true;
            }
            if (elementos[i] != null && elementos[i].equals(elemento)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean eliminar(T elemento) {
        for (int i = 0; i < tamaño; i++) {
            if (elementos[i] == null && elemento == null) {
                eliminar(i);
                return true;
            }
            if (elementos[i] != null && elementos[i].equals(elemento)) {
                eliminar(i);
                return true;
            }
        }
        return false;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public T eliminar(int indice) {
        if (indice < 0 || indice >= tamaño) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
        
        T elementoEliminado = (T) elementos[indice];
        
        // Desplazar elementos hacia la izquierda
        for (int i = indice; i < tamaño - 1; i++) {
            elementos[i] = elementos[i + 1];
        }
        
        elementos[tamaño - 1] = null; // Limpiar referencia
        tamaño--;
        
        return elementoEliminado;
    }
    
    @Override
    public void limpiar() {
        for (int i = 0; i < tamaño; i++) {
            elementos[i] = null;
        }
        tamaño = 0;
    }
    
    @Override
    public Object[] aArreglo() {
        Object[] arreglo = new Object[tamaño];
        for (int i = 0; i < tamaño; i++) {
            arreglo[i] = elementos[i];
        }
        return arreglo;
    }
    
    /**
     * Redimensiona el arreglo interno cuando se llena.
     * Duplica la capacidad actual.
     */
    private void redimensionar() {
        Object[] nuevoArreglo = new Object[elementos.length * FACTOR_CRECIMIENTO];
        for (int i = 0; i < tamaño; i++) {
            nuevoArreglo[i] = elementos[i];
        }
        elementos = nuevoArreglo;
    }
    
    /**
     * Ordena la lista usando un algoritmo de ordenamiento por inserción.
     * Requiere que los elementos implementen Comparable o se proporcione un comparador.
     * 
     * @param comparador Comparador para ordenar los elementos
     */
    public void ordenar(Comparador<T> comparador) {
        if (comparador == null || tamaño <= 1) {
            return;
        }
        
        // Ordenamiento por inserción
        for (int i = 1; i < tamaño; i++) {
            T clave = obtener(i);
            int j = i - 1;
            
            while (j >= 0 && comparador.comparar(obtener(j), clave) > 0) {
                elementos[j + 1] = elementos[j];
                j--;
            }
            elementos[j + 1] = clave;
        }
    }
    
    /**
     * Representación en cadena de la lista.
     * 
     * @return Cadena con los elementos de la lista
     */
    @Override
    public String toString() {
        if (estaVacia()) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < tamaño; i++) {
            sb.append(elementos[i]);
            if (i < tamaño - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}

