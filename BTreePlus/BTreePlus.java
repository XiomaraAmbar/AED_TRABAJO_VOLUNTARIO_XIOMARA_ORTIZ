package BTreePlus;

import LinkedList.ListaEnlazada;
import Queue.Cola;
import java.util.ArrayList;

public class BTreePlus<E extends Comparable<E>> {
    private BNodePlus<E> raiz;
    private int orden;
    private BNodePlus<E> primeraHoja; //Referencia a la primera hoja para recorrido secuencial

    //Útiles para la inserción
    private boolean dividido; //Controla si se realizó división durante inserción
    private BNodePlus<E> derechoTemporal; //Almacena nodo derecho resultante de división

    //Constructor
    public BTreePlus(int orden) {
        this.orden = orden;
        this.raiz = null;
        this.primeraHoja = null;
    }

    //Verifica si el árbol está vacío
    public boolean isEmpty() {
        return this.raiz == null;
    }

    /************************************************************************************
     * MÉTODOS DE BÚSQUEDA
     ************************************************************************************/

    //Busca una clave y retorna true si existe, false en caso contrario
    public boolean search(E clave) {
        if (isEmpty() || clave == null) {
            return false;
        }
        return searchKey(this.raiz, clave);
    }

    //Busca una clave recursivamente en el árbol
    private boolean searchKey(BNodePlus<E> nodoActual, E clave) {
        if (nodoActual == null) {
            return false;
        }

        int[] posicion = new int[1];
        boolean encontrado = nodoActual.searchKey(clave, posicion);

        //Si es nodo hoja, se verifica si se encontro la clave
        if (nodoActual.esHoja()) {
            return encontrado;
        }

        //Si es nodo interno, se continua la búsqueda hacia abajo
        //Incluso si se encuentra la clave en nodo interno, se debe ir a la hoja
        if (encontrado) {
            //Si se encuentra la clave en nodo interno, vamos al hijo derecho
            return searchKey(nodoActual.getChild(posicion[0] + 1), clave);
        } else {
            //Si no encontramos la clave, vamos al hijo correspondiente
            return searchKey(nodoActual.getChild(posicion[0]), clave);
        }
    }

    //Busca una clave y retorna el nodo hoja donde debería estar
    public BNodePlus<E> searchNodoHoja(E clave) {
        if (isEmpty() || clave == null) {
            return null;
        }
        return searchNodoHojaRecursivo(this.raiz, clave);
    }

    //Encuentra el nodo hoja recursivamente
    private BNodePlus<E> searchNodoHojaRecursivo(BNodePlus<E> nodoActual, E clave) {
        if (nodoActual == null) {
            return null;
        }

        //Si es nodo hoja, lo retornamos
        if (nodoActual.esHoja()) {
            return nodoActual;
        }

        //Si es nodo interno, encontramos el hijo correcto
        int[] posicion = new int[1];
        boolean encontrado = nodoActual.searchKey(clave, posicion);

        if (encontrado) {
            //Si encontramos la clave en nodo interno, vamos al hijo derecho
            return searchNodoHojaRecursivo(nodoActual.getChild(posicion[0] + 1), clave);
        } else {
            //Si no encontramos la clave, vamos al hijo correspondiente
            return searchNodoHojaRecursivo(nodoActual.getChild(posicion[0]), clave);
        }
    }

    //Búsqueda por rango -> retorna todas las claves entre minimo y maximo -> rango
    public ArrayList<E> searchRango(E minimo, E maximo) {
        ArrayList<E> resultado = new ArrayList<>();

        if (isEmpty() || minimo == null || maximo == null || minimo.compareTo(maximo) > 0) {
            return resultado;
        }

        //Encuentra el nodo hoja donde inicia el rango
        BNodePlus<E> nodoActual = searchNodoHoja(minimo);

        if (nodoActual == null) {
            return resultado;
        }

        //Recorre las hojas secuencialmente usando los enlaces
        while (nodoActual != null) {
            //Examina todas las claves en el nodo actual
            for (int i = 0; i < nodoActual.getContadorClaves(); i++) {
                E clave = nodoActual.getKey(i);

                //Si la clave está en el rango, se agrega
                if (clave.compareTo(minimo) >= 0 && clave.compareTo(maximo) <= 0) {
                    resultado.add(clave);
                }

                //Si pasamos el máximo, termina
                if (clave.compareTo(maximo) > 0) {
                    return resultado;
                }
            }

            //Avanza al siguiente nodo hoja
            nodoActual = nodoActual.getNext();
        }

        return resultado;
    }

