package Pong;

import Pong.Game.Game;
import Pong.Gui.Controls;
import Pong.Gui.GameScene;
import Pong.Network.Connection;
import Pong.Network.PacketHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application{
    private Connection connection;
    private PacketHandler packetHandler;
    private Controls controls;


    private Scene loginScene;
    private Scene overlayScene;
    private Scene lobbyScene;
    private Scene gameScene;

    private long timeDifference;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        packetHandler = new PacketHandler();
        controls = new Controls();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new GameScene(new Game(this, "hello", "jello", null), 0.7));
        stage.show();
    }

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

    public Controls getControls() {
        return controls;
    }
}