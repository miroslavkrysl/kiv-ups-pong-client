package Pong.Game;

import Pong.App;
import Pong.Game.Types.Side;
import com.sun.istack.internal.NotNull;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Game {
    public enum Type {
        LOCAL,
        NET
    }

    public enum Phase {
        WAITING,
        START,
        PLAYING,
    }

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;

    private App app;
    private Type type;

    private Side playerSide;

    private String nicknameLeft;
    private String nicknameRight;

    private IntegerProperty scoreLeft;
    private IntegerProperty scoreRight;

    private ObjectProperty<Phase> phase;

    private Player playerLeft;
    private Player playerRight;
    private Ball ball;

    public Game(@NotNull App app, String nicknameLeft, String nicknameRight, Side side) {
        long now = app.getCurrentTime();

        this.app = app;
        this.phase = new SimpleObjectProperty<>(Phase.WAITING);
        this.nicknameLeft = nicknameLeft;
        this.nicknameRight = nicknameRight;
        this.scoreLeft = new SimpleIntegerProperty(0);
        this.scoreRight = new SimpleIntegerProperty(0);
        this.playerLeft = new Player(now);
        this.playerRight = new Player(now);
        this.ball = new Ball(now);
        this.playerSide = side;

        if (side == null) {
            this.type = Type.LOCAL;
        }
        else {
            this.type = Type.NET;
        }
    }

    public App getApp() {
        return app;
    }

    public Type getType() {
        return type;
    }

    public Player getPlayer(Side side) {
        switch (side) {
            case LEFT:
                return playerLeft;
            case RIGHT:
                return playerRight;
        }

        return null;
    }

    public Side getPlayerSide() {
        return playerSide;
    }

    public String getNicknameLeft() {
        return nicknameLeft;
    }

    public String getNicknameRight() {
        return nicknameRight;
    }

    public int getScoreLeft() {
        return scoreLeft.get();
    }

    public IntegerProperty scoreLeftProperty() {
        return scoreLeft;
    }

    public int getScoreRight() {
        return scoreRight.get();
    }

    public IntegerProperty scoreRightProperty() {
        return scoreRight;
    }

    public Phase getPhase() {
        return phase.get();
    }

    public ObjectProperty<Phase> phaseProperty() {
        return phase;
    }

    public Player getPlayerLeft() {
        return playerLeft;
    }

    public Player getPlayerRight() {
        return playerRight;
    }

    public Ball getBall() {
        return ball;
    }
}
