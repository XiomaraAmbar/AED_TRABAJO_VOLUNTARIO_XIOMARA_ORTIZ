package PruebasRendimiento;

import Inventario.*;
import java.util.*;

class AnalisisFactorCarga {

    public void ejecutarAnalisis() {
        System.out.println("Analizando impacto del factor de carga en Hash Tables...");
        System.out.printf("%-15s %-15s %-15s %-15s%n",
                "Factor Carga", "Tiempo Ins(ms)", "Tiempo Bus(ms)", "Colisiones");
        System.out.println("------------------------------------------------------------");

        // Probar diferentes factores de carga
        double[] factores = {0.25, 0.5, 0.75, 0.9, 0.95, 0.99};

        for (double factor : factores) {
            ResultadoFactorCarga resultado = probarFactorCarga(factor, 1000);
            System.out.printf("%-15.2f %-15.2f %-15.2f %-15d%n",
                    factor, resultado.tiempoInsercion,
                    resultado.tiempoBusqueda, resultado.colisiones);
        }

        System.out.println("\nCONCLUSIONES DEL FACTOR DE CARGA:");
        System.out.println("- Factor < 0.75: Rendimiento óptimo");
        System.out.println("- Factor > 0.75: Degradación notable");
        System.out.println("- Factor > 0.9: Rendimiento crítico");
    }

    private ResultadoFactorCarga probarFactorCarga(double factorCarga, int numElementos) {
        int capacidadTabla = (int) (numElementos / factorCarga);

        // Simular hash table con factor de carga específico
        SistemaInventario sistema = new SistemaInventario(capacidadTabla);
        sistema.insertarCategoria("TestCategoria");

        Random random = new Random(42);
        List<String> codigos = new ArrayList<>();

        // Generar códigos únicos
        for (int i = 0; i < numElementos; i++) {
            codigos.add("PROD_FC_" + String.format("%06d", i));
        }

        // Medir inserción
        long inicioInsercion = System.nanoTime();
        for (String codigo : codigos) {
            Producto producto = new Producto(codigo, "Producto " + codigo,
                    "TestCategoria", random.nextDouble() * 100,
                    random.nextInt(50));
            sistema.insertarProducto(producto);
        }
        long finInsercion = System.nanoTime();

        // Medir búsqueda
        long inicioBusqueda = System.nanoTime();
        for (String codigo : codigos) {
            sistema.buscarProducto(codigo);
        }
        long finBusqueda = System.nanoTime();

        ResultadoFactorCarga resultado = new ResultadoFactorCarga();
        resultado.tiempoInsercion = (finInsercion - inicioInsercion) / 1_000_000.0;
        resultado.tiempoBusqueda = (finBusqueda - inicioBusqueda) / 1_000_000.0;
        resultado.colisiones = estimarColisiones(factorCarga, numElementos);

        return resultado;
    }

    private int estimarColisiones(double factorCarga, int numElementos) {
        // Estimación basada en el factor de carga
        // Más elementos = más probabilidad de colisiones
        return (int) (numElementos * factorCarga * factorCarga);
    }
}