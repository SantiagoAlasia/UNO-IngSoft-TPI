package domain.player;

import java.util.ArrayList; 
import java.util.Arrays;
import java.util.List; 
import java.util.UUID;
import java.util.stream.Stream;

public class PlayerRoundIterator {
    private List<Player> players;
    private int current = 0;
    private Direction direction = Direction.CLOCKWISE;

    public PlayerRoundIterator(Player[] players) {
        this.players = new ArrayList<>(Arrays.asList(players));
    }

    public Stream<Player> stream() {
        return players.stream();
    }

    public Player getCurrentPlayer() {
        if (players.isEmpty()) {
            return null; // Si no hay jugadores, no hay jugador actual
        }
        return players.get(current);
    }

    public Player getPlayerById(UUID playerId) {
        for (var player : players) {
            if (player.getId().equals(playerId)) {
                return player;
            }
        }
        return null;
    }

    public void reverseDirection() {
        direction = Direction.COUNTER_CLOCK_WISE;
    }

    public Player next() {
        if (players.isEmpty()) {
            return null; // No hay jugadores para avanzar
        }
        current = getNextIndex();
        return getCurrentPlayer();
    }

    private int getNextIndex() {
        if (players.isEmpty()) {
            return 0; // No hay jugadores, no hay siguiente índice
        }
        var increment = direction == Direction.CLOCKWISE ? 1 : -1;
        int nextIndex = (players.size() + current + increment) % players.size();
        // Asegurarse de que el índice sea positivo en caso de módulo negativo con números negativos
        if (nextIndex < 0) {
            nextIndex += players.size();
        }
        return nextIndex;
    }

    public Player peekNextPlayer() {
        if (players.isEmpty()) {
            return null;
        }
        int nextIndex = getNextIndex(); // Calcula el siguiente índice sin cambiar el actual
        return players.get(nextIndex);
    }

    public boolean isForwardDirection() {
        return this.direction == Direction.CLOCKWISE;
    }

    // ***** NUEVO MÉTODO: Remover un jugador *****
    /**
     * Remueve un jugador del iterador. Ajusta el índice actual si el jugador removido
     * era el jugador actual o si el índice actual está fuera de los límites.
     * @param playerId El ID del jugador a remover.
     * @return true si el jugador fue removido, false en caso contrario.
     */
    public boolean removePlayer(UUID playerId) {
        int indexToRemove = -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId().equals(playerId)) {
                indexToRemove = i;
                break;
            }
        }

        if (indexToRemove != -1) {
            players.remove(indexToRemove);

            // Ajusta el índice 'current' después de la remoción
            if (players.isEmpty()) {
                current = 0; // Si no quedan jugadores, reinicia
            } else if (current >= players.size()) {
                // Si el índice actual apunta a un espacio fuera de los límites (porque se eliminó el último o varios)
                current = 0; // Volver al inicio o al último jugador válido (aquí, al inicio)
            } else if (indexToRemove < current) {
                // Si el jugador removido estaba antes del índice actual, el índice actual se "desplaza" hacia atrás
                current--;
            }
            // Si el jugador removido era el "current", el siguiente `next()` lo saltará automáticamente
            // porque el `current` ya estará apuntando al jugador siguiente o al inicio si el último fue removido.

            // Asegurarse de que 'current' siempre sea un índice válido
            if (!players.isEmpty()) {
                current = current % players.size(); // Para manejar el caso de que current se haya desbordado si se eliminó el último y current fue el último.
                if (current < 0) { // Asegura un índice positivo
                    current += players.size();
                }
            } else {
                current = 0; // No hay jugadores
            }


            return true;
        }
        return false;
    }

    public int getNumberOfPlayers() {
        return players.size();
    }
}
