import controller.ControladorStayNow;
import exception.*;

import java.util.Scanner;

/**
 * Aplicación principal en modo consola.
 * @author Juan Cogua
 * @version 1.0
 */
public class Main {
    private static ControladorStayNow controlador = new ControladorStayNow();
    private static Scanner sc = new Scanner(System.in);

    /**
     * Punto de entrada en modo consola.
     * @param args argumentos de la aplicación
     * @version 1.0
     */
    public static void main(String[] args) {

        boolean running = true;
        while (running) {
            System.out.println("\n--- StayNow ---");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Registrar alojamiento");
            System.out.println("3. Crear reserva");
            System.out.println("4. Mostrar reservas");
            System.out.println("5. Registrar pago");
            System.out.println("6. Exportar datos TXT");
            System.out.println("7. Salir");
            System.out.print("Elige opción: ");
            String opt = sc.nextLine();
            try {
                switch (opt) {
                    case "1": controlador.opRegistrarCliente(sc); break;
                    case "2": controlador.opRegistrarAlojamiento(sc); break;
                    case "3": controlador.opCrearReserva(sc); break;
                    case "4": controlador.opMostrarReservas(); break;
                    case "5": controlador.opRegistrarPago(sc); break;
                    case "6": controlador.guardarDatosTXT(); System.out.println("Datos guardados."); break;
                    case "7": running = false; break;
                    default: System.out.println("Opción inválida.");
                }
            } catch (ClienteNoEncontradoException | FechaInvalidaException e) {
                // mostrar mensaje breve al usuario y registrar traza para diagnóstico
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // errores de validación/parseo (propagados desde controlador)
                System.err.println("Entrada inválida: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                // errores inesperados: registrar y terminar si es crítico
                System.err.println("Error inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("Saliendo...");
    }
}