    /************************************************************************************
     * MÉTODOS DE INSERCIÓN
     ************************************************************************************/

    //Inserta nueva clave manejando división de raíz si es necesario
    public void insert(E nuevaClave) {
        if (nuevaClave == null) {
            return;
        }

        dividido = false;
        E claveMedia;
        BNodePlus<E> nuevaRaiz;

        //Si el árbol está vacío, se crea la primera hoja
        if (isEmpty()) {
            this.raiz = new BNodePlus<E>(true, this.orden);
            this.raiz.insertKey(nuevaClave);
            this.primeraHoja = this.raiz;
            return;
        }

        claveMedia = push(this.raiz, nuevaClave);

        //Si hubo división, se crea una nueva raíz
        if (dividido) {
            nuevaRaiz = new BNodePlus<E>(false, this.orden); //Nueva raíz es nodo interno
            nuevaRaiz.insertKey(claveMedia);
            nuevaRaiz.setChild(0, this.raiz);
            nuevaRaiz.setChild(1, derechoTemporal);
            this.raiz = nuevaRaiz;
        }
    }

    //Inserta recursivamente y propaga divisiones hacia arriba
    //Retorna la clave mediana en caso de división
    private E push(BNodePlus<E> nodoActual, E nuevaClave) {
        int[] posicion = new int[1];
        E claveMedia = null;

        //Caso base: nodo nulo, se inserta aquí
        if (nodoActual == null) {
            dividido = true;
            derechoTemporal = null;
            return nuevaClave;
        }

        boolean claveEncontrada = nodoActual.searchKey(nuevaClave, posicion);

        //No permite duplicados
        if (claveEncontrada && nodoActual.esHoja()) {
            System.out.println("Item duplicado");
            dividido = false;
            return null;
        }

        //Si es nodo hoja, se inserta directamente
        if (nodoActual.esHoja()) {
            //Inserta en hoja
            if (nodoActual.isFull()) {
                //Dividir hoja
                claveMedia = dividirHoja(nodoActual, nuevaClave, posicion[0]);
            } else {
                //Inserta sin división
                nodoActual.insertKey(nuevaClave);
                dividido = false;
                claveMedia = null; //No hay división, no hay clave mediana
            }
            return claveMedia;
        } else {
            // Nodo interno: encontrar hijo apropiado
            int indiceHijo;
            if (claveEncontrada) {
                //Si encontramos la clave en nodo interno, ir al hijo derecho
                indiceHijo = posicion[0] + 1;
            } else {
                //Si no encontramos la clave, ir al hijo correspondiente
                indiceHijo = posicion[0];
            }

            //Insertar recursivamente en hijo apropiado
            claveMedia = push(nodoActual.getChild(indiceHijo), nuevaClave);

            //Manejar división propagada desde hijo
            if (dividido) {
                if (nodoActual.isFull()) {
                    claveMedia = dividirNodoInterno(nodoActual, claveMedia, indiceHijo);
                } else {
                    dividido = false;
                    putNode(nodoActual, claveMedia, derechoTemporal, indiceHijo);
                }
            }
            return claveMedia;
        }
    }

