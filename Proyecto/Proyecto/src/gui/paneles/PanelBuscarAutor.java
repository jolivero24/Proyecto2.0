package gui.paneles;

import gui.dialogos.DialogoDetalles;
import logica.GestorResumenes;
import modelo.Autor;
import modelo.Resumen;
import estructuras.listas.Lista;
import javax.swing.*;
import java.awt.*;

/**
 * Panel que permite buscar investigaciones por autor.
 * Muestra un combo box con todos los autores y una lista de
 * investigaciones del autor seleccionado.
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class PanelBuscarAutor extends JPanel {
    
    /**
     * Gestor de resúmenes para realizar búsquedas.
     */
    private GestorResumenes gestor;
    
    /**
     * Combo box con la lista de autores.
     */
    private JComboBox<Autor> cmbAutores;
    
    /**
     * Modelo de combo box para los autores.
     */
    private DefaultComboBoxModel<Autor> modeloCombo;
    
    /**
     * Lista de investigaciones del autor seleccionado.
     */
    private JList<Resumen> listaInvestigaciones;
    
    /**
     * Modelo de lista para las investigaciones.
     */
    private DefaultListModel<Resumen> modeloLista;
    
    /**
     * Botón para realizar la búsqueda.
     */
    private JButton btnBuscar;
    
    /**
     * Botón para ver detalles del resumen seleccionado.
     */
    private JButton btnVerDetalles;
    
    /**
     * Constructor que inicializa el panel.
     * 
     * @param gestor Gestor de resúmenes
     */
    public PanelBuscarAutor(GestorResumenes gestor) {
        if (gestor == null) {
            throw new IllegalArgumentException("El gestor no puede ser null");
        }
        
        this.gestor = gestor;
        
        inicializarComponentes();
        cargarAutores();
    }
    
    /**
     * Inicializa todos los componentes del panel.
     */
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panel superior con instrucciones y selección de autor
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        
        JLabel lblInstrucciones = new JLabel(
            "<html><b>Instrucciones:</b> Seleccione un autor del combo box y haga clic en 'Buscar'<br>" +
            "para ver todas las investigaciones de ese autor.</html>"
        );
        panelSuperior.add(lblInstrucciones, BorderLayout.NORTH);
        
        // Panel de selección
        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel lblAutor = new JLabel("Autor:");
        lblAutor.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        modeloCombo = new DefaultComboBoxModel<>();
        cmbAutores = new JComboBox<>(modeloCombo);
        cmbAutores.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbAutores.setPreferredSize(new Dimension(300, 30));
        // Habilitar botón cuando se selecciona un autor
        cmbAutores.addActionListener(e -> {
            Autor seleccionado = (Autor) cmbAutores.getSelectedItem();
            btnBuscar.setEnabled(seleccionado != null && 
                                 !seleccionado.getNombre().contains("(No hay autores"));
        });
        
        btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.addActionListener(e -> buscarPorAutor());
        btnBuscar.setToolTipText("Busca investigaciones del autor seleccionado");
        btnBuscar.setEnabled(false); // Inicialmente deshabilitado
        
        panelSeleccion.add(lblAutor);
        panelSeleccion.add(cmbAutores);
        panelSeleccion.add(btnBuscar);
        
        panelSuperior.add(panelSeleccion, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central con lista de investigaciones
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        JLabel lblInvestigaciones = new JLabel("Investigaciones del Autor:");
        lblInvestigaciones.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        modeloLista = new DefaultListModel<>();
        listaInvestigaciones = new JList<>(modeloLista);
        listaInvestigaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaInvestigaciones.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        listaInvestigaciones.addListSelectionListener(e -> {
            btnVerDetalles.setEnabled(listaInvestigaciones.getSelectedValue() != null);
        });
        
        // Permitir doble clic para ver detalles
        listaInvestigaciones.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    mostrarDetalles();
                }
            }
        });
        
        JScrollPane scrollInvestigaciones = new JScrollPane(listaInvestigaciones);
        scrollInvestigaciones.setBorder(BorderFactory.createTitledBorder("Lista de Investigaciones"));
        
        panelCentral.add(lblInvestigaciones, BorderLayout.NORTH);
        panelCentral.add(scrollInvestigaciones, BorderLayout.CENTER);
        
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior con botón ver detalles
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnVerDetalles = new JButton("Ver Detalles");
        btnVerDetalles.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVerDetalles.setPreferredSize(new Dimension(200, 40));
        btnVerDetalles.setEnabled(false);
        btnVerDetalles.addActionListener(e -> mostrarDetalles());
        btnVerDetalles.setToolTipText("Muestra los detalles completos del resumen seleccionado");
        panelInferior.add(btnVerDetalles);
        
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    /**
     * Carga la lista de autores desde el gestor.
     */
    public void cargarAutores() {
        modeloCombo.removeAllElements();
        Lista<Autor> autores = gestor.listarAutores();
        
        for (int i = 0; i < autores.tamaño(); i++) {
            modeloCombo.addElement(autores.obtener(i));
        }
        
        if (autores.estaVacia()) {
            modeloCombo.addElement(new Autor("(No hay autores disponibles)"));
            btnBuscar.setEnabled(false);
        } else {
            // Habilitar botón si hay autores disponibles
            btnBuscar.setEnabled(true);
        }
    }
    
    /**
     * Busca las investigaciones del autor seleccionado.
     */
    private void buscarPorAutor() {
        Autor autorSeleccionado = (Autor) cmbAutores.getSelectedItem();
        
        if (autorSeleccionado == null) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor seleccione un autor.",
                "Selección requerida",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        Lista<Resumen> investigaciones = gestor.buscarPorAutor(autorSeleccionado.getNombre());
        modeloLista.clear();
        
        if (investigaciones.estaVacia()) {
            JOptionPane.showMessageDialog(
                this,
                "El autor '" + autorSeleccionado.getNombre() + "' no tiene investigaciones registradas.",
                "Sin resultados",
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            for (int i = 0; i < investigaciones.tamaño(); i++) {
                modeloLista.addElement(investigaciones.obtener(i));
            }
        }
        
        btnVerDetalles.setEnabled(!investigaciones.estaVacia() && listaInvestigaciones.getSelectedValue() != null);
    }
    
    /**
     * Muestra los detalles completos del resumen seleccionado.
     */
    private void mostrarDetalles() {
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
        
        DialogoDetalles dialogo = new DialogoDetalles(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            seleccionado
        );
        dialogo.setVisible(true);
    }
}

