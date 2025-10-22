package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase Pago (asociación) que representa un pago realizado.
 * @author Juan Cogua
 * @version 1.0
 */
public class Pago {
    private String idPago;
    private double monto;
    private String metodoPago;
    private LocalDateTime fechaPago;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Pago(String idPago, double monto, String metodoPago) {
        this.idPago = idPago;
        this.monto = monto;
        this.metodoPago = metodoPago;
    }

    public String getIdPago() { return idPago; }
    public double getMonto() { return monto; }
    public String getMetodoPago() { return metodoPago; }
    public LocalDateTime getFechaPago() { return fechaPago; }

    /**
     * Registra la fecha del pago con la fecha/hora actual.
     * @version 1.0
     */
    public void registrarPago() {
        this.fechaPago = LocalDateTime.now();
    }

    /**
     * Exporta el pago a formato TXT.
     * @return línea TXT
     * @version 1.0
     */
    public String toTXT() {
        return String.join("|",
            idPago,
            String.valueOf(monto),
            metodoPago,
            fechaPago == null ? "" : fechaPago.format(FMT)
        );
    }
}