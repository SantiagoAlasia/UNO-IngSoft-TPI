package domain.card;

import java.util.List;
import java.util.stream.Stream;

/**
 * Clase utilitaria para ayudar en los tests a contar la cantidad de cartas de cada tipo y color en una colección.
 * Permite validar que el mazo de UNO contiene la cantidad correcta de cartas según las reglas del juego.
 * 
 * Uso típico: se utiliza en tests de integridad del mazo y de barajado para asegurar que no falten ni sobren cartas.
 */
public class CardCounter {
    private final List<Card> cards;

    /**
     * Crea un contador para una lista de cartas.
     * @param cards lista de cartas a analizar
     */
    public CardCounter(List<Card> cards) {
        this.cards = cards;
    }

    /**
     * Devuelve un arreglo con la cantidad de cartas numéricas (0-9) presentes.
     * El índice representa el valor de la carta.
     */
    public int[] getNumberCounts() {
        var numberCounts = new int[10];

        getNumberStream()
            .forEach(c -> numberCounts[c.getValue()] += 1);

        return numberCounts;
    }

    /**
     * Devuelve un arreglo con la cantidad de cartas numéricas por color (rojo, verde, azul, amarillo).
     * El índice representa el ordinal del color.
     */
    public int[] getNumberColorCounts() {
        var colorCounts = new int[4];

        getNumberStream()
            .forEach(c -> colorCounts[c.getColor().ordinal()] += 1);

        return colorCounts;
    }

    /**
     * Devuelve un stream de cartas numéricas para facilitar el conteo y filtrado.
     */
    private Stream<NumberCard> getNumberStream() {
        return cards.stream()
            .filter(c -> c.getType() == CardType.NUMBER)
            .map(c -> (NumberCard) c);
    }

    /**
     * Devuelve un arreglo con la cantidad de cartas SKIP por color.
     */
    public int[] getSkipColorCounts() {
        var colorCounts = new int[4];

        cards.stream()
            .filter(c -> c.getType() == CardType.SKIP)
            .map(Card::getColor)
            .forEach(color -> colorCounts[color.ordinal()] += 1);

        return colorCounts;
    }

    /**
     * Devuelve un arreglo con la cantidad de cartas REVERSE por color.
     */
    public int[] getReverseColorCounts() {
        var colorCounts = new int[4];

        cards.stream()
            .filter(c -> c.getType() == CardType.REVERSE)
            .map(Card::getColor)
            .forEach(color -> colorCounts[color.ordinal()] += 1);

        return colorCounts;
    }

    /**
     * Devuelve un arreglo con la cantidad de cartas DRAW_TWO (+2) por color.
     */
    public int[] getDrawTwoColorCounts() {
        var colorCounts = new int[4];

        cards.stream()
            .filter(c -> c.getType() == CardType.DRAW_TWO)
            .map(Card::getColor)
            .forEach(color -> colorCounts[color.ordinal()] += 1);

        return colorCounts;
    }

    /**
     * Devuelve la cantidad de cartas comodín (WILD_COLOR o WILD_DRAW_FOUR) según el tipo indicado.
     * @param type tipo de carta comodín a contar
     */
    public long getWildCardCounts(CardType type) {
        return cards.stream()
            .filter(c -> c.getType() == type)
            .count();
    }
}
