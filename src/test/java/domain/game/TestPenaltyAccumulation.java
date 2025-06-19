package domain.game;

import domain.card.*;
import domain.player.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test para verificar la acumulación de penalidades (+2, +4) en la clase Game.
 * Cubre casos donde se juegan varias cartas de penalidad seguidas y se verifica
 * que la penalidad acumulada se aplica correctamente al jugador siguiente.
 */
class TestPenaltyAccumulation {
    private Game game;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        // Crear dos manos vacías
        HandCardList hand1 = new HandCardList();
        HandCardList hand2 = new HandCardList();
        // Agregar cartas de penalidad a las manos para simular jugadas
        hand1.addCard(new ActionCard(CardType.DRAW_TWO, CardColor.RED));
        hand2.addCard(new ActionCard(CardType.DRAW_TWO, CardColor.BLUE));
        // Crear jugadores
        player1 = new Player("Jugador 1", hand1);
        player2 = new Player("Jugador 2", hand2);
        // Crear iterador de jugadores
        Player[] jugadores = new Player[] { player1, player2 };
        PlayerRoundIterator players = new PlayerRoundIterator(jugadores);
        // Crear un mazo con algunas cartas
        DrawPile drawPile = new DrawPile(List.of(
                new NumberCard(1, CardColor.RED),
                new NumberCard(2, CardColor.BLUE),
                new NumberCard(3, CardColor.GREEN),
                new NumberCard(4, CardColor.YELLOW)
        ));
        game = new Game(drawPile, players);
    }

    @Test
    void testAcumulacionDePenalidades() {
        // Simular que la carta superior es un +2
        Card drawTwo = new ActionCard(CardType.DRAW_TWO, CardColor.RED);
        game.playCard(player1.toImmutable().getId(), drawTwo, false);
        // El siguiente jugador también juega un +2
        Card drawTwo2 = new ActionCard(CardType.DRAW_TWO, CardColor.BLUE);
        game.playCard(player2.toImmutable().getId(), drawTwo2, false);
        // Ahora la penalidad acumulada debe ser 4
        assertEquals(4, game.getAccumulatedPenalty());
    }
}
