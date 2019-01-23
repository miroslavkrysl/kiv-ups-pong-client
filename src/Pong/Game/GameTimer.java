package Pong.Game;

import javafx.animation.AnimationTimer;

/**
 * The game timer which will call the Games game loop method.
 */
public class GameTimer extends AnimationTimer {

    private Game game;

    /**
     * Instantiates a new GameTimer.
     *
     * @param game the game
     */
    public GameTimer(Game game) {
        this.game = game;
    }

    @Override
    public void handle(long l) {
        game.gameLoop(game.getTime(), game.getPhase());
    }
}
