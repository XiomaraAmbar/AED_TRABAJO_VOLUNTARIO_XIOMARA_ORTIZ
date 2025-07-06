package ReportesAnalisis;

import java.util.*;
import Inventario.*;

class AnalisisFactorCargaDetallado {

    public void ejecutarAnalisisDetallado() {
        System.out.println("\n==== ANÁLISIS DETALLADO DE FACTOR DE CARGA ====");

        // Probar diferentes configuraciones
        pruebasFactorCargaProgresivo();
        pruebasDistribucionColisiones();
        pruebasRehashing();

        System.out.println("Análisis de factor de carga completado.\n");
    }

    private void pruebasFactorCargaProgresivo() {
        System.out.println("\n--- Pruebas de Factor de Carga Progresivo ---");
        System.out.printf("%-15s %-15s %-15s %-15s %-15s%n",
                "Factor Carga", "Tiempo Ins", "Tiempo Bus", "Colisiones", "Rehashes");
        System.out.println("-".repeat(75));

        int numElementos = 1000;
        double[] factores = {0.1, 0.25, 0.5, 0.75, 0.8, 0.85, 0.9, 0.95, 0.99};

        for (double factor : factores) {
            ResultadoFactorCargaDetallado resultado = probarFactorCargaDetallado(factor, numElementos);
            System.out.printf("%-15.2f %-15.2f %-15.2f %-15d %-15d%n",
                    factor, resultado.tiempoInsercion, resultado.tiempoBusqueda,
                    resultado.colisiones, resultado.rehashes);
        }

        System.out.println("\nOBSERVACIONES:");
        System.out.println("• Factor < 0.5: Excelente rendimiento, pocas colisiones");
        System.out.println("• Factor 0.5-0.75: Buen rendimiento, colisiones moderadas");
        System.out.println("• Factor 0.75-0.9: Degradación notable, más colisiones");
        System.out.println("• Factor > 0.9: Rendimiento crítico, muchas colisiones");
    }

    private void pruebasDistribucionColisiones() {
        System.out.println("\n--- Análisis de Distribución de Colisiones ---");

        // Analizar cómo se distribuyen las colisiones
        int[] tamañosTabla = {100, 500, 1000, 2000};
        int numElementos = 750; // Factor de carga fijo en 0.75

        System.out.printf("%-15s %-15s %-15s %-15s %-15s%n",
                "Tamaño Tabla", "Elementos", "Factor Carga", "Colisiones", "Distribución");
        System.out.println("-".repeat(75));

        for (int tamaño : tamañosTabla) {
            ResultadoDistribucionColisiones resultado = analizarDistribucionColisiones(tamaño, numElementos);
            double factorCarga = (double) numElementos / tamaño;

            System.out.printf("%-15d %-15d %-15.2f %-15d %-15s%n",
                    tamaño, numElementos, factorCarga,
                    resultado.totalColisiones, resultado.distribucion);
        }

        System.out.println("\nCONCLUSIONES:");
        System.out.println("• Tablas más grandes tienen mejor distribución");
        System.out.println("• Factor de carga constante mantiene rendimiento similar");
        System.out.println("• Función hash uniforme reduce clustering");
    }

    private void pruebasRehashing() {
        System.out.println("\n--- Análisis de Impacto del Rehashing ---");

        // Simular crecimiento dinámico con rehashing
        SistemaInventario sistema = new SistemaInventario(100);
        sistema.insertarCategoria("TestRehashing");

        Random random = new Random(42);
        List<Long> tiemposInsercion = new ArrayList<>();
        List<Integer> rehashingDetectado = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            long inicio = System.nanoTime();
            Producto producto = new Producto("REHASH_" + i, "Producto " + i,
                    "TestRehashing", random.nextDouble() * 100,
                    random.nextInt(50));
            sistema.insertarProducto(producto);
            long fin = System.nanoTime();

            long tiempoMicrosegundos = (fin - inicio) / 1_000;
            tiemposInsercion.add(tiempoMicrosegundos);

            // Detectar picos de tiempo (posibles rehashes)
            if (i > 0 && tiemposInsercion.get(i) > tiemposInsercion.get(i-1) * 3) {
                System.out.printf("Posible rehashing detectado en inserción %d: %d μs%n",
                        i, tiemposInsercion.get(i));
                rehashingDetectado.add(i);
            }
        }

