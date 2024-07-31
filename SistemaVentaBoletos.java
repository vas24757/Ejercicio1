import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona el sistema de venta de boletos para eventos.
 * Incluye funcionalidades para inicializar eventos, agregar compradores, realizar compras,
 * consultar disponibilidad de boletos y generar reportes.
 * También maneja la persistencia de datos en archivos CSV.
 * 
 * @author [Tu Nombre]
 * @version 1.0
 */
public class SistemaVentaBoletos {
    private Evento evento;
    private List<Localidad> localidades;
    private List<Comprador> compradores;
    private int correlativo;
    private final int LIMITE_BOLETOS_POR_PERSONA = 5;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Constructor que inicializa el sistema de venta de boletos.
     * Carga compradores, boletos y el historial de compras desde archivos CSV.
     */
    public SistemaVentaBoletos() {
        this.localidades = new ArrayList<>();
        this.compradores = new ArrayList<>();
        this.correlativo = 1;
        cargarCompradores();
        cargarBoletos();
        cargarHistorialCompras();
    }

    /**
     * Inicializa un nuevo evento con una fecha y capacidad específica,
     * y define las localidades disponibles para el evento.
     * 
     * @param nombre El nombre del evento.
     * @param fecha La fecha del evento.
     * @param asientosBalcon2 Número de asientos en el Balcón 2.
     * @param asientosPlatea Número de asientos en la Platea.
     * @param asientosVIP Número de asientos en el Balcón 1 o VIP.
     */
    public void inicializarEvento(String nombre, LocalDate fecha, int asientosBalcon2, int asientosPlatea, int asientosVIP) {
        int capacidadTotal = asientosBalcon2 + asientosPlatea + asientosVIP;
        this.evento = new Evento(nombre, capacidadTotal, fecha);
        localidades.add(new Localidad("Balcón 2", asientosBalcon2, 300));
        localidades.add(new Localidad("Platea", asientosPlatea, 600));
        localidades.add(new Localidad("Balcón 1 o VIP", asientosVIP, 1800));
    }

    /**
     * Agrega un comprador al sistema y guarda la información en un archivo CSV.
     * 
     * @param comprador El comprador a agregar.
     */
    public void agregarComprador(Comprador comprador) {
        this.compradores.add(comprador);
        guardarCompradores();
    }

    /**
     * Obtiene la lista de compradores.
     * 
     * @return La lista de compradores.
     */
    public List<Comprador> getCompradores() {
        return compradores;
    }

    /**
     * Obtiene la lista de localidades.
     * 
     * @return La lista de localidades.
     */
    public List<Localidad> getLocalidades() {
        return localidades;
    }

    /**
     * Realiza una compra de boletos para un comprador en una localidad específica.
     * Verifica el presupuesto del comprador y la disponibilidad de boletos antes de realizar la compra.
     * 
     * @param comprador El comprador que realiza la compra.
     * @param nombreLocalidad El nombre de la localidad donde se desea comprar boletos.
     * @param cantidad La cantidad de boletos a comprar.
     * @return true si la compra fue exitosa, false en caso contrario.
     */
    public boolean comprarBoletos(Comprador comprador, String nombreLocalidad, int cantidad) {
        if (cantidad > LIMITE_BOLETOS_POR_PERSONA) {
            System.out.println("No se puede comprar más de " + LIMITE_BOLETOS_POR_PERSONA + " boletos por persona.");
            return false;
        }

        for (Localidad localidad : localidades) {
            if (localidad.getNombre().equals(nombreLocalidad)) {
                double total = localidad.getPrecio() * cantidad;
                if (comprador.getPresupuesto() >= total) {
                    if (localidad.venderBoletos(cantidad)) {
                        comprador.setPresupuesto(comprador.getPresupuesto() - total);
                        List<Boleto> boletosComprados = new ArrayList<>();
                        for (int i = 0; i < cantidad; i++) {
                            Boleto boleto = new Boleto(generarNumeroBoleto(), evento.getFecha(), localidad, localidad.getPrecio());
                            comprador.agregarBoleto(boleto);
                            boletosComprados.add(boleto);
                        }
                        guardarCompradores();
                        guardarHistorialCompras(boletosComprados, comprador);
                        guardarBoletos(boletosComprados);
                        System.out.println("Compra realizada. Presupuesto restante: " + comprador.getPresupuesto());
                        return true;
                    } else {
                        System.out.println("No hay suficientes boletos disponibles en la localidad seleccionada.");
                    }
                } else {
                    System.out.println("No hay suficiente presupuesto para realizar la compra.");
                }
            }
        }
        return false;
    }

    /**
     * Consulta la disponibilidad total de boletos en todas las localidades.
     */
    public void consultarDisponibilidadTotal() {
        int total = 0;
        for (Localidad localidad : localidades) {
            total += localidad.getDisponibilidad();
        }
        System.out.println("Disponibilidad total de boletos: " + total);
    }

