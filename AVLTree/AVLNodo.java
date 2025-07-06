package AVLTree;

import BinarySearchTree.NodoTree;

class AVLNodo<E> extends NodoTree<E> {
    protected int factorBalance; //Factor de balance del nodo, importante para mantener equilibrio

    //Constructor que inicializa el nodo con un valor y establece el factor de balance en 0
    public AVLNodo(E dato) {
        super(dato);
        this.factorBalance = 0;
    }

    //Métodos con tipos correctos heredados
    public AVLNodo<E> getIzquierdo() {
        return (AVLNodo<E>) super.getIzquierdo();
    }

    public AVLNodo<E> getDerecho() {
        return (AVLNodo<E>) super.getDerecho();
    }

    public String toString() {
        return "AVLNodo{" +
                "valor=" + this.getValor() +
                ", factorBalance=" + factorBalance +
                '}';
    }
}