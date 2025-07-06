package modelo;

import Excepciones.*;
import AVLTree.AVLTree;
import java.util.ArrayList;
import Hash.HashSondeoLineal;
import Hash.HashEncadenamiento;
import LinkedList.MensajeException;
import MatrizDispersa.MatrizDispersa;

public class SistemaInventario {
    private final int ORDEN_BTREEPLUS = 5;

    private HashSondeoLineal<String> hashCategorias;
    private AVLTree<CategoriaData> arbolCategoriasProductos;
    private MatrizDispersa<Integer> matrizStocks;
    private final int COLUMNA_STOCK = 0;

    // AHORA: HashEncadenamiento para almacenar objetos ProductoFilaMapping
    private HashEncadenamiento<ProductoFilaMapeo> mapeoCodigoProductoFila;
    private int proximaFilaDisponibleMatriz = 0;

    // También necesitamos un HashEncadenamiento para almacenar los objetos Producto completos
    // Esto es vital para 'buscarProducto' globalmente y obtener todos sus datos.
    private HashEncadenamiento<Producto> hashProductosGlobal;


    // Constructor
    public SistemaInventario(int numMaxProductosMatriz) {
        int capacidadHashCategoriasCalculada = Math.max(10, numMaxProductosMatriz / 10); // Mínimo 10, o 10% de productos

        // Para HashEncadenamiento (productos globales y mapeo producto-fila):
        // Si el máximo de productos es 2000, entonces el tamaño de la tabla HashEncadenamiento será 4000.
        int capacidadHashEncadenamientoCalculada = numMaxProductosMatriz * 2; // El doble del máximo de productos

        this.hashCategorias = new HashSondeoLineal<>(capacidadHashCategoriasCalculada);
        this.arbolCategoriasProductos = new AVLTree<CategoriaData>(); // El AVL no tiene tamaño fijo por constructor
        this.hashProductosGlobal = new HashEncadenamiento<>(capacidadHashEncadenamientoCalculada);
        this.matrizStocks = new MatrizDispersa<>(numMaxProductosMatriz, 1, 0);
        this.mapeoCodigoProductoFila = new HashEncadenamiento<>(capacidadHashEncadenamientoCalculada);

    }

    public int getOrdenBTreePlus() {
        return ORDEN_BTREEPLUS;
    }

    /* ******************************************************************
     * GESTIÓN DE CATEGORÍAS (sin cambios relevantes en esta sección)
     ****************************************************************** */

    public boolean insertarCategoria(String nombreCategoria) {
        if (nombreCategoria == null || nombreCategoria.trim().isEmpty()) {
            System.out.println("El nombre de la categoría no puede estar vacío.");
            return false;
        }

        int indiceHash = hashCategorias.principal(nombreCategoria);

        CategoriaData busquedaCatData = null;
        try {
            busquedaCatData = arbolCategoriasProductos.search(new CategoriaData(indiceHash, "", ORDEN_BTREEPLUS));
            if (busquedaCatData != null) {
                if (busquedaCatData.nombreCategoriaOriginal.equalsIgnoreCase(nombreCategoria)) {
                    System.out.println("La categoría '" + nombreCategoria + "' ya existe.");
                    return false;
                } else {
                    System.err.println("¡Colisión de hash! La categoría '" + nombreCategoria + "' tiene el mismo hash (" + indiceHash + ") que '" + busquedaCatData.nombreCategoriaOriginal + "'. Considera ajustar la función hash o aumentar la capacidad de la tabla hash de categorías.");
                    return false;
                }
            }
        } catch (ItemNotFound ignored) {
            // No encontrado, se puede insertar.
        } catch (Exception e) {
            System.err.println("Error al buscar categoría en AVL: " + e.getMessage());
            return false;
        }

        if (!hashCategorias.insertar(nombreCategoria)) {
            System.err.println("Error al insertar categoría en la tabla hash de categorías: " + nombreCategoria);
            return false;
        }

        CategoriaData nuevaCatData = new CategoriaData(indiceHash, nombreCategoria, ORDEN_BTREEPLUS);
        try {
            arbolCategoriasProductos.insert(nuevaCatData);
            System.out.println("Categoría '" + nombreCategoria + "' insertada con hash: " + indiceHash);
            return true;
        } catch (ItemDuplicated e) {
            System.err.println("Error inesperado: Categoría duplicada en AVL: " + e.getMessage());
            hashCategorias.eliminar(nombreCategoria);
            return false;
        }
    }

