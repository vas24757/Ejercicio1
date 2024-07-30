import java.util.ArrayList;
import java.util.List;

public class Comprador {
    private String nombre;
    private String email;
    private double presupuesto;
    private List<Boleto> boletos;

    public Comprador(String nombre, String email, double presupuesto) {
        this.nombre = nombre;
        this.email = email;
        this.presupuesto = presupuesto;
        this.boletos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public void agregarBoleto(Boleto boleto) {
        boletos.add(boleto);
    }

    public List<Boleto> getBoletos() {
        return boletos;
    }
}
