package domain.common;

import java.util.List;
import java.util.ArrayList;

// -----------------------------
// Patrón Observer en eventos de juego
// -----------------------------
// Esta clase implementa el patrón Observer para la publicación y suscripción
// de eventos de dominio en el juego UNO. Permite que múltiples componentes
// (como la UI o el motor de juego) se suscriban a eventos y reaccionen cuando
// ocurren cambios importantes (por ejemplo, una carta jugada o el fin del juego).
//
// Los suscriptores implementan DomainEventSubscriber y se registran aquí.
// Cuando ocurre un evento, se notifica a todos los suscriptores.

// Implementación de la interfaz Publisher
public class DomainEventPublisher implements Publisher { // Ahora implementa Publisher
    private static final ThreadLocal<List<DomainEventSubscriber>> subscribers = ThreadLocal.withInitial(ArrayList::new);
    private static final ThreadLocal<Boolean> isPublishing = ThreadLocal.withInitial(() -> Boolean.FALSE);
    private static final DomainEventPublisher instance = new DomainEventPublisher();



    private DomainEventPublisher() {
        // private constructor to prevent instantiation
    }

    public static DomainEventPublisher getInstance() {
        return instance;    
    }

    public static DomainEventPublisher getInstance() {
        return INSTANCE;
    }

    @Override 
    public void subscribe(DomainEventSubscriber subscriber) {
        if (Boolean.TRUE.equals(isPublishing.get())) {
            return;
        }
        var registeredSubscribers = subscribers.get();
        registeredSubscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(DomainEventSubscriber subscriber) {
        if(Boolean.TRUE.equals(isPublishing.get())){
            return;
        }
        subscribers.get().remove(subscriber);
    }

    @Override
    public void publish(final DomainEvent event) {
        if (Boolean.TRUE.equals(isPublishing.get())) {
            return;
        }
        try {
            isPublishing.set(Boolean.TRUE);
            var registeredSubscribers = subscribers.get();
            for (var subscriber : registeredSubscribers) {
                subscriber.handleEvent(event);
            }
        } finally {
            isPublishing.set(Boolean.FALSE);
        }
    }

    @Override
    public void reset() {
        subscribers.remove();
        isPublishing.remove();
    }
}