    public CategoriaData buscarCategoria(String nombreCategoria) {
        if (nombreCategoria == null || nombreCategoria.trim().isEmpty()) {
            return null;
        }
        int indiceHash = hashCategorias.principal(nombreCategoria);
        try {
            CategoriaData encontrado = arbolCategoriasProductos.search(new CategoriaData(indiceHash, "", ORDEN_BTREEPLUS));
            if (encontrado != null && encontrado.nombreCategoriaOriginal.equalsIgnoreCase(nombreCategoria)) {
                return encontrado;
            }
        } catch (ItemNotFound e) {
            // Categoría no encontrada
        }
        return null;
    }

    public boolean eliminarCategoria(String nombreCategoria) {
        if (nombreCategoria == null || nombreCategoria.trim().isEmpty()) {
            return false;
        }

        CategoriaData catDataAEliminar = buscarCategoria(nombreCategoria);
        if (catDataAEliminar == null) {
            System.out.println("La categoría '" + nombreCategoria + "' no existe.");
            return false;
        }

        boolean eliminadoHash = hashCategorias.eliminar(nombreCategoria);

        try {
            arbolCategoriasProductos.delete(catDataAEliminar);
            System.out.println("Categoría '" + nombreCategoria + "' eliminada del AVL.");

            ArrayList<Producto> productosEliminados = catDataAEliminar.productosPorCodigo.getTotalClaves();
            for (Producto p : productosEliminados) {
                hashProductosGlobal.eliminarClave(p);
                // Usamos la nueva clase ProductoFilaMapeo para la eliminación
                mapeoCodigoProductoFila.eliminarClave(new ProductoFilaMapeo(p.getCodigoProducto()));
                Integer filaProducto = getFilaProductoMatriz(p.getCodigoProducto()); // Esto aún usa el valor anterior o null
                if (filaProducto != null) {
                    matrizStocks.eliminar(filaProducto, COLUMNA_STOCK);
                }
            }
            return eliminadoHash;
        } catch (ExceptionIsEmpty e) {
            System.err.println("Error: El AVL de categorías está vacío. " + e.getMessage());
            return false;
        }
    }

    /* ******************************************************************
     * GESTIÓN DE PRODUCTOS (AQUÍ ESTÁN LOS CAMBIOS IMPORTANTES)
     ****************************************************************** */

