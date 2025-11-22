package gui;

import gui.paneles.*;
import logica.GestorResumenes;
import persistencia.ManejadorArchivos;
import modelo.Resumen;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Ventana principal de la aplicación SuperMetroMendeley.
 * Contiene un menú superior y un panel con pestañas para las diferentes
 * funcionalidades del sistema.
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class VentanaPrincipal extends JFrame {
    
    /**
     * Gestor de resúmenes que maneja toda la lógica de negocio.
     */
    private GestorResumenes gestor;
    
    /**
     * Panel principal con pestañas para las diferentes funcionalidades.
     */
    private JTabbedPane tabbedPane;
    
    /**
     * Manejador de archivos para cargar datos iniciales.
     */
    private ManejadorArchivos manejadorArchivos;
    
    /**
     * Referencias a los paneles para poder actualizarlos.
     */
    private PanelAgregarResumen panelAgregarResumen;
    private PanelAnalizar panelAnalizar;
    private PanelBuscarPalabra panelBuscarPalabra;
    private PanelBuscarAutor panelBuscarAutor;
    private PanelListarPalabras panelListarPalabras;
    
    /**
     * Constructor que inicializa la ventana principal.
     */
    public VentanaPrincipal() {
        gestor = new GestorResumenes();
        manejadorArchivos = new ManejadorArchivos();
        
        inicializarComponentes();
        cargarDatosIniciales();
        configurarCierre();
    }
    
    /**
     * Inicializa todos los componentes de la ventana.
     */
    private void inicializarComponentes() {
        setTitle("SuperMetroMendeley - Sistema de Gestión de Investigaciones");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Configurar menú
        configurarMenu();
        
        // Crear panel con pestañas
        tabbedPane = new JTabbedPane();
        
        // Crear paneles con referencia a esta ventana para actualización
        panelAgregarResumen = new PanelAgregarResumen(gestor, this);
        panelAnalizar = new PanelAnalizar(gestor);
        panelBuscarPalabra = new PanelBuscarPalabra(gestor);
        panelBuscarAutor = new PanelBuscarAutor(gestor);
        panelListarPalabras = new PanelListarPalabras(gestor);
        
        tabbedPane.addTab("Agregar Resumen", panelAgregarResumen);
        tabbedPane.addTab("Analizar", panelAnalizar);
        tabbedPane.addTab("Buscar por Palabra", panelBuscarPalabra);
        tabbedPane.addTab("Buscar por Autor", panelBuscarAutor);
        tabbedPane.addTab("Palabras Clave", panelListarPalabras);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    /**
     * Configura el menú superior de la ventana.
     */
    private void configurarMenu() {
        JMenuBar menuBar = new JMenuBar();
        
        // Menú Archivo
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> salirDelSistema());
        menuArchivo.add(itemSalir);
        
        // Menú Ayuda
        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemAcercaDe = new JMenuItem("Acerca de");
        itemAcercaDe.addActionListener(e -> mostrarAcercaDe());
        menuAyuda.add(itemAcercaDe);
        
        menuBar.add(menuArchivo);
        menuBar.add(menuAyuda);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * Maneja el cierre de la ventana con confirmación.
     */
    private void configurarCierre() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salirDelSistema();
            }
        });
    }
    
    /**
     * Muestra un diálogo de confirmación antes de salir.
     */
    private void salirDelSistema() {
        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Desea salir del sistema?",
            "Confirmar salida",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    /**
     * Muestra información acerca de la aplicación.
     */
    private void mostrarAcercaDe() {
        String mensaje = "SuperMetroMendeley\n\n" +
                        "Sistema de gestión de artículos científicos\n" +
                        "Desarrollado en Java con Hash Table y Árbol AVL\n\n" +
                        "Versión 1.0\n\n" +
                        "© 2024 Equipo SuperMetroMendeley";
        
        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Acerca de",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Carga los datos iniciales desde archivos de resúmenes.
     */
    private void cargarDatosIniciales() {
        File carpetaIniciales = new File("recursos/resumenes_iniciales");
        
        if (!carpetaIniciales.exists() || !carpetaIniciales.isDirectory()) {
            // Crear carpeta si no existe
            carpetaIniciales.mkdirs();
            return;
        }
        
        File[] archivos = carpetaIniciales.listFiles((dir, name) -> 
            name.toLowerCase().endsWith(".txt")
        );
        
        if (archivos == null || archivos.length == 0) {
            return;
        }
        
        int cargados = 0;
        for (File archivo : archivos) {
            try {
                Resumen resumen = manejadorArchivos.leerResumen(archivo.getPath());
                gestor.agregarResumen(resumen);
                cargados++;
            } catch (Exception e) {
                // Ignorar errores al cargar archivos iniciales
                System.err.println("Error al cargar " + archivo.getName() + ": " + e.getMessage());
            }
        }
        
        if (cargados > 0) {
            // Actualizar paneles
            actualizarTodosLosPaneles();
        }
    }
    
    /**
     * Actualiza todos los paneles después de agregar un resumen.
     */
    public void actualizarTodosLosPaneles() {
        SwingUtilities.invokeLater(() -> {
            if (panelAnalizar != null) {
                panelAnalizar.cargarLista();
            }
            if (panelBuscarAutor != null) {
                panelBuscarAutor.cargarAutores();
            }
            if (panelListarPalabras != null) {
                panelListarPalabras.cargarPalabras();
            }
            tabbedPane.revalidate();
            tabbedPane.repaint();
        });
    }
}

