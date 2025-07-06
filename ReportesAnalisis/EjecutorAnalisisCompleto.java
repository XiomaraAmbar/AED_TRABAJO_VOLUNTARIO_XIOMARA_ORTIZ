package ReportesAnalisis;

import PruebasRendimiento.*;

class EjecutorAnalisisCompleto {

    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE ANÁLISIS DE RENDIMIENTO COMPLETO ===");
        System.out.println("Iniciando análisis exhaustivo del Sistema de Inventario...\n");

        // 1. Ejecutar análisis de rendimiento básico
        AnalisisRendimiento analisisBasico = new AnalisisRendimiento();
        analisisBasico.ejecutarAnalisisCompleto();

        // 2. Ejecutar pruebas de estrés
        PruebasEstres pruebasEstres = new PruebasEstres();
        pruebasEstres.ejecutarPruebasEstres();

        // 3. Generar reportes detallados
        GeneradorReportes generadorReportes = new GeneradorReportes();
        generadorReportes.ejecutarAnalisisCompleto();

        // 4. Ejecutar análisis de factor de carga específico
        AnalisisFactorCargaDetallado factorCarga = new AnalisisFactorCargaDetallado();
        factorCarga.ejecutarAnalisisDetallado();

        System.out.println("\n=== ANÁLISIS COMPLETO FINALIZADO ===");
        System.out.println("Revise los reportes generados en el directorio 'reportes_rendimiento'");
        System.out.println("Los archivos incluyen:");
        System.out.println("- CSV con datos tabulares");
        System.out.println("- HTML con visualizaciones");
        System.out.println("- Análisis estadístico detallado");
        System.out.println("- Recomendaciones específicas");
    }
}

