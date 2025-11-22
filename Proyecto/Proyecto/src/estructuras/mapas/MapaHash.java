package estructuras.mapas;

import estructuras.listas.Lista;
import estructuras.listas.ListaDinamica;
import java.io.Serializable;

/**
 * Implementación manual de un mapa hash que reemplaza HashMap.
 * Utiliza encadenamiento separado para manejar colisiones.
 * 
 * Complejidad temporal:
 * - Inserción: O(1) promedio
 * - Búsqueda: O(1) promedio
 * - Eliminación: O(1) promedio
 * 
 * @param <K> Tipo de las claves
 * @param <V> Tipo de los valores
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class MapaHash<K, V> implements Mapa<K, V>, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Factor de carga máximo antes de redimensionar (0.75 = 75%).
     */
    private static final double FACTOR_CARGA_MAXIMO = 0.75;
    
    /**
     * Capacidad inicial de la tabla (número primo).
     */
    private static final int CAPACIDAD_INICIAL = 16;
    
    /**
     * Clase interna para representar un nodo en la cadena de colisiones.
     */
    private static class NodoMapa<K, V> implements Serializable {
        private static final long serialVersionUID = 1L;
        K clave;
        V valor;
        NodoMapa<K, V> siguiente;
        
        NodoMapa(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
            this.siguiente = null;
        }
    }
    
    /**
     * Clase interna para representar una entrada del mapa.
     */
    private static class EntradaMapa<K, V> implements Mapa.Entrada<K, V>, Serializable {
        private static final long serialVersionUID = 1L;
        private K clave;
        private V valor;
        
        EntradaMapa(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
        }
        
        @Override
        public K obtenerClave() {
            return clave;
        }
        
        @Override
        public V obtenerValor() {
            return valor;
        }
    }
    
    /**
     * Arreglo de nodos que representa la tabla hash.
     * Usa Object[] para evitar problemas con genéricos en arrays.
     */
    private Object[] tabla;
    
    /**
     * Capacidad actual de la tabla.
     */
    private int capacidad;
    
    /**
     * Número de elementos almacenados.
     */
    private int tamaño;
    
    /**
     * Constructor que crea un mapa vacío.
     */
    public MapaHash() {
        this.capacidad = CAPACIDAD_INICIAL;
        this.tabla = new Object[capacidad];
        this.tamaño = 0;
    }
    
    /**
     * Obtiene el nodo en el índice especificado con el tipo correcto.
     * 
     * @param indice Índice en la tabla
     * @return Nodo en esa posición, o null
     */
    @SuppressWarnings("unchecked")
    private NodoMapa<K, V> obtenerNodo(int indice) {
        return (NodoMapa<K, V>) tabla[indice];
    }
    
    /**
     * Establece el nodo en el índice especificado.
     * 
     * @param indice Índice en la tabla
     * @param nodo Nodo a establecer
     */
    private void establecerNodo(int indice, NodoMapa<K, V> nodo) {
        tabla[indice] = nodo;
    }
    
    @Override
    public V insertar(K clave, V valor) {
        if (clave == null) {
            throw new IllegalArgumentException("La clave no puede ser null");
        }
        
        // Verificar factor de carga y redimensionar si es necesario
        if ((double)tamaño / capacidad > FACTOR_CARGA_MAXIMO) {
            redimensionar();
        }
        
        int indice = calcularIndice(clave);
        NodoMapa<K, V> actual = obtenerNodo(indice);
        
        // Buscar si la clave ya existe
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                V valorAnterior = actual.valor;
                actual.valor = valor;
                return valorAnterior;
            }
            actual = actual.siguiente;
        }
        
        // Insertar nuevo nodo al inicio de la cadena
        NodoMapa<K, V> nuevoNodo = new NodoMapa<>(clave, valor);
        nuevoNodo.siguiente = obtenerNodo(indice);
        establecerNodo(indice, nuevoNodo);
        tamaño++;
        
        return null;
    }
    
    @Override
    public V obtener(K clave) {
        if (clave == null) {
            return null;
        }
        
        int indice = calcularIndice(clave);
        NodoMapa<K, V> actual = obtenerNodo(indice);
        
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                return actual.valor;
            }
            actual = actual.siguiente;
        }
        
        return null;
    }
    
    @Override
    public boolean contieneClave(K clave) {
        return obtener(clave) != null;
    }
    
    @Override
    public V eliminar(K clave) {
        if (clave == null) {
            return null;
        }
        
        int indice = calcularIndice(clave);
        NodoMapa<K, V> actual = obtenerNodo(indice);
        NodoMapa<K, V> anterior = null;
        
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                V valorEliminado = actual.valor;
                
                if (anterior == null) {
                    establecerNodo(indice, actual.siguiente);
                } else {
                    anterior.siguiente = actual.siguiente;
                }
                
                tamaño--;
                return valorEliminado;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        
        return null;
    }
    
    @Override
    public int tamaño() {
        return tamaño;
    }
    
    @Override
    public boolean estaVacio() {
        return tamaño == 0;
    }
    
    @Override
    public Lista<Mapa.Entrada<K, V>> obtenerEntradas() {
        Lista<Mapa.Entrada<K, V>> entradas = new ListaDinamica<>();
        
        for (int i = 0; i < capacidad; i++) {
            NodoMapa<K, V> actual = obtenerNodo(i);
            while (actual != null) {
                entradas.agregar(new EntradaMapa<>(actual.clave, actual.valor));
                actual = actual.siguiente;
            }
        }
        
        return entradas;
    }
    
    /**
     * Calcula el índice en la tabla hash para una clave.
     * 
     * @param clave Clave para calcular el índice
     * @return Índice en la tabla
     */
    private int calcularIndice(K clave) {
        int hashCode = clave.hashCode();
        return Math.abs(hashCode) % capacidad;
    }
    
    /**
     * Redimensiona la tabla hash cuando el factor de carga supera el máximo.
     * Duplica la capacidad y reinserta todos los elementos.
     */
    private void redimensionar() {
        int capacidadAnterior = capacidad;
        Object[] tablaAnterior = tabla;
        
        capacidad *= 2;
        tabla = new Object[capacidad];
        tamaño = 0;
        
        // Reinsertar todos los elementos
        for (int i = 0; i < capacidadAnterior; i++) {
            NodoMapa<K, V> actual = obtenerNodoDeArray(tablaAnterior, i);
            while (actual != null) {
                insertar(actual.clave, actual.valor);
                actual = actual.siguiente;
            }
        }
    }
    
    /**
     * Obtiene un nodo de un array Object[] con el tipo correcto.
     * 
     * @param array Array del cual obtener el nodo
     * @param indice Índice en el array
     * @return Nodo en esa posición, o null
     */
    @SuppressWarnings("unchecked")
    private NodoMapa<K, V> obtenerNodoDeArray(Object[] array, int indice) {
        return (NodoMapa<K, V>) array[indice];
    }
}

