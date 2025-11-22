package estructuras.hashtable;

/**
 * Clase que implementa una función hash personalizada basada en el
 * algoritmo DJB2 modificado. Esta función hash es utilizada para
 * distribuir los resúmenes en la tabla de dispersión.
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class FuncionHash {
    
    /**
     * Capacidad actual de la tabla de dispersión.
     */
    private int capacidad;
    
    /**
     * Constructor que inicializa la función hash con una capacidad.
     * 
     * @param capacidad Capacidad inicial de la tabla (debe ser un número primo)
     */
    public FuncionHash(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser positiva");
        }
        this.capacidad = capacidad;
    }
    
    /**
     * Establece una nueva capacidad para la función hash.
     * 
     * @param capacidad Nueva capacidad (debe ser un número primo)
     */
    public void setCapacidad(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser positiva");
        }
        this.capacidad = capacidad;
    }
    
    /**
     * Calcula el hash de un texto usando el algoritmo DJB2 modificado.
     * Primero normaliza el texto y luego aplica el algoritmo hash.
     * 
     * @param texto Cadena a hashear
     * @return Índice en la tabla (0 a capacidad-1)
     */
    public int calcularHash(String texto) {
        if (texto == null) {
            throw new IllegalArgumentException("El texto no puede ser null");
        }
        
        String normalizado = normalizarTexto(texto);
        long hash = 5381; // Valor inicial del algoritmo DJB2
        
        // Aplicar algoritmo DJB2: hash = hash * 33 + char
        for (int i = 0; i < normalizado.length(); i++) {
            hash = ((hash << 5) + hash) + normalizado.charAt(i);
            // Equivalente a: hash = hash * 33 + char
        }
        
        // Aplicar módulo y asegurar valor positivo
        return Math.abs((int)(hash % capacidad));
    }
    
    /**
     * Normaliza un texto eliminando acentos, convirtiendo a minúsculas
     * y eliminando espacios extra. Esto asegura una distribución uniforme
     * del hash independientemente de mayúsculas o acentos.
     * 
     * @param texto Texto a normalizar
     * @return Texto normalizado
     */
    private String normalizarTexto(String texto) {
        if (texto == null) {
            return "";
        }
        
        String normalizado = texto.toLowerCase();
        
        // Eliminar acentos
        normalizado = normalizado.replaceAll("[áàäâ]", "a");
        normalizado = normalizado.replaceAll("[éèëê]", "e");
        normalizado = normalizado.replaceAll("[íìïî]", "i");
        normalizado = normalizado.replaceAll("[óòöô]", "o");
        normalizado = normalizado.replaceAll("[úùüû]", "u");
        normalizado = normalizado.replaceAll("[ñ]", "n");
        
        // Eliminar espacios extra y trim
        normalizado = normalizado.replaceAll("\\s+", " ").trim();
        
        return normalizado;
    }
    
    /**
     * Encuentra el siguiente número primo mayor o igual a un número dado.
     * Utilizado para redimensionar la tabla manteniendo capacidad prima.
     * 
     * @param numero Número a partir del cual buscar el siguiente primo
     * @return Siguiente número primo
     */
    public static int siguientePrimo(int numero) {
        if (numero <= 2) {
            return 2;
        }
        
        int candidato = numero;
        if (candidato % 2 == 0) {
            candidato++; // Si es par, empezar con el siguiente impar
        }
        
        while (!esPrimo(candidato)) {
            candidato += 2; // Solo verificar impares
        }
        
        return candidato;
    }
    
    /**
     * Verifica si un número es primo.
     * 
     * @param numero Número a verificar
     * @return true si es primo, false en caso contrario
     */
    private static boolean esPrimo(int numero) {
        if (numero < 2) {
            return false;
        }
        if (numero == 2) {
            return true;
        }
        if (numero % 2 == 0) {
            return false;
        }
        
        // Verificar divisores hasta la raíz cuadrada
        for (int i = 3; i * i <= numero; i += 2) {
            if (numero % i == 0) {
                return false;
            }
        }
        
        return true;
    }
}

