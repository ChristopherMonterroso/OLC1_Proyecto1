/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizadores;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author crist
 */
public class tablaSiguientes {

    public void append(int numNode, String lexeme, ArrayList flwList, ArrayList<ArrayList> table) {
        for (ArrayList item : table) {
            if ((int) item.get(0) == numNode && item.get(1) == lexeme) {
                for (Object flwItem : flwList) {
                    if (!((ArrayList) item.get(2)).contains((int) flwItem)) {
                        ((ArrayList) item.get(2)).add(flwItem);
                    }
                }
                return;
            }
        }
        ArrayList dato = new ArrayList();
        dato.add(numNode);
        dato.add(lexeme);
        dato.add(flwList);

        table.add(dato);
    }

    public ArrayList next(int numNode, ArrayList<ArrayList> table) {
        ArrayList result = new ArrayList();
        for (ArrayList item : table) {
            if ((int) item.get(0) == numNode) {
                result.add(item.get(1));
                result.add(((ArrayList) item.get(2)).clone());
                return result;
            }
        }
        result.add("");
        result.add(new ArrayList());
        return result;
    }

    public void printTable(ArrayList<ArrayList> table,String nombre) {
        int num = table.size() + 1;
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            String path = "project1\\Siguientes_201902363\\"+nombre+".html";
            fichero = new FileWriter(path);
            pw = new PrintWriter(fichero);
            pw.println(
                "<!DOCTYPE html>\n"
                + "<html>\n"
                + "  <head>\n"
                + "    <title>Tabla Centrada</title>\n"
                + "    <style>\n"
                + "      /* Estilos para centrar la tabla */\n"
                + "      .center {\n"
                + "        text-align: center;\n"
                + "        margin: auto;\n"
                + "        width: 50%;\n"
                + "        border: 1px solid black;\n"
                + "        padding: 10px;\n"
                + "      \n"
                + "        }\n"
                + "        th{\n"
                + "            border: 1px solid black;\n"
                + "        }\n"
                + "        h1{\n"
                + "            text-align: center;\n"
                + "        }\n"
                + "    </style>"
                + "  </head>\n"
                + "  <body>\n"
                + "    <h1>Tabla de siguientes.</h1>\n"
                + "    <table class=\"center\">\n"
                + "      <tr>\n"
                + "        <th>Hoja</th>\n"
                + "        <th>Número</th>\n"
                + "        <th>Siguientes</th>\n"
                + "      </tr>"
                
        );
        for (ArrayList item : table) {
            System.out.println(item.get(0) + " - " + item.get(1) + " - " + item.get(2));
            pw.println("<tr>\n"
                + "    <td>"+item.get(1)+"</td>\n"
                + "    <td>"+item.get(0)+"</td>\n"
                + "    <td>"+item.get(2)+"</td>\n"
                + "</tr>");
        }
        System.out.println(num + " - " + "#" + " - " + "--");
        pw.println("<tr>\n"
                + "    <td>#</td>\n"
                + "    <td>"+num+"</td>\n"
                + "    <td>--</td>\n"
                + "</tr>\n");
        pw.println("</table>\n"
                + "  </body>\n"
                + "</html>");
        } catch (Exception e) {
        }
        

    }
}
