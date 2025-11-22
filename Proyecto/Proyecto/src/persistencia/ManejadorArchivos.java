package persistencia;

import modelo.Resumen;
import logica.Validador;
import estructuras.listas.Lista;
import estructuras.listas.ListaDinamica;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Clase que maneja la lectura y escritura de archivos de resúmenes.
 * Proporciona funcionalidades para leer archivos .txt y parsearlos
 * en objetos Resumen.
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class ManejadorArchivos {
    
    /**
     * Validador para verificar archivos y resúmenes.
     */
    private Validador validador;
    
    /**
     * Constructor que inicializa el manejador de archivos.
     */
    public ManejadorArchivos() {
        this.validador = new Validador();
    }
    
    /**
     * Lee un archivo de resumen y lo parsea en un objeto Resumen.
     * 
     * @param ruta Ruta del archivo a leer
     * @return Resumen parseado del archivo
     * @throws IOException si hay error al leer el archivo
     * @throws IllegalArgumentException si el archivo no es válido
     */
    public Resumen leerResumen(String ruta) throws IOException {
        if (ruta == null || ruta.trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta no puede estar vacía");
        }
        
        File archivo = new File(ruta);
        
        if (!validador.validarArchivo(archivo)) {
            throw new IllegalArgumentException("El archivo no es válido o no se puede leer");
        }
        
        StringBuilder contenido = new StringBuilder();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
        }
        
        return parsearResumen(contenido.toString());
    }
    
    /**
     * Parsea el contenido de un archivo de texto en un objeto Resumen.
     * El formato esperado es:
     * - Título: Primera línea o línea que comienza con #
     * - Autores: Sección marcada con "Autores:" o "Autor:"
     * - Resumen: Sección marcada con "Resumen:"
     * - Palabras clave: Sección marcada con "Palabras claves:" o "Palabras clave:"
     * 
     * @param contenido Contenido completo del archivo
     * @return Resumen parseado
     * @throws IllegalArgumentException si el contenido no es válido
     */
    public Resumen parsearResumen(String contenido) {
        if (contenido == null || contenido.trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido no puede estar vacío");
        }
        
        String[] lineas = contenido.split("\n");
        
        String titulo = "";
        Lista<String> autores = new ListaDinamica<>();
        StringBuilder cuerpo = new StringBuilder();
        Lista<String> palabrasClave = new ListaDinamica<>();
        
        String seccionActual = "";
        boolean enSeccion = false;
        
        for (String linea : lineas) {
            linea = linea.trim();
            
            if (linea.isEmpty()) {
                continue;
            }
            
            // Detectar título (primera línea no vacía o línea con #)
            if (titulo.isEmpty() && !linea.startsWith("Autores") && 
                !linea.startsWith("Autor") && !linea.startsWith("Resumen") &&
                !linea.startsWith("Palabras")) {
                if (linea.startsWith("#")) {
                    titulo = linea.replaceAll("#", "").trim();
                } else if (seccionActual.isEmpty()) {
                    titulo = linea;
                }
            }
            
            // Detectar secciones
            String lineaLower = linea.toLowerCase();
            if (lineaLower.contains("autores") || lineaLower.contains("autor:")) {
                seccionActual = "AUTORES";
                enSeccion = true;
                continue;
            } else if (lineaLower.contains("resumen")) {
                seccionActual = "RESUMEN";
                enSeccion = true;
                continue;
            } else if (lineaLower.contains("palabras claves") || 
                       lineaLower.contains("palabras clave") ||
                       lineaLower.contains("palabras clave:")) {
                seccionActual = "PALABRAS";
                enSeccion = true;
                continue;
            }
            
            // Procesar según sección
            if (enSeccion) {
                switch (seccionActual) {
                    case "AUTORES":
                        if (!linea.startsWith("#") && !linea.startsWith(":")) {
                            // Puede haber múltiples autores separados por comas o líneas
                            String[] autoresArray = linea.split(",");
                            for (String autor : autoresArray) {
                                autor = autor.trim();
                                if (!autor.isEmpty()) {
                                    autores.agregar(autor);
                                }
                            }
                        }
                        break;
                        
                    case "RESUMEN":
                        if (!linea.startsWith("#")) {
                            cuerpo.append(linea).append(" ");
                        }
                        break;
                        
                    case "PALABRAS":
                        // Eliminar marcadores como ** o :
                        String palabrasLinea = linea.replaceAll("\\*+", "")
                                                      .replaceAll(":", "")
                                                      .trim();
                        if (!palabrasLinea.isEmpty()) {
                            String[] palabras = palabrasLinea.split(",");
                            for (String palabra : palabras) {
                                palabra = palabra.trim();
                                if (!palabra.isEmpty()) {
                                    palabrasClave.agregar(palabra);
                                }
                            }
                        }
                        break;
                }
            }
        }
        
        // Si no se encontró título en formato especial, usar primera línea
        if (titulo.isEmpty() && lineas.length > 0) {
            titulo = lineas[0].trim();
        }
        
        // Validar que se obtuvieron datos mínimos
        if (titulo.isEmpty()) {
            throw new IllegalArgumentException("No se pudo extraer el título del archivo");
        }
        
        if (autores.estaVacia()) {
            throw new IllegalArgumentException("No se encontraron autores en el archivo");
        }
        
        // Crear y retornar resumen
        return new Resumen(titulo, autores, cuerpo.toString().trim(), palabrasClave);
    }
}