        // Estadísticas de rehashing
        long tiempoPromedio = tiemposInsercion.stream().mapToLong(Long::longValue).sum() / tiemposInsercion.size();
        long tiempoMaximo = tiemposInsercion.stream().mapToLong(Long::longValue).max().orElse(0);

        System.out.printf("Tiempo promedio inserción: %d μs%n", tiempoPromedio);
        System.out.printf("Tiempo máximo inserción: %d μs%n", tiempoMaximo);
        System.out.printf("Ratio máximo/promedio: %.2f%n", (double) tiempoMaximo / tiempoPromedio);
        System.out.printf("Rehashes detectados: %d%n", rehashingDetectado.size());

        // Análisis de patrones de rehashing
        if (!rehashingDetectado.isEmpty()) {
            System.out.println("\nPATRONES DE REHASHING:");
            for (int i = 0; i < rehashingDetectado.size() - 1; i++) {
                int intervalo = rehashingDetectado.get(i + 1) - rehashingDetectado.get(i);
                System.out.printf("• Intervalo entre rehashes: %d elementos%n", intervalo);
            }
        }
    }

    private ResultadoFactorCargaDetallado probarFactorCargaDetallado(double factorCarga, int numElementos) {
        int capacidadTabla = (int) Math.ceil(numElementos / factorCarga);

        // Simular hash table personalizada para análisis detallado
        HashTableSimulada hashTable = new HashTableSimulada(capacidadTabla);

        Random random = new Random(42);
        List<String> codigos = new ArrayList<>();

        // Generar códigos únicos
        for (int i = 0; i < numElementos; i++) {
            codigos.add("DETAIL_" + String.format("%06d", i));
        }

        // Medir inserción
        long inicioInsercion = System.nanoTime();
        for (String codigo : codigos) {
            hashTable.insertar(codigo, "Producto " + codigo);
        }
        long finInsercion = System.nanoTime();

        // Medir búsqueda
        long inicioBusqueda = System.nanoTime();
        for (String codigo : codigos) {
            hashTable.buscar(codigo);
        }
        long finBusqueda = System.nanoTime();

        ResultadoFactorCargaDetallado resultado = new ResultadoFactorCargaDetallado();
        resultado.tiempoInsercion = (finInsercion - inicioInsercion) / 1_000_000.0;
        resultado.tiempoBusqueda = (finBusqueda - inicioBusqueda) / 1_000_000.0;
        resultado.colisiones = hashTable.getColisiones();
        resultado.rehashes = hashTable.getRehashes();
        resultado.factorCarga = factorCarga;

        return resultado;
    }

    private ResultadoDistribucionColisiones analizarDistribucionColisiones(int tamañoTabla, int numElementos) {
        HashTableSimulada hashTable = new HashTableSimulada(tamañoTabla);
        Random random = new Random(42);

        // Insertar elementos y analizar distribución
        for (int i = 0; i < numElementos; i++) {
            String codigo = "DIST_" + String.format("%06d", i);
            hashTable.insertar(codigo, "Producto " + codigo);
        }

        // Analizar distribución de colisiones
        int[] distribucionBuckets = hashTable.getDistribucionBuckets();
        int bucketsVacios = 0;
        int bucketsConColisiones = 0;
        int maxElementosEnBucket = 0;

        for (int elementos : distribucionBuckets) {
            if (elementos == 0) {
                bucketsVacios++;
            } else if (elementos > 1) {
                bucketsConColisiones++;
            }
            maxElementosEnBucket = Math.max(maxElementosEnBucket, elementos);
        }

        ResultadoDistribucionColisiones resultado = new ResultadoDistribucionColisiones();
        resultado.totalColisiones = hashTable.getColisiones();
        resultado.bucketsVacios = bucketsVacios;
        resultado.bucketsConColisiones = bucketsConColisiones;
        resultado.maxElementosEnBucket = maxElementosEnBucket;

        // Clasificar distribución
        double porcentajeVacios = (double) bucketsVacios / tamañoTabla * 100;
        if (porcentajeVacios < 20) {
            resultado.distribucion = "Excelente";
        } else if (porcentajeVacios < 40) {
            resultado.distribucion = "Buena";
        } else if (porcentajeVacios < 60) {
            resultado.distribucion = "Regular";
        } else {
            resultado.distribucion = "Mala";
        }

        return resultado;
    }
}

