package controller;

import model.*;
import exception.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
/**
 * Controlador principal que maneja colecciones y persistencia en TXT.
 * @version 1.0
 * @author Juan Cogua
 */
public class ControladorStayNow {
    private List<Cliente> clientes = new ArrayList<>();
    private List<Alojamiento> alojamientos = new ArrayList<>();
    private List<Reserva> reservas = new ArrayList<>();
    private List<Pago> pagos = new ArrayList<>();

    private final Path clientesFile = Paths.get("scr", "clientes.txt");
    private final Path alojamientosFile = Paths.get("scr", "alojamientos.txt");
    private final Path reservasFile = Paths.get("scr", "reservas.txt");

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    public ControladorStayNow() {
        try { cargarDatosTXT(); } catch (ArchivoNoEncontradoException e) { /* archivos aún no creados */ }
    }

    // métodos de dominio ya existentes
    /**
     * Registra un nuevo cliente en memoria.
     * @param c cliente a registrar
     * @return void
     * @version 1.0
     */
    public void registrarCliente(Cliente c) { clientes.add(c); }

    /**
     * Registra un nuevo alojamiento en memoria.
     * @param a alojamiento a registrar
     * @return void
     * @version 1.0
     */
    public void registrarAlojamiento(Alojamiento a) { alojamientos.add(a); }

