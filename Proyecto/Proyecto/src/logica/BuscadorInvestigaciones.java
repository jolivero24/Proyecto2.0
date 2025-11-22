package logica;

import modelo.Resumen;
import estructuras.listas.Lista;
import estructuras.listas.ListaDinamica;
import estructuras.listas.Comparador;

/**
 * Clase que proporciona funcionalidades de búsqueda avanzada de investigaciones.
 * 
 * @author Equipo SuperMetroMendeley
 * @version 1.0
 */
public class BuscadorInvestigaciones {
    
    /**
     * Gestor de resúmenes que contiene las estructuras de datos.
     */
    private GestorResumenes gestor;
    
    /**
     * Constructor que inicializa el buscador con un gestor de resúmenes.
     * 
     * @param gestor Gestor de resúmenes
     * @throws IllegalArgumentException si el gestor es null
     */
    public BuscadorInvestigaciones(GestorResumenes gestor) {
        if (gestor == null) {
            throw new IllegalArgumentException("El gestor no puede ser null");
        }
        this.gestor = gestor;
    }
    
    /**
     * Busca investigaciones por un criterio específico.
     * 
     * @param criterio Criterio de búsqueda ("titulo", "autor", "palabra")
     * @param valor Valor a buscar
     * @return Lista de resúmenes que coinciden con el criterio
     */
    public Lista<Resumen> buscarPorCriterio(String criterio, String valor) {
        if (criterio == null || valor == null) {
            return new ListaDinamica<>();
        }
        
        switch (criterio.toLowerCase()) {
            case "titulo":
                Resumen resumen = gestor.buscarPorTitulo(valor);
                Lista<Resumen> resultado = new ListaDinamica<>();
                if (resumen != null) {
                    resultado.agregar(resumen);
                }
                return resultado;
                
            case "autor":
                return gestor.buscarPorAutor(valor);
                
            case "palabra":
            case "palabraclave":
                return gestor.buscarPorPalabraClave(valor);
                
            default:
                return new ListaDinamica<>();
        }
    }
    
    /**
     * Ordena una lista de resúmenes por título alfabéticamente.
     * 
     * @param resumenes Lista de resúmenes a ordenar
     * @return Lista ordenada por título
     */
    public Lista<Resumen> ordenarPorTitulo(Lista<Resumen> resumenes) {
        if (resumenes == null) {
            return new ListaDinamica<>();
        }
        
        ListaDinamica<Resumen> copia = new ListaDinamica<>(resumenes);
        copia.ordenar(new Comparador<Resumen>() {
            @Override
            public int comparar(Resumen r1, Resumen r2) {
                return r1.getTitulo().compareToIgnoreCase(r2.getTitulo());
            }
        });
        return copia;
    }
}

