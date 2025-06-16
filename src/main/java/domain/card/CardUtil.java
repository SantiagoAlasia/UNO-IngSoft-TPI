package domain.card;

/**
 * Clase utilitaria para validaciones y operaciones comunes sobre cartas UNO.
 * 
 * Aunque esta clase no implementa el patrón Strategy directamente,
 * es fundamental para garantizar la validez de los objetos que sí lo aplican (NumberCard, ActionCard, WildCard).
 * 
 * El patrón Strategy se ve reforzado aquí porque CardUtil permite validar y distinguir entre los distintos tipos de cartas,
 * facilitando que cada implementación de Card (la estrategia) sea correcta y segura.
 */
public class CardUtil {
    /**
     * Constructor privado para evitar instanciación.
     */
    private CardUtil() {
    }

    /**
     * Valida que el color de la carta no sea nulo.
     */
    public static void validateColor(CardColor color) {
        if (color == null) {
            throw new IllegalArgumentException("Card color should be defined");
        }
    }

    /**
     * Valida que el número de la carta esté entre 0 y 9.
     */
    public static void validateNumber(int number) {
        if (number < 0 || number > 9) {
            throw new IllegalArgumentException("Card number should between 0 and 9");
        }
    }

    /**
     * Valida que el tipo de carta de acción sea válido.
     */
    public static void validateActionType(CardType type) {
        if (type == CardType.SKIP || type == CardType.REVERSE || type == CardType.DRAW_TWO) {
            return;
        }

        throw new IllegalArgumentException("Invalid action type");
    }

    /**
     * Determina si una carta es comodín.
     */
    public static boolean isWildCard(Card card) {
        return card.getType() == CardType.WILD_COLOR || card.getType() == CardType.WILD_DRAW_FOUR;
    }
}
