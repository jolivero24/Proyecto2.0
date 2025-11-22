package estructuras.listas;

/**
 * Interfaz para comparar dos objetos.
 * Reemplaza java.util.Comparator para evitar dependencias
 * de librerías estándar de Java.
 * 
 * @param <T> Tipo de objetos a comparar
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public interface Comparador<T> {
    
    /**
     * Compara dos objetos.
     * 
     * @param o1 Primer objeto
     * @param o2 Segundo objeto
     * @return Valor negativo si o1 < o2, cero si o1 == o2,
     *         valor positivo si o1 > o2
     */
    int comparar(T o1, T o2);
}

