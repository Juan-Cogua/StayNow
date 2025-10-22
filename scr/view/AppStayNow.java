package view;

import controller.ControladorStayNow;
import model.*;
import exception.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Interfaz gráfica básica para StayNow (Swing).
 * @author Juan Cogua
 * @version 1.0
 */
public class AppStayNow extends JFrame {
    @SuppressWarnings("unused") // usado por listeners; su acceso puede no detectarse por análisis estático
    private ControladorStayNow controlador;
    private JTextArea outputArea;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Crea la ventana principal de la GUI usando un controlador.
     * @param controlador controlador que maneja la lógica y persistencia
     * @version 1.0
     */
    public AppStayNow(ControladorStayNow controlador) {
        this.controlador = controlador;
        setTitle("StayNow - Interfaz gráfica");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de acciones
        JPanel actions = new JPanel(new GridLayout(0, 1, 5, 5));
        JButton btnRegistrarCliente = new JButton("Registrar cliente");
        JButton btnRegistrarAlojamiento = new JButton("Registrar alojamiento");
        JButton btnCrearReserva = new JButton("Crear reserva");
        JButton btnMostrarReservas = new JButton("Mostrar reservas");
        JButton btnRegistrarPago = new JButton("Registrar pago");
        JButton btnExportar = new JButton("Exportar datos TXT");

        actions.add(btnRegistrarCliente);
        actions.add(btnRegistrarAlojamiento);
        actions.add(btnCrearReserva);
        actions.add(btnMostrarReservas);
        actions.add(btnRegistrarPago);
        actions.add(btnExportar);

        add(actions, BorderLayout.WEST);

        // Área de salida
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Listeners
        btnRegistrarCliente.addActionListener((ActionEvent e) -> {
            String id = JOptionPane.showInputDialog(this, "ID cliente:");
            if (id == null) return;
            String nombre = JOptionPane.showInputDialog(this, "Nombre:");
            if (nombre == null) return;
            String email = JOptionPane.showInputDialog(this, "Email:");
            if (email == null) return;
            controlador.registrarCliente(new Cliente(id, nombre, email));
            output("Cliente registrado: " + id + " - " + nombre);
        });

        btnRegistrarAlojamiento.addActionListener((ActionEvent e) -> {
            String tipo = JOptionPane.showInputDialog(this, "Tipo (HOTEL/APARTAMENTO/CABANA):");
            if (tipo == null) return;
            tipo = tipo.toUpperCase();
            String codigo = JOptionPane.showInputDialog(this, "Código:");
            if (codigo == null) return;
            String nombre = JOptionPane.showInputDialog(this, "Nombre:");
            if (nombre == null) return;
            String ubic = JOptionPane.showInputDialog(this, "Ubicación:");
            if (ubic == null) return;
            String precioStr = JOptionPane.showInputDialog(this, "Precio por noche:");
            if (precioStr == null) return;
            double precio = Double.parseDouble(precioStr.trim());
            try {
                if ("HOTEL".equals(tipo)) {
                    int est = Integer.parseInt(JOptionPane.showInputDialog(this, "Estrellas:"));
                    boolean desayuno = Boolean.parseBoolean(JOptionPane.showInputDialog(this, "Servicio desayuno (true/false):"));
                    controlador.registrarAlojamiento(new Hotel(codigo, nombre, ubic, precio, est, desayuno));
                } else if ("APARTAMENTO".equals(tipo)) {
                    int nh = Integer.parseInt(JOptionPane.showInputDialog(this, "Num habitaciones:"));
                    boolean p = Boolean.parseBoolean(JOptionPane.showInputDialog(this, "Parqueadero (true/false):"));
                    controlador.registrarAlojamiento(new model.Apartamento(codigo, nombre, ubic, precio, nh, p));
                } else if ("CABANA".equals(tipo)) {
                    String zona = JOptionPane.showInputDialog(this, "Zona:");
                    int cap = Integer.parseInt(JOptionPane.showInputDialog(this, "Capacidad personas:"));
                    controlador.registrarAlojamiento(new model.Cabana(codigo, nombre, ubic, precio, zona, cap));
                } else {
                    JOptionPane.showMessageDialog(this, "Tipo no reconocido.");
                    return;
                }
                output("Alojamiento registrado: " + codigo + " - " + nombre);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Dato numérico inválido.");
            }
        });

        btnCrearReserva.addActionListener((ActionEvent e) -> {
            try {
                String idCliente = JOptionPane.showInputDialog(this, "ID cliente:");
                if (idCliente == null) return;
                String codigo = JOptionPane.showInputDialog(this, "Código alojamiento:");
                if (codigo == null) return;
                String entrada = JOptionPane.showInputDialog(this, "Fecha entrada (YYYY-MM-DD):");
                if (entrada == null) return;
                String salida = JOptionPane.showInputDialog(this, "Fecha salida (YYYY-MM-DD):");
                if (salida == null) return;
                Reserva r = controlador.crearReserva(idCliente, codigo, LocalDate.parse(entrada, DATE_FMT), LocalDate.parse(salida, DATE_FMT));
                output("Reserva creada: " + r.mostrarInfo());
            } catch (ClienteNoEncontradoException | FechaInvalidaException ex) {
                JOptionPane.showMessageDialog(this, "No se pudo crear reserva: " + ex.getMessage());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        btnMostrarReservas.addActionListener((ActionEvent e) -> {
            List<Reserva> lista = controlador.listarReservas();
            if (lista.isEmpty()) output("No hay reservas.");
            else {
                output("Reservas actuales:");
                lista.forEach(r -> output(r.mostrarInfo()));
            }
        });

        btnRegistrarPago.addActionListener((ActionEvent e) -> {
            try {
                String idp = JOptionPane.showInputDialog(this, "ID pago:");
                if (idp == null) return;
                String montoStr = JOptionPane.showInputDialog(this, "Monto:");
                if (montoStr == null) return;
                double monto = Double.parseDouble(montoStr.trim());
                String metodo = JOptionPane.showInputDialog(this, "Método de pago:");
                if (metodo == null) return;
                Pago p = new Pago(idp, monto, metodo);
                controlador.registrarPago(p);
                output("Pago registrado: " + idp + " - " + monto + " - " + metodo);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Monto inválido.");
            }
        });

        btnExportar.addActionListener((ActionEvent e) -> {
            try {
                controlador.guardarDatosTXT();
                output("Datos exportados a TXT.");
            } catch (exception.ArchivoNoEncontradoException ex) {
                // mostrar diálogo en GUI y registrar en área de salida
                JOptionPane.showMessageDialog(this, "Error al guardar archivos TXT: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                output("Error exportando datos: " + ex.getMessage());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                output("Error inesperado: " + ex.getMessage());
            }
        });
    }

    /**
     * Añade texto al área de salida.
     * @param msg mensaje a mostrar
     * @version 1.0
     */
    private void output(String msg) {
        outputArea.append(msg + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    /**
     * Permite ejecutar solo la interfaz gráfica (crea su propio controlador).
     * @param args argumentos de la línea de comandos
     * @version 1.0
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            controller.ControladorStayNow ctrl = new controller.ControladorStayNow();
            new AppStayNow(ctrl).setVisible(true);
        });
    }
}