package gui.dialogos;

import javax.swing.*;

/**
 * Diálogo de confirmación reutilizable para diferentes propósitos.
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class DialogoConfirmacion {
    
    /**
     * Muestra un diálogo de confirmación y retorna la opción seleccionada.
     * 
     * @param parent Componente padre
     * @param mensaje Mensaje a mostrar
     * @param titulo Título del diálogo
     * @return JOptionPane.YES_OPTION, JOptionPane.NO_OPTION, o JOptionPane.CANCEL_OPTION
     */
    public static int mostrarConfirmacion(java.awt.Component parent, String mensaje, String titulo) {
        return JOptionPane.showConfirmDialog(
            parent,
            mensaje,
            titulo,
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
    }
    
    /**
     * Muestra un diálogo de confirmación simple (Sí/No).
     * 
     * @param parent Componente padre
     * @param mensaje Mensaje a mostrar
     * @param titulo Título del diálogo
     * @return true si se seleccionó "Sí", false en caso contrario
     */
    public static boolean mostrarConfirmacionSimple(java.awt.Component parent, String mensaje, String titulo) {
        int resultado = JOptionPane.showConfirmDialog(
            parent,
            mensaje,
            titulo,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        return resultado == JOptionPane.YES_OPTION;
    }
}

