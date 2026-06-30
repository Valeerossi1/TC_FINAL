package com.compilador;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Optimizacion 1: plegado de constantes -> t = 2 + 3  pasa a  t = 5
public class PlegadoConstantes {

    private static final Pattern BINARIA_CONST = Pattern.compile(
        "^(t\\d+) = (-?\\d+(?:\\.\\d+)?) ([+\\-*/%]) (-?\\d+(?:\\.\\d+)?)$");

    private static final Pattern COMPARACION_CONST = Pattern.compile(
        "^(t\\d+) = (-?\\d+(?:\\.\\d+)?) (==|!=|>=|<=|>|<) (-?\\d+(?:\\.\\d+)?)$");

    private static final Pattern LOGICA_CONST = Pattern.compile(
        "^(t\\d+) = (true|false) (&&|\\|\\|) (true|false)$");

    private static final Pattern UNARIA_CONST = Pattern.compile(
        "^(t\\d+) = (-|!)(-?\\d+(?:\\.\\d+)?|true|false)$");

    public boolean aplicar(List<String> codigo) {
        boolean cambio = false;
        for (int i = 0; i < codigo.size(); i++) {
            String linea = codigo.get(i);
            Matcher m;

            m = BINARIA_CONST.matcher(linea);
            if (m.matches()) {
                String r = calcular(m.group(2), m.group(4), m.group(3));
                if (r != null) {
                    codigo.set(i, m.group(1) + " = " + r);
                    cambio = true;
                }
                continue;
            }

            m = COMPARACION_CONST.matcher(linea);
            if (m.matches()) {
                boolean r = comparar(m.group(2), m.group(4), m.group(3));
                codigo.set(i, m.group(1) + " = " + r);
                cambio = true;
                continue;
            }

            m = LOGICA_CONST.matcher(linea);
            if (m.matches()) {
                boolean a = Boolean.parseBoolean(m.group(2));
                boolean b = Boolean.parseBoolean(m.group(4));
                boolean r = m.group(3).equals("&&") ? (a && b) : (a || b);
                codigo.set(i, m.group(1) + " = " + r);
                cambio = true;
                continue;
            }

            m = UNARIA_CONST.matcher(linea);
            if (m.matches()) {
                String op = m.group(2);
                String val = m.group(3);
                String r;
                if (op.equals("-")) {
                    r = val.contains(".") ? String.valueOf(-Double.parseDouble(val))
                                           : String.valueOf(-Long.parseLong(val));
                } else {
                    r = String.valueOf(!Boolean.parseBoolean(val));
                }
                codigo.set(i, m.group(1) + " = " + r);
                cambio = true;
            }
        }
        return cambio;
    }

    private String calcular(String a, String b, String op) {
        double da = Double.parseDouble(a);
        double db = Double.parseDouble(b);
        boolean esDouble = a.contains(".") || b.contains(".");

        if ((op.equals("/") || op.equals("%")) && db == 0) {
            return null; // no plegar division por cero, que reviente en runtime
        }

        double r;
        switch (op) {
            case "+": r = da + db; break;
            case "-": r = da - db; break;
            case "*": r = da * db; break;
            case "/": r = da / db; break;
            case "%": r = da % db; break;
            default: return null;
        }
        return esDouble ? String.valueOf(r) : String.valueOf((long) r);
    }

    private boolean comparar(String a, String b, String op) {
        double da = Double.parseDouble(a);
        double db = Double.parseDouble(b);
        switch (op) {
            case "==": return da == db;
            case "!=": return da != db;
            case ">":  return da > db;
            case "<":  return da < db;
            case ">=": return da >= db;
            case "<=": return da <= db;
        }
        return false;
    }
}
