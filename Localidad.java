public class Localidad {
    private String nombre;
    private int capacidad;
    private int boletosVendidos;
    private double precio;

    public Localidad(String nombre, int capacidad, double precio) {
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.precio = precio;
        this.boletosVendidos = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public double getPrecio() {
        return precio;
    }

    public int getDisponibilidad() {
        return capacidad - boletosVendidos;
    }

    public int getBoletosVendidos() {
        return boletosVendidos;
    }

    public boolean venderBoletos(int cantidad) {
        if (cantidad <= getDisponibilidad()) {
            boletosVendidos += cantidad;
            return true;
        }
        return false;
    }
}
