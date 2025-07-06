package Hash;

import LinkedList.ListaEnlazada;
import LinkedList.MensajeException;

import java.util.ArrayList;

public class HashEncadenamiento<E> {
    private ArrayList<ListaEnlazada<E>> listaHash; //Tabla con listas enlazadas
    private int capacidad; //Capacidad de la tabla hash
    private int modulo; //Tamaño de la tabla para función hash
    private int contadorElementos; //Contador de elementos totales
    private final double factorCargaMaximo = 0.75; //Factor de carga máximo

    public HashEncadenamiento(int capacidad) {
        this.capacidad = capacidad;
        this.modulo = siguientePrimo(capacidad);
        this.listaHash = new ArrayList<>(this.capacidad);
        this.contadorElementos = 0;

        //Inicializar cada posición con una lista enlazada vacía
        for (int i = 0; i < this.capacidad; i++) {
            this.listaHash.add(new ListaEnlazada<E>());
        }
    }

    //Método para encontrar el siguiente número primo mayor o igual a n
    private int siguientePrimo(int numero) {
        if (numero <= 1) return 2;

        while (!esPrimo(numero)) {
            numero++;
        }
        return numero;
    }

    //Método para verificar si un número es primo
    private boolean esPrimo(int numero) {
        if (numero <= 1) return false;
        if (numero <= 3) return true;
        if (numero % 2 == 0 || numero % 3 == 0) return false;

        for (int i = 5; i * i <= numero; i += 6) {
            if (numero % i == 0 || numero % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    //Método para calcular el factor de carga actual
    public double factorCarga() {
        return (double) contadorElementos / capacidad;
    }

    /***********************************************************************************
     * MÉTODOS PARA HASHEAR SEGUN TIPO INTEGER O STRING
     ***********************************************************************************/

    //Método principal que calcula el índice según el tipo de clave
    public int principal(E clave) {
        int indice = 0;

        if (clave instanceof Number) {
            long numeroLong = ((Number) clave).longValue();
            indice = metodoPliegue(numeroLong);
        }
        else if (clave instanceof String) {
            indice = metodoSuma((String) clave);
        }
        else {
            // Usar hashCode() para otros tipos
            indice = clave.hashCode();
        }

        indice = Math.abs(indice) % modulo;
        return ajustarIndice(indice);
    }

    //Método por Pliegue para números
    public int metodoPliegue(long clave) {
        String numeroCadena = String.valueOf(clave);
        int suma = 0;

        //Recorrer de dos en dos
        for (int i = 0; i < numeroCadena.length(); i += 2) {
            String par = "";

            //Tomar el primer dígito
            par += numeroCadena.charAt(i);

            //Tomar el segundo dígito si existe
            if (i + 1 < numeroCadena.length()) {
                par += numeroCadena.charAt(i + 1);
            }

            //Sumar el par a la suma total
            suma += Integer.parseInt(par);
        }

        return suma;
    }

    //Método de suma para strings
    public int metodoSuma(String clave) {
        int suma = 0;
        for (int i = 0; i < clave.length(); i++) {
            suma += clave.charAt(i) * (i + 1);
        }
        return suma;
    }

    //Método auxiliar para ajustar el índice al tamaño real de la tabla
    private int ajustarIndice(int indice) {
        if (indice >= capacidad){
            return indice % capacidad;
        }
        return indice;
    }

    /***********************************************************************************
     * MÉTODOS PRINCIPALES DE LA TABLA HASH
     ***********************************************************************************/

    //Método para insertar una clave
    public boolean insertarClave(E clave) throws MensajeException {
        if (clave == null) return false;

        //Verificar si necesitamos rehashing antes de insertar
        if (factorCarga() >= factorCargaMaximo) {
            rehashing();
        }

        int indice = principal(clave); // Calcular posición
        ListaEnlazada<E> listaTemporal = listaHash.get(indice); // Obtener lista correspondiente

        listaTemporal.insertLast(clave); // Insertar siempre (permitir duplicados)
        contadorElementos++;
        return true;
    }

    //Método para buscar una clave
    public boolean buscarClave(E clave) {
        if (clave == null) return false;

        int indice = principal(clave); //Calcular posición
        return listaHash.get(indice).contains(clave); //Buscar en lista correspondiente
    }

    //Método para eliminar una clave
    public boolean eliminarClave(E clave) {
        if (clave == null) return false;

        int indice = principal(clave); //Calcular posición
        ListaEnlazada<E> listaTemporal = listaHash.get(indice); //Obtener lista
        try {
            if (listaTemporal.contains(clave)) { //Verificar existencia
                listaTemporal.removeNode(clave); //Eliminar de la lista
                contadorElementos--;
                return true;
            }
            return false;
        } catch (MensajeException e) {
            return false; //Error en eliminación
        }
    }

    //Método para redimensionar la tabla (rehashing)
    private void rehashing() {
        System.out.println("Rehashing... Factor de carga: " + factorCarga());

        //Guardar la tabla actual
        ArrayList<ListaEnlazada<E>> tablaAnterior = new ArrayList<>(listaHash);

        //Duplicar la capacidad y encontrar el siguiente primo
        int nuevaCapacidad = capacidad * 2;
        this.capacidad = siguientePrimo(nuevaCapacidad);
        this.modulo = this.capacidad;
        this.contadorElementos = 0;

        //Crear nueva tabla
        this.listaHash = new ArrayList<ListaEnlazada<E>>(this.capacidad);

        //Inicializar nueva tabla con listas vacías
        for (int i = 0; i < this.capacidad; i++) {
            this.listaHash.add(new ListaEnlazada<E>());
        }

        //Reinsertar todos los elementos de la tabla anterior
        for (ListaEnlazada<E> lista : tablaAnterior) {
            if (!lista.isEmpty()) {
                try {
                    //Recorrer cada elemento de la lista
                    for (int i = 0; i < lista.length(); i++) {
                        E elemento = lista.searchK(i);
                        insertarSinVerificarCarga(elemento);
                    }
                } catch (MensajeException e) {
                    System.err.println("Error durante rehashing: " + e.getMessage());
                }
            }
        }

        System.out.println("Rehashing completado. Nueva capacidad: " + capacidad);
    }

    //Método auxiliar para insertar sin verificar carga (usado en rehashing)
    private boolean insertarSinVerificarCarga(E clave) throws MensajeException {
        if (clave == null) return false;

        int indice = principal(clave);
        ListaEnlazada<E> listaTemporal = listaHash.get(indice);

        listaTemporal.insertLast(clave);
        contadorElementos++;
        return true;
    }

    //Método para mostrar la tabla
    public void mostrarTabla() {
        System.out.println("Tabla Hash con Encadenamiento:");
        System.out.println("Capacidad: " + capacidad);
        System.out.println("Elementos: " + contadorElementos);
        System.out.println("Factor de carga: " + String.format("%.2f", factorCarga()));
        System.out.println("------------------------");

        for (int i = 0; i < listaHash.size(); i++) {
            System.out.print("Bucket " + i + ": ");
            if (listaHash.get(i).isEmpty()) {
                System.out.println("[VACÍO]");
            } else {
                System.out.println(listaHash.get(i).toString());
            }
        }
    }

    //Métodos getter para información de la tabla
    public int getCapacidad() {
        return capacidad;
    }

    public int getContadorElementos() {
        return contadorElementos;
    }

    //Método getter para el módulo
    public int getModulo() {
        return modulo;
    }

    //Método para buscar una clave y retornar el elemento si se encuentra
    public E obtenerPorClave(E clave) throws MensajeException {
        if (clave == null) return null;
        int indice = principal(clave);
        ListaEnlazada<E> listaTemporal = listaHash.get(indice);
        //Recorrer la lista para encontrar el elemento que es 'equals' a la clave
        for (int i = 0; i < listaTemporal.length(); i++) {
            E elemento = listaTemporal.searchK(i);
            if (elemento != null && elemento.equals(clave)) { //Usar equals para comparar el contenido
                return elemento;
            }
        }
        return null; //No encontrado
    }

    //Método para obtener todos los elementos no nulos (necesario para mostrarProductosYStocks)
    public ArrayList<E> obtenerTodosLosElementos() throws MensajeException {
        ArrayList<E> todos = new ArrayList<>();
        for (ListaEnlazada<E> lista : listaHash) {
            if (!lista.isEmpty()) {
                for (int i = 0; i < lista.length(); i++) {
                    todos.add(lista.searchK(i));
                }
            }
        }
        return todos;
    }
}
