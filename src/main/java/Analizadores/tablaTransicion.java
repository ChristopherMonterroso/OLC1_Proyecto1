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
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.mycompany.project1.home;
import java.io.File;
import java.io.FileReader;
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
     public void Analizar(String Nombre,ArrayList<ArrayList> conjuntos,ArrayList<ArrayList> declaraciones){

        ArrayList<ArrayList> Aceptacion=new ArrayList<>();
        ArrayList<ArrayList> Transiociones=new ArrayList<>();

        boolean accept;
        for (ArrayList state : states) {
            if (state.get(3).equals(true)){
                accept =true;
            }else{
                accept=false;
            }
            ArrayList<String> A= new ArrayList<>();
            A.add((String) state.get(0));
            A.add(String.valueOf(accept));
            Aceptacion.add(A);

            for (Object tr : (ArrayList) state.get(2)) {
                transicion t = (transicion) tr;
                //System.out.println(accept);
                ArrayList<String> Transicion= new ArrayList<>();
                Transicion.add(t.initialState);
                if(t.transition.length()==3){
                    String i= String.valueOf(t.transition.charAt(0));
                    String j= String.valueOf(t.transition.charAt(2));
                    if(i.equals("\"")&&j.equals("\"")){
                        Transicion.add(String.valueOf(t.transition.charAt(1)));
                    }else{
                        Transicion.add(t.transition);
                    }
                }else if(t.transition.length()==4){
                    String i= String.valueOf(t.transition.charAt(0));
                    String j= String.valueOf(t.transition.charAt(3));
                    if(i.equals("\"")&&j.equals("\"")){
                        Transicion.add(String.valueOf(t.transition.charAt(1))+String.valueOf(t.transition.charAt(2)));
                    }else{
                        Transicion.add(t.transition);
                    }
                }else{
                    Transicion.add(t.transition);
                }
                Transicion.add(t.finalState);
                Transiociones.add(Transicion);
            }
        }

        for (ArrayList declaracion : declaraciones) {
            if(declaracion.get(0).equals(Nombre)){
                String cadena = (String) declaracion.get(1);
                cadena=cadena.substring(1,cadena.length()-1);
                boolean state=Analizador(Aceptacion,Transiociones,conjuntos,cadena);
                if(state){
                    //System.out.println("Valido");
                    home.printConsole("La cadena \'"+cadena+"\' es valida con la estructura "+Nombre);
                    GenerarJson(cadena,Nombre,"Cadena Valida");
                }else{
                    //System.out.println("Invalido");
                    home.printConsole("La cadena \'"+cadena+"\' no es valida con la estructura "+Nombre);
                    GenerarJson(cadena,Nombre,"Cadena Invalida");
                }
            }
        }
    }

    public void GenerarJson(String Valor,String ExpresionRegular,String Resultado){
        Gson gson = new Gson();

        try {
            File file = new File("salidas_201902363/salida.json");
            if(!file.exists()){
                file.createNewFile();
            }
            JsonReader reader = new JsonReader(new FileReader("Salidas_201902363/salida.json"));
            //[{"nombre":"Pedro","edad":25,"casado":true},{"nombre":"Pedro","edad":25,"casado":true}]
            // Convertir contenido del archivo en un objeto JsonElement
            JsonElement contenido = gson.fromJson(reader, JsonElement.class);

            // Convertir el objeto JsonElement a un objeto JsonObject o JsonArray
            JsonArray arreglo;
            if(contenido==null){
                arreglo = new JsonArray();
            }else{
                arreglo = contenido.getAsJsonArray();
            }


            // Agregar nuevo objeto
            JsonObject nuevoObjeto = new JsonObject();
            nuevoObjeto.addProperty("Valor", Valor);
            nuevoObjeto.addProperty("ExpresionRegular", ExpresionRegular);
            nuevoObjeto.addProperty("Resultado", Resultado);
            arreglo.add(nuevoObjeto);

            // Escribir objeto de nuevo en el archivo
            try (FileWriter archivoEscritura = new FileWriter("Salidas_201902363/salida.json")) {
                gson.toJson(arreglo, archivoEscritura);
            }
        } catch (IOException e) {
            System.out.println("Error al leer o escribir el archivo: " + e.getMessage());
        }
    }

    public boolean Analizador(ArrayList<ArrayList> Aceptacion,ArrayList<ArrayList> Transiociones,ArrayList<ArrayList> conjuntos,String cadena){
        //System.out.println("Cadena a analizar: "+cadena);
        ArrayList<String> Lista = new ArrayList<>();
        for (int i = 0; i < cadena.length(); i++) {
            String caracter = Character.toString(cadena.charAt(i));
            Lista.add(caracter);
        }

        boolean key=false;
        String currentState="S0";

        for (int i = 0; i <Lista.size() ; i++) {
            String currentCaracter=Lista.get(i);
            for (int j = 0; j < Transiociones.size(); j++) {
                if (Transiociones.get(j).get(0).equals(currentState)){
                    if(Transiociones.get(j).get(1).equals(currentCaracter)||BusquedaConjuntos(conjuntos,currentCaracter, (String) Transiociones.get(j).get(1))){
                        //System.out.println("Se pasa del estado["+currentState+"]->{"+currentCaracter+"}->["+Transiociones.get(j).get(2)+"]");
                        currentState= (String) Transiociones.get(j).get(2);
                        break;
                    }

                }
            }
            if(i+1==Lista.size()){
                //System.out.println("analizo el ultimo caracter");
                for (int j = 0; j < Aceptacion.size(); j++) {
                    if (currentState.equals(Aceptacion.get(j).get(0))&&Aceptacion.get(j).get(1).equals("true")){
                        //System.out.println("Estado Actual["+currentState+"] = Estado de Aceptacion["+Aceptacion.get(j).get(0)+"]");
                        //System.out.println("Acptacion["+Aceptacion.get(j).get(1)+"] = true");
                        key=true;
                    }
                }
            }
        }

        return key;
    }
    public boolean BusquedaConjuntos(ArrayList<ArrayList> conjuntos,String currentCaracter,String posibleConjunto){
        boolean key=false;
        for (ArrayList conjunto:conjuntos) {
            if (conjunto.get(0).equals(posibleConjunto)){
                for (int i = 1; i < conjunto.size(); i++) {
                    if(conjunto.get(i).equals(currentCaracter)){
                        key=true;
                    }
                }
            }
        }
        return key;
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
