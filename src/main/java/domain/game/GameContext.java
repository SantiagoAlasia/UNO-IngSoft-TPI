package domain.game;

import domain.card.Card;
import domain.player.Player;
import java.util.List;

/**
 * Contexto del juego UNO, contiene el estado relevante para que las cartas puedan modificarlo al ser jugadas.
 * 
 * Es el objeto que se pasa a cada carta en su método play, permitiendo que la estrategia de cada carta
 * interactúe con el estado real del juego (Strategy).
 */
public class GameContext {
    private final List<Player> players;
    private int currentPlayerIndex;
    private final List<Card> discardPile;
    // Puedes agregar más atributos según lo que necesite el juego (mazo, dirección, etc.)

    public GameContext(List<Player> players, int currentPlayerIndex, List<Card> discardPile) {
        this.players = players;
        this.currentPlayerIndex = currentPlayerIndex;
        this.discardPile = discardPile;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int index) {
        this.currentPlayerIndex = index;
    }

    public List<Card> getDiscardPile() {
        return discardPile;
    }

    public void addToDiscardPile(Card card) {
        discardPile.add(card);
    }
}
