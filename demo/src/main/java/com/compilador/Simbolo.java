package com.compilador;

import java.util.List;

public class Simbolo {

    // Un símbolo puede ser una VARIABLE o una FUNCIÓN.
    // Usamos un 'enum' (tipo enumerado) para representar
    // estas dos posibilidades de forma segura: el compilador
    // no te va a dejar escribir "VARIBLE" por error de tipeo,
    // como sí podría pasar si usáramos un String.
    public enum Categoria {
        VARIABLE,
        FUNCION,
        PARAMETRO
    }



    // Datos que guarda cada símbolo:
    private String nombre;       // ej: "x", "sumar"
    private String tipo;         // ej: "int", "double", "void"
    private Categoria categoria; // VARIABLE, FUNCION o PARAMETRO
    private List<String> parametros; // solo para FUNCION: tipos de cada parametro en orden

    // datos extra para mostrar la tabla de simbolos
    private int linea;
    private int columna;
    private String ambito; // "global" o el nombre de la funcion que la contiene

    // Constructor: se usa para crear un símbolo nuevo
    // Ejemplo de uso: new Simbolo("x", "int", Categoria.VARIABLE)
    public Simbolo(String nombre, String tipo, Categoria categoria) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public List<String> getParametros() {
        return parametros;
    }

    public void setParametros(List<String> parametros) {
        this.parametros = parametros;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    @Override
    public String toString() {
        return categoria + " '" + nombre + "' : " + tipo;
    }
    
}