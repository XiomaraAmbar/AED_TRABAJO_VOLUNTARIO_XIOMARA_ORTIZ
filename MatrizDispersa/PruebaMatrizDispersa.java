package MatrizDispersa;

public class PruebaMatrizDispersa {
    public static void main(String[] args) {
        System.out.println("=== Prueba de Matriz Dispersa ===");

        // Crear matriz 5x5 con valor por defecto 0
        MatrizDispersa<Integer> matriz = new MatrizDispersa<>(5, 5, 0);

        // Establecer algunos valores
        matriz.establecer(0, 0, 10);
        matriz.establecer(1, 2, 20);
        matriz.establecer(3, 4, 30);
        matriz.establecer(2, 1, 15);
        matriz.establecer(4, 3, 25);

        System.out.println("Matriz completa:");
        matriz.mostrarMatriz();

        System.out.println("\nElementos no nulos:");
        matriz.mostrarElementosNoNulos();

        System.out.println("\nCantidad de elementos: " + matriz.cantidadElementos());
        System.out.println("Valor en (1,2): " + matriz.obtener(1, 2));
        System.out.println("Valor en (0,1): " + matriz.obtener(0, 1));
        System.out.println("Existe elemento en (3,4): " + matriz.existe(3, 4));

        // Mostrar fila específica
        System.out.println();
        matriz.mostrarFila(1);

        // Transponer
        System.out.println("\nMatriz transpuesta:");
        MatrizDispersa<Integer> transpuesta = matriz.transponer();
        transpuesta.mostrarMatriz();

        // Eliminar un elemento
        System.out.println("\nEliminando elemento (1,2):");
        matriz.eliminar(1, 2);
        matriz.mostrarElementosNoNulos();

        // Ejemplo con cadenas
        System.out.println("\n=== Ejemplo con cadenas ===");
        MatrizDispersa<String> matrizTexto = new MatrizDispersa<>(3, 3, "vacío");
        matrizTexto.establecer(0, 0, "Hola");
        matrizTexto.establecer(1, 1, "Mundo");
        matrizTexto.establecer(2, 2, "Java");

        matrizTexto.mostrarMatriz();
        System.out.println("\nElementos no nulos:");
        matrizTexto.mostrarElementosNoNulos();
    }
}
