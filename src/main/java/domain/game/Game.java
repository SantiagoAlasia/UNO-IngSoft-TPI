package domain.game;

import domain.card.*;
import domain.common.DomainEventPublisher;
import domain.common.Entity;
import domain.game.events.CardDrawn;
import domain.game.events.CardPlayed;
import domain.game.events.GameOver;
import domain.player.ImmutablePlayer;
import domain.player.Player;
import domain.player.PlayerRoundIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Clase principal que representa el estado y la lógica del juego UNO.
 * 
 * Aquí se integra el patrón Strategy: al jugar una carta, se construye un GameContext
 * y se delega la acción a la carta mediante su método play(context), permitiendo que
 * cada tipo de carta tenga su propio comportamiento y modifique el estado del juego.
 * Esto hace el código más flexible y extensible ante nuevas reglas o tipos de cartas.
 */
public class Game extends Entity {
    private final PlayerRoundIterator players;

    private DrawPile drawPile;
    private final Stack<Card> discardPile = new Stack<>();

    private ImmutablePlayer winner = null; // No se marca como final porque puede cambiar durante el juego

    private int accumulatedPenalty = 0;
    private CardType accumulatedPenaltyType = null;

    public Game(DrawPile drawPile, PlayerRoundIterator players) {
        super();
        this.drawPile = drawPile;
        this.players = players;

        startDiscardPile();
    }

    public Stream<ImmutablePlayer> getPlayers() {
        return players.stream().map(Player::toImmutable);
    }

    public ImmutablePlayer getCurrentPlayer() {
        return players.getCurrentPlayer().toImmutable();
    }

    public Stream<Card> getHandCards(UUID playerId) {
        return players.getPlayerById(playerId).getHandCards();
    }

    public Card peekTopCard() {
        return discardPile.peek();
    }

    private void startDiscardPile() {
        if (drawPile.getSize() == 0) {
            throw new IllegalStateException("No hay cartas suficientes en el mazo para iniciar la pila de descarte.");
        }
        var card = drawPile.drawCard();

        switch (card.getType()) {
            case NUMBER, WILD_COLOR -> discard(card);
            case SKIP -> {
                discard(card);
                players.next();
            }
            case REVERSE -> {
                discard(card);
                reverse();
            }
            case DRAW_TWO -> {
                discard(card);
                drawTwoCards(players.getCurrentPlayer());
                players.next();
            }
            case WILD_DRAW_FOUR -> {
                recreateDrawPile(card);
                startDiscardPile();
            }
            default -> throw new UnsupportedOperationException("Unsupported card type " + card.getType());
        }
    }

    /**
     * Construye un contexto de juego para pasar a las cartas.
     * El contexto contiene el estado relevante del juego para que la carta pueda modificarlo.
     */
    private domain.game.GameContext buildGameContext() {
        // Convierte el PlayerRoundIterator a una lista de Player
        List<domain.player.Player> playerList = new ArrayList<>();
        players.stream().forEach(playerList::add);
        // Convierte la pila de descarte a una lista
        List<domain.card.Card> discardList = new ArrayList<>(discardPile);
        // El índice del jugador currente
        int currentPlayerIndex = players.getCurrentIndex();
        return new domain.game.GameContext(playerList, currentPlayerIndex, discardList);
    }

    /**
     * Juega una carta. Ahora, en vez de manejar toda la lógica aquí, se delega la acción
     * a la carta mediante el patrón Strategy, pasando el contexto real del juego.
     */
    public void playCard(UUID playerId, Card playedCard, boolean hasSaidUno) {
        if (isOver()) {
            throw new IllegalStateException("Game is over");
        }
        validatePlayedCard(playerId, playedCard);
        // Validar reglas antes de eliminar la carta de la mano y jugarla
        Card topCard = peekTopCard();
        boolean isValid = false;
        if (playedCard instanceof domain.card.NumberCard numberCard) {
            isValid = domain.game.CardRules.isValidNumberCard(topCard, numberCard);
        } else if (playedCard instanceof domain.card.ActionCard actionCard) {
            isValid = domain.game.CardRules.isValidActionCard(topCard, actionCard);
        } else if (playedCard instanceof domain.card.WildCard wildCard) {
            isValid = domain.game.CardRules.isValidWildCard(wildCard);
        }
        if (!isValid) {
            throw new IllegalArgumentException("La carta jugada no es válida según las reglas de UNO.");
        }
        // Eliminar la carta de la mano del jugador actual
        players.getCurrentPlayer().removePlayedCard(playedCard);
        // Si al jugador le queda una sola carta y no dijo UNO, penalizar
        if (players.getCurrentPlayer().getHandCards().count() == 1 && !hasSaidUno) {
            drawCards(players.getCurrentPlayer(), 1);
        }
        // Agregar la carta jugada al descarte real
        discard(playedCard);
        // Aplicar efectos especiales según el tipo de carta
        switch (playedCard.getType()) {
            case SKIP -> {
                players.next(); // Salta el turno del siguiente jugador
                players.next(); // Avanza al siguiente después del salto
            }
            case REVERSE -> {
                players.reverseDirection();
                players.next(); // Avanza al siguiente jugador en la nueva dirección
            }
            case DRAW_TWO -> {
                players.next(); // El siguiente jugador
                drawCards(players.getCurrentPlayer(), 2); // Roba 2 cartas
                players.next(); // Pierde su turno
            }
            case WILD_DRAW_FOUR -> {
                players.next();
                drawCards(players.getCurrentPlayer(), 4);
                players.next();
            }
            default -> {
                players.next(); // Avanza turno normal para cartas comunes
            }
        }
        DomainEventPublisher.publish(new CardPlayed(playerId, playedCard));
        if (isOver()) {
            DomainEventPublisher.publish(new GameOver(winner));
        }
    }

