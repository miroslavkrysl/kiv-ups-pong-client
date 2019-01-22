package Pong;

import Pong.Exceptions.OperatorException;
import Pong.Game.BallState;
import Pong.Game.Game;
import Pong.Game.Player;
import Pong.Game.PlayerState;
import Pong.Game.Types.GamePhase;
import Pong.Game.Types.Side;
import Pong.Gui.SceneType;
import Pong.Network.Connection;
import Pong.Network.Exceptions.ConnectionException;
import Pong.Network.Packet;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Operator {
    public static final int NICKNAME_LENGTH_MIN = 3;
    public static final int NICKNAME_LENGTH_MAX = 16;

    private App app;
    private Game game;
    private Connection connection;
    private long timeDifference;
    private long latency;

    private String nickname;

    private StringProperty loginErrorMessage;

    public Operator(App app) {
        this.app = app;
        this.connection = null;
        this.game = null;

        this.timeDifference = 0;

        this.loginErrorMessage = new SimpleStringProperty("");
    }

    public long getTime() {
        return System.currentTimeMillis() + timeDifference;
    }

    public App getApp() {
        return app;
    }

    public Game getGame() {
        return game;
    }

    public String getLoginErrorMessage() {
        return loginErrorMessage.get();
    }

    public StringProperty loginErrorMessageProperty() {
        return loginErrorMessage;
    }

    public void startLocalGame(String playerLeft, String playerRight) throws OperatorException {
        if (game != null || connection != null) {
            throw new OperatorException("can not start local game, while connected to server");
        }
        this.game = new Game(this, playerLeft, playerRight);
        app.switchScene(app.createGameScene(game));
    }

    public void requestConnect(String ip, int port, String nickname) throws ConnectionException {
        connection = new Connection(this, ip, port);
        Thread thread = new Thread(connection);
        thread.start();
        this.nickname = nickname;
        connection.send(new Packet("login", nickname));
        app.switchScene(app.getConnectingScene());
        requestSynchronize();
    }

    public void requestSynchronize() {
        Platform.runLater(() -> {
            for (int i = 0; i < 10; i++) {
                connection.send(new Packet("time", Long.toString(System.currentTimeMillis())));
            }
        });
    }

    public void synchronize(long sendTime, long serverTime) {
        long now = System.currentTimeMillis();

        this.latency = (3 * latency + (now - sendTime)) / 4;
        this.timeDifference = (serverTime - sendTime) + (latency / 2);


        System.out.println(latency);
        System.out.println(timeDifference);
    }

    public void notLogged() {
        connection.close();
        connection = null;
        loginErrorMessage.set("Nickname is in wrong format");
        app.switchScene(app.getLoginScene());
    }

    public void logged() {
        loginErrorMessage.set("");
        app.switchScene(app.createLobyScene(this));
    }

    public void requestDisconnect() throws OperatorException {
        if (connection == null) {
            throw new OperatorException("already disconnected");
        }
        connection.close();
        connection = null;
        app.switchScene(app.getLoginScene());
    }

    public void requestJoinGame() {
        connection.send(new Packet("join"));
        app.switchScene(app.getWaitingScene());
    }

    public void joinGame(Side side) {
        this.game = new Game(this, nickname, "", side);
    }

    public void opponentJoinedGame(String opponent) {
        this.game.setOpponentNickname(opponent);
        System.out.println("opponent");

        game.getPlayer(game.getSide()).stateProperty().addListener((observableValue, oldState, newState) -> {
            Player player = game.getPlayer(game.getSide());
            if (player.isChangedFromServer()) {
                player.setChangedFromServer(false);
                return;
            }
            connection.send(new Packet("state", newState.itemize()));
        });

        app.switchScene(app.createGameScene(game));
    }

    public void opponentLeft() {
        app.switchScene(app.createGameOverScene(this, game));
    }

    public void requestLeaveGame() {
        if (game != null && !game.isLocal()) {
            connection.send(new Packet("leave"));
        }

        app.switchScene(app.createLobyScene(this));
        game = null;
    }

    public void requestReady() {
        if (game.getPhase() == GamePhase.PLAYING || game.getPhase() == GamePhase.START) {
            return;
        }

        if (game.isLocal()) {
            game.startRound();
        }
        else {
            connection.send(new Packet("ready"));
        }
    }

    public void gameOver() {
        app.switchScene(app.createGameOverScene(this, game));
    }

    public Connection getConnection() {
        return connection;
    }

    public void stop() {
        if (connection != null) {
            connection.stop();
        }
    }

    public void updateYourState(PlayerState playerState) {
        Player player = game.getPlayer(game.getSide());
        player.setChangedFromServer(true);
        game.setPlayerState(playerState, game.getSide());
    }

    public void updateOpponentState(PlayerState playerState) {
        Side side = (game.getSide() == Side.LEFT ? Side.RIGHT : Side.LEFT);
        Player player = game.getPlayer(side);
        player.setChangedFromServer(true);
        game.setPlayerState(playerState, side);
    }

    public void newRound(int scoreLeft, int scoreRight) {
        Platform.runLater(() -> {
            game.setScoreLeft(scoreLeft);
            game.setScoreRight(scoreRight);
        });
        game.newRound();
    }

    public void ballReleased(BallState state) {
        game.startRound(state);
    }

    public void ballHit(BallState state) {
        game.setPhase(GamePhase.PLAYING);
        game.setBallState(state);
    }
}
