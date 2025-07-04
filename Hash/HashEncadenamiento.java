package Hash;

import LinkedList.ListaEnlazada;
import LinkedList.MensajeException;

import java.util.ArrayList;

public class HashEncadenamiento<E>{
    private ArrayList<ListaEnlazada<E>> listaHash; //Tabla con listas enlazadas
    private int modulo; //Tamaño de la tabla para función hash

    public HashEncadenamiento(int capacidad, int modulo){
        this.listaHash = new ArrayList<ListaEnlazada<E>>(capacidad);
        this.modulo = modulo;

        //Inicializar cada posición con una lista enlazada vacía
        for (int i = 0; i < capacidad; i++) {
            this.listaHash.add(new ListaEnlazada<E>());
        }
    }

    //Función hash genérica
    private int hash(E clave) {
        if (clave == null) return 0;
        return Math.abs(clave.hashCode()); //Valor absoluto para evitar negativos
    }

    public void insertarClave(E clave){
        int indice = hash(clave) % modulo; //Calcular posición
        ListaEnlazada<E> lista = listaHash.get(indice); //Obtener lista correspondiente
        if (!lista.contains(clave)) { //Evitar duplicados
            lista.insertFirst(clave); //Insertar al inicio (O(1))
        }
    }

    public boolean buscarClave(E clave){
        int indice = hash(clave) % modulo; //Calcular posición
        return listaHash.get(indice).contains(clave); //Buscar en lista correspondiente
    }

    public boolean eliminarClave(E clave){
        int indice = hash(clave) % modulo; //Calcular posición
        ListaEnlazada<E> lista = listaHash.get(indice); //Obtener lista
        try {
            if (lista.contains(clave)) { //Verificar existencia
                lista.removeNode(clave); //Eliminar de la lista
                return true;
            }
            return false;
        } catch (MensajeException e) {
            return false; //Error en eliminación
        }
    }

    public void mostrarTabla(){
        for (int i = 0; i < listaHash.size(); i++) {
            System.out.print("Bucket " + i + ": ");
            if (listaHash.get(i).isEmpty()) {
                System.out.println("[VACÍO]");
            } else {
                System.out.println(listaHash.get(i).toString()); //Mostrar contenido de lista
            }
        }
    }
}
