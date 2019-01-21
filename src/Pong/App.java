package Pong;

import Pong.Game.Game;
import Pong.Gui.Controllers.GameController;
import Pong.Gui.Controls;
import Pong.Network.Connection;
import Pong.Network.PacketHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
        Game game = new Game(this, "hello", "jello");
        GameController controller = new GameController(game, 0.7);

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "Gui/Templates/game_pane.fxml"
                )
        );
        loader.setController(controller);

        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        scene.lookup("#field").requestFocus();
    }

    public long getTime() {
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