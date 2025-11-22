package logica;

import estructuras.listas.Lista;
import estructuras.listas.ListaDinamica;

/**
 * Clase que proporciona funcionalidades para analizar y procesar texto.
 * Utilizada para contar frecuencias de palabras clave en el cuerpo de resúmenes.
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class AnalizadorTexto {
    
    /**
     * Cuenta cuántas veces aparece una palabra clave en un texto.
     * La búsqueda es case-insensitive y busca la palabra como subcadena.
     * 
     * @param texto Texto donde buscar
     * @param palabraClave Palabra clave a buscar
     * @return Número de apariciones de la palabra clave
     */
    public int contarFrecuencia(String texto, String palabraClave) {
        if (texto == null || palabraClave == null || texto.isEmpty() || palabraClave.isEmpty()) {
            return 0;
        }
        
        String textoLimpio = limpiarTexto(texto);
        String palabraLimpia = palabraClave.toLowerCase().trim();
        
        int contador = 0;
        int indice = 0;
        
        // Buscar todas las ocurrencias de la palabra
        while ((indice = textoLimpio.indexOf(palabraLimpia, indice)) != -1) {
            contador++;
            indice += palabraLimpia.length();
        }
        
        return contador;
    }
    
    /**
     * Limpia y normaliza un texto eliminando caracteres especiales,
     * normalizando espacios y convirtiendo a minúsculas.
     * 
     * @param texto Texto a limpiar
     * @return Texto limpio y normalizado
     */
    public String limpiarTexto(String texto) {
        if (texto == null) {
            return "";
        }
        
        // Convertir a minúsculas
        String limpio = texto.toLowerCase();
        
        // Normalizar espacios (múltiples espacios a uno solo)
        limpio = limpio.replaceAll("\\s+", " ");
        
        // Eliminar caracteres especiales al inicio/final de palabras
        // pero mantener guiones dentro de palabras
        limpio = limpio.trim();
        
        return limpio;
    }
    
    /**
     * Divide un texto en tokens (palabras) individuales.
     * 
     * @param texto Texto a tokenizar
     * @return Lista de palabras (tokens)
     */
    public Lista<String> tokenizar(String texto) {
        Lista<String> tokens = new ListaDinamica<>();
        
        if (texto == null || texto.isEmpty()) {
            return tokens;
        }
        
        String textoLimpio = limpiarTexto(texto);
        
        // Dividir por espacios y caracteres de puntuación
        String[] palabras = textoLimpio.split("[\\s\\p{Punct}]+");
        
        for (String palabra : palabras) {
            palabra = palabra.trim();
            if (!palabra.isEmpty()) {
                tokens.agregar(palabra);
            }
        }
        
        return tokens;
    }
    
    /**
     * Normaliza una palabra eliminando acentos y caracteres especiales.
     * 
     * @param palabra Palabra a normalizar
     * @return Palabra normalizada
     */
    public String normalizarPalabra(String palabra) {
        if (palabra == null) {
            return "";
        }
        
        String normalizada = palabra.toLowerCase();
        
        // Eliminar acentos
        normalizada = normalizada.replaceAll("[áàäâ]", "a");
        normalizada = normalizada.replaceAll("[éèëê]", "e");
        normalizada = normalizada.replaceAll("[íìïî]", "i");
        normalizada = normalizada.replaceAll("[óòöô]", "o");
        normalizada = normalizada.replaceAll("[úùüû]", "u");
        normalizada = normalizada.replaceAll("[ñ]", "n");
        
        return normalizada.trim();
    }
}

