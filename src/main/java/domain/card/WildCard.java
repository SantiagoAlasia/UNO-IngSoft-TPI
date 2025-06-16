package domain.card;

import java.util.Objects;
import domain.game.GameContext;

/**
 * Representa una carta comodín en el juego UNO (por ejemplo: Cambia color, +4).
 * 
 * Esta clase extiende AbstractCard y define el comportamiento específico de una carta comodín
 * sobrescribiendo el método play(). Aquí se aplica el patrón Strategy, ya que cada tipo de carta
 * (número, acción, comodín) implementa su propia lógica en play(), permitiendo que el comportamiento
 * de la carta sea intercambiable y extensible sin modificar el código cliente.
 * 
 * Implementa su propia estrategia en play(GameContext), cumpliendo el patrón Strategy.
 */
public class WildCard extends AbstractCard {
    /**
     * Crea una carta comodín con el tipo especificado (sin color inicial).
     */
    public WildCard(CardType type) {
        super(type, null);
    }

    /**
     * Crea una carta comodín con el tipo y color especificados.
     */
    public WildCard(CardType type, CardColor color) {
        super(type, color);
        CardUtil.validateColor(color);
    }

    /**
     * Implementación de la estrategia para una carta comodín.
     * Aquí se definiría la lógica concreta de lo que ocurre al jugar esta carta.
     */
    @Override
    public void play(GameContext context) {
        // Ejemplo: agregar la carta a la pila de descarte
        context.addToDiscardPile(this);
        System.out.println("Playing WildCard: " + getType() + ", color: " + getColor());
        // Aquí podrías permitir al jugador elegir color, etc.
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WildCard wildCard = (WildCard) o;
        return getType() == wildCard.getType() && getColor() == wildCard.getColor();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getColor());
    }

    @Override
    public String toString() {
        return "WildCard{" +
            getType() + ", " + getColor() +
            '}';
    }
}
