package exception;

/**
 * Excepción para problemas al acceder archivos TXT.
 * @author Juan Cogua
 * @version 1.0
 */
public class ArchivoNoEncontradoException extends Exception {
    /**
     * @param msg mensaje de error
     */
    public ArchivoNoEncontradoException(String msg) {
        super(msg);
    }

    /**
     * @param msg mensaje de error
     * @param cause causa original
     */
    public ArchivoNoEncontradoException(String msg, Throwable cause) {
        super(msg, cause);
    }
}