    //Divide un nodo hoja lleno
    //Retorna la clave mediana para propagar al padre
    private E dividirHoja(BNodePlus<E> nodoHoja, E nuevaClave, int posicionInsertar) {
        dividido = true;

        //Crea nuevo nodo hoja derecho
        derechoTemporal = new BNodePlus<E>(true, this.orden);

        //Crea array temporal con todas las claves (incluyendo la nueva)
        ArrayList<E> listaClavesTemporal = new ArrayList<>();

        //Copiar claves existentes y la nueva en orden
        int i = 0, j = 0;
        while (i < nodoHoja.getContadorClaves()) {
            if (j == posicionInsertar) {
                listaClavesTemporal.add(nuevaClave);
                j++;
            }
            listaClavesTemporal.add(nodoHoja.getKey(i));
            i++;
            j++;
        }
        if (j == posicionInsertar) {
            listaClavesTemporal.add(nuevaClave);
        }

        // Calcular posición de división
        int posicionMediana = (listaClavesTemporal.size() + 1) / 2;

        // Limpiar nodo original
        nodoHoja.contadorClaves = 0;

        // Distribuir claves
        // Claves menores permanecen en nodo original (izquierdo)
        for (i = 0; i < posicionMediana; i++) {
            nodoHoja.insertKey(listaClavesTemporal.get(i));
        }

        // Clave mediana y mayores van al nuevo nodo derecho
        for (i = posicionMediana; i < listaClavesTemporal.size(); i++) {
            derechoTemporal.insertKey(listaClavesTemporal.get(i));
        }

        // Mantener enlaces entre hojas
        derechoTemporal.setNext(nodoHoja.getNext());
        nodoHoja.setNext(derechoTemporal);

        // La clave mediana se copia al padre (primera clave del nodo derecho)
        return derechoTemporal.getKey(0);
    }

    //Divide un nodo interno lleno
    //Retorna la clave mediana para propagar al padre
    private E dividirNodoInterno(BNodePlus<E> nodoInterno, E nuevaClave, int posicionInsertar) {
        BNodePlus<E> nuevoDerecho = derechoTemporal;
        int posicionMediana;

        //Calcula posición de la mediana
        posicionMediana = (posicionInsertar <= this.orden / 2) ? this.orden / 2 : this.orden / 2 + 1;
        derechoTemporal = new BNodePlus<E>(false, this.orden);

        //Mover mitad superior de claves e hijos al nuevo nodo
        for (int i = posicionMediana; i < this.orden - 1; i++) {
            if (i < nodoInterno.getContadorClaves()) {
                derechoTemporal.insertKey(nodoInterno.getKey(i));
            }
        }

        //Mover hijos correspondientes
        for (int i = posicionMediana; i <= nodoInterno.getContadorClaves(); i++) {
            BNodePlus<E> hijo = nodoInterno.getChild(i);
            if (hijo != null) {
                derechoTemporal.setChild(i - posicionMediana, hijo);
            }
        }

        //Ajustar contador de claves del nodo original
        int clavesOriginales = nodoInterno.contadorClaves;
        nodoInterno.contadorClaves = posicionMediana;

        //Insertar nueva clave en el nodo apropiado
        if (posicionInsertar <= this.orden / 2) {
            putNode(nodoInterno, nuevaClave, nuevoDerecho, posicionInsertar);
        } else {
            putNode(derechoTemporal, nuevaClave, nuevoDerecho, posicionInsertar - posicionMediana);
        }

        //Extraer y retornar clave mediana
        E claveMediana = nodoInterno.getKey(nodoInterno.contadorClaves - 1);

        return claveMediana;
    }

    //Inserta clave e hijo en nodo con espacio disponible
    private void putNode(BNodePlus<E> nodoActual, E nuevaClave, BNodePlus<E> nuevoDerecho, int posicionInsertar) {
        //Inserta la clave
        nodoActual.insertKey(nuevaClave);

        //Inserta el hijo derecho
        if (nuevoDerecho != null) {
            nodoActual.insertChild(posicionInsertar + 1, nuevoDerecho);
        }
    }

    /************************************************************************************
     * MÉTODOS DE ELIMINACIÓN
     ************************************************************************************/

