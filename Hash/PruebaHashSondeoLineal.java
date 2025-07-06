package Hash;

public class PruebaHashSondeoLineal {
    public static void main(String[] args) {
        System.out.println("=== PRUEBA DE TABLA HASH CON SONDEO LINEAL ===\n");

        // Crear tabla hash con capacidad inicial de 7
        HashSondeoLineal<Integer> tablaEnteros = new HashSondeoLineal<>(7);

        System.out.println("1. INFORMACIÓN INICIAL:");
        System.out.println("Capacidad inicial: " + tablaEnteros.getCapacidad());
        System.out.println("Módulo utilizado: " + tablaEnteros.getModulo());
        System.out.println("Elementos: " + tablaEnteros.getContadorElementos());
        System.out.println("Factor de carga: " + String.format("%.2f", tablaEnteros.factorCarga()));
        System.out.println();

        // Insertar números enteros
        System.out.println("2. INSERTANDO NÚMEROS ENTEROS:");
        int[] numeros = {123, 456, 789, 321, 654, 987};

        for (int num : numeros) {
            boolean insertado = tablaEnteros.insertar(num);
            System.out.println("Insertando " + num + ": " + (insertado ? "ÉXITO" : "FALLÓ"));
        }
        System.out.println();

        // Mostrar tabla después de insertar
        System.out.println("3. ESTADO DE LA TABLA DESPUÉS DE INSERTAR:");
        tablaEnteros.mostrarTabla();
        System.out.println();

        // Buscar elementos
        System.out.println("4. BUSCANDO ELEMENTOS:");
        int[] buscar = {123, 999, 456, 111, 789};

        for (int num : buscar) {
            boolean encontrado = tablaEnteros.buscar(num);
            System.out.println("Buscando " + num + ": " + (encontrado ? "ENCONTRADO" : "NO ENCONTRADO"));
        }
        System.out.println();

        // Eliminar un elemento
        System.out.println("5. ELIMINANDO ELEMENTO:");
        boolean eliminado = tablaEnteros.eliminar(456);
        System.out.println("Eliminando 456: " + (eliminado ? "ÉXITO" : "FALLÓ"));
        System.out.println("Elementos después de eliminar: " + tablaEnteros.getContadorElementos());
        System.out.println();

        // Forzar rehashing insertando más elementos
        System.out.println("6. FORZANDO REHASHING:");
        int[] masNumeros = {111, 222, 333, 444, 555};

        for (int num : masNumeros) {
            System.out.println("Insertando " + num + " (Factor de carga antes: " +
                    String.format("%.2f", tablaEnteros.factorCarga()) + ")");
            tablaEnteros.insertar(num);
        }
        System.out.println();

        // Mostrar tabla final
        System.out.println("7. ESTADO FINAL DE LA TABLA:");
        tablaEnteros.mostrarTabla();
        System.out.println();

        // Prueba con strings
        System.out.println("8. PRUEBA CON STRINGS:");
        HashSondeoLineal<String> tablaStrings = new HashSondeoLineal<>(5);

        String[] palabras = {"casa", "perro", "gato", "sol", "luna", "mar"};

        System.out.println("Módulo para strings: " + tablaStrings.getModulo());

        for (String palabra : palabras) {
            boolean insertado = tablaStrings.insertar(palabra);
            System.out.println("Insertando '" + palabra + "': " + (insertado ? "ÉXITO" : "FALLÓ"));
        }
        System.out.println();

        tablaStrings.mostrarTabla();
        System.out.println();

        // Buscar en tabla de strings
        System.out.println("9. BUSCANDO EN TABLA DE STRINGS:");
        String[] buscarStrings = {"casa", "agua", "gato", "fuego"};

        for (String palabra : buscarStrings) {
            boolean encontrado = tablaStrings.buscar(palabra);
            System.out.println("Buscando '" + palabra + "': " + (encontrado ? "ENCONTRADO" : "NO ENCONTRADO"));
        }
        System.out.println();

        // Prueba de elementos duplicados
        System.out.println("10. PRUEBA DE ELEMENTOS DUPLICADOS:");
        boolean duplicado = tablaEnteros.insertar(123); // Ya existe
        System.out.println("Insertando 123 (duplicado): " + (duplicado ? "ÉXITO" : "FALLÓ (esperado)"));

        duplicado = tablaStrings.insertar("casa"); // Ya existe
        System.out.println("Insertando 'casa' (duplicado): " + (duplicado ? "ÉXITO" : "FALLÓ (esperado)"));

        System.out.println("\n=== PRUEBA COMPLETADA ===");
    }
}