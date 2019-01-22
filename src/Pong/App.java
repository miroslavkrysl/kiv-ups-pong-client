package Pong;

import Pong.Game.Game;
import Pong.Gui.Controllers.*;
import Pong.Gui.Controls;
import Pong.Gui.SceneType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application{
    private static final String GAME_TEMPLATE = "Gui/Templates/game.fxml";
    private static final String GAME_OVER_TEMPLATE = "Gui/Templates/game_over.fxml";
    private static final String LOGIN_TEMPLATE = "Gui/Templates/login.fxml";
    private static final String LOBBY_TEMPLATE = "Gui/Templates/lobby.fxml";
    private static final String WAITING_TEMPLATE = "Gui/Templates/waiting.fxml";
    private static final String UNAVAILABLE_TEMPLATE = "Gui/Templates/unavailable.fxml";
    private static final String CONNECTING_TEMPLATE = "Gui/Templates/connecting.fxml";
    private static final double GAME_SIZE_RATIO = 0.5;

    private Operator operator;
    private Controls controls;
    private Stage stage;

    private Scene loginScene;
    private Scene waitingScene;
    private Scene unavailableScene;
    private Scene connectingScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        controls = new Controls();
        operator = new Operator(this);

        // setup scenes
        this.loginScene = createScene(new LoginController(operator), LOGIN_TEMPLATE);
        this.waitingScene = createScene(new WaitingController(operator), WAITING_TEMPLATE);
        this.unavailableScene = createScene(new UnavailableController(operator), UNAVAILABLE_TEMPLATE);
        this.connectingScene = createScene(new ConnectingController(operator), CONNECTING_TEMPLATE);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setScene(loginScene);
        stage.show();
    }

    private void centerStage() {
        Platform.runLater(() -> {
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        });
    }

    void switchScene(Scene scene) {
        Platform.runLater(() -> {
            stage.setScene(scene);
            scene.getRoot().requestFocus();
            centerStage();
        });
    }

    public Scene createGameScene(Game game) {
        return createScene(new GameController(game, GAME_SIZE_RATIO), GAME_TEMPLATE);
    }

    public Scene createGameOverScene(Operator operator, Game game) {
        return createScene(new GameOverController(operator, game), GAME_OVER_TEMPLATE);
    }

    public Scene createLobyScene(Operator operator) {
        return createScene(new LobbyController(operator), LOBBY_TEMPLATE);
    }

    public Scene getLoginScene() {
        return loginScene;
    }

    public Scene getWaitingScene() {
        return waitingScene;
    }

    public Scene getUnavailableScene() {
        return unavailableScene;
    }

    private Scene createScene(Object controller, String rootPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rootPath));
            loader.setController(controller);
            return new Scene(loader.load());
        } catch (IOException e) {
            System.out.println("can not create scene");
            Platform.exit();
        }
        return null;
    }

    public Controls getControls() {
        return controls;
    }

    public Scene getConnectingScene() {
        return connectingScene;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        operator.stop();
    }

    public Scene getCurrentScene() {
        return stage.getScene();
    }
}