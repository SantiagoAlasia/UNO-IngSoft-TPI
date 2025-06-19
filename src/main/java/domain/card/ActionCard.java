package domain.card;

import java.util.Objects;

// -----------------------------
// Patrón Strategy en cartas UNO
// -----------------------------
// Esta clase representa una carta de acción (Skip, Reverse, Draw Two) en UNO.
// El patrón Strategy se aplica porque cada tipo de carta (ActionCard, WildCard, NumberCard)
// implementa su propio comportamiento y validaciones, permitiendo que la lógica de jugada
// sea intercambiable y extensible según el tipo de carta.
//
// Por ejemplo, la validación de si una carta puede ser jugada depende de su tipo y color,
// y se delega a métodos específicos en CardRules.
//
// Si se agregan nuevas cartas especiales, solo se debe crear una nueva clase que extienda
// AbstractCard y defina su lógica, sin modificar el resto del sistema.

public class ActionCard extends AbstractCard {
    public ActionCard(CardType type, CardColor color) {
        super(type, color);
        CardUtil.validateActionType(type);
        CardUtil.validateColor(color);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionCard that = (ActionCard) o;
        return getType() == that.getType() && getColor() == that.getColor();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getColor());
    }

    @Override
    public String toString() {
        return "ActionCard{" +
            getType() + ", " + getColor() +
            '}';
    }
}
