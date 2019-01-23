package Pong;

import Pong.Game.Game;
import Pong.Gui.Controllers.*;
import Pong.Gui.Controls;
import Pong.Network.Connection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application{
    private static final String GAME_TEMPLATE = "Gui/templates/game.fxml";
    private static final String GAME_OVER_TEMPLATE = "Gui/templates/game_over.fxml";
    private static final String LOGIN_TEMPLATE = "Gui/templates/login.fxml";
    private static final String LOBBY_TEMPLATE = "Gui/templates/lobby.fxml";
    private static final String WAITING_TEMPLATE = "Gui/templates/waiting.fxml";
    private static final String UNAVAILABLE_TEMPLATE = "Gui/templates/unavailable.fxml";

    private static final double GAME_SIZE_RATIO = 0.5;

    private App app;
    private Controls controls;
    private Stage stage;

    private Scene loginScene;
    private Scene waitingScene;
    private Scene unavailableScene;

    private LoginController loginController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        this.controls = new Controls();
        this.app = new App(this);

        // setup scenes
        this.loginController = new LoginController(app);
        this.loginScene = createScene(loginController, LOGIN_TEMPLATE);
        this.unavailableScene = createScene(new UnavailableController(app), UNAVAILABLE_TEMPLATE);
        this.waitingScene = createScene(new WaitingController(app), WAITING_TEMPLATE);
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
        });
    }

    public Scene createLobbyScene(App app, Connection connection) {
        return createScene(new LobbyController(app, connection), LOBBY_TEMPLATE);
    }

    public Scene createGameScene(Game game) {
        return createScene(new GameController(app, controls, game, GAME_SIZE_RATIO), GAME_TEMPLATE);
    }

    public Scene createGameOverScene(App app, Game game, String message) {
        return createScene(new GameOverController(app, game, message), GAME_OVER_TEMPLATE);
    }

    public Scene getLoginScene() {
        return loginScene;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public Scene getWaitingScene() {
        return waitingScene;
    }

    public Scene getUnavailableScene() {
        return unavailableScene;
    }

    public Scene getCurrentScene() {
        return stage.getScene();
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

    @Override
    public void stop() throws Exception {
        super.stop();
        app.getConnection().stop();
    }
}