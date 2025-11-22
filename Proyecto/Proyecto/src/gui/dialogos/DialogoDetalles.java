package gui.dialogos;

import modelo.Resumen;
import estructuras.listas.Lista;
import javax.swing.*;
import java.awt.*;

/**
 * Diálogo que muestra los detalles completos de un resumen de investigación.
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class DialogoDetalles extends JDialog {
    
    /**
     * Constructor que crea un nuevo diálogo de detalles.
     * 
     * @param parent Ventana padre
     * @param resumen Resumen a mostrar
     */
    public DialogoDetalles(JFrame parent, Resumen resumen) {
        super(parent, "Detalles del Resumen", true);
        
        if (resumen == null) {
            throw new IllegalArgumentException("El resumen no puede ser null");
        }
        
        inicializarComponentes(resumen);
    }
    
    /**
     * Inicializa todos los componentes del diálogo.
     * 
     * @param resumen Resumen a mostrar
     */
    private void inicializarComponentes(Resumen resumen) {
        setLayout(new BorderLayout(10, 10));
        setSize(700, 600);
        setLocationRelativeTo(getParent());
        
        // Área de texto con los detalles
        JTextArea txtDetalles = new JTextArea();
        txtDetalles.setEditable(false);
        txtDetalles.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtDetalles.setBackground(new Color(248, 248, 248));
        
        // Formatear detalles
        StringBuilder detalles = new StringBuilder();
        detalles.append("═══════════════════════════════════════════════════════════\n");
        detalles.append("                    DETALLES DEL RESUMEN\n");
        detalles.append("═══════════════════════════════════════════════════════════\n\n");
        
        detalles.append("TÍTULO:\n");
        detalles.append(resumen.getTitulo()).append("\n\n");
        
        detalles.append("───────────────────────────────────────────────────────────\n\n");
        
        detalles.append("AUTORES:\n");
        Lista<String> autores = resumen.getAutores();
        for (int i = 0; i < autores.tamaño(); i++) {
            detalles.append("  • ").append(autores.obtener(i)).append("\n");
        }
        detalles.append("\n");
        
        detalles.append("───────────────────────────────────────────────────────────\n\n");
        
        detalles.append("RESUMEN:\n");
        detalles.append(resumen.getCuerpo()).append("\n\n");
        
        detalles.append("───────────────────────────────────────────────────────────\n\n");
        
        detalles.append("PALABRAS CLAVE:\n");
        Lista<String> palabrasClave = resumen.getPalabrasClave();
        if (palabrasClave.estaVacia()) {
            detalles.append("  (No hay palabras clave registradas)\n");
        } else {
            for (int i = 0; i < palabrasClave.tamaño(); i++) {
                detalles.append("  • ").append(palabrasClave.obtener(i)).append("\n");
            }
        }
        
        detalles.append("\n═══════════════════════════════════════════════════════════\n");
        
        txtDetalles.setText(detalles.toString());
        txtDetalles.setCaretPosition(0); // Scroll al inicio
        
        JScrollPane scrollPane = new JScrollPane(txtDetalles);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Botón cerrar
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnCerrar);
        
        add(panelBotones, BorderLayout.SOUTH);
    }
}

