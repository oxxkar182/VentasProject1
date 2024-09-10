package ventas;

/**
 * Clase para representar un producto.
 */
public class Producto {
    private String id;
    private String nombre;
    private double precioPorUnidad;

    public Producto(String id, String nombre, double precioPorUnidad) {
        this.id = id;
        this.nombre = nombre;
        this.precioPorUnidad = precioPorUnidad;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioPorUnidad() {
        return precioPorUnidad;
    }
}
