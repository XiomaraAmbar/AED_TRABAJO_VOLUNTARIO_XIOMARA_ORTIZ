package Hash;

import LinkedList.MensajeException;

public class PruebaEncadenamiento {
    public static void main(String[] args) {
        System.out.println("=== PRUEBA DE TABLA HASH CON ENCADENAMIENTO ===\n");

        // Crear tabla hash con capacidad inicial pequeña para probar rehashing
        HashEncadenamiento<Integer> tablaNumeros = new HashEncadenamiento<>(5);
        HashEncadenamiento<String> tablaCadenas = new HashEncadenamiento<>(7);

        try {
            // ===== PRUEBA CON NÚMEROS =====
            System.out.println("🔢 PRUEBA CON NÚMEROS:");
            System.out.println("Capacidad inicial: " + tablaNumeros.getCapacidad());
            System.out.println("Módulo: " + tablaNumeros.getModulo());
            System.out.println();

            // Insertar números
            int[] numeros = {123, 456, 789, 321, 654, 987, 147, 258, 369};
            System.out.println("Insertando números: ");
            for (int num : numeros) {
                System.out.print(num + " ");
                tablaNumeros.insertarClave(num);
            }
            System.out.println("\n");

            // Mostrar tabla después de inserciones
            tablaNumeros.mostrarTabla();
            System.out.println();

            // Buscar algunos números
            System.out.println("🔍 BÚSQUEDAS:");
            int[] buscar = {123, 999, 456, 111, 789};
            for (int num : buscar) {
                boolean encontrado = tablaNumeros.buscarClave(num);
                System.out.println("Buscar " + num + ": " + (encontrado ? "✓ ENCONTRADO" : "✗ NO ENCONTRADO"));
            }
            System.out.println();

            // Eliminar algunos números
            System.out.println("🗑️ ELIMINACIONES:");
            int[] eliminar = {123, 999, 456};
            for (int num : eliminar) {
                boolean eliminado = tablaNumeros.eliminarClave(num);
                System.out.println("Eliminar " + num + ": " + (eliminado ? "✓ ELIMINADO" : "✗ NO EXISTÍA"));
            }
            System.out.println();

            // Mostrar tabla después de eliminaciones
            System.out.println("📊 TABLA DESPUÉS DE ELIMINACIONES:");
            tablaNumeros.mostrarTabla();
            System.out.println();

            // ===== PRUEBA CON STRINGS =====
            System.out.println("🔤 PRUEBA CON CADENAS:");
            System.out.println("Capacidad inicial: " + tablaCadenas.getCapacidad());
            System.out.println("Módulo: " + tablaCadenas.getModulo());
            System.out.println();

            // Insertar cadenas
            String[] cadenas = {"Juan", "María", "Pedro", "Ana", "Carlos", "Laura", "Miguel", "Sofia", "Diego"};
            System.out.println("Insertando cadenas: ");
            for (String str : cadenas) {
                System.out.print(str + " ");
                tablaCadenas.insertarClave(str);
            }
            System.out.println("\n");

            // Mostrar tabla de cadenas
            tablaCadenas.mostrarTabla();
            System.out.println();

            // Buscar algunas cadenas
            System.out.println("🔍 BÚSQUEDAS EN CADENAS:");
            String[] buscarStr = {"Juan", "Roberto", "Ana", "Patricia", "Carlos"};
            for (String str : buscarStr) {
                boolean encontrado = tablaCadenas.buscarClave(str);
                System.out.println("Buscar '" + str + "': " + (encontrado ? "✓ ENCONTRADO" : "✗ NO ENCONTRADO"));
            }
            System.out.println();

            // ===== PRUEBA DE REHASHING FORZADO =====
            System.out.println("🔄 PRUEBA DE REHASHING FORZADO:");
            HashEncadenamiento<Integer> tablaRehash = new HashEncadenamiento<>(3);
            System.out.println("Tabla inicial (capacidad 3):");
            tablaRehash.mostrarTabla();

            // Insertar suficientes elementos para forzar rehashing
            System.out.println("\nInsertando elementos para forzar rehashing...");
            for (int i = 1; i <= 10; i++) {
                System.out.println("Insertando: " + (i * 10));
                tablaRehash.insertarClave(i * 10);

                // Mostrar información después de cada inserción
                System.out.println("Factor de carga: " + String.format("%.2f", tablaRehash.factorCarga()));
                System.out.println("Capacidad actual: " + tablaRehash.getCapacidad());
                System.out.println("Elementos: " + tablaRehash.getContadorElementos());
                System.out.println("---");
            }

            System.out.println("\n📊 TABLA FINAL DESPUÉS DE REHASHING:");
            tablaRehash.mostrarTabla();

            // ===== PRUEBA CON DUPLICADOS =====
            System.out.println("\n🔄 PRUEBA CON DUPLICADOS:");
            HashEncadenamiento<String> tablaDuplicados = new HashEncadenamiento<>(5);

            // Insertar elementos duplicados
            String[] elementosDuplicados = {"A", "B", "A", "C", "B", "A"};
            for (String elemento : elementosDuplicados) {
                tablaDuplicados.insertarClave(elemento);
                System.out.println("Insertado: " + elemento);
            }

            System.out.println("\n📊 TABLA CON DUPLICADOS:");
            tablaDuplicados.mostrarTabla();

            // Eliminar un duplicado
            System.out.println("\nEliminando un 'A':");
            tablaDuplicados.eliminarClave("A");
            tablaDuplicados.mostrarTabla();

            System.out.println("\n✅ TODAS LAS PRUEBAS COMPLETADAS EXITOSAMENTE!");

        } catch (MensajeException e) {
            System.err.println("❌ Error durante las pruebas: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}