package domain.card;

import java.io.Serializable;

/**
 * -----------------------------
 * Patrón Strategy en cartas UNO
 * -----------------------------
 * Esta interfaz define el contrato común para todas las cartas del juego UNO.
 * Permite que el sistema trate todas las cartas de forma polimórfica, pero
 * cada implementación concreta (ActionCard, WildCard, NumberCard) puede tener
 * su propia lógica de validación y acción.
 */
public interface Card extends Serializable {
    CardType getType();
    CardColor getColor();
}
