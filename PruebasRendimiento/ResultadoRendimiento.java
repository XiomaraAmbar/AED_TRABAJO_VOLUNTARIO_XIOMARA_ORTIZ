package PruebasRendimiento;

class ResultadoRendimiento {
    public double tiempoInsercion;
    public double tiempoBusqueda;
    public double tiempoEliminacion;
    public double memoriaUtilizada;

    @Override
    public String toString() {
        return String.format("Inserción: %.2fms, Búsqueda: %.2fms, Eliminación: %.2fms, Memoria: %.2fMB",
                tiempoInsercion, tiempoBusqueda, tiempoEliminacion, memoriaUtilizada);
    }
}