grammar MiLenguaje;

// =====================================================================
//  ANÁLISIS SINTÁCTICO - Mini Lenguaje tipo C++
//  Proyecto educativo de Técnicas de Compilación
//
//  Un archivo .g4 contiene DOS tipos de reglas:
//    - Reglas del PARSER  (en minúscula): definen la ESTRUCTURA
//    - Reglas del LEXER   (en MAYÚSCULA): definen los TOKENS
//
//  ANTLR4 genera automáticamente el Lexer y el Parser a partir
//  de este archivo. ¡No hay que escribirlos a mano!
// =====================================================================


// =====================================================================
//  REGLAS DEL PARSER (Análisis Sintáctico)
//
//  Estas reglas definen la GRAMÁTICA del lenguaje:
//  qué combinaciones de tokens son válidas.
//
//  Ejemplo: una declaración válida es:
//    tipo  ID  '='  expresión  ';'
//    int   x   =    10 + 5    ;
// =====================================================================

// REGLA INICIAL: Un programa es una secuencia de sentencias
// La regla 'programa' es el punto de entrada del parser.
// EOF marca el fin del archivo de entrada.
programa
    : sentencia* EOF
    ;

// =====================================================================
//  SENTENCIAS
//  Una sentencia es la "instrucción básica" del lenguaje.
//  Cada tipo de sentencia termina en ';' o en un bloque '{...}'.
// =====================================================================

// Una sentencia puede ser cualquiera de estos tipos.
// ANTLR probará cada alternativa en orden hasta encontrar una que encaje.
sentencia
    : declaracion        // int x = 5;
    | asignacion         // x = x + 1;
    | sentenciaCout      // cout << x;
    | sentenciaIf        // if (x > 0) { ... }
    | sentenciaWhile     // while (x < 10) { ... }
    | bloque             // { ... }
    ;

// DECLARACIÓN DE VARIABLE: tipo nombre [= expresión] ;
// El '= expresión' es OPCIONAL (marcado con ?)
// Ejemplos válidos:
//   int x;           <- sin valor inicial
//   int x = 5;       <- con valor inicial
//   float pi = 3.14;
declaracion
    : tipo ID (IGUAL expresion)? PYC
    ;

// TIPOS DE DATOS disponibles en el mini lenguaje
tipo
    : INT          // Números enteros:   int x = 5;
    | FLOAT        // Números decimales: float pi = 3.14;
    | DOUBLE       // Decimal preciso:   double d = 3.14159;
    | CHAR         // Caracteres:        char c = 'A';
    | STRING_TYPE  // Cadenas:           string s = "hola";
    | BOOL         // Booleanos:         bool b = true;
    | VOID         // Sin valor (para futuras funciones)
    ;

// ASIGNACIÓN: nombre = expresión ;
// ATENCIÓN: el parser distingue declaración de asignación
// porque la declaración empieza con un TIPO y esta con un ID.
asignacion
    : ID IGUAL expresion PYC
    ;

// COUT: salida por pantalla
// Sintaxis: cout << expresión ;
// El operador << es "insertar en", viene de C++ streams
sentenciaCout
    : COUT SHIFT_L expresion PYC
    ;

// IF-ELSE: selección condicional
// El bloque 'else' es OPCIONAL (marcado con ?)
// Sintaxis:
//   if (condición) { ... }
//   if (condición) { ... } else { ... }
sentenciaIf
    : IF PA expresion PC bloque (ELSE bloque)?
    ;

// WHILE: bucle mientras la condición sea verdadera
// Sintaxis: while (condición) { ... }
sentenciaWhile
    : WHILE PA expresion PC bloque
    ;

// BLOQUE: secuencia de sentencias entre llaves
// Un bloque puede estar vacío: {}
// Ejemplo: { int x = 1; x = x + 1; cout << x; }
bloque
    : LA sentencia* LC
    ;


// =====================================================================
//  EXPRESIONES (con precedencia de operadores)
//
//  La PRECEDENCIA define qué operador se evalúa primero.
//  En ANTLR4, las alternativas MÁS ARRIBA tienen MENOR precedencia.
//  Las MÁS ABAJO tienen MAYOR precedencia.
//
//  Esto es equivalente a la precedencia matemática estándar:
//    2 + 3 * 4  →  2 + (3 * 4) = 14   (no  (2+3) * 4 = 20)
//
//  ANTLR4 maneja la recursividad izquierda AUTOMÁTICAMENTE.
//  No es necesario transformar la gramática manualmente.
//
//  Las etiquetas (#nombre) generan métodos separados en el Visitor,
//  lo que facilita manejar cada tipo de expresión de forma distinta.
// =====================================================================

