package BinarySearchTree;

import Excepciones.*;

public class LinkedBST<E extends Comparable<E>> implements BinarySearchTree<E> {
    private NodoTree<E> raiz;
    private int size;

    public LinkedBST(){
        this.raiz = null;
        this.size = 0;
    }

    //Elimina todos los elementos del BST dejándolo vacío
    public void destroy(){
        raiz = null;//Raiz esta vacía, árbol vacío
        size = 0;
    }

    //Verifica si el BST está vacío
    public boolean isEmpty(){
        return raiz == null; //Si la raiz es null, el árbol esta vacío
    }

    //Retorna el número de elementos en el BST
    public int size(){
        return size;
    }

    //Busca un elemento en el BST
    public E search(E valor) throws ItemNotFound{
        E resultadoBusqueda = searchRecursivo(raiz,valor);
        if (resultadoBusqueda == null){
            throw new ItemNotFound();
        }
        return resultadoBusqueda;
    }

    //Busca recursivamente el valor de un nodo en el árbol
    private E searchRecursivo(NodoTree<E> nodoTemporal, E valor){
        if(nodoTemporal == null){ return null; }

        int resultado = valor.compareTo(nodoTemporal.getValor());
        if (resultado > 0){ //Si el valor a buscar es mayor al nodoTemporal (raizTemporal)
            //Busca en la derecha(mayores)
            return searchRecursivo(nodoTemporal.getDerecho(),valor);
        }

        else if (resultado < 0){ //Si el valor a buscar es menor al nodoTemporal
            //Busca en la izquierda(menores)
            return searchRecursivo(nodoTemporal.getIzquierdo(),valor);
        }

        else{ //Si resultado es igual a 0
            //El nodoTemporal y el valor son iguales, es decir, encontró el valor en el BSTree
            return valor;
        }
    }

    /*
    Método de búsqueda privada para usarlo en insert
     */
    private E searchInterno(E valor) {
        return searchRecursivo(raiz,valor);
    }

    //Agrega un elemento en el BST
    public void insert(E valor) throws ItemDuplicated{
        if (valor == null) {
            throw new IllegalArgumentException("El valor no puede ser null");
        }

        if (isEmpty()){
            NodoTree<E> nuevoNodo = new NodoTree<>(valor);
            raiz = nuevoNodo;
            size++;
        }
        else{
            //Busca si el elemento ya existe en el árbol
            if (searchInterno(valor) != null){
                throw new ItemDuplicated();
            }
            insertRecursivo(raiz, valor);
            size++;
        }
    }

    //Busca recursivamente el valor de un nodo en el árbol, e inserta el valor en el lugar correcto
    private void insertRecursivo(NodoTree<E> nodoTemporal, E valor){
        NodoTree<E> nuevoNodo = new NodoTree<>(valor);

        int resultado = valor.compareTo(nodoTemporal.getValor());

        if (resultado > 0){ //Si el valor a buscar es mayor al nodoTemporal (raizTemporal)
            //Busca en la derecha(mayores)
            if(nodoTemporal.getDerecho() == null){ //Si el hijo derecho del nodo está vacío, se inserta
                nodoTemporal.setDerecho(nuevoNodo);
            }
            else{
                insertRecursivo(nodoTemporal.getDerecho(),valor); //nodoTemporal = nodoTemporal.getDerecho();
            }
        }

        else {//Si el valor a buscar es menor al nodoTemporal (raizTemporal) (resultado < 0)
            //Busca en la izquierda(menores)
            if(nodoTemporal.getIzquierdo() == null){ //Si el hijo izquierdo del nodo está vacío, se inserta
                nodoTemporal.setIzquierdo(nuevoNodo);
            }
            else{
                insertRecursivo(nodoTemporal.getIzquierdo(),valor); //nodoTemporal = nodoTemporal.getIzquierdo();
            }
        }
    }

