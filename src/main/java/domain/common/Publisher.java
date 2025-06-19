package domain.common;

// 1. Interfaz Publisher
public interface Publisher {
    void subscribe(DomainEventSubscriber subscriber);
    void unsubscribe(DomainEventSubscriber subscriber);
    void publish(final DomainEvent event);
    void reset(); // Podría ser parte de la interfaz si el reseteo es una operación común
}
