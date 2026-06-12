

package com.compilador;

import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.tree.TerminalNode;


public class SemanticVisitor extends MiLenguajeBaseVisitor<String> {

    
    private TablaSimbolos tabla = new TablaSimbolos();

    
    private List<String> errores = new ArrayList<>();
    private List<String> warnings = new ArrayList<>();

    // tipo de retorno de la funcion que se esta visitando, para chequear los return
    private String tipoRetornoActual = null;

   
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

        // bool -> int: en C++ un resultado booleano (comparacion, &&, ||, etc.)
        // se promueve a int (true = 1, false = 0).
        if (tipoVar.equals("int") && tipoExpr.equals("bool")) {
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
    public String visitBloque(MiLenguajeParser.BloqueContext ctx) {
        // nuevo ambito por cada bloque, las variables de adentro no se ven afuera
        tabla.abrirAmbito();

        for (MiLenguajeParser.SentenciaContext sentencia : ctx.sentencia()) {
            visit(sentencia);
        }

        tabla.cerrarAmbito();
        return null;
    }

    @Override
    public String visitDeclaracionFuncion(MiLenguajeParser.DeclaracionFuncionContext ctx) {
        String tipoRetorno = ctx.tipo().getText();
        String nombre = ctx.ID().getText();
        int linea = ctx.ID().getSymbol().getLine();

        List<String> tiposParams = new ArrayList<>();
        if (ctx.parametros() != null) {
            for (MiLenguajeParser.TipoContext t : ctx.parametros().tipo()) {
                tiposParams.add(t.getText());
            }
        }

        Simbolo funcion = new Simbolo(nombre, tipoRetorno, Simbolo.Categoria.FUNCION);
        funcion.setParametros(tiposParams);
        if (!tabla.declarar(funcion)) {
            errores.add("Línea " + linea + ": '" + nombre + "' ya fue declarado en este ámbito.");
        }

        // guardamos el tipo de retorno actual para que sentenciaReturn lo pueda chequear
        String tipoRetornoAnterior = tipoRetornoActual;
        tipoRetornoActual = tipoRetorno;

        // el cuerpo de la funcion es un ambito nuevo, ahi entran los parametros
        tabla.abrirAmbito();
        if (ctx.parametros() != null) {
            List<MiLenguajeParser.TipoContext> tipos = ctx.parametros().tipo();
            List<TerminalNode> ids = ctx.parametros().ID();
            for (int i = 0; i < ids.size(); i++) {
                tabla.declarar(new Simbolo(ids.get(i).getText(), tipos.get(i).getText(), Simbolo.Categoria.VARIABLE));
            }
        }
        for (MiLenguajeParser.SentenciaContext s : ctx.bloque().sentencia()) {
            visit(s);
        }
        tabla.cerrarAmbito();

        tipoRetornoActual = tipoRetornoAnterior;
        return null;
    }

    @Override
    public String visitSentenciaReturn(MiLenguajeParser.SentenciaReturnContext ctx) {
        int linea = ctx.getStart().getLine();
        String tipoRetorno = (tipoRetornoActual == null) ? "void" : tipoRetornoActual;

        if (ctx.expresion() == null) {
            if (!tipoRetorno.equals("void")) {
                errores.add("Línea " + linea + ": falta el valor de retorno, la función es de tipo '" + tipoRetorno + "'.");
            }
            return null;
        }

        String tipoExpr = visit(ctx.expresion());
        if (tipoExpr == null) {
            return null;
        }

        if (tipoRetorno.equals("void")) {
            errores.add("Línea " + linea + ": una función 'void' no puede retornar un valor.");
            return null;
        }

        compararTipos(tipoRetorno, tipoExpr, linea);
        return null;
    }

    @Override
    public String visitExprLlamada(MiLenguajeParser.ExprLlamadaContext ctx) {
        return visit(ctx.llamadaFuncion());
    }

    @Override
    public String visitLlamadaFuncion(MiLenguajeParser.LlamadaFuncionContext ctx) {
        String nombre = ctx.ID().getText();
        int linea = ctx.ID().getSymbol().getLine();

        Simbolo funcion = tabla.buscar(nombre);
        if (funcion == null || funcion.getCategoria() != Simbolo.Categoria.FUNCION) {
            errores.add("Línea " + linea + ": '" + nombre + "' no es una función declarada.");
            return null;
        }

        List<MiLenguajeParser.ExpresionContext> args = (ctx.argumentos() != null)
            ? ctx.argumentos().expresion() : new ArrayList<>();

        List<String> tiposArgs = new ArrayList<>();
        for (MiLenguajeParser.ExpresionContext arg : args) {
            tiposArgs.add(visit(arg));
        }

        List<String> tiposEsperados = funcion.getParametros();
        if (tiposArgs.size() != tiposEsperados.size()) {
            errores.add("Línea " + linea + ": '" + nombre + "' espera " + tiposEsperados.size()
                       + " argumento(s), se encontraron " + tiposArgs.size() + ".");
        } else {
            for (int i = 0; i < tiposArgs.size(); i++) {
                if (tiposArgs.get(i) != null) {
                    compararTipos(tiposEsperados.get(i), tiposArgs.get(i), linea);
                }
            }
        }

        return funcion.getTipo();
    }

      @Override
public String visitExprAgrupada(MiLenguajeParser.ExprAgrupadaContext ctx) {
    // Los paréntesis solo agrupan: el tipo de (expr) es el tipo de expr.
    return visit(ctx.expresion());
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
    public String visitExprNegativo(MiLenguajeParser.ExprNegativoContext ctx) {
        String tipo = visit(ctx.expresion());
        int linea = ctx.getStart().getLine();

        if (tipo == null) {
            return null; // ya hubo un error antes, no seguir arrastrando
        }

        boolean esNumerico = tipo.equals("int") || tipo.equals("double") || tipo.equals("char");
        if (!esNumerico) {
            errores.add("Línea " + linea + ": el operador '-' (negativo) requiere un operando "
                       + "numérico, pero se encontró '" + tipo + "'.");
            return null;
        }

        // char se promueve a int al aplicar '-', igual que en C++ (-'a' es int)
        if (tipo.equals("char")) {
            return "int";
        }
        return tipo;
    }

    @Override
    public String visitExprNot(MiLenguajeParser.ExprNotContext ctx) {
        String tipo = visit(ctx.expresion());
        int linea = ctx.getStart().getLine();

        if (tipo == null) {
            return null; // ya hubo un error antes, no seguir arrastrando
        }

        boolean esValido = tipo.equals("bool") || tipo.equals("int")
                          || tipo.equals("double") || tipo.equals("char");
        if (!esValido) {
            errores.add("Línea " + linea + ": el operador '!' requiere un operando booleano "
                       + "o numérico, pero se encontró '" + tipo + "'.");
            return null;
        }

        return "bool";
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
    @Override
    public String visitExprRelacional(MiLenguajeParser.ExprRelacionalContext ctx) {
        String tipoIzq = visit(ctx.expresion(0));
        String tipoDer = visit(ctx.expresion(1));
        int linea = ctx.getStart().getLine();

        if (tipoIzq != null && tipoDer != null) {
            boolean izqNumerico = tipoIzq.equals("int") || tipoIzq.equals("double") || tipoIzq.equals("char");
            boolean derNumerico = tipoDer.equals("int") || tipoDer.equals("double") || tipoDer.equals("char");

            if (!izqNumerico || !derNumerico) {
                errores.add("Línea " + linea + ": no se puede comparar tipo '" + tipoIzq
                           + "' con tipo '" + tipoDer + "'.");
            }
        }

        return "bool";
    }

    @Override
    public String visitExprIgualdad(MiLenguajeParser.ExprIgualdadContext ctx) {
        String tipoIzq = visit(ctx.expresion(0));
        String tipoDer = visit(ctx.expresion(1));
        int linea = ctx.getStart().getLine();

        if (tipoIzq != null && tipoDer != null) {
            boolean izqNumerico = tipoIzq.equals("int") || tipoIzq.equals("double") || tipoIzq.equals("char");
            boolean derNumerico = tipoDer.equals("int") || tipoDer.equals("double") || tipoDer.equals("char");
            boolean ambosNumericos = izqNumerico && derNumerico;
            boolean mismoTipo = tipoIzq.equals(tipoDer);

            if (!mismoTipo && !ambosNumericos) {
                errores.add("Línea " + linea + ": no se puede comparar tipo '" + tipoIzq
                           + "' con tipo '" + tipoDer + "'.");
            }
        }

        return "bool";
    }

    @Override
    public String visitExprAnd(MiLenguajeParser.ExprAndContext ctx) {
        return verificarOperandosBooleanos(ctx.expresion(0), ctx.expresion(1), ctx.getStart().getLine(), "&&");
    }

    @Override
    public String visitExprOr(MiLenguajeParser.ExprOrContext ctx) {
        return verificarOperandosBooleanos(ctx.expresion(0), ctx.expresion(1), ctx.getStart().getLine(), "||");
    }

    private String verificarOperandosBooleanos(MiLenguajeParser.ExpresionContext izq,
                                                 MiLenguajeParser.ExpresionContext der,
                                                 int linea, String operador) {
        String tipoIzq = visit(izq);
        String tipoDer = visit(der);

        if (tipoIzq != null && !tipoIzq.equals("bool")) {
            errores.add("Línea " + linea + ": el operador '" + operador
                       + "' requiere operandos booleanos, pero se encontró '" + tipoIzq + "'.");
        }
        if (tipoDer != null && !tipoDer.equals("bool")) {
            errores.add("Línea " + linea + ": el operador '" + operador
                       + "' requiere operandos booleanos, pero se encontró '" + tipoDer + "'.");
        }

        return "bool";
    }
}