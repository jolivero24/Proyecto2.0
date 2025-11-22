package gui.paneles;

import logica.GestorResumenes;
import modelo.PalabraClave;
import modelo.Resumen;
import estructuras.listas.Lista;
import javax.swing.*;
import java.awt.*;

/**
 * Panel que muestra todas las palabras clave ordenadas alfabéticamente
 * y permite ver detalles sobre cada palabra (frecuencia total, artículos asociados).
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class PanelListarPalabras extends JPanel {
    
    /**
     * Gestor de resúmenes para obtener datos.
     */
    private GestorResumenes gestor;
    
    /**
     * Lista de palabras clave.
     */
    private JList<PalabraClave> listaPalabras;
    
    /**
     * Modelo de lista para las palabras clave.
     */
    private DefaultListModel<PalabraClave> modeloLista;
    
    /**
     * Área de texto para mostrar detalles de la palabra seleccionada.
     */
    private JTextArea txtDetalles;
    
    /**
     * Constructor que inicializa el panel.
     * 
     * @param gestor Gestor de resúmenes
     */
    public PanelListarPalabras(GestorResumenes gestor) {
        if (gestor == null) {
            throw new IllegalArgumentException("El gestor no puede ser null");
        }
        
        this.gestor = gestor;
        
        inicializarComponentes();
        cargarPalabras();
    }
    
    /**
     * Inicializa todos los componentes del panel.
     */
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panel superior con instrucciones
        JPanel panelSuperior = new JPanel(new BorderLayout());
        JLabel lblInstrucciones = new JLabel(
            "<html><b>Instrucciones:</b> Seleccione una palabra clave de la lista para ver<br>" +
            "sus detalles: frecuencia total y artículos asociados.</html>"
        );
        panelSuperior.add(lblInstrucciones, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central con lista y detalles
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 10));
        
        // Panel izquierdo: Lista de palabras clave
        JPanel panelLista = new JPanel(new BorderLayout(5, 5));
        JLabel lblLista = new JLabel("Palabras Clave (Ordenadas Alfabéticamente):");
        lblLista.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        modeloLista = new DefaultListModel<>();
        listaPalabras = new JList<>(modeloLista);
        listaPalabras.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPalabras.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        listaPalabras.addListSelectionListener(e -> mostrarDetalles());
        JScrollPane scrollLista = new JScrollPane(listaPalabras);
        scrollLista.setBorder(BorderFactory.createTitledBorder("Lista de Palabras Clave"));
        
        panelLista.add(lblLista, BorderLayout.NORTH);
        panelLista.add(scrollLista, BorderLayout.CENTER);
        
        // Panel derecho: Detalles
        JPanel panelDetalles = new JPanel(new BorderLayout(5, 5));
        JLabel lblDetalles = new JLabel("Detalles de la Palabra Clave:");
        lblDetalles.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        txtDetalles = new JTextArea(20, 40);
        txtDetalles.setEditable(false);
        txtDetalles.setFont(new Font("Consolas", Font.PLAIN, 11));
        txtDetalles.setBackground(new Color(248, 248, 248));
        JScrollPane scrollDetalles = new JScrollPane(txtDetalles);
        scrollDetalles.setBorder(BorderFactory.createTitledBorder("Información Detallada"));
        
        panelDetalles.add(lblDetalles, BorderLayout.NORTH);
        panelDetalles.add(scrollDetalles, BorderLayout.CENTER);
        
        panelCentral.add(panelLista);
        panelCentral.add(panelDetalles);
        
        add(panelCentral, BorderLayout.CENTER);
    }
    
    /**
     * Carga la lista de palabras clave desde el gestor.
     */
    public void cargarPalabras() {
        modeloLista.clear();
        Lista<PalabraClave> palabras = gestor.listarPalabrasClave();
        
        for (int i = 0; i < palabras.tamaño(); i++) {
            modeloLista.addElement(palabras.obtener(i));
        }
        
        if (palabras.estaVacia()) {
            txtDetalles.setText("No hay palabras clave registradas en el sistema.\n" +
                               "Agregue resúmenes desde la pestaña 'Agregar Resumen'.");
        }
    }
    
    /**
     * Muestra los detalles de la palabra clave seleccionada.
     */
    private void mostrarDetalles() {
        PalabraClave seleccionada = listaPalabras.getSelectedValue();
        
        if (seleccionada == null) {
            txtDetalles.setText("");
            return;
        }
        
        StringBuilder detalles = new StringBuilder();
        detalles.append("PALABRA CLAVE: ").append(seleccionada.getTexto()).append("\n\n");
        
        Lista<Resumen> resumenes = seleccionada.getResumenes();
        detalles.append("ARTÍCULOS ASOCIADOS: ").append(resumenes.tamaño()).append("\n");
        detalles.append("─────────────────────────────────────────\n");
        
        if (resumenes.estaVacia()) {
            detalles.append("No hay artículos asociados a esta palabra clave.\n");
        } else {
            int contador = 1;
            for (int i = 0; i < resumenes.tamaño(); i++) {
                Resumen resumen = resumenes.obtener(i);
                detalles.append(String.format("%d. %s\n", contador++, resumen.getTitulo()));
            }
        }
        
        detalles.append("\n");
        detalles.append("FRECUENCIA TOTAL: ").append(seleccionada.getFrecuenciaTotal())
                .append(" apariciones en el cuerpo de los resúmenes\n");
        detalles.append("─────────────────────────────────────────\n");
        
        if (seleccionada.getFrecuenciaTotal() == 0 && !resumenes.estaVacia()) {
            detalles.append("\nNota: Esta palabra clave está registrada en los artículos\n");
            detalles.append("pero no aparece en el cuerpo de los textos. Esto puede\n");
            detalles.append("ocurrir cuando la palabra clave es un concepto general\n");
            detalles.append("que no se menciona explícitamente en el resumen.\n");
        }
        
        txtDetalles.setText(detalles.toString());
    }
}