expresion
    //------------------------------------------------------------------
    // Nivel 6 — OR lógico (menor precedencia de todos)
    // a || b : verdadero si al menos uno es verdadero
    : expresion OR expresion                                              # exprOr

    //------------------------------------------------------------------
    // Nivel 5 — AND lógico
    // a && b : verdadero solo si ambos son verdaderos
    | expresion AND expresion                                             # exprAnd

    //------------------------------------------------------------------
    // Nivel 4 — Igualdad y desigualdad
    // a == b : comprueba si son iguales
    // a != b : comprueba si son distintos
    | expresion (EQL | DISTINTO) expresion                               # exprIgualdad

    //------------------------------------------------------------------
    // Nivel 3 — Comparaciones relacionales
    // a > b, a < b, a >= b, a <= b
    | expresion (MAYOR | MENOR | MAYOR_IGUAL | MENOR_IGUAL) expresion   # exprRelacional

    //------------------------------------------------------------------
    // Nivel 2 — Suma y resta
    // a + b, a - b
    | expresion (SUM | RES) expresion                                    # exprAditiva

    //------------------------------------------------------------------
    // Nivel 1 — Multiplicación, división, módulo (mayor precedencia binaria)
    // a * b, a / b, a % b
    | expresion (MUL | DIV | MOD) expresion                             # exprMultiplicativa

    //------------------------------------------------------------------
    // Operadores UNARIOS (actúan sobre un solo operando)
    // !verdadero  →  falso
    | NOT expresion                                                       # exprNot

    // -5, -(x + 1)
    | RES expresion                                                       # exprNegativo

    //------------------------------------------------------------------
    // EXPRESIONES PRIMARIAS (mayor precedencia — se evalúan primero)

    // Paréntesis: agrupan y fuerzan el orden de evaluación
    // (2 + 3) * 4  →  20
    | PA expresion PC                                                     # exprAgrupada

    // Literales numéricos
    | INTEGER                                                             # exprEntero
    | DECIMAL                                                             # exprDecimal

    // Literal de carácter
    | CHARACTER                                                           # exprCaracter

    // Literal de cadena de texto
    | CADENA                                                              # exprCadena

    // Literales booleanos
    | VERDADERO                                                           # exprVerdadero
    | FALSO                                                               # exprFalso

    // Variable: referencia a un identificador declarado
    | ID                                                                  # exprIdentificador
    ;


// =====================================================================
//  REGLAS DEL LEXER (Análisis Léxico)
//
//  Estas reglas definen qué son los TOKENS (unidades básicas).
//  El lexer lee el texto carácter por carácter y agrupa caracteres
//  en tokens que luego el parser puede usar.
//
//  REGLA IMPORTANTE: si dos reglas del lexer pueden coincidir
//  con la misma entrada, ANTLR elige:
//    1. La que coincide con el texto MÁS LARGO
//    2. Si mismo largo, la que aparece PRIMERO en el archivo
//
//  Por eso las PALABRAS CLAVE van antes que ID:
//  'int' podría ser tanto INT como ID, pero INT está primero.
// =====================================================================

// --- FRAGMENTOS auxiliares ---
// Los fragmentos NO son tokens por sí mismos.
// Solo pueden usarse como partes de otras reglas del lexer.
fragment LETRA  : [A-Za-z];
fragment DIGITO : [0-9];


// --- DELIMITADORES Y PUNTUACIÓN ---
PA   : '(' ;    // Paréntesis abre
PC   : ')' ;    // Paréntesis cierra
CA   : '[' ;    // Corchete abre  (reservado para arrays futuros)
CC   : ']' ;    // Corchete cierra
LA   : '{' ;    // Llave abre
LC   : '}' ;    // Llave cierra

PYC  : ';' ;    // Punto y coma — termina cada sentencia
COMA : ',' ;    // Coma — separador (para futuras listas de parámetros)


// --- OPERADORES DE ASIGNACIÓN ---
// IMPORTANTE: '==' debe ir ANTES de '=' para que ANTLR use la regla
// correcta. ANTLR elige el token más largo, así que '==' siempre
// gana sobre '=' cuando el texto es '=='.
IGUAL : '=' ;   // Asignación: x = 5


// --- OPERADORES DE COMPARACIÓN ---
// Orden: tokens de 2 caracteres primero (aunque ANTLR los detecta por longitud)
EQL         : '==' ;    // Igual a:         x == y
DISTINTO    : '!=' ;    // Distinto de:     x != y
MAYOR_IGUAL : '>=' ;    // Mayor o igual:   x >= y
MENOR_IGUAL : '<=' ;    // Menor o igual:   x <= y
MAYOR       : '>'  ;    // Mayor que:       x > y
MENOR       : '<'  ;    // Menor que:       x < y


