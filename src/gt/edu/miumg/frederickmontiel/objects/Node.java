package gt.edu.miumg.frederickmontiel.objects;


public class Node {
    String valor;
    Node izquierda, derecha;
    
    public Node(String valor) {
        this.valor = valor;
        this.izquierda = null;
        this.derecha = null;
    }
}
