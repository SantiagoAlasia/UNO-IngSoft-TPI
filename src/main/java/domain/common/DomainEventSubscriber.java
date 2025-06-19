package domain.common;

// -----------------------------
// Patrón Observer en eventos de juego
// -----------------------------
// Esta interfaz define el contrato para los suscriptores de eventos de dominio.
// Cualquier clase que quiera reaccionar a eventos debe implementar este método
// y registrarse en DomainEventPublisher.

public interface DomainEventSubscriber {
    void handleEvent(DomainEvent event);
}
