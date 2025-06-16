package domain.card;

import domain.game.GameContext;

/**
 * Clase abstracta base para todas las cartas del juego UNO.
 * 
 * Esta clase implementa la interfaz Card y provee la lógica común para todas las cartas,
 * como el tipo y el color. Además, fuerza a las subclases a definir el método play(),
 * que es el punto central del patrón Strategy en este diseño.
 * 
 * El patrón Strategy se aplica aquí porque cada subclase (NumberCard, ActionCard, WildCard)
 * implementa su propia versión de play(), permitiendo que el comportamiento de la carta
 * sea intercambiable y extensible sin modificar el código cliente ni la clase base.
 * 
 * Clase abstracta base para todas las cartas UNO.
 * 
 * Implementa la interfaz Card y fuerza a las subclases a definir su propia estrategia (play).
 * Así, cada carta puede modificar el estado del juego de manera diferente (Strategy).
 */
public abstract class AbstractCard implements Card {
    /** Tipo de carta (número, acción, comodín, etc). */
    private final CardType type;
    /** Color de la carta (puede ser null para comodines). */
    private final CardColor color;

    /**
     * Constructor protegido para inicializar tipo y color.
     */
    protected AbstractCard(CardType type, CardColor color) {
        this.type = type;
        this.color = color;
    }

    @Override
    public CardType getType() {
        return type;
    }

    @Override
    public CardColor getColor() {
        return color;
    }

    /**
     * Método abstracto que representa la estrategia de la carta.
     * Cada subclase debe implementar su propio comportamiento.
     */
    @Override
    public abstract void play(GameContext context); // Ahora recibe el contexto de juego // Estrategia a implementar por cada carta

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}
