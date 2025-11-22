package gui.paneles;

import gui.dialogos.DialogoDetalles;
import logica.GestorResumenes;
import modelo.Resumen;
import estructuras.listas.Lista;
import javax.swing.*;
import java.awt.*;

/**
 * Panel que permite buscar investigaciones por palabra clave.
 * Muestra una lista de resúmenes que contienen la palabra clave buscada.
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class PanelBuscarPalabra extends JPanel {
    
    /**
     * Gestor de resúmenes para realizar búsquedas.
     */
    private GestorResumenes gestor;
    
    /**
     * Campo de texto para ingresar la palabra clave.
     */
    private JTextField txtPalabraClave;
    
    /**
     * Lista de resultados de la búsqueda.
     */
    private JList<Resumen> listaResultados;
    
    /**
     * Modelo de lista para los resultados.
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
    public PanelBuscarPalabra(GestorResumenes gestor) {
        if (gestor == null) {
            throw new IllegalArgumentException("El gestor no puede ser null");
        }
        
        this.gestor = gestor;
        
        inicializarComponentes();
    }
    
    /**
     * Inicializa todos los componentes del panel.
     */
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panel superior con instrucciones y búsqueda
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        
        JLabel lblInstrucciones = new JLabel(
            "<html><b>Instrucciones:</b> Ingrese una palabra clave y haga clic en 'Buscar'<br>" +
            "para encontrar todas las investigaciones que contienen esa palabra clave.</html>"
        );
        panelSuperior.add(lblInstrucciones, BorderLayout.NORTH);
        
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel lblPalabra = new JLabel("Palabra Clave:");
        lblPalabra.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        txtPalabraClave = new JTextField(20);
        txtPalabraClave.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtPalabraClave.addActionListener(e -> buscar());
        
        btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.addActionListener(e -> buscar());
        btnBuscar.setToolTipText("Busca investigaciones que contienen la palabra clave");
        
        panelBusqueda.add(lblPalabra);
        panelBusqueda.add(txtPalabraClave);
        panelBusqueda.add(btnBuscar);
        
        panelSuperior.add(panelBusqueda, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central con lista de resultados
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        JLabel lblResultados = new JLabel("Resultados de la Búsqueda:");
        lblResultados.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        modeloLista = new DefaultListModel<>();
        listaResultados = new JList<>(modeloLista);
        listaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaResultados.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        listaResultados.addListSelectionListener(e -> {
            btnVerDetalles.setEnabled(listaResultados.getSelectedValue() != null);
        });
        
        // Permitir doble clic para ver detalles
        listaResultados.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    mostrarDetalles();
                }
            }
        });
        
        JScrollPane scrollResultados = new JScrollPane(listaResultados);
        scrollResultados.setBorder(BorderFactory.createTitledBorder("Investigaciones Encontradas"));
        
        panelCentral.add(lblResultados, BorderLayout.NORTH);
        panelCentral.add(scrollResultados, BorderLayout.CENTER);
        
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
     * Realiza la búsqueda de investigaciones por palabra clave.
     */
    private void buscar() {
        String palabra = txtPalabraClave.getText().trim();
        
        if (palabra.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor ingrese una palabra clave para buscar.",
                "Campo requerido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        Lista<Resumen> resultados = gestor.buscarPorPalabraClave(palabra);
        modeloLista.clear();
        
        if (resultados.estaVacia()) {
            JOptionPane.showMessageDialog(
                this,
                "No se encontraron investigaciones con la palabra clave: " + palabra,
                "Sin resultados",
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            for (int i = 0; i < resultados.tamaño(); i++) {
                modeloLista.addElement(resultados.obtener(i));
            }
        }
        
        btnVerDetalles.setEnabled(!resultados.estaVacia() && listaResultados.getSelectedValue() != null);
    }
    
    /**
     * Muestra los detalles completos del resumen seleccionado.
     */
    private void mostrarDetalles() {
        Resumen seleccionado = listaResultados.getSelectedValue();
        
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