// --- OPERADOR DE SALIDA (cout) ---
// '<<' debe definirse ANTES de '<' para que ANTLR no confunda
// 'cout << x' con 'cout < < x' (dos tokens separados).
// (ANTLR usa el más largo, pero ser explícito ayuda a la claridad)
SHIFT_L : '<<' ;        // Operador de inserción para cout


// --- OPERADORES ARITMÉTICOS ---
SUM : '+' ;     // Suma
RES : '-' ;     // Resta / negativo unario
MUL : '*' ;     // Multiplicación
DIV : '/' ;     // División
MOD : '%' ;     // Módulo (resto de la división entera)


// --- OPERADORES LÓGICOS ---
OR  : '||' ;    // OR lógico:  a || b  (verdadero si alguno es true)
AND : '&&' ;    // AND lógico: a && b  (verdadero solo si ambos son true)
NOT : '!'  ;    // NOT lógico: !a      (invierte el valor booleano)


// --- PALABRAS CLAVE DE CONTROL DE FLUJO ---
// Estas palabras NO pueden usarse como nombres de variables.
// Si el usuario escribe 'while', el lexer produce el token WHILE,
// no el token ID, porque WHILE está definido ANTES que ID.
FOR    : 'for'    ;     // Para futuras extensiones
WHILE  : 'while'  ;     // Bucle mientras
IF     : 'if'     ;     // Selección condicional
ELSE   : 'else'   ;     // Alternativa del if
RETURN : 'return' ;     // Para futuras funciones


// --- PALABRAS CLAVE DE TIPOS ---
// Tipos de datos que el mini lenguaje soporta.
INT         : 'int'    ;    // Entero
FLOAT       : 'float'  ;    // Decimal de precisión simple
DOUBLE      : 'double' ;    // Decimal de doble precisión
CHAR        : 'char'   ;    // Carácter
STRING_TYPE : 'string' ;    // Cadena de texto (estilo C++)
BOOL        : 'bool'   ;    // Booleano
VOID        : 'void'   ;    // Sin tipo de retorno


// --- PALABRAS CLAVE DE VALORES ---
// 'true' y 'false' son PALABRAS CLAVE, no identificadores.
VERDADERO : 'true'  ;
FALSO     : 'false' ;


// --- INSTRUCCIÓN DE SALIDA ---
COUT : 'cout' ;     // cout (console output) de C++


// --- IDENTIFICADORES ---
// DEBE ir DESPUÉS de todas las palabras clave.
// Un identificador empieza con letra o '_', seguido de letras, dígitos o '_'.
// Ejemplos válidos:  x, mi_variable, contador1, _privado
// Ejemplos inválidos: 1x (empieza con número), int (es palabra clave)
ID : (LETRA | '_') (LETRA | DIGITO | '_')* ;


// --- LITERALES (valores escritos directamente en el código) ---

// Número entero: secuencia de dígitos
// Ejemplos: 0, 42, 1000
INTEGER : DIGITO+ ;

// Número decimal: dígitos, punto, más dígitos
// Ejemplos: 3.14, 0.5, 100.0
DECIMAL : DIGITO+ '.' DIGITO+ ;

// Carácter: un carácter entre comillas simples
// Ejemplos: 'A', '7', '\n' (secuencia de escape)
CHARACTER : '\'' (~['\r\n] | '\\' .) '\'' ;

// Cadena de texto: caracteres entre comillas dobles
// Ejemplos: "hola", "Hola mundo", "valor: 42"
CADENA : '"' (~["\r\n] | '\\' .)* '"' ;


// --- COMENTARIOS (se ignoran durante el análisis) ---
// La directiva '-> skip' le dice a ANTLR que descarte estos tokens.
// El parser nunca los verá.

// Comentario de línea: desde '//' hasta el fin de línea
COMENTARIO_LINEA  : '//' ~[\r\n]*      -> skip ;

// Comentario de bloque: desde '/*' hasta '*/'
// El '?' hace que '.*' sea NO-GREEDY (mínimo posible)
// Sin '?': '/* a */ b */' consumiría hasta el último '*/'
// Con '?': '/* a */ b */' consume solo hasta el primer '*/'
COMENTARIO_BLOQUE : '/*' .*? '*/'      -> skip ;


// --- ESPACIOS EN BLANCO (se ignoran) ---
// Espacios, tabulaciones y saltos de línea se descartan.
WS : [ \r\n\t]+ -> skip ;


// --- CARÁCTER NO RECONOCIDO ---
// Si ninguna regla anterior coincide, OTRO captura el carácter.
// Esto NO lanza un error en el lexer, pero sí en el parser,
// porque OTRO no aparece en ninguna regla sintáctica.
// Útil para detectar y reportar errores léxicos con más control.
OTRO : . ;
