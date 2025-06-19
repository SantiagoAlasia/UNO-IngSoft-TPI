package domain.card;

import domain.testhelper.CardTestFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitarios para la lógica de igualdad e identidad entre cartas UNO.
 * Estos tests validan el comportamiento polimórfico de las cartas (Strategy),
 * asegurando que la comparación entre cartas de diferentes tipos y colores sea correcta.
 *
 * Casos cubiertos:
 * - Igualdad entre cartas del mismo tipo y valor
 * - Diferencia entre cartas de distinto tipo o color
 * - Comportamiento de cartas especiales (Skip, Reverse, Wild, etc.)
 */
class TestCard {
    /**
     * Verifica que dos cartas numéricas con el mismo valor y color son iguales,
     * aunque sean instancias diferentes.
     */
    @Test
    void GivenSameNumberCardValues_ShouldBeEqual() {
        var numberCard = new NumberCard(1, CardColor.RED);
        var anotherNumberCard = new NumberCard(1, CardColor.RED);

        assertNotSame(numberCard, anotherNumberCard);
        assertEquals(numberCard, anotherNumberCard);
    }

    /**
     * Verifica que dos cartas numéricas con el mismo valor pero distinto color no son iguales.
     */
    @Test
    void GivenDifferentNumberCardValues_ShouldNotBeEqual() {
        var numberCard = new NumberCard(1, CardColor.GREEN);
        var anotherNumberCard = new NumberCard(1, CardColor.RED);

        assertNotEquals(numberCard, anotherNumberCard);
    }

    /**
     * Verifica que dos cartas SKIP del mismo color son iguales.
     */
    @Test
    void GivenSameSkipCard_ShouldBeEqual() {
        var skipCard = CardTestFactory.createSkipCard(CardColor.RED);
        var anotherSkipCard = CardTestFactory.createSkipCard(CardColor.RED);

        assertEquals(skipCard, anotherSkipCard);
    }

    /**
     * Verifica que dos cartas SKIP de distinto color no son iguales.
     */
    @Test
    void GivenDifferentSkipCard_ShouldNotBeEqual() {
        var skipCard = CardTestFactory.createSkipCard(CardColor.RED);
        var anotherSkipCard = CardTestFactory.createSkipCard(CardColor.GREEN);

        assertNotEquals(skipCard, anotherSkipCard);
    }

    /**
     * Verifica que cartas de acción diferentes (SKIP vs REVERSE) no son iguales aunque tengan el mismo color.
     */
    @Test
    void GivenDifferentActionCard_ShouldNotBeEqual() {
        var skipCard = CardTestFactory.createSkipCard(CardColor.RED);
        var reverseCard = CardTestFactory.createReverseCard(CardColor.RED);

        assertNotEquals(skipCard, reverseCard);
    }

    /**
     * Verifica que dos cartas Wild Color (comodín) son iguales.
     */
    @Test
    void GivenSameWildColorCard_ShouldBeEqual() {
        var card = CardTestFactory.createWildColorCard();
        var anotherCard = CardTestFactory.createWildColorCard();

        assertEquals(card, anotherCard);
    }

    /**
     * Verifica que dos cartas Wild Draw Four (comodín +4) son iguales.
     */
    @Test
    void GivenSameWildDrawFourCard_ShouldBeEqual() {
        var card = CardTestFactory.createWildDrawFourCard();
        var anotherCard = CardTestFactory.createWildDrawFourCard();

        assertEquals(card, anotherCard);
    }
}
