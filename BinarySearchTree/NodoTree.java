package BinarySearchTree;

public class NodoTree<E>{
    private E valor; //Información del nodo
    private NodoTree<E> derecho; //Referencia a la rama derecha
    private NodoTree<E> izquierdo; //Referencia a la rama izquierda
    //private NodoTree<E> padre; //Referencia al anterior en jerarquía -> su padre

    //Constructor que inicializa la información del nodo con su valor y la referencia del
    //siguiente apunta a null
    public NodoTree(E valor){
        this(valor, null, null);
    }
    public NodoTree(E valor, NodoTree<E> derecho, NodoTree<E> izquierdo){
        this.valor = valor;
        this.derecho = derecho;
        this.izquierdo = izquierdo;
        //this.padre = padre;
    }

    public E getValor(){
        return valor;
    } //Retorna valor o la información del nodo

    public void setValor(E valor){
        this.valor = valor;
    } //Modifica el valor del nodo

    public NodoTree<E> getDerecho(){
        return derecho;
    } //Retorna la referencia del siguiente derecho de la raiz

    public void setDerecho(NodoTree<E> derecho){
        this.derecho = derecho;
    } //Modifica la referencia del siguiente derecho de la raiz

    public NodoTree<E> getIzquierdo(){
        return izquierdo;
    } //Retorna la referencia del siguiente izquierdo de la raiz

    public void setIzquierdo(NodoTree<E> izquierdo){
        this.izquierdo = izquierdo;
    }//Modifica la referencia del siguiente izquierdo de la raiz

    /*public NodoTree<E> getPadre(){
        return padre;
    } //Retorna la referencia del nodoPadre

    public void setPadre(NodoTree<E> padre){
        this.padre = padre;
    } //Modifica la referencia del nodo padre*/

}