package model;

/**
 * Subclase Cabaña.
 * @author Juan Cogua
 * @version 1.0
 */
public class Cabana extends Alojamiento {
    private String zona;
    private int capacidadPersonas;

    public Cabana(String codigo, String nombre, String ubicacion, double precioPorNoche, String zona, int capacidadPersonas) {
        super(codigo, nombre, ubicacion, precioPorNoche);
        this.zona = zona;
        this.capacidadPersonas = capacidadPersonas;
    }

    public String getZona() { return zona; }
    public int getCapacidadPersonas() { return capacidadPersonas; }

    /**
     * Calcula costo aplicando recargos según capacidad y zona.
     * @param dias número de noches
     * @return costo total
     * @version 1.0
     */
    @Override
    public double calcularCostoEstadia(int dias) {
        double base = precioPorNoche * dias;
        // si la cabaña tiene alta capacidad, se aplica un recargo del 20%
        if (capacidadPersonas > 6) base *= 1.20;
        // si la zona es "premium" (ejemplo), se añade 10%
        if (zona != null && zona.toLowerCase().contains("premium")) base *= 1.10;
        return base;
    }

    @Override
    public String mostrarInfo() {
        return super.mostrarInfo() + String.format(" - Cabaña zona %s - Capacidad: %d", zona, capacidadPersonas);
    }

    /**
     * Representación TXT de la cabaña.
     * @return línea TXT
     * @version 1.0
     */
    @Override
    public String toTXT() {
        return String.join(",",
            "CABANA",
            codigo,
            nombre,
            ubicacion,
            String.valueOf(precioPorNoche),
            zona,
            String.valueOf(capacidadPersonas)
        );
    }
}