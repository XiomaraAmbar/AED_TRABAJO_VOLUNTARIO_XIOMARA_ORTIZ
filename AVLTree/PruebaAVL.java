package AVLTree;

import Excepciones.*;

public class PruebaAVL {

    public static void main(String[] args) {
        System.out.println("=== PRUEBAS COMPLETAS DE AVL TREE ===\n");

        //Crear instancia del AVL
        AVLTree<Integer> avl = new AVLTree<Integer>();

        //Test 1: Árbol vacío
        testArbolVacio(avl);

        //Test 2: Inserción básica
        testInsercionBasica(avl);

        //Test 3: Inserción con rotaciones
        testRotaciones(avl);

        //Test 4: Búsqueda
        testBusqueda(avl);

        //Test 5: Eliminación
        testEliminacion(avl);

        //Test 6: Recorridos
        testRecorridos(avl);

        //Test 7: Casos extremos
        testCasosExtremos();

        //Test 8: Validación AVL
        testValidacionAVL(avl);

        System.out.println("\n=== TODAS LAS PRUEBAS COMPLETADAS ===");
    }

    //Test 1: Verificar comportamiento con árbol vacío
    private static void testArbolVacio(AVLTree<Integer> avl) {
        System.out.println("--- TEST 1: ÁRBOL VACÍO ---");

        //Verificar propiedades básicas del árbol vacío
        System.out.println("¿Está vacío? " + avl.isEmpty());
        System.out.println("Tamaño: " + avl.size());
        System.out.println("Altura: " + avl.getHeight());

        //Probar búsqueda en árbol vacío - debe lanzar ItemNotFound
        try {
            avl.search(5);
            System.out.println("ERROR: Debería lanzar excepción al buscar en árbol vacío");
        } catch (ItemNotFound e) {
            System.out.println("✓ Excepción correcta al buscar en árbol vacío");
        }

        //Probar eliminación en árbol vacío - debe lanzar ExceptionIsEmpty
        try {
            avl.delete(5);
            System.out.println("ERROR: Debería lanzar excepción al eliminar en árbol vacío");
        } catch (ExceptionIsEmpty e) {
            System.out.println("✓ Excepción correcta al eliminar en árbol vacío");
        }

        System.out.println();
    }

    //Test 2: Inserción básica sin rotaciones
    private static void testInsercionBasica(AVLTree<Integer> avl) {
        System.out.println("--- TEST 2: INSERCIÓN BÁSICA ---");

        try {
            //Insertar elementos que no requieren rotaciones - árbol balanceado naturalmente
            avl.insert(10);
            avl.insert(5);
            avl.insert(15);
            avl.insert(3);
            avl.insert(7);

            System.out.println("✓ Inserción básica exitosa");
            System.out.println("Tamaño: " + avl.size());
            System.out.println("Altura: " + avl.getHeight());
            System.out.println("Árbol: " + avl.toString());

            //Probar inserción duplicada - debe lanzar ItemDuplicated
            try {
                avl.insert(10);
                System.out.println("ERROR: Debería lanzar excepción al insertar duplicado");
            } catch (ItemDuplicated e) {
                System.out.println("✓ Excepción correcta al insertar duplicado: " + e.getMessage());
            }

        } catch (ItemDuplicated e) {
            System.out.println("ERROR en inserción básica: " + e.getMessage());
        }

        System.out.println();
    }

    //Test 3: Inserción que requiere rotaciones
    private static void testRotaciones(AVLTree<Integer> avl) {
        System.out.println("--- TEST 3: ROTACIONES AVL ---");

        //Limpiar árbol para empezar desde cero
        avl.destroy();

        try {
            //Caso que requiere rotación derecha (LL) - inserción en línea izquierda
            System.out.println("Probando rotación LL (derecha):");
            avl.insert(30);
            avl.insert(20);
            avl.insert(10); //Esto debería activar rotación derecha

            System.out.println("Después de rotación LL:");
            avl.mostrarFactoresBalance();

            //Limpiar para siguiente prueba
            avl.destroy();

            //Caso que requiere rotación izquierda (RR) - inserción en línea derecha
            System.out.println("\nProbando rotación RR (izquierda):");
            avl.insert(10);
            avl.insert(20);
            avl.insert(30); //Esto debería activar rotación izquierda

            System.out.println("Después de rotación RR:");
            avl.mostrarFactoresBalance();

            //Limpiar para siguiente prueba
            avl.destroy();

            //Caso que requiere rotación LR - inserción zigzag izquierda-derecha
            System.out.println("\nProbando rotación LR (izquierda-derecha):");
            avl.insert(30);
            avl.insert(10);
            avl.insert(20); //Esto debería activar rotación LR

            System.out.println("Después de rotación LR:");
            avl.mostrarFactoresBalance();

            //Limpiar para siguiente prueba
            avl.destroy();

            //Caso que requiere rotación RL - inserción zigzag derecha-izquierda
            System.out.println("\nProbando rotación RL (derecha-izquierda):");
            avl.insert(10);
            avl.insert(30);
            avl.insert(20); //Esto debería activar rotación RL

            System.out.println("Después de rotación RL:");
            avl.mostrarFactoresBalance();

        } catch (ItemDuplicated e) {
            System.out.println("ERROR en prueba de rotaciones: " + e.getMessage());
        }

        System.out.println();
    }

