package ventas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Clase principal para procesar archivos y generar reportes.
 */
public class Main {

    private static final String VENDEDOR_FILE = "vendedores.txt";
    private static final String PRODUCTO_FILE = "productos.txt";
    private static final String VENTAS_DIR = "." + File.separator + "ventas";

    private Map<String, Vendedor> vendedores = new HashMap<>();
    private Map<String, Producto> productos = new HashMap<>();
    private Map<String, Double> ventas = new HashMap<>();
    private Map<String, Integer> productosVendidos = new HashMap<>();

    /**
     * Carga la información de vendedores desde un archivo.
     *
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    private void cargarVendedores() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(VENDEDOR_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 4) {
                    String tipoDocumento = partes[0];
                    String numeroDocumento = partes[1];
                    String nombres = partes[2];
                    String apellidos = partes[3];
                    String idVendedor = tipoDocumento + ";" + numeroDocumento;
                    vendedores.put(idVendedor, new Vendedor(tipoDocumento, numeroDocumento, nombres, apellidos));
                    ventas.put(idVendedor, 0.0); // Inicializar ventas a 0
                }
            }
        }
    }

    /**
     * Carga la información de productos desde un archivo.
     *
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    private void cargarProductos() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(PRODUCTO_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 3) {
                    String idProducto = partes[0];
                    String nombreProducto = partes[1];
                    double precioPorUnidad = Double.parseDouble(partes[2].replace(',', '.'));
                    productos.put(idProducto, new Producto(idProducto, nombreProducto, precioPorUnidad));
                }
            }
        }
    }

    /**
     * Procesa los archivos de ventas y actualiza las ventas y productos vendidos.
     *
     * @throws IOException Si ocurre un error al leer los archivos.
     */
    private void procesarVentas() throws IOException {
        File folder = new File(VENTAS_DIR);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IOException("La carpeta de ventas no existe o no es un directorio.");
        }

        File[] archivos = folder.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isFile() && archivo.getName().endsWith(".txt")) {
                    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                        String linea;
                        String vendedorId = null;
                        while ((linea = br.readLine()) != null) {
                            String[] partes = linea.split(";");
                            if (partes.length == 2) {
                                if (vendedorId == null) {
                                    vendedorId = partes[0] + ";" + partes[1];
                                    if (!vendedores.containsKey(vendedorId)) {
                                        System.err.println("Vendedor no encontrado: " + vendedorId);
                                        vendedorId = null;
                                        break;
                                    }
                                } else {
                                    String idProducto = partes[0];
                                    int cantidad = Integer.parseInt(partes[1]);
                                    if (productos.containsKey(idProducto)) {
                                        double precio = productos.get(idProducto).getPrecioPorUnidad();
                                        double totalVenta = cantidad * precio;
                                        ventas.put(vendedorId, ventas.get(vendedorId) + totalVenta);
                                        productosVendidos.put(idProducto, productosVendidos.getOrDefault(idProducto, 0) + cantidad);
                                    } else {
                                        System.err.println("Producto no encontrado: " + idProducto);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Genera el archivo CSV con el reporte de ventas de los vendedores.
     *
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    private void generarReporteVendedores() throws IOException {
        File file = new File("reporte_vendedores.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            ventas.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .forEach(entry -> {
                    try {
                        String idVendedor = entry.getKey();
                        Vendedor vendedor = vendedores.get(idVendedor);
                        writer.write(vendedor.getNombres() + " " + vendedor.getApellidos() + ";" +String.format(Locale.US, "%.2f", entry.getValue()));
                        writer.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        }
    }

    /**
     * Genera el archivo CSV con el reporte de productos vendidos.
     *
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    private void generarReporteProductos() throws IOException {
        File file = new File("reporte_productos.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            productosVendidos.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .forEach(entry -> {
                    try {
                        String idProducto = entry.getKey();
                        Producto producto = productos.get(idProducto);
                        writer.write(producto.getNombre() + ";" + String.format(Locale.US, "%.2f", producto.getPrecioPorUnidad()) + ";" + entry.getValue());
                        writer.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        }
    }

    /**
     * Método principal para procesar archivos y generar reportes.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        Main app = new Main();
        try {
            app.cargarVendedores();
            app.cargarProductos();
            app.procesarVentas();
            app.generarReporteVendedores();
            app.generarReporteProductos();
            System.out.println("Reportes generados exitosamente.");
        } catch (IOException e) {
            System.err.println("Error al procesar archivos: " + e.getMessage());
        }
    }
}
