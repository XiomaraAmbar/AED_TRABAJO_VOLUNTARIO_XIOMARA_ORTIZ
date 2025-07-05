package BTreePlus;

import java.util.ArrayList;

public class BNodePlus<E extends Comparable<E>> {
    //True para nodos hoja, False para nodos internos
    protected boolean esHoja;
    protected ArrayList<E> claves; //Las claves almacenadas
    protected ArrayList<BNodePlus<E>> hijos; //Nodos hijos
    protected BNodePlus<E> siguiente; //Enlace al siguiente nodo hoja
    protected int contadorClaves; //Contador de claves del nodo
    protected int orden; //Orden del árbol

    //Identificación única para nodos
    private static int contadorId = 0;
    private int idNodo;

    //Constructor para inicializar un nodo
    public BNodePlus(boolean esHoja, int orden) {
        this.esHoja = esHoja;
        this.orden = orden;
        this.claves = new ArrayList<>(orden - 1);
        this.hijos = new ArrayList<>(orden);
        this.siguiente = null;
        this.contadorClaves = 0;

        //Inicializar listas con capacidad apropiada
        if (esHoja) {
            //Nodos hoja: pueden tener hasta (orden-1) claves
            for (int i = 0; i < orden - 1; i++) {
                this.claves.add(null);
            }
        } else {
            //Nodos internos: pueden tener hasta (orden-1) claves y orden hijos
            for (int i = 0; i < orden - 1; i++) {
                this.claves.add(null);
            }
            for (int i = 0; i < orden; i++) {
                this.hijos.add(null);
            }
        }

        asignarId();
    }

    //Verifica si el nodo está lleno
    public boolean isFull() {
        return this.contadorClaves == this.orden - 1;
    }

    //Verifica si el nodo está vacío
    public boolean isEmpty() {
        return this.contadorClaves == 0;
    }

    //Busca una clave en el nodo y retorna su posición
    public boolean searchKey(E clave, int[] posicion) {
        int i = 0;

        //Busca posición correcta comparando con claves existentes
        while (i < this.contadorClaves && clave.compareTo(this.claves.get(i)) > 0) {
            i++;
        }

        posicion[0] = i;

        //Verifica si la clave fue encontrada exactamente
        if (i < this.contadorClaves && clave.compareTo(this.claves.get(i)) == 0) {
            return true; //Clave encontrada
        } else {
            return false; //Clave no encontrada
        }
    }

    //Obtiene una clave por índice
    public E getKey(int indice) {
        if (indice >= 0 && indice < this.contadorClaves) {
            return this.claves.get(indice);
        }
        return null;
    }

    //Obtiene un hijo por índice -> solo para nodos internos
    public BNodePlus<E> getChild(int indice) {
        if (!this.esHoja && indice >= 0 && indice < this.hijos.size()) {
            return this.hijos.get(indice);
        }
        return null;
    }

    //Establece un hijo en una posición específica -> solo para nodos internos
    public void setChild(int indice, BNodePlus<E> hijo) {
        if (!this.esHoja) {
            // Expandir ArrayList si no tiene suficiente capacidad
            while (this.hijos.size() <= indice) {
                this.hijos.add(null);
            }
            this.hijos.set(indice, hijo);
        }
    }

    //Inserta una clave manteniendo el orden ascendente
    public void insertKey(E clave) {
        int position = 0;

        //Encontrar posición de inserción manteniendo orden
        while (position < this.contadorClaves && clave.compareTo(this.claves.get(position)) > 0) {
            position++;
        }

        //Desplazar claves hacia la derecha para hacer espacio
        for (int i = this.contadorClaves; i > position; i--) {
            if (this.claves.size() <= i) {
                this.claves.add(null);
            }
            this.claves.set(i, this.claves.get(i - 1));
        }

        //Insertar nueva clave en posición correcta
        if (this.claves.size() <= position) {
            this.claves.add(null);
        }
        this.claves.set(position, clave);
        this.contadorClaves++;
    }

    //Elimina una clave específica del nodo
    public void removeKey(E clave) {
        int posicion = -1;

        //Localizar posición de la clave a eliminar
        for (int i = 0; i < this.contadorClaves; i++) {
            if (this.claves.get(i).compareTo(clave) == 0) {
                posicion = i;
                break;
            }
        }

        if (posicion != -1) {
            //Desplazar claves hacia la izquierda para llenar hueco
            for (int i = posicion; i < this.contadorClaves - 1; i++) {
                this.claves.set(i, this.claves.get(i + 1));
            }
            this.contadorClaves--;
        }
    }

