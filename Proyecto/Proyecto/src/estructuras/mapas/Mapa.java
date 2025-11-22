package estructuras.mapas;

import estructuras.listas.Lista;

/**
 * Interfaz que define las operaciones básicas de un mapa (tabla hash).
 * Reemplaza la interfaz java.util.Map para evitar dependencias
 * de librerías estándar de Java.
 * 
 * @param <K> Tipo de las claves
 * @param <V> Tipo de los valores
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public interface Mapa<K, V> {
    
    /**
     * Inserta o actualiza un par clave-valor en el mapa.
     * 
     * @param clave Clave del elemento
     * @param valor Valor del elemento
     * @return Valor anterior asociado a la clave, o null si no existía
     */
    V insertar(K clave, V valor);
    
    /**
     * Obtiene el valor asociado a una clave.
     * 
     * @param clave Clave a buscar
     * @return Valor asociado, o null si no existe
     */
    V obtener(K clave);
    
    /**
     * Verifica si el mapa contiene una clave.
     * 
     * @param clave Clave a verificar
     * @return true si la clave existe
     */
    boolean contieneClave(K clave);
    
    /**
     * Elimina un elemento del mapa.
     * 
     * @param clave Clave del elemento a eliminar
     * @return Valor eliminado, o null si no existía
     */
    V eliminar(K clave);
    
    /**
     * Obtiene el número de elementos en el mapa.
     * 
     * @return Tamaño del mapa
     */
    int tamaño();
    
    /**
     * Verifica si el mapa está vacío.
     * 
     * @return true si el mapa no tiene elementos
     */
    boolean estaVacio();
    
    /**
     * Obtiene una lista con todas las entradas del mapa.
     * 
     * @return Lista de entradas
     */
    Lista<Entrada<K, V>> obtenerEntradas();
    
    /**
     * Interfaz para representar una entrada clave-valor en el mapa.
     * 
     * @param <K> Tipo de la clave
     * @param <V> Tipo del valor
     */
    interface Entrada<K, V> {
        /**
         * Obtiene la clave de la entrada.
         * 
         * @return Clave
         */
        K obtenerClave();
        
        /**
         * Obtiene el valor de la entrada.
         * 
         * @return Valor
         */
        V obtenerValor();
    }
}

