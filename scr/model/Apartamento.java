package model;

/**
 * Subclase Apartamento.
 * @author Juan Cogua
 * @version 1.0
 */
public class Apartamento extends Alojamiento {
    private int numHabitaciones;
    private boolean tieneParqueadero;

    public Apartamento(String codigo, String nombre, String ubicacion, double precioPorNoche, int numHabitaciones, boolean tieneParqueadero) {
        super(codigo, nombre, ubicacion, precioPorNoche);
        this.numHabitaciones = numHabitaciones;
        this.tieneParqueadero = tieneParqueadero;
    }

    public int getNumHabitaciones() { return numHabitaciones; }
    public boolean isTieneParqueadero() { return tieneParqueadero; }

    /**
     * Calcula costo aplicando descuento por estadía larga y cargo por parqueadero.
     * @param dias número de noches
     * @return costo total
     * @version 1.0
     */
    @Override
    public double calcularCostoEstadia(int dias) {
        double base = precioPorNoche * dias;
        // descuento por estadías largas (>7 días): 10%
        if (dias > 7) {
            base *= 0.90;
        }
        // cargo por parqueadero fijo por estadía
        if (tieneParqueadero) base += 15.0;
        return base;
    }

    @Override
    public String mostrarInfo() {
        return super.mostrarInfo() + String.format(" - Apartamento %d hab - Parqueadero: %s", numHabitaciones, tieneParqueadero ? "Sí" : "No");
    }

    /**
     * Representación TXT del apartamento.
     * @return línea TXT
     * @version 1.0
     */
    @Override
    public String toTXT() {
        return String.join("|",
            "APARTAMENTO",
            codigo,
            nombre,
            ubicacion,
            String.valueOf(precioPorNoche),
            String.valueOf(numHabitaciones),
            String.valueOf(tieneParqueadero)
        );
    }
}