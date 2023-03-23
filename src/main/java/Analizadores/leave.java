/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizadores;

/**
 *
 * @author crist
 */
import java.util.ArrayList;


public class leave {
    
    public void addLeave(Nodo_binario nodo, ArrayList<Nodo_binario> leaves){
        leaves.add(nodo);
    }
    
    public Nodo_binario getLeave(int numLeave, ArrayList<Nodo_binario> leaves){
        for (Nodo_binario item : leaves) {
            if(item.getIdentificador() == numLeave) return item;
        }
        return null;
    }
    
    public boolean isAccept(int numLeave, ArrayList<Nodo_binario> leaves){
        for (Nodo_binario item : leaves) {
            if(item.getIdentificador() == numLeave) return "#".equals(item.getDato());
        }
        return false;
    }
}