    public boolean insertarProducto(Producto producto) {
        if (producto == null) {
            System.out.println("El producto no puede ser nulo.");
            return false;
        }

        CategoriaData categoriaDelProducto = buscarCategoria(producto.getCategoria());
        if (categoriaDelProducto == null) {
            System.out.println("La categoría '" + producto.getCategoria() + "' no existe. Por favor, insértela primero.");
            return false;
        }

        // Primero, verificar si el producto ya existe globalmente
        if (buscarProducto(producto.getCodigoProducto()) != null) {
            System.out.println("Error: El producto con código '" + producto.getCodigoProducto() + "' ya existe.");
            return false;
        }

        // 1. Insertar en el BTreePlus de su categoría
        categoriaDelProducto.productosPorCodigo.insert(producto);

        // 2. Insertar en el HashGlobal de productos (para búsqueda rápida por código en todo el sistema)
        try {
            if (!hashProductosGlobal.insertarClave(producto)) {
                // Si la inserción global falla, revertir inserción en BTreePlus
                categoriaDelProducto.productosPorCodigo.remove(producto);
                System.err.println("Error: No se pudo insertar el producto globalmente (posible duplicado, o problema de rehashing).");
                return false;
            }
        } catch (MensajeException e) {
            System.err.println("Error al insertar producto en hash global: " + e.getMessage());
            try { categoriaDelProducto.productosPorCodigo.remove(producto); } catch (Exception ignored) {}
            return false;
        }


        // 3. Actualizar Matriz Dispersa de Stocks
        // Asignar una fila al producto si es nuevo
        Integer filaAsignada = getFilaProductoMatriz(producto.getCodigoProducto()); // Este sigue siendo válido
        if (filaAsignada == null) {
            filaAsignada = proximaFilaDisponibleMatriz++;
            // AHORA: Insertamos un objeto ProductoFilaMapping en el HashEncadenamiento
            try {
                mapeoCodigoProductoFila.insertarClave(new ProductoFilaMapeo(producto.getCodigoProducto(), filaAsignada));
            } catch (MensajeException e) {
                System.err.println("Error al mapear producto a fila en matriz: " + e.getMessage());
                // Considerar si es un error fatal. Por ahora, el producto se insertó pero su stock no se mapeó.
            }
        }
        matrizStocks.establecer(filaAsignada, COLUMNA_STOCK, producto.getStockDisponible());

        System.out.println("Producto '" + producto.getNombre() + "' insertado correctamente.");
        return true;
    }

    public Producto buscarProducto(String codigoProducto) {
        if (codigoProducto == null || codigoProducto.trim().isEmpty()) {
            return null;
        }
        try {
            // Para buscar en hashProductosGlobal, necesitas un objeto Producto que solo contenga el código
            // porque el método 'equals' de Producto solo compara por código.
            Producto productoDummy = new Producto(codigoProducto, null, null, 0, 0);
            return hashProductosGlobal.obtenerPorClave(productoDummy);
        } catch (MensajeException e) {
            System.err.println("Error al buscar producto en hash global: " + e.getMessage());
            return null;
        }
    }

    public boolean eliminarProducto(String codigoProducto) {
        if (codigoProducto == null || codigoProducto.trim().isEmpty()) {
            System.out.println("El código de producto no puede ser nulo.");
            return false;
        }

        Producto productoAEliminar = buscarProducto(codigoProducto);
        if (productoAEliminar == null) {
            System.out.println("Producto con código '" + codigoProducto + "' no encontrado.");
            return false;
        }

        // 1. Eliminar del BTreePlus de su categoría
        CategoriaData categoriaDelProducto = buscarCategoria(productoAEliminar.getCategoria());
        if (categoriaDelProducto != null) {
            try {
                categoriaDelProducto.productosPorCodigo.remove(productoAEliminar);
            } catch (Exception e) {
                System.err.println("Error al eliminar producto del BTreePlus de categoría: " + e.getMessage());
                // Podría ocurrir si el producto ya no estaba en el BTreePlus por alguna razón
            }
        }

        // 2. Eliminar del HashGlobal de productos
        hashProductosGlobal.eliminarClave(productoAEliminar);

        // 3. Eliminar de la Matriz Dispersa de Stocks
        Integer filaProducto = getFilaProductoMatriz(codigoProducto);
        if (filaProducto != null) {
            matrizStocks.eliminar(filaProducto, COLUMNA_STOCK); // Establece el stock a 0 (valor por defecto)
            // AHORA: Eliminar el objeto ProductoFilaMapping del HashEncadenamiento
            mapeoCodigoProductoFila.eliminarClave(new ProductoFilaMapeo(codigoProducto));
        }

        System.out.println("Producto '" + codigoProducto + "' eliminado correctamente.");
        return true;
    }

