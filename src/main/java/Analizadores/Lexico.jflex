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


BLANCOS=[ \t\r\n]+
NUMERO=[0-9]
LETRA_MAYUSCULA=[A-Z]
LETRA_MINUSCULA=[a-z]




VARIABLE=[a-zA-Z][a-zA-Z0-9]*
ASCCI=[ -/:-@\[-`{-}]
CADENA=\" ([^\"] | "\\\"")+ \"

NO_ESCAPADOS = [^\'\"]
CARACTER = (\" {NO_ESCAPADOS} \")


COMENTARIO_SIMPLE    = "//" .*
CARACTER_ENTRADA =[^\!]| ("!"[^\>])
COMENTARIO_EXTENSO    = "<!" {CARACTER_ENTRADA} + "!>"
ESPACIO = " "




%%
{COMENTARIO_SIMPLE} {}
{COMENTARIO_EXTENSO} {}

{BLANCOS} {}
"\"" {}

"CONJ" { return new Symbol(sym.CONJUNTO,yyline,yychar, yytext());}

"{" { return new Symbol(sym.ABRE_LLAVES,yyline,yychar, yytext());}
"}" { return new Symbol(sym.CIERRA_LLAVES,yyline,yychar, yytext());}

":" { return new Symbol(sym.DOS_PUNTOS,yyline,yychar, yytext());}
"-" { return new Symbol(sym.GUION,yyline,yychar, yytext());}
">" { return new Symbol(sym.MAYOR_QUE,yyline,yychar, yytext());}
";" { return new Symbol(sym.PUNTO_COMA,yyline,yychar, yytext());}
"," { return new Symbol(sym.COMA,yyline,yychar, yytext());}
"%%" { return new Symbol(sym.PORCENTAJES,yyline,yychar, yytext());}
"~" { return new Symbol(sym.VIRGULILLA,yyline,yychar, yytext());}



"." { return new Symbol(sym.CONCATENACION,yyline,yychar, yytext());}
"|" { return new Symbol(sym.DISYUNCION,yyline,yychar, yytext());}
"*" { return new Symbol(sym.CERRADURA_KLEENE,yyline,yychar, yytext());}
"+" { return new Symbol(sym.CERRADURA_POSITIVA,yyline,yychar, yytext());}
"?" { return new Symbol(sym.CIERRA_INTERROGACION,yyline,yychar, yytext());}

"\\" { return new Symbol(sym.BARRA_INVERTIDA,yyline,yychar, yytext());}
"/" { return new Symbol(sym.BARRA,yyline,yychar, yytext());}
"\\\"" { return new Symbol(sym.COMILLAS_DOBLES,yyline,yychar, yytext());}
"\\'" { return new Symbol(sym.COMILLA_SIMPLE,yyline,yychar, yytext());}
"\\n" { return new Symbol(sym.SALTO_LINEA,yyline,yychar, yytext());}

{CARACTER} { return new Symbol(sym.CARACTER,yyline,yychar, yytext());}
{CADENA} { return new Symbol(sym.CADENA,yyline,yychar, yytext());}
{NUMERO} { return new Symbol(sym.NUMERO,yyline,yychar, yytext());}
{LETRA_MAYUSCULA} { return new Symbol(sym.LETRA_MAYUSCULA,yyline,yychar, yytext());}
{LETRA_MINUSCULA} { return new Symbol(sym.LETRA_MINUSCULA,yyline,yychar, yytext());}
{VARIABLE} { return new Symbol(sym.VARIABLE,yyline,yychar, yytext());}


{ASCCI} { return new Symbol(sym.ASCCI,yyline,yychar, yytext());}


. {
    System.out.println("error lexico: "+yytext()+ ", en la linea: "+yyline+", en la columna: "+yychar);
    Errores.add(new Excepcion("Léxico", "Caracter no válido detectado: " + yytext(), yyline + "", yychar + ""));
}