    //Elimina un elemento en el BST
    public void delete(E valor) throws ExceptionIsEmpty{
        if (valor == null) {
            throw new IllegalArgumentException("El valor no puede ser null");
        }

        if (isEmpty()){
            throw new ExceptionIsEmpty();
        }

        //Verificar si existe ANTES de eliminar
        if (searchInterno(valor) != null) {
            raiz = deleteRecursivo(raiz, valor);
            size--; //Solo decrementa si el elemento existía
        }
    }

    //Busca recursivamente el valor de un nodo en el árbol, y elimina el valor del lugar correcto
    private NodoTree<E> deleteRecursivo(NodoTree<E> nodoTemporal, E valor){

        if (nodoTemporal == null) {
            return null; // No se encontró el nodo
        }

        int resultado = valor.compareTo(nodoTemporal.getValor());

        if (resultado < 0) {
            nodoTemporal.setIzquierdo(deleteRecursivo(nodoTemporal.getIzquierdo(), valor));
        } else if (resultado > 0) {
            nodoTemporal.setDerecho(deleteRecursivo(nodoTemporal.getDerecho(), valor));
        } else {
            // Nodo encontrado
            // Caso 1: Nodo hoja
            if (nodoTemporal.getIzquierdo() == null && nodoTemporal.getDerecho() == null) {
                return null;
            }

            // Caso 2: Un hijo (solo derecho o solo izquierdo)
            if (nodoTemporal.getIzquierdo() == null) {
                return nodoTemporal.getDerecho();
            }
            if (nodoTemporal.getDerecho() == null) {
                return nodoTemporal.getIzquierdo();
            }

            // Caso 3: Dos hijos
            NodoTree<E> sucesor = nodoMinimo(nodoTemporal.getDerecho());
            nodoTemporal.setValor(sucesor.getValor());
            nodoTemporal.setDerecho(deleteRecursivo(nodoTemporal.getDerecho(), sucesor.getValor()));
        }

        return nodoTemporal;
    }

    private NodoTree<E> nodoMinimo(NodoTree<E> nodoTemporal){
        while (nodoTemporal.getIzquierdo() != null) {
            nodoTemporal = nodoTemporal.getIzquierdo();
        }
        return nodoTemporal;
    }

    // Método para encontrar el predecesor en orden (implementado)
    private NodoTree<E> predecesorInOrden(NodoTree<E> nodo){
        if (nodo == null || nodo.getIzquierdo() == null) {
            return null;
        }

        // El predecesor es el nodo más a la derecha del subárbol izquierdo
        NodoTree<E> predecesor = nodo.getIzquierdo();
        while (predecesor.getDerecho() != null) {
            predecesor = predecesor.getDerecho();
        }
        return predecesor;
    }

    /*
    Recorridos del árbol según corresponda al recorrido.
     */

    //Subárbol Izquierdo – Nodo (Raíz) – Subárbol Derecho
    public void InOrder() throws MensajeException{
        if(isEmpty()){
            throw new MensajeException("Árbol vacío, no hay elementos.");
        }
        System.out.print("InOrden: [");
        InOrdenRecursivo(raiz);
        System.out.println("]");
    }

    //Subárbol Izquierdo – Nodo (Raíz) – Subárbol Derecho
    public void InOrdenRecursivo(NodoTree<E> nodoTemporal){
        if (nodoTemporal != null) {
            //Recorre subárbol izquierdo
            InOrdenRecursivo(nodoTemporal.getIzquierdo());

            //Visita la raíz (nodo actual)
            System.out.print(nodoTemporal.getValor() + " ");

            //Recorre subárbol derecho
            InOrdenRecursivo(nodoTemporal.getDerecho());
        }
    }

    //Subárbol Izquierdo – Subárbol Derecho – Nodo (Raíz)
    public void PostOrden() throws MensajeException {
        if(isEmpty()){
            throw new MensajeException("Árbol vacío, no hay elementos.");
        }
        System.out.print("PostOrden: [");
        PostOrdenRecursivo(raiz);
        System.out.println("]");
    }

