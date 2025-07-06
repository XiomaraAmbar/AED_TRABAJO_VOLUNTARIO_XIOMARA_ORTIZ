package modelo;

import BTreePlus.BTreePlus;

// Clase auxiliar para el nodo del AVL
class CategoriaData implements Comparable<CategoriaData> {
    public int indiceHashCategoria;
    public String nombreCategoriaOriginal; // Para referencia fácil
    public BTreePlus<Producto> productosPorCodigo;

    public CategoriaData(int indiceHashCategoria, String nombreCategoria, int ordenBTreePlus) {
        this.indiceHashCategoria = indiceHashCategoria;
        this.nombreCategoriaOriginal = nombreCategoria;
        this.productosPorCodigo = new BTreePlus<Producto>(ordenBTreePlus);
    }

    @Override
    public int compareTo(CategoriaData otra) {
        return Integer.compare(this.indiceHashCategoria, otra.indiceHashCategoria);
    }

    @Override
    public String toString() {
        return "CatData[Hash:" + indiceHashCategoria + ", Nombre:'" + nombreCategoriaOriginal + "', Prods:" + productosPorCodigo.getTotalClaves().size() + "]";
    }
}
