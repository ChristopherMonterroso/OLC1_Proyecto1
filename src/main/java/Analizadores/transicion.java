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
public class transicion {
     public String initialState;
    public String transition;
    public String finalState;
    
    public transicion( String initialState, String transition, String finalState ) {
        this.initialState = initialState;
        this.transition = transition;
        this.finalState = finalState;
    }
    
    public boolean compare(String initialState, String transition){
        
        return this.initialState.equals(initialState) && this.transition.equals(transition);
    }
    
    @Override
    public String toString(){
        return this.initialState + " -> " + this.transition + " -> " + this.finalState;
    }
    
    public String graph(boolean accept){
        
        if (accept==true) {
            if (this.transition.charAt(0)=='"') {
            return this.initialState +  "->"  + this.finalState + "[label=" + this.transition + "]";
        }
            return this.finalState + "[shape=doublecircle]\n"+ this.initialState +  "->"  + this.finalState + "[label=\"" + this.transition + "\"]";
        }
        if (this.transition.charAt(0)=='"') {
            return this.initialState +  "->"  + this.finalState + "[label=" + this.transition + "]";
        }
        return this.initialState +  "->"  + this.finalState + "[label=\"" + this.transition + "\"]";
    }
}
