import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SistemaVentaBoletos {
    private Evento evento;
    private List<Localidad> localidades;
    private List<Comprador> compradores;
    private int correlativo;
    private final int LIMITE_BOLETOS_POR_PERSONA = 5;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public SistemaVentaBoletos() {
        this.localidades = new ArrayList<>();
        this.compradores = new ArrayList<>();
        this.correlativo = 1;
        cargarCompradores();
        cargarBoletos();
        cargarHistorialCompras();
    }

    public void inicializarEvento(String nombre, LocalDate fecha, int asientosBalcon2, int asientosPlatea, int asientosVIP) {
        int capacidadTotal = asientosBalcon2 + asientosPlatea + asientosVIP;
        this.evento = new Evento(nombre, capacidadTotal, fecha);
        localidades.add(new Localidad("Balcón 2", asientosBalcon2, 300));
        localidades.add(new Localidad("Platea", asientosPlatea, 600));
        localidades.add(new Localidad("Balcón 1 o VIP", asientosVIP, 1800));
    }

    public void agregarComprador(Comprador comprador) {
        this.compradores.add(comprador);
        guardarCompradores();
    }

    public List<Comprador> getCompradores() {
        return compradores;
    }

    public List<Localidad> getLocalidades() {
        return localidades;
    }

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

    public void consultarDisponibilidadTotal() {
        int total = 0;
        for (Localidad localidad : localidades) {
            total += localidad.getDisponibilidad();
        }
        System.out.println("Disponibilidad total de boletos: " + total);
    }

    public void consultarDisponibilidadPorLocalidad(int indiceLocalidad) {
        if (indiceLocalidad >= 0 && indiceLocalidad < localidades.size()) {
            Localidad localidad = localidades.get(indiceLocalidad);
            System.out.println("Disponibilidad en " + localidad.getNombre() + ": " + localidad.getDisponibilidad());
        } else {
            System.out.println("Índice de localidad no válido.");
        }
    }

    public void generarReporteCaja() {
        double totalIngresos = 0;
        for (Localidad localidad : localidades) {
            totalIngresos += localidad.getPrecio() * localidad.getBoletosVendidos();
        }
        System.out.println("Total ingresos por venta de boletos: " + totalIngresos);
    }

    private String generarNumeroBoleto() {
        return "B-" + evento.getFecha().format(dateFormatter).replaceAll("-", "") + "-" + (correlativo++);
    }

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
                // Process historial de compras if necessary
            }
        } catch (IOException e) {
            System.out.println("Error al cargar el historial de compras: " + e.getMessage());
        }
    }
}