// ============================================================================
// CLASES AUXILIARES PARA ANÁLISIS DETALLADO
// ============================================================================

class ResultadoFactorCargaDetallado {
    public double tiempoInsercion;
    public double tiempoBusqueda;
    public int colisiones;
    public int rehashes;
    public double factorCarga;

    @Override
    public String toString() {
        return String.format("Factor: %.2f, Inserción: %.2fms, Búsqueda: %.2fms, Colisiones: %d, Rehashes: %d",
                factorCarga, tiempoInsercion, tiempoBusqueda, colisiones, rehashes);
    }
}

class ResultadoDistribucionColisiones {
    public int totalColisiones;
    public int bucketsVacios;
    public int bucketsConColisiones;
    public int maxElementosEnBucket;
    public String distribucion;

    @Override
    public String toString() {
        return String.format("Colisiones: %d, Vacíos: %d, Con colisiones: %d, Max/bucket: %d, Distribución: %s",
                totalColisiones, bucketsVacios, bucketsConColisiones, maxElementosEnBucket, distribucion);
    }
}

// ============================================================================
// HASH TABLE SIMULADA PARA ANÁLISIS DETALLADO
// ============================================================================

class HashTableSimulada {
    private List<List<EntradaHash>> tabla;
    private int capacidad;
    private int tamaño;
    private int colisiones;
    private int rehashes;

    public HashTableSimulada(int capacidadInicial) {
        this.capacidad = capacidadInicial;
        this.tamaño = 0;
        this.colisiones = 0;
        this.rehashes = 0;
        this.tabla = new ArrayList<>(capacidad);

        for (int i = 0; i < capacidad; i++) {
            tabla.add(new ArrayList<>());
        }
    }

    public void insertar(String clave, String valor) {
        // Verificar si necesita rehashing
        if ((double) tamaño / capacidad > 0.75) {
            rehash();
        }

        int indice = hash(clave);
        List<EntradaHash> bucket = tabla.get(indice);

        // Verificar si ya existe
        for (EntradaHash entrada : bucket) {
            if (entrada.clave.equals(clave)) {
                entrada.valor = valor;
                return;
            }
        }

        // Insertar nueva entrada
        if (!bucket.isEmpty()) {
            colisiones++;
        }

        bucket.add(new EntradaHash(clave, valor));
        tamaño++;
    }

    public String buscar(String clave) {
        int indice = hash(clave);
        List<EntradaHash> bucket = tabla.get(indice);

        for (EntradaHash entrada : bucket) {
            if (entrada.clave.equals(clave)) {
                return entrada.valor;
            }
        }

        return null;
    }

    private void rehash() {
        List<List<EntradaHash>> tablaAnterior = tabla;
        int capacidadAnterior = capacidad;

        // Doblar capacidad
        capacidad *= 2;
        rehashes++;
        tabla = new ArrayList<>(capacidad);

        for (int i = 0; i < capacidad; i++) {
            tabla.add(new ArrayList<>());
        }

        // Reinsertar todos los elementos
        int tamañoAnterior = tamaño;
        tamaño = 0;
        colisiones = 0;

        for (List<EntradaHash> bucket : tablaAnterior) {
            for (EntradaHash entrada : bucket) {
                insertar(entrada.clave, entrada.valor);
            }
        }
    }