    //Test 4: Búsqueda de elementos
    private static void testBusqueda(AVLTree<Integer> avl) {
        System.out.println("--- TEST 4: BÚSQUEDA ---");

        //Preparar árbol con varios elementos para probar búsqueda
        avl.destroy();
        try {
            int[] valores = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45};
            for (int valor : valores) {
                avl.insert(valor);
            }

            System.out.println("Árbol preparado con elementos: 50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45");
            System.out.println("Tamaño: " + avl.size());

            //Buscar elementos existentes - deben encontrarse
            System.out.println("\nBuscando elementos existentes:");
            int[] buscar = {50, 10, 80, 35};
            for (int valor : buscar) {
                try {
                    Integer encontrado = avl.search(valor);
                    System.out.println("✓ Encontrado: " + encontrado);
                } catch (ItemNotFound e) {
                    System.out.println("✗ No encontrado: " + valor);
                }
            }

            //Buscar elementos no existentes - deben lanzar ItemNotFound
            System.out.println("\nBuscando elementos no existentes:");
            int[] noExisten = {5, 15, 55, 90};
            for (int valor : noExisten) {
                try {
                    Integer encontrado = avl.search(valor);
                    System.out.println("ERROR: No debería encontrar: " + valor);
                } catch (ItemNotFound e) {
                    System.out.println("✓ Correctamente no encontrado: " + valor);
                }
            }

            //Probar funciones de min y max
            System.out.println("\nBuscando min y max:");
            System.out.println("Mínimo: " + avl.getMin());
            System.out.println("Máximo: " + avl.getMax());

        } catch (ItemDuplicated | ItemNotFound e) {
            System.out.println("ERROR en prueba de búsqueda: " + e.getMessage());
        }