    public boolean actualizarStockProducto(String codigoProducto, int nuevoStock) {
        if (codigoProducto == null || codigoProducto.trim().isEmpty() || nuevoStock < 0) {
            System.out.println("Datos de stock inválidos.");
            return false;
        }

        Producto productoAActualizar = buscarProducto(codigoProducto);
        if (productoAActualizar == null) {
            System.out.println("Producto con código '" + codigoProducto + "' no encontrado.");
            return false;
        }

        productoAActualizar.setStockDisponible(nuevoStock);

        Integer filaProducto = getFilaProductoMatriz(codigoProducto);
        if (filaProducto != null) {
            matrizStocks.establecer(filaProducto, COLUMNA_STOCK, nuevoStock);
            System.out.println("Stock de '" + productoAActualizar.getNombre() + "' actualizado a " + nuevoStock);
            return true;
        } else {
            System.err.println("Error: No se encontró la fila para el producto en la matriz de stock.");
            return false;
        }
    }

    // OBTENER LA FILA DE UN PRODUCTO EN LA MATRIZ DE STOCK (CORREGIDO)
    private Integer getFilaProductoMatriz(String codigoProducto) {
        try {
            // Creamos un objeto ProductoFilaMapping para buscar por el código de producto
            ProductoFilaMapeo keyToSearch = new ProductoFilaMapeo(codigoProducto);
            // Usamos obtenerPorClave para recuperar el objeto ProductoFilaMapping completo
            ProductoFilaMapeo foundMapping = mapeoCodigoProductoFila.obtenerPorClave(keyToSearch);

            if (foundMapping != null) {
                return foundMapping.getFilaMatriz();
            }
            return null; // No se encontró el mapeo para este código de producto
        } catch (MensajeException e) {
            System.err.println("Error al buscar fila de producto en mapeo: " + e.getMessage());
            return null;
        }
    }

    /* ******************************************************************
     * MÉTODOS DE REPORTE/VISUALIZACIÓN
     ****************************************************************** */

    public void mostrarCategorias() throws MensajeException {
        System.out.println("\n--- Resumen de Categorías ---");
        try {
            arbolCategoriasProductos.InOrden();
        } catch (Excepciones.MensajeException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("-----------------------------\n");
    }

    public void mostrarProductosPorCategoria(String nombreCategoria) {
        System.out.println("\n--- Productos en Categoría: " + nombreCategoria + " ---");
        CategoriaData categoria = buscarCategoria(nombreCategoria);
        if (categoria != null) {
            ArrayList<Producto> productos = categoria.productosPorCodigo.getTotalClaves();
            if (productos.isEmpty()) {
                System.out.println("No hay productos en esta categoría.");
            } else {
                for (Producto p : productos) {
                    System.out.println(p);
                }
            }
        } else {
            System.out.println("Categoría no encontrada.");
        }
        System.out.println("-----------------------------------------\n");
    }

    public void mostrarStocksComoMatriz() {
        System.out.println("\n--- Resumen de Stocks (Matriz Dispersa) ---");
        matrizStocks.mostrarMatriz();
        System.out.println("-------------------------------------------\n");
    }

    public void mostrarProductosYStocks() {
        System.out.println("\n--- Productos y Stocks ---");
        ArrayList<Producto> todosLosProductos = new ArrayList<>(); // Initialize to avoid NullPointerException
        try {
            todosLosProductos = hashProductosGlobal.obtenerTodosLosElementos();
        } catch (MensajeException e) {
            System.err.println("Error al obtener todos los productos para mostrar stocks: " + e.getMessage());
            return;
        }

        if (todosLosProductos.isEmpty()) {
            System.out.println("No hay productos en el inventario.");
            return;
        }

        System.out.printf("%-20s %-30s %-10s%n", "Código Producto", "Nombre Producto", "Stock");
        System.out.println("------------------------------------------------------------------");

        for (Producto p : todosLosProductos) {
            Integer fila = getFilaProductoMatriz(p.getCodigoProducto());
            int stock = 0;
            if (fila != null) {
                stock = matrizStocks.obtener(fila, COLUMNA_STOCK);
            }
            System.out.printf("%-20s %-30s %-10d%n", p.getCodigoProducto(), p.getNombre(), stock);
        }
        System.out.println("------------------------------------------------------------------");
    }
}