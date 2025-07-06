package modelo;

import java.util.Scanner;
import LinkedList.MensajeException; // Para manejar excepciones de tu ListaEnlazada/AVL

public class MenuSistemaInventario {

    private SistemaInventario sistema;
    private Scanner scanner;

    public MenuSistemaInventario() {
        this.scanner = new Scanner(System.in);
        inicializarSistema();
    }

    private void inicializarSistema() {
        System.out.println("--- CONFIGURACIÓN INICIAL DEL SISTEMA DE INVENTARIO ---");

        int numMaxProductosMatriz = 0;

        // Solicitar número máximo de productos para la Matriz Dispersa
        // Esto definirá el número de filas de la matriz y el tamaño de las tablas hash.
        while (numMaxProductosMatriz <= 0) {
            System.out.print("Ingrese el número MÁXIMO de productos que el inventario puede almacenar (ej. 2000): ");
            numMaxProductosMatriz = leerInt();
            if (numMaxProductosMatriz <= 0) {
                System.out.println("La capacidad debe ser un número positivo.");
            }
        }

        // Aquí inicializamos el sistema con el valor proporcionado.
        // Las capacidades de las tablas hash se calcularán DENTRO de SistemaInventario.
        this.sistema = new SistemaInventario(numMaxProductosMatriz);

        System.out.println("\n--- Sistema de Inventario Inicializado ---");
        System.out.println("Máximo Productos en Inventario: " + numMaxProductosMatriz);
        // Opcionalmente, podrías agregar getters en SistemaInventario para mostrar los tamaños de hash calculados.
        // System.out.println("Capacidad Hash Categorías (calculada): " + sistema.getCapacidadHashCategorias());
        // System.out.println("Capacidad Hash Productos Global (calculada): " + sistema.getCapacidadHashProductosGlobal());
        System.out.println("Orden del B+Tree interno: " + sistema.getOrdenBTreePlus()); // Accedemos directamente a la constante estática
    }

    public void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n--- MENÚ DE GESTIÓN DE INVENTARIO ---");
            System.out.println("1. Gestión de Categorías");
            System.out.println("2. Gestión de Productos");
            System.out.println("3. Reportes y Visualización");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    menuCategorias();
                    break;
                case 2:
                    menuProductos();
                    break;
                case 3:
                    menuReportes();
                    break;
                case 0:
                    System.out.println("Saliendo del sistema de inventario. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    private void menuCategorias() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE CATEGORÍAS ---");
            System.out.println("1. Insertar Categoría");
            System.out.println("2. Buscar Categoría");
            System.out.println("3. Eliminar Categoría");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el nombre de la nueva categoría: ");
                    String nombreCatInsertar = scanner.nextLine();
                    sistema.insertarCategoria(nombreCatInsertar);
                    break;
                case 2:
                    System.out.print("Ingrese el nombre de la categoría a buscar: ");
                    String nombreCatBuscar = scanner.nextLine();
                    CategoriaData catData = sistema.buscarCategoria(nombreCatBuscar);
                    if (catData != null) {
                        System.out.println("Categoría encontrada: " + catData.nombreCategoriaOriginal +
                                " (Hash: " + catData.indiceHashCategoria +
                                ", Productos: " + catData.productosPorCodigo.getTotalClaves().size() + ")");
                    } else {
                        System.out.println("Categoría '" + nombreCatBuscar + "' no encontrada.");
                    }
                    break;
                case 3:
                    System.out.print("Ingrese el nombre de la categoría a eliminar: ");
                    String nombreCatEliminar = scanner.nextLine();
                    if (sistema.eliminarCategoria(nombreCatEliminar)) {
                        System.out.println("Categoría '" + nombreCatEliminar + "' eliminada con éxito.");
                    } else {
                        System.out.println("No se pudo eliminar la categoría '" + nombreCatEliminar + "'.");
                    }
                    break;
                case 0:
                    System.out.println("Volviendo al Menú Principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    private void menuProductos() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE PRODUCTOS ---");
            System.out.println("1. Insertar Producto");
            System.out.println("2. Buscar Producto");
            System.out.println("3. Eliminar Producto");
            System.out.println("4. Actualizar Stock de Producto");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese Código del Producto: ");
                    String codigoProd = scanner.nextLine();
                    System.out.print("Ingrese Nombre del Producto: ");
                    String nombreProd = scanner.nextLine();
                    System.out.print("Ingrese Categoría del Producto: ");
                    String categoriaProd = scanner.nextLine();
                    System.out.print("Ingrese Precio del Producto: ");
                    double precioProd = leerDouble();
                    System.out.print("Ingrese Stock Disponible: ");
                    int stockProd = leerInt();

