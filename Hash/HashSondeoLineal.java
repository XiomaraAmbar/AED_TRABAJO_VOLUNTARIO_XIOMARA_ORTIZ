package Hash;

import java.util.ArrayList;

public class HashSondeoLineal<E> {
    private ArrayList<E> listaHash; //Tabla hash con sondeo lineal
    private int modulo; //Tamaño de la lista y modulo
    private int contadorElementos;
    private int capacidad;
    private final double factorCargaMaximo = 0.75;

    public HashSondeoLineal(int capacidad){
        this.capacidad = capacidad;
        this.modulo = siguientePrimo(capacidad);
        this.listaHash = new ArrayList<E>(capacidad);
        this.contadorElementos = 0;

        //Inicializar tabla con valores null
        for (int i = 0; i < capacidad; i++)
            this.listaHash.add(null);
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
            System.out.println("Tipo de clave no soportado");
            return -1; //Valor de error
        }

        indice = Math.abs(indice) % modulo;
        return ajustarIndice(indice);
    }

    //Método por Pliegue
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
     * MÉTODO INSERTAR CLAVE EN TABLA HASH
     ***********************************************************************************/

    //Método para insertar un elemento en la tabla hash
    public boolean insertar(E clave) {
        if (clave == null) return false;

        //Verificar si necesitamos rehashing antes de insertar
        if (factorCarga() >= factorCargaMaximo) {
            rehashing();
        }

        int indice = principal(clave);
        if (indice == -1) return false; //Tipo no soportado

        //Sondeo lineal para encontrar posición libre
        int indiceOriginal = indice;
        while (listaHash.get(indice) != null) {
            //Si el elemento ya existe, no lo insertamos
            if (listaHash.get(indice).equals(clave)) {
                return false;
            }
            indice = (indice + 1) % modulo;

            //Si hemos dado una vuelta completa, la tabla está llena
            if (indice == indiceOriginal) {
                return false;
            }
        }

        // Insertar el elemento
        listaHash.set(indice, clave);
        contadorElementos++;
        return true;
    }

    //Método para buscar un elemento
    public boolean buscar(E clave) {
        if (clave == null) return false;

        int indice = principal(clave);
        if (indice == -1) return false;

        int indiceOriginal = indice;
        while (listaHash.get(indice) != null) {
            if (listaHash.get(indice).equals(clave)) {
                return true;
            }
            indice = (indice + 1) % modulo;

            if (indice == indiceOriginal) {
                break;
            }
        }
        return false;
    }

    //Método para eliminar un elemento (versión simple)
    public boolean eliminar(E clave) {
        if (clave == null) return false;

        int indice = principal(clave);
        if (indice == -1) return false;

        //Buscar el elemento recorriendo toda la tabla si es necesario
        for (int i = 0; i < modulo; i++) {
            int indiceActual = (indice + i) % modulo;

            if (listaHash.get(indiceActual) == null) {
                //Llegamos a un espacio vacío, el elemento no existe
                return false;
            }

            if (listaHash.get(indiceActual).equals(clave)) {
                //Encontramos el elemento, lo eliminamos
                listaHash.set(indiceActual, null);
                contadorElementos--;
                return true;
            }
        }

        return false; //No encontrado después de recorrer toda la tabla
    }

    //Método para redimensionar la tabla (rehashing)
    private void rehashing() {
        System.out.println("Rehashing... Factor de carga: " + factorCarga());

        //Guardar la tabla actual
        ArrayList<E> tablaAnterior = new ArrayList<>(listaHash);

        //Duplicar la capacidad y encontrar el siguiente primo
        int nuevaCapacidad = capacidad * 2;
        int nuevoModulo = siguientePrimo(nuevaCapacidad);

        //Crear nueva tabla
        this.capacidad = nuevaCapacidad;
        this.modulo = nuevoModulo;
        this.listaHash = new ArrayList<E>(capacidad);
        this.contadorElementos = 0;

        //Inicializar nueva tabla con null
        for (int i = 0; i < capacidad; i++) {
            this.listaHash.add(null);
        }

        //Reinsertar todos los elementos de la tabla anterior
        for (E elemento : tablaAnterior) {
            if (elemento != null) {
                insertarSinVerificarCarga(elemento);
            }
        }

        System.out.println("Rehashing completado. Nueva capacidad: " + capacidad);
    }

    //Método auxiliar para insertar sin verificar carga (usado en rehashing)
    private boolean insertarSinVerificarCarga(E clave) {
        if (clave == null) return false;

        int indice = principal(clave);
        if (indice == -1) return false;

        //Sondeo lineal para encontrar posición libre
        int indiceOriginal = indice;
        while (listaHash.get(indice) != null) {
            //Si el elemento ya existe, no lo insertamos
            if (listaHash.get(indice).equals(clave)) {
                return false;
            }
            indice = (indice + 1) % modulo;

            //Si hemos dado una vuelta completa, la tabla está llena
            if (indice == indiceOriginal) {
                return false;
            }
        }

        // Insertar el elemento
        listaHash.set(indice, clave);
        contadorElementos++;
        return true;
    }

    //Método para mostrar la tabla
    public void mostrarTabla() {
        System.out.println("Tabla Hash:");
        System.out.println("Capacidad: " + capacidad);
        System.out.println("Elementos: " + contadorElementos);
        System.out.println("Factor de carga: " + String.format("%.2f", factorCarga()));
        System.out.println("------------------------");
        for (int i = 0; i < listaHash.size(); i++) {
            System.out.println("Índice " + i + ": " + listaHash.get(i));
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
}
