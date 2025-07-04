package Hash;

import java.util.ArrayList;

public class HashPruebaLineal<E>{

    private ArrayList<E> listaHash; //Tabla hash con sondeo lineal
    private int modulo; //Tamaño para función hash

    public HashPruebaLineal(int capacidad, int modulo){
        this.listaHash = new ArrayList<E>(capacidad);
        this.modulo = modulo;

        //Inicializar tabla con valores null
        for (int i = 0; i < capacidad; i++)
            this.listaHash.add(null);
    }

    //Función hash genérica
    private int hash(E clave) {
        if (clave == null) return 0;
        return Math.abs(clave.hashCode());
    }

    public int indice(E clave, int modulo){
        return hash(clave) % modulo; //Función hash básica
    }

    /**
     * INSERTAR UNA CLAVE EN LA LISTA
     **/
    public boolean insertarClave(E clave){
        int indice = hash(clave) % modulo; //Calcular posición inicial
        return insertarClaveRecursivo(clave, indice, 0);
    }

    public boolean insertarClaveRecursivo(E clave, int indice, int intentos){
        //Prevenir bucle infinito
        if (intentos >= listaHash.size()) {
            return false; //Tabla llena
        }

        //Asegurar que el índice esté dentro de los límites
        indice = indice % listaHash.size();

        if (listaHash.get(indice) == null){
            listaHash.set(indice, clave); //Insertar en posición libre
            return true; //Inserción exitosa
        } else if (listaHash.get(indice).equals(clave)) {
            return false; //Ya existe (evitar duplicados)
        } else{
            //Prueba lineal: siguiente posición
            return insertarClaveRecursivo(clave, indice + 1, intentos + 1);
        }
    }

    /**
     * BUSCAR UNA CLAVE EN LA LISTA
     **/
    public boolean buscarClave(E clave){
        int indice = hash(clave) % modulo; //Posición inicial
        return buscarClaveRecursivo(clave, indice, 0);
    }

    public boolean buscarClaveRecursivo(E clave, int indice, int intentos){
        //Prevenir bucle infinito
        if (intentos >= listaHash.size()) {
            return false;
        }

        //Asegurar que el índice esté dentro de los límites
        indice = indice % listaHash.size();

        if (listaHash.get(indice) == null){
            return false; //No encontrado
        } else if (listaHash.get(indice).equals(clave)){
            return true; //Encontrado
        } else{
            //Sondeo lineal: siguiente posición
            return buscarClaveRecursivo(clave, indice + 1, intentos + 1);
        }
    }

    /**
     * ELIMINAR UNA CLAVE DE LA LISTA
     **/
    public boolean eliminarClave(E clave){
        int indice = hash(clave) % modulo; //Posición inicial
        return eliminarClaveRecursivo(clave, indice, 0);
    }

    public boolean eliminarClaveRecursivo(E clave, int indice, int intentos){
        //Prevenir bucle infinito
        if (intentos >= listaHash.size()) {
            return false;
        }

        //Asegurar que el índice esté dentro de los límites
        indice = indice % listaHash.size();

        if (listaHash.get(indice) == null){
            return false; //No encontrado
        } else if (listaHash.get(indice).equals(clave)){
            listaHash.set(indice, null); //Eliminar poniendo null
            return true; //Eliminado exitosamente
        } else{
            //Sondeo lineal: siguiente posición
            return eliminarClaveRecursivo(clave, indice + 1, intentos + 1);
        }
    }

    //MÉTODO PARA VISUALIZAR LA TABLA
    public void mostrarTabla(){
        System.out.println("Estado de la tabla hash (Prueba Lineal):");
        for (int i = 0; i < listaHash.size(); i++) {
            E valor = listaHash.get(i);
            if (valor == null) {
                System.out.println("Índice " + i + ": [VACÍO]");
            } else {
                System.out.println("Índice " + i + ": " + valor);
            }
        }
        System.out.println();
    }
}