import java.time.LocalDate;

/**
 * Clase que representa un boleto para un evento.
 * Incluye información sobre el número de boleto, la fecha del evento, la localidad y el precio.
 * 
 * @author [Tu Nombre]
 * @version 1.0
 */
public class Boleto {
    private String numero;
    private LocalDate fecha;
    private Localidad localidad;
    private double precio;

    /**
     * Constructor que inicializa un boleto con el número, fecha, localidad y precio especificados.
     * 
     * @param numero El número del boleto.
     * @param fecha La fecha en la que se realiza el evento.
     * @param localidad La localidad para la que es válido el boleto.
     * @param precio El precio del boleto.
     */
    public Boleto(String numero, LocalDate fecha, Localidad localidad, double precio) {
        this.numero = numero;
        this.fecha = fecha;
        this.localidad = localidad;
        this.precio = precio;
    }

    /**
     * Obtiene el número del boleto.
     * 
     * @return El número del boleto.
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Obtiene la fecha en la que se realiza el evento.
     * 
     * @return La fecha del evento.
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Obtiene la localidad para la que es válido el boleto.
     * 
     * @return La localidad del boleto.
     */
    public Localidad getLocalidad() {
        return localidad;
    }

    /**
     * Obtiene el precio del boleto.
     * 
     * @return El precio del boleto.
     */
    public double getPrecio() {
        return precio;
    }
}
