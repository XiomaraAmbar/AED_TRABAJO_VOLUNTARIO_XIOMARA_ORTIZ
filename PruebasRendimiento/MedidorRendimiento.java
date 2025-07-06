package PruebasRendimiento;

import Inventario.*;
import java.util.*;

class MedidorRendimiento {
    private SistemaInventario sistema;
    private int tamaño;
    private List<String> codigosProductos;
    private List<String> categorias;

    public MedidorRendimiento(int tamaño) {
        this.tamaño = tamaño;
        this.sistema = new SistemaInventario(tamaño * 2); // Capacidad extra
        this.codigosProductos = new ArrayList<>();
        this.categorias = Arrays.asList("Electrónicos", "Ropa", "Hogar", "Deportes", "Libros");

        // Preparar categorías
        for (String categoria : categorias) {
            sistema.insertarCategoria(categoria);
        }
    }

    public ResultadoRendimiento medirOperacionesBasicas() {
        ResultadoRendimiento resultado = new ResultadoRendimiento();
        Random random = new Random(42); // Seed fijo para reproducibilidad

        // Generar datos de prueba
        generarDatosPrueba(random);

        // Medir inserción
        long inicioInsercion = System.nanoTime();
        for (int i = 0; i < tamaño; i++) {
            String codigo = codigosProductos.get(i);
            String categoria = categorias.get(random.nextInt(categorias.size()));
            Producto producto = new Producto(codigo, "Producto " + i, categoria,
                    random.nextDouble() * 1000, random.nextInt(100));
            sistema.insertarProducto(producto);
        }
        long finInsercion = System.nanoTime();
        resultado.tiempoInsercion = (finInsercion - inicioInsercion) / 1_000_000.0;

        // Medir búsqueda
        long inicioBusqueda = System.nanoTime();
        for (int i = 0; i < tamaño; i++) {
            sistema.buscarProducto(codigosProductos.get(i));
        }
        long finBusqueda = System.nanoTime();
        resultado.tiempoBusqueda = (finBusqueda - inicioBusqueda) / 1_000_000.0;

        // Medir eliminación (solo la mitad para no vaciar el sistema)
        long inicioEliminacion = System.nanoTime();
        for (int i = 0; i < tamaño / 2; i++) {
            sistema.eliminarProducto(codigosProductos.get(i));
        }
        long finEliminacion = System.nanoTime();
        resultado.tiempoEliminacion = (finEliminacion - inicioEliminacion) / 1_000_000.0;

        // Medir uso de memoria
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // Forzar garbage collection
        long memoriaUsada = runtime.totalMemory() - runtime.freeMemory();
        resultado.memoriaUtilizada = memoriaUsada / (1024.0 * 1024.0); // MB

        return resultado;
    }

    private void generarDatosPrueba(Random random) {
        Set<String> codigosUnicos = new HashSet<>();
        while (codigosUnicos.size() < tamaño) {
            codigosUnicos.add("PROD_" + String.format("%06d", random.nextInt(999999)));
        }
        codigosProductos.addAll(codigosUnicos);
    }
}