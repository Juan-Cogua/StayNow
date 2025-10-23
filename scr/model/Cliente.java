package model;

/**
 * Clase Cliente.
 * @author Juan Cogua
 * @version 1.0
 */
public class Cliente {
    private String id;
    private String nombre;
    private String email;

    public Cliente(String id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }

    public String mostrarInfo() {
        return String.format("Cliente %s - %s (%s)", id, nombre, email);
    }

    /**
     * Representación TXT del cliente.
     * @return línea TXT con id|nombre|email
     * @version 1.0
     */

    public String toTXT() {
        return String.join(",", id, nombre, email);
    }
}