    /**
     * Busca un cliente por su id.
     * @param id identificador del cliente
     * @return Cliente encontrado o null
     * @version 1.0
     */
    public Cliente buscarClientePorId(String id) { return clientes.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null); }

    /**
     * Busca un alojamiento por su código.
     * @param codigo código del alojamiento
     * @return Alojamiento encontrado o null
     * @version 1.0
     */
    public Alojamiento buscarAlojamientoPorCodigo(String codigo) { return alojamientos.stream().filter(a -> a.getCodigo().equals(codigo)).findFirst().orElse(null); }

    /**
     * Crea una reserva entre cliente y alojamiento.
     * @param idCliente id del cliente
     * @param codigoAlojamiento código del alojamiento
     * @param entrada fecha de entrada
     * @param salida fecha de salida
     * @return Reserva creada
     * @throws ClienteNoEncontradoException si no existe el cliente
     * @throws FechaInvalidaException si la fecha de salida no es posterior a la de entrada
     * @version 1.0
     */
    public Reserva crearReserva(String idCliente, String codigoAlojamiento, LocalDate entrada, LocalDate salida)
            throws ClienteNoEncontradoException, FechaInvalidaException {
        Cliente c = buscarClientePorId(idCliente);
        if (c == null) throw new ClienteNoEncontradoException("Cliente no encontrado: " + idCliente);
        Alojamiento a = buscarAlojamientoPorCodigo(codigoAlojamiento);
        if (a == null) throw new IllegalArgumentException("Alojamiento no encontrado: " + codigoAlojamiento);
        if (!salida.isAfter(entrada)) throw new FechaInvalidaException("La fecha de salida debe ser posterior a la de entrada.");
        Reserva r = new Reserva(c, a, entrada, salida);
        r.calcularTotal();
        reservas.add(r);
        return r;
    }

    /**
     * Registra un pago (asocia fecha y lo guarda en memoria).
     * @param p pago a registrar
     * @return void
     * @version 1.0
     */
    public void registrarPago(Pago p) { p.registrarPago(); pagos.add(p); }

    /**
     * Lista las reservas actuales.
     * @return lista inmodificable de reservas
     * @version 1.0
     */
    public List<Reserva> listarReservas() { return Collections.unmodifiableList(reservas); }
    public List<Cliente> listarClientes() { return Collections.unmodifiableList(clientes); }
    public List<Alojamiento> listarAlojamientos() { return Collections.unmodifiableList(alojamientos); }
    public List<Pago> listarPagos() { return Collections.unmodifiableList(pagos); }

    // persistencia
    /**
     * Guarda los datos en archivos TXT.
     * @throws ArchivoNoEncontradoException si ocurre un error de E/S
     * @return void
     * @version 1.0
     */
    public void guardarDatosTXT() throws ArchivoNoEncontradoException {
        try {
            Files.createDirectories(clientesFile.getParent());
            // Guardar clientes con encabezado
            try (BufferedWriter bw = Files.newBufferedWriter(clientesFile)) {
                bw.write("ID,Nombre,Email\n"); // Encabezado
                for (Cliente c : clientes) bw.write(c.toTXT() + System.lineSeparator());
            }
            // Guardar alojamientos con encabezado
            try (BufferedWriter bw = Files.newBufferedWriter(alojamientosFile)) {
                bw.write("Tipo,Codigo,Nombre,Ubicacion,Precio,Extra1,Extra2\n"); // Encabezado
                for (Alojamiento a : alojamientos) bw.write(a.toTXT() + System.lineSeparator());
            }
            // Guardar reservas con encabezado
            try (BufferedWriter bw = Files.newBufferedWriter(reservasFile)) {
                bw.write("IDCliente,CodigoAlojamiento,FechaEntrada,FechaSalida,TotalReserva\n"); // Encabezado
                for (Reserva r : reservas) bw.write(r.toTXT() + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new ArchivoNoEncontradoException("Error al guardar archivos TXT", e);
        }
    }

    /**
     * Carga datos desde archivos TXT a las colecciones en memoria.
     * @throws ArchivoNoEncontradoException si ocurre un error de E/S
     * @return void
     * @version 1.0
     */
    public void cargarDatosTXT() throws ArchivoNoEncontradoException {
        cargarClientesTXT();
        cargarAlojamientosTXT();

        // Cargar reservas
        if (Files.exists(reservasFile)) {
            try (BufferedReader br = Files.newBufferedReader(reservasFile)) {
                String line = br.readLine(); // Leer encabezado
                while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                    String[] tk = line.split(",");
                    if (tk.length >= 4) {
                        String idCliente = tk[0];
                        String codigoAloj = tk[1];
                        LocalDate entrada = LocalDate.parse(tk[2], DATE_FMT);
                        LocalDate salida = LocalDate.parse(tk[3], DATE_FMT);
                        Cliente c = buscarClientePorId(idCliente);
                        Alojamiento a = buscarAlojamientoPorCodigo(codigoAloj);
                        if (c != null && a != null) {
                            Reserva r = new Reserva(c, a, entrada, salida);
                            r.calcularTotal();
                            reservas.add(r);
                        }
                    }
                }
            } catch (IOException e) {
                throw new ArchivoNoEncontradoException("Error cargando reservas", e);
            }
        }
    }

    /**
     * Carga clientes desde archivos TXT a las colecciones en memoria.
     * @throws ArchivoNoEncontradoException si ocurre un error de E/S
     * @return void
     * @version 1.0
     */
    public void cargarClientesTXT() throws ArchivoNoEncontradoException {
        if (Files.exists(clientesFile)) {
            try (BufferedReader br = Files.newBufferedReader(clientesFile)) {
                br.readLine(); // Ignorar la primera línea (encabezado)
                String line;
                while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                    String[] tk = line.split(",");
                    if (tk.length >= 3) {
                        clientes.add(new Cliente(tk[0], tk[1], tk[2]));
                    }
                }
            } catch (IOException e) {
                throw new ArchivoNoEncontradoException("Error cargando clientes", e);
            }
        }
    }

    /**
     * Carga alojamientos desde archivos TXT a las colecciones en memoria.
     * @throws ArchivoNoEncontradoException si ocurre un error de E/S
     * @return void
     * @version 1.0
     */
    public void cargarAlojamientosTXT() throws ArchivoNoEncontradoException {
        if (Files.exists(alojamientosFile)) {
            try (BufferedReader br = Files.newBufferedReader(alojamientosFile)) {
                br.readLine(); // Ignorar la primera línea (encabezado)
                String line;
                while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                    String[] tk = line.split(",");
                    if (tk.length >= 5) {
                        String tipo = tk[0];
                        String codigo = tk[1];
                        String nombre = tk[2];
                        String ubic = tk[3];
                        double precio = Double.parseDouble(tk[4]);
                        if ("HOTEL".equalsIgnoreCase(tipo) && tk.length >= 7) {
                            int estrellas = Integer.parseInt(tk[5]);
                            boolean desayuno = Boolean.parseBoolean(tk[6]);
                            alojamientos.add(new Hotel(codigo, nombre, ubic, precio, estrellas, desayuno));
                        } else if ("APARTAMENTO".equalsIgnoreCase(tipo) && tk.length >= 7) {
                            int hab = Integer.parseInt(tk[5]);
                            boolean parqueo = Boolean.parseBoolean(tk[6]);
                            alojamientos.add(new Apartamento(codigo, nombre, ubic, precio, hab, parqueo));
                        } else if ("CABANA".equalsIgnoreCase(tipo) && tk.length >= 7) {
                            String zona = tk[5];
                            int cap = Integer.parseInt(tk[6]);
                            alojamientos.add(new Cabana(codigo, nombre, ubic, precio, zona, cap));
                        }
                    }
                }
            } catch (IOException e) {
                throw new ArchivoNoEncontradoException("Error cargando alojamientos", e);
            }
        }
    }

    // Métodos auxiliares para interacción por consola (mueven lógica UI ligera al controlador tal como pediste)
    /**
     * Operación por consola para registrar cliente.
     * @param sc Scanner usado para leer entrada
     * @version 1.0
     */
    public void opRegistrarCliente(Scanner sc) {
        System.out.print("ID: "); String id = sc.nextLine();
        System.out.print("Nombre: "); String nombre = sc.nextLine();
        System.out.print("Email: "); String email = sc.nextLine();
        Cliente c = new Cliente(id, nombre, email);
        registrarCliente(c);
        System.out.println("Cliente registrado.");
    }

    /**
     * Operación por consola para registrar alojamiento.
     * @param sc Scanner usado para leer entrada
     * @version 1.0
     */
    public void opRegistrarAlojamiento(Scanner sc) {
        System.out.print("Tipo (HOTEL/APARTAMENTO/CABANA): "); String tipo = sc.nextLine().toUpperCase();
        System.out.print("Codigo: "); String codigo = sc.nextLine();
        System.out.print("Nombre: "); String nombre = sc.nextLine();
        System.out.print("Ubicacion: "); String ubic = sc.nextLine();
        System.out.print("Precio por noche: "); double precio = Double.parseDouble(sc.nextLine());
        if ("HOTEL".equals(tipo)) {
            System.out.print("Estrellas: "); int est = Integer.parseInt(sc.nextLine());
            System.out.print("Servicio desayuno (true/false): "); boolean d = Boolean.parseBoolean(sc.nextLine());
            registrarAlojamiento(new model.Hotel(codigo, nombre, ubic, precio, est, d));
        } else if ("APARTAMENTO".equals(tipo)) {
            System.out.print("Num habitaciones: "); int nh = Integer.parseInt(sc.nextLine());
            System.out.print("Parqueadero (true/false): "); boolean p = Boolean.parseBoolean(sc.nextLine());
            registrarAlojamiento(new model.Apartamento(codigo, nombre, ubic, precio, nh, p));
        } else if ("CABANA".equals(tipo)) {
            System.out.print("Zona: "); String zona = sc.nextLine();
            System.out.print("Capacidad personas: "); int cap = Integer.parseInt(sc.nextLine());
            registrarAlojamiento(new model.Cabana(codigo, nombre, ubic, precio, zona, cap));
        } else {
            System.out.println("Tipo no reconocido.");
            return;
        }
        System.out.println("Alojamiento registrado.");
    }

    /**
     * Operación por consola para crear reserva.
     * @param sc Scanner usado para leer entrada
     * @throws ClienteNoEncontradoException si no existe el cliente
     * @throws FechaInvalidaException si la fecha es inválida
     * @version 1.0
     */
    public void opCrearReserva(Scanner sc) throws ClienteNoEncontradoException, FechaInvalidaException {
        System.out.print("ID cliente: "); String id = sc.nextLine();
        System.out.print("Codigo alojamiento: "); String cod = sc.nextLine();
        System.out.print("Fecha entrada (YYYY-MM-DD): "); LocalDate entrada = LocalDate.parse(sc.nextLine(), DATE_FMT);
        System.out.print("Fecha salida (YYYY-MM-DD): "); LocalDate salida = LocalDate.parse(sc.nextLine(), DATE_FMT);
        Reserva r = crearReserva(id, cod, entrada, salida);
        System.out.println("Reserva creada: " + r.mostrarInfo());
    }

    /**
     * Operación por consola que muestra reservas.
     * @version 1.0
     */
    public void opMostrarReservas() {
        List<Reserva> lista = listarReservas();
        if (lista.isEmpty()) System.out.println("No hay reservas.");
        else lista.forEach(r -> System.out.println(r.mostrarInfo()));
    }

    /**
     * Operación por consola para registrar pago.
     * @param sc Scanner usado para leer entrada
     * @version 1.0
     */
    public void opRegistrarPago(Scanner sc) {
        System.out.print("ID pago: "); String idp = sc.nextLine();
        System.out.print("Monto: "); double m = Double.parseDouble(sc.nextLine());
        System.out.print("Metodo: "); String met = sc.nextLine();
        Pago p = new Pago(idp, m, met);
        registrarPago(p);
        System.out.println("Pago registrado con fecha: " + p.getFechaPago());
    }

}