    //Elimina una clave por índice
    public void removeKeyIndex(int indice) {
        if (indice >= 0 && indice < this.contadorClaves) {
            //Desplazar claves hacia la izquierda
            for (int i = indice; i < this.contadorClaves - 1; i++) {
                this.claves.set(i, this.claves.get(i + 1));
            }
            this.contadorClaves--;
        }
    }

    //Inserta un hijo en una posición específica -> solo para nodos internos
    public void insertChild(int indice, BNodePlus<E> hijo) {
        if (!this.esHoja) {
            //Desplazar hijos hacia la derecha
            for (int i = this.getChildCount(); i > indice; i--) {
                if (this.hijos.size() <= i) {
                    this.hijos.add(null);
                }
                this.hijos.set(i, this.hijos.get(i - 1));
            }

            //Insertar nuevo hijo
            if (this.hijos.size() <= indice) {
                this.hijos.add(null);
            }
            this.hijos.set(indice, hijo);
        }
    }

    //Elimina un hijo en la posición especificada -> solo para nodos internos
    public void removeChild(int posicion) {
        if (!this.esHoja && posicion >= 0 && posicion < this.hijos.size()) {
            //Mueve hijos hacia la izquierda
            for (int i = posicion; i < this.hijos.size() - 1; i++) {
                this.hijos.set(i, this.hijos.get(i + 1));
            }

            //Limpia última posición o reducir tamaño
            if (this.hijos.size() > 0) {
                this.hijos.set(this.hijos.size() - 1, null);
            }
        }
    }

    //Establece una clave en la posición especificada
    public void setKey(int posicion, E clave) {
        if (posicion >= 0 && posicion < this.contadorClaves) {
            this.claves.set(posicion, clave);
        }
    }

    //Inserta una clave en una posición específica (alternativa a insertKey)
    public void insertKeyIndex(int posicion, E clave) {
        if (posicion >= 0 && posicion <= this.contadorClaves && this.contadorClaves < this.orden - 1) {
            //Expandir ArrayList si es necesario
            while (this.claves.size() <= this.contadorClaves) {
                this.claves.add(null);
            }

            //Mover claves hacia la derecha
            for (int i = this.contadorClaves; i > posicion; i--) {
                this.claves.set(i, this.claves.get(i - 1));
            }

            //Insertar nueva clave
            this.claves.set(posicion, clave);
            this.contadorClaves++;
        }
    }


    //Cuenta hijos no nulos del nodo
    public int getChildCount() {
        if (this.esHoja) {
            return 0;
        }

        int contador = 0;
        for (int i = 0; i <= this.contadorClaves; i++) {
            if (i < this.hijos.size() && this.hijos.get(i) != null) {
                contador = i + 1;
            }
        }
        return contador;
    }

    //Obtiene el número mínimo de claves permitidas
    public int getMinKeys() {
        if (this.esHoja) {
            return (this.orden - 1) / 2; //Para nodos hoja
        } else {
            return (this.orden / 2) - 1; //Para nodos internos
        }
    }

    //Verifica si el nodo tiene el mínimo de claves requeridas
    public boolean hasMinKeys() {
        return this.contadorClaves >= getMinKeys();
    }

    //Métodos para el enlace de nodos hoja
    public BNodePlus<E> getNext() {
        return this.siguiente;
    }

    public void setNext(BNodePlus<E> siguiente) {
        if (this.esHoja) {
            this.siguiente = siguiente;
        }
    }

    //Identificación única para nodos
    public static void reiniciarContadorId() {
        contadorId = 0;
    }

    private void asignarId() {
        this.idNodo = ++contadorId;
    }

    public String getIdNode() {
        if (this.idNodo == 0) {
            asignarId();
        }
        return String.format("%02d", this.idNodo);
    }

    public boolean esHoja() {
        return this.esHoja;
    }

    public int getContadorClaves() {
        return this.contadorClaves;
    }

    //Representación en cadena del nodo
    public String toString() {
        StringBuilder resultado = new StringBuilder();

        //Indicar tipo de nodo
        if (this.esHoja) {
            resultado.append("Nodo Hoja[");
        } else {
            resultado.append("Nodo interno[");
        }

        //Mostrar claves
        for (int i = 0; i < this.contadorClaves; i++) {
            if (i > 0) {
                resultado.append(", ");
            }
            resultado.append(this.claves.get(i));
        }

        resultado.append("]");

        //Para nodos hoja, mostrar si hay enlace al siguiente
        if (this.esHoja && this.siguiente != null) {
            resultado.append(" -> ").append(this.siguiente.getIdNode());
        }

        return resultado.toString();
    }

}