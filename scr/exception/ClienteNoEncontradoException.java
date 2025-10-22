package exception;

/**
 * Excepción cuando no se encuentra un cliente.
 * @author Juan Cogua
 * @version 1.0
 */
public class ClienteNoEncontradoException extends Exception {
    /**
     * @param msg mensaje de error
     */
    public ClienteNoEncontradoException(String msg) {
        super(msg);
    }
}