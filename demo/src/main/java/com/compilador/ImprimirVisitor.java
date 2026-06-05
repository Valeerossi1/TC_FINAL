package com.compilador;

/**
 * ImprimirVisitor — Visitor educativo para recorrer el árbol de parseo.
 *
 * El patrón VISITOR permite ejecutar operaciones sobre cada nodo del
 * árbol sin modificar las clases del árbol en sí.
 *
 * ANTLR4 genera automáticamente:
 *   - MiLenguajeVisitor<T>     : interfaz con un método por cada regla
 *   - MiLenguajeBaseVisitor<T> : implementación vacía (devuelve null)
 *
 * Extendemos MiLenguajeBaseVisitor<String> y sobreescribimos
 * los métodos que nos interesan. Cada método recibe un "Context"
 * que da acceso a todos los hijos del nodo en el árbol.
 *
 * El parámetro de tipo <String> es el tipo de RETORNO de cada visita.
 * En un compilador real se podría usar <Void>, <Integer>, o un tipo AST.
 *
 * NOTA: Este archivo usa las clases generadas por ANTLR4.
 *       Debes ejecutar 'mvn generate-sources' antes de compilar.
 */
public class ImprimirVisitor extends MiLenguajeBaseVisitor<String> {

    // Nivel de indentación actual (se incrementa al entrar en un bloque)
    private int nivel = 0;

