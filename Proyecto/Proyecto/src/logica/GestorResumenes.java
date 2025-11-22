package logica;

import modelo.Autor;
import modelo.PalabraClave;
import modelo.Resumen;
import estructuras.hashtable.HashTable;
import estructuras.avl.ArbolAVL;
import estructuras.listas.Lista;
import estructuras.listas.ListaDinamica;
import estructuras.mapas.Mapa;
import estructuras.mapas.MapaHash;

/**
 * Clase principal que gestiona todas las operaciones sobre los resúmenes.
 * Integra la Hash Table para almacenamiento rápido y dos árboles AVL
 * para mantener palabras clave y autores ordenados alfabéticamente.
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class GestorResumenes {
    
    /**
     * Tabla de dispersión que almacena todos los resúmenes por título.
     */
    private HashTable tablaResumenes;
    
    /**
     * Árbol AVL que mantiene las palabras clave ordenadas alfabéticamente.
     */
    private ArbolAVL arbolPalabras;
    
    /**
     * Árbol AVL que mantiene los autores ordenados alfabéticamente.
     */
    private ArbolAVL arbolAutores;
    
    /**
     * Analizador de texto para contar frecuencias de palabras clave.
     */
    private AnalizadorTexto analizador;
    
    /**
     * Constructor que inicializa todas las estructuras de datos.
     */
    public GestorResumenes() {
        this.tablaResumenes = new HashTable();
        this.arbolPalabras = new ArbolAVL();
        this.arbolAutores = new ArbolAVL();
        this.analizador = new AnalizadorTexto();
    }
    
    /**
     * Agrega un nuevo resumen al sistema. Inserta en la hash table y
     * actualiza los árboles AVL de palabras clave y autores.
     * 
     * @param resumen Resumen a agregar
     * @return true si se agregó exitosamente, false si ya existe
     * @throws IllegalArgumentException si el resumen es null
     */
    public boolean agregarResumen(Resumen resumen) {
        if (resumen == null) {
            throw new IllegalArgumentException("El resumen no puede ser null");
        }
        
        // Verificar si ya existe
        if (tablaResumenes.existe(resumen.getTitulo())) {
            return false;
        }
        
        // Insertar en hash table
        boolean insertado = tablaResumenes.insertar(resumen.getTitulo(), resumen);
        
        if (insertado) {
            // Actualizar árboles AVL
            actualizarPalabrasClave(resumen);
            actualizarAutores(resumen);
        }
        
        return insertado;
    }
    
    /**
     * Busca un resumen por su título.
     * 
     * @param titulo Título del resumen a buscar
     * @return Resumen encontrado, o null si no existe
     */
    public Resumen buscarPorTitulo(String titulo) {
        if (titulo == null) {
            return null;
        }
        return tablaResumenes.buscar(titulo);
    }
    
    /**
     * Busca todos los resúmenes que contienen una palabra clave específica.
     * 
     * @param palabra Palabra clave a buscar
     * @return Lista de resúmenes que contienen la palabra clave
     */
    public Lista<Resumen> buscarPorPalabraClave(String palabra) {
        if (palabra == null || palabra.trim().isEmpty()) {
            return new ListaDinamica<>();
        }
        
        String palabraNormalizada = palabra.trim().toLowerCase();
        Object resultado = arbolPalabras.buscar(palabraNormalizada);
        
        if (resultado instanceof PalabraClave) {
            PalabraClave palabraClave = (PalabraClave) resultado;
            return palabraClave.getResumenes();
        }
        
        return new ListaDinamica<>();
    }
    
    /**
     * Busca todos los resúmenes de un autor específico.
     * 
     * @param nombreAutor Nombre del autor a buscar
     * @return Lista de resúmenes del autor
     */
    public Lista<Resumen> buscarPorAutor(String nombreAutor) {
        if (nombreAutor == null || nombreAutor.trim().isEmpty()) {
            return new ListaDinamica<>();
        }
        
        String autorNormalizado = nombreAutor.trim();
        Object resultado = arbolAutores.buscar(autorNormalizado);
        
        if (resultado instanceof Autor) {
            Autor autor = (Autor) resultado;
            return autor.getInvestigaciones();
        }
        
        return new ListaDinamica<>();
    }
    
    /**
     * Obtiene una lista de todas las palabras clave ordenadas alfabéticamente.
     * 
     * @return Lista de objetos PalabraClave ordenados
     */
    public Lista<PalabraClave> listarPalabrasClave() {
        Lista<Object> objetos = arbolPalabras.recorridoInOrden();
        Lista<PalabraClave> palabras = new ListaDinamica<>();
        
        for (int i = 0; i < objetos.tamaño(); i++) {
            Object obj = objetos.obtener(i);
            if (obj instanceof PalabraClave) {
                palabras.agregar((PalabraClave) obj);
            }
        }
        
        return palabras;
    }
    
    /**
     * Obtiene una lista de todos los autores ordenados alfabéticamente.
     * 
     * @return Lista de objetos Autor ordenados
     */
    public Lista<Autor> listarAutores() {
        Lista<Object> objetos = arbolAutores.recorridoInOrden();
        Lista<Autor> autores = new ListaDinamica<>();
        
        for (int i = 0; i < objetos.tamaño(); i++) {
            Object obj = objetos.obtener(i);
            if (obj instanceof Autor) {
                autores.agregar((Autor) obj);
            }
        }
        
        return autores;
    }
    
    /**
     * Obtiene una lista de todos los resúmenes ordenados por título.
     * 
     * @return Lista de resúmenes ordenados alfabéticamente por título
     */
    public Lista<Resumen> listarResumenes() {
        Lista<Resumen> resumenes = tablaResumenes.listarTodos();
        // Ordenar alfabéticamente por título
        if (resumenes instanceof estructuras.listas.ListaDinamica) {
            ((estructuras.listas.ListaDinamica<Resumen>) resumenes).ordenar(
                new estructuras.listas.Comparador<Resumen>() {
                    @Override
                    public int comparar(Resumen r1, Resumen r2) {
                        return r1.getTitulo().compareToIgnoreCase(r2.getTitulo());
                    }
                }
            );
        }
        return resumenes;
    }
    
    /**
     * Analiza un resumen y cuenta las frecuencias de cada palabra clave
     * en el cuerpo del resumen.
     * 
     * @param titulo Título del resumen a analizar
     * @return Mapa con las frecuencias de cada palabra clave
     */
    public Mapa<String, Integer> analizarResumen(String titulo) {
        Resumen resumen = buscarPorTitulo(titulo);
        if (resumen == null) {
            return new MapaHash<>();
        }
        
        // Obtener todas las palabras clave del sistema
        Lista<PalabraClave> todasLasPalabras = listarPalabrasClave();
        Mapa<String, Integer> frecuencias = new MapaHash<>();
        
        // Contar frecuencias usando el analizador
        for (int i = 0; i < todasLasPalabras.tamaño(); i++) {
            PalabraClave palabraClave = todasLasPalabras.obtener(i);
            int frecuencia = analizador.contarFrecuencia(
                resumen.getCuerpo(), 
                palabraClave.getTexto()
            );
            frecuencias.insertar(palabraClave.getTexto(), frecuencia);
        }
        
        return frecuencias;
    }
    
    /**
     * Actualiza el árbol AVL de palabras clave al agregar un resumen.
     * Calcula y actualiza la frecuencia total de cada palabra clave.
     * 
     * @param resumen Resumen recién agregado
     */
    private void actualizarPalabrasClave(Resumen resumen) {
        Lista<String> palabrasClave = resumen.getPalabrasClave();
        for (int i = 0; i < palabrasClave.tamaño(); i++) {
            String palabraTexto = palabrasClave.obtener(i);
            String palabraNormalizada = palabraTexto.trim().toLowerCase();
            
            // Calcular frecuencia de esta palabra en el cuerpo del resumen
            int frecuencia = analizador.contarFrecuencia(resumen.getCuerpo(), palabraNormalizada);
            
            Object resultado = arbolPalabras.buscar(palabraNormalizada);
            
            if (resultado instanceof PalabraClave) {
                // La palabra ya existe, agregar resumen y actualizar frecuencia
                PalabraClave palabraClave = (PalabraClave) resultado;
                palabraClave.agregarResumen(resumen);
                palabraClave.incrementarFrecuencia(frecuencia);
            } else {
                // Nueva palabra clave
                PalabraClave nuevaPalabra = new PalabraClave(palabraNormalizada);
                nuevaPalabra.agregarResumen(resumen);
                nuevaPalabra.incrementarFrecuencia(frecuencia);
                arbolPalabras.insertar(palabraNormalizada, nuevaPalabra);
            }
        }
    }
    
    /**
     * Actualiza el árbol AVL de autores al agregar un resumen.
     * 
     * @param resumen Resumen recién agregado
     */
    private void actualizarAutores(Resumen resumen) {
        Lista<String> autores = resumen.getAutores();
        for (int i = 0; i < autores.tamaño(); i++) {
            String nombreAutor = autores.obtener(i);
            String autorNormalizado = nombreAutor.trim();
            
            Object resultado = arbolAutores.buscar(autorNormalizado);
            
            if (resultado instanceof Autor) {
                // El autor ya existe, agregar investigación
                Autor autor = (Autor) resultado;
                autor.agregarInvestigacion(resumen);
            } else {
                // Nuevo autor
                Autor nuevoAutor = new Autor(autorNormalizado);
                nuevoAutor.agregarInvestigacion(resumen);
                arbolAutores.insertar(autorNormalizado, nuevoAutor);
            }
        }
    }
    
    /**
     * Obtiene el número total de resúmenes almacenados.
     * 
     * @return Número de resúmenes
     */
    public int getCantidadResumenes() {
        return tablaResumenes.getTamaño();
    }
    
    /**
     * Obtiene la tabla de resúmenes (para persistencia).
     * 
     * @return HashTable de resúmenes
     */
    public HashTable getTablaResumenes() {
        return tablaResumenes;
    }
    
    /**
     * Obtiene el árbol AVL de palabras clave (para persistencia).
     * 
     * @return ArbolAVL de palabras clave
     */
    public ArbolAVL getArbolPalabras() {
        return arbolPalabras;
    }
    
    /**
     * Obtiene el árbol AVL de autores (para persistencia).
     * 
     * @return ArbolAVL de autores
     */
    public ArbolAVL getArbolAutores() {
        return arbolAutores;
    }
}

