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

import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class home extends JFrame implements ActionListener {

    private JTextArea textArchivoEntrada, console;
    private JButton generarAutomata, analizarEntrada;
    private JMenuItem newA, open, save, saveAs;
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
        add(generarAutomata);

        analizarEntrada = new JButton("Analizar entrada");
        analizarEntrada.setBounds(265, 400, 150, 30);
        analizarEntrada.addActionListener(this);
        add(analizarEntrada);

        JLabel labelArchivo = new JLabel("Archivo");
        labelArchivo.setBounds(25, 10, 150, 30);
        add(labelArchivo);

        textArchivoEntrada = new JTextArea();
        textArchivoEntrada.setBounds(25, 35, 400, 350);
        textArchivoEntrada.setBorder(borde);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(25, 35, 400, 350);
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

        String[] elements = {"Arboles", "Siguientes", "Transiciones", "Autómatas"};
        JComboBox comboBox = new JComboBox(elements);
        comboBox.setBounds(450, 35, 100, 40);

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
        } else if (e.getSource() == analizarEntrada) {
            String entrada = textArchivoEntrada.getText();
            ArrayList<Excepcion> errores = new ArrayList();
            try {
                Lexico scanner = new Lexico(new java.io.StringReader(entrada));
                Sintactico analizador = new Sintactico(scanner);
                analizador.parse();
                
                errores.addAll(scanner.Errores);
                errores.addAll(analizador.getErrores());

                generarReporteHTML(errores);
            } catch (Exception s) {
                System.out.println(s);
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

    public static void generarReporteHTML(ArrayList<Excepcion> errores) throws IOException {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {

            String path = "Reporteerrores.html";
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

}
