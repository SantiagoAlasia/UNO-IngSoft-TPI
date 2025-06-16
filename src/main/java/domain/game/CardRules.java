package domain.game;

import domain.card.*;

/**
 * Clase utilitaria para validar las reglas de juego de las cartas UNO.
 * 
 * Aunque no implementa el patrón Strategy directamente, CardRules es fundamental para determinar
 * si una carta puede ser jugada según el estado actual del juego.
 * 
 * El patrón Strategy se ve reforzado aquí porque permite que cada tipo de carta tenga su propia lógica
 * de validación y jugada, y CardRules ayuda a centralizar y reutilizar esas reglas en el flujo del juego.
 */
public class CardRules {
    /**
     * Constructor privado para evitar instanciación.
     */
    private CardRules(){
        throw new IllegalStateException("Utility class");
    }

    /**
     * Valida si una carta numérica puede ser jugada sobre la carta superior.
     */
    public static boolean isValidNumberCard(Card topCard, NumberCard playedCard) {
        if(isSameColor(topCard, playedCard)){
            return true;
        }

        if (topCard.getType() == CardType.NUMBER) {
            return ((NumberCard) topCard).getValue() == playedCard.getValue();
        }

        return false;
    }

    /**
     * Valida si una carta de acción puede ser jugada sobre la carta superior.
     */
    public static boolean isValidActionCard(Card topCard, ActionCard playedCard) {
        if(isSameColor(topCard, playedCard)){
            return true;
        }

        return topCard.getType() == playedCard.getType();
    }

    /**
     * Valida si una carta comodín es jugable (debe tener color asignado).
     */
    public static boolean isValidWildCard(WildCard playedCard) {
        return playedCard.getColor() != null;
    }

    /**
     * Verifica si dos cartas tienen el mismo color.
     */
    private static boolean isSameColor(Card topCard, Card playedCard) {
        return topCard.getColor() == playedCard.getColor();
    }
}
