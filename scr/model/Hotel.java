package model;

/**
 * Subclase Hotel.
 * @author Juan Cogua
 * @version 1.0
 */
public class Hotel extends Alojamiento {
    private int estrellas;
    private boolean servicioDesayuno;

    public Hotel(String codigo, String nombre, String ubicacion, double precioPorNoche, int estrellas, boolean servicioDesayuno) {
        super(codigo, nombre, ubicacion, precioPorNoche);
        this.estrellas = estrellas;
        this.servicioDesayuno = servicioDesayuno;
    }

    public int getEstrellas() { return estrellas; }
    public boolean isServicioDesayuno() { return servicioDesayuno; }

    /**
     * Calcula costo de estadía aplicando recargos por estrellas y desayuno.
     * @param dias número de noches
     * @return costo total
     * @version 1.0
     */
    @Override
    public double calcularCostoEstadia(int dias) {
        double base = precioPorNoche * dias;
        // recargo por cantidad de estrellas: 5% por estrella
        double recargoEstrellas = base * (0.05 * estrellas);
        // desayuno aumenta 10% sobre el subtotal si está activado
        double recargoDesayuno = servicioDesayuno ? base * 0.10 : 0;
        return base + recargoEstrellas + recargoDesayuno;
    }

    @Override
    public String mostrarInfo() {
        return super.mostrarInfo() + String.format(" - Hotel %d★ - Desayuno: %s", estrellas, servicioDesayuno ? "Sí" : "No");
    }

    /**
     * Representación TXT del hotel.
     * @return línea TXT
     * @version 1.0
     */
    @Override
    public String toTXT() {
        return String.join(",",
            "HOTEL",
            codigo,
            nombre,
            ubicacion,
            String.valueOf(precioPorNoche),
            String.valueOf(estrellas),
            String.valueOf(servicioDesayuno)
        );
    }
}