                    Producto nuevoProducto = new Producto(codigoProd, nombreProd, categoriaProd, precioProd, stockProd);
                    sistema.insertarProducto(nuevoProducto);
                    break;
                case 2:
                    System.out.print("Ingrese Código del Producto a buscar: ");
                    String codigoBuscar = scanner.nextLine();
                    Producto prodEncontrado = sistema.buscarProducto(codigoBuscar);
                    if (prodEncontrado != null) {
                        System.out.println("Producto encontrado: " + prodEncontrado);
                    } else {
                        System.out.println("Producto con código '" + codigoBuscar + "' no encontrado.");
                    }
                    break;
                case 3:
                    System.out.print("Ingrese Código del Producto a eliminar: ");
                    String codigoEliminar = scanner.nextLine();
                    if (sistema.eliminarProducto(codigoEliminar)) {
                        System.out.println("Producto '" + codigoEliminar + "' eliminado con éxito.");
                    } else {
                        System.out.println("No se pudo eliminar el producto '" + codigoEliminar + "'.");
                    }
                    break;
                case 4:
                    System.out.print("Ingrese Código del Producto para actualizar stock: ");
                    String codigoActualizar = scanner.nextLine();
                    System.out.print("Ingrese Nuevo Stock: ");
                    int nuevoStock = leerInt();
                    sistema.actualizarStockProducto(codigoActualizar, nuevoStock);
                    break;
                case 0:
                    System.out.println("Volviendo al Menú Principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    private void menuReportes() {
        int opcion;
        do {
            System.out.println("\n--- REPORTES Y VISUALIZACIÓN ---");
            System.out.println("1. Mostrar todas las Categorías (y cantidad de productos)");
            System.out.println("2. Mostrar Productos por Categoría Específica");
            System.out.println("3. Mostrar Stocks como Matriz Dispersa");
            System.out.println("4. Mostrar Productos y sus Stocks (Formato Tabla)");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    try {
                        sistema.mostrarCategorias();
                    } catch (MensajeException e) {
                        System.out.println("Error al mostrar categorías: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.print("Ingrese el nombre de la categoría para ver sus productos: ");
                    String catReporte = scanner.nextLine();
                    sistema.mostrarProductosPorCategoria(catReporte);
                    break;
                case 3:
                    sistema.mostrarStocksComoMatriz();
                    break;
                case 4:
                    sistema.mostrarProductosYStocks();
                    break;
                case 0:
                    System.out.println("Volviendo al Menú Principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    // Métodos auxiliares para leer entrada de usuario de forma segura
    private int leerOpcion() {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Por favor, ingrese un número.");
            scanner.next(); // Consumir la entrada inválida
            System.out.print("Seleccione una opción: ");
        }
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea
        return opcion;
    }

    private double leerDouble() {
        while (!scanner.hasNextDouble()) {
            System.out.println("Entrada inválida. Por favor, ingrese un número decimal.");
            scanner.next();
            System.out.print("Ingrese un valor válido: ");
        }
        double valor = scanner.nextDouble();
        scanner.nextLine();
        return valor;
    }

    private int leerInt() {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Por favor, ingrese un número entero.");
            scanner.next();
            System.out.print("Ingrese un valor válido: ");
        }
        int valor = scanner.nextInt();
        scanner.nextLine();
        return valor;
    }

    public static void main(String[] args) {
        MenuSistemaInventario menu = new MenuSistemaInventario();
        menu.mostrarMenu();
    }
}