import java.time.LocalDate;

public class Boleto {
    private String numero;
    private LocalDate fecha;
    private Localidad localidad;
    private double precio;

    public Boleto(String numero, LocalDate fecha, Localidad localidad, double precio) {
        this.numero = numero;
        this.fecha = fecha;
        this.localidad = localidad;
        this.precio = precio;
    }

    public String getNumero() {
        return numero;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public double getPrecio() {
        return precio;
    }
}
