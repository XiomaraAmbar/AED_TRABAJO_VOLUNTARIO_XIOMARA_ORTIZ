package AVLTree;

import BinarySearchTree.*;
import Excepciones.*;

public class AVLTree<E extends Comparable<E>> implements BinarySearchTree<E> {
    private boolean altura;
    private AVLNodo<E> raiz;
    private int size;

    public AVLTree() {
        this.raiz = null;
        this.size = 0;
    }

    //Elimina todos los elementos del AVL dejándolo vacío
    public void destroy() {
        raiz = null;
        size = 0;
    }

    //Verifica si el AVL está vacío
    public boolean isEmpty() {
        return raiz == null;
    }

    //Retorna el número de elementos en el AVL
    public int size() {
        return size;
    }

    //Obtiene la altura de un nodo (null = 0)
    private int getAltura(AVLNodo<E> nodo) {
        if (nodo == null) {
            return 0;
        }
        return getHeightRecursivo(nodo);
    }

    //Calcula el factor de balance de un nodo
    private int getFactorBalance(AVLNodo<E> nodo) {
        if (nodo == null) {
            return 0;
        }
        return getAltura(nodo.getDerecho()) - getAltura(nodo.getIzquierdo());
    }

    /********************************************************************
     ************************* ROTACIONES AVL ***************************
     ********************************************************************/

    //Rotación izquierda -> casos derecha - derecha
    private AVLNodo<E> rotacionIzquierda(AVLNodo<E> nodo) {
        AVLNodo<E> nuevoRaiz = nodo.getDerecho();
        nodo.setDerecho(nuevoRaiz.getIzquierdo());
        nuevoRaiz.setIzquierdo(nodo);

        // Actualizar factores de balance
        nodo.factorBalance = getFactorBalance(nodo);
        nuevoRaiz.factorBalance = getFactorBalance(nuevoRaiz);

        return nuevoRaiz;
    }

    //Rotación derecha -> casos izquierda - izquierda
    private AVLNodo<E> rotacionDerecha(AVLNodo<E> nodo) {
        AVLNodo<E> nuevoRaiz = nodo.getIzquierdo();
        nodo.setIzquierdo(nuevoRaiz.getDerecho());
        nuevoRaiz.setDerecho(nodo);

        // Actualizar factores de balance
        nodo.factorBalance = getFactorBalance(nodo);
        nuevoRaiz.factorBalance = getFactorBalance(nuevoRaiz);

        return nuevoRaiz;
    }

    //Rotación izquierda-derecha -> casos izquierda - derecha
    private AVLNodo<E> rotacionIzquierdaDerecha(AVLNodo<E> nodo) {
        nodo.setIzquierdo(rotacionIzquierda(nodo.getIzquierdo()));
        return rotacionDerecha(nodo);
    }

    //Rotación derecha-izquierda -> casos derecha - izquierda
    private AVLNodo<E> rotacionDerechaIzquierda(AVLNodo<E> nodo) {
        nodo.setDerecho(rotacionDerecha(nodo.getDerecho()));
        return rotacionIzquierda(nodo);
    }

    //Método para balancear un nodo
    private AVLNodo<E> balancear(AVLNodo<E> nodo) {
        //Actualizar factor de balance
        nodo.factorBalance = getFactorBalance(nodo);

        //Caso Izquierda-Izquierda
        if (nodo.factorBalance < -1 && getFactorBalance(nodo.getIzquierdo()) <= 0) {
            return rotacionDerecha(nodo);
        }

        //Caso Derecha-Derecha
        if (nodo.factorBalance > 1 && getFactorBalance(nodo.getDerecho()) >= 0) {
            return rotacionIzquierda(nodo);
        }

        //Caso Izquierda-Derecha
        if (nodo.factorBalance < -1 && getFactorBalance(nodo.getIzquierdo()) > 0) {
            return rotacionIzquierdaDerecha(nodo);
        }

        //Caso Derecha-Izquierda
        if (nodo.factorBalance > 1 && getFactorBalance(nodo.getDerecho()) < 0) {
            return rotacionDerechaIzquierda(nodo);
        }

        return nodo;
    }

    /********************************************************************
     **************************** BÚSQUEDA  ****************************
     ********************************************************************/

    //Busca un elemento en el AVL
    public E search(E valor) throws ItemNotFound {
        E resultadoBusqueda = searchRecursivo(raiz, valor);
        if (resultadoBusqueda == null) {
            throw new ItemNotFound();
        }
        return resultadoBusqueda;
    }

