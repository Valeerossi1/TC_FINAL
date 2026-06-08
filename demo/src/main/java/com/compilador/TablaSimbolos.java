package com.compilador;

import java.util.ArrayList;
import java.util.HashMap;


public class TablaSimbolos {

    // Pila de ambitos. Cada ambito es un HashMap: nombre a simbolo.
    private ArrayList<HashMap<String, Simbolo>> pila;

    public TablaSimbolos() {
        pila = new ArrayList<>();
        pila.add(new HashMap<>()); 
    }

        // Se llama cuando entramos a un bloque nuevo: { ... }
    // Apila un HashMap vacío -> nuevo ámbito "actual"
    public void abrirAmbito() {
        pila.add(new HashMap<>());
    }

    // Se llama cuando salimos de un bloque: }
    // Desapila el ámbito actual -> sus variables dejan de existir
    public void cerrarAmbito() {
        pila.remove(pila.size() - 1);
    }


    // Intenta agregar un símbolo nuevo al ámbito ACTUAL (el tope de la pila).
    // Devuelve 'true' si se pudo declarar, 'false' si ya existía
    // un símbolo con ese nombre EN ESE MISMO ámbito (redeclaración).
    public boolean declarar(Simbolo simbolo) {
        HashMap<String, Simbolo> ambitoActual = pila.get(pila.size() - 1);

        if (ambitoActual.containsKey(simbolo.getNombre())) {
            return false; // ya existe -> error de redeclaración
        }

        ambitoActual.put(simbolo.getNombre(), simbolo);
        return true;
    }

    // Busca un símbolo por nombre, empezando por el ámbito ACTUAL
    // y bajando hacia los ámbitos exteriores (el global queda último).
    // Devuelve el Simbolo si lo encuentra, o null si no existe en ningún ámbito.
    public Simbolo buscar(String nombre) {
        for (int i = pila.size() - 1; i >= 0; i--) {
            HashMap<String, Simbolo> ambito = pila.get(i);
            if (ambito.containsKey(nombre)) {
                return ambito.get(nombre);
            }
        }
        return null; // no se encontró en ningún ámbito -> no está declarado
    }

}

