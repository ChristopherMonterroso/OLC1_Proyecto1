package Generador;

public class Generador_analizadores {

    public static void main(String[] args) {
        try{
            String ruta = "src/main/java/Analizadores/"; 
            String opcFlex []= {ruta+"Lexico.jflex","-d",ruta};
            jflex.Main.generate(opcFlex);
            String opcCup[]={"-destdir",ruta,"-parser","Sintactico",ruta+"sintactico.cup"};
            java_cup.Main.main(opcCup);
        }catch(Exception e){}
    }
}