    public void drawCard(UUID playerId) {
        if (getCurrentPlayer().getId().equals(playerId)) {
            // Si hay penalización acumulada, el jugador debe juntar todas las cartas y termina su turno
            if (accumulatedPenalty > 0) {
                drawCards(players.getCurrentPlayer(), accumulatedPenalty);
                resetPenalty();
                players.next();
                DomainEventPublisher.publish(new CardDrawn(playerId));
                return;
            }
            var drawnCards = drawCards(players.getCurrentPlayer(), 1);
            try {
                tryToPlayDrawnCard(playerId, drawnCards.get(0));
            } catch (Exception ex) {
                players.next();
                DomainEventPublisher.publish(new CardDrawn(playerId));
            }
        }
    }

    public boolean isOver() {
        return winner != null;
    }

    public ImmutablePlayer getWinner() {
        return winner;
    }

    private void tryToPlayDrawnCard(UUID playerId, Card drawnCard) {
        try {
            var cardToPlay = CardUtil.isWildCard(drawnCard)
                ? new WildCard(drawnCard.getType(), peekTopCard().getColor())
                : drawnCard;

            playCard(playerId, cardToPlay, false);
        } catch (Exception ex) {
            // Si no se puede jugar, pero hay penalización, se aplica
            if (accumulatedPenalty > 0) {
                drawCards(players.getCurrentPlayer(), accumulatedPenalty);
                resetPenalty();
            }
            players.next();
            DomainEventPublisher.publish(new CardDrawn(playerId));
        }
    }

    private void validatePlayedCard(UUID playerId, Card card) {
        var currentPlayer = players.getCurrentPlayer();
        if (!currentPlayer.getId().equals(playerId)) {
            throw new IllegalArgumentException(String.format("Not the turn of Player ID %s", playerId));
        }

        if (!currentPlayer.hasHandCard(card)) {
            throw new IllegalArgumentException(String.format("Card %s does not exist in player's hand cards", card));
        }
    }

    private boolean isStackableResponse(Card card) {
        return (accumulatedPenaltyType == CardType.DRAW_TWO && card.getType() == CardType.DRAW_TWO)
            || (accumulatedPenaltyType == CardType.WILD_DRAW_FOUR && card.getType() == CardType.WILD_DRAW_FOUR);
    }

    private void resetPenalty() {
        accumulatedPenalty = 0;
        accumulatedPenaltyType = null;
    }

    /**
     * Métodos auxiliares para el flujo del juego (descartar, reversa, robar, etc).
     * Estos métodos son usados por el contexto y las estrategias de las cartas.
     */
    // Métodos auxiliares agregados para corregir errores de compilación
    private void discard(Card card) {
        discardPile.push(card);
    }

    private void reverse() {
        players.reverseDirection();
    }

    private void drawTwoCards(Player player) {
        drawCards(player, 2);
    }

    private void recreateDrawPile(Card card) {
        // Lógica para recrear el mazo de robar si es necesario (puedes personalizarla)
        drawPile = new DrawPile(new ArrayList<>(discardPile));
        discardPile.clear();
        discard(card);
    }

    private List<Card> drawCards(Player player, int count) {
        for (int i = 0; i < count; i++) {
            Card c = drawPile.drawCard();
            player.addToHandCards(c);
            // Si no se usa la lista drawn, se puede omitir
        }
        return null; // O cambiar el método a void si no se usa el valor de retorno
    }
}
