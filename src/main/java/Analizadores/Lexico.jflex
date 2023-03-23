package Analizadores;
import java_cup.runtime.Symbol;
import Errores.Excepcion;
import java.util.ArrayList;

%%
/* 2. Configuraciones para el analisis (Opciones y Declaraciones) */
%{
    //Codigo de usuario en sintaxis java
    //Agregar clases, variables, arreglos, objetos etc...
    
    public ArrayList<Excepcion> Errores = new ArrayList();

%}

//Directivas
%class Lexico
%public 
%cup
%char
%column
%full
%line
%unicode

//Inicializar el contador de columna y fila con 1
%init{ 
    yyline = 1; 
    yychar = 1; 
%init}

//Expresiones regulares



PR_CONJ = "CONJ"
DOS_PUNTOS = ":"
GUION = "-"
MAYOR = ">"
COMA = ","
SEPARADOR = "~"
LLAVE_A = "{"
LLAVE_C = "}"
CONCATENACION = "."
DISYUNCION = "|"
CERRADURA_KLEENE = "*"
CERRADURA_POSITIVA = "+"
CERRADURA_BOOLEANA = "?"
PORCENTAJE = "%"
PUNTO_Y_COMA = ";"

ESCAPADOS = "\\n" | "\\\"" | "\\\'"
NO_ESCAPADOS = [^\'\"]
ESPACIOS = [ \t\r\n]+
CARACTER_ENTRADA =[^\!]| ("!"[^\>])
COMENTARIO_MULTILINEA = "<!" {CARACTER_ENTRADA} + "!>"
COMENTARIO_SIMPLE = "//" .*

IDENTIFICADOR = [a-zA-Z][a-zA-Z0-9_]+
LETRA_MINUSCULA = [a-z]
LETRA_MAYUSCULA = [A-Z]
NUMEROS = [0-9]
CARACTERES_ESPECIALES = [ -/:-@\[-`{-}]
COMILLAS= \"
CADENA = \" ([^\"] | "\\\"")+ \"
CARACTER = (\" {NO_ESCAPADOS} \") | {ESCAPADOS}

%%
/* 3. Reglas Semanticas */
{COMENTARIO_MULTILINEA} {System.out.println(yytext());}
{COMENTARIO_SIMPLE} {System.out.println(yytext());}
{ESPACIOS} { }
{COMILLAS} { }
{PR_CONJ} { return new Symbol(sym.PR_CONJ,yyline,yychar,yytext());}
{DOS_PUNTOS} { return new Symbol(sym.DOS_PUNTOS,yyline,yychar,yytext());}
{PUNTO_Y_COMA} { return new Symbol(sym.PUNTO_Y_COMA,yyline,yychar,yytext());}
{GUION} { return new Symbol(sym.GUION,yyline,yychar,yytext());}
{MAYOR} { return new Symbol(sym.MAYOR,yyline,yychar,yytext());}
{COMA} { return new Symbol(sym.COMA,yyline,yychar,yytext());}
{SEPARADOR} { return new Symbol(sym.SEPARADOR,yyline,yychar,yytext());}
{LLAVE_A} { return new Symbol(sym.LLAVE_A,yyline,yychar,yytext());}
{LLAVE_C} { return new Symbol(sym.LLAVE_C,yyline,yychar,yytext());}
{CONCATENACION} { return new Symbol(sym.CONCATENACION,yyline,yychar,yytext());}
{DISYUNCION} { return new Symbol(sym.DISYUNCION,yyline,yychar,yytext());}
{CERRADURA_KLEENE} { return new Symbol(sym.CERRADURA_KLEENE,yyline,yychar,yytext());}
{CERRADURA_POSITIVA} { return new Symbol(sym.CERRADURA_POSITIVA,yyline,yychar,yytext());}
{CERRADURA_BOOLEANA} { return new Symbol(sym.CERRADURA_BOOLEANA,yyline,yychar,yytext());}
{PORCENTAJE} { return new Symbol(sym.PORCENTAJE,yyline,yychar,yytext());}
{IDENTIFICADOR} { return new Symbol(sym.IDENTIFICADOR,yyline,yychar,yytext());}
{LETRA_MINUSCULA} { return new Symbol(sym.LETRA_MINUSCULA,yyline,yychar,yytext());}
{LETRA_MAYUSCULA} { return new Symbol(sym.LETRA_MAYUSCULA,yyline,yychar,yytext());}
{NUMEROS} { return new Symbol(sym.NUMEROS,yyline,yychar,yytext());}
{CARACTER} { return new Symbol(sym.CARACTER,yyline,yychar,yytext());}
{CADENA} { return new Symbol(sym.CADENA,yyline,yychar,yytext());}
{CARACTERES_ESPECIALES} { return new Symbol(sym.CARACTERES_ESPECIALES,yyline,yychar,yytext());}


. {
    
    System.out.println("error lexico: "+yytext()+ ", en la linea: "+yyline+", en la columna: "+yychar);
    Errores.add(new Excepcion("Léxico", "Caracter no válido detectado: " + yytext(), yyline + "", yychar + ""));
    
}