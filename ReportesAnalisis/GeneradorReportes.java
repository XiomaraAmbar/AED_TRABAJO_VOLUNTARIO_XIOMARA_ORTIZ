package ReportesAnalisis;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class GeneradorReportes {

    private List<DatoRendimiento> datosRendimiento;
    private String directorioReportes;

    public GeneradorReportes() {
        this.datosRendimiento = new ArrayList<>();
        this.directorioReportes = "reportes_rendimiento";
        crearDirectorioReportes();
    }

    private void crearDirectorioReportes() {
        File directorio = new File(directorioReportes);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
    }

    public void agregarDatoRendimiento(DatoRendimiento dato) {
        datosRendimiento.add(dato);
    }

    public void generarReporteCompleto() {
        System.out.println("Generando reporte completo de rendimiento...");

        try {
            generarReporteCSV();
            generarReporteHTML();
            generarAnalisisEstadistico();
            generarRecomendaciones();

            System.out.println("Reportes generados en directorio: " + directorioReportes);
        } catch (IOException e) {
            System.err.println("Error al generar reportes: " + e.getMessage());
        }
    }

    private void generarReporteCSV() throws IOException {
        String nombreArchivo = directorioReportes + "/rendimiento_" +
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            // Cabecera
            writer.println("Tamaño,Operacion,Tiempo_ms,Memoria_MB,Throughput_ops_sec,Complejidad_Teorica");

            // Datos
            for (DatoRendimiento dato : datosRendimiento) {
                writer.printf("%d,%s,%.2f,%.2f,%.2f,%s%n",
                        dato.tamaño, dato.operacion, dato.tiempo,
                        dato.memoria, dato.throughput, dato.complejidadTeorica);
            }
        }
    }

    private void generarReporteHTML() throws IOException {
        String nombreArchivo = directorioReportes + "/reporte_rendimiento.html";

        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            writer.println("<!DOCTYPE html>");
            writer.println("<html lang='es'>");
            writer.println("<head>");
            writer.println("    <meta charset='UTF-8'>");
            writer.println("    <title>Reporte de Rendimiento - Sistema de Inventario</title>");
            writer.println("    <style>");
            writer.println("        body { font-family: Arial, sans-serif; margin: 20px; }");
            writer.println("        table { border-collapse: collapse; width: 100%; }");
            writer.println("        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            writer.println("        th { background-color: #f2f2f2; }");
            writer.println("        .section { margin: 20px 0; }");
            writer.println("        .chart { width: 100%; height: 400px; margin: 20px 0; }");
            writer.println("        .conclusion { background-color: #f9f9f9; padding: 15px; border-left: 4px solid #007acc; }");
            writer.println("    </style>");
            writer.println("</head>");
            writer.println("<body>");

            writer.println("<h1>Reporte de Análisis de Rendimiento</h1>");
            writer.println("<p>Fecha de generación: " + new Date() + "</p>");

            // Resumen ejecutivo
            generarResumenEjecutivo(writer);

            // Tabla de resultados
            generarTablaResultados(writer);

            // Análisis de complejidad
            generarAnalisisComplejidad(writer);

            // Recomendaciones
            generarRecomendacionesHTML(writer);

            writer.println("</body>");
            writer.println("</html>");
        }
    }

    private void generarResumenEjecutivo(PrintWriter writer) {
        writer.println("<div class='section'>");
        writer.println("<h2>Resumen Ejecutivo</h2>");

        if (!datosRendimiento.isEmpty()) {
            DatoRendimiento mejorInsercion = datosRendimiento.stream()
                    .filter(d -> d.operacion.equals("Insercion"))
                    .min(Comparator.comparingDouble(d -> d.tiempo))
                    .orElse(null);

            DatoRendimiento mejorBusqueda = datosRendimiento.stream()
                    .filter(d -> d.operacion.equals("Busqueda"))
                    .min(Comparator.comparingDouble(d -> d.tiempo))
                    .orElse(null);

            writer.println("<ul>");
            if (mejorInsercion != null) {
                writer.printf("<li>Mejor rendimiento en inserción: %.2f ms para %d elementos</li>%n",
                        mejorInsercion.tiempo, mejorInsercion.tamaño);
            }
            if (mejorBusqueda != null) {
                writer.printf("<li>Mejor rendimiento en búsqueda: %.2f ms para %d elementos</li>%n",
                        mejorBusqueda.tiempo, mejorBusqueda.tamaño);
            }
            writer.println("</ul>");
        }

        writer.println("</div>");
    }

    private void generarTablaResultados(PrintWriter writer) {
        writer.println("<div class='section'>");
        writer.println("<h2>Resultados Detallados</h2>");
        writer.println("<table>");
        writer.println("<tr><th>Tamaño</th><th>Operación</th><th>Tiempo (ms)</th><th>Memoria (MB)</th><th>Throughput</th><th>Complejidad</th></tr>");

        for (DatoRendimiento dato : datosRendimiento) {
            writer.printf("<tr><td>%d</td><td>%s</td><td>%.2f</td><td>%.2f</td><td>%.2f</td><td>%s</td></tr>%n",
                    dato.tamaño, dato.operacion, dato.tiempo,
                    dato.memoria, dato.throughput, dato.complejidadTeorica);
        }

        writer.println("</table>");
        writer.println("</div>");
    }

    private void generarAnalisisComplejidad(PrintWriter writer) {
        writer.println("<div class='section'>");
        writer.println("<h2>Análisis de Complejidad</h2>");

        writer.println("<h3>Estructuras de Datos Analizadas:</h3>");
        writer.println("<ul>");
        writer.println("<li><strong>AVL Tree:</strong> O(log n) - Búsqueda balanceada, garantiza altura logarítmica</li>");
        writer.println("<li><strong>Hash Table:</strong> O(1) promedio - Acceso directo, degrada con colisiones</li>");
        writer.println("<li><strong>B-Tree Plus:</strong> O(log n) - Optimizado para operaciones en disco</li>");
        writer.println("<li><strong>Matriz Dispersa:</strong> O(1) - Acceso directo por índices</li>");
        writer.println("</ul>");

        writer.println("<h3>Verificación Empírica:</h3>");
        analizarTendencias(writer);

        writer.println("</div>");
    }

    private void analizarTendencias(PrintWriter writer) {
        Map<String, List<DatoRendimiento>> datosPorOperacion = new HashMap<>();

        for (DatoRendimiento dato : datosRendimiento) {
            datosPorOperacion.computeIfAbsent(dato.operacion, k -> new ArrayList<>()).add(dato);
        }

        writer.println("<table>");
        writer.println("<tr><th>Operación</th><th>Tendencia Observada</th><th>Coincide con Teoría</th></tr>");

        for (Map.Entry<String, List<DatoRendimiento>> entry : datosPorOperacion.entrySet()) {
            String operacion = entry.getKey();
            List<DatoRendimiento> datos = entry.getValue();
            datos.sort(Comparator.comparingInt(d -> d.tamaño));

            String tendencia = calcularTendencia(datos);
            String coincidencia = evaluarCoincidencia(tendencia, operacion);

            writer.printf("<tr><td>%s</td><td>%s</td><td>%s</td></tr>%n",
                    operacion, tendencia, coincidencia);
        }

        writer.println("</table>");
    }

    private String calcularTendencia(List<DatoRendimiento> datos) {
        if (datos.size() < 2) return "Datos insuficientes";

        double primerTiempo = datos.get(0).tiempo;
        double ultimoTiempo = datos.get(datos.size() - 1).tiempo;
        int primerTamaño = datos.get(0).tamaño;
        int ultimoTamaño = datos.get(datos.size() - 1).tamaño;

        double ratioTiempo = ultimoTiempo / primerTiempo;
        double ratioTamaño = (double) ultimoTamaño / primerTamaño;

        if (ratioTiempo < ratioTamaño * 0.5) {
            return "Mejor que lineal (posiblemente constante)";
        } else if (ratioTiempo < ratioTamaño * 1.5) {
            return "Aproximadamente lineal";
        } else if (ratioTiempo < ratioTamaño * Math.log(ratioTamaño) * 1.5) {
            return "Aproximadamente logarítmica";
        } else {
            return "Peor que logarítmica";
        }
    }

    private String evaluarCoincidencia(String tendencia, String operacion) {
        if (operacion.contains("Hash") && tendencia.contains("constante")) {
            return "✓ Sí";
        } else if (operacion.contains("AVL") && tendencia.contains("logarítmica")) {
            return "✓ Sí";
        } else if (operacion.contains("BTree") && tendencia.contains("logarítmica")) {
            return "✓ Sí";
        } else {
            return "⚠ Parcialmente";
        }
    }

    private void generarRecomendacionesHTML(PrintWriter writer) {
        writer.println("<div class='section conclusion'>");
        writer.println("<h2>Recomendaciones</h2>");

        writer.println("<h3>Para Optimización de Rendimiento:</h3>");
        writer.println("<ul>");
        writer.println("<li><strong>Búsquedas frecuentes:</strong> Utilizar Hash Tables con factor de carga < 0.75</li>");
        writer.println("<li><strong>Datos ordenados:</strong> Preferir AVL Tree o B-Tree Plus</li>");
        writer.println("<li><strong>Grandes volúmenes:</strong> B-Tree Plus para mejor gestión de memoria</li>");
        writer.println("<li><strong>Acceso por posición:</strong> Matriz Dispersa para operaciones O(1)</li>");
        writer.println("</ul>");

        writer.println("<h3>Para Escalabilidad:</h3>");
        writer.println("<ul>");
        writer.println("<li>Monitorear factor de carga en Hash Tables</li>");
        writer.println("<li>Considerar rehashing automático cuando factor > 0.75</li>");
        writer.println("<li>Implementar pooling de objetos para reducir GC</li>");
        writer.println("<li>Considerar particionamiento para datasets > 100K elementos</li>");
        writer.println("</ul>");

        writer.println("</div>");
    }

    private void generarAnalisisEstadistico() throws IOException {
        String nombreArchivo = directorioReportes + "/analisis_estadistico.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            writer.println("ANÁLISIS ESTADÍSTICO DE RENDIMIENTO");
            writer.println("=====================================");
            writer.println();

            Map<String, List<DatoRendimiento>> datosPorOperacion = new HashMap<>();
            for (DatoRendimiento dato : datosRendimiento) {
                datosPorOperacion.computeIfAbsent(dato.operacion, k -> new ArrayList<>()).add(dato);
            }

            for (Map.Entry<String, List<DatoRendimiento>> entry : datosPorOperacion.entrySet()) {
                String operacion = entry.getKey();
                List<DatoRendimiento> datos = entry.getValue();

                writer.println("Operación: " + operacion);
                writer.println("-".repeat(30));

                EstadisticasDescriptivas stats = calcularEstadisticas(datos);
                writer.printf("Tiempo promedio: %.2f ms%n", stats.media);
                writer.printf("Desviación estándar: %.2f ms%n", stats.desviacionEstandar);
                writer.printf("Mínimo: %.2f ms%n", stats.minimo);
                writer.printf("Máximo: %.2f ms%n", stats.maximo);
                writer.printf("Mediana: %.2f ms%n", stats.mediana);
                writer.printf("Coeficiente de variación: %.2f%%%n", stats.coeficienteVariacion);
                writer.println();
            }

            // Análisis de correlación
            writer.println("ANÁLISIS DE CORRELACIÓN");
            writer.println("========================");
            analizarCorrelacion(writer);
        }
    }

    private EstadisticasDescriptivas calcularEstadisticas(List<DatoRendimiento> datos) {
        EstadisticasDescriptivas stats = new EstadisticasDescriptivas();

        if (datos.isEmpty()) return stats;

        // Extraer tiempos
        double[] tiempos = datos.stream().mapToDouble(d -> d.tiempo).toArray();
        Arrays.sort(tiempos);

        // Calcular estadísticas
        stats.minimo = tiempos[0];
        stats.maximo = tiempos[tiempos.length - 1];
        stats.media = Arrays.stream(tiempos).average().orElse(0.0);
        stats.mediana = tiempos.length % 2 == 0 ?
                (tiempos[tiempos.length/2 - 1] + tiempos[tiempos.length/2]) / 2.0 :
                tiempos[tiempos.length/2];

        // Desviación estándar
        double sumaCuadrados = Arrays.stream(tiempos)
                .map(t -> Math.pow(t - stats.media, 2))
                .sum();
        stats.desviacionEstandar = Math.sqrt(sumaCuadrados / tiempos.length);

        // Coeficiente de variación
        stats.coeficienteVariacion = (stats.desviacionEstandar / stats.media) * 100;

        return stats;
    }

    private void analizarCorrelacion(PrintWriter writer) {
        if (datosRendimiento.size() < 2) {
            writer.println("Datos insuficientes para análisis de correlación");
            return;
        }

        // Correlación entre tamaño y tiempo
        double[] tamaños = datosRendimiento.stream().mapToDouble(d -> d.tamaño).toArray();
        double[] tiempos = datosRendimiento.stream().mapToDouble(d -> d.tiempo).toArray();

        double correlacion = calcularCorrelacion(tamaños, tiempos);
        writer.printf("Correlación Tamaño-Tiempo: %.3f%n", correlacion);

        if (correlacion > 0.8) {
            writer.println("Correlación fuerte positiva - El tiempo aumenta significativamente con el tamaño");
        } else if (correlacion > 0.5) {
            writer.println("Correlación moderada positiva - El tiempo tiende a aumentar con el tamaño");
        } else if (correlacion > 0.2) {
            writer.println("Correlación débil positiva - Ligera tendencia al aumento");
        } else {
            writer.println("Correlación baja - El tiempo no depende fuertemente del tamaño");
        }

        // Correlación entre memoria y tamaño
        double[] memorias = datosRendimiento.stream().mapToDouble(d -> d.memoria).toArray();
        double correlacionMemoria = calcularCorrelacion(tamaños, memorias);
        writer.printf("Correlación Tamaño-Memoria: %.3f%n", correlacionMemoria);
    }

    private double calcularCorrelacion(double[] x, double[] y) {
        if (x.length != y.length || x.length < 2) return 0.0;

        double mediaX = Arrays.stream(x).average().orElse(0.0);
        double mediaY = Arrays.stream(y).average().orElse(0.0);

        double numerador = 0.0;
        double denominadorX = 0.0;
        double denominadorY = 0.0;

        for (int i = 0; i < x.length; i++) {
            double difX = x[i] - mediaX;
            double difY = y[i] - mediaY;

            numerador += difX * difY;
            denominadorX += difX * difX;
            denominadorY += difY * difY;
        }

        if (denominadorX == 0 || denominadorY == 0) return 0.0;

        return numerador / Math.sqrt(denominadorX * denominadorY);
    }

    private void generarRecomendaciones() throws IOException {
        String nombreArchivo = directorioReportes + "/recomendaciones.md";

        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            writer.println("# Recomendaciones de Optimización");
            writer.println();

            writer.println("## Resumen de Hallazgos");
            writer.println();

            // Analizar los datos para generar recomendaciones específicas
            Map<String, List<DatoRendimiento>> datosPorOperacion = new HashMap<>();
            for (DatoRendimiento dato : datosRendimiento) {
                datosPorOperacion.computeIfAbsent(dato.operacion, k -> new ArrayList<>()).add(dato);
            }

            for (Map.Entry<String, List<DatoRendimiento>> entry : datosPorOperacion.entrySet()) {
                String operacion = entry.getKey();
                List<DatoRendimiento> datos = entry.getValue();

                writer.println("### " + operacion);
                writer.println();

                EstadisticasDescriptivas stats = calcularEstadisticas(datos);

                if (stats.coeficienteVariacion > 50) {
                    writer.println("⚠️ **Alta variabilidad detectada** - Considerar:");
                    writer.println("- Optimización de algoritmos");
                    writer.println("- Revisión de estructuras de datos");
                    writer.println("- Análisis de casos extremos");
                } else if (stats.coeficienteVariacion > 25) {
                    writer.println("🔍 **Variabilidad moderada** - Monitorear:");
                    writer.println("- Patrones de acceso a datos");
                    writer.println("- Distribución de cargas de trabajo");
                } else {
                    writer.println("✅ **Rendimiento estable** - Mantener:");
                    writer.println("- Configuración actual");
                    writer.println("- Estrategias de optimización existentes");
                }
                writer.println();
            }

            writer.println("## Recomendaciones Específicas");
            writer.println();

            writer.println("### Hash Tables");
            writer.println("- Mantener factor de carga < 0.75");
            writer.println("- Implementar rehashing automático");
            writer.println("- Considerar hash functions más robustas");
            writer.println();

            writer.println("### AVL Trees");
            writer.println("- Evaluar si el balanceo automático justifica el overhead");
            writer.println("- Considerar Red-Black Trees para mejor rendimiento de inserción");
            writer.println("- Implementar rotaciones lazy para casos específicos");
            writer.println();

            writer.println("### B-Tree Plus");
            writer.println("- Ajustar orden del árbol según patrón de acceso");
            writer.println("- Implementar caching de nodos frecuentemente accedidos");
            writer.println("- Considerar compresión de nodos para datasets grandes");
            writer.println();

            writer.println("### Matriz Dispersa");
            writer.println("- Optimizar estructura para patrones de acceso específicos");
            writer.println("- Considerar diferentes estrategias de almacenamiento");
            writer.println("- Implementar compresión para matrices muy dispersas");
            writer.println();

            writer.println("## Próximos Pasos");
            writer.println();
            writer.println("1. Implementar métricas de monitoreo continuo");
            writer.println("2. Establecer benchmarks de rendimiento");
            writer.println("3. Automatizar pruebas de regresión");
            writer.println("4. Crear alertas para degradación de rendimiento");
            writer.println("5. Documentar casos de uso optimizados");
        }
    }

    public void ejecutarAnalisisCompleto() {
        System.out.println("Ejecutando análisis completo de rendimiento...");

        // Generar datos de prueba
        generarDatosPrueba();

        // Generar todos los reportes
        generarReporteCompleto();

        System.out.println("Análisis completo finalizado.");
    }

    private void generarDatosPrueba() {
        // Simular datos de diferentes operaciones y tamaños
        Random random = new Random(42);
        int[] tamaños = {100, 500, 1000, 2000, 5000, 10000};
        String[] operaciones = {"Insercion", "Busqueda", "Eliminacion", "HashLookup", "AVLSearch", "BTreeSearch"};

        for (int tamaño : tamaños) {
            for (String operacion : operaciones) {
                DatoRendimiento dato = new DatoRendimiento();
                dato.tamaño = tamaño;
                dato.operacion = operacion;

                // Simular tiempos basados en complejidad teórica
                if (operacion.contains("Hash")) {
                    dato.tiempo = 1.0 + random.nextGaussian() * 0.2; // O(1)
                    dato.complejidadTeorica = "O(1)";
                } else if (operacion.contains("AVL") || operacion.contains("BTree")) {
                    dato.tiempo = Math.log(tamaño) * 0.5 + random.nextGaussian() * 0.1; // O(log n)
                    dato.complejidadTeorica = "O(log n)";
                } else {
                    dato.tiempo = tamaño * 0.001 + random.nextGaussian() * 0.05; // O(n)
                    dato.complejidadTeorica = "O(n)";
                }

                dato.memoria = tamaño * 0.05 + random.nextGaussian() * 0.01;
                dato.throughput = tamaño / (dato.tiempo / 1000.0);

                agregarDatoRendimiento(dato);
            }
        }
    }
}