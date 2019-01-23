package Pong.Game;

import Pong.App;
import Pong.Game.Types.Side;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * The class NetworkGame represents a game played over the network with only one local player.
 * The game loop does not check collisions, its all handled by the server.
 */
public class NetworkGame extends Game{

    private Side localPlayerSide;
    private BooleanProperty playerLeftReady;
    private BooleanProperty playerRightReady;

    /**
     * Instantiates a new NetworkGame.
     *
     * @param app             the app instance
     * @param nicknameLeft    the left players nickname
     * @param nicknameRight   the right players nickname
     * @param localPlayerSide the local player side
     */
    public NetworkGame(App app, String nicknameLeft, String nicknameRight, Side localPlayerSide) {
        super(app, nicknameLeft, nicknameRight);
        this.localPlayerSide = localPlayerSide;
        this.playerLeftReady = new SimpleBooleanProperty(false);
        this.playerRightReady = new SimpleBooleanProperty(false);
    }

    @Override
    public void newRound() {
        super.newRound();
        playerLeftReady.set(false);
        playerRightReady.set(false);
    }

    @Override
    public long getTime() {
        return app.getConnection().getTime();
    }

    /**
     * Gets the local player side.
     *
     * @return the local player side
     */
    public Side getLocalPlayerSide() {
        return localPlayerSide;
    }

    /**
     * Set player state to ready.
     *
     * @param side the side of the player
     */
    public void playerReady(Side side) {
        playerReadyProperty(side).set(true);
    }

    /**
     * Check whether the player is ready.
     *
     * @param side the side of the player
     * @return the boolean
     */
    public boolean isPlayerReady(Side side) {
        return playerReadyProperty(side).get();
    }

    /**
     * Gets the player ready boolean property.
     *
     * @param side the side of the player
     * @return the boolean property
     */
    public BooleanProperty playerReadyProperty(Side side) {
        if (side == Side.LEFT) {
            return playerLeftReady;
        }
        else if (side == Side.RIGHT) {
            return playerLeftReady;
        }

        return null;
    }

    public void startRound(BallState state) {
        super.startRound();
        getBall().setState(state);
    }

    public void ballHit(BallState state) {
        getBall().setState(state);
        setPhase(Phase.PLAYING);
    }
}
