package PruebasRendimiento;

import Inventario.*;
import java.util.*;

public class PruebasEstres {

    public void ejecutarPruebasEstres() {
        System.out.println("==== PRUEBAS DE ESTRÉS ====");

        // Prueba de inserción masiva
        pruebaInsercionMasiva();

        // Prueba de búsqueda intensiva
        pruebaBusquedaIntensiva();

        // Prueba de operaciones concurrentes simuladas
        pruebaOperacionesMixtas();

        // Prueba de límites del sistema
        pruebaLimitesDelSistema();
    }

    private void pruebaInsercionMasiva() {
        System.out.println("\n--- Prueba de Inserción Masiva ---");
        int[] tamaños = {500, 1000, 2000};

        for (int tamaño : tamaños) {
            System.out.printf("Insertando %d productos...", tamaño);

            SistemaInventario sistema = new SistemaInventario(tamaño * 2);
            sistema.insertarCategoria("TestMasivo");

            long inicio = System.nanoTime();

            for (int i = 0; i < tamaño; i++) {
                Producto producto = new Producto("MASIVO_" + i, "Producto " + i,
                        "TestMasivo", 100.0, 10);
                sistema.insertarProducto(producto);
            }

            long fin = System.nanoTime();
            double tiempo = (fin - inicio) / 1_000_000.0;

            System.out.printf(" Completado en %.2f ms (%.2f ms/producto)%n",
                    tiempo, tiempo / tamaño);
        }
    }

    private void pruebaBusquedaIntensiva() {
        System.out.println("\n--- Prueba de Búsqueda Intensiva ---");

        SistemaInventario sistema = new SistemaInventario(2000);
        sistema.insertarCategoria("TestBusqueda");

        // Preparar datos
        List<String> codigos = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            String codigo = "BUSQ_" + i;
            codigos.add(codigo);
            Producto producto = new Producto(codigo, "Producto " + i,
                    "TestBusqueda", 100.0, 10);
            sistema.insertarProducto(producto);
        }

        // Búsqueda intensiva
        long inicio = System.nanoTime();
        for (int ciclo = 0; ciclo < 10; ciclo++) {
            for (String codigo : codigos) {
                sistema.buscarProducto(codigo);
            }
        }
        long fin = System.nanoTime();

        double tiempo = (fin - inicio) / 1_000_000.0;
        System.out.printf("10,000 búsquedas completadas en %.2f ms%n", tiempo);
    }

    private void pruebaOperacionesMixtas() {
        System.out.println("\n--- Prueba de Operaciones Mixtas ---");

        SistemaInventario sistema = new SistemaInventario(2000);
        sistema.insertarCategoria("TestMixto");

        Random random = new Random(42);
        List<String> codigos = new ArrayList<>();

        long inicio = System.nanoTime();

        for (int i = 0; i < 1000; i++) {
            String codigo = "MIXTO_" + i;
            codigos.add(codigo);

            // Insertar
            Producto producto = new Producto(codigo, "Producto " + i,
                    "TestMixto", random.nextDouble() * 100,
                    random.nextInt(50));
            sistema.insertarProducto(producto);

            // Buscar algunos productos existentes
            if (i > 0 && i % 10 == 0) {
                sistema.buscarProducto(codigos.get(random.nextInt(codigos.size())));
            }

            // Actualizar stock de algunos productos
            if (i > 0 && i % 20 == 0) {
                sistema.actualizarStockProducto(codigos.get(random.nextInt(codigos.size())),
                        random.nextInt(100));
            }
        }

        long fin = System.nanoTime();
        double tiempo = (fin - inicio) / 1_000_000.0;

        System.out.printf("Operaciones mixtas completadas en %.2f ms%n", tiempo);
    }

    private void pruebaLimitesDelSistema() {
        System.out.println("\n--- Prueba de Límites del Sistema ---");

        try {
            SistemaInventario sistema = new SistemaInventario(2000);
            sistema.insertarCategoria("TestLimites");

            System.out.println("Probando límites de memoria...");

            int insertados = 0;
            long inicioMemoria = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            while (insertados < 2000) {
                String codigo = "LIMITE_" + insertados;
                Producto producto = new Producto(codigo, "Producto " + insertados,
                        "TestLimites", 100.0, 10);

                if (sistema.insertarProducto(producto)) {
                    insertados++;

                    if (insertados % 250 == 0) {
                        long memoriaActual = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                        double memoriaUsada = (memoriaActual - inicioMemoria) / (1024.0 * 1024.0);
                        System.out.printf("Insertados: %d, Memoria usada: %.2f MB%n",
                                insertados, memoriaUsada);
                    }
                } else {
                    System.out.println("Límite alcanzado en: " + insertados + " productos");
                    break;
                }
            }

        } catch (OutOfMemoryError e) {
            System.out.println("Límite de memoria alcanzado durante las pruebas");
        }
    }
}
