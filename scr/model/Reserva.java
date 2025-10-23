package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Clase Reserva (composición). Guarda referencias a Cliente y Alojamiento.
 * @author Juan Cogua
 * @version 1.0
 */
public class Reserva {
    private Cliente cliente;
    private Alojamiento alojamiento;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private double totalReserva;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    public Reserva(Cliente cliente, Alojamiento alojamiento, LocalDate fechaEntrada, LocalDate fechaSalida) {
        this.cliente = cliente;
        this.alojamiento = alojamiento;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.totalReserva = 0;
    }

    public Cliente getCliente() { return cliente; }
    public Alojamiento getAlojamiento() { return alojamiento; }
    public LocalDate getFechaEntrada() { return fechaEntrada; }
    public LocalDate getFechaSalida() { return fechaSalida; }
    public double getTotalReserva() { return totalReserva; }

    /**
     * Calcula los días entre entrada y salida.
     * @return número de días (>=0)
     * @version 1.0
     */
    public int calcularDias() {
        long dias = ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
        return (int) Math.max(dias, 0);
    }

    /**
     * Calcula el total usando el método polimórfico del alojamiento.
     * @version 1.0
     */
    public void calcularTotal() {
        int dias = calcularDias();
        this.totalReserva = alojamiento.calcularCostoEstadia(dias);
    }

    /**
     * Exporta la reserva a formato TXT.
     * @return línea TXT
     * @version 1.0
     */
    public String toTXT() {
        return String.join(",",
            cliente.getId(),
            alojamiento.getCodigo(),
            fechaEntrada.format(FMT),
            fechaSalida.format(FMT),
            String.valueOf(totalReserva)
        );
    }

    /**
     * Muestra información legible de la reserva.
     * @return cadena con información
     * @version 1.0
     */
    public String mostrarInfo() {
        return String.format("Reserva: Cliente=%s, Alojamiento=%s, %s -> %s, total=%.2f",
            cliente.getId(), alojamiento.getCodigo(), fechaEntrada, fechaSalida, totalReserva);
    }
}