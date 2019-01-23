package Pong.Network;

import Pong.App;
import Pong.Game.BallState;
import Pong.Game.PlayerState;
import Pong.Game.Types.Side;

public class PacketHandler {

    private App app;
    private Connection connection;

    public PacketHandler(App app, Connection connection) {
        this.app = app;
        this.connection = connection;
    }

    public void handle(Packet packet) {
        try {
            switch (packet.getType()) {
                case "time": {
                    packet.validateItemsCount(2);
                    String[] items = packet.getItems();
                    long sendTime = Long.parseLong(items[0]);
                    long serverTime = Long.parseLong(items[1]);
                    connection.updateTime(sendTime, serverTime);
                    break;
                }
                case "poke":
                    connection.send(new Packet("poke_back"));
                    break;
                case "poke_back":
                    break;
                case "logged":
                    connection.setConnectingStatus(Connection.ConnectingStatus.LOGGED);
                    break;
                case "not_logged":
                    connection.setConnectingStatus(Connection.ConnectingStatus.NOT_LOGGED);
                    break;
                case "server_full":
                    connection.setConnectingStatus(Connection.ConnectingStatus.REFUSED);
                    break;
                case "joined":
                    packet.validateItemsCount(1);
                    app.joined(Side.fromString(packet.getItems()[0]));
                    break;
                case "opponent_joined":
                    app.opponentJoinedGame(packet.getItems()[0]);
                    break;
                case "left":
                    break;
                case "opponent_left":
                    app.opponentLeft();
                    break;
                case "your_state":
                    packet.validateItemsCount(PlayerState.ITEMS_COUNT);
                    app.updateLocalState(new PlayerState(packet.getItems()));
                    break;
                case "opponent_state":
                    packet.validateItemsCount(PlayerState.ITEMS_COUNT);
                    app.updateOpponentState(new PlayerState(packet.getItems()));
                    break;
                case "opponent_ready":
                    app.opponentReady();
                    break;
                case "ball_hit":
                    packet.validateItemsCount(BallState.ITEMS_COUNT);
                    app.ballHit(new BallState(packet.getItems()));
                    break;
                case "new_round":
                    packet.validateItemsCount(2);
                    int scoreLeft = Integer.parseInt(packet.getItems()[0]);
                    int scoreRight = Integer.parseInt(packet.getItems()[1]);
                    app.newRound(scoreLeft, scoreRight);
                    break;
                case "ball_released":
                    packet.validateItemsCount(BallState.ITEMS_COUNT);
                    app.ballReleased(new BallState(packet.getItems()));
                    break;
                case "game_over":
                    packet.validateItemsCount(2);
                    int sL = Integer.parseInt(packet.getItems()[0]);
                    int sR = Integer.parseInt(packet.getItems()[1]);
                    app.gameOver(sL, sR);
                    break;
                case "game_ended":
                    app.gameEnded();
                    break;
            }
        }
    }
}
