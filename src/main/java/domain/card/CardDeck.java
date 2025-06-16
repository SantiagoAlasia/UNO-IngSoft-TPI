package domain.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa el mazo de cartas del juego UNO.
 * 
 * Esta clase se encarga de crear y almacenar las 108 cartas del mazo, siguiendo las reglas oficiales del juego.
 * Utiliza la interfaz Card y sus implementaciones (NumberCard, ActionCard, WildCard),
 * permitiendo que cada carta tenga su propio comportamiento gracias al patrón Strategy.
 * 
 * El patrón Strategy se ve reflejado en que el mazo almacena una lista de Card,
 * y cada carta puede ejecutar su propia lógica al invocar el método play(),
 * sin que el mazo necesite saber los detalles de cada tipo de carta.
 */
public class CardDeck {
    /** Lista de cartas del mazo (inmutable desde fuera de la clase). */
    private final List<Card> cards = new ArrayList<>(108);

    /**
     * Crea un mazo de cartas UNO con la composición oficial.
     */
    public CardDeck() {
        createCards();
    }

    /**
     * Devuelve una vista inmodificable de las cartas del mazo.
     */
    public List<Card> getImmutableCards() {
        return Collections.unmodifiableList(cards);
    }

    /**
     * Crea todas las cartas del mazo (números, acción y comodines).
     */
    private void createCards() {
        createNumberCards();
        createActionCards();
        createWildCards();
    }

    /**
     * Crea las cartas numéricas para cada color.
     */
    private void createNumberCards() {
        for (var color : CardColor.values()) {
            cards.add(new NumberCard(0, color));

            for (var i = 1; i <= 9; i++) {
                cards.add(new NumberCard(i, color));
                cards.add(new NumberCard(i, color));
            }
        }
    }

    /**
     * Crea las cartas de acción (Salta, Reversa, +2) para cada color.
     */
    private void createActionCards() {
        for (var color : CardColor.values()) {
            for (var i = 0; i < 2; i++) {
                cards.add(new ActionCard(CardType.SKIP, color));
                cards.add(new ActionCard(CardType.REVERSE, color));
                cards.add(new ActionCard(CardType.DRAW_TWO, color));
            }
        }
    }

    /**
     * Crea las cartas comodín (cambia color y +4).
     */
    private void createWildCards() {
        for (var i = 0; i < 4; i++) {
            cards.add(new WildCard(CardType.WILD_COLOR));
            cards.add(new WildCard(CardType.WILD_DRAW_FOUR));
        }
    }
}
