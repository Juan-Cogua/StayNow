package exception;

/**
 * Excepción para fechas inválidas (salida antes que entrada).
 * @author Juan Cogua
 * @version 1.0
 */
public class FechaInvalidaException extends Exception {
    /**
     * @param msg mensaje de error
     */
    public FechaInvalidaException(String msg) {
        super(msg);
    }
}