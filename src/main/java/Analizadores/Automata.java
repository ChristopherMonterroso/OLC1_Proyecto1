/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizadores;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Automata {

    private Nodo_binario arbol_expresion, arbol_expresionCopi;;
    private int num_nodo = 1;
    private String nombre;
    ArrayList<ArrayList> table = new ArrayList();
    
    ArrayList<Nodo_binario> leaves = new ArrayList();
    
     public Automata(Nodo_binario arbol_expresion,String nombre){
        this.arbol_expresion=arbol_expresion;
        this.arbol_expresionCopi=arbol_expresion;
        this.nombre=nombre;
    }
   public void Analizar(Nodo_binario arbol_expresion,ArrayList<ArrayList> conjuntos,ArrayList<ArrayList> declaraciones){
        Nodo_binario raiz = new Nodo_binario(".");
        Nodo_binario aceptacion = new Nodo_binario("#");
        aceptacion.setHoja(true);
        aceptacion.setAnulable(false);
        raiz.setHijo_derecho(aceptacion);
        leave hoja = new leave();

        hoja.addLeave(aceptacion, leaves);
        raiz.setHijo_izquierdo(arbol_expresion);
        this.arbol_expresion = raiz;
        asignar_numeros(this.arbol_expresion);
        num_nodo = 0;
        metodo_arbol(this.arbol_expresion);

        
        graficar_arbol(this.arbol_expresion, num_nodo);
        follows(this.arbol_expresion);
        tablaSiguientes ft = new tablaSiguientes();


        tablaTransicion tran = new tablaTransicion(raiz, table, leaves); // bug
        tran.Analizar(nombre,conjuntos,declaraciones);

    }
    public void GenerarAutomata(Nodo_binario arbol_expresion,int i) throws IOException {
        Nodo_binario raiz = new Nodo_binario(".");
        Nodo_binario aceptacion = new Nodo_binario("#");
        aceptacion.setHoja(true);
        aceptacion.setAnulable(false);
        raiz.setHijo_derecho(aceptacion);
        leave hoja = new leave();

        hoja.addLeave(aceptacion, leaves);
        raiz.setHijo_izquierdo(arbol_expresion);
        this.arbol_expresion = raiz;
        asignar_numeros(this.arbol_expresion);
        num_nodo = 0;
        metodo_arbol(this.arbol_expresion);

        GenerarDot(graficar_arbol(this.arbol_expresion, num_nodo),nombre);
        follows(this.arbol_expresion);
        tablaSiguientes ft = new tablaSiguientes();

        ft.printTable(table,nombre);
        tablaTransicion tran = new tablaTransicion(raiz, table, leaves); // bug

        tran.impTable(nombre);

        tran.impGraph(nombre);
    }

    

    public void asignar_numeros(Nodo_binario actual) {
        if (actual == null) {
            return;
        }
        if (actual.isHoja()) {
            actual.setIdentificador(num_nodo);
            leave hoja = new leave();
            
            hoja.addLeave(actual, leaves); //Tabla de siguientes o transiciones
            num_nodo++;
            return;
        }
        asignar_numeros(actual.getHijo_izquierdo());
        asignar_numeros(actual.getHijo_derecho());
    }

    public void metodo_arbol(Nodo_binario actual) {
        if (actual == null) {
            return;
        }
        metodo_arbol(actual.getHijo_izquierdo());
        metodo_arbol(actual.getHijo_derecho());

        if (actual.isHoja()) {
            actual.getPrimeros().add(actual.getIdentificador());
            actual.getUltimos().add(actual.getIdentificador());
            return;
        }
        if (actual.getDato().equals("*")) {

            actual.setAnulable(true);
            actual.getPrimeros().addAll(actual.getHijo_izquierdo().getPrimeros());
            actual.getUltimos().addAll(actual.getHijo_izquierdo().getUltimos());
        } else if (actual.getDato().equals("+")) {

            actual.setAnulable(actual.getHijo_izquierdo().isAnulable());
            actual.getPrimeros().addAll(actual.getHijo_izquierdo().getPrimeros());
            actual.getUltimos().addAll(actual.getHijo_izquierdo().getUltimos());
        } else if (actual.getDato().equals("?")) {
            actual.setAnulable(true);
            actual.getPrimeros().addAll(actual.getHijo_izquierdo().getPrimeros());
            actual.getUltimos().addAll(actual.getHijo_izquierdo().getUltimos());
        } else if (actual.getDato().equals("|")) {
            actual.setAnulable(actual.getHijo_izquierdo().isAnulable() || actual.getHijo_derecho().isAnulable());
            actual.getPrimeros().addAll(actual.getHijo_izquierdo().getPrimeros());
            actual.getPrimeros().addAll(actual.getHijo_derecho().getPrimeros());
            actual.getUltimos().addAll(actual.getHijo_izquierdo().getUltimos());
            actual.getUltimos().addAll(actual.getHijo_derecho().getUltimos());
        } else if (actual.getDato().equals(".")) {
            actual.setAnulable(actual.getHijo_izquierdo().isAnulable() && actual.getHijo_derecho().isAnulable());
            if (actual.getHijo_izquierdo().isAnulable()) {
                actual.getPrimeros().addAll(actual.getHijo_izquierdo().getPrimeros());
                actual.getPrimeros().addAll(actual.getHijo_derecho().getPrimeros());
            } else {
                actual.getPrimeros().addAll(actual.getHijo_izquierdo().getPrimeros());
            }
            if (actual.getHijo_derecho().isAnulable()) {
                actual.getUltimos().addAll(actual.getHijo_izquierdo().getUltimos());
                actual.getUltimos().addAll(actual.getHijo_derecho().getUltimos());
            } else {
                actual.getUltimos().addAll(actual.getHijo_derecho().getUltimos());
            }

        } else {

        }
    }

    public String graficar_arbol(Nodo_binario nodo, int padre) {
        String cadena = "";
        num_nodo += 1;
        int actual = num_nodo;
        if (nodo == null) {
            num_nodo -= 1;
            return cadena;
        }
        
        if (nodo.isHoja()) {
            String anulable = "A";
            if (nodo.isAnulable() == false) {
                anulable = "N";
            }
            cadena += "n_" + actual + "[shape=none label=<\n"
                    + "<table border =\"1\" cellspacing=\"2\" cellpadding=\"10\" >\n"
                    + " <tr>\n"
                    + " <td colspan=\"3\">" + anulable + "</td>\n"
                    + " </tr>\n"
                    + " <tr>\n"
                    + " <td>" + nodo.getPrimeros() + "</td>\n"
                    + " <td>" + nodo.getDato() + "</td>\n"
                    + " <td>" + nodo.getUltimos() + "</td>\n"
                    + " </tr>\n"
                    + " <tr>\n"
                    + " <td colspan=\"3\">" + nodo.getIdentificador() + "</td>\n"
                    + " </tr>\n"
                    + " </table>>];";

        } else {
            String anulable = "A";
            if (nodo.isAnulable() == false) {
                anulable = "N";
            }
            cadena += "n_" + actual + "[shape=none label=<\n"
                    + "<table border =\"1\" cellspacing=\"2\" cellpadding=\"10\" >\n"
                    + " <tr>\n"
                    + " <td colspan=\"3\">" + anulable + "</td>\n"
                    + " </tr>\n"
                    + " <tr>\n"
                    + " <td>" + nodo.getPrimeros() + "</td>\n"
                    + " <td>" + nodo.getDato() + "</td>\n"
                    + " <td>" + nodo.getUltimos() + "</td>\n"
                    + " </tr>\n"
                    + " </table>>];";

        }
        if (padre != 0) {
            cadena += "n_" + padre + " -> n_" + actual + ";\n";
        }
        cadena += graficar_arbol(nodo.getHijo_izquierdo(), actual);
        cadena += graficar_arbol(nodo.getHijo_derecho(), actual);
        
        return cadena;
    }

    public void follows(Nodo_binario actual) {
        if (actual == null) {
            return;
        }
        follows(actual.getHijo_izquierdo());
        follows(actual.getHijo_derecho());

        if (actual.getDato().equals(".")) {
            for (int item : ((Nodo_binario) actual.getHijo_izquierdo()).getUltimos()) {
                leave hoja = new leave();
                
                Nodo_binario nodo = hoja.getLeave(item, leaves);
                tablaSiguientes tabla = new tablaSiguientes();
                tabla.append(nodo.getIdentificador(), nodo.getDato(), ((Nodo_binario) actual.getHijo_derecho()).getPrimeros(), table);

            }

        } else if (actual.getDato().equals("*")) {
            for (int item : ((Nodo_binario) actual.getHijo_izquierdo()).getUltimos()) {

                leave hoja = new leave();
                Nodo_binario nodo = hoja.getLeave(item, leaves);
                tablaSiguientes tabla = new tablaSiguientes();
                tabla.append(nodo.getIdentificador(), nodo.getDato(), ((Nodo_binario) actual.getHijo_izquierdo()).getPrimeros(), table);

            }

        } else if (actual.getDato().equals("+")) {
            for (int item : ((Nodo_binario) actual.getHijo_izquierdo()).getUltimos()) {

                leave hoja = new leave();
                Nodo_binario nodo = hoja.getLeave(item, leaves);
                tablaSiguientes tabla = new tablaSiguientes();
                tabla.append(nodo.getIdentificador(), nodo.getDato(), ((Nodo_binario) actual.getHijo_izquierdo()).getPrimeros(), table);
            }

        }

    }

    private void GenerarDot(String cadena, String nombre) {
        FileWriter fichero = null;
        PrintWriter escritor = null;
        String s="digraph G {\n ";
        s+=cadena;
        s+="\n }";
        try {
            fichero = new FileWriter("Arboles_201902363/Arbol_Sintactico" + nombre + ".dot");
            escritor = new PrintWriter(fichero);
            escritor.println(s);
            escritor.close();
            fichero.close();
            reportar(nombre);
            System.out.println("aqui");
        } catch (Exception e) {
            System.out.println("error en generar dot");
            e.printStackTrace();
        }
    }

    public void reportar(String nombre) throws IOException {

        String file_input_path = "Arboles_201902363/Arbol_Sintactico" + nombre + ".dot";
        String do_path = "C:\\Program Files (x86)\\Graphviz\\bin\\dot.exe";

        String file_get_path = "Arboles_201902363/Arbol_Sintactico_" + nombre + ".png";
        try {
            ProcessBuilder pBuilder;
            pBuilder = new ProcessBuilder(do_path, "-Tpng", "-o", file_get_path, file_input_path);
            pBuilder.redirectErrorStream(true);
            pBuilder.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
  
       public Nodo_binario getArbol_expresion() {
        return arbol_expresion;
    }
    public Nodo_binario getArbol_expresionCopi() {
        
        return arbol_expresionCopi;
    }

    public String getNombre() {
        return nombre;
    }

}
