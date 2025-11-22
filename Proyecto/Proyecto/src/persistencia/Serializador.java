package persistencia;

import estructuras.hashtable.HashTable;
import estructuras.avl.ArbolAVL;
import modelo.Resumen;
import estructuras.listas.Lista;
import java.io.*;

/**
 * Clase que maneja la serialización y deserialización de las estructuras
 * de datos del sistema para persistencia entre sesiones.
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class Serializador {
    
    /**
     * Serializa una Hash Table a un archivo.
     * 
     * @param tabla Hash Table a serializar
     * @param rutaArchivo Ruta del archivo donde guardar
     * @return true si la serialización fue exitosa, false en caso contrario
     */
    public boolean serializarHashTable(HashTable tabla, String rutaArchivo) {
        if (tabla == null || rutaArchivo == null) {
            return false;
        }
        
        try {
            // Obtener todos los resúmenes
            Lista<Resumen> resumenes = tabla.listarTodos();
            
            // Crear directorio si no existe
            File archivo = new File(rutaArchivo);
            archivo.getParentFile().mkdirs();
            
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(archivo))) {
                oos.writeInt(resumenes.tamaño());
                for (int i = 0; i < resumenes.tamaño(); i++) {
                    oos.writeObject(resumenes.obtener(i));
                }
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error al serializar Hash Table: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Deserializa una Hash Table desde un archivo.
     * 
     * @param rutaArchivo Ruta del archivo a leer
     * @param tabla Hash Table donde cargar los datos
     * @return true si la deserialización fue exitosa, false en caso contrario
     */
    public boolean deserializarHashTable(String rutaArchivo, HashTable tabla) {
        if (rutaArchivo == null || tabla == null) {
            return false;
        }
        
        File archivo = new File(rutaArchivo);
        if (!archivo.exists() || !archivo.canRead()) {
            return false;
        }
        
        try {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(archivo))) {
                int cantidad = ois.readInt();
                
                for (int i = 0; i < cantidad; i++) {
                    Resumen resumen = (Resumen) ois.readObject();
                    tabla.insertar(resumen.getTitulo(), resumen);
                }
            }
            
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al deserializar Hash Table: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Serializa un árbol AVL a un archivo.
     * 
     * @param arbol Árbol AVL a serializar
     * @param rutaArchivo Ruta del archivo donde guardar
     * @return true si la serialización fue exitosa, false en caso contrario
     */
    public boolean serializarAVL(ArbolAVL arbol, String rutaArchivo) {
        if (arbol == null || rutaArchivo == null) {
            return false;
        }
        
        try {
            // Obtener todos los valores del árbol
            Lista<Object> valores = arbol.recorridoInOrden();
            Lista<String> claves = arbol.obtenerClaves();
            
            // Crear directorio si no existe
            File archivo = new File(rutaArchivo);
            archivo.getParentFile().mkdirs();
            
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(archivo))) {
                oos.writeInt(valores.tamaño());
                for (int i = 0; i < valores.tamaño(); i++) {
                    oos.writeObject(claves.obtener(i));
                    oos.writeObject(valores.obtener(i));
                }
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error al serializar AVL: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Deserializa un árbol AVL desde un archivo.
     * 
     * @param rutaArchivo Ruta del archivo a leer
     * @param arbol Árbol AVL donde cargar los datos
     * @return true si la deserialización fue exitosa, false en caso contrario
     */
    public boolean deserializarAVL(String rutaArchivo, ArbolAVL arbol) {
        if (rutaArchivo == null || arbol == null) {
            return false;
        }
        
        File archivo = new File(rutaArchivo);
        if (!archivo.exists() || !archivo.canRead()) {
            return false;
        }
        
        try {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(archivo))) {
                int cantidad = ois.readInt();
                
                for (int i = 0; i < cantidad; i++) {
                    String clave = (String) ois.readObject();
                    Object valor = ois.readObject();
                    arbol.insertar(clave, valor);
                }
            }
            
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al deserializar AVL: " + e.getMessage());
            return false;
        }
    }
}

