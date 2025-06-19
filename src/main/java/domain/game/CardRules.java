package domain.game;

import domain.card.*;

public class CardRules {
    // -----------------------------
    // Patrón Strategy en cartas UNO
    // -----------------------------
    // Esta clase utilitaria centraliza la lógica de validación de jugadas según
    // el tipo de carta. Cada método representa una estrategia diferente para
    // validar si una carta puede ser jugada sobre otra, según las reglas de UNO.
    //
    // Así, el comportamiento de cada tipo de carta es intercambiable y extensible,
    // cumpliendo con el patrón Strategy.

    private CardRules(){
        throw new IllegalStateException("Utility class");
    }

    public static boolean isValidNumberCard(Card topCard, NumberCard playedCard) {
        if(isSameColor(topCard, playedCard)){
            return true;
        }

        if (topCard.getType() == CardType.NUMBER) {
            return ((NumberCard) topCard).getValue() == playedCard.getValue();
        }

        return false;
    }

    public static boolean isValidActionCard(Card topCard, ActionCard playedCard) {
        if(isSameColor(topCard, playedCard)){
            return true;
        }

        return topCard.getType() == playedCard.getType();
    }

    public static boolean isValidWildCard(WildCard playedCard) {
        return playedCard.getColor() != null;
    }

    private static boolean isSameColor(Card topCard, Card playedCard) {
        return topCard.getColor() == playedCard.getColor();
    }
}