    //Busca recursivamente el valor de un nodo en el árbol
    private E searchRecursivo(AVLNodo<E> nodoTemporal, E valor) {
        if (nodoTemporal == null) {
            return null;
        }

        int resultado = valor.compareTo(nodoTemporal.getValor());
        if (resultado > 0) {
            return searchRecursivo(nodoTemporal.getDerecho(), valor);
        } else if (resultado < 0) {
            return searchRecursivo(nodoTemporal.getIzquierdo(), valor);
        } else {
            return nodoTemporal.getValor();
        }
    }

    //Método de búsqueda privada para uso interno
    private E searchInterno(E valor) {
        return searchRecursivo(raiz, valor);
    }

    /********************************************************************
     **************************** INSERCIÓN  ****************************
     ********************************************************************/

    public void insert(E x) throws ItemDuplicated {
        this.altura = false;
        this.raiz = insertRecursivo(x, this.raiz);
        if (this.altura) {
            this.size++;
        }
    }

    private AVLNodo<E> insertRecursivo(E nuevoValor, AVLNodo<E> nodo) throws ItemDuplicated {
        //1. Inserción normal de BST
        if (nodo == null) {
            this.altura = true;
            return new AVLNodo<>(nuevoValor);
        }

        int resultado = nuevoValor.compareTo(nodo.getValor());

        if (resultado == 0) {
            throw new ItemDuplicated(nuevoValor + " ya se encuentra en el árbol...");
        }

        if (resultado < 0) {
            nodo.setIzquierdo(insertRecursivo(nuevoValor, nodo.getIzquierdo()));
        } else {
            nodo.setDerecho(insertRecursivo(nuevoValor, nodo.getDerecho()));
        }

        //2. Balancear el nodo si es necesario
        if (this.altura) {
            nodo = balancear(nodo);
        }

        return nodo;
    }

    /********************************************************************
     **************************** ELIMINACIÓN  ****************************
     ********************************************************************/

    //Elimina un elemento en el AVL
    public void delete(E valor) throws ExceptionIsEmpty {
        if (valor == null) {
            throw new IllegalArgumentException("El valor no puede ser null");
        }
        if (isEmpty()) {
            throw new ExceptionIsEmpty();
        }

        //Verifica si existe antes de eliminar
        if (searchInterno(valor) != null) {
            raiz = deleteRecursivo(raiz, valor);
            size--;
        }
    }

    //Eliminación recursiva con balanceo AVL
    private AVLNodo<E> deleteRecursivo(AVLNodo<E> nodo, E valor) {
        // 1. Eliminación normal de BST
        if (nodo == null) {
            return null;
        }

        int resultado = valor.compareTo(nodo.getValor());
        if (resultado < 0) {
            nodo.setIzquierdo(deleteRecursivo(nodo.getIzquierdo(), valor));
        } else if (resultado > 0) {
            nodo.setDerecho(deleteRecursivo(nodo.getDerecho(), valor));
        } else {
            //Nodo a eliminar encontrado
            if (nodo.getIzquierdo() == null && nodo.getDerecho() == null) {
                return null;
            }
            if (nodo.getIzquierdo() == null) {
                return nodo.getDerecho();
            }
            if (nodo.getDerecho() == null) {
                return nodo.getIzquierdo();
            }

            //Nodo con dos hijos
            AVLNodo<E> sucesor = nodoMinimo(nodo.getDerecho());
            nodo.setValor(sucesor.getValor());
            nodo.setDerecho(deleteRecursivo(nodo.getDerecho(), sucesor.getValor()));
        }

        //2. Balancear el nodo
        return balancear(nodo);
    }

    //Encuentra el nodo mínimo en un subárbol
    private AVLNodo<E> nodoMinimo(AVLNodo<E> nodoTemporal) {
        while (nodoTemporal.getIzquierdo() != null) {
            nodoTemporal = nodoTemporal.getIzquierdo();
        }
        return nodoTemporal;
    }

    /********************************************************************
     **************************** RECORRIDOS  ****************************
     ********************************************************************/

    //Subárbol Izquierdo – Nodo (Raíz) – Subárbol Derecho
    public void InOrden() throws MensajeException{
        if(isEmpty()){
            throw new MensajeException("Árbol vacío, no hay elementos.");
        }
        System.out.print("Recorrido InOrden: [");
        InOrdenRecursivo(raiz);
        System.out.println("]");
    }

