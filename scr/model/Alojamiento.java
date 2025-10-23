package model;

import java.util.Objects;

/**
 * Clase abstracta Alojamiento que define los atributos y comportamiento base.
 * @author Juan Cogua
 * @version 1.0
 */
public abstract class Alojamiento {
    protected String codigo;
    protected String nombre;
    protected String ubicacion;
    protected double precioPorNoche;

    public Alojamiento(String codigo, String nombre, String ubicacion, double precioPorNoche) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.precioPorNoche = precioPorNoche;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getUbicacion() { return ubicacion; }
    public double getPrecioPorNoche() { return precioPorNoche; }

    /**
     * Calcula el costo total de la estadía (implementación polimórfica en subclases).
     * @param dias días de la estadía
     * @return costo total
     * @version 1.0
     */
    public abstract double calcularCostoEstadia(int dias);

    /**
     * Muestra info básica del alojamiento.
     */
    public String mostrarInfo() {
        return String.format("%s (%s) - %s - %.2f por noche", nombre, codigo, ubicacion, precioPorNoche);
    }

    /**
     * Representación en texto para persistencia.
     * @return línea TXT con los campos del alojamiento
     * @version 1.0
     */
    public abstract String toTXT();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Alojamiento)) return false;
        Alojamiento that = (Alojamiento) o;
        return Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}