import java.time.LocalDate;

public class Evento {
    private String nombre;
    private int capacidadTotal;
    private LocalDate fecha;

    public Evento(String nombre, int capacidadTotal, LocalDate fecha) {
        this.nombre = nombre;
        this.capacidadTotal = capacidadTotal;
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidadTotal() {
        return capacidadTotal;
    }

    public LocalDate getFecha() {
        return fecha;
    }
}