    //Elimina una clave
    public void remove(E clave) {
        if (isEmpty() || clave == null) {
            System.out.println("El árbol está vacío o la clave es nula. No se puede eliminar.");
            return;
        }

        boolean eliminado = removeKey(this.raiz, clave);

        if (eliminado) {
            System.out.println("Se eliminó la clave " + clave + " del árbol.");

            //Ajusta la raíz si se quedó vacía
            if (!this.raiz.esHoja() && this.raiz.getContadorClaves() == 0) {
                this.raiz = this.raiz.getChild(0);
            }
        } else {
            System.out.println("La clave " + clave + " no se encontró en el árbol.");
        }
    }

    //Elimina clave recursivamente manejando underflow y actualización de índices
    private boolean removeKey(BNodePlus<E> nodoActual, E clave) {
        if (nodoActual == null) {
            return false;
        }

        int[] posicion = new int[1];
        boolean encontrado = nodoActual.searchKey(clave, posicion);

        if (nodoActual.esHoja()) {
            //En nodo hoja: eliminar si existe
            if (encontrado) {
                //Obtener el sucesor antes de eliminar (si existe)
                E sucesor = null;
                if (posicion[0] < nodoActual.getContadorClaves() - 1) {
                    //Hay un sucesor en la misma hoja
                    sucesor = nodoActual.getKey(posicion[0] + 1);
                } else {
                    //Buscar sucesor en la siguiente hoja
                    BNodePlus<E> siguienteHoja = nodoActual.getNext();
                    if (siguienteHoja != null && siguienteHoja.getContadorClaves() > 0) {
                        sucesor = siguienteHoja.getKey(0);
                    }
                }

                //Eliminar la clave de la hoja
                nodoActual.removeKey(clave);

                //Actualizar índices en nodos internos si es necesario
                if (sucesor != null) {
                    actualizarIndices(this.raiz, clave, sucesor);
                }

                return true;
            } else {
                return false; //Clave no encontrada
            }
        } else {
            //En nodo interno: continuar búsqueda hacia abajo
            int indiceHijo;
            if (encontrado) {
                //Si encontramos la clave en nodo interno, ir al hijo derecho
                indiceHijo = posicion[0] + 1;
            } else {
                //Si no encontramos la clave, ir al hijo correspondiente
                indiceHijo = posicion[0];
            }

            boolean eliminado = removeKey(nodoActual.getChild(indiceHijo), clave);

            //Verificar y corregir underflow en hijo
            if (eliminado && nodoActual.getChild(indiceHijo) != null) {
                BNodePlus<E> hijoAfectado = nodoActual.getChild(indiceHijo);

                //Verificar underflow
                if (hijoAfectado.getContadorClaves() < getMinimoClaves(hijoAfectado)) {
                    corrigeUnderflow(nodoActual, indiceHijo);
                }
            }

            return eliminado;
        }
    }

    //Actualiza los índices en nodos internos cuando se elimina una clave
    private void actualizarIndices(BNodePlus<E> nodo, E claveEliminada, E sucesor) {
        if (nodo == null || nodo.esHoja()) {
            return;
        }

        //Buscar la clave eliminada en el nodo actual
        for (int i = 0; i < nodo.getContadorClaves(); i++) {
            if (nodo.getKey(i).equals(claveEliminada)) {
                // Reemplazar con el sucesor
                nodo.setKey(i, sucesor);
                break;
            }
        }

        //Recursivamente actualizar en todos los hijos
        for (int i = 0; i <= nodo.getContadorClaves(); i++) {
            actualizarIndices(nodo.getChild(i), claveEliminada, sucesor);
        }
    }

