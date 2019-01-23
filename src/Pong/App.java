package Pong;

import Pong.Game.Game;
import Pong.Network.Connection;
import Pong.Network.PacketHandler;
import javafx.beans.property.SimpleStringProperty;

public class App {

    private Main main;
    private Connection connection;
    private PacketHandler PacketHandler;
    private Game game;

    public App(Main main) {
        this.main = main;
        this.packetHandler = new PacketHandler()et();
        this.connection = new Connection();
        this.game = null;
    }


}
