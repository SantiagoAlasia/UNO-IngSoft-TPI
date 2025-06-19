package domain.card;

import domain.testhelper.CardTestFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test unitarios para los métodos utilitarios relacionados con las cartas UNO.
 * Se valida la correcta identificación de cartas comodín (wild) y el uso del patrón Strategy
 * para distinguir comportamientos según el tipo de carta.
 *
 * Casos cubiertos:
 * - Identificación de cartas Wild Color y Wild Draw Four como comodines
 * - Confirmación de que una carta numérica no es comodín
 */
class TestCardUtil {
    /**
     * Verifica que una carta Wild Color es correctamente identificada como comodín.
     */
    @Test
    void GivenWildColorCard_ShouldBeWildCard() {
        var result = CardUtil.isWildCard(CardTestFactory.createWildColorCard());

        assertTrue(result);
    }

    /**
     * Verifica que una carta Wild Draw Four es correctamente identificada como comodín.
     */
    @Test
    void GivenWildDrawFourCard_ShouldBeWildCard() {
        var result = CardUtil.isWildCard(CardTestFactory.createWildDrawFourCard());

        assertTrue(result);
    }

    /**
     * Verifica que una carta numérica no es identificada como comodín.
     */
    @Test
    void GivenNumberCard_ShouldNotBeWildCard() {
        var result = CardUtil.isWildCard(CardTestFactory.createNumberCard());

        assertFalse(result);
    }
}
