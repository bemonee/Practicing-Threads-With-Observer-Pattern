package tpcartas;

import entitys.Contador;
import entitys.Jugador;
import entitys.Mesa;
import entitys.Repartidor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ramiro
 */
public class Tpcartas {

    public static int CANT_JUGADORES = 4;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {

        Mesa mesa = new Mesa();
        Repartidor repartidor = new Repartidor(mesa);
        List<Jugador> lista_jugadores = new ArrayList<>();

        for (int i = 0; i < CANT_JUGADORES; i++) {
            Jugador jugador = new Jugador(mesa);
            jugador.addObserver(repartidor);
            lista_jugadores.add(jugador);
        }

        for (int i = 0; i < CANT_JUGADORES; i++) {
            Thread thread_jugador = new Thread(lista_jugadores.get(i));
            thread_jugador.start();
        }

        repartidor.start();
        repartidor.join();

        
        for (int i = 0; i < CANT_JUGADORES; i++) {
            lista_jugadores.get(i).shutdown();      
        }

        Contador contador = new Contador(lista_jugadores);
        Jugador ganador = contador.getGanador();
        if (ganador != null) {
            if (contador.persistirGanador(ganador) > 0) {
                System.out.println("Juego Terminado - Ganador: " + ganador.toString());
            }
        }
        
        System.exit(0);
    }

}
