package main;

import gui.VentanaPrincipal;
import javax.swing.SwingUtilities;

/**
 * Clase principal que inicia la aplicación SuperMetroMendeley.
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class Main {
    
    /**
     * Método principal que inicia la aplicación.
     * 
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}

