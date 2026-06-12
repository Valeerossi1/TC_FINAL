package com.compilador;

import static org.junit.Assert.*;

import org.junit.Test;

public class TablaSimbolosTest {

    @Test
    public void variableGlobalEsVisibleDesdeAmbitoInterno() {
        TablaSimbolos tabla = new TablaSimbolos();
        tabla.declarar(new Simbolo("x", "int", Simbolo.Categoria.VARIABLE));

        tabla.abrirAmbito(); // simula entrar a una funcion
        assertNotNull(tabla.buscar("x")); // la global se ve desde adentro
        tabla.cerrarAmbito();
    }

    @Test
    public void funcionInternaNoExisteFueraDeSuAmbito() {
        TablaSimbolos tabla = new TablaSimbolos();

        tabla.abrirAmbito(); // ambito de la funcion contenedora
        tabla.declarar(new Simbolo("interna", "int", Simbolo.Categoria.FUNCION));
        assertNotNull(tabla.buscar("interna")); // visible adentro
        tabla.cerrarAmbito();

        assertNull(tabla.buscar("interna")); // desaparecio al cerrar el ambito
    }

    @Test
    public void redeclaracionEnMismoAmbitoFalla() {
        TablaSimbolos tabla = new TablaSimbolos();
        assertTrue(tabla.declarar(new Simbolo("x", "int", Simbolo.Categoria.VARIABLE)));
        assertFalse(tabla.declarar(new Simbolo("x", "double", Simbolo.Categoria.VARIABLE)));
    }

    @Test
    public void mismoNombreEnAmbitosDistintosNoColisiona() {
        TablaSimbolos tabla = new TablaSimbolos();
        assertTrue(tabla.declarar(new Simbolo("x", "int", Simbolo.Categoria.VARIABLE)));

        tabla.abrirAmbito();
        assertTrue(tabla.declarar(new Simbolo("x", "double", Simbolo.Categoria.VARIABLE)));
        assertEquals("double", tabla.buscar("x").getTipo()); // gana la mas cercana
        tabla.cerrarAmbito();

        assertEquals("int", tabla.buscar("x").getTipo()); // vuelve a verse la global
    }

    @Test
    public void buscarSimboloInexistenteDevuelveNull() {
        TablaSimbolos tabla = new TablaSimbolos();
        assertNull(tabla.buscar("noexiste"));
    }
}
