import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SistemaVentaBoletos sistema = new SistemaVentaBoletos();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        System.out.print("Ingrese el nombre del teatro: ");
        String nombreTeatro = scanner.nextLine();

        System.out.print("Ingrese la cantidad de asientos para Balcón 2: ");
        int asientosBalcon2 = scanner.nextInt();

        System.out.print("Ingrese la cantidad de asientos para Platea: ");
        int asientosPlatea = scanner.nextInt();

        System.out.print("Ingrese la cantidad de asientos para Balcón 1 o VIP: ");
        int asientosVIP = scanner.nextInt();

        scanner.nextLine();  // Consumir el salto de línea pendiente

        System.out.print("Ingrese la fecha del evento (dd-MM-yyyy): ");
        String fechaStr = scanner.nextLine();
        LocalDate fecha = LocalDate.parse(fechaStr, dateFormatter);

        sistema.inicializarEvento(nombreTeatro, fecha, asientosBalcon2, asientosPlatea, asientosVIP);

        while (true) {
            System.out.println("\nMenú:");
            System.out.println("1. Agregar nuevo comprador");
            System.out.println("2. Comprar boletos");
            System.out.println("3. Consultar disponibilidad total de boletos");
            System.out.println("4. Consultar disponibilidad por localidad");
            System.out.println("5. Generar reporte de caja");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    scanner.nextLine();  // Consumir el salto de línea pendiente
                    System.out.print("Ingrese el nombre del comprador: ");
                    String nombreComprador = scanner.nextLine();

                    System.out.print("Ingrese el email del comprador: ");
                    String emailComprador = scanner.nextLine();

                    System.out.print("Ingrese el presupuesto del comprador: ");
                    double presupuestoComprador = scanner.nextDouble();

                    Comprador comprador = new Comprador(nombreComprador, emailComprador, presupuestoComprador);
                    sistema.agregarComprador(comprador);
                    break;
                case 2:
                    System.out.println("Compradores disponibles:");
                    List<Comprador> compradores = sistema.getCompradores();
                    for (int i = 0; i < compradores.size(); i++) {
                        System.out.println((i + 1) + ". " + compradores.get(i).getNombre());
                    }
                    System.out.print("Seleccione el comprador por número: ");
                    int indiceComprador = scanner.nextInt();
                    Comprador compradorSeleccionado = compradores.get(indiceComprador - 1);

                    System.out.println("Localidades disponibles:");
                    List<Localidad> localidades = sistema.getLocalidades();
                    for (int i = 0; i < localidades.size(); i++) {
                        Localidad localidad = localidades.get(i);
                        System.out.println((i + 1) + ". " + localidad.getNombre() + " (Q" + localidad.getPrecio() + ")");
                    }
                    System.out.print("Seleccione la localidad por número: ");
                    int indiceLocalidad = scanner.nextInt();
                    Localidad localidadSeleccionada = localidades.get(indiceLocalidad - 1);

                    System.out.print("Ingrese la cantidad de boletos a comprar: ");
                    int cantidadBoletos = scanner.nextInt();

                    boolean compraExitosa = sistema.comprarBoletos(compradorSeleccionado, localidadSeleccionada.getNombre(), cantidadBoletos);
                    if (!compraExitosa) {
                        System.out.println("Compra fallida. No se realizaron cambios.");
                    }
                    break;
                case 3:
                    sistema.consultarDisponibilidadTotal();
                    break;
                case 4:
                    System.out.println("Localidades disponibles:");
                    List<Localidad> localidadesDisponibles = sistema.getLocalidades();
                    for (int i = 0; i < localidadesDisponibles.size(); i++) {
                        Localidad localidad = localidadesDisponibles.get(i);
                        System.out.println((i + 1) + ". " + localidad.getNombre());
                    }
                    System.out.print("Seleccione la localidad por número: ");
                    int indiceLocalidadConsulta = scanner.nextInt();
                    sistema.consultarDisponibilidadPorLocalidad(indiceLocalidadConsulta - 1);
                    break;
                case 5:
                    sistema.generarReporteCaja();
                    break;
                case 6:
                    System.out.println("Saliendo del sistema...");
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }
}
