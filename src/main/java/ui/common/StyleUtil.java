package ui.common;

import java.awt.*;
import domain.card.Card;
import domain.card.CardColor;
import domain.card.NumberCard;

public class StyleUtil {
    private StyleUtil() {
    }

    public static final Color GREEN = new Color(0, 153, 0);
    public static final Color RED = new Color(192, 80, 77);
    public static final Color BLUE = new Color(31, 73, 125);
    public static final Color YELLOW = new Color(255, 204, 0);
    public static final Color BLACK = new Color(0, 0, 0);

    public static Color convertCardColor(CardColor color) {
        if (color == null) return BLACK;
        switch (color) {
            case RED:
                return RED;
            case GREEN:
                return GREEN;
            case BLUE:
                return BLUE;
            case YELLOW:
                return YELLOW;
            default:
                return BLACK;
        }
    }

    public static final String DEFAULT_FONT = "Helvetica";

    private static final Character REVERSE_CHAR = (char) 8634;
    private static final Character SKIP_CHAR = (char) Integer.parseInt("2718", 16);

    public static String getValueToDisplay(Card card) {
        switch (card.getType()) {
            case NUMBER:
                return Integer.toString(((NumberCard) card).getValue());
            case SKIP:
                return SKIP_CHAR.toString();
            case REVERSE:
                return REVERSE_CHAR.toString();
            case DRAW_TWO:
                return "2+";
            case WILD_COLOR:
                return "W";
            case WILD_DRAW_FOUR:
                return "4+";
            default:
                throw new IllegalArgumentException("Unsupported card type " + card.getType());
        }
    }
}
