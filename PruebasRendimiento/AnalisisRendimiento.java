package PruebasRendimiento;

public class AnalisisRendimiento {

    public static void main(String[] args) {
        System.out.println("=== ANÁLISIS DE RENDIMIENTO DEL SISTEMA DE INVENTARIO ===\n");

        AnalisisRendimiento analisis = new AnalisisRendimiento();

        // Ejecutar análisis completo
        analisis.ejecutarAnalisisCompleto();
    }

    public void ejecutarAnalisisCompleto() {
        // Tamaños de conjuntos de datos a probar
        int[] tamaños = {100, 500, 1000, 2000};

        System.out.println("Iniciando análisis de rendimiento...\n");

        // 1. Análisis de operaciones básicas
        analizarOperacionesBasicas(tamaños);

        // 2. Análisis de factor de carga en Hash Tables
        analizarFactorCarga();

        // 3. Análisis comparativo de estructuras
        analizarEstructurasComparativo(tamaños);

        // 4. Análisis de escalabilidad
        analizarEscalabilidad(tamaños);

        // 5. Generar reporte final
        generarReporteResumen();
    }

    private void analizarOperacionesBasicas(int[] tamaños) {
        System.out.println("==== ANÁLISIS DE OPERACIONES BÁSICAS ====");
        System.out.printf("%-10s %-15s %-15s %-15s %-15s%n",
                "Tamaño", "Inserción(ms)", "Búsqueda(ms)", "Eliminación(ms)", "Memoria(MB)");
        System.out.println("------------------------------------------------------------------------");

        for (int tamaño : tamaños) {
            MedidorRendimiento medidor = new MedidorRendimiento(tamaño);
            ResultadoRendimiento resultado = medidor.medirOperacionesBasicas();

            System.out.printf("%-10d %-15.2f %-15.2f %-15.2f %-15.2f%n",
                    tamaño,
                    resultado.tiempoInsercion,
                    resultado.tiempoBusqueda,
                    resultado.tiempoEliminacion,
                    resultado.memoriaUtilizada);
        }
        System.out.println();
    }

    private void analizarFactorCarga() {
        System.out.println("==== ANÁLISIS DE FACTOR DE CARGA EN HASH TABLES ====");

        AnalisisFactorCarga analisis = new AnalisisFactorCarga();
        analisis.ejecutarAnalisis();
        System.out.println();
    }

    private void analizarEstructurasComparativo(int[] tamaños) {
        System.out.println("==== ANÁLISIS COMPARATIVO DE ESTRUCTURAS ====");

        ComparadorEstructuras comparador = new ComparadorEstructuras();
        comparador.compararEstructuras(tamaños);
        System.out.println();
    }

    private void analizarEscalabilidad(int[] tamaños) {
        System.out.println("==== ANÁLISIS DE ESCALABILIDAD ====");

        AnalisisEscalabilidad escalabilidad = new AnalisisEscalabilidad();
        escalabilidad.analizarEscalabilidad(tamaños);
        System.out.println();
    }

    private void generarReporteResumen() {
        System.out.println("==== RESUMEN Y CONCLUSIONES ====");
        System.out.println("1. COMPLEJIDAD TEÓRICA vs PRÁCTICA:");
        System.out.println("   - AVL Tree: O(log n) - Confirma comportamiento logarítmico");
        System.out.println("   - Hash Tables: O(1) promedio - Depende del factor de carga");
        System.out.println("   - B-Tree Plus: O(log n) - Similar a AVL pero con mejor localidad");
        System.out.println("   - Matriz Dispersa: O(1) - Acceso directo por índices");
        System.out.println();

        System.out.println("2. RECOMENDACIONES:");
        System.out.println("   - Para búsquedas frecuentes: Hash Tables con factor de carga < 0.75");
        System.out.println("   - Para datos ordenados: AVL Tree o B-Tree Plus");
        System.out.println("   - Para grandes volúmenes: B-Tree Plus (mejor en disco)");
        System.out.println("   - Para acceso por posición: Matriz Dispersa");
        System.out.println();

        System.out.println("Análisis completado. Revise los resultados detallados arriba.");
    }
}