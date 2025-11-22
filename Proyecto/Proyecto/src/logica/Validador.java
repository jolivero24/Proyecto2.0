package logica;

import modelo.Resumen;
import java.io.File;
import estructuras.listas.Lista;

/**
 * Clase que proporciona métodos de validación para resúmenes y archivos.
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class Validador {
    
    /**
     * Longitud mínima permitida para un título.
     */
    private static final int LONGITUD_MINIMA_TITULO = 5;
    
    /**
     * Longitud máxima permitida para un título.
     */
    private static final int LONGITUD_MAXIMA_TITULO = 200;
    
    /**
     * Valida que un resumen tenga todos los campos requeridos y válidos.
     * 
     * @param resumen Resumen a validar
     * @return true si el resumen es válido, false en caso contrario
     */
    public boolean validarResumen(Resumen resumen) {
        if (resumen == null) {
            return false;
        }
        
        return validarTitulo(resumen.getTitulo()) &&
               validarAutores(resumen.getAutores()) &&
               validarCuerpo(resumen.getCuerpo());
    }
    
    /**
     * Valida que un título sea válido (no vacío, longitud adecuada).
     * 
     * @param titulo Título a validar
     * @return true si el título es válido, false en caso contrario
     */
    public boolean validarTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return false;
        }
        
        String tituloLimpio = titulo.trim();
        return tituloLimpio.length() >= LONGITUD_MINIMA_TITULO &&
               tituloLimpio.length() <= LONGITUD_MAXIMA_TITULO;
    }
    
    /**
     * Valida que haya al menos un autor.
     * 
     * @param autores Lista de autores a validar
     * @return true si hay al menos un autor válido, false en caso contrario
     */
    public boolean validarAutores(Lista<String> autores) {
        if (autores == null || autores.estaVacia()) {
            return false;
        }
        
        // Verificar que al menos un autor no esté vacío
        for (int i = 0; i < autores.tamaño(); i++) {
            String autor = autores.obtener(i);
            if (autor != null && !autor.trim().isEmpty()) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Valida que el cuerpo del resumen no esté vacío.
     * 
     * @param cuerpo Cuerpo del resumen a validar
     * @return true si el cuerpo es válido, false en caso contrario
     */
    public boolean validarCuerpo(String cuerpo) {
        return cuerpo != null && !cuerpo.trim().isEmpty();
    }
    
    /**
     * Valida que un archivo sea válido para lectura.
     * 
     * @param archivo Archivo a validar
     * @return true si el archivo es válido, false en caso contrario
     */
    public boolean validarArchivo(File archivo) {
        if (archivo == null) {
            return false;
        }
        
        // Verificar que existe
        if (!archivo.exists()) {
            return false;
        }
        
        // Verificar que es un archivo (no directorio)
        if (!archivo.isFile()) {
            return false;
        }
        
        // Verificar que se puede leer
        if (!archivo.canRead()) {
            return false;
        }
        
        // Verificar extensión .txt
        String nombre = archivo.getName().toLowerCase();
        return nombre.endsWith(".txt");
    }
}

