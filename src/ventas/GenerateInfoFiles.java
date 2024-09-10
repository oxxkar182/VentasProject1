package ventas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;

/**
 * Clase para generar archivos de prueba con información de vendedores y productos.
 */
public class GenerateInfoFiles {

    private static final String[] NOMBRES = {"Juan", "Ana", "Luis", "Marta", "Carlos", "Laura"};
    private static final String[] APELLIDOS = {"Pérez", "García", "Rodríguez", "Martínez", "Hernández", "López"};
    private static final String[] PRODUCTOS = {"Laptop", "Mouse", "Teclado", "Monitor", "Impresora"};

    /**
     * Crea un archivo de ventas pseudoaleatorio para un vendedor.
     *
     * @param randomSalesCount Cantidad de ventas aleatorias a generar.
     * @param name Nombre del vendedor.
     * @param id ID del vendedor.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    public static void createSalesMenFile(int randomSalesCount, String name, long id) throws IOException {
        File file = new File("ventas_" + id + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(name + ";" + id);
            writer.newLine();
            Random random = new Random();
            for (int i = 0; i < randomSalesCount; i++) {
                String productId = String.format("%03d", random.nextInt(PRODUCTOS.length) + 1);
                int quantity = random.nextInt(10) + 1;
                writer.write(productId + ";" + quantity);
                writer.newLine();
            }
        }
    }

    /**
     * Crea un archivo con información pseudoaleatoria de productos.
     *
     * @param productsCount Cantidad de productos a generar.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    public static void createProductsFile(int productsCount) throws IOException {
        File file = new File("productos.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            Random random = new Random();
            for (int i = 1; i <= productsCount; i++) {
                String productId = String.format("%03d", i);
                String productName = PRODUCTOS[random.nextInt(PRODUCTOS.length)];
                double price = random.nextDouble() * 100 + 1;
                writer.write(productId + ";" + productName + ";" + String.format(Locale.US, "%.2f", price));
                writer.newLine();
            }
        }
    }

    /**
     * Crea un archivo con información pseudoaleatoria de vendedores.
     *
     * @param salesmanCount Cantidad de vendedores a generar.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    public static void createSalesManInfoFile(int salesmanCount) throws IOException {
        File file = new File("vendedores.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            Random random = new Random();
            for (int i = 1; i <= salesmanCount; i++) {
                String typeDoc = String.format("%08d", random.nextInt(100000000));
                String numDoc = String.format("%04d", random.nextInt(10000));
                String name = NOMBRES[random.nextInt(NOMBRES.length)];
                String surname = APELLIDOS[random.nextInt(APELLIDOS.length)];
                writer.write(typeDoc + ";" + numDoc + ";" + name + ";" + surname);
                writer.newLine();
            }
        }
    }

    /**
     * Método principal para generar archivos de prueba.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        try {
            createSalesManInfoFile(10); // Generar 10 vendedores
            createProductsFile(5); // Generar 5 productos
            createSalesMenFile(20, "Juan Pérez", 12345678L); // Generar archivo de ventas para un vendedor
            System.out.println("Archivos de prueba generados exitosamente.");
        } catch (IOException e) {
            System.err.println("Error al generar archivos: " + e.getMessage());
        }
    }
}
