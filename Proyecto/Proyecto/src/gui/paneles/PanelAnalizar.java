package gui.paneles;

import logica.GestorResumenes;
import modelo.Resumen;
import estructuras.listas.Lista;
import estructuras.mapas.Mapa;
import estructuras.mapas.Mapa.Entrada;
import javax.swing.*;
import java.awt.*;

/**
 * Panel que permite analizar un resumen seleccionado y mostrar
 * las frecuencias de aparición de cada palabra clave en el cuerpo del resumen.
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class PanelAnalizar extends JPanel {
    
    /**
     * Gestor de resúmenes para obtener datos y realizar análisis.
     */
    private GestorResumenes gestor;
    
    /**
     * Lista de investigaciones disponibles.
     */
    private JList<Resumen> listaInvestigaciones;
    
    /**
     * Modelo de lista para las investigaciones.
     */
    private DefaultListModel<Resumen> modeloLista;
    
    /**
     * Área de texto para mostrar resultados del análisis.
     */
    private JTextArea txtResultados;
    
    /**
     * Botón para realizar el análisis.
     */
    private JButton btnAnalizar;
    
    /**
     * Constructor que inicializa el panel.
     * 
     * @param gestor Gestor de resúmenes
     */
    public PanelAnalizar(GestorResumenes gestor) {
        if (gestor == null) {
            throw new IllegalArgumentException("El gestor no puede ser null");
        }
        
        this.gestor = gestor;
        
        inicializarComponentes();
        cargarLista();
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
            "<html><b>Instrucciones:</b> Seleccione una investigación de la lista y haga clic en 'Analizar'<br>" +
            "para ver las frecuencias de aparición de cada palabra clave en el resumen.</html>"
        );
        panelSuperior.add(lblInstrucciones, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central con lista y resultados
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 10));
        
        // Panel izquierdo: Lista de investigaciones
        JPanel panelLista = new JPanel(new BorderLayout(5, 5));
        JLabel lblLista = new JLabel("Investigaciones Disponibles:");
        lblLista.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        modeloLista = new DefaultListModel<>();
        listaInvestigaciones = new JList<>(modeloLista);
        listaInvestigaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaInvestigaciones.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        JScrollPane scrollLista = new JScrollPane(listaInvestigaciones);
        scrollLista.setBorder(BorderFactory.createTitledBorder("Lista de Investigaciones"));
        
        panelLista.add(lblLista, BorderLayout.NORTH);
        panelLista.add(scrollLista, BorderLayout.CENTER);
        
        // Panel derecho: Resultados
        JPanel panelResultados = new JPanel(new BorderLayout(5, 5));
        JLabel lblResultados = new JLabel("Resultados del Análisis:");
        lblResultados.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        txtResultados = new JTextArea(20, 40);
        txtResultados.setEditable(false);
        txtResultados.setFont(new Font("Consolas", Font.PLAIN, 11));
        txtResultados.setBackground(new Color(248, 248, 248));
        JScrollPane scrollResultados = new JScrollPane(txtResultados);
        scrollResultados.setBorder(BorderFactory.createTitledBorder("Frecuencias de Palabras Clave"));
        
        panelResultados.add(lblResultados, BorderLayout.NORTH);
        panelResultados.add(scrollResultados, BorderLayout.CENTER);
        
        panelCentral.add(panelLista);
        panelCentral.add(panelResultados);
        
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior con botón
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAnalizar = new JButton("Analizar Resumen");
        btnAnalizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAnalizar.setPreferredSize(new Dimension(200, 40));
        btnAnalizar.addActionListener(e -> analizarSeleccion());
        btnAnalizar.setToolTipText("Analiza el resumen seleccionado y muestra las frecuencias");
        panelInferior.add(btnAnalizar);
        
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    /**
     * Carga la lista de investigaciones desde el gestor.
     */
    public void cargarLista() {
        modeloLista.clear();
        Lista<Resumen> resumenes = gestor.listarResumenes();
        
        for (int i = 0; i < resumenes.tamaño(); i++) {
            modeloLista.addElement(resumenes.obtener(i));
        }
        
        if (resumenes.estaVacia()) {
            txtResultados.setText("No hay investigaciones disponibles en el sistema.\n" +
                                 "Agregue resúmenes desde la pestaña 'Agregar Resumen'.");
        }
    }
    
    /**
     * Analiza el resumen seleccionado y muestra los resultados.
     */
    private void analizarSeleccion() {
        Resumen seleccionado = listaInvestigaciones.getSelectedValue();
        
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor seleccione una investigación de la lista.",
                "Selección requerida",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        // Realizar análisis
        Mapa<String, Integer> frecuencias = gestor.analizarResumen(seleccionado.getTitulo());
        
        // Formatear resultados
        StringBuilder resultado = new StringBuilder();
        resultado.append("TÍTULO: ").append(seleccionado.getTitulo()).append("\n\n");
        
        resultado.append("AUTORES:\n");
        Lista<String> autores = seleccionado.getAutores();
        for (int i = 0; i < autores.tamaño(); i++) {
            resultado.append("- ").append(autores.obtener(i)).append("\n");
        }
        resultado.append("\n");
        
        resultado.append("FRECUENCIAS DE PALABRAS CLAVE:\n");
        resultado.append("─────────────────────────────────────────\n");
        
        if (frecuencias.estaVacio()) {
            resultado.append("No hay palabras clave registradas en el sistema.\n");
        } else {
            boolean hayApariciones = false;
            Lista<Entrada<String, Integer>> entradas = frecuencias.obtenerEntradas();
            for (int i = 0; i < entradas.tamaño(); i++) {
                Entrada<String, Integer> entrada = entradas.obtener(i);
                int frecuencia = entrada.obtenerValor();
                if (frecuencia > 0) {
                    hayApariciones = true;
                    resultado.append(String.format("%-30s: %3d apariciones\n", 
                                                 entrada.obtenerClave(), frecuencia));
                }
            }
            
            if (!hayApariciones) {
                resultado.append("Ninguna palabra clave aparece en el cuerpo del resumen.\n");
            }
        }
        
        txtResultados.setText(resultado.toString());
    }
}

