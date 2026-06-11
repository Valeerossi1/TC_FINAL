

package com.compilador;

import java.util.ArrayList;
import java.util.List;


public class SemanticVisitor extends MiLenguajeBaseVisitor<String> {

    
    private TablaSimbolos tabla = new TablaSimbolos();

    
    private List<String> errores = new ArrayList<>();
    private List<String> warnings = new ArrayList<>();

   
    public List<String> getErrores() {
        return errores;
    }

    public List<String> getWarnings() {
        return warnings;
    }

      
   @Override
    public String visitDeclaracion(MiLenguajeParser.DeclaracionContext ctx) {
        String tipoVar = ctx.tipo().getText();
        String nombre  = ctx.ID().getText();
        int linea = ctx.ID().getSymbol().getLine();

        Simbolo simbolo = new Simbolo(nombre, tipoVar, Simbolo.Categoria.VARIABLE);
        boolean pudoDeclarar = tabla.declarar(simbolo);

        if (!pudoDeclarar) {
            errores.add("Línea " + linea + ": la variable '" + nombre
                       + "' ya fue declarada en este ámbito.");
        }

        // Si tiene valor inicial, chequear que el tipo sea compatible
        if (ctx.expresion() != null) {
            String tipoExpr = visit(ctx.expresion());
            if (tipoExpr != null) {
                compararTipos(tipoVar, tipoExpr, linea);
            }
        }

        return null;
    }
    
    private void compararTipos(String tipoVar, String tipoExpr, int linea) {
      
        if (tipoVar.equals(tipoExpr)) {
            return; // mismo tipo -> todo bien
        }

    
        if (tipoVar.equals("double") && tipoExpr.equals("int")) {
            return;
        }

      
        if (tipoVar.equals("int") && tipoExpr.equals("double")) {
            warnings.add("Línea " + linea + ": se asigna un valor 'double' a una "
                        + "variable 'int' (se pierde la parte decimal).");
            return;
        }

      
        boolean varEsIntOCarCar = tipoVar.equals("int") || tipoVar.equals("char");
        boolean exprEsIntOCarCar = tipoExpr.equals("int") || tipoExpr.equals("char");
        if (varEsIntOCarCar && exprEsIntOCarCar) {
            return;
        }


        errores.add("Línea " + linea + ": tipos incompatibles. No se puede asignar "
                   + "un valor de tipo '" + tipoExpr + "' a una variable de tipo '"
                   + tipoVar + "'.");
    }

      @Override
public String visitExprEntero(MiLenguajeParser.ExprEnteroContext ctx) {
    return "int";
}

@Override
public String visitExprDecimal(MiLenguajeParser.ExprDecimalContext ctx) {
    return "double";
}

@Override
public String visitExprCaracter(MiLenguajeParser.ExprCaracterContext ctx) {
    return "char";
}

@Override
public String visitExprCadena(MiLenguajeParser.ExprCadenaContext ctx) {
    return "string";
}

@Override
public String visitExprVerdadero(MiLenguajeParser.ExprVerdaderoContext ctx) {
    return "bool";
}

@Override
public String visitExprFalso(MiLenguajeParser.ExprFalsoContext ctx) {
    return "bool";
}
    
@Override
    public String visitExprIdentificador(MiLenguajeParser.ExprIdentificadorContext ctx) {
        String nombre = ctx.ID().getText();
        int linea = ctx.ID().getSymbol().getLine();

        Simbolo simbolo = tabla.buscar(nombre);

        if (simbolo == null) {
            errores.add("Línea " + linea + ": la variable '" + nombre + "' no fue declarada.");
            return null;
        }

        return simbolo.getTipo();
    }

    @Override
    public String visitExprAditiva(MiLenguajeParser.ExprAditivaContext ctx) {
        String tipoIzq = visit(ctx.expresion(0));
        String tipoDer = visit(ctx.expresion(1));
        int linea = ctx.getStart().getLine();
        return tipoResultadoAritmetico(tipoIzq, tipoDer, linea);
    }

    @Override
    public String visitExprMultiplicativa(MiLenguajeParser.ExprMultiplicativaContext ctx) {
        String tipoIzq = visit(ctx.expresion(0));
        String tipoDer = visit(ctx.expresion(1));
        int linea = ctx.getStart().getLine();
        return tipoResultadoAritmetico(tipoIzq, tipoDer, linea);
    }

    private String tipoResultadoAritmetico(String tipoIzq, String tipoDer, int linea) {
        if (tipoIzq == null || tipoDer == null) {
            return null; // ya hubo un error antes, no seguir arrastrando
        }

        boolean izqNumerico = tipoIzq.equals("int") || tipoIzq.equals("double") || tipoIzq.equals("char");
        boolean derNumerico = tipoDer.equals("int") || tipoDer.equals("double") || tipoDer.equals("char");

        if (!izqNumerico || !derNumerico) {
            errores.add("Línea " + linea + ": operación aritmética inválida entre tipos '"
                       + tipoIzq + "' y '" + tipoDer + "'.");
            return null;
        }

        if (tipoIzq.equals("double") || tipoDer.equals("double")) {
            return "double";
        }

        return "int";
    }
}