package com.compilador;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Optimizador {

    private static final Pattern BINARIA_CONST = Pattern.compile(
        "^(t\\d+) = (-?\\d+(?:\\.\\d+)?) ([+\\-*/%]) (-?\\d+(?:\\.\\d+)?)$");

    private static final Pattern COMPARACION_CONST = Pattern.compile(
        "^(t\\d+) = (-?\\d+(?:\\.\\d+)?) (==|!=|>=|<=|>|<) (-?\\d+(?:\\.\\d+)?)$");

    private static final Pattern LOGICA_CONST = Pattern.compile(
        "^(t\\d+) = (true|false) (&&|\\|\\|) (true|false)$");

    private static final Pattern UNARIA_CONST = Pattern.compile(
        "^(t\\d+) = (-|!)(-?\\d+(?:\\.\\d+)?|true|false)$");

    private static final Pattern COPIA_SIMPLE = Pattern.compile(
        "^(t\\d+) = ([A-Za-z_][A-Za-z0-9_]*|-?\\d+(?:\\.\\d+)?|true|false)$");

    private static final Pattern DEFINE_TEMP = Pattern.compile("^(t\\d+) = (.*)$");

    // aplica las 4 optimizaciones en rondas hasta que no cambie nada
    public List<String> optimizar(List<String> original) {
        List<String> codigo = new ArrayList<>(original);

        for (int ronda = 0; ronda < 5; ronda++) {
            boolean cambio = false;
            cambio |= plegarConstantes(codigo);
            cambio |= propagarCopias(codigo);
            cambio |= eliminarSaltosRedundantes(codigo);
            cambio |= eliminarCodigoMuerto(codigo);
            if (!cambio) break;
        }
        return codigo;
    }

    // --- 1) Plegado de constantes: t = 2 + 3  ->  t = 5 ---
    private boolean plegarConstantes(List<String> codigo) {
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

    // --- 2) Propagacion de copias: t1 = x ... usar(t1)  ->  ... usar(x) ---
    private boolean propagarCopias(List<String> codigo) {
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

    // --- 3) Saltos redundantes: goto L1 seguido justo de L1: ---
    private boolean eliminarSaltosRedundantes(List<String> codigo) {
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

    // --- 4) Codigo muerto: temporales que se calculan y nunca se usan ---
    private boolean eliminarCodigoMuerto(List<String> codigo) {
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
