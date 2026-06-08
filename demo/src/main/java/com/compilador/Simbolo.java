package com.compilador;

public class Simbolo {

    // Un símbolo puede ser una VARIABLE o una FUNCIÓN.
    // Usamos un 'enum' (tipo enumerado) para representar
    // estas dos posibilidades de forma segura: el compilador
    // no te va a dejar escribir "VARIBLE" por error de tipeo,
    // como sí podría pasar si usáramos un String.
    public enum Categoria {
        VARIABLE,
        FUNCION
    }



    // Datos que guarda cada símbolo:
    private String nombre;       // ej: "x", "sumar"
    private String tipo;         // ej: "int", "double", "void"
    private Categoria categoria; // VARIABLE o FUNCION

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

    @Override
    public String toString() {
        return categoria + " '" + nombre + "' : " + tipo;
    }
    
}