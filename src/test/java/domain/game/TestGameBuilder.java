package domain.game;

import org.junit.jupiter.api.Test;

import Modelo.game.GameBuilder;
import Modelo.player.ImmutablePlayer;
import Modelo.player.Player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestGameBuilder {

    @Test
    void WhenCreatedWithOnePlayer_ShouldThrowError() {
        var gameBuilder = new GameBuilder()
            .withPlayer("Player 1");

        assertThrows(IllegalStateException.class, gameBuilder::build);
    }

    @Test
    void WhenHavingThreePlayers_ShouldBuildGame(){
        var game = new GameBuilder()
            .withPlayer("Player 1")
            .withPlayer("Player 2")
            .withPlayer("Player 3")
            .build();

        var players = game.getPlayers().toArray(ImmutablePlayer[]::new);

        assertEquals(3, players.length);
        assertEquals("Player 1", players[0].getName());
    }
}
