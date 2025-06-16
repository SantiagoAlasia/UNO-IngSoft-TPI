package domain.card;

import java.util.Objects;
import domain.game.GameContext;

/**
 * Representa una carta numérica en el juego UNO.
 * 
 * Esta clase extiende AbstractCard y define el comportamiento específico de una carta de número
 * sobrescribiendo el método play(). Aquí se aplica el patrón Strategy, ya que cada tipo de carta
 * (número, acción, comodín) implementa su propia lógica en play(), permitiendo que el comportamiento
 * de la carta sea intercambiable y extensible sin modificar el código cliente.
 */
public class NumberCard extends AbstractCard {
    /** Valor numérico de la carta (0-9). */
    private final int value;

    /**
     * Crea una carta numérica con el valor y color especificados.
     */
    public NumberCard(int value, CardColor color) {
        super(CardType.NUMBER, color);

        CardUtil.validateColor(color);

        CardUtil.validateNumber(value);
        this.value = value;
    }

    /**
     * Devuelve el valor numérico de la carta.
     */
    public int getValue() {
        return value;
    }

    /**
     * Implementación de la estrategia para una carta numérica.
     * Aquí se definiría la lógica concreta de lo que ocurre al jugar esta carta.
     */
    @Override
    public void play(GameContext context) {
        // Ejemplo: agregar la carta a la pila de descarte
        context.addToDiscardPile(this);
        System.out.println("Playing NumberCard: " + value + ", color: " + getColor());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberCard that = (NumberCard) o;
        return value == that.value && getColor() == that.getColor();
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, getColor());
    }

    @Override
    public String toString() {
        return "NumberCard{" +
            value + ", " + getColor() +
            '}';
    }
}
