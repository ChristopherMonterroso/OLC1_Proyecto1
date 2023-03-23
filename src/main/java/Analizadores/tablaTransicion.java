/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizadores;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author crist
 */
public class tablaTransicion {

    public ArrayList<ArrayList> acceptStates;
    public ArrayList<ArrayList> states;

    ArrayList<Nodo_binario> leaves = new ArrayList();

    public int cont;

    public tablaTransicion(Nodo_binario root, ArrayList tabla, ArrayList<Nodo_binario> leaves) {
        this.states = new ArrayList();

        ArrayList datos = new ArrayList();
        datos.add("S0");
        datos.add(root.getPrimeros());
        datos.add(new ArrayList());
        datos.add(false);
        //datos.add(new ArrayList());
        int aceptacion = 0;
        // graphs

        this.states.add(datos);
        this.cont = 1;

        for (int i = 0; i < states.size(); i++) {
            ArrayList state = states.get(i);

            ArrayList<Integer> elementos = (ArrayList) state.get(1);
            Map<String, ArrayList<Integer>> transiciones = new HashMap<>();

            for (int hoja : elementos) {
                tablaSiguientes sigTabla = new tablaSiguientes();
                ArrayList lexemeNext = (ArrayList) sigTabla.next(hoja, tabla).clone();

                if (lexemeNext.get(0) == "") {
                    continue;
                }

                ArrayList<Integer> sigEstados = transiciones.get(lexemeNext.get(0));
                if (sigEstados == null) {
                    sigEstados = new ArrayList<>();
                    transiciones.put((String) lexemeNext.get(0), sigEstados);
                }
                sigEstados.addAll((ArrayList<Integer>) lexemeNext.get(1));

                leave hojas = new leave();

            }

            for (Map.Entry<String, ArrayList<Integer>> entrada : transiciones.entrySet()) {
                ArrayList<Integer> sigEstados = entrada.getValue();

                String nombreEstadoSiguiente = null;
                for (ArrayList estado : states) {
                    if (estado.get(1).equals(sigEstados)) {
                        nombreEstadoSiguiente = (String) estado.get(0);
                        break;
                    }
                }

                if (nombreEstadoSiguiente == null) {
                    nombreEstadoSiguiente = "S" + cont;
                    cont++;

                    ArrayList nuevo = new ArrayList();
                    nuevo.add(nombreEstadoSiguiente);
                    nuevo.add(sigEstados);
                    nuevo.add(new ArrayList());

                    if (sigEstados.contains(aceptacion)) {
                        nuevo.add(true);

                    } else {
                        nuevo.add(false);
                    }
                    //nuevo.add(new ArrayList());
                    states.add(nuevo);
                    // add the accept state

                    //System.out.println("nuevo: " + nuevo);
                }

                transicion trans = new transicion(state.get(0) + "", entrada.getKey(), nombreEstadoSiguiente);
                ((ArrayList) state.get(2)).add(trans);
                //((ArrayList)state.get(4)).add(trans.toArray());
            }
        }
        for (ArrayList state : states) {
            boolean acceptState = false;
            for (Integer leaf : (ArrayList<Integer>) state.get(1)) {
                if (new leave().isAccept(leaf, leaves)) {
                    acceptState = true;
                    break;
                }
            }
            state.set(3, acceptState);
        }

    }

    public void impTable(String nombre) throws IOException {
        FileWriter fichero = null;
        PrintWriter pw = null;
        String s = "";
        try {

            s
                    += "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "  <head>\n"
                    + "    <title>Transiciones</title>\n"
                    + "    <style>\n"
                    + "      /* Estilos para centrar la tabla */\n"
                    + "      html{\n"
                    + "        background-color: lightgrey;\n"
                    + "      }\n"
                    + "      .center {\n"
                    + "        text-align: center;\n"
                    + "        margin: auto;\n"
                    + "        width: 50%;\n"
                    + "        border: 1px solid black;\n"
                    + "        padding: 10px;\n"
                    + "        background-color: azure;\n"
                    + "        }\n"
                    + "        th{\n"
                    + "            border: 1px solid black;\n"
                    + "            \n"
                    + "            \n"
                    + "        }\n"
                    + "        h1{\n"
                    + "            text-align: center;\n"
                    + "        }\n"
                    + "       \n"
                    + "    </style>  </head>\n"
                    + "  <body>"
                    + "    <h1>Tabla de transiciones(" + nombre + ").</h1>\n"
                    + "    <table class=\"center\">\n"
                    + "      <tr>\n"
                    + "        <th>Estado</th>\n"
                    + "        <th>Transiciones</th>\n"
                    + "        <th>Aceptación</th>\n"
                    + "      </tr>\n";

            for (ArrayList state : states) {
                String tran = "[";
                for (Object tr : (ArrayList) state.get(2)) {
                    transicion t = (transicion) tr;
                    tran += t.toString() + ", ";
                }
                tran += "]";
                tran = tran.replace(", ]", "]");
                s += "<tr>\n"
                        + "    <td>" + state.get(0) + "</td>\n"
                        + "    <td>" + state.get(1)+" "+tran + " </td>\n"
                        + "    <td>" + state.get(3) + "</td>\n"
                        + "</tr>\n";
                //System.out.println(state.get(0) + " " + state.get(1) + " " + tran + " " + state.get(3));

            }
            
            s += "</table>\n"
                    + "  </body>\n"
                    + "</html>";
            String path = "Transiciones_201902363/transiciones_" + nombre + ".html";
            fichero = new FileWriter(path);
            pw = new PrintWriter(fichero);
            pw.println(s);
        } catch (Exception e) {
        } finally {
            if (null != fichero) {
                fichero.close();
            }
        }
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void impGraph(String nombre) {
        boolean accept;
        String graph = "";
        for (ArrayList state : states) {
            String s= "";
            if (state.get(3).equals(true)) {
                accept = true;
            } else {
                accept = false;
            }
            if(accept){
                graph+=state.get(0)+"[shape=doublecircle]";
            }
           
            for (Object tr : (ArrayList) state.get(2)) {
                transicion t = (transicion) tr;
                
                s += t.graph();
            }
            graph+=s;

        }
        
        GenerarDot(graph,nombre);
    }
    
    private void GenerarDot(String cadena, String nombre) {
        FileWriter fichero = null;
        PrintWriter escritor = null;
        String s="digraph G { rankdir=\"LR\"";
        s+=cadena;
        s+="\n }";
        try {
            fichero = new FileWriter("AFD_201902363/AFD" + nombre + ".dot");
            escritor = new PrintWriter(fichero);
            escritor.println(s);
            escritor.close();
            fichero.close();
            reportar(nombre);
        } catch (Exception e) {
            System.out.println("error en generar dot");
            e.printStackTrace();
        }
    }

    public void reportar(String nombre) throws IOException {

        String file_input_path = "AFD_201902363/AFD" + nombre + ".dot";
        String do_path = "C:\\Program Files (x86)\\Graphviz\\bin\\dot.exe";

        String file_get_path = "AFD_201902363/AFD_" + nombre + ".png";
        try {
            ProcessBuilder pBuilder;
            pBuilder = new ProcessBuilder(do_path, "-Tpng", "-o", file_get_path, file_input_path);
            pBuilder.redirectErrorStream(true);
            pBuilder.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