    //Corrige underflow en un nodo hijo
    private void corrigeUnderflow(BNodePlus<E> padre, int indiceHijo) {
        BNodePlus<E> hijoDeficitario = padre.getChild(indiceHijo);

        // Intentar redistribuir con hermano izquierdo
        if (indiceHijo > 0) {
            BNodePlus<E> hermanoIzquierdo = padre.getChild(indiceHijo - 1);
            if (hermanoIzquierdo.getContadorClaves() > getMinimoClaves(hermanoIzquierdo)) {
                redistribuyeIzquierdo(padre, indiceHijo - 1, indiceHijo);
                return;
            }
        }

        //Intentar redistribuir con hermano derecho
        if (indiceHijo < padre.getContadorClaves()) {
            BNodePlus<E> hermanoDerecho = padre.getChild(indiceHijo + 1);
            if (hermanoDerecho.getContadorClaves() > getMinimoClaves(hermanoDerecho)) {
                redistribuyeDerecho(padre, indiceHijo, indiceHijo + 1);
                return;
            }
        }

        //Si no se puede redistribuir, fusionar
        if (indiceHijo > 0) {
            //Fusionar con hermano izquierdo
            fusionIzquierdo(padre, indiceHijo - 1, indiceHijo);
        } else if (indiceHijo < padre.getContadorClaves()) {
            //Fusionar con hermano derecho
            fusionDerecho(padre, indiceHijo, indiceHijo + 1);
        }
    }

    //Redistribución desde hermano izquierdo
    private void redistribuyeIzquierdo(BNodePlus<E> padre, int indiceIzquierdo, int indiceDerecho) {
        BNodePlus<E> hermanoIzquierdo = padre.getChild(indiceIzquierdo);
        BNodePlus<E> hijoDeficitario = padre.getChild(indiceDerecho);

        if (hermanoIzquierdo.esHoja()) {
            //Redistribución en hojas
            E claveAMover = hermanoIzquierdo.getKey(hermanoIzquierdo.getContadorClaves() - 1);
            hermanoIzquierdo.removeKey(claveAMover);
            hijoDeficitario.insertKeyIndex(0, claveAMover); // Insertar al inicio

            //Actualizar clave guía en el padre (primera clave del hijo derecho)
            padre.setKey(indiceIzquierdo, hijoDeficitario.getKey(0));
        } else {
            //Redistribución en nodos internos
            E claveDelPadre = padre.getKey(indiceIzquierdo);
            E claveAMover = hermanoIzquierdo.getKey(hermanoIzquierdo.getContadorClaves() - 1);
            BNodePlus<E> hijoAMover = hermanoIzquierdo.getChild(hermanoIzquierdo.getContadorClaves());

            //Mover clave del padre al hijo deficitario (al inicio)
            hijoDeficitario.insertKeyIndex(0, claveDelPadre);
            //Mover hijo al inicio
            hijoDeficitario.insertChild(0, hijoAMover);

            //Remover del hermano izquierdo
            hermanoIzquierdo.removeKey(claveAMover);
            hermanoIzquierdo.removeChild(hermanoIzquierdo.getContadorClaves() + 1);

            //Actualizar clave en el padre
            padre.setKey(indiceIzquierdo, claveAMover);
        }
    }

    //Redistribución desde hermano derecho
    private void redistribuyeDerecho(BNodePlus<E> padre, int indiceIzquierdo, int indiceDerecho) {
        BNodePlus<E> hijoDeficitario = padre.getChild(indiceIzquierdo);
        BNodePlus<E> hermanoDerecho = padre.getChild(indiceDerecho);

        if (hermanoDerecho.esHoja()) {
            //Redistribución en hojas
            E claveAMover = hermanoDerecho.getKey(0);
            hermanoDerecho.removeKey(claveAMover);
            hijoDeficitario.insertKey(claveAMover); // Insertar al final

            // Actualizar clave guía en el padre (primera clave del hermano derecho)
            padre.setKey(indiceIzquierdo, hermanoDerecho.getKey(0));
        } else {
            // Redistribución en nodos internos
            E claveDelPadre = padre.getKey(indiceIzquierdo);
            E claveAMover = hermanoDerecho.getKey(0);
            BNodePlus<E> hijoAMover = hermanoDerecho.getChild(0);

            // Mover clave del padre al hijo deficitario
            hijoDeficitario.insertKey(claveDelPadre);
            // Mover hijo
            hijoDeficitario.setChild(hijoDeficitario.getContadorClaves(), hijoAMover);

            // Remover del hermano derecho
            hermanoDerecho.removeKey(claveAMover);
            hermanoDerecho.removeChild(0);

            // Actualizar clave en el padre
            padre.setKey(indiceIzquierdo, claveAMover);
        }
    }

