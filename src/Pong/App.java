package Pong;

import Pong.Game.*;
import Pong.Game.Types.Side;
import Pong.Network.Connection;
import Pong.Network.Exceptions.*;
import Pong.Network.Packet;
import javafx.application.Platform;
import javafx.scene.Scene;

import java.net.InetSocketAddress;

/**
 * The class App is a center of this app logic.
 */
public class App {
    private Main main;
    private Connection connection;
    private Game game;

    private Scene lastScene;
    private String nickname;

    /**
     * Instantiates a new App.
     *
     * @param main the main javafx application class
     */
    public App(Main main) {
        this.main = main;
        this.connection = new Connection(this);
        this.game = null;

        this.connection.closedProperty().addListener((observableValue, oldClosed, newClosed) -> {
            if (newClosed) {
                if (connection.isClosedByServer()) {
                    main.getLoginController().setErrorMessage("Connection with the server is lost", true);
                }
                main.switchScene(main.getLoginScene());
            }
        });

        this.connection.unavailableProperty().addListener((observableValue, oldUnavailable, newUnavailable) -> {
            if (newUnavailable) {
                this.lastScene = main.getCurrentScene();
                main.switchScene(main.getUnavailableScene());
            }
            else {
                main.switchScene(lastScene);
            }
        });
    }

    /**
     * Start local game.
     *
     * @param playerLeft  the player left nickname
     * @param playerRight the player right nickname
     */
    public void startLocalGame(String playerLeft, String playerRight) {
        this.game = new LocalGame(this, playerLeft, playerRight);

        main.switchScene(main.createGameScene(game));
    }

    /**
     * Connect to the server.
     *
     * @param ip       the ip
     * @param port     the port
     * @param nickname the nickname
     */
    public void connect(String ip, int port, String nickname) {
        String message = "";
        try {
            connection.connect(new InetSocketAddress(ip, port), nickname);
            Platform.runLater(() -> {
                main.switchScene(main.createLobbyScene(this, connection));
            });
            this.nickname = nickname;
            return;
        }
        catch (ConnectingRefusedException exception) {
            message = "Server is full";
        }
        catch (ConnectingTimeoutException exception) {
            message = "Server is not responding";
        }
        catch (BadNicknameException exception) {
            message = "Nickname is in bad format";
        }
        catch (CantConnectException exception) {
            message = "Can't connect to the server";
        } catch (ConnectionException e) {
            message = "An error occurred";
        }

        main.getLoginController().setErrorMessage(message, true);
    }

    /**
     * Disconnect from the server.
     */
    public void disconnect() {
        this.game = null;
        connection.stop();
    }

    /**
     * Leave the game.
     */
    public void leaveGame() {
        if (game instanceof NetworkGame) {
            connection.send(new Packet("leave"));
            main.switchScene(main.createLobbyScene(this, connection));
        }
        else {
            main.switchScene(main.getLoginScene());
            game = null;
        }
    }

    public void joinGame() {
        connection.send(new Packet("join"));
        main.switchScene(main.getWaitingScene());
    }

    public void joinedGame(Side side) {
        String nicknameLeft = "";
        String nicknameRight = "";

        if (side == Side.LEFT) {
            nicknameLeft = nickname;
        }
        else {
            nicknameRight = nickname;
        }

        this.game = new NetworkGame(this, nicknameLeft, nicknameRight, side);
    }

    public void opponentJoinedGame(String opponent) {
        NetworkGame game = (NetworkGame)this.game;
        this.game.setNickname(Side.getOther(game.getLocalPlayerSide()), opponent);

        System.out.println(game.getLocalPlayerSide());
        game.getPlayer(game.getLocalPlayerSide()).stateProperty().addListener((observableValue, oldState, newState) -> {
            Player player = game.getPlayer(game.getLocalPlayerSide());
            System.out.println("player " + nickname + " moved");
            if (player.isChangedFromServer()) {
                return;
            }
            connection.send(new Packet("state", newState.itemize()));
        });

        game.playerReadyProperty(game.getLocalPlayerSide()).addListener((observableValue, oldReady, newReady) -> {
            if (newReady) {
                Player player = game.getPlayer(game.getLocalPlayerSide());
                connection.send(new Packet("ready"));
            }
        });

        main.switchScene(main.createGameScene(game));
    }

    public void opponentLeft() {
        main.switchScene(main.createGameOverScene(this, game, "Opponent left"));
    }

    public void gameOver(int scoreLeft, int scoreRight) {
        Platform.runLater(() -> {
            game.setScore(Side.LEFT, scoreLeft);
            game.setScore(Side.RIGHT, scoreRight);
        });
        main.switchScene(main.createGameOverScene(this, game, "GameOver"));
    }

    public void gameEnded() {
        main.switchScene(main.createGameOverScene(this, game, "Game ended"));
    }

    public void updateYourState(PlayerState playerState) {
        NetworkGame game = (NetworkGame)this.game;

        Player player = game.getPlayer(game.getLocalPlayerSide());
        game.getPlayer(game.getLocalPlayerSide()).setState(playerState, true);
    }

    public void updateOpponentState(PlayerState playerState) {
        NetworkGame game = (NetworkGame)this.game;

        game.getPlayer(Side.getOther(game.getLocalPlayerSide())).setState(playerState, true);
    }

    public void newRound(int scoreLeft, int scoreRight) {
        Platform.runLater(() -> {
            game.setScore(Side.LEFT, scoreLeft);
            game.setScore(Side.RIGHT, scoreRight);
        });
        game.newRound();
    }

    public void ballReleased(BallState state) {
        NetworkGame game = (NetworkGame)this.game;
        game.startRound(state);
    }

    public void ballHit(BallState state) {
        NetworkGame game = (NetworkGame)this.game;

        game.ballHit(state);
    }

    /**
     * Gets the connection instance.
     *
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }
}
