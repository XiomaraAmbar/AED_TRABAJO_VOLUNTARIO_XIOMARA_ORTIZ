package modelo;

public class ProductoFilaMapeo implements Comparable<ProductoFilaMapeo> {
    private String codigoProducto;
    private Integer filaMatriz;

    public ProductoFilaMapeo(String codigoProducto, Integer filaMatriz) {
        this.codigoProducto = codigoProducto;
        this.filaMatriz = filaMatriz;
    }

    // Constructor para BÚSQUEDA o ELIMINACIÓN
    public ProductoFilaMapeo(String codigoProducto) {
        this.codigoProducto = codigoProducto;
        this.filaMatriz = null; // La fila no es relevante para la comparación, solo el código
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public Integer getFilaMatriz() {
        return filaMatriz;
    }

    public void setFilaMatriz(Integer filaMatriz) {
        this.filaMatriz = filaMatriz;
    }

    @Override
    public int compareTo(ProductoFilaMapeo other) {
        return this.codigoProducto.compareTo(other.codigoProducto);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProductoFilaMapeo that = (ProductoFilaMapeo) obj;
        return codigoProducto.equals(that.codigoProducto);
    }

    @Override
    public int hashCode() {
        return codigoProducto.hashCode();
    }

    @Override
    public String toString() {
        return "{" + codigoProducto + " -> " + filaMatriz + "}";
    }
}