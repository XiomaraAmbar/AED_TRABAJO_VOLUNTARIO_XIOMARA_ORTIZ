package ReportesAnalisis;

class DatoRendimiento {
    public int tamaño;
    public String operacion;
    public double tiempo;
    public double memoria;
    public double throughput;
    public String complejidadTeorica;

    @Override
    public String toString() {
        return String.format("DatoRendimiento{tamaño=%d, operacion='%s', tiempo=%.2f, memoria=%.2f, throughput=%.2f, complejidad='%s'}",
                tamaño, operacion, tiempo, memoria, throughput, complejidadTeorica);
    }
}

class EstadisticasDescriptivas {
    public double media;
    public double desviacionEstandar;
    public double minimo;
    public double maximo;
    public double mediana;
    public double coeficienteVariacion;
}
