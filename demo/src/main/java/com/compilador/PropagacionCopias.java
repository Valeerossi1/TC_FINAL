package com.compilador;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Optimizacion 2: propagacion de copias -> t1 = x ... usar(t1)  pasa a  ... usar(x)
public class PropagacionCopias {

    private static final Pattern COPIA_SIMPLE = Pattern.compile(
        "^(t\\d+) = ([A-Za-z_][A-Za-z0-9_]*|-?\\d+(?:\\.\\d+)?|true|false)$");

    public boolean aplicar(List<String> codigo) {
        boolean cambioGlobal = false;
        for (int i = 0; i < codigo.size(); i++) {
            Matcher m = COPIA_SIMPLE.matcher(codigo.get(i));
            if (!m.matches()) continue;

            String temp = m.group(1);
            String valor = m.group(2);
            if (temp.equals(valor)) continue;

            Pattern usoTemp = Pattern.compile("\\b" + Pattern.quote(temp) + "\\b");
            boolean usado = false;
            for (int j = 0; j < codigo.size(); j++) {
                if (j == i) continue;
                String linea = codigo.get(j);
                if (usoTemp.matcher(linea).find()) {
                    codigo.set(j, usoTemp.matcher(linea).replaceAll(Matcher.quoteReplacement(valor)));
                    usado = true;
                    cambioGlobal = true;
                }
            }
            if (usado) {
                codigo.set(i, "");
                cambioGlobal = true;
            }
        }
        codigo.removeIf(String::isEmpty);
        return cambioGlobal;
    }
}
