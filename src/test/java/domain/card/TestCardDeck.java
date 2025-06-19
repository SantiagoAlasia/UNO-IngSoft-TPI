package domain.card;

/**
 * Test de la composición e inmutabilidad del mazo de cartas de UNO.
 * Verifica la correcta inclusión de cartas con diferentes estrategias (Strategy).
 * Cada test valida una propiedad fundamental del mazo inicial de UNO:
 * - Inmutabilidad (no se puede modificar)
 * - Cantidad total de cartas
 * - Cantidad de cartas por tipo y color
 * Esto asegura que el mazo cumple con las reglas oficiales del juego.
 */

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestCardDeck {

    private CardDeck testee;

    @BeforeEach
    private void setup() {
        testee = new CardDeck();
    }

    /**
     * Verifica que la lista de cartas del mazo es inmutable (no se puede modificar después de creada).
     * Esto previene errores por manipulación accidental del mazo durante el juego.
     */
    @Test
    void WhenInitialized_ShouldBeImmutable() {
        var cards = testee.getImmutableCards();

        Assertions.assertThrows(UnsupportedOperationException.class, () -> cards.remove(0));
    }

    /**
     * Verifica que el mazo inicial contiene exactamente 108 cartas, según las reglas de UNO.
     */
    @Test
    void WhenInitialized_ShouldHave108Cards() {
        assertEquals(108, testee.getImmutableCards().size());
    }

    /**
     * Verifica que el mazo contiene 76 cartas numéricas (0-9 de cada color, con las repeticiones correctas).
     */
    @Test
    void WhenInitialized_ShouldHave76NumberCards() {
        var cards = testee.getImmutableCards();

        CardCounterAssertionHelper.assertNumberCards(cards);
    }

    /**
     * Verifica que el mazo contiene 8 cartas SKIP (2 de cada color).
     */
    @Test
    void WhenInitialized_ShouldHave8SkipCards() {
        var cards = testee.getImmutableCards();

        CardCounterAssertionHelper.assertSkipCards(cards);
    }

    /**
     * Verifica que el mazo contiene 8 cartas REVERSE (2 de cada color).
     */
    @Test
    void WhenInitialized_ShouldHave8ReverseCards() {
        var cards = testee.getImmutableCards();

        CardCounterAssertionHelper.assertReverseCards(cards);
    }

    /**
     * Verifica que el mazo contiene 8 cartas DRAW TWO (+2, 2 de cada color).
     */
    @Test
    void WhenInitialized_ShouldHave8DrawTwoCards() {
        var cards = testee.getImmutableCards();

        CardCounterAssertionHelper.assertDrawTwoCards(cards);
    }

    /**
     * Verifica que el mazo contiene 8 cartas comodín (4 Wild Color y 4 Wild Draw Four).
     */
    @Test
    void WhenInitialized_ShouldHave8WildCards() {
        var cards = testee.getImmutableCards();

        CardCounterAssertionHelper.assertWildCards(cards);
    }
}