    //Subárbol Izquierdo – Subárbol Derecho – Nodo (Raíz)
    public void PostOrdenRecursivo(NodoTree<E> nodoTemporal){
        if (nodoTemporal != null) {
            //Recorre subárbol izquierdo
            PostOrdenRecursivo(nodoTemporal.getIzquierdo());

            //Recorre subárbol derecho
            PostOrdenRecursivo(nodoTemporal.getDerecho());

            //Visita la raíz (nodo actual)
            System.out.print(nodoTemporal.getValor() + " ");
        }
    }

    //Nodo (Raíz) – Subárbol Izquierdo – Subárbol Derecho
    public void PreOrden() throws MensajeException{
        if(isEmpty()){
            throw new MensajeException("Árbol vacío, no hay elementos.");
        }
        System.out.print("PreOrden: [");
        PreOrdenRecursivo(raiz);
        System.out.println("]");
    }

    //Nodo (Raíz) – Subárbol Izquierdo – Subárbol Derecho
    public void PreOrdenRecursivo(NodoTree<E> nodoTemporal){
        if (nodoTemporal != null) {

            //Visita la raíz (nodo actual)
            System.out.print(nodoTemporal.getValor() + " ");

            //Recorre subárbol izquierdo
            PreOrdenRecursivo(nodoTemporal.getIzquierdo());

            //Recorre subárbol derecho
            PreOrdenRecursivo(nodoTemporal.getDerecho());
        }
    }

    // Implementación del método toString
    public String toString(){
        if (isEmpty()) {
            return "BST vacío";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("BST [").append(size).append(" elementos]: ");
        toStringRecursivo(raiz, sb);
        return sb.toString();
    }

    private void toStringRecursivo(NodoTree<E> nodo, StringBuilder sb) {
        if (nodo != null) {
            toStringRecursivo(nodo.getIzquierdo(), sb);
            sb.append(nodo.getValor()).append(" ");
            toStringRecursivo(nodo.getDerecho(), sb);
        }
    }

    // Métodos optimizados para encontrar min y max
    private E findMinNode(NodoTree<E> node) throws ItemNotFound {
        if (node == null) {
            throw new ItemNotFound();
        }

        //Buscar el nodo mínimo del subárbol (recorre por la izquierda)
        NodoTree<E> nodoTemporal = node;
        while (nodoTemporal.getIzquierdo() != null) {
            nodoTemporal = nodoTemporal.getIzquierdo();
        }

        // Retornar directamente el valor encontrado (ya sabemos que existe)
        return nodoTemporal.getValor();
    }

    private E findMaxNode(NodoTree<E> node) throws ItemNotFound {
        if (node == null) {
            throw new ItemNotFound();
        }

        //Buscar el nodo máximo del subárbol (recorre por la derecha)
        NodoTree<E> nodoTemporal = node;
        while (nodoTemporal.getDerecho() != null) {
            nodoTemporal = nodoTemporal.getDerecho();
        }

        // Retornar directamente el valor encontrado (ya sabemos que existe)
        return nodoTemporal.getValor();
    }

    public E getMin() throws ItemNotFound { //Método público para el uso del método findMinNode
        return findMinNode(raiz);
    }

    public E getMax() throws ItemNotFound { //Método público para el uso del método findMaxNode
        return findMaxNode(raiz);
    }

    // Método adicional para obtener la altura del árbol
    public int getHeight() {
        return getHeightRecursivo(raiz);
    }

    private int getHeightRecursivo(NodoTree<E> nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + Math.max(getHeightRecursivo(nodo.getIzquierdo()),
                getHeightRecursivo(nodo.getDerecho()));
    }

    // Método para verificar si el árbol está balanceado
    public boolean isBalanced() {
        return isBalancedRecursivo(raiz) != -1;
    }

    private int isBalancedRecursivo(NodoTree<E> nodo) {
        if (nodo == null) {
            return 0;
        }

        int leftHeight = isBalancedRecursivo(nodo.getIzquierdo());
        if (leftHeight == -1) return -1;

        int rightHeight = isBalancedRecursivo(nodo.getDerecho());
        if (rightHeight == -1) return -1;

        if (Math.abs(leftHeight - rightHeight) > 1) {
            return -1;
        }

        return Math.max(leftHeight, rightHeight) + 1;
    }
}
