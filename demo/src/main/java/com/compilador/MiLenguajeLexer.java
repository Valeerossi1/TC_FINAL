// Generated from com\compilador\MiLenguaje.g4 by ANTLR 4.9.3
package com.compilador;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MiLenguajeLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		PA=1, PC=2, CA=3, CC=4, LA=5, LC=6, PYC=7, COMA=8, IGUAL=9, EQL=10, DISTINTO=11, 
		MAYOR_IGUAL=12, MENOR_IGUAL=13, MAYOR=14, MENOR=15, SHIFT_L=16, SUM=17, 
		RES=18, MUL=19, DIV=20, MOD=21, OR=22, AND=23, NOT=24, FOR=25, WHILE=26, 
		IF=27, ELSE=28, RETURN=29, BREAK=30, CONTINUE=31, INT=32, FLOAT=33, DOUBLE=34, 
		CHAR=35, STRING_TYPE=36, BOOL=37, VOID=38, VERDADERO=39, FALSO=40, COUT=41, 
		ID=42, INTEGER=43, DECIMAL=44, CHARACTER=45, CADENA=46, COMENTARIO_LINEA=47, 
		COMENTARIO_BLOQUE=48, WS=49, OTRO=50;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"LETRA", "DIGITO", "PA", "PC", "CA", "CC", "LA", "LC", "PYC", "COMA", 
			"IGUAL", "EQL", "DISTINTO", "MAYOR_IGUAL", "MENOR_IGUAL", "MAYOR", "MENOR", 
			"SHIFT_L", "SUM", "RES", "MUL", "DIV", "MOD", "OR", "AND", "NOT", "FOR", 
			"WHILE", "IF", "ELSE", "RETURN", "BREAK", "CONTINUE", "INT", "FLOAT", 
			"DOUBLE", "CHAR", "STRING_TYPE", "BOOL", "VOID", "VERDADERO", "FALSO", 
			"COUT", "ID", "INTEGER", "DECIMAL", "CHARACTER", "CADENA", "COMENTARIO_LINEA", 
			"COMENTARIO_BLOQUE", "WS", "OTRO"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'['", "']'", "'{'", "'}'", "';'", "','", "'='", 
			"'=='", "'!='", "'>='", "'<='", "'>'", "'<'", "'<<'", "'+'", "'-'", "'*'", 
			"'/'", "'%'", "'||'", "'&&'", "'!'", "'for'", "'while'", "'if'", "'else'", 
			"'return'", "'break'", "'continue'", "'int'", "'float'", "'double'", 
			"'char'", "'string'", "'bool'", "'void'", "'true'", "'false'", "'cout'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "PA", "PC", "CA", "CC", "LA", "LC", "PYC", "COMA", "IGUAL", "EQL", 
			"DISTINTO", "MAYOR_IGUAL", "MENOR_IGUAL", "MAYOR", "MENOR", "SHIFT_L", 
			"SUM", "RES", "MUL", "DIV", "MOD", "OR", "AND", "NOT", "FOR", "WHILE", 
			"IF", "ELSE", "RETURN", "BREAK", "CONTINUE", "INT", "FLOAT", "DOUBLE", 
			"CHAR", "STRING_TYPE", "BOOL", "VOID", "VERDADERO", "FALSO", "COUT", 
			"ID", "INTEGER", "DECIMAL", "CHARACTER", "CADENA", "COMENTARIO_LINEA", 
			"COMENTARIO_BLOQUE", "WS", "OTRO"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public MiLenguajeLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "MiLenguaje.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\64\u0156\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3"+
		"\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\16\3\17\3"+
		"\17\3\17\3\20\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3"+
		"\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3\32\3"+
		"\33\3\33\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3"+
		"\36\3\37\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\3"+
		"\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3$\3$\3$\3$\3$\3$\3%\3"+
		"%\3%\3%\3%\3%\3%\3&\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3"+
		"(\3(\3)\3)\3)\3)\3)\3*\3*\3*\3*\3*\3+\3+\3+\3+\3+\3+\3,\3,\3,\3,\3,\3"+
		"-\3-\5-\u0108\n-\3-\3-\3-\7-\u010d\n-\f-\16-\u0110\13-\3.\6.\u0113\n."+
		"\r.\16.\u0114\3/\6/\u0118\n/\r/\16/\u0119\3/\3/\6/\u011e\n/\r/\16/\u011f"+
		"\3\60\3\60\3\60\3\60\5\60\u0126\n\60\3\60\3\60\3\61\3\61\3\61\3\61\7\61"+
		"\u012e\n\61\f\61\16\61\u0131\13\61\3\61\3\61\3\62\3\62\3\62\3\62\7\62"+
		"\u0139\n\62\f\62\16\62\u013c\13\62\3\62\3\62\3\63\3\63\3\63\3\63\7\63"+
		"\u0144\n\63\f\63\16\63\u0147\13\63\3\63\3\63\3\63\3\63\3\63\3\64\6\64"+
		"\u014f\n\64\r\64\16\64\u0150\3\64\3\64\3\65\3\65\3\u0145\2\66\3\2\5\2"+
		"\7\3\t\4\13\5\r\6\17\7\21\b\23\t\25\n\27\13\31\f\33\r\35\16\37\17!\20"+
		"#\21%\22\'\23)\24+\25-\26/\27\61\30\63\31\65\32\67\339\34;\35=\36?\37"+
		"A C!E\"G#I$K%M&O\'Q(S)U*W+Y,[-]._/a\60c\61e\62g\63i\64\3\2\b\4\2C\\c|"+
		"\3\2\62;\5\2\f\f\17\17))\5\2\f\f\17\17$$\4\2\f\f\17\17\5\2\13\f\17\17"+
		"\"\"\2\u0160\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2"+
		"\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2"+
		"\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2"+
		"\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2"+
		"\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2"+
		"\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2"+
		"\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W"+
		"\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2"+
		"\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\3k\3\2\2\2\5m\3\2\2\2\7o\3\2\2\2"+
		"\tq\3\2\2\2\13s\3\2\2\2\ru\3\2\2\2\17w\3\2\2\2\21y\3\2\2\2\23{\3\2\2\2"+
		"\25}\3\2\2\2\27\177\3\2\2\2\31\u0081\3\2\2\2\33\u0084\3\2\2\2\35\u0087"+
		"\3\2\2\2\37\u008a\3\2\2\2!\u008d\3\2\2\2#\u008f\3\2\2\2%\u0091\3\2\2\2"+
		"\'\u0094\3\2\2\2)\u0096\3\2\2\2+\u0098\3\2\2\2-\u009a\3\2\2\2/\u009c\3"+
		"\2\2\2\61\u009e\3\2\2\2\63\u00a1\3\2\2\2\65\u00a4\3\2\2\2\67\u00a6\3\2"+
		"\2\29\u00aa\3\2\2\2;\u00b0\3\2\2\2=\u00b3\3\2\2\2?\u00b8\3\2\2\2A\u00bf"+
		"\3\2\2\2C\u00c5\3\2\2\2E\u00ce\3\2\2\2G\u00d2\3\2\2\2I\u00d8\3\2\2\2K"+
		"\u00df\3\2\2\2M\u00e4\3\2\2\2O\u00eb\3\2\2\2Q\u00f0\3\2\2\2S\u00f5\3\2"+
		"\2\2U\u00fa\3\2\2\2W\u0100\3\2\2\2Y\u0107\3\2\2\2[\u0112\3\2\2\2]\u0117"+
		"\3\2\2\2_\u0121\3\2\2\2a\u0129\3\2\2\2c\u0134\3\2\2\2e\u013f\3\2\2\2g"+
		"\u014e\3\2\2\2i\u0154\3\2\2\2kl\t\2\2\2l\4\3\2\2\2mn\t\3\2\2n\6\3\2\2"+
		"\2op\7*\2\2p\b\3\2\2\2qr\7+\2\2r\n\3\2\2\2st\7]\2\2t\f\3\2\2\2uv\7_\2"+
		"\2v\16\3\2\2\2wx\7}\2\2x\20\3\2\2\2yz\7\177\2\2z\22\3\2\2\2{|\7=\2\2|"+
		"\24\3\2\2\2}~\7.\2\2~\26\3\2\2\2\177\u0080\7?\2\2\u0080\30\3\2\2\2\u0081"+
		"\u0082\7?\2\2\u0082\u0083\7?\2\2\u0083\32\3\2\2\2\u0084\u0085\7#\2\2\u0085"+
		"\u0086\7?\2\2\u0086\34\3\2\2\2\u0087\u0088\7@\2\2\u0088\u0089\7?\2\2\u0089"+
		"\36\3\2\2\2\u008a\u008b\7>\2\2\u008b\u008c\7?\2\2\u008c \3\2\2\2\u008d"+
		"\u008e\7@\2\2\u008e\"\3\2\2\2\u008f\u0090\7>\2\2\u0090$\3\2\2\2\u0091"+
		"\u0092\7>\2\2\u0092\u0093\7>\2\2\u0093&\3\2\2\2\u0094\u0095\7-\2\2\u0095"+
		"(\3\2\2\2\u0096\u0097\7/\2\2\u0097*\3\2\2\2\u0098\u0099\7,\2\2\u0099,"+
		"\3\2\2\2\u009a\u009b\7\61\2\2\u009b.\3\2\2\2\u009c\u009d\7\'\2\2\u009d"+
		"\60\3\2\2\2\u009e\u009f\7~\2\2\u009f\u00a0\7~\2\2\u00a0\62\3\2\2\2\u00a1"+
		"\u00a2\7(\2\2\u00a2\u00a3\7(\2\2\u00a3\64\3\2\2\2\u00a4\u00a5\7#\2\2\u00a5"+
		"\66\3\2\2\2\u00a6\u00a7\7h\2\2\u00a7\u00a8\7q\2\2\u00a8\u00a9\7t\2\2\u00a9"+
		"8\3\2\2\2\u00aa\u00ab\7y\2\2\u00ab\u00ac\7j\2\2\u00ac\u00ad\7k\2\2\u00ad"+
		"\u00ae\7n\2\2\u00ae\u00af\7g\2\2\u00af:\3\2\2\2\u00b0\u00b1\7k\2\2\u00b1"+
		"\u00b2\7h\2\2\u00b2<\3\2\2\2\u00b3\u00b4\7g\2\2\u00b4\u00b5\7n\2\2\u00b5"+
		"\u00b6\7u\2\2\u00b6\u00b7\7g\2\2\u00b7>\3\2\2\2\u00b8\u00b9\7t\2\2\u00b9"+
		"\u00ba\7g\2\2\u00ba\u00bb\7v\2\2\u00bb\u00bc\7w\2\2\u00bc\u00bd\7t\2\2"+
		"\u00bd\u00be\7p\2\2\u00be@\3\2\2\2\u00bf\u00c0\7d\2\2\u00c0\u00c1\7t\2"+
		"\2\u00c1\u00c2\7g\2\2\u00c2\u00c3\7c\2\2\u00c3\u00c4\7m\2\2\u00c4B\3\2"+
		"\2\2\u00c5\u00c6\7e\2\2\u00c6\u00c7\7q\2\2\u00c7\u00c8\7p\2\2\u00c8\u00c9"+
		"\7v\2\2\u00c9\u00ca\7k\2\2\u00ca\u00cb\7p\2\2\u00cb\u00cc\7w\2\2\u00cc"+
		"\u00cd\7g\2\2\u00cdD\3\2\2\2\u00ce\u00cf\7k\2\2\u00cf\u00d0\7p\2\2\u00d0"+
		"\u00d1\7v\2\2\u00d1F\3\2\2\2\u00d2\u00d3\7h\2\2\u00d3\u00d4\7n\2\2\u00d4"+
		"\u00d5\7q\2\2\u00d5\u00d6\7c\2\2\u00d6\u00d7\7v\2\2\u00d7H\3\2\2\2\u00d8"+
		"\u00d9\7f\2\2\u00d9\u00da\7q\2\2\u00da\u00db\7w\2\2\u00db\u00dc\7d\2\2"+
		"\u00dc\u00dd\7n\2\2\u00dd\u00de\7g\2\2\u00deJ\3\2\2\2\u00df\u00e0\7e\2"+
		"\2\u00e0\u00e1\7j\2\2\u00e1\u00e2\7c\2\2\u00e2\u00e3\7t\2\2\u00e3L\3\2"+
		"\2\2\u00e4\u00e5\7u\2\2\u00e5\u00e6\7v\2\2\u00e6\u00e7\7t\2\2\u00e7\u00e8"+
		"\7k\2\2\u00e8\u00e9\7p\2\2\u00e9\u00ea\7i\2\2\u00eaN\3\2\2\2\u00eb\u00ec"+
		"\7d\2\2\u00ec\u00ed\7q\2\2\u00ed\u00ee\7q\2\2\u00ee\u00ef\7n\2\2\u00ef"+
		"P\3\2\2\2\u00f0\u00f1\7x\2\2\u00f1\u00f2\7q\2\2\u00f2\u00f3\7k\2\2\u00f3"+
		"\u00f4\7f\2\2\u00f4R\3\2\2\2\u00f5\u00f6\7v\2\2\u00f6\u00f7\7t\2\2\u00f7"+
		"\u00f8\7w\2\2\u00f8\u00f9\7g\2\2\u00f9T\3\2\2\2\u00fa\u00fb\7h\2\2\u00fb"+
		"\u00fc\7c\2\2\u00fc\u00fd\7n\2\2\u00fd\u00fe\7u\2\2\u00fe\u00ff\7g\2\2"+
		"\u00ffV\3\2\2\2\u0100\u0101\7e\2\2\u0101\u0102\7q\2\2\u0102\u0103\7w\2"+
		"\2\u0103\u0104\7v\2\2\u0104X\3\2\2\2\u0105\u0108\5\3\2\2\u0106\u0108\7"+
		"a\2\2\u0107\u0105\3\2\2\2\u0107\u0106\3\2\2\2\u0108\u010e\3\2\2\2\u0109"+
		"\u010d\5\3\2\2\u010a\u010d\5\5\3\2\u010b\u010d\7a\2\2\u010c\u0109\3\2"+
		"\2\2\u010c\u010a\3\2\2\2\u010c\u010b\3\2\2\2\u010d\u0110\3\2\2\2\u010e"+
		"\u010c\3\2\2\2\u010e\u010f\3\2\2\2\u010fZ\3\2\2\2\u0110\u010e\3\2\2\2"+
		"\u0111\u0113\5\5\3\2\u0112\u0111\3\2\2\2\u0113\u0114\3\2\2\2\u0114\u0112"+
		"\3\2\2\2\u0114\u0115\3\2\2\2\u0115\\\3\2\2\2\u0116\u0118\5\5\3\2\u0117"+
		"\u0116\3\2\2\2\u0118\u0119\3\2\2\2\u0119\u0117\3\2\2\2\u0119\u011a\3\2"+
		"\2\2\u011a\u011b\3\2\2\2\u011b\u011d\7\60\2\2\u011c\u011e\5\5\3\2\u011d"+
		"\u011c\3\2\2\2\u011e\u011f\3\2\2\2\u011f\u011d\3\2\2\2\u011f\u0120\3\2"+
		"\2\2\u0120^\3\2\2\2\u0121\u0125\7)\2\2\u0122\u0126\n\4\2\2\u0123\u0124"+
		"\7^\2\2\u0124\u0126\13\2\2\2\u0125\u0122\3\2\2\2\u0125\u0123\3\2\2\2\u0126"+
		"\u0127\3\2\2\2\u0127\u0128\7)\2\2\u0128`\3\2\2\2\u0129\u012f\7$\2\2\u012a"+
		"\u012e\n\5\2\2\u012b\u012c\7^\2\2\u012c\u012e\13\2\2\2\u012d\u012a\3\2"+
		"\2\2\u012d\u012b\3\2\2\2\u012e\u0131\3\2\2\2\u012f\u012d\3\2\2\2\u012f"+
		"\u0130\3\2\2\2\u0130\u0132\3\2\2\2\u0131\u012f\3\2\2\2\u0132\u0133\7$"+
		"\2\2\u0133b\3\2\2\2\u0134\u0135\7\61\2\2\u0135\u0136\7\61\2\2\u0136\u013a"+
		"\3\2\2\2\u0137\u0139\n\6\2\2\u0138\u0137\3\2\2\2\u0139\u013c\3\2\2\2\u013a"+
		"\u0138\3\2\2\2\u013a\u013b\3\2\2\2\u013b\u013d\3\2\2\2\u013c\u013a\3\2"+
		"\2\2\u013d\u013e\b\62\2\2\u013ed\3\2\2\2\u013f\u0140\7\61\2\2\u0140\u0141"+
		"\7,\2\2\u0141\u0145\3\2\2\2\u0142\u0144\13\2\2\2\u0143\u0142\3\2\2\2\u0144"+
		"\u0147\3\2\2\2\u0145\u0146\3\2\2\2\u0145\u0143\3\2\2\2\u0146\u0148\3\2"+
		"\2\2\u0147\u0145\3\2\2\2\u0148\u0149\7,\2\2\u0149\u014a\7\61\2\2\u014a"+
		"\u014b\3\2\2\2\u014b\u014c\b\63\2\2\u014cf\3\2\2\2\u014d\u014f\t\7\2\2"+
		"\u014e\u014d\3\2\2\2\u014f\u0150\3\2\2\2\u0150\u014e\3\2\2\2\u0150\u0151"+
		"\3\2\2\2\u0151\u0152\3\2\2\2\u0152\u0153\b\64\2\2\u0153h\3\2\2\2\u0154"+
		"\u0155\13\2\2\2\u0155j\3\2\2\2\17\2\u0107\u010c\u010e\u0114\u0119\u011f"+
		"\u0125\u012d\u012f\u013a\u0145\u0150\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}