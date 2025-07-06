package Inventario;

public class Producto implements Comparable<Producto> {
    private String codigoProducto;
    private String nombre;
    private String categoria;
    private double precio;
    private int stockDisponible;

    //Constructor
    public Producto(String codigoProducto, String nombre, String categoria,
                    double precio, int stockDisponible) {
        this.codigoProducto = codigoProducto;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stockDisponible = stockDisponible;
    }

    //Getters y Setters
    public String getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(String codigoProducto) { this.codigoProducto = codigoProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStockDisponible() { return stockDisponible; }
    public void setStockDisponible(int stockDisponible) { this.stockDisponible = stockDisponible; }

    //Implementación de compareTo para que funcione con AVL Tree
    //Compara por código de producto (clave principal)
    public int compareTo(Producto otro) {
        if (otro == null) {
            return 1;
        }
        return this.codigoProducto.compareToIgnoreCase(otro.codigoProducto);
    }

    public String toString() {
        return String.format("Producto{codigo='%s', nombre='%s', categoria='%s', precio=%.2f, stock=%d}",
                codigoProducto, nombre, categoria, precio, stockDisponible);
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Producto producto = (Producto) obj;
        return codigoProducto.equals(producto.codigoProducto);
    }

    public int hashCode() {
        return codigoProducto.hashCode();
    }
}