    //Fusión con hermano izquierdo
    private void fusionIzquierdo(BNodePlus<E> padre, int indiceIzquierdo, int indiceDerecho) {
        BNodePlus<E> hermanoIzquierdo = padre.getChild(indiceIzquierdo);
        BNodePlus<E> hijoDeficitario = padre.getChild(indiceDerecho);

        if (hermanoIzquierdo.esHoja()) {
            //Fusión de hojas
            //Mover todas las claves del hijo deficitario al hermano izquierdo
            for (int i = 0; i < hijoDeficitario.getContadorClaves(); i++) {
                hermanoIzquierdo.insertKey(hijoDeficitario.getKey(i));
            }

            //Actualizar enlace entre hojas
            hermanoIzquierdo.setNext(hijoDeficitario.getNext());
        } else {
            //Fusión de nodos internos
            //Bajar la clave del padre
            E claveDelPadre = padre.getKey(indiceIzquierdo);
            hermanoIzquierdo.insertKey(claveDelPadre);

            //Mover todas las claves del hijo deficitario
            for (int i = 0; i < hijoDeficitario.getContadorClaves(); i++) {
                hermanoIzquierdo.insertKey(hijoDeficitario.getKey(i));
            }

            //Mover todos los hijos
            for (int i = 0; i <= hijoDeficitario.getContadorClaves(); i++) {
                BNodePlus<E> hijo = hijoDeficitario.getChild(i);
                if (hijo != null) {
                    int nuevaPosicion = hermanoIzquierdo.getContadorClaves() - hijoDeficitario.getContadorClaves() + i;
                    hermanoIzquierdo.setChild(nuevaPosicion, hijo);
                }
            }
        }

        //Remover clave del padre y reorganizar hijos
        padre.removeKey(padre.getKey(indiceIzquierdo));
        for (int i = indiceDerecho; i <= padre.getContadorClaves(); i++) {
            padre.setChild(i, padre.getChild(i + 1));
        }
    }

    //Fusión con hermano derecho
    private void fusionDerecho(BNodePlus<E> padre, int indiceIzquierdo, int indiceDerecho) {
        BNodePlus<E> hijoDeficitario = padre.getChild(indiceIzquierdo);
        BNodePlus<E> hermanoDerecho = padre.getChild(indiceDerecho);

        if (hijoDeficitario.esHoja()) {
            //Fusión de hojas
            //Mover todas las claves del hermano derecho al hijo deficitario
            for (int i = 0; i < hermanoDerecho.getContadorClaves(); i++) {
                hijoDeficitario.insertKey(hermanoDerecho.getKey(i));
            }

            //Actualizar enlace entre hojas
            hijoDeficitario.setNext(hermanoDerecho.getNext());
        } else {
            //Fusión de nodos internos
            //Bajar la clave del padre
            E claveDelPadre = padre.getKey(indiceIzquierdo);
            hijoDeficitario.insertKey(claveDelPadre);

            //Mover todas las claves del hermano derecho
            for (int i = 0; i < hermanoDerecho.getContadorClaves(); i++) {
                hijoDeficitario.insertKey(hermanoDerecho.getKey(i));
            }

            //Mover todos los hijos
            for (int i = 0; i <= hermanoDerecho.getContadorClaves(); i++) {
                BNodePlus<E> hijo = hermanoDerecho.getChild(i);
                if (hijo != null) {
                    int nuevaPosicion = hijoDeficitario.getContadorClaves() - hermanoDerecho.getContadorClaves() + i;
                    hijoDeficitario.setChild(nuevaPosicion, hijo);
                }
            }
        }

        //Remover clave del padre y reorganizar hijos
        padre.removeKey(padre.getKey(indiceIzquierdo));
        for (int i = indiceDerecho; i <= padre.getContadorClaves(); i++) {
            padre.setChild(i, padre.getChild(i + 1));
        }
    }

