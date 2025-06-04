package domain.game;

import org.junit.jupiter.api.Test;

import domain.game.GameBuilder;
import domain.player.ImmutablePlayer;
import domain.player.Player;

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
