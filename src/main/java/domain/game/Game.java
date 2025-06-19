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

public class Game extends Entity {
    private final PlayerRoundIterator players;

    private DrawPile drawPile;
    private final Stack<Card> discardPile = new Stack<>();

    private ImmutablePlayer winner = null;

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

    public void playCard(UUID playerId, Card playedCard) {
        playCard(playerId, playedCard, false);
    }

    public void playCard(UUID playerId, Card playedCard, boolean hasSaidUno) {
        if (isOver()) {
            throw new IllegalStateException("Game is over");
        }

        validatePlayedCard(playerId, playedCard);

        // Si hay penalización acumulada, solo se puede responder con la misma carta
        if (accumulatedPenalty > 0 && !isStackableResponse(playedCard)) {
            // No puede apilar, aplica penalización y termina turno
            drawCards(players.getCurrentPlayer(), accumulatedPenalty);
            resetPenalty();
            players.next();
            DomainEventPublisher.getInstance().publish(new CardDrawn(playerId));
            return;
        }

        switch (playedCard.getType()) {
            case NUMBER -> {
                checkNumberCardRule(playedCard);
                acceptPlayedCard(playedCard, hasSaidUno);
                resetPenalty();
                players.next();
            }
            case SKIP -> {
                checkActionCardRule(playedCard);
                acceptPlayedCard(playedCard, hasSaidUno);
                resetPenalty();
                players.next();
                players.next();
            }
            case REVERSE -> {
                checkActionCardRule(playedCard);
                acceptPlayedCard(playedCard, hasSaidUno);
                resetPenalty();
                reverse();
            }
            case DRAW_TWO -> {
                checkActionCardRule(playedCard);
                acceptPlayedCard(playedCard, hasSaidUno);
                accumulatedPenalty += 2;
                accumulatedPenaltyType = CardType.DRAW_TWO;
                players.next();
            }
            case WILD_COLOR -> {
                checkWildCardRule(playedCard);
                acceptPlayedCard(playedCard, hasSaidUno);
                resetPenalty();
                players.next();
            }
            case WILD_DRAW_FOUR -> {
                checkWildCardRule(playedCard);
                acceptPlayedCard(playedCard, hasSaidUno);
                accumulatedPenalty += 4;
                accumulatedPenaltyType = CardType.WILD_DRAW_FOUR;
                players.next();
            }
            default -> rejectPlayedCard(playedCard);
        }

        DomainEventPublisher.getInstance().publish(new CardPlayed(playerId, playedCard));

        if (isOver()) {
            DomainEventPublisher.getInstance().publish(new GameOver(winner));
        }
    }

    public void drawCard(UUID playerId) {
        if (getCurrentPlayer().getId().equals(playerId)) {
            // Si hay penalización acumulada, el jugador debe juntar todas las cartas y termina su turno
            if (accumulatedPenalty > 0) {
                drawCards(players.getCurrentPlayer(), accumulatedPenalty);
                resetPenalty();
                players.next();
                DomainEventPublisher.getInstance().publish(new CardDrawn(playerId));
                return;
            }
            var drawnCards = drawCards(players.getCurrentPlayer(), 1);
            try {
                tryToPlayDrawnCard(playerId, drawnCards.get(0));
            } catch (Exception ex) {
                players.next();
                DomainEventPublisher.getInstance().publish(new CardDrawn(playerId));
            }
        }
    }

    public boolean isOver() {
        return winner != null;
    }

    public ImmutablePlayer getWinner() {
        return winner;
    }

    public int getAccumulatedPenalty() {
        return accumulatedPenalty;
    }