        System.out.println();
    }

    //Test 5: Eliminación de elementos
    private static void testEliminacion(AVLTree<Integer> avl) {
        System.out.println("--- TEST 5: ELIMINACIÓN ---");

        try {
            System.out.println("Árbol antes de eliminaciones:");
            System.out.println("Tamaño: " + avl.size());
            avl.mostrarFactoresBalance();

            //Eliminar hoja - caso más simple
            System.out.println("\nEliminando hoja (10):");
            avl.delete(10);
            System.out.println("Tamaño después: " + avl.size());

            //Eliminar nodo con un hijo - caso intermedio
            System.out.println("\nEliminando nodo con un hijo (25):");
            avl.delete(25);
            System.out.println("Tamaño después: " + avl.size());

            //Eliminar nodo con dos hijos - caso más complejo
            System.out.println("\nEliminando nodo con dos hijos (30):");
            avl.delete(30);
            System.out.println("Tamaño después: " + avl.size());

            System.out.println("\nÁrbol después de eliminaciones:");
            avl.mostrarFactoresBalance();

            //Intentar eliminar elemento no existente - no debería cambiar el tamaño
            System.out.println("\nIntentando eliminar elemento no existente (99):");
            int tamañoAntes = avl.size();
            avl.delete(99);
            if (avl.size() == tamañoAntes) {
                System.out.println("✓ Tamaño sin cambios (elemento no existía)");
            }

        } catch (ExceptionIsEmpty e) {
            System.out.println("ERROR en eliminación: " + e.getMessage());
        }

        System.out.println();
    }

    //Test 6: Recorridos del árbol
    private static void testRecorridos(AVLTree<Integer> avl) {
        System.out.println("--- TEST 6: RECORRIDOS ---");

        try {
            System.out.println("Recorridos del árbol actual:");
            avl.InOrden();   //Izquierda, raíz, derecha
            avl.PreOrden();  //Raíz, izquierda, derecha
            avl.PostOrden(); //Izquierda, derecha, raíz

            //Probar recorridos en árbol vacío - deben lanzar excepciones
            System.out.println("\nProbando recorridos en árbol vacío:");
            AVLTree<Integer> avlVacio = new AVLTree<Integer>();

            try {
                avlVacio.InOrden();
                System.out.println("ERROR: Debería lanzar excepción");
            } catch (MensajeException e) {
                System.out.println("✓ Excepción correcta en InOrder: " + e.getMessage());
            }

            try {
                avlVacio.PreOrden();
                System.out.println("ERROR: Debería lanzar excepción");
            } catch (MensajeException e) {
                System.out.println("✓ Excepción correcta en PreOrden: " + e.getMessage());
            }

            try {
                avlVacio.PostOrden();
                System.out.println("ERROR: Debería lanzar excepción");
            } catch (MensajeException e) {
                System.out.println("✓ Excepción correcta en PostOrden: " + e.getMessage());
            }

        } catch (MensajeException e) {
            System.out.println("ERROR en recorridos: " + e.getMessage());
        }

        System.out.println();
    }

    //Test 7: Casos extremos
    private static void testCasosExtremos() {
        System.out.println("--- TEST 7: CASOS EXTREMOS ---");

        AVLTree<Integer> avlTest = new AVLTree<Integer>();

        try {
            //Inserción secuencial (peor caso para BST normal) - AVL debe mantener balance
            System.out.println("Insertando elementos secuenciales 1-10:");
            for (int i = 1; i <= 10; i++) {
                avlTest.insert(i);
            }

            System.out.println("Altura después de inserción secuencial: " + avlTest.getHeight());
            System.out.println("Tamaño: " + avlTest.size());

            //El AVL debería mantener una altura logarítmica
            double alturaEsperada = Math.log(10) / Math.log(2);
            System.out.println("Altura esperada (log₂(10)): " + String.format("%.2f", alturaEsperada));

            //Prueba con null - debe manejar correctamente valores nulos
            System.out.println("\nProbando inserción/eliminación con null:");
            try {
                avlTest.insert(null);
                System.out.println("ERROR: Debería lanzar excepción con null");
            } catch (Exception e) {
                System.out.println("✓ Excepción correcta con null en insert: " + e.getClass().getSimpleName());
            }

            try {
                avlTest.delete(null);
                System.out.println("ERROR: Debería lanzar excepción con null");
            } catch (Exception e) {
                System.out.println("✓ Excepción correcta con null en delete: " + e.getClass().getSimpleName());
            }

            //Eliminar todos los elementos - verificar que el árbol quede vacío
            System.out.println("\nEliminando todos los elementos:");
            for (int i = 1; i <= 10; i++) {
                avlTest.delete(i);
            }
            System.out.println("Tamaño después de eliminar todo: " + avlTest.size());
            System.out.println("¿Está vacío? " + avlTest.isEmpty());

        } catch (ItemDuplicated | ExceptionIsEmpty e) {
            System.out.println("ERROR en casos extremos: " + e.getMessage());
        }

        System.out.println();
    }

    //Test 8: Validación específica de propiedades AVL
    private static void testValidacionAVL(AVLTree<Integer> avl) {
        System.out.println("--- TEST 8: VALIDACIÓN AVL ---");

        //Crear un árbol balanceado con estructura interesante
        avl.destroy();
        try {
            //Insertar elementos que generen un árbol con múltiples niveles
            int[] valores = {50, 25, 75, 12, 37, 62, 87, 6, 18, 31, 43};
            for (int valor : valores) {
                avl.insert(valor);
            }

            System.out.println("Árbol creado con valores: 50, 25, 75, 12, 37, 62, 87, 6, 18, 31, 43");
            System.out.println("Tamaño final: " + avl.size());
            System.out.println("Altura final: " + avl.getHeight());

            //Mostrar factores de balance para verificar que están entre -1 y 1
            System.out.println("\nFactores de balance finales:");
            avl.mostrarFactoresBalance();

            //Verificar que el recorrido InOrder produzca una secuencia ordenada
            System.out.println("\nRecorrido InOrder (debería estar ordenado):");
            avl.InOrden();

            //Mostrar representación final del árbol
            System.out.println("\nRepresentación toString:");
            System.out.println(avl.toString());

        } catch (ItemDuplicated | MensajeException e) {
            System.out.println("ERROR en validación AVL: " + e.getMessage());
        }

        System.out.println();
    }
}