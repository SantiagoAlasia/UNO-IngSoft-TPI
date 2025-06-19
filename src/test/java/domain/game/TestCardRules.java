package domain.game;

/**
 * Test unitarios de la lógica de validación de jugadas según las reglas de UNO.
 * Valida el uso del patrón Strategy aplicado a las cartas (cada tipo de carta tiene su propia lógica de jugada).
 * 
 * Casos cubiertos:
 * - Validación de jugadas válidas e inválidas para cartas numéricas y de acción
 * - Validación de cartas comodín con y sin color elegido
 * - Cobertura de combinaciones de cartas según las reglas oficiales
 */

import domain.card.ActionCard;
import domain.card.Card;
import domain.card.CardColor;
import domain.card.CardType;
import domain.testhelper.CardTestFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestCardRules {
    /**
     * Verifica que una carta numérica puede ser jugada sobre otra carta válida según las reglas de UNO.
     * Se prueban combinaciones de valor y color, así como cartas especiales compatibles.
     */
    @ParameterizedTest
    @MethodSource("provideValidTopCardsForNumberCard")
    void WhenNumberCardPlayed_ShouldBeValid(Card topCard) {
        var cardToPlay = CardTestFactory.createNumberCard(5, CardColor.RED);

        var result = CardRules.isValidNumberCard(topCard, cardToPlay);

        assertTrue(result, createTestMessage(topCard, cardToPlay));
    }

    private static Stream<Arguments> provideValidTopCardsForNumberCard() {
        return Stream.of(
            Arguments.of(CardTestFactory.createNumberCard(5, CardColor.RED)),
            Arguments.of(CardTestFactory.createNumberCard(4, CardColor.RED)),
            Arguments.of(CardTestFactory.createNumberCard(5, CardColor.BLUE)),
            Arguments.of(CardTestFactory.createSkipCard(CardColor.RED)),
            Arguments.of(CardTestFactory.createReverseCard(CardColor.RED)),
            Arguments.of(CardTestFactory.createDrawTwoCard(CardColor.RED)),
            Arguments.of(CardTestFactory.createWildColorCard(CardColor.RED)),
            Arguments.of(CardTestFactory.createWildDrawFourCard(CardColor.RED))
        );
    }

    /**
     * Verifica que una carta numérica NO puede ser jugada sobre cartas incompatibles.
     */
    @ParameterizedTest
    @MethodSource("provideInvalidTopCardsForNumberCard")
    void WhenMisMatchNumberCardPlayed_ShouldBeInvalid(Card topCard) {
        var cardToPlay = CardTestFactory.createNumberCard(5, CardColor.RED);

        var result = CardRules.isValidNumberCard(topCard, cardToPlay);

        assertFalse(result, createTestMessage(topCard, cardToPlay));
    }

    private static Stream<Arguments> provideInvalidTopCardsForNumberCard() {
        return Stream.of(
            Arguments.of(CardTestFactory.createNumberCard(4, CardColor.BLUE)),
            Arguments.of(CardTestFactory.createSkipCard(CardColor.BLUE)),
            Arguments.of(CardTestFactory.createReverseCard(CardColor.BLUE)),
            Arguments.of(CardTestFactory.createDrawTwoCard(CardColor.BLUE)),
            Arguments.of(CardTestFactory.createWildColorCard(CardColor.BLUE)),
            Arguments.of(CardTestFactory.createWildDrawFourCard(CardColor.BLUE)),
            Arguments.of(CardTestFactory.createWildColorCard()),
            Arguments.of(CardTestFactory.createWildColorCard(CardColor.BLUE)),
            Arguments.of(CardTestFactory.createWildDrawFourCard()),
            Arguments.of(CardTestFactory.createWildDrawFourCard(CardColor.BLUE))
        );
    }

    /**
     * Verifica que una carta de acción (Skip, Reverse, Draw Two) puede ser jugada sobre cartas compatibles.
     */
    @ParameterizedTest
    @MethodSource("provideValidTopCardsForActionCard")
    void WhenActionCardPlayed_ShouldBeValid(Card cardToPlay, Card topCard) {
        var result = CardRules.isValidActionCard(topCard, (ActionCard) cardToPlay);

        assertTrue(result, createTestMessage(topCard, cardToPlay));
    }

    private static Stream<Arguments> provideValidTopCardsForActionCard() {
        var arguments = new ArrayList<Arguments>();
        var actionTypes = new CardType[]{CardType.SKIP, CardType.REVERSE, CardType.DRAW_TWO};

        for (var action : actionTypes) {
            var cardToPlay = CardTestFactory.createActionCard(action, CardColor.YELLOW);

            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createActionCard(action, CardColor.RED)));
            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createNumberCard(5, CardColor.YELLOW)));
            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createSkipCard(CardColor.YELLOW)));
            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createReverseCard(CardColor.YELLOW)));
            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createDrawTwoCard(CardColor.YELLOW)));
            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createWildColorCard(CardColor.YELLOW)));
            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createWildDrawFourCard(CardColor.YELLOW)));
        }

        return arguments.stream();
    }

    /**
     * Verifica que una carta de acción NO puede ser jugada sobre cartas incompatibles.
     */
    @ParameterizedTest
    @MethodSource("provideInvalidTopCardsForActionCard")
    void WhenMismatchActionCardPlayed_ShouldBeInvalid(Card cardToPlay, Card topCard) {
        var result = CardRules.isValidActionCard(topCard, (ActionCard) cardToPlay);

        assertFalse(result, createTestMessage(topCard, cardToPlay));
    }

    private static Stream<Arguments> provideInvalidTopCardsForActionCard() {
        var arguments = new ArrayList<Arguments>();
        var actionTypes = new CardType[]{CardType.SKIP, CardType.REVERSE, CardType.DRAW_TWO};

        for (var action : actionTypes) {
            var cardToPlay = CardTestFactory.createActionCard(action, CardColor.YELLOW);

            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createNumberCard(5, CardColor.BLUE)));

            for (var otherAction : actionTypes) {
                if (otherAction != action) {
                    arguments.add(Arguments.of(cardToPlay, CardTestFactory.createActionCard(otherAction, CardColor.BLUE)));
                }
            }

            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createWildColorCard()));
            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createWildColorCard(CardColor.BLUE)));
            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createWildDrawFourCard()));
            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createWildDrawFourCard(CardColor.BLUE)));
        }

        return arguments.stream();
    }

    /**
     * Verifica que una carta comodín con color elegido es válida para jugar.
     */
    @Test
    void WhenWildCardWithChosenColor_ShouldBeValid() {
        var wildCard = CardTestFactory.createWildColorCard(CardColor.RED);

        var result = CardRules.isValidWildCard(wildCard);

        assertTrue(result, wildCard.toString());
    }

    /**
     * Verifica que una carta comodín sin color elegido NO es válida para jugar.
     */
    @Test
    void WhenWildCardWithoutColor_ShouldBeInvalid() {
        var wildCard = CardTestFactory.createWildColorCard();

        var result = CardRules.isValidWildCard(wildCard);

        assertFalse(result, wildCard.toString());
    }

    private String createTestMessage(Card topCard, Card playedCard) {
        return String.format("Top Card %s, Played Card %s", topCard, playedCard);
    }
}
