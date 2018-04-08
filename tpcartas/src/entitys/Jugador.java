package entitys;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que sirve para tomar casas de una Mesa
 * @author ramiro
 */
public class Jugador extends Observable implements Runnable {
    
    private final List<Integer> cartas_tomadas;
    private final Mesa mesa;

    public static Integer JUGADORES = 0;
    private final int id_jugador;
    private volatile boolean shutdown;

    public Jugador(Mesa mesa) {
        this.cartas_tomadas = new ArrayList<>();
        this.mesa = mesa;
        this.id_jugador = ++Jugador.JUGADORES;
        this.shutdown = false;
    }

    @Override
    public void run() {
        while (!this.shutdown) {
            try {
                this.tomarCartaDeLaMesa();
            } catch (InterruptedException ex) {
                Logger.getLogger(Repartidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void tomarCartaDeLaMesa() throws InterruptedException {
        synchronized (this.mesa) {
            while (this.mesa.getCarta_en_mesa() == null) {
                this.mesa.wait();
            }
            
            Integer carta = mesa.getCarta_en_mesa();
            this.cartas_tomadas.add(carta);
            this.setChanged();
            this.notifyObservers(carta);
            this.mesa.setCarta_en_mesa(null);
            this.mesa.notifyAll();
        }
    }
    
    public void shutdown(){
        this.shutdown = true;
    }

    public Integer getId_jugador() {
        return id_jugador;
    }

    public List<Integer> getCartas_tomadas() {
        return cartas_tomadas;
    }

    @Override
    public String toString() {
        return "Jugador "+ this.id_jugador;
    }

}
