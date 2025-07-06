package MatrizDispersa;

import LinkedList.ListaEnlazada;
import LinkedList.MensajeException;

public class MatrizDispersa<T> {

    //Clase interna para representar cada elemento de la matriz
    private class ElementoMatriz {
        private int fila;
        private int columna;
        private T valor;

        public ElementoMatriz(int fila, int columna, T valor) {
            this.fila = fila;
            this.columna = columna;
            this.valor = valor;
        }

        public int getFila() {
            return fila;
        }

        public int getColumna() {
            return columna;
        }

        public T getValor() {
            return valor;
        }

        public void setValor(T valor) {
            this.valor = valor;
        }

        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            ElementoMatriz elemento = (ElementoMatriz) obj;
            return fila == elemento.fila && columna == elemento.columna;
        }

        public String toString() {
            return "(" + fila + "," + columna + ") = " + valor;
        }
    }

    private ListaEnlazada<ElementoMatriz> elementos;
    private int totalFilas;
    private int totalColumnas;
    private T valorPorDefecto;

    //Constructor principal
    public MatrizDispersa(int filas, int columnas, T valorPorDefecto) {
        if (filas <= 0 || columnas <= 0) {
            throw new IllegalArgumentException("Las dimensiones deben ser positivas");
        }
        this.totalFilas = filas;
        this.totalColumnas = columnas;
        this.valorPorDefecto = valorPorDefecto;
        this.elementos = new ListaEnlazada<>();
    }

    //Constructor sin valor por defecto (será null)
    public MatrizDispersa(int filas, int columnas) {
        this(filas, columnas, null);
    }

    //Valida que las coordenadas estén dentro de los límites
    private void validarCoordenadas(int fila, int columna) {
        if (fila < 0 || fila >= totalFilas || columna < 0 || columna >= totalColumnas) {
            throw new IndexOutOfBoundsException(
                    "Coordenadas fuera de rango: (" + fila + "," + columna +
                            "). Rango válido: [0," + (totalFilas - 1) + "] x [0," + (totalColumnas - 1) + "]");
        }
    }

    //Busca un elemento en la lista por coordenadas
    private ElementoMatriz buscarElemento(int fila, int columna) {
        ElementoMatriz elementoBuscado = new ElementoMatriz(fila, columna, null);

        try {
            // Recorrer la lista buscando el elemento
            for (int i = 0; i < elementos.length(); i++) {
                ElementoMatriz elemento = elementos.searchK(i);
                if (elemento.getFila() == fila && elemento.getColumna() == columna) {
                    return elemento;
                }
            }
        } catch (MensajeException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    //Establece un valor en la posición especificada
    public void establecer(int fila, int columna, T valor) {
        validarCoordenadas(fila, columna);

        ElementoMatriz elementoExistente = buscarElemento(fila, columna);

        if (elementoExistente != null) {
            //Si ya existe, actualizar el valor
            elementoExistente.setValor(valor);
        } else {
            //Si no existe, crear nuevo elemento y agregarlo
            ElementoMatriz nuevoElemento = new ElementoMatriz(fila, columna, valor);
            elementos.insertFirst(nuevoElemento);
        }
    }

    //Obtiene el valor en la posición especificada
    public T obtener(int fila, int columna) {
        validarCoordenadas(fila, columna);

        ElementoMatriz elemento = buscarElemento(fila, columna);
        if (elemento != null) {
            return elemento.getValor();
        }
        return valorPorDefecto;
    }

    //Elimina el valor en la posición especificada
    public void eliminar(int fila, int columna) {
        validarCoordenadas(fila, columna);

        ElementoMatriz elemento = buscarElemento(fila, columna);
        if (elemento != null) {
            try {
                elementos.removeNode(elemento);
            } catch (MensajeException e) {
                // El elemento no existe, no hacer nada
            }
        }
    }

    //Verifica si existe un valor en la posición especificada
    public boolean existe(int fila, int columna) {
        validarCoordenadas(fila, columna);
        return buscarElemento(fila, columna) != null;
    }

    //Limpia toda la matriz
    public void limpiar() {
        elementos.destroyList();
    }

    //Retorna el número de elementos no nulos almacenados
    public int cantidadElementos() {
        return elementos.length();
    }

    //Retorna el número de filas
    public int getFilas() {
        return totalFilas;
    }

    //Retorna el número de columnas
    public int getColumnas() {
        return totalColumnas;
    }

    //Retorna el valor por defecto
    public T getValorPorDefecto() {
        return valorPorDefecto;
    }

    //Verifica si la matriz está vacía (no tiene elementos no nulos)
    public boolean estaVacia() {
        return elementos.isEmpty();
    }

    //Muestra la matriz completa (incluyendo valores por defecto)
    public void mostrarMatriz() {
        System.out.println("Matriz " + totalFilas + "x" + totalColumnas + ":");
        for (int i = 0; i < totalFilas; i++) {
            for (int j = 0; j < totalColumnas; j++) {
                T valor = obtener(i, j);
                System.out.print(valor + "\t");
            }
            System.out.println();
        }
    }

    //Muestra solo los elementos no nulos
    public void mostrarElementosNoNulos() {
        System.out.println("Elementos no nulos (" + cantidadElementos() + "):");

        if (elementos.isEmpty()) {
            System.out.println("La matriz está vacía.");
            return;
        }

        try {
            for (int i = 0; i < elementos.length(); i++) {
                ElementoMatriz elemento = elementos.searchK(i);
                System.out.println(elemento);
            }
        } catch (MensajeException e) {
            System.out.println("Error al mostrar elementos: " + e.getMessage());
        }
    }

    //Transpone la matriz (intercambia filas por columnas)
    public MatrizDispersa<T> transponer() {
        MatrizDispersa<T> resultado = new MatrizDispersa<>(totalColumnas, totalFilas, valorPorDefecto);

        try {
            for (int i = 0; i < elementos.length(); i++) {
                ElementoMatriz elemento = elementos.searchK(i);
                resultado.establecer(elemento.getColumna(), elemento.getFila(), elemento.getValor());
            }
        } catch (MensajeException e) {
            System.out.println("Error al transponer: " + e.getMessage());
        }

        return resultado;
    }

    //Crea una copia de la matriz
    public MatrizDispersa<T> copiar() {
        MatrizDispersa<T> copia = new MatrizDispersa<>(totalFilas, totalColumnas, valorPorDefecto);

        try {
            for (int i = 0; i < elementos.length(); i++) {
                ElementoMatriz elemento = elementos.searchK(i);
                copia.establecer(elemento.getFila(), elemento.getColumna(), elemento.getValor());
            }
        } catch (MensajeException e) {
            System.out.println("Error al copiar: " + e.getMessage());
        }

        return copia;
    }

    //Suma esta matriz con otra matriz dispersa
    //Para tipos numéricos, necesitarías implementar la operación suma específica
    public MatrizDispersa<T> sumar(MatrizDispersa<T> otra) {
        if (this.totalFilas != otra.totalFilas || this.totalColumnas != otra.totalColumnas) {
            throw new IllegalArgumentException("Las matrices deben tener las mismas dimensiones");
        }

        MatrizDispersa<T> resultado = new MatrizDispersa<>(totalFilas, totalColumnas, valorPorDefecto);

        // Copiar elementos de la primera matriz
        try {
            for (int i = 0; i < elementos.length(); i++) {
                ElementoMatriz elemento = elementos.searchK(i);
                resultado.establecer(elemento.getFila(), elemento.getColumna(), elemento.getValor());
            }

            // Procesar elementos de la segunda matriz
            for (int i = 0; i < otra.elementos.length(); i++) {
                ElementoMatriz elemento = otra.elementos.searchK(i);
                int fila = elemento.getFila();
                int columna = elemento.getColumna();
                T valor = elemento.getValor();

                // Si ya existe un elemento en esta posición, necesitarías implementar la suma
                // Por ahora, simplemente sobrescribe
                resultado.establecer(fila, columna, valor);
            }
        } catch (MensajeException e) {
            System.out.println("Error al sumar matrices: " + e.getMessage());
        }

        return resultado;
    }

    //Obtiene todos los elementos de una fila específica
    public void mostrarFila(int fila) {
        validarCoordenadas(fila, 0);

        System.out.println("Fila " + fila + ":");
        for (int j = 0; j < totalColumnas; j++) {
            T valor = obtener(fila, j);
            System.out.print(valor + "\t");
        }
        System.out.println();
    }

    //Obtiene todos los elementos de una columna específica
    public void mostrarColumna(int columna) {
        validarCoordenadas(0, columna);

        System.out.println("Columna " + columna + ":");
        for (int i = 0; i < totalFilas; i++) {
            T valor = obtener(i, columna);
            System.out.println(valor);
        }
    }
}