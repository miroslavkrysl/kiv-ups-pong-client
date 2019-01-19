package Pong.Game;

import Pong.App;
import Pong.Game.Types.Side;
import Pong.Gui.GameScene;
import com.sun.istack.internal.NotNull;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Game {
    enum Type {
        LOCAL,
        NET
    }

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;

    private App app;
    private Type type;
    private GameScene scene;

    private Side playerSide;

    private String nicknameLeft;
    private String nicknameRight;

    private IntegerProperty scoreLeft;
    private IntegerProperty scoreRight;

    private ObjectProperty<PlayerState> playerStateLeft;
    private ObjectProperty<PlayerState> playerStateRight;
    private ObjectProperty<BallState> ballState;

    public Game(@NotNull App app, String nicknameLeft, String nicknameRight, Side side) {
        long now = app.getCurrentTime();

        this.app = app;
        this.nicknameLeft = nicknameLeft;
        this.nicknameRight = nicknameRight;
        this.scoreLeft = new SimpleIntegerProperty(0);
        this.scoreRight = new SimpleIntegerProperty(0);
        this.playerStateLeft = new SimpleObjectProperty<>(new PlayerState(now));
        this.playerStateRight = new SimpleObjectProperty<>(new PlayerState(now));
        this.ballState = new SimpleObjectProperty<>(new BallState(now));
        this.playerSide = side;

        if (side == null) {
            this.type = Type.LOCAL;
        }
        else {
            this.type = Type.NET;
        }

        this.scene = new GameScene(this, 0.7);
    }

    public App getApp() {
        return app;
    }

    public Type getType() {
        return type;
    }

    public GameScene getScene() {
        return scene;
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

    public PlayerState getPlayerStateLeft() {
        return playerStateLeft.get();
    }

    public ObjectProperty<PlayerState> playerStateLeftProperty() {
        return playerStateLeft;
    }

    public PlayerState getPlayerStateRight() {
        return playerStateRight.get();
    }

    public ObjectProperty<PlayerState> playerStateRightProperty() {
        return playerStateRight;
    }

    public BallState getBallState() {
        return ballState.get();
    }

    public ObjectProperty<BallState> ballStateProperty() {
        return ballState;
    }
}
