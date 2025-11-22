package estructuras.hashtable;

import modelo.Resumen;
import estructuras.listas.Lista;
import estructuras.listas.ListaDinamica;

/**
 * Implementación propia de una tabla de dispersión (Hash Table) para almacenar
 * resúmenes de investigaciones. Utiliza encadenamiento separado para manejar
 * colisiones y redimensionamiento automático cuando el factor de carga supera 0.75.
 * 
 * Complejidad temporal:
 * - Inserción: O(1) promedio
 * - Búsqueda: O(1) promedio
 * - Listar todos: O(n)
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class HashTable {
    
    /**
     * Factor de carga máximo antes de redimensionar (0.75 = 75%).
     */
    private static final double FACTOR_CARGA_MAXIMO = 0.75;
    
    /**
     * Capacidad inicial de la tabla (número primo).
     */
    private static final int CAPACIDAD_INICIAL = 101;
    
    /**
     * Arreglo de nodos que representa la tabla de dispersión.
     * Cada posición puede contener una cadena de nodos (encadenamiento).
     */
    private NodoHash[] tabla;
    
    /**
     * Capacidad actual de la tabla (tamaño del arreglo).
     */
    private int capacidad;
    
    /**
     * Número de elementos almacenados en la tabla.
     */
    private int tamaño;
    
    /**
     * Función hash utilizada para calcular índices.
     */
    private FuncionHash funcionHash;
    
    /**
     * Constructor que crea una nueva tabla de dispersión con capacidad inicial.
     */
    public HashTable() {
        this.capacidad = CAPACIDAD_INICIAL;
        this.tabla = new NodoHash[capacidad];
        this.tamaño = 0;
        this.funcionHash = new FuncionHash(capacidad);
    }
    
    /**
     * Inserta un resumen en la tabla usando el título como clave.
     * Si el título ya existe, no realiza la inserción.
     * 
     * @param titulo Título del resumen (clave)
     * @param resumen Objeto Resumen a almacenar
     * @return true si la inserción fue exitosa, false si la clave ya existe
     * @throws IllegalArgumentException si título o resumen son null
     */
    public boolean insertar(String titulo, Resumen resumen) {
        if (titulo == null || resumen == null) {
            throw new IllegalArgumentException("Título y resumen no pueden ser null");
        }
        
        // Verificar factor de carga y redimensionar si es necesario
        if ((double)tamaño / capacidad > FACTOR_CARGA_MAXIMO) {
            redimensionar();
        }
        
        int indice = funcionHash.calcularHash(titulo);
        
        // Verificar si ya existe el título
        NodoHash actual = tabla[indice];
        while (actual != null) {
            if (actual.getClave().equals(titulo)) {
                return false; // Ya existe
            }
            actual = actual.getSiguiente();
        }
        
        // Insertar al inicio de la lista (encadenamiento separado)
        NodoHash nuevoNodo = new NodoHash(titulo, resumen);
        nuevoNodo.setSiguiente(tabla[indice]);
        tabla[indice] = nuevoNodo;
        tamaño++;
        
        return true;
    }
    
    /**
     * Busca un resumen por su título.
     * 
     * @param titulo Título del resumen a buscar
     * @return Resumen encontrado, o null si no existe
     */
    public Resumen buscar(String titulo) {
        if (titulo == null) {
            return null;
        }
        
        int indice = funcionHash.calcularHash(titulo);
        NodoHash actual = tabla[indice];
        
        // Recorrer la cadena de colisiones
        while (actual != null) {
            if (actual.getClave().equals(titulo)) {
                return actual.getValor();
            }
            actual = actual.getSiguiente();
        }
        
        return null; // No encontrado
    }
    
    /**
     * Verifica si existe un resumen con el título dado.
     * 
     * @param titulo Título a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existe(String titulo) {
        return buscar(titulo) != null;
    }
    
    /**
     * Elimina un resumen de la tabla.
     * 
     * @param titulo Título del resumen a eliminar
     * @return true si se eliminó exitosamente, false si no existe
     */
    public boolean eliminar(String titulo) {
        if (titulo == null) {
            return false;
        }
        
        int indice = funcionHash.calcularHash(titulo);
        NodoHash actual = tabla[indice];
        NodoHash anterior = null;
        
        // Buscar el nodo a eliminar
        while (actual != null) {
            if (actual.getClave().equals(titulo)) {
                // Encontrado, eliminar de la cadena
                if (anterior == null) {
                    // Es el primer nodo
                    tabla[indice] = actual.getSiguiente();
                } else {
                    // Es un nodo intermedio
                    anterior.setSiguiente(actual.getSiguiente());
                }
                tamaño--;
                return true;
            }
            anterior = actual;
            actual = actual.getSiguiente();
        }
        
        return false; // No encontrado
    }
    
    /**
     * Retorna una lista con todos los resúmenes almacenados.
     * 
     * @return Lista de todos los resúmenes
     */
    public Lista<Resumen> listarTodos() {
        Lista<Resumen> resumenes = new ListaDinamica<>();
        
        for (int i = 0; i < capacidad; i++) {
            NodoHash actual = tabla[i];
            while (actual != null) {
                resumenes.agregar(actual.getValor());
                actual = actual.getSiguiente();
            }
        }
        
        return resumenes;
    }
    
    /**
     * Obtiene el número de elementos almacenados.
     * 
     * @return Tamaño actual de la tabla
     */
    public int getTamaño() {
        return tamaño;
    }
    
    /**
     * Obtiene la capacidad actual de la tabla.
     * 
     * @return Capacidad actual
     */
    public int getCapacidad() {
        return capacidad;
    }
    
    /**
     * Redimensiona la tabla cuando el factor de carga supera el máximo.
     * Crea una nueva tabla con capacidad prima mayor y reinserta todos los elementos.
     */
    private void redimensionar() {
        int nuevaCapacidad = FuncionHash.siguientePrimo(capacidad * 2);
        NodoHash[] tablaAnterior = tabla;
        int capacidadAnterior = capacidad;
        
        // Crear nueva tabla
        capacidad = nuevaCapacidad;
        tabla = new NodoHash[capacidad];
        tamaño = 0;
        funcionHash.setCapacidad(capacidad);
        
        // Reinsertar todos los elementos
        for (int i = 0; i < capacidadAnterior; i++) {
            NodoHash actual = tablaAnterior[i];
            while (actual != null) {
                insertar(actual.getClave(), actual.getValor());
                actual = actual.getSiguiente();
            }
        }
    }
}

