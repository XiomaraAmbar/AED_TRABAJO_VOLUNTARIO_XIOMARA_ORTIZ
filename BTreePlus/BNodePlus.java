package BTreePlus;

import java.util.ArrayList;

public class BNodePlus<E extends Comparable<E>> {
    //Define si el nodo es una hoja terminal o un nodo interno del árbol
    protected boolean esHoja;
    //Almacena las claves del nodo en orden ascendente usando ArrayList
    protected ArrayList<E> claves;
    //Contiene referencias a los nodos hijos para navegación en nodos internos
    protected ArrayList<BNodePlus<E>> hijos;
    //Enlaza horizontalmente los nodos hoja para recorrido secuencial
    protected BNodePlus<E> siguiente;
    //Cuenta el número actual de claves almacenadas en el nodo
    protected int contadorClaves;
    //Define el orden máximo del árbol B+ que determina capacidad del nodo
    protected int orden;

    //Genera identificadores únicos incrementales para cada nodo creado
    private static int contadorId = 0;
    //Almacena el identificador único asignado a este nodo específico
    private int idNodo;

    //Inicializa un nuevo nodo configurando su tipo y capacidades según el orden
    public BNodePlus(boolean esHoja, int orden) {
        this.esHoja = esHoja;
        this.orden = orden;
        //Crea lista de claves con capacidad máxima de orden-1 elementos
        this.claves = new ArrayList<>(orden - 1);
        //Crea lista de hijos con capacidad máxima de orden elementos
        this.hijos = new ArrayList<>(orden);
        this.siguiente = null;
        this.contadorClaves = 0;

        //Prellena con valores nulos la lista de claves según el tipo de nodo
        if (esHoja) {
            //Nodos hoja pueden almacenar hasta orden-1 claves
            for (int i = 0; i < orden - 1; i++) {
                this.claves.add(null);
            }
        } else {
            //Nodos internos almacenan hasta orden-1 claves y orden hijos
            for (int i = 0; i < orden - 1; i++) {
                this.claves.add(null);
            }
            for (int i = 0; i < orden; i++) {
                this.hijos.add(null);
            }
        }

        //Asigna un identificador único al nodo recién creado
        asignarId();
    }

    //Verifica si el nodo alcanzó su capacidad máxima de claves
    public boolean isFull() {
        return this.contadorClaves == this.orden - 1;
    }

    //Comprueba si el nodo no contiene ninguna clave almacenada
    public boolean isEmpty() {
        return this.contadorClaves == 0;
    }

    //Busca una clave específica en el nodo y retorna su posición mediante referencia
    public boolean searchKey(E clave, int[] posicion) {
        int i = 0;

        //Recorre las claves existentes hasta encontrar la posición correcta
        while (i < this.contadorClaves && clave.compareTo(this.claves.get(i)) > 0) {
            i++;
        }

        //Almacena la posición encontrada en el array de referencia
        posicion[0] = i;

        //Determina si la clave existe exactamente en la posición encontrada
        if (i < this.contadorClaves && clave.compareTo(this.claves.get(i)) == 0) {
            return true; //Clave encontrada
        } else {
            return false; //Clave no encontrada
        }
    }

    //Obtiene la clave almacenada en el índice especificado del nodo
    public E getKey(int indice) {
        if (indice >= 0 && indice < this.contadorClaves) {
            return this.claves.get(indice);
        }
        return null;
    }

    //Retorna el nodo hijo ubicado en el índice dado para nodos internos
    public BNodePlus<E> getChild(int indice) {
        if (!this.esHoja && indice >= 0 && indice < this.hijos.size()) {
            return this.hijos.get(indice);
        }
        return null;
    }

    //Establece un nodo hijo en la posición especificada para nodos internos
    public void setChild(int indice, BNodePlus<E> hijo) {
        if (!this.esHoja) {
            //Expande el ArrayList si el índice excede el tamaño actual
            while (this.hijos.size() <= indice) {
                this.hijos.add(null);
            }
            this.hijos.set(indice, hijo);
        }
    }

    //Inserta una clave nueva manteniendo el orden ascendente en el nodo
    public void insertKey(E clave) {
        int position = 0;

        //Localiza la posición correcta para insertar manteniendo el orden
        while (position < this.contadorClaves && clave.compareTo(this.claves.get(position)) > 0) {
            position++;
        }

        //Desplaza todas las claves hacia la derecha para crear espacio
        for (int i = this.contadorClaves; i > position; i--) {
            if (this.claves.size() <= i) {
                this.claves.add(null);
            }
            this.claves.set(i, this.claves.get(i - 1));
        }

        //Coloca la nueva clave en la posición correcta calculada
        if (this.claves.size() <= position) {
            this.claves.add(null);
        }
        this.claves.set(position, clave);
        this.contadorClaves++;
    }

    //Elimina una clave específica del nodo buscándola por valor
    public void removeKey(E clave) {
        int posicion = -1;

        //Busca la posición exacta de la clave a eliminar
        for (int i = 0; i < this.contadorClaves; i++) {
            if (this.claves.get(i).compareTo(clave) == 0) {
                posicion = i;
                break;
            }
        }

        if (posicion != -1) {
            //Desplaza todas las claves hacia la izquierda para llenar el hueco
            for (int i = posicion; i < this.contadorClaves - 1; i++) {
                this.claves.set(i, this.claves.get(i + 1));
            }
            this.contadorClaves--;
        }
    }