    private void InOrdenRecursivo(AVLNodo<E> nodoTemporal){
        if (nodoTemporal != null) {
            InOrdenRecursivo(nodoTemporal.getIzquierdo());
            System.out.print(nodoTemporal.getValor() + " ");
            InOrdenRecursivo(nodoTemporal.getDerecho());
        }
    }

    //Subárbol Izquierdo – Subárbol Derecho – Nodo (Raíz)
    public void PostOrden() throws MensajeException {
        if(isEmpty()){
            throw new MensajeException("Árbol vacío, no hay elementos.");
        }
        System.out.print("Recorrido PostOrden: [");
        PostOrdenRecursivo(raiz);
        System.out.println("]");
    }

    private void PostOrdenRecursivo(AVLNodo<E> nodoTemporal){
        if (nodoTemporal != null) {
            PostOrdenRecursivo(nodoTemporal.getIzquierdo());
            PostOrdenRecursivo(nodoTemporal.getDerecho());
            System.out.print(nodoTemporal.getValor() + " ");
        }
    }

    //Nodo (Raíz) – Subárbol Izquierdo – Subárbol Derecho
    public void PreOrden() throws MensajeException{
        if(isEmpty()){
            throw new MensajeException("Árbol vacío, no hay elementos.");
        }
        System.out.print("Recorrido PreOrden: [");
        PreOrdenRecursivo(raiz);
        System.out.println("]");
    }

    private void PreOrdenRecursivo(AVLNodo<E> nodoTemporal){
        if (nodoTemporal != null) {
            System.out.print(nodoTemporal.getValor() + " ");
            PreOrdenRecursivo(nodoTemporal.getIzquierdo());
            PreOrdenRecursivo(nodoTemporal.getDerecho());
        }
    }

    //Implementación del método toString
    public String toString(){
        if (isEmpty()) {
            return "AVL vacío";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("AVL [").append(size).append(" elementos]: ");
        toStringRecursivo(raiz, sb);
        return sb.toString();
    }

    private void toStringRecursivo(AVLNodo<E> nodo, StringBuilder sb) {
        if (nodo != null) {
            toStringRecursivo(nodo.getIzquierdo(), sb);
            sb.append(nodo.getValor()).append(" ");
            toStringRecursivo(nodo.getDerecho(), sb);
        }
    }

    //Métodos para encontrar minimo y maximo
    private E searchminimoNodo(AVLNodo<E> node) throws ItemNotFound {
        if (node == null) {
            throw new ItemNotFound();
        }
        AVLNodo<E> nodoTemporal = node;
        while (nodoTemporal.getIzquierdo() != null) {
            nodoTemporal = nodoTemporal.getIzquierdo();
        }
        return nodoTemporal.getValor();
    }

    private E searchMaximoNodo(AVLNodo<E> node) throws ItemNotFound {
        if (node == null) {
            throw new ItemNotFound();
        }
        AVLNodo<E> nodoTemporal = node;
        while (nodoTemporal.getDerecho() != null) {
            nodoTemporal = nodoTemporal.getDerecho();
        }
        return nodoTemporal.getValor();
    }

    public E getMin() throws ItemNotFound {
        return searchminimoNodo(raiz);
    }

    public E getMax() throws ItemNotFound {
        return searchMaximoNodo(raiz);
    }

    //Método para obtener la altura del árbol
    public int getHeight() {
        return getHeightRecursivo(raiz);
    }

    private int getHeightRecursivo(AVLNodo<E> nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + Math.max(getHeightRecursivo(nodo.getIzquierdo()),
                getHeightRecursivo(nodo.getDerecho()));
    }

    //Método para mostrar el factor de balance de cada nodo
    public void mostrarFactoresBalance() {
        if (isEmpty()) {
            System.out.println("AVL vacío");
            return;
        }
        System.out.println("Factores de balance:");
        mostrarFactoresBalanceRecursivo(raiz, 0);
    }

    private void mostrarFactoresBalanceRecursivo(AVLNodo<E> nodo, int nivel) {
        if (nodo != null) {
            mostrarFactoresBalanceRecursivo(nodo.getDerecho(), nivel + 1);

            //Mostrar indentación según el nivel
            for (int i = 0; i < nivel; i++) {
                System.out.print("    ");
            }
            System.out.println(nodo.getValor() + " (FB: " + getFactorBalance(nodo) + ")");

            mostrarFactoresBalanceRecursivo(nodo.getIzquierdo(), nivel + 1);
        }
    }
}