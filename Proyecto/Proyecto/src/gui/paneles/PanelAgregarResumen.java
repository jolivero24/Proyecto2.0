package gui.paneles;

import logica.GestorResumenes;
import logica.Validador;
import persistencia.ManejadorArchivos;
import modelo.Resumen;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;


/**
 * Panel que permite agregar nuevos resúmenes al sistema mediante
 * la carga de archivos de texto usando JFileChooser.
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class PanelAgregarResumen extends JPanel {
    
    /**
     * Gestor de resúmenes para agregar nuevos resúmenes.
     */
    private GestorResumenes gestor;
    
    /**
     * Manejador de archivos para leer resúmenes.
     */
    private ManejadorArchivos manejadorArchivos;
    
    /**
     * Validador para verificar archivos y resúmenes.
     */
    private Validador validador;
    
    /**
     * Botón para cargar un archivo.
     */
    private JButton btnCargarArchivo;
    
    /**
     * Área de texto para mostrar vista previa del contenido.
     */
    private JTextArea txtVistaPrevia;
    
    /**
     * Botón para agregar el resumen al sistema.
     */
    private JButton btnAgregarResumen;
    
    /**
     * Archivo actualmente seleccionado.
     */
    private File archivoSeleccionado;
    
    /**
     * Resumen parseado del archivo seleccionado.
     */
    private Resumen resumenActual;
    
    /**
     * Referencia a la ventana principal para actualizar otros paneles.
     */
    private gui.VentanaPrincipal ventanaPrincipal;
    
    /**
     * Constructor que inicializa el panel.
     * 
     * @param gestor Gestor de resúmenes
     * @param ventanaPrincipal Ventana principal para actualizar otros paneles
     */
    public PanelAgregarResumen(GestorResumenes gestor, gui.VentanaPrincipal ventanaPrincipal) {
        if (gestor == null) {
            throw new IllegalArgumentException("El gestor no puede ser null");
        }
        
        this.gestor = gestor;
        this.ventanaPrincipal = ventanaPrincipal;
        this.manejadorArchivos = new ManejadorArchivos();
        this.validador = new Validador();
        
        inicializarComponentes();
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
            "<html><b>Instrucciones:</b> Seleccione un archivo .txt con el formato de resumen.<br>" +
            "El archivo debe contener: Título, Autores, Resumen y Palabras clave.</html>"
        );
        panelSuperior.add(lblInstrucciones, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central con botón y vista previa
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        
        // Botón cargar archivo
        btnCargarArchivo = new JButton("Cargar Archivo");
        btnCargarArchivo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCargarArchivo.setPreferredSize(new Dimension(200, 40));
        btnCargarArchivo.addActionListener(e -> cargarArchivo());
        btnCargarArchivo.setToolTipText("Haga clic para seleccionar un archivo de resumen");
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.add(btnCargarArchivo);
        
        // Botón agregar resumen (inicialmente deshabilitado)
        btnAgregarResumen = new JButton("Agregar Resumen al Sistema");
        btnAgregarResumen.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAgregarResumen.setPreferredSize(new Dimension(250, 40));
        btnAgregarResumen.setEnabled(false);
        btnAgregarResumen.addActionListener(e -> agregarResumen());
        panelBotones.add(btnAgregarResumen);
        
        panelCentral.add(panelBotones, BorderLayout.NORTH);
        
        // Área de texto para vista previa
        JLabel lblVistaPrevia = new JLabel("Vista Previa:");
        lblVistaPrevia.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        txtVistaPrevia = new JTextArea(15, 50);
        txtVistaPrevia.setEditable(false);
        txtVistaPrevia.setFont(new Font("Consolas", Font.PLAIN, 11));
        txtVistaPrevia.setBackground(new Color(248, 248, 248));
        JScrollPane scrollVistaPrevia = new JScrollPane(txtVistaPrevia);
        scrollVistaPrevia.setBorder(BorderFactory.createTitledBorder("Contenido del Archivo"));
        
        JPanel panelVistaPrevia = new JPanel(new BorderLayout(5, 5));
        panelVistaPrevia.add(lblVistaPrevia, BorderLayout.NORTH);
        panelVistaPrevia.add(scrollVistaPrevia, BorderLayout.CENTER);
        
        panelCentral.add(panelVistaPrevia, BorderLayout.CENTER);
        
        add(panelCentral, BorderLayout.CENTER);
    }
    
    /**
     * Abre un diálogo para seleccionar un archivo de resumen.
     */
    private void cargarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Archivo de Resumen");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto (*.txt)", "txt"));
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            archivoSeleccionado = fileChooser.getSelectedFile();
            
            if (!validador.validarArchivo(archivoSeleccionado)) {
                JOptionPane.showMessageDialog(
                    this,
                    "El archivo seleccionado no es válido.\n" +
                    "Asegúrese de que sea un archivo .txt y que se pueda leer.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            
            try {
                // Leer y parsear el archivo
                resumenActual = manejadorArchivos.leerResumen(archivoSeleccionado.getPath());
                
                // Validar el resumen
                if (!validador.validarResumen(resumenActual)) {
                    JOptionPane.showMessageDialog(
                        this,
                        "El resumen no es válido.\n" +
                        "Asegúrese de que contenga título, autores y cuerpo.",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE
                    );
                    resumenActual = null;
                    btnAgregarResumen.setEnabled(false);
                    txtVistaPrevia.setText("");
                    return;
                }
                
                // Mostrar vista previa
                mostrarVistaPrevia(resumenActual);
                btnAgregarResumen.setEnabled(true);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error al leer el archivo:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                resumenActual = null;
                btnAgregarResumen.setEnabled(false);
                txtVistaPrevia.setText("");
            }
        }
    }
    
    /**
     * Muestra una vista previa del resumen en el área de texto.
     * 
     * @param resumen Resumen a mostrar
     */
    private void mostrarVistaPrevia(Resumen resumen) {
        StringBuilder vista = new StringBuilder();
        vista.append("TÍTULO:\n");
        vista.append(resumen.getTitulo()).append("\n\n");
        
        vista.append("AUTORES:\n");
        for (int i = 0; i < resumen.getAutores().tamaño(); i++) {
            String autor = resumen.getAutores().obtener(i);
            vista.append("- ").append(autor).append("\n");
        }
        vista.append("\n");
        
        vista.append("RESUMEN:\n");
        String cuerpo = resumen.getCuerpo();
        if (cuerpo.length() > 500) {
            vista.append(cuerpo.substring(0, 500)).append("...\n");
        } else {
            vista.append(cuerpo).append("\n");
        }
        vista.append("\n");
        
        vista.append("PALABRAS CLAVE:\n");
        for (int i = 0; i < resumen.getPalabrasClave().tamaño(); i++) {
            String palabra = resumen.getPalabrasClave().obtener(i);
            vista.append("- ").append(palabra).append("\n");
        }
        
        txtVistaPrevia.setText(vista.toString());
    }
    
    /**
     * Agrega el resumen actual al sistema.
     */
    private void agregarResumen() {
        if (resumenActual == null) {
            JOptionPane.showMessageDialog(
                this,
                "No hay un resumen cargado para agregar.",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        // Intentar agregar directamente - el gestor ya verifica duplicados
        boolean agregado = gestor.agregarResumen(resumenActual);
        
        if (agregado) {
            JOptionPane.showMessageDialog(
                this,
                "Resumen agregado exitosamente.\n\n" +
                "Título: " + resumenActual.getTitulo() + "\n" +
                "Total de resúmenes en el sistema: " + gestor.getCantidadResumenes(),
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Actualizar todos los paneles
            if (ventanaPrincipal != null) {
                ventanaPrincipal.actualizarTodosLosPaneles();
            }
            
            // Limpiar vista
            resumenActual = null;
            archivoSeleccionado = null;
            txtVistaPrevia.setText("");
            btnAgregarResumen.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(
                this,
                "El resumen ya existe en el sistema.",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
}

