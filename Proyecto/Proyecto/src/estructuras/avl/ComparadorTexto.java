package estructuras.avl;

import java.text.Collator;
import java.util.Locale;

/**
 * Comparador de texto que utiliza java.text.Collator para realizar
 * comparaciones alfabéticas correctas en español, considerando acentos
 * y caracteres especiales.
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class ComparadorTexto {
    
    /**
     * Collator configurado para español de España.
     * Maneja correctamente la comparación de caracteres con acentos.
     */
    private Collator collator;
    
    /**
     * Constructor que inicializa el Collator para español.
     */
    public ComparadorTexto() {
        collator = Collator.getInstance((Locale.of ("es", "ES")));
        // PRIMARY: Ignora diferencias de mayúsculas/minúsculas y acentos
        collator.setStrength(Collator.PRIMARY);
    }
    
    /**
     * Compara dos textos considerando las reglas del español.
     * 
     * @param texto1 Primer texto a comparar
     * @param texto2 Segundo texto a comparar
     * @return Valor negativo si texto1 < texto2, 0 si son iguales,
     *         valor positivo si texto1 > texto2
     */
    public int comparar(String texto1, String texto2) {
        if (texto1 == null && texto2 == null) {
            return 0;
        }
        if (texto1 == null) {
            return -1;
        }
        if (texto2 == null) {
            return 1;
        }
        return collator.compare(texto1, texto2);
    }
}

