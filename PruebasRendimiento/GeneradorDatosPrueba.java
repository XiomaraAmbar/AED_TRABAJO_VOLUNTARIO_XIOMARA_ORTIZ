package PruebasRendimiento;

import Inventario.*;
import java.util.*;

class GeneradorDatosPrueba {
    private static final String[] CATEGORIAS = {
            "Electrónicos", "Ropa", "Hogar", "Deportes", "Libros",
            "Automóviles", "Salud", "Belleza", "Juguetes", "Música"
    };

    private static final String[] PREFIJOS_PRODUCTOS = {
            "ELEC", "ROPA", "HOGAR", "DEPORT", "LIBRO",
            "AUTO", "SALUD", "BELLE", "JUGUETE", "MUSICA"
    };

    private Random random;

    public GeneradorDatosPrueba(long seed) {
        this.random = new Random(seed);
    }

    public List<Producto> generarProductos(int cantidad) {
        List<Producto> productos = new ArrayList<>();
        Set<String> codigosUsados = new HashSet<>();

        for (int i = 0; i < cantidad; i++) {
            String codigo;
            do {
                String prefijo = PREFIJOS_PRODUCTOS[random.nextInt(PREFIJOS_PRODUCTOS.length)];
                codigo = prefijo + "_" + String.format("%06d", random.nextInt(999999));
            } while (codigosUsados.contains(codigo));

            codigosUsados.add(codigo);

            String categoria = CATEGORIAS[random.nextInt(CATEGORIAS.length)];
            String nombre = "Producto " + codigo;
            double precio = 10.0 + random.nextDouble() * 990.0; // 10-1000
            int stock = random.nextInt(100);

            productos.add(new Producto(codigo, nombre, categoria, precio, stock));
        }

        return productos;
    }

    public List<String> getCategorias() {
        return Arrays.asList(CATEGORIAS);
    }
}