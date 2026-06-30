package com.compilador;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Optimizacion 4: elimina temporales que se calculan y nunca se usan
public class EliminacionCodigoMuerto {

    private static final Pattern DEFINE_TEMP = Pattern.compile("^(t\\d+) = (.*)$");

    public boolean aplicar(List<String> codigo) {
        boolean cambio = false;
        for (int i = 0; i < codigo.size(); i++) {
            Matcher m = DEFINE_TEMP.matcher(codigo.get(i));
            if (!m.matches()) continue;

            String temp = m.group(1);
            String expr = m.group(2);
            if (expr.startsWith("call ")) continue; // puede tener efectos secundarios

            Pattern usoTemp = Pattern.compile("\\b" + Pattern.quote(temp) + "\\b");
            boolean usado = false;
            for (int j = 0; j < codigo.size(); j++) {
                if (j == i) continue;
                if (usoTemp.matcher(codigo.get(j)).find()) {
                    usado = true;
                    break;
                }
            }
            if (!usado) {
                codigo.set(i, "");
                cambio = true;
            }
        }
        codigo.removeIf(String::isEmpty);
        return cambio;
    }
}