    //Calcula el mínimo número de claves para un nodo
    private int getMinimoClaves(BNodePlus<E> nodo) {
        if (nodo == this.raiz) {
            //La raíz puede tener menos claves
            return nodo.esHoja() ? 1 : 1;
        }

        if (nodo.esHoja()) {
            // Para hojas: al menos (orden-1)/2 claves
            return (this.orden - 1) / 2;
        } else {
            // Para nodos internos: al menos orden/2 - 1 claves
            return (this.orden / 2) - 1;
        }
    }

    //Método auxiliar para obtener la clave mínima de un subárbol
    private E getMinimaClave(BNodePlus<E> nodo) {
        if (nodo == null) {
            return null;
        }

        // Ir siempre al hijo más izquierdo hasta llegar a una hoja
        while (!nodo.esHoja()) {
            nodo = nodo.getChild(0);
        }

        return nodo.getKey(0);
    }

    //Método auxiliar para obtener la clave máxima de un subárbol
    private E getMaximaClave(BNodePlus<E> nodo) {
        if (nodo == null) {
            return null;
        }

        // Ir siempre al hijo más derecho hasta llegar a una hoja
        while (!nodo.esHoja()) {
            nodo = nodo.getChild(nodo.getContadorClaves());
        }

        return nodo.getKey(nodo.getContadorClaves() - 1);
    }

    //Retorna todas las claves del árbol en orden ascendente
    public ArrayList<E> getTotalClaves() {
        ArrayList<E> resultado = new ArrayList<>();

        if (isEmpty()) {
            return resultado;
        }

        // Empezar desde la primera hoja
        BNodePlus<E> nodoActual = this.primeraHoja;

        // Si no tenemos referencia a la primera hoja, la encontramos
        if (nodoActual == null) {
            nodoActual = searchPrimeraHoja();
        }

        // Recorrer todas las hojas
        while (nodoActual != null) {
            for (int i = 0; i < nodoActual.getContadorClaves(); i++) {
                resultado.add(nodoActual.getKey(i));
            }
            nodoActual = nodoActual.getNext();
        }

        return resultado;
    }

    //Encuentra la primera hoja del árbol (hoja más a la izquierda)
    private BNodePlus<E> searchPrimeraHoja() {
        if (isEmpty()) {
            return null;
        }

        BNodePlus<E> nodoActual = this.raiz;

        //Ir siempre hacia el hijo más a la izquierda
        while (!nodoActual.esHoja()) {
            nodoActual = nodoActual.getChild(0);
        }

        return nodoActual;
    }

    //Encuentra el primer nodo hoja (más a la izquierda)
    private BNodePlus<E> searchPrimeraHojaNodo(BNodePlus<E> nodo) {
        if (nodo == null) {
            return null;
        }

        BNodePlus<E> actual = nodo;
        while (!actual.esHoja()) {
            actual = actual.getChild(0);
            if (actual == null) {
                break;
            }
        }

        return actual;
    }

    //Getters
    public BNodePlus<E> getRaiz() {
        return this.raiz;
    }

    public int getOrden() {
        return this.orden;
    }

    public BNodePlus<E> getPrimeraHoja() {
        return this.primeraHoja;
    }

    public BNodePlus<E> getPrimeraHojaNodo() {
        return this.primeraHoja;
    }

    //Setter para la primera hoja
    public void setPrimeraHoja(BNodePlus<E> primeraHoja) {
        this.primeraHoja = primeraHoja;
    }

    /************************************************************************************
     * REPRESENTACIÓN ARBOL EN CADENA
     ************************************************************************************/