    // Prefijo visual para mostrar la jerarquía del árbol
    private String indentar() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nivel; i++) {
            sb.append("  │  ");
        }
        return sb.toString();
    }

    private void imprimir(String texto) {
        System.out.println(indentar() + texto);
    }

    // =========================================================
    //  REGLA: programa
    //  Nodo raíz del árbol. Contiene todas las sentencias.
    // =========================================================
    @Override
    public String visitPrograma(MiLenguajeParser.ProgramaContext ctx) {
        imprimir("PROGRAMA");
        nivel++;
        visitChildren(ctx);
        nivel--;
        return null;
    }

    // =========================================================
    //  REGLA: declaracion
    //  Ejemplo: int x = 10;
    //  ctx.tipo()   → el tipo de dato
    //  ctx.ID()     → el nombre de la variable
    //  ctx.expresion() → el valor inicial (puede ser null)
    // =========================================================
    @Override
    public String visitDeclaracion(MiLenguajeParser.DeclaracionContext ctx) {
        String tipo  = ctx.tipo().getText();
        String nombre = ctx.ID().getText();
        boolean tieneValor = ctx.expresion() != null;

        imprimir("DECLARACION → " + tipo + " " + nombre
                 + (tieneValor ? " = ..." : " (sin valor inicial)"));
        if (tieneValor) {
            nivel++;
            visit(ctx.expresion());
            nivel--;
        }
        return null;
    }

    // =========================================================
    //  REGLA: asignacion
    //  Ejemplo: x = x + 1;
    //  ctx.ID()       → nombre de la variable a asignar
    //  ctx.expresion() → el nuevo valor
    // =========================================================
    @Override
    public String visitAsignacion(MiLenguajeParser.AsignacionContext ctx) {
        imprimir("ASIGNACION → " + ctx.ID().getText() + " = ...");
        nivel++;
        visit(ctx.expresion());
        nivel--;
        return null;
    }

    // =========================================================
    //  REGLA: sentenciaCout
    //  Ejemplo: cout << x;
    //  ctx.expresion() → lo que se imprime
    // =========================================================
    @Override
    public String visitSentenciaCout(MiLenguajeParser.SentenciaCoutContext ctx) {
        imprimir("COUT <<");
        nivel++;
        visit(ctx.expresion());
        nivel--;
        return null;
    }

    // =========================================================
    //  REGLA: sentenciaIf
    //  Ejemplo: if (x > 0) { ... } else { ... }
    //  ctx.expresion()  → la condición
    //  ctx.bloque(0)    → bloque del IF
    //  ctx.bloque(1)    → bloque del ELSE (puede no existir)
    // =========================================================
    @Override
    public String visitSentenciaIf(MiLenguajeParser.SentenciaIfContext ctx) {
        boolean tieneElse = ctx.bloque().size() > 1;
        imprimir("IF" + (tieneElse ? " / ELSE" : ""));

        // Visitar la condición
        imprimir("  CONDICION:");
        nivel++;
        visit(ctx.expresion());
        nivel--;

        // Visitar el bloque del IF
        imprimir("  ENTONCES:");
        nivel++;
        visit(ctx.bloque(0));
        nivel--;

        // Visitar el bloque del ELSE (si existe)
        if (tieneElse) {
            imprimir("  SINO:");
            nivel++;
            visit(ctx.bloque(1));
            nivel--;
        }
        return null;
    }

    // =========================================================
    //  REGLA: sentenciaWhile
    //  Ejemplo: while (x < 100) { ... }
    //  ctx.expresion() → condición del bucle
    //  ctx.bloque()    → cuerpo del bucle
    // =========================================================
    @Override
    public String visitSentenciaWhile(MiLenguajeParser.SentenciaWhileContext ctx) {
        imprimir("WHILE");

        imprimir("  CONDICION:");
        nivel++;
        visit(ctx.expresion());
        nivel--;

        imprimir("  CUERPO:");
        nivel++;
        visit(ctx.bloque());
        nivel--;
        return null;
    }

    // =========================================================
    //  REGLA: bloque
    //  Un bloque agrupa sentencias entre llaves { ... }
    //  ctx.sentencia() → lista de sentencias dentro del bloque
    // =========================================================
    @Override
    public String visitBloque(MiLenguajeParser.BloqueContext ctx) {
        imprimir("BLOQUE { " + ctx.sentencia().size() + " sentencia(s) }");
        nivel++;
        visitChildren(ctx);
        nivel--;
        return null;
    }

    // =========================================================
    //  EXPRESIONES
    //
    //  Cada tipo de expresión tiene su propio método visitor
    //  gracias a las etiquetas (#nombre) en la gramática.
    //
    //  ctx.expresion(0) → operando izquierdo
    //  ctx.expresion(1) → operando derecho
    // =========================================================

    @Override
    public String visitExprOr(MiLenguajeParser.ExprOrContext ctx) {
        imprimir("OR (||)");
        nivel++;
        visit(ctx.expresion(0));
        visit(ctx.expresion(1));
        nivel--;
        return null;
    }

    @Override
    public String visitExprAnd(MiLenguajeParser.ExprAndContext ctx) {
        imprimir("AND (&&)");
        nivel++;
        visit(ctx.expresion(0));
        visit(ctx.expresion(1));
        nivel--;
        return null;
    }

    @Override
    public String visitExprIgualdad(MiLenguajeParser.ExprIgualdadContext ctx) {
        // ctx.getChild(1) obtiene el operador (== o !=)
        imprimir("IGUALDAD (" + ctx.getChild(1).getText() + ")");
        nivel++;
        visit(ctx.expresion(0));
        visit(ctx.expresion(1));
        nivel--;
        return null;
    }

    @Override
    public String visitExprRelacional(MiLenguajeParser.ExprRelacionalContext ctx) {
        imprimir("RELACIONAL (" + ctx.getChild(1).getText() + ")");
        nivel++;
        visit(ctx.expresion(0));
        visit(ctx.expresion(1));
        nivel--;
        return null;
    }

    @Override
    public String visitExprAditiva(MiLenguajeParser.ExprAditivaContext ctx) {
        imprimir("ARITMETICA (" + ctx.getChild(1).getText() + ")");
        nivel++;
        visit(ctx.expresion(0));
        visit(ctx.expresion(1));
        nivel--;
        return null;
    }

    @Override
    public String visitExprMultiplicativa(MiLenguajeParser.ExprMultiplicativaContext ctx) {
        imprimir("ARITMETICA (" + ctx.getChild(1).getText() + ")");
        nivel++;
        visit(ctx.expresion(0));
        visit(ctx.expresion(1));
        nivel--;
        return null;
    }

    @Override
    public String visitExprNot(MiLenguajeParser.ExprNotContext ctx) {
        imprimir("NOT (!)");
        nivel++;
        visit(ctx.expresion());
        nivel--;
        return null;
    }

    @Override
    public String visitExprNegativo(MiLenguajeParser.ExprNegativoContext ctx) {
        imprimir("NEGATIVO (-)");
        nivel++;
        visit(ctx.expresion());
        nivel--;
        return null;
    }

    @Override
    public String visitExprAgrupada(MiLenguajeParser.ExprAgrupadaContext ctx) {
        imprimir("AGRUPADA ( ... )");
        nivel++;
        visit(ctx.expresion());
        nivel--;
        return null;
    }

    // =========================================================
    //  LITERALES (hojas del árbol — no tienen hijos)
    //  Estos son los valores "finales" de las expresiones.
    // =========================================================

    @Override
    public String visitExprEntero(MiLenguajeParser.ExprEnteroContext ctx) {
        imprimir("ENTERO = " + ctx.INTEGER().getText());
        return ctx.INTEGER().getText();
    }

    @Override
    public String visitExprDecimal(MiLenguajeParser.ExprDecimalContext ctx) {
        imprimir("DECIMAL = " + ctx.DECIMAL().getText());
        return ctx.DECIMAL().getText();
    }

    @Override
    public String visitExprCaracter(MiLenguajeParser.ExprCaracterContext ctx) {
        imprimir("CARACTER = " + ctx.CHARACTER().getText());
        return ctx.CHARACTER().getText();
    }

    @Override
    public String visitExprCadena(MiLenguajeParser.ExprCadenaContext ctx) {
        imprimir("CADENA = " + ctx.CADENA().getText());
        return ctx.CADENA().getText();
    }

    @Override
    public String visitExprVerdadero(MiLenguajeParser.ExprVerdaderoContext ctx) {
        imprimir("BOOLEANO = true");
        return "true";
    }

    @Override
    public String visitExprFalso(MiLenguajeParser.ExprFalsoContext ctx) {
        imprimir("BOOLEANO = false");
        return "false";
    }

    @Override
    public String visitExprIdentificador(MiLenguajeParser.ExprIdentificadorContext ctx) {
        imprimir("VARIABLE = " + ctx.ID().getText());
        return ctx.ID().getText();
    }
}
