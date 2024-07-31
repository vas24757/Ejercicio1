import java.time.LocalDate;

/**
 * Clase que representa un evento con un nombre, capacidad total y fecha.
 * 
 * @author [Tu Nombre]
 * @version 1.0
 */
public class Evento {
    private String nombre;
    private int capacidadTotal;
    private LocalDate fecha;

    /**
     * Constructor que inicializa un evento con el nombre, capacidad total y fecha especificados.
     * 
     * @param nombre El nombre del evento.
     * @param capacidadTotal La capacidad total del evento.
     * @param fecha La fecha en la que se realiza el evento.
     */
    public Evento(String nombre, int capacidadTotal, LocalDate fecha) {
        this.nombre = nombre;
        this.capacidadTotal = capacidadTotal;
        this.fecha = fecha;
    }

    /**
     * Obtiene el nombre del evento.
     * 
     * @return El nombre del evento.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la capacidad total del evento.
     * 
     * @return La capacidad total del evento.
     */
    public int getCapacidadTotal() {
        return capacidadTotal;
    }

    /**
     * Obtiene la fecha en la que se realiza el evento.
     * 
     * @return La fecha del evento.
     */
    public LocalDate getFecha() {
        return fecha;
    }
}
