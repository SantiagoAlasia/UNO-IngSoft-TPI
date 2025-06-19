package domain.common;

/**
 * Test unitarios del mecanismo de publicación y suscripción de eventos de dominio (Observer).
 * 
 * El patrón Observer permite que distintos componentes del juego (por ejemplo, la UI o servicios)
 * se suscriban a eventos importantes (como que se juegue una carta) y reaccionen automáticamente
 * cuando estos eventos ocurren, sin acoplarse directamente entre sí.
 * 
 * Estos tests verifican:
 * - Que un suscriptor recibe correctamente los eventos publicados si está suscrito.
 * - Que un suscriptor NO recibe eventos si se desuscribe antes de la publicación.
 * Así se garantiza la correcta comunicación desacoplada entre componentes.
 */

import domain.card.Card;
import domain.card.CardColor;
import domain.game.events.CardPlayed;
import domain.testhelper.CardTestFactory;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TestDomainEventPublisher {
    /**
     * Suscriptor de prueba que cuenta cuántas veces fue notificado y qué carta recibió.
     */
    class TestSubscriber implements DomainEventSubscriber {
        int timesInvoked = 0;
        Card playedCard = null;

        @Override
        public void handleEvent(DomainEvent event) {
            timesInvoked += 1;
            playedCard = ((CardPlayed) event).getPlayedCard();
        }
    }

    /**
     * Verifica que cuando un suscriptor está suscrito, recibe el evento publicado exactamente una vez
     * y puede acceder a la carta jugada.
     */
    @Test
    void WhenEventPublished_ShouldInvokeSubscriber() {
        // Arrange: crear suscriptor y suscribirlo
        var subscriber = new TestSubscriber();
        DomainEventPublisher.getInstance().subscribe(subscriber);
        // Act: publicar un evento y desuscribir
        DomainEventPublisher.getInstance().publish(new CardPlayed(UUID.randomUUID(),
            CardTestFactory.createNumberCard(1, CardColor.RED)));
        DomainEventPublisher.getInstance().unsubscribe(subscriber);
        // Assert: el suscriptor fue notificado una vez y recibió la carta correcta
        assertEquals(1, subscriber.timesInvoked);
        assertEquals(CardTestFactory.createNumberCard(1, CardColor.RED), subscriber.playedCard);
    }

    /**
     * Verifica que si un suscriptor se desuscribe antes de la publicación, NO recibe el evento.
     */
    @Test
    void WhenUnsubscribed_ShouldNotInvokeSubscriber() {
        // Arrange: crear suscriptor, suscribir y luego desuscribir
        var subscriber = new TestSubscriber();
        DomainEventPublisher.getInstance().subscribe(subscriber);
        DomainEventPublisher.getInstance().unsubscribe(subscriber);
        // Act: publicar un evento
        DomainEventPublisher.getInstance().publish(new CardPlayed(UUID.randomUUID(),
            CardTestFactory.createNumberCard(1, CardColor.RED)));
        // Assert: el suscriptor no fue notificado
        assertEquals(0, subscriber.timesInvoked);
        assertNull(subscriber.playedCard);
    }
}
