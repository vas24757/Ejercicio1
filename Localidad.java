/**
 * Clase que representa una localidad en un evento.
 * Incluye información sobre el nombre, capacidad, precio y el número de boletos vendidos.
 * Permite gestionar la venta de boletos y consultar la disponibilidad.
 * 
 * @author [Tu Nombre]
 * @version 1.0
 */
public class Localidad {
    private String nombre;
    private int capacidad;
    private int boletosVendidos;
    private double precio;

    /**
     * Constructor que inicializa una localidad con el nombre, capacidad y precio especificados.
     * 
     * @param nombre El nombre de la localidad.
     * @param capacidad La capacidad total de asientos en la localidad.
     * @param precio El precio por boleto en la localidad.
     */
    public Localidad(String nombre, int capacidad, double precio) {
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.precio = precio;
        this.boletosVendidos = 0;
    }

    /**
     * Obtiene el nombre de la localidad.
     * 
     * @return El nombre de la localidad.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la capacidad total de asientos en la localidad.
     * 
     * @return La capacidad total de asientos.
     */
    public int getCapacidad() {
        return capacidad;
    }

    /**
     * Obtiene el precio por boleto en la localidad.
     * 
     * @return El precio del boleto.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Obtiene la disponibilidad de boletos en la localidad.
     * 
     * @return El número de boletos disponibles.
     */
    public int getDisponibilidad() {
        return capacidad - boletosVendidos;
    }

    /**
     * Obtiene el número total de boletos vendidos en la localidad.
     * 
     * @return El número de boletos vendidos.
     */
    public int getBoletosVendidos() {
        return boletosVendidos;
    }

    /**
     * Vende una cantidad específica de boletos si hay suficientes boletos disponibles.
     * 
     * @param cantidad La cantidad de boletos a vender.
     * @return true si la venta fue exitosa, false si no hay suficientes boletos disponibles.
     */
    public boolean venderBoletos(int cantidad) {
        if (cantidad <= getDisponibilidad()) {
            boletosVendidos += cantidad;
            return true;
        }
        return false;
    }
}
