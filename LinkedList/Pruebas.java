package LinkedList;

public class Pruebas {
    public static void main(String[] args) throws MensajeException {
        ListaEnlazada<String> listaPrueba1 = new ListaEnlazada<String>();

        //VERIFICA SI LA LISTA ESTA VACÍA
        System.out.println("¿La lista esta vacía?" + listaPrueba1.isEmpty()); //TRUE

        //SE AÑADEN ELEMENTOS A LA LISTA
        listaPrueba1.insertFirst("E");
        listaPrueba1.insertFirst("D");
        listaPrueba1.insertFirst("C");
        listaPrueba1.insertFirst("B");
        listaPrueba1.insertFirst("A");

        //Se imprime la lista
        listaPrueba1.print(); // A,B,C,D,E
        System.out.println("Tamaño de la lista: " + listaPrueba1.length()); //5

        //Se añaden elementos al final de la lista
        listaPrueba1.insertLast("X");
        listaPrueba1.insertLast("Y");
        listaPrueba1.insertLast("Z");

        //Se imprime la lista
        listaPrueba1.print(); // A,B,C,D,E, X,Y,Z
        System.out.println("Tamaño de la lista: " + listaPrueba1.length()); //8

        //Se añaden elementos en ciertas posiciones de la lista
        listaPrueba1.insertPosicionK("H",5);
        listaPrueba1.insertPosicionK("I",6);
        //Se imprime la lista
        listaPrueba1.print(); // A,B,C,D,E, H,I,X,Y,Z
        System.out.println("Tamaño de la lista: " + listaPrueba1.length()); //10

        listaPrueba1.insertPosicionK("F",5);
        listaPrueba1.insertPosicionK("J",8);

        //Se imprime la lista
        listaPrueba1.print(); // A,B,C,D,E,F, H,I,J,X,Y,Z
        System.out.println("Tamaño de la lista: " + listaPrueba1.length()); //12

        /*
        //PRUEBA CON POSICION 0

        listaPrueba1.insertPosicionK("J",0);

        //Se imprime la lista
        listaPrueba1.print(); // J, A,B,C,D,E,F, H,I,J,X,Y,Z
        System.out.println("Tamaño de la lista: " + listaPrueba1.length()); //13
        */

        //Se busca un elemento por valor
        System.out.println("Busqueda de B: " + listaPrueba1.search("B")); //1
        try {
            System.out.println("Busqueda de B: " + listaPrueba1.search("L")); //Lanza excepción -> mensaje fuera de rango
        } catch (MensajeException e) {
            System.out.println("Error al buscar: " + e.getMessage());
        }

        //Se busca un elemento por posicion
        listaPrueba1.searchK(5); //F
        try {
            listaPrueba1.searchK(14); //Lanza excepción -> mensaje fuera de rango
        } catch (MensajeException e) {
            System.out.println("Error al buscar: " + e.getMessage());
        }

        //Eliminar un elemento por contenido
        System.out.println("Eliminación de H: ");
        listaPrueba1.removeNode("H");
        //Se imprime la lista
        listaPrueba1.print(); // A,B,C,D,E,F,I,J,X,Y,Z
        System.out.println("Tamaño de la lista: " + listaPrueba1.length()); //11

        //Eliminar un elemento por posicion
        System.out.println("Eliminación de J en posición 7: ");
        listaPrueba1.removeNodeK(7);
        //Se imprime la lista
        listaPrueba1.print(); // A,B,C,D,E,F,I,X,Y,Z
        System.out.println("Tamaño de la lista: " + listaPrueba1.length()); //10

        System.out.println("La lista esta vacía?: " + listaPrueba1.isEmpty()); //FALSE

        //Destruir la lista
        listaPrueba1.destroyList();

        System.out.println("La lista esta vacía?: " + listaPrueba1.isEmpty()); //TRUE
    }
}
