package com.compilador;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.gui.TreeViewer;
import javax.swing.*;
import java.util.Arrays;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Punto de entrada del compilador educativo.
 *
 * Este programa realiza DOS fases del análisis:
 *   1. ANÁLISIS LÉXICO  — convierte el texto en tokens
 *   2. ANÁLISIS SINTÁCTICO — verifica que los tokens forman
 *      estructuras válidas según la gramática
 *
 * Para ejecutar:
 *   java -jar demo-1.0-jar-with-dependencies.jar <archivo.txt>
 */
public class App {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java -jar demo-1.0-jar-with-dependencies.jar <archivo.txt>");
            System.exit(1);
        }

        try {
            // Cargar el archivo de texto como un stream de caracteres
            CharStream input = CharStreams.fromFileName(args[0]);
            System.out.println("Analizando archivo: " + args[0]);
            System.out.println("=".repeat(65));

            // =========================================================
            //  FASE 1: ANÁLISIS LÉXICO
            //
            //  El Lexer lee los caracteres del archivo y los agrupa
            //  en unidades con significado llamadas TOKENS.
            //
            //  Ejemplo:
            //    "int x = 5 + 3 ;" → [INT] [ID:x] [IGUAL] [INTEGER:5]
            //                          [SUM] [INTEGER:3] [PYC]
            // =========================================================

            MiLenguajeLexer lexer = new MiLenguajeLexer(input);

            // Reemplazamos el manejador de errores por defecto del lexer.
            // Por defecto ANTLR imprime errores en System.err; aquí los
            // capturamos para mostrarlos de forma más clara.
            List<String> erroresLexicos = new ArrayList<>();
            lexer.removeErrorListeners();
            lexer.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer,
                                        Object offendingSymbol,
                                        int line, int charPositionInLine,
                                        String msg, RecognitionException e) {
                    erroresLexicos.add(
                        "  [Línea " + line + ":" + charPositionInLine + "] " + msg
                    );
                }
            });

            // fill() ejecuta el lexer y almacena TODOS los tokens en memoria.
            // Esto nos permite mostrarlos y luego reutilizarlos para el parser.
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tokens.fill();

            // Mostrar tabla de tokens
            System.out.println("\n=== FASE 1: ANÁLISIS LÉXICO ===\n");
            System.out.printf("  %-20s %-25s %-8s %-8s%n",
                              "TIPO DE TOKEN", "LEXEMA", "LÍNEA", "COLUMNA");
            System.out.println("  " + "-".repeat(63));

            for (Token token : tokens.getTokens()) {
                if (token.getType() == Token.EOF) continue;

                String tipo = MiLenguajeLexer.VOCABULARY.getSymbolicName(token.getType());
                // Si el tipo es null, probablemente es OTRO (char no reconocido)
                if (tipo == null) tipo = "DESCONOCIDO";

                System.out.printf("  %-20s %-25s %-8d %-8d%n",
                                  tipo,
                                  token.getText(),
                                  token.getLine(),
                                  token.getCharPositionInLine());
            }

            // Si hubo errores léxicos, reportar y detener
            if (!erroresLexicos.isEmpty()) {
                System.out.println("\n  ❌ ERRORES LÉXICOS:");
                for (String error : erroresLexicos) {
                    System.out.println(error);
                }
                System.out.println("\n  El análisis no puede continuar con errores léxicos.");
                return;
            }

            System.out.println("\n  ✅ Análisis léxico completado sin errores.");

            // =========================================================
            //  FASE 2: ANÁLISIS SINTÁCTICO (PARSING)
            //
            //  El Parser recibe los tokens y verifica que forman
            //  estructuras válidas según las REGLAS de la gramática.
            //
            //  Si la estructura es válida, construye un ÁRBOL DE PARSEO
            //  (Parse Tree) que representa la jerarquía del programa.
            //
            //  Ejemplo para "int x = 5 + 3;":
            //    programa
            //      sentencia
            //        declaracion
            //          tipo: INT
            //          ID: x
            //          expresion
            //            exprAditiva
            //              exprEntero: 5
            //              SUM
            //              exprEntero: 3
            // =========================================================

            System.out.println("\n=== FASE 2: ANÁLISIS SINTÁCTICO ===\n");

            // El parser necesita leer los tokens desde el principio.
            // reset() rebobina el stream al token 0.
            tokens.reset();

            MiLenguajeParser parser = new MiLenguajeParser(tokens);

            // Capturar errores sintácticos de forma personalizada
            List<String> erroresSintacticos = new ArrayList<>();
            parser.removeErrorListeners();
            parser.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer,
                                        Object offendingSymbol,
                                        int line, int charPositionInLine,
                                        String msg, RecognitionException e) {
                    String tokenErroneo = (offendingSymbol != null)
                                         ? "'" + offendingSymbol + "'"
                                         : "fin de archivo";
                    erroresSintacticos.add(
                        "  [Línea " + line + ":" + charPositionInLine + "] "
                        + "cerca de " + tokenErroneo + " → " + msg
                    );
                }
            });

            // Ejecutar el parser desde la REGLA INICIAL 'programa'.
            // Esta llamada construye el árbol de parseo (o reporta errores).
            MiLenguajeParser.ProgramaContext arbolParseo = parser.programa();

            // Verificar si hubo errores
            if (!erroresSintacticos.isEmpty()) {
                System.out.println("  ❌ ERRORES SINTÁCTICOS:");
                for (String error : erroresSintacticos) {
                    System.out.println(error);
                }
                System.out.println();
                System.out.println("  Pista: revisa que cada sentencia:");
                System.out.println("    - Termine con punto y coma ';'");
                System.out.println("    - Tenga paréntesis balanceados");
                System.out.println("    - Use tipos válidos (int, char, double, void)");
                return;
            }

            System.out.println("  ✅ Análisis sintáctico completado sin errores.");

            // =========================================================
            //  FASE 3: ANÁLISIS SEMÁNTICO
            //
            //  El SemanticVisitor recorre el MISMO árbol de parseo,
            //  pero esta vez verificando reglas de SIGNIFICADO:
            //    - ¿Las variables están declaradas antes de usarse?
            //    - ¿Los tipos son compatibles (asignaciones, operaciones)?
            //    - ¿Hay variables redeclaradas?
            //
            //  visit() dispara la recorrida: por cada nodo, ANTLR llama
            //  al método visitXxx que sobreescribimos en SemanticVisitor.
            // =========================================================

            System.out.println("\n=== FASE 3: ANÁLISIS SEMÁNTICO ===\n");

            SemanticVisitor analizadorSemantico = new SemanticVisitor();
            analizadorSemantico.visit(arbolParseo);

            List<String> erroresSemanticos = analizadorSemantico.getErrores();
            List<String> warningsSemanticos = analizadorSemantico.getWarnings();

            if (!erroresSemanticos.isEmpty()) {
                System.out.println("  ❌ ERRORES SEMÁNTICOS:");
                for (String error : erroresSemanticos) {
                    System.out.println("    " + error);
                }
            } else {
                System.out.println("  ✅ No se encontraron errores semánticos.");
            }

            if (!warningsSemanticos.isEmpty()) {
                System.out.println();
                System.out.println("  ⚠️  WARNINGS:");
                for (String warning : warningsSemanticos) {
                    System.out.println("    " + warning);
                }
            }

            // FASE 4: generamos el codigo de tres direcciones (TAC) si no hubo errores semanticos
            if (erroresSemanticos.isEmpty()) {
                System.out.println("\n=== FASE 4: CODIGO INTERMEDIO (TAC) ===\n");

                CodigoIntermedioVisitor generadorTAC = new CodigoIntermedioVisitor();
                generadorTAC.visit(arbolParseo);

                for (String instruccion : generadorTAC.getCodigo()) {
                    System.out.println("    " + instruccion);
                }
            } else {
                System.out.println("\n  No se genera codigo intermedio porque hay errores semanticos.");
            }

            System.out.println("\n" + "=".repeat(65));
            System.out.println("  Compilacion exitosa.");

            // =========================================================
            //  VISUALIZADOR GRÁFICO (Swing)
            //
            //  TreeViewer es la herramienta de depuración incluida en
            //  ANTLR4. Abre una ventana Swing con el árbol de parseo
            //  completo, interactivo y con zoom.
            //
            //  Se muestra DESPUÉS de la salida en consola para que
            //  el alumno pueda leer primero la salida de texto.
            // =========================================================

            System.out.println("\n  Abriendo visualizador grafico del arbol...");
            mostrarArbol(arbolParseo, parser);

        } catch (IOException e) {
            System.err.println("❌ No se pudo leer el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =========================================================
    //  ÁRBOL VISUAL — métodos auxiliares
    // =========================================================
    private static void mostrarArbol(ParseTree tree, Parser parser) {
        JFrame frame = new JFrame("Árbol Sintáctico");
        JPanel panel = new JPanel();
        TreeViewer viewer = new TreeViewer(Arrays.asList(parser.getRuleNames()), tree);
        viewer.setScale(1.5);
        panel.add(viewer);

        JScrollPane scrollPane = new JScrollPane(panel);
        frame.add(scrollPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