    /**
     * Consulta la disponibilidad de boletos en una localidad específica.
     * 
     * @param indiceLocalidad El índice de la localidad en la lista de localidades.
     */
    public void consultarDisponibilidadPorLocalidad(int indiceLocalidad) {
        if (indiceLocalidad >= 0 && indiceLocalidad < localidades.size()) {
            Localidad localidad = localidades.get(indiceLocalidad);
            System.out.println("Disponibilidad en " + localidad.getNombre() + ": " + localidad.getDisponibilidad());
        } else {
            System.out.println("Índice de localidad no válido.");
        }
    }

    /**
     * Genera un reporte de los ingresos totales por venta de boletos.
     */
    public void generarReporteCaja() {
        double totalIngresos = 0;
        for (Localidad localidad : localidades) {
            totalIngresos += localidad.getPrecio() * localidad.getBoletosVendidos();
        }
        System.out.println("Total ingresos por venta de boletos: " + totalIngresos);
    }

    /**
     * Genera un número único para un boleto basado en la fecha del evento y un correlativo.
     * 
     * @return El número de boleto generado.
     */
    private String generarNumeroBoleto() {
        return "B-" + evento.getFecha().format(dateFormatter).replaceAll("-", "") + "-" + (correlativo++);
    }

    /**
     * Guarda la información de los compradores en un archivo CSV.
     */
    private void guardarCompradores() {
        try (PrintWriter writer = new PrintWriter(new File("compradores.csv"))) {
            StringBuilder sb = new StringBuilder();
            sb.append("Nombre,Email,Presupuesto\n");
            for (Comprador comprador : compradores) {
                sb.append(comprador.getNombre()).append(",")
                  .append(comprador.getEmail()).append(",")
                  .append(comprador.getPresupuesto()).append("\n");
            }
            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            System.out.println("Error al guardar los compradores: " + e.getMessage());
        }
    }

    /**
     * Guarda el historial de compras en un archivo CSV.
     * 
     * @param boletos La lista de boletos comprados.
     * @param comprador El comprador que realizó la compra.
     */
    private void guardarHistorialCompras(List<Boleto> boletos, Comprador comprador) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("historial_compras.csv", true))) {
            StringBuilder sb = new StringBuilder();
            for (Boleto boleto : boletos) {
                sb.append(comprador.getEmail()).append(",")
                  .append(boleto.getNumero()).append(",")
                  .append(boleto.getFecha().format(dateFormatter)).append(",")
                  .append(boleto.getLocalidad().getNombre()).append(",")
                  .append(boleto.getPrecio()).append("\n");
            }
            writer.write(sb.toString());
        } catch (IOException e) {
            System.out.println("Error al guardar el historial de compras: " + e.getMessage());
        }
    }

    /**
     * Guarda la información de los boletos en un archivo CSV.
     * 
     * @param boletos La lista de boletos a guardar.
     */
    private void guardarBoletos(List<Boleto> boletos) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("boletos.csv", true))) {
            StringBuilder sb = new StringBuilder();
            for (Boleto boleto : boletos) {
                sb.append(boleto.getNumero()).append(",")
                  .append(boleto.getFecha().format(dateFormatter)).append(",")
                  .append(boleto.getLocalidad().getNombre()).append(",")
                  .append(boleto.getPrecio()).append("\n");
            }
            writer.write(sb.toString());
        } catch (IOException e) {
            System.out.println("Error al guardar los boletos: " + e.getMessage());
        }
    }

    /**
     * Carga la información de los compradores desde un archivo CSV.
     * Si el archivo no existe, se crea uno nuevo.
     */
    public void cargarCompradores() {
        File file = new File("compradores.csv");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error al crear el archivo compradores.csv: " + e.getMessage());
                return;
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Saltar la primera línea (encabezado)
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Comprador comprador = new Comprador(values[0], values[1], Double.parseDouble(values[2]));
                compradores.add(comprador);
            }
        } catch (IOException e) {
            System.out.println("Error al cargar los compradores: " + e.getMessage());
        }
    }

    /**
     * Carga la información de los boletos desde un archivo CSV.
     * Si el archivo no existe, se crea uno nuevo.
     */
    public void cargarBoletos() {
        File file = new File("boletos.csv");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error al crear el archivo boletos.csv: " + e.getMessage());
                return;
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Saltar la primera línea (encabezado)
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                LocalDate fecha = LocalDate.parse(values[1], dateFormatter);
                Localidad localidad = null;
                for (Localidad loc : localidades) {
                    if (loc.getNombre().equals(values[2])) {
                        localidad = loc;
                        break;
                    }
                }
                if (localidad != null) {
                    Boleto boleto = new Boleto(values[0], fecha, localidad, Double.parseDouble(values[3]));
                    // Asignar este boleto a algún comprador
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar los boletos: " + e.getMessage());
        }
    }

    /**
     * Carga el historial de compras desde un archivo CSV.
     * Si el archivo no existe, se crea uno nuevo.
     */
    private void cargarHistorialCompras() {
        File file = new File("historial_compras.csv");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error al crear el archivo historial_compras.csv: " + e.getMessage());
                return;
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Saltar la primera línea (encabezado)
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                // Procesar historial de compras si es necesario
            }
        } catch (IOException e) {
            System.out.println("Error al cargar el historial de compras: " + e.getMessage());
        }
    }
}
