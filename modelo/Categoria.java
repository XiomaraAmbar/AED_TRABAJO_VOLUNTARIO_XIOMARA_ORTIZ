package modelo;

import BTreePlus.BTreePlus;

public class Categoria implements Comparable<Categoria> {
    private String nombreCategoria;
    // El índice hasheado de la categoría lo manejaremos en el SistemaInventario
    // private int indiceHashCategoria; // No es necesario que la Categoria lo sepa internamente

    // Cada categoría tendrá su propio BTreePlus para almacenar sus productos
    // El BTreePlus almacenará objetos Producto, ordenados por su código de producto.
    private BTreePlus<Producto> productosPorCodigo;

    // Constructor
    public Categoria(String nombreCategoria, int ordenBTreePlus) {
        this.nombreCategoria = nombreCategoria;
        this.productosPorCodigo = new BTreePlus<Producto>(ordenBTreePlus);
    }

    // Getters y Setters
    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public BTreePlus<Producto> getProductosPorCodigo() {
        return productosPorCodigo;
    }

    // Implementación de compareTo para el estructuras.AVLTree.
    // Usaremos el nombre de la categoría para comparar.
    // Aunque el AVL almacenará un *índice* de categoría,
    // el nodo AVL en sí necesitará saber cómo comparar categorías.
    // O mejor aún, el AVL puede almacenar directamente el índice entero y no el objeto Categoría,
    // y el mapeo de índice a Categoria (y su BTreePlus) se hace en el SistemaInventario.
    // Reevaluando: es mejor que el AVL almacene el índice entero (int)
    // y que el SistemaInventario se encargue de mapear ese índice a la Categoria.
    // Pero si queremos que el AVL maneje *objetos* Categoria, entonces Categoria debe ser Comparable.
    // Para simplificar la gestión en el AVL, es más limpio que el AVL almacene los índices INT.
    // Por lo tanto, esta implementación de Comparable sería si el AVL almacenara objetos Categoria.
    // Para tu diseño propuesto (AVL de INTs, donde INT es el hash de la categoría), esta clase Categoria
    // NO NECESITA ser Comparable, ya que no se almacenará directamente en el AVL.
    // El AVL almacenará el hash (Integer) y los datos asociados (BTreePlus) por separado.
    // Sin embargo, si decides que un nodo AVL almacene un objeto 'CategoriaMetadata' que contenga el hash y el BTreePlus,
    // esa clase 'CategoriaMetadata' sí necesitaría ser Comparable por el hash.


    // Si el AVL almacenara objetos Categoria, entonces sería así:
    @Override
    public int compareTo(Categoria otra) {
        if (otra == null) {
            return 1;
        }
        return this.nombreCategoria.compareToIgnoreCase(otra.nombreCategoria);
    }

    @Override
    public String toString() {
        return "Categoría: " + nombreCategoria + " (Productos: " + productosPorCodigo.getTotalClaves().size() + ")";
    }
}