    public String toString() {
        StringBuilder resultado = new StringBuilder();
        if (this.raiz == null || this.raiz.isEmpty()) {
            resultado.append("B+ Tree está vacío...");
        } else {
            writeTree(this.raiz, resultado);
        }
        return resultado.toString();
    }

    //Genera representación tabular usando recorrido por niveles
    private StringBuilder writeTree(BNodePlus<E> nodoRaiz, StringBuilder resultado) {
        if (nodoRaiz == null) {
            return resultado;
        }

        try {
            //Colas para procesamiento por niveles
            Cola<BNodePlus<E>> colaNodos = new Cola<>();
            Cola<BNodePlus<E>> colaPadres = new Cola<>();

            colaNodos.enqueue(nodoRaiz);
            colaPadres.enqueue(null);

            //Encabezados de tabla
            resultado.append(String.format("%-10s %-15s %-10s %-15s %-10s%n",
                    "Id.Nodo", "Claves Nodo", "Id.Padre", "Id.Hijos", "Tipo"));

            //Procesar todos los nodos nivel por nivel
            while (!colaNodos.isEmpty()) {
                BNodePlus<E> nodoActual = colaNodos.dequeue();
                BNodePlus<E> nodoPadre = colaPadres.dequeue();

                //Formatear claves del nodo
                StringBuilder claves = new StringBuilder("(");
                for (int i = 0; i < nodoActual.getContadorClaves(); i++) {
                    claves.append(nodoActual.getKey(i));
                    if (i < nodoActual.getContadorClaves() - 1) {
                        claves.append(", ");
                    }
                }
                claves.append(")");

                //Formatear información del padre
                String padreStr = (nodoPadre != null) ? "[" + nodoPadre.getIdNode() + "]" : "--";

                //Formatear información de hijos y agregar a cola
                StringBuilder hijos = new StringBuilder("[");
                boolean conHijos = false;

                // Solo procesar hijos si no es nodo hoja
                if (!nodoActual.esHoja()) {
                    for (int i = 0; i <= nodoActual.getContadorClaves(); i++) {
                        BNodePlus<E> hijo = nodoActual.getChild(i);
                        if (hijo != null) {
                            if (conHijos) {
                                hijos.append(", ");
                            }
                            hijos.append(hijo.getIdNode());
                            conHijos = true;

                            //Agregar hijo para procesamiento posterior
                            colaNodos.enqueue(hijo);
                            colaPadres.enqueue(nodoActual);
                        }
                    }
                }

                if (!conHijos) {
                    hijos.append("--");
                }
                hijos.append("]");

                //Determinar tipo de nodo
                String tipoNodo = nodoActual.esHoja() ? "HOJA" : "INTERNO";

                //Agregar fila formateada a la tabla
                resultado.append(String.format("%-10s %-15s %-10s %-15s %-10s%n",
                        nodoActual.getIdNode(),
                        claves.toString(),
                        padreStr,
                        hijos.toString(),
                        tipoNodo));
            }

            // Agregar información adicional sobre enlaces de nodos hoja
            resultado.append("\n" + "=".repeat(70) + "\n");
            resultado.append("ENLACES ENTRE NODOS HOJA:\n");
            resultado.append("=".repeat(70) + "\n");

            // Mostrar la secuencia de nodos hoja enlazados
            BNodePlus<E> nodoHojaActual = searchPrimeraHojaNodo(nodoRaiz);
            if (nodoHojaActual != null) {
                StringBuilder enlacesHoja = new StringBuilder();
                while (nodoHojaActual != null) {
                    enlacesHoja.append("[").append(nodoHojaActual.getIdNode()).append("]");
                    nodoHojaActual = nodoHojaActual.getNext();
                    if (nodoHojaActual != null) {
                        enlacesHoja.append(" -> ");
                    }
                }
                resultado.append(enlacesHoja.toString()).append("\n");
            }

        } catch (Exception e) {
            resultado.append("Error al procesar el árbol: ").append(e.getMessage());
        }

        return resultado;
    }
}