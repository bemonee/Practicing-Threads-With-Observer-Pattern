package entitys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * Clase que sirve para contar puntos de jugadores y persistirlos
 * @author ramiro
 */
public class Contador {

    List<Jugador> lista_jugadores;

    public Contador(List<Jugador> lista_jugadores) {
        this.lista_jugadores = lista_jugadores;
    }

    public Jugador getGanador() {
        Jugador ganador = null;
        if (this.lista_jugadores != null && !this.lista_jugadores.isEmpty()) {
            int size = this.lista_jugadores.size();
            ganador = this.lista_jugadores.get(0);
            if (size > 1) {
                int puntaje_ganador = this.contarCartas(ganador);
                for (int i = 1; i < size; i++) {
                    Jugador posible_ganador = this.lista_jugadores.get(i);
                    int puntaje_posible_ganador = this.contarCartas(posible_ganador);
                    if (puntaje_ganador < puntaje_posible_ganador) {
                        puntaje_ganador = puntaje_posible_ganador;
                        ganador = posible_ganador;
                    }
                }
            }
        }
        return ganador;
    }

    private int contarCartas(Jugador j) {
        Integer puntaje = 0;
        for (Integer carta : j.getCartas_tomadas()) {
            puntaje += carta;
        }
        return puntaje;
    }

    public int persistirGanador(Jugador j) {
        int filas_afectadas = 0;
        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost/juego_cartas?user=root&password=");
            PreparedStatement ps = conn.prepareStatement("INSERT INTO juegos(nombre_jugador, puntos) values (?, ?)");

            ps.setString(1, j.toString());
            ps.setInt(2, this.contarCartas(j));
            filas_afectadas = ps.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return filas_afectadas;
    }
}
