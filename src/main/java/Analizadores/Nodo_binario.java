
package Analizadores;

import java.util.ArrayList;

public class Nodo_binario {
    private String dato;
    private Nodo_binario hijo_izquierdo;
    private Nodo_binario hijo_derecho;
    private boolean hoja = false;
    private int identificador;
    private boolean anulable;
    private ArrayList<Integer> primeros = new ArrayList<>();
    private ArrayList<Integer> ultimos = new ArrayList<>();
    private ArrayList<Integer> siguientes = new ArrayList<>();

    public ArrayList<Integer> getSiguientes() {
        return siguientes;
    }

    public void setSiguientes(ArrayList<Integer> siguientes) {
        this.siguientes = siguientes;
    }
    public ArrayList<Integer> getUltimos() {
        return ultimos;
    }

    public void setUltimos(ArrayList<Integer> ultimos) {
        this.ultimos = ultimos;
    }
    public ArrayList<Integer> getPrimeros() {
        return primeros;
    }

    public void setPrimeros(ArrayList<Integer> primeros) {
        this.primeros = primeros;
    }
    public boolean isAnulable() {
        return anulable;
    }

    public void setAnulable(boolean anulable) {
        this.anulable = anulable;
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public boolean isHoja() {
        return hoja;
    }

    public void setHoja(boolean hoja) {
        this.hoja = hoja;
    }
     
    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public Nodo_binario getHijo_izquierdo() {
        return hijo_izquierdo;
    }

    public void setHijo_izquierdo(Nodo_binario hijo_izquierdo) {
        this.hijo_izquierdo = hijo_izquierdo;
    }

    public Nodo_binario getHijo_derecho() {
        return hijo_derecho;
    }

    public void setHijo_derecho(Nodo_binario hijo_derecho) {
        this.hijo_derecho = hijo_derecho;
    }
    
    public Nodo_binario(String dato){
        this.dato=dato;
    }
    
}
