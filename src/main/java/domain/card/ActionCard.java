package domain.card;

import java.util.Objects;
import domain.game.GameContext;
import domain.player.Player;

/**
 * Representa una carta de acción en el juego UNO (por ejemplo: Salta, Reversa, +2).
 * 
 * Esta clase extiende AbstractCard y define el comportamiento específico de una carta de acción
 * sobrescribiendo el método play(). Aquí se aplica el patrón Strategy, ya que cada tipo de carta
 * (número, acción, comodín) implementa su propia lógica en play(), permitiendo que el comportamiento
 * de la carta sea intercambiable y extensible sin modificar el código cliente.
 * 
 * Implementa su propia estrategia en play(GameContext), cumpliendo el patrón Strategy.
 */
public class ActionCard extends AbstractCard {
    /**
     * Crea una carta de acción con el tipo y color especificados.
     */
    public ActionCard(CardType type, CardColor color) {
        super(type, color);
        CardUtil.validateActionType(type);
        CardUtil.validateColor(color);
    }

    /**
     * Implementación de la estrategia para una carta de acción.
     * Aquí se definiría la lógica concreta de lo que ocurre al jugar esta carta.
     */
    @Override
    public void play(GameContext context) {
        // Obtener el jugador siguiente
        int totalPlayers = context.getPlayers().size();
        int current = context.getCurrentPlayerIndex();
        switch (getType()) {
            case SKIP -> {
                // Saltar el turno del siguiente jugador
                int next = (current + 1) % totalPlayers;
                context.setCurrentPlayerIndex((next + 1) % totalPlayers);
            }
            case REVERSE -> {
                // Invertir la dirección: para simplificar, solo invertir el orden de la lista
                java.util.Collections.reverse(context.getPlayers());
                // Ajustar el índice del jugador actual
                context.setCurrentPlayerIndex(totalPlayers - 1 - current);
            }
            case DRAW_TWO -> {
                // El siguiente jugador roba dos cartas y pierde su turno
                int next = (current + 1) % totalPlayers;
                Player nextPlayer = context.getPlayers().get(next);
                // Suponiendo que hay un método para robar cartas en el contexto real
                // Aquí solo mostramos el efecto esperado
                System.out.println("El jugador " + nextPlayer.getName() + " debe robar 2 cartas y pierde su turno.");
                context.setCurrentPlayerIndex((next + 1) % totalPlayers);
            }
            default -> {}
        }
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
