/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizadores;

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

    public void impTable() {
        for (ArrayList state : states) {
            String tran = "[";
            for (Object tr : (ArrayList) state.get(2)) {
                transicion t = (transicion) tr;
                tran += t.toString() + ", ";
            }
            tran += "]";
            tran = tran.replace(", ]", "]");
            System.out.println(state.get(0) + " " + state.get(1) + " " + tran + " " + state.get(3));
        }
    }

    public void impGraph() {
        boolean accept ;
        for (ArrayList state : states) {
            String graph = "";
            if (state.get(3).equals(true)){
                accept =true;
            }else{
                accept=false;
            }
            for (Object tr : (ArrayList) state.get(2)) {
                transicion t = (transicion) tr;
                graph += t.graph(accept);
            }
            System.out.println(graph);
        }
    }
}
