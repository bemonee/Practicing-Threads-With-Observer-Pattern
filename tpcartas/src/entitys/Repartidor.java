package entitys;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ramir
 */
public class Repartidor extends Thread implements Observer {

    private final List<Integer> cartas;

    private final Mesa mesa;

    private final Random rand;

    public Repartidor(Mesa mesa) {
        this.cartas = new ArrayList<>();
        this.inicializarMazo();
        this.mesa = mesa;
        this.rand = new Random();
    }

    private void inicializarMazo() {
        for (int i = 1; i <= 12; i++) {
            for (int j = 0; j < 4; j++) {
                this.cartas.add(i);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Jugador) {
            Jugador jugador = (Jugador) o;
            System.out.println("El jugador " + jugador.getId_jugador() + " tomo la carta " + arg);
        }
    }

    @Override
    public void run() {
        while (this.cartas.size() > 0) {
            try {
                this.ponerCartaEnMesa();
            } catch (InterruptedException ex) {
                Logger.getLogger(Repartidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void ponerCartaEnMesa() throws InterruptedException {
        synchronized (this.mesa) {
            while (this.mesa.getCarta_en_mesa() != null) {
                this.mesa.wait();
            }
            
            Integer random = this.rand.nextInt(this.cartas.size());
            Integer carta = this.cartas.get(random);
            this.cartas.remove(carta);
            this.mesa.setCarta_en_mesa(carta);
            this.mesa.notifyAll();
        }
    }
}
