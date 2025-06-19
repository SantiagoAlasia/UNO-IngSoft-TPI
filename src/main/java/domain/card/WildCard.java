package domain.card;

import java.util.Objects;

// -----------------------------
// Patrón Strategy en cartas UNO
// -----------------------------
// Esta clase representa una carta comodín (Wild o Wild Draw Four) en UNO.
// Forma parte del patrón Strategy porque encapsula el comportamiento específico
// de las cartas comodín, permitiendo que la lógica de jugada y validación sea
// diferente a la de otras cartas.
//
// El sistema puede tratar todas las cartas de forma polimórfica (como Card),
// pero cada subclase implementa su propia lógica.

public class WildCard extends AbstractCard {
    public WildCard(CardType type) {
        super(type, null);
    }

    public WildCard(CardType type, CardColor color) {
        super(type, color);
        CardUtil.validateColor(color);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WildCard wildCard = (WildCard) o;
        return getType() == wildCard.getType() && getColor() == wildCard.getColor();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getColor());
    }

    @Override
    public String toString() {
        return "WildCard{" +
            getType() + ", " + getColor() +
            '}';
    }
}
