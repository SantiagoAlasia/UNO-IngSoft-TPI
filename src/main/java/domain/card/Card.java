package domain.card;

import java.io.Serializable;

import domain.game.GameContext;

/**
 * La interfaz principal para las cartas del juego UNO.
 * 
 * Esta interfaz define el contrato para todas las cartas, y es el punto central para aplicar el patrón Strategy.
 * Cada tipo de carta (número, acción, comodín) implementa esta interfaz y define su propio comportamiento en el método play().
 * De esta manera, el comportamiento de cada carta puede variar independientemente, cumpliendo con el patrón Strategy.
 */
public interface Card extends Serializable {
    /**
     * Devuelve el tipo de carta (número, acción, comodín, etc).
     */
    CardType getType();

    /**
     * Devuelve el color de la carta (rojo, azul, verde, amarillo, o null para comodines).
     */
    CardColor getColor();

    /**
     * Método que representa la estrategia de la carta.
     * Cada implementación define su propio comportamiento al ser jugada.
     * Esto es el núcleo del patrón Strategy: el algoritmo (comportamiento) se delega a la subclase concreta.
     * 
     * Ejecuta la estrategia de la carta, modificando el estado del juego según corresponda.
     */
    void play(GameContext context);
}
