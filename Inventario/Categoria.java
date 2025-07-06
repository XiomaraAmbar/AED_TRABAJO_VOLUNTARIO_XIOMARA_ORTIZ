package Inventario;

import BTreePlus.BTreePlus;

public class Categoria implements Comparable<Categoria> {
    private String nombreCategoria;
    private BTreePlus<Producto> productosPorCodigo;

    //Constructor
    public Categoria(String nombreCategoria, int ordenBTreePlus) {
        this.nombreCategoria = nombreCategoria;
        this.productosPorCodigo = new BTreePlus<Producto>(ordenBTreePlus);
    }

    //Getters y Setters
    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public BTreePlus<Producto> getProductosPorCodigo() {
        return productosPorCodigo;
    }

    //Si el AVL almacenara objetos Categoria
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
