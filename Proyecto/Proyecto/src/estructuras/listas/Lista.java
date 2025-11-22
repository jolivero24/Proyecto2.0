package estructuras.listas;

/**
 * Interfaz que define las operaciones básicas de una lista.
 * Reemplaza la interfaz java.util.List para evitar dependencias
 * de librerías estándar de Java.
 * 
 * @param <T> Tipo de elementos almacenados en la lista
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public interface Lista<T> {
    
    /**
     * Agrega un elemento al final de la lista.
     * 
     * @param elemento Elemento a agregar
     * @return true si se agregó exitosamente
     */
    boolean agregar(T elemento);
    
    /**
     * Obtiene el elemento en la posición especificada.
     * 
     * @param indice Índice del elemento (0-based)
     * @return Elemento en la posición indicada
     * @throws IndexOutOfBoundsException si el índice es inválido
     */
    T obtener(int indice);
    
    /**
     * Obtiene el número de elementos en la lista.
     * 
     * @return Tamaño de la lista
     */
    int tamaño();
    
    /**
     * Verifica si la lista está vacía.
     * 
     * @return true si la lista no tiene elementos
     */
    boolean estaVacia();
    
    /**
     * Verifica si la lista contiene el elemento especificado.
     * 
     * @param elemento Elemento a buscar
     * @return true si el elemento está en la lista
     */
    boolean contiene(T elemento);
    
    /**
     * Elimina el elemento especificado de la lista.
     * 
     * @param elemento Elemento a eliminar
     * @return true si se eliminó exitosamente
     */
    boolean eliminar(T elemento);
    
    /**
     * Elimina el elemento en la posición especificada.
     * 
     * @param indice Índice del elemento a eliminar
     * @return Elemento eliminado
     * @throws IndexOutOfBoundsException si el índice es inválido
     */
    T eliminar(int indice);
    
    /**
     * Limpia todos los elementos de la lista.
     */
    void limpiar();
    
    /**
     * Convierte la lista a un arreglo.
     * 
     * @return Arreglo con los elementos de la lista
     */
    Object[] aArreglo();
}

