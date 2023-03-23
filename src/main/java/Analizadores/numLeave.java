
package Analizadores;


public class numLeave {
    public int content;

    public numLeave(String content) {
        this.content = clean(content) + 1;
    }
    
    public int getNum(){
        content -= 1;
        return content;
    }
    
    
    public int clean(String content){
        return content.replace(".", "").replace("|", "").replace("*", "").length();
    }
}
