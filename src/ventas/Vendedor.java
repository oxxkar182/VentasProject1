package ventas;

/**
 * Clase para representar un vendedor.
 */
public class Vendedor {
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombres;
    private String apellidos;

    public Vendedor(String tipoDocumento, String numeroDocumento, String nombres, String apellidos) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }
}