    private void tryToPlayDrawnCard(UUID playerId, Card drawnCard) {
        try {
            var cardToPlay = CardUtil.isWildCard(drawnCard)
                ? new WildCard(drawnCard.getType(), peekTopCard().getColor())
                : drawnCard;

            playCard(playerId, cardToPlay);
        } catch (Exception ex) {
            // Si no se puede jugar, pero hay penalización, se aplica
            if (accumulatedPenalty > 0) {
                drawCards(players.getCurrentPlayer(), accumulatedPenalty);
                resetPenalty();
            }
            players.next();
            DomainEventPublisher.getInstance().publish(new CardDrawn(playerId));
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

    private void checkNumberCardRule(Card playedCard) {
        var topCard = peekTopCard();

        if (isFirstDiscardAWildCard() || CardRules.isValidNumberCard(topCard, (NumberCard) playedCard)) {
            return;
        }

        rejectPlayedCard(playedCard);
    }

    private void checkActionCardRule(Card playedCard) {
        var topCard = peekTopCard();

        if (isFirstDiscardAWildCard() || CardRules.isValidActionCard(topCard, (ActionCard) playedCard)) {
            return;
        }

        rejectPlayedCard(playedCard);
    }

    private void checkWildCardRule(Card playedCard) {
        if (!CardRules.isValidWildCard((WildCard) playedCard)) {
            rejectPlayedCard(playedCard);
        }
    }

    private boolean isFirstDiscardAWildCard() {
        return discardPile.size() == 1 && peekTopCard().getType() == CardType.WILD_COLOR;
    }

    private void recreateDrawPile(Card card) {
        if (drawPile.getSize() == 0) {
            throw new IllegalStateException("Not enough cards to recreate draw pile");
        }

        drawPile = DealerService.shuffle(drawPile, card);
    }

    private void acceptPlayedCard(Card card, boolean hasSaidUno) {
        players.getCurrentPlayer().removePlayedCard(card);
        discard(card);

        var remainingTotalCards = getCurrentPlayer().getTotalCards();
        checkSaidUno(remainingTotalCards, hasSaidUno);

        if (remainingTotalCards == 0) {
            winner = getCurrentPlayer();
        }
    }

    private void checkSaidUno(int remainingTotalCards, boolean hasSaidUno) {
        if (remainingTotalCards == 1 && !hasSaidUno) {
            drawCards(players.getCurrentPlayer(), 2);
        }
    }

    private void discard(Card card) {
        discardPile.add(card);
    }

    private void reverse() {
        players.reverseDirection();
        players.next();
    }

    private void drawTwoCards(Player player) {
        drawCards(player, 2);
    }

    private List<Card> drawCards(Player player, int total) {
        int min = Math.min(total, drawPile.getSize());
        var drawnCards = new ArrayList<Card>();

        for (int i = 0; i < min; i++) {
            var drawnCard = drawPile.drawCard();
            drawnCards.add(drawnCard);
            player.addToHandCards(drawnCard);
        }

        return drawnCards;
    }

    private void rejectPlayedCard(Card playedCard) {
        throw new IllegalArgumentException(
            String.format("Played card %s is not valid for %s", playedCard, peekTopCard()));
    }

    private boolean isStackableResponse(Card card) {
        return (accumulatedPenaltyType == CardType.DRAW_TWO && card.getType() == CardType.DRAW_TWO)
            || (accumulatedPenaltyType == CardType.WILD_DRAW_FOUR && card.getType() == CardType.WILD_DRAW_FOUR);
    }

    private void resetPenalty() {
        accumulatedPenalty = 0;
        accumulatedPenaltyType = null;
    }

    // -----------------------------
    // Patrón Observer y Strategy en el juego
    // -----------------------------
    // Esta clase representa la lógica principal del juego UNO.
    // Utiliza el patrón Observer para notificar a la UI y otros componentes
    // cuando ocurren eventos importantes (cartas jugadas, fin de juego, etc.)
    // mediante DomainEventPublisher.
    //
    // Además, delega la validación de jugadas a CardRules y a las subclases de Card,
    // aplicando el patrón Strategy para que cada tipo de carta tenga su propia lógica.
}