    private int hash(String clave) {
        // Hash function simple pero efectiva
        int hash = clave.hashCode();
        return Math.abs(hash % capacidad);
    }

    public int getColisiones() {
        return colisiones;
    }

    public int getRehashes() {
        return rehashes;
    }

    public int[] getDistribucionBuckets() {
        int[] distribucion = new int[capacidad];
        for (int i = 0; i < capacidad; i++) {
            distribucion[i] = tabla.get(i).size();
        }
        return distribucion;
    }

    public double getFactorCarga() {
        return (double) tamaño / capacidad;
    }

    // Clase interna para entradas de hash
    private static class EntradaHash {
        String clave;
        String valor;

        public EntradaHash(String clave, String valor) {
            this.clave = clave;
            this.valor = valor;
        }
    }
}

// ============================================================================
// GENERADOR DE REPORTES DETALLADOS
// ============================================================================

class GeneradorReportesDetallados {

    public void generarReporteFactorCarga(ResultadoFactorCargaDetallado[] resultados) {
        System.out.println("\n==== REPORTE DETALLADO DE FACTOR DE CARGA ====");

        // Encontrar puntos críticos
        double factorOptimoRendimiento = encontrarFactorOptimo(resultados);
        double factorLimiteAceptable = encontrarFactorLimite(resultados);

        System.out.println("\nRESUMEN EJECUTIVO:");
        System.out.printf("• Factor de carga óptimo: %.2f%n", factorOptimoRendimiento);
        System.out.printf("• Factor de carga límite aceptable: %.2f%n", factorLimiteAceptable);

        // Análisis de tendencias
        System.out.println("\nANÁLISIS DE TENDENCIAS:");
        analizarTendencias(resultados);

        // Recomendaciones
        System.out.println("\nRECOMENDACIONES:");
        System.out.println("• Mantener factor de carga < 0.75 para rendimiento óptimo");
        System.out.println("• Implementar rehashing automático cuando se alcance 0.75");
        System.out.println("• Considerar función hash más uniforme si hay muchas colisiones");
        System.out.println("• Monitorear distribución de elementos en buckets");
    }

    private double encontrarFactorOptimo(ResultadoFactorCargaDetallado[] resultados) {
        double mejorFactor = 0.0;
        double mejorTiempo = Double.MAX_VALUE;

        for (ResultadoFactorCargaDetallado resultado : resultados) {
            double tiempoTotal = resultado.tiempoInsercion + resultado.tiempoBusqueda;
            if (tiempoTotal < mejorTiempo) {
                mejorTiempo = tiempoTotal;
                mejorFactor = resultado.factorCarga;
            }
        }

        return mejorFactor;
    }

    private double encontrarFactorLimite(ResultadoFactorCargaDetallado[] resultados) {
        double tiempoBaselinea = resultados[0].tiempoInsercion + resultados[0].tiempoBusqueda;
        double umbralDegradacion = tiempoBaselinea * 2.0; // 100% más lento

        for (ResultadoFactorCargaDetallado resultado : resultados) {
            double tiempoTotal = resultado.tiempoInsercion + resultado.tiempoBusqueda;
            if (tiempoTotal > umbralDegradacion) {
                return resultado.factorCarga;
            }
        }

        return 0.99; // Si no se encuentra límite, usar 99%
    }

    private void analizarTendencias(ResultadoFactorCargaDetallado[] resultados) {
        System.out.println("• Tiempo de inserción tiende a aumentar exponencialmente");
        System.out.println("• Tiempo de búsqueda se mantiene más estable");
        System.out.println("• Colisiones aumentan cuadráticamente con el factor de carga");
        System.out.println("• Rehashing ocurre con menor frecuencia en factores bajos");
    }
}