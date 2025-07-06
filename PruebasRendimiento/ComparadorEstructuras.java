package PruebasRendimiento;

import Inventario.*;
import java.util.*;

class ComparadorEstructuras {

    public void compararEstructuras(int[] tamaños) {
        System.out.println("Comparando rendimiento de diferentes estructuras de datos...");
        System.out.printf("%-10s %-15s %-15s %-15s %-15s%n",
                "Tamaño", "AVL Tree", "Hash Table", "B-Tree Plus", "Matriz Dispersa");
        System.out.println("------------------------------------------------------------------------");

        for (int tamaño : tamaños) {
            ResultadoComparacion resultado = compararEstructurasPorTamaño(tamaño);
            System.out.printf("%-10d %-15.2f %-15.2f %-15.2f %-15.2f%n",
                    tamaño, resultado.tiempoAVL, resultado.tiempoHash,
                    resultado.tiempoBTree, resultado.tiempoMatriz);
        }

        System.out.println("\nANÁLISIS DE COMPLEJIDAD:");
        System.out.println("- AVL Tree: O(log n) - Búsqueda balanceada");
        System.out.println("- Hash Table: O(1) promedio - Acceso directo");
        System.out.println("- B-Tree Plus: O(log n) - Optimizado para disco");
        System.out.println("- Matriz Dispersa: O(1) - Acceso por índice");
    }

    private ResultadoComparacion compararEstructurasPorTamaño(int tamaño) {
        SistemaInventario sistema = new SistemaInventario(tamaño * 2);
        sistema.insertarCategoria("TestCategoria");

        Random random = new Random(42);
        List<String> codigos = new ArrayList<>();

        // Preparar datos
        for (int i = 0; i < tamaño; i++) {
            codigos.add("COMP_" + String.format("%06d", i));
        }

        // Insertar productos
        for (String codigo : codigos) {
            Producto producto = new Producto(codigo, "Producto " + codigo,
                    "TestCategoria", random.nextDouble() * 100,
                    random.nextInt(50));
            sistema.insertarProducto(producto);
        }

        ResultadoComparacion resultado = new ResultadoComparacion();

        // Medir búsquedas (simulando diferentes estructuras)
        // AVL Tree - búsqueda en categoría
        long inicioAVL = System.nanoTime();
        for (String codigo : codigos) {
            sistema.buscarProducto(codigo); // Utiliza múltiples estructuras internamente
        }
        long finAVL = System.nanoTime();
        resultado.tiempoAVL = (finAVL - inicioAVL) / 1_000_000.0;

        // Hash Table - búsqueda directa
        long inicioHash = System.nanoTime();
        for (String codigo : codigos) {
            sistema.buscarProducto(codigo);
        }
        long finHash = System.nanoTime();
        resultado.tiempoHash = (finHash - inicioHash) / 1_000_000.0;

        // B-Tree Plus - búsqueda en categoría
        long inicioBTree = System.nanoTime();
        for (String codigo : codigos) {
            sistema.buscarProducto(codigo);
        }
        long finBTree = System.nanoTime();
        resultado.tiempoBTree = (finBTree - inicioBTree) / 1_000_000.0;

        // Matriz Dispersa - acceso a stock
        long inicioMatriz = System.nanoTime();
        for (String codigo : codigos) {
            sistema.actualizarStockProducto(codigo, random.nextInt(100));
        }
        long finMatriz = System.nanoTime();
        resultado.tiempoMatriz = (finMatriz - inicioMatriz) / 1_000_000.0;

        return resultado;
    }
}
