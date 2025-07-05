package BTreePlus;

import java.util.ArrayList;

public class PruebaBTreePlus {

    public static void main(String[] args) {
        System.out.println("--- Prueba de Árbol B+ ---");

        // Orden del árbol
        int orden = 3; // Un orden pequeño para facilitar la depuración y visualización
        BTreePlus<Integer> bTree = new BTreePlus<Integer>(orden);

        // --- Prueba de Inserción ---
        System.out.println("\n--- Pruebas de Inserción ---");
        int[] valoresAInsertar = {10, 20, 5, 30, 15, 25, 35, 2, 8, 12, 18, 22, 28, 32, 38, 1, 3, 6, 9, 11, 13, 16, 19, 21, 23, 26, 29, 31, 33, 36, 39, 4, 7, 14, 17, 24, 27, 34, 37, 40};
        for (int valor : valoresAInsertar) {
            System.out.println("Insertando: " + valor);
            bTree.insert(valor);
            System.out.println("Árbol después de " + valor + ": " + bTree.getTotalClaves());
            // Opcional: imprimir estructura del árbol para depuración
            // printTreeStructure(bTree.getRaiz(), 0);
        }

        System.out.println("\nClaves totales en el árbol después de inserciones: " + bTree.getTotalClaves());
        System.out.println("Primera hoja: " + (bTree.getPrimeraHoja() != null ? bTree.getPrimeraHojaNodo() : "N/A"));

        // --- Prueba de Búsqueda ---
        System.out.println("\n--- Pruebas de Búsqueda ---");
        System.out.println("Buscando 15: " + bTree.search(15)); // Debería ser true
        System.out.println("Buscando 100: " + bTree.search(100)); // Debería ser false
        System.out.println("Buscando 2: " + bTree.search(2)); // Debería ser true
        System.out.println("Buscando 40: " + bTree.search(40)); // Debería ser true
        System.out.println("Buscando 0: " + bTree.search(0)); // Debería ser false

        // --- Prueba de Búsqueda por Rango ---
        System.out.println("\n--- Pruebas de Búsqueda por Rango ---");
        System.out.println("Rango [10, 20]: " + bTree.searchRango(10, 20)); // Esperado: [10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]
        System.out.println("Rango [1, 5]: " + bTree.searchRango(1, 5)); // Esperado: [1, 2, 3, 4, 5]
        System.out.println("Rango [35, 40]: " + bTree.searchRango(35, 40)); // Esperado: [35, 36, 37, 38, 39, 40]
        System.out.println("Rango [0, 1]: " + bTree.searchRango(0, 1)); // Esperado: [1]
        System.out.println("Rango [41, 50]: " + bTree.searchRango(41, 50)); // Esperado: []
        System.out.println("Rango [20, 10]: " + bTree.searchRango(20, 10)); // Esperado: [] (min > max)

        // --- Prueba de Eliminación ---
        System.out.println("\n--- Pruebas de Eliminación ---");
        System.out.println("Claves antes de eliminación: " + bTree.getTotalClaves());

        System.out.println("Eliminando 15...");
        bTree.remove(15);
        System.out.println("Claves después de eliminar 15: " + bTree.getTotalClaves());
        System.out.println("Buscando 15: " + bTree.search(15)); // Debería ser false
        System.out.println("Rango [10, 20] después: " + bTree.searchRango(10, 20));

        System.out.println("\nEliminando 2...");
        bTree.remove(2);
        System.out.println("Claves después de eliminar 2: " + bTree.getTotalClaves());
        System.out.println("Buscando 2: " + bTree.search(2)); // Debería ser false
        System.out.println("Rango [1, 5] después: " + bTree.searchRango(1, 5));

        System.out.println("\nEliminando 30...");
        bTree.remove(30);
        System.out.println("Claves después de eliminar 30: " + bTree.getTotalClaves());
        System.out.println("Buscando 30: " + bTree.search(30)); // Debería ser false
        System.out.println("Rango [28, 32] después: " + bTree.searchRango(28, 32));

        System.out.println("\nEliminando una clave que no existe (99)...");
        bTree.remove(99);
        System.out.println("Claves después de eliminar 99: " + bTree.getTotalClaves());

        System.out.println("\nEliminando claves hasta que el árbol esté vacío...");
        // Eliminar todas las claves para probar underflow y colapso de la raíz
        ArrayList<Integer> todasLasClaves = bTree.getTotalClaves();
        for (Integer clave : todasLasClaves) {
            System.out.println("Eliminando: " + clave);
            bTree.remove(clave);
            System.out.println("Claves restantes: " + bTree.getTotalClaves());
            // Opcional: imprimir estructura del árbol para depuración
            // printTreeStructure(bTree.getRaiz(), 0);
        }

        System.out.println("\nÁrbol vacío: " + bTree.isEmpty());
        System.out.println("Claves en árbol vacío: " + bTree.getTotalClaves());
    }

}
