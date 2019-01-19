package Pong;

import Pong.Game.Game;
import Pong.Gui.GameScene;
import Pong.Network.Connection;
import Pong.Network.PacketHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application{
    private Connection connection;
    private PacketHandler packetHandler;
    private Game game;
    private Scene loginScene;
    private Scene overlayScene;
    private Scene lobbyScene;
    private Scene gameScene;

    private long timeDifference;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new GameScene(new Game(this, "hello", "jello", null), 0.7));
        stage.show();
    }

//    private App() {
//        packetHandler = new PacketHandler();
//
//        // create scenes
//    }

    public long getCurrentTime() {
        return System.currentTimeMillis() + timeDifference;
    }

    public void setTimeDifference(long difference) {
        this.timeDifference = difference;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public PacketHandler getPacketHandler() {
        return packetHandler;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}