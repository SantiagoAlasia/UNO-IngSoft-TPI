package domain.player;

import domain.card.Card;
import domain.card.CardType;
import domain.card.CardUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class HandCardList {
    private final List<Card> handCards = new ArrayList<>();

    public void addCard(Card newCard) {
        handCards.add(newCard);
    }

    public boolean removeCard(Card card) {
        var cardToRemove = CardUtil.isWildCard(card) ? findCardOfType(card.getType()) : card;
        return handCards.remove(cardToRemove);
    }

    private Card findCardOfType(CardType type) {
        for (var card : handCards) {
            if (card.getType() == type) {
                return card;
            }
        }

        return null;
    }

    public Stream<Card> getCardStream() {
        return handCards.stream();
    }

    public boolean hasCard(Card card) {
        // Compara por tipo y color para todas las cartas, no solo por equals
        return getCardStream().anyMatch(c ->
            c.getType() == card.getType() &&
            (c.getColor() == null || card.getColor() == null || c.getColor() == card.getColor())
        );
    }

    public int size() {
        return handCards.size();
    }
}
