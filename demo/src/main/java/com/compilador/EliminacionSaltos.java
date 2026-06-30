package com.compilador;

import java.util.List;

// Optimizacion 3: elimina saltos redundantes -> goto L1 seguido justo de L1:
public class EliminacionSaltos {

    public boolean aplicar(List<String> codigo) {
        boolean cambio = false;
        for (int i = 0; i < codigo.size() - 1; i++) {
            String actual = codigo.get(i);
            String siguiente = codigo.get(i + 1);
            if (actual.startsWith("goto ") && siguiente.equals(actual.substring(5) + ":")) {
                codigo.set(i, "");
                cambio = true;
            }
        }
        codigo.removeIf(String::isEmpty);
        return cambio;
    }
}
