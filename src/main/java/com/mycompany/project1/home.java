package com.mycompany.project1;

import Analizadores.Sintactico;
import Analizadores.Lexico;
import Errores.Excepcion;
import java.awt.Color;
import java.awt.Desktop;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.*;
import javax.swing.border.Border;
import Analizadores.Sintactico;
import Analizadores.Lexico;

import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class home extends JFrame implements ActionListener {

    static JTextArea textArchivoEntrada, console;
    private JButton generarAutomata, analizarEntrada,abrirUbicacion;
    private JMenuItem newA, open, save, saveAs;
    private JComboBox comboBox;
    private String ruta = "";

    public home() {
        this.setTitle("ExRegan USAC");
        this.setSize(900, 700);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(null);
        this.setLocationRelativeTo(null);

        widgets();

    }

    public void widgets() {
        Border borde = BorderFactory.createLineBorder(Color.BLACK, 2, true);
        JMenuBar menuBar = new JMenuBar();

        JMenu archive = new JMenu("Archivo");
        newA = new JMenuItem("Nuevo");
        newA.addActionListener(this);
        open = new JMenuItem("Abrir");
        open.addActionListener(this);
        save = new JMenuItem("Guardar");
        save.addActionListener(this);
        saveAs = new JMenuItem("Guardar como");
        saveAs.addActionListener(this);
        archive.add(newA);
        archive.add(open);
        archive.add(save);
        archive.add(saveAs);

        menuBar.add(archive);
        setJMenuBar(menuBar);

        generarAutomata = new JButton("Generar Autómata");
        generarAutomata.setBounds(30, 400, 150, 30);
        generarAutomata.addActionListener(this);
        add(generarAutomata);
        
        abrirUbicacion = new JButton("Mostrar archivos");
        abrirUbicacion.setBounds(550, 100, 150, 40);
        abrirUbicacion.addActionListener(this);
        add(abrirUbicacion);

        analizarEntrada = new JButton("Analizar entrada");
        analizarEntrada.setBounds(265, 400, 150, 30);
        analizarEntrada.addActionListener(this);
        add(analizarEntrada);

        JLabel labelArchivo = new JLabel("Archivo");
        labelArchivo.setBounds(25, 10, 150, 30);
        add(labelArchivo);

        textArchivoEntrada = new JTextArea();
        textArchivoEntrada.setBounds(25, 35, 500, 350);
        textArchivoEntrada.setBorder(borde);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(25, 35, 500, 350);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.getViewport().add(textArchivoEntrada);
        add(scrollPane);
        //add(textArchivoEntrada);

        JLabel labelConsola = new JLabel("Consola");
        labelConsola.setBounds(25, 460, 150, 30);
        add(labelConsola);

        console = new JTextArea();
        console.setBorder(borde);
        console.setBounds(25, 490, 825, 125);
        JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.setBounds(25, 490, 825, 125);
        scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane2.getViewport().setBackground(Color.WHITE);
        scrollPane2.getViewport().add(console);
        add(scrollPane2);

        String[] elements = {"Arboles", "Siguientes", "Transiciones", "AFD","AFND","Errores","Salidas"};
        comboBox = new JComboBox(elements);
        comboBox.setBounds(570, 35, 100, 40);

        add(comboBox);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newA) {
            ruta = "";
            textArchivoEntrada.setText("");
            this.saveAS();

        } else if (e.getSource() == open) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String fileContent = null;
                ruta = selectedFile.getPath();
                System.out.println(ruta);
                try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                    String line = null;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    fileContent = stringBuilder.toString();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(home.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(home.class.getName()).log(Level.SEVERE, null, ex);
                }
                textArchivoEntrada.setText("");
                textArchivoEntrada.setText(fileContent);

            }

        } else if (e.getSource() == save) {
            if ("".equals(ruta)) {
                this.saveAS();
            } else {
                try {
                    try (FileWriter writer = new FileWriter(ruta)) {
                        writer.write(textArchivoEntrada.getText());
                    }
                    JOptionPane.showMessageDialog(null, "Se han guardado los cambios");
                    System.out.println("guardado");
                } catch (IOException s) {
                    JOptionPane.showMessageDialog(null, "No se han guardado los cambios");
                }
            }

        } else if (e.getSource() == saveAs) {
            this.saveAS();

        } else if (e.getSource() == generarAutomata) {
        
        ArrayList<Excepcion> errores = new ArrayList();
        try {
            Lexico scanner = new Lexico(new BufferedReader(new StringReader(textArchivoEntrada.getText())));
            Sintactico parse = new Sintactico(scanner);
            parse.parse();
            errores.addAll(scanner.Errores);
            errores.addAll(parse.getErrores());
            generarReporteHTML(errores);
            if (errores.size()>=1) {
                console.setText("ExRegan -> Error(compruebe el error en el archivo de errores)");
            }else{
                console.setText("ExRegan -> Automatas generados");
            }

            for (int i = 0; i < parse.arboles.size(); i++) {
                ArrayList<String> Arbol= new ArrayList();
                Arbol.add(parse.arboles.get(i).getNombre());
                Arbol.add(parse.arboles.get(i).getArbol_expresion().getDato());
                
                parse.arboles.get(i).GenerarAutomata(parse.arboles.get(i).getArbol_expresionCopi(),i);
            }
             
               
            


        } catch (Exception ex) {
            Logger.getLogger(home.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error fatal en compilación de entrada.");
//                    System.out.println("Causa: "+ex.getCause());
        }
            
        }else if (e.getSource()== abrirUbicacion){
           String option = (String) comboBox.getSelectedItem();
            System.out.println(option);
            openExplorer(option(option));
        }else if (e.getSource()==analizarEntrada) {
             ArrayList<Excepcion> errores = new ArrayList();



        try {
            Lexico scanner = new Lexico(new BufferedReader(new StringReader(textArchivoEntrada.getText())));
            Sintactico parse = new Sintactico(scanner);
            parse.parse();
            errores.addAll(scanner.Errores);
            errores.addAll(parse.getErrores());
            generarReporteHTML(errores);


            ArrayList<ArrayList> Conjuntos_Arreglados=ArreglarConjuntos(parse.conjuntos);

            for (int i = 0; i < parse.arboles.size(); i++) {
                parse.arboles.get(i).Analizar(parse.arboles.get(i).getArbol_expresionCopi(),Conjuntos_Arreglados, (ArrayList<ArrayList>) parse.declaraciones);
            }



        } catch (Exception ex) {
            Logger.getLogger(home.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error fatal en compilación de entrada.");
//                    System.out.println("Causa: "+ex.getCause());
        }
        }
    }

    public void saveAS() {
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showSaveDialog(null);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            ruta = archivoSeleccionado.getPath();
            try {
                try (FileWriter writer = new FileWriter(archivoSeleccionado)) {
                    // Escribir contenido en el archivo
                    writer.write(textArchivoEntrada.getText());
                }
            } catch (IOException s) {

            }
        }
    }
    public String option(String s){
        switch(s){
            case "Transiciones":
                return "Transiciones_201902363/";
            case "Arboles":
                return "Arboles_201902363/";
            case "Siguientes":
                return "Siguientes_201902363/";
            case "Errores":
                return "Errores_201902363/";
            case "Salidas":
                return "Salidas_201902363/";
            case "AFD":
                return "AFD_201902363/";
            
        }
        return "";
    }
    
     public static void openExplorer(String path) {
        try {
            File file = new File(path);
            Desktop desktop = Desktop.getDesktop();
            if (file.exists()) {
                desktop.open(file);
            } else {
                System.out.println("El archivo o directorio no existe");
            }
        } catch (IOException e) {
            System.out.println("Se produjo un error al intentar abrir el explorador de archivos");
            e.printStackTrace();
        }
    }
    
    public static void generarReporteHTML(ArrayList<Excepcion> errores) throws IOException {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {

            String path = "Errores_201902363/Reporte_Errores.html";
            fichero = new FileWriter(path);
            pw = new PrintWriter(fichero);

            //Comenzamos a escribir el html
            pw.println("<html>");
            pw.println("<head><title>REPORTE DE ERRORES</title></head>");
            pw.println("<body>");
            pw.println("<div align=\"center\">");
            pw.println("<h1>Reporte de Errores</h1>");
            pw.println("<br></br>");
            pw.println("<table border=1>");
            pw.println("<tr>");
            pw.println("<td>ERROR</td>");
            pw.println("<td>DESCRIPCION</td>");
            pw.println("<td>FILA</td>");
            pw.println("<td>COLUMNA</td>");
            pw.println("</tr>");

            for (Excepcion err : errores) {
                pw.println("<tr>");
                pw.println("<td>" + err.tipo + "</td>");
                pw.println("<td>" + err.descripcion + "</td>");
                pw.println("<td>" + err.linea + "</td>");
                pw.println("<td>" + err.columna + "</td>");
                pw.println("</tr>");
            }

            pw.println("</table>");
            pw.println("</div");
            pw.println("</body>");
            pw.println("</html>");
            //Desktop.getDesktop().open(new File(path));

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
    private ArrayList<ArrayList> ArreglarConjuntos(List<ArrayList> parse_conjuntos){
        ArrayList<ArrayList> conjuntos= new ArrayList<>();
        for (int i = 0; i < parse_conjuntos.size(); i++) {
            List<ArrayList>  parse_conjunto=parse_conjuntos.get(i);
            //System.out.println("Conjunto #"+(i+1)+" de nombre \""+parse_conjunto.get(parse_conjunto.size()-1)+"\" y tipo \""+parse_conjunto.get(parse_conjunto.size()-2)+"\": "+parse_conjunto);
            ArrayList<String> conjunto= new ArrayList<>();
            conjunto.add(String.valueOf(parse_conjunto.get(parse_conjunto.size()-1)));
            if(String.valueOf(parse_conjunto.get(parse_conjunto.size()-2)).equals(",")){
                for (int j = 0; j < parse_conjunto.size()-2; j++) {
                    conjunto.add(String.valueOf(parse_conjunto.get(j)));
                }
                //System.out.println("Arreglando Conjunto de tipo Coma: "+conjunto);
            }else if (String.valueOf(parse_conjunto.get(parse_conjunto.size()-2)).equals("~")){
                char a=String.valueOf(parse_conjunto.get(0)).charAt(0);
                int ini=(int) a;
                char b=String.valueOf(parse_conjunto.get(1)).charAt(0);
                int fin=(int) b;
                for (int j = ini; j < fin+1; j++) {
                        char ascci=(char) j;
                        conjunto.add(String.valueOf(ascci));
                }
                //System.out.println("Arreglando Conjunto de tipo Virgulilla: "+conjunto);
            }

            conjuntos.add(conjunto);
        }
        return  conjuntos;
    }
    public static void printConsole(String msj){
        if (Objects.equals(console.getText(), "")){
            console.setText(msj);
        }else{
            console.setText(console.getText()+"\n"+msj);
        }
    }
}
