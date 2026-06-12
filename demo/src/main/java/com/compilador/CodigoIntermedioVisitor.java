package com.compilador;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CodigoIntermedioVisitor extends MiLenguajeBaseVisitor<String> {

    private List<String> codigo = new ArrayList<>();

    private int contadorTemp = 0;
    private int contadorEtiqueta = 0;

    // para resolver break/continue: cada loop guarda [etiquetaContinue, etiquetaFin]
    private Deque<String[]> pilaLoops = new ArrayDeque<>();

    public List<String> getCodigo() {
        return codigo;
    }

    private String nuevoTemp() {
        contadorTemp++;
        return "t" + contadorTemp;
    }

    private String nuevaEtiqueta() {
        contadorEtiqueta++;
        return "L" + contadorEtiqueta;
    }

    private void emit(String instruccion) {
        codigo.add(instruccion);
    }

    // --- declaraciones y asignaciones ---

    @Override
    public String visitDeclaracion(MiLenguajeParser.DeclaracionContext ctx) {
        if (ctx.expresion() != null) {
            String valor = visit(ctx.expresion());
            emit(ctx.ID().getText() + " = " + valor);
        }
        return null;
    }

    @Override
    public String visitAsignacion(MiLenguajeParser.AsignacionContext ctx) {
        String valor = visit(ctx.expresion());
        emit(ctx.ID().getText() + " = " + valor);
        return null;
    }

    @Override
    public String visitSentenciaCout(MiLenguajeParser.SentenciaCoutContext ctx) {
        String valor = visit(ctx.expresion());
        emit("print " + valor);
        return null;
    }

    // --- control de flujo ---

    @Override
    public String visitSentenciaIf(MiLenguajeParser.SentenciaIfContext ctx) {
        String cond = visit(ctx.expresion());
        String lFalse = nuevaEtiqueta();
        emit("if not " + cond + " goto " + lFalse);

        visit(ctx.bloque(0));

        if (ctx.bloque().size() > 1) {
            String lFin = nuevaEtiqueta();
            emit("goto " + lFin);
            emit(lFalse + ":");
            visit(ctx.bloque(1));
            emit(lFin + ":");
        } else {
            emit(lFalse + ":");
        }
        return null;
    }

    @Override
    public String visitSentenciaWhile(MiLenguajeParser.SentenciaWhileContext ctx) {
        String lInicio = nuevaEtiqueta();
        String lFin = nuevaEtiqueta();

        emit(lInicio + ":");
        String cond = visit(ctx.expresion());
        emit("if not " + cond + " goto " + lFin);

        pilaLoops.push(new String[]{lInicio, lFin});
        visit(ctx.bloque());
        pilaLoops.pop();

        emit("goto " + lInicio);
        emit(lFin + ":");
        return null;
    }

    @Override
    public String visitSentenciaFor(MiLenguajeParser.SentenciaForContext ctx) {
        visit(ctx.declaracion());

        String lInicio = nuevaEtiqueta();
        String lInc = nuevaEtiqueta();
        String lFin = nuevaEtiqueta();

        emit(lInicio + ":");
        String cond = visit(ctx.expresion(0));
        emit("if not " + cond + " goto " + lFin);

        pilaLoops.push(new String[]{lInc, lFin});
        visit(ctx.bloque());
        pilaLoops.pop();

        emit(lInc + ":");
        String incremento = visit(ctx.expresion(1));
        emit(ctx.ID().getText() + " = " + incremento);
        emit("goto " + lInicio);
        emit(lFin + ":");
        return null;
    }

    @Override
    public String visitSentenciaBreak(MiLenguajeParser.SentenciaBreakContext ctx) {
        if (!pilaLoops.isEmpty()) {
            emit("goto " + pilaLoops.peek()[1]);
        }
        return null;
    }

    @Override
    public String visitSentenciaContinue(MiLenguajeParser.SentenciaContinueContext ctx) {
        if (!pilaLoops.isEmpty()) {
            emit("goto " + pilaLoops.peek()[0]);
        }
        return null;
    }

    // --- funciones ---

    @Override
    public String visitDeclaracionFuncion(MiLenguajeParser.DeclaracionFuncionContext ctx) {
        emit("");
        emit("func " + ctx.ID().getText() + ":");
        for (MiLenguajeParser.SentenciaContext s : ctx.bloque().sentencia()) {
            visit(s);
        }
        emit("end func " + ctx.ID().getText());
        return null;
    }

    @Override
    public String visitSentenciaReturn(MiLenguajeParser.SentenciaReturnContext ctx) {
        if (ctx.expresion() != null) {
            String valor = visit(ctx.expresion());
            emit("return " + valor);
        } else {
            emit("return");
        }
        return null;
    }

    @Override
    public String visitExprLlamada(MiLenguajeParser.ExprLlamadaContext ctx) {
        return visit(ctx.llamadaFuncion());
    }

    @Override
    public String visitLlamadaFuncion(MiLenguajeParser.LlamadaFuncionContext ctx) {
        List<MiLenguajeParser.ExpresionContext> args = (ctx.argumentos() != null)
            ? ctx.argumentos().expresion() : new ArrayList<>();

        List<String> valores = new ArrayList<>();
        for (MiLenguajeParser.ExpresionContext arg : args) {
            valores.add(visit(arg));
        }
        for (String v : valores) {
            emit("param " + v);
        }

        String temp = nuevoTemp();
        emit(temp + " = call " + ctx.ID().getText() + ", " + valores.size());
        return temp;
    }

    // --- expresiones ---

    @Override
    public String visitExprAgrupada(MiLenguajeParser.ExprAgrupadaContext ctx) {
        // los parentesis no generan codigo, solo agrupan
        return visit(ctx.expresion());
    }

    @Override
    public String visitExprEntero(MiLenguajeParser.ExprEnteroContext ctx) {
        return ctx.getText();
    }

    @Override
    public String visitExprDecimal(MiLenguajeParser.ExprDecimalContext ctx) {
        return ctx.getText();
    }

    @Override
    public String visitExprCaracter(MiLenguajeParser.ExprCaracterContext ctx) {
        return ctx.getText();
    }

    @Override
    public String visitExprCadena(MiLenguajeParser.ExprCadenaContext ctx) {
        return ctx.getText();
    }

    @Override
    public String visitExprVerdadero(MiLenguajeParser.ExprVerdaderoContext ctx) {
        return "true";
    }

    @Override
    public String visitExprFalso(MiLenguajeParser.ExprFalsoContext ctx) {
        return "false";
    }

    @Override
    public String visitExprIdentificador(MiLenguajeParser.ExprIdentificadorContext ctx) {
        return ctx.ID().getText();
    }

    @Override
    public String visitExprNegativo(MiLenguajeParser.ExprNegativoContext ctx) {
        return unaria(ctx.expresion(), "-");
    }

    @Override
    public String visitExprNot(MiLenguajeParser.ExprNotContext ctx) {
        return unaria(ctx.expresion(), "!");
    }

    @Override
    public String visitExprMultiplicativa(MiLenguajeParser.ExprMultiplicativaContext ctx) {
        return binaria(ctx.expresion(0), ctx.expresion(1), ctx.getChild(1).getText());
    }

    @Override
    public String visitExprAditiva(MiLenguajeParser.ExprAditivaContext ctx) {
        return binaria(ctx.expresion(0), ctx.expresion(1), ctx.getChild(1).getText());
    }

    @Override
    public String visitExprRelacional(MiLenguajeParser.ExprRelacionalContext ctx) {
        return binaria(ctx.expresion(0), ctx.expresion(1), ctx.getChild(1).getText());
    }

    @Override
    public String visitExprIgualdad(MiLenguajeParser.ExprIgualdadContext ctx) {
        return binaria(ctx.expresion(0), ctx.expresion(1), ctx.getChild(1).getText());
    }

    @Override
    public String visitExprAnd(MiLenguajeParser.ExprAndContext ctx) {
        return binaria(ctx.expresion(0), ctx.expresion(1), "&&");
    }

    @Override
    public String visitExprOr(MiLenguajeParser.ExprOrContext ctx) {
        return binaria(ctx.expresion(0), ctx.expresion(1), "||");
    }

    private String binaria(MiLenguajeParser.ExpresionContext izq, MiLenguajeParser.ExpresionContext der, String operador) {
        String l = visit(izq);
        String r = visit(der);
        String temp = nuevoTemp();
        emit(temp + " = " + l + " " + operador + " " + r);
        return temp;
    }

    private String unaria(MiLenguajeParser.ExpresionContext expr, String operador) {
        String v = visit(expr);
        String temp = nuevoTemp();
        emit(temp + " = " + operador + v);
        return temp;
    }
}