    //Remueve la clave ubicada en el índice especificado del nodo
    public void removeKeyIndex(int indice) {
        if (indice >= 0 && indice < this.contadorClaves) {
            //Mueve todas las claves hacia la izquierda desde el índice
            for (int i = indice; i < this.contadorClaves - 1; i++) {
                this.claves.set(i, this.claves.get(i + 1));
            }
            this.contadorClaves--;
        }
    }

    //Inserta un nodo hijo en la posición especificada para nodos internos
    public void insertChild(int indice, BNodePlus<E> hijo) {
        if (!this.esHoja) {
            //Desplaza todos los hijos hacia la derecha desde el índice
            for (int i = this.getChildCount(); i > indice; i--) {
                if (this.hijos.size() <= i) {
                    this.hijos.add(null);
                }
                this.hijos.set(i, this.hijos.get(i - 1));
            }

            //Coloca el nuevo hijo en la posición especificada
            if (this.hijos.size() <= indice) {
                this.hijos.add(null);
            }
            this.hijos.set(indice, hijo);
        }
    }

    //Elimina el nodo hijo ubicado en la posición dada para nodos internos
    public void removeChild(int posicion) {
        if (!this.esHoja && posicion >= 0 && posicion < this.hijos.size()) {
            //Desplaza todos los hijos hacia la izquierda desde la posición
            for (int i = posicion; i < this.hijos.size() - 1; i++) {
                this.hijos.set(i, this.hijos.get(i + 1));
            }

            //Limpia la última posición estableciéndola como null
            if (this.hijos.size() > 0) {
                this.hijos.set(this.hijos.size() - 1, null);
            }
        }
    }

    //Modifica directamente la clave en la posición especificada del nodo
    public void setKey(int posicion, E clave) {
        if (posicion >= 0 && posicion < this.contadorClaves) {
            this.claves.set(posicion, clave);
        }
    }

    //Inserta una clave en el índice exacto especificado sin buscar posición
    public void insertKeyIndex(int posicion, E clave) {
        if (posicion >= 0 && posicion <= this.contadorClaves && this.contadorClaves < this.orden - 1) {
            //Expande el ArrayList si no tiene capacidad suficiente
            while (this.claves.size() <= this.contadorClaves) {
                this.claves.add(null);
            }

            //Mueve todas las claves hacia la derecha desde la posición
            for (int i = this.contadorClaves; i > posicion; i--) {
                this.claves.set(i, this.claves.get(i - 1));
            }

            //Coloca la nueva clave en la posición exacta especificada
            this.claves.set(posicion, clave);
            this.contadorClaves++;
        }
    }

    //Cuenta el número total de hijos no nulos en el nodo
    public int getChildCount() {
        if (this.esHoja) {
            return 0;
        }

        int contador = 0;
        //Recorre hasta encontrar el último hijo no nulo consecutivo
        for (int i = 0; i <= this.contadorClaves; i++) {
            if (i < this.hijos.size() && this.hijos.get(i) != null) {
                contador = i + 1;
            }
        }
        return contador;
    }

    //Calcula el número mínimo de claves requeridas según el tipo de nodo
    public int getMinKeys() {
        if (this.esHoja) {
            return (this.orden - 1) / 2; //Para nodos hoja
        } else {
            return (this.orden / 2) - 1; //Para nodos internos
        }
    }

    //Verifica si el nodo cumple con el mínimo de claves requeridas
    public boolean hasMinKeys() {
        return this.contadorClaves >= getMinKeys();
    }

    //Obtiene la referencia al siguiente nodo hoja en la secuencia horizontal
    public BNodePlus<E> getNext() {
        return this.siguiente;
    }

    //Establece el enlace al siguiente nodo hoja para recorrido secuencial
    public void setNext(BNodePlus<E> siguiente) {
        if (this.esHoja) {
            this.siguiente = siguiente;
        }
    }

    //Reinicia el contador global de identificadores a cero
    public static void reiniciarContadorId() {
        contadorId = 0;
    }

    //Asigna un identificador único incrementando el contador global
    private void asignarId() {
        this.idNodo = ++contadorId;
    }

    //Retorna el identificador del nodo formateado como cadena de dos dígitos
    public String getIdNode() {
        if (this.idNodo == 0) {
            asignarId();
        }
        return String.format("%02d", this.idNodo);
    }

    //Retorna verdadero si el nodo es una hoja terminal
    public boolean esHoja() {
        return this.esHoja;
    }

    //Obtiene el número actual de claves almacenadas en el nodo
    public int getContadorClaves() {
        return this.contadorClaves;
    }

    //Genera una representación textual del nodo mostrando tipo y claves
    public String toString() {
        StringBuilder resultado = new StringBuilder();

        //Identifica el tipo de nodo en la representación
        if (this.esHoja) {
            resultado.append("Nodo Hoja[");
        } else {
            resultado.append("Nodo interno[");
        }

        //Construye la lista de claves separadas por comas
        for (int i = 0; i < this.contadorClaves; i++) {
            if (i > 0) {
                resultado.append(", ");
            }
            resultado.append(this.claves.get(i));
        }

        resultado.append("]");

        //Muestra el enlace al siguiente nodo para nodos hoja
        if (this.esHoja && this.siguiente != null) {
            resultado.append(" -> ").append(this.siguiente.getIdNode());
        }

        return resultado.toString();
    }
}