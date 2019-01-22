package Pong.Network;

import Pong.Game.BallState;
import Pong.Game.PlayerState;
import Pong.Game.Types.Side;
import Pong.Network.Exceptions.ConnectionException;
import Pong.Operator;
import com.sun.istack.internal.NotNull;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.*;
import java.net.*;

public class Connection implements Runnable {

    private static final int SOCKET_TIMEOUT = 3000;
    private static final long INACTIVE_TIMEOUT = 5000;

    private Operator operator;
    private Socket socket;
    private InetSocketAddress address;
    private PrintWriter sender;
    private BufferedReader receiver;

    boolean shouldStop;

    private BooleanProperty inactive;
    private BooleanProperty closed;

    public Connection(Operator operator, String ip, int port) throws ConnectionException {
        this.operator = operator;
        this.address = new InetSocketAddress(ip, port);

        try {
            this.socket = new Socket();
            this.socket.connect(address);
            this.socket.setSoTimeout(SOCKET_TIMEOUT);
            this.sender = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException exception) {
            throw new ConnectionException("can not create socket for connection");
        }

        this.shouldStop = false;
        this.inactive = new SimpleBooleanProperty(false);
        this.closed = new SimpleBooleanProperty(false);
    }

    synchronized public void send(@NotNull Packet packet) {
        if (socket == null) {
            return;
        }

        sender.write(packet.serialize());
        sender.flush();
    }

    @Override
    public void run() {
        if (socket == null) {
            return;
        }

        long lastActiveAt = System.currentTimeMillis();
        char[] buffer = new char[1024];
        String data = "";

        while (!shouldStop) {

            int bytesRead;

            try {
                bytesRead = receiver.read(buffer);
            }
            catch (SocketTimeoutException exception) {
                // receive timeout
                long now = System.currentTimeMillis();
                long inactiveFor = now - lastActiveAt;

                if (inactiveFor > INACTIVE_TIMEOUT) {
                    inactive.set(true);
                }

                // send poke packet
                send(new Packet("poke"));

                continue;
            }
            catch (IOException e) {
                // stream closed
                break;
            }

            if (bytesRead == -1) {
                // stream closed
                break;
            }

            if (inactive.get()) {
                inactive.set(false);
            }

            lastActiveAt = System.currentTimeMillis();
            data += new String(buffer, 0, bytesRead);

            while (true) {
                String[] result = data.split(Packet.TERMINATOR, 2);

                if (result.length == 1) {
                    break;
                }

                data = result[1];

                Packet packet = Packet.parse(result[0]);
                handle(packet);
            }

            if (data.length() > Packet.MAX_SIZE) {
                System.out.println("too long data stream to be a valid packet");
                break;
            }
        }

        close();
    }

    public void stop() {
        this.shouldStop = true;

        try {
            this.socket.shutdownInput();
            this.socket.shutdownOutput();
        } catch (IOException e) {
            // already closed
        }
    }

    public void close() {
        try {
            closed.set(true);
            this.sender.close();
            this.receiver.close();
            socket.close();
        }
        catch (IOException exception) {
            // already closed
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public boolean isInactive() {
        return inactive.get();
    }

    public BooleanProperty inactiveProperty() {
        return inactive;
    }

    public boolean isClosed() {
        return closed.get();
    }

    public BooleanProperty closedProperty() {
        return closed;
    }

    private void handle(Packet packet) {
        try {
            switch (packet.getType()) {
                case "time": {
                    String[] items = packet.getItems();
                    long sendTime = Long.parseLong(items[0]);
                    long serverTime = Long.parseLong(items[1]);
                    operator.synchronize(sendTime, serverTime);
                    break;
                }
                case "poke":
                    send(new Packet("poke_back"));
                    break;
                case "poke_back":
                    break;
                case "not_logged":
                    operator.notLogged();
                    break;
                case "server_full":
                    operator.notLogged();
                    break;
                case "logged":
                    operator.logged();
                    break;
                case "joined":
                    operator.joinGame(Side.fromString(packet.getItems()[0]));
                    break;
                case "opponent_joined":
                    operator.opponentJoinedGame(packet.getItems()[0]);
                    break;
                case "left":
                    break;
                case "opponent_left":
                    operator.opponentLeft();
                    break;
                case "your_state":
                    operator.updateYourState(new PlayerState(packet.getItems()));
                    break;
                case "opponent_state":
                    operator.updateOpponentState(new PlayerState(packet.getItems()));
                    break;
                case "opponent_ready":
                    break;
                case "ball_hit":
                    operator.ballHit(new BallState(packet.getItems()));
                    break;
                case "new_round":
                    int scoreLeft = Integer.parseInt(packet.getItems()[0]);
                    int scoreRight = Integer.parseInt(packet.getItems()[1]);
                    operator.newRound(scoreLeft, scoreRight);
                    break;
                case "ball_released":
                    operator.ballReleased(new BallState(packet.getItems()));
                    break;
                case "game_over":
                    int sL = Integer.parseInt(packet.getItems()[0]);
                    int sR = Integer.parseInt(packet.getItems()[1]);
                    operator.gameOver(sL, sR);
                    break;
                case "game_ended":
                    operator.gameEnded();
                    break;
            }
        }
        catch (Exception exception) {
            System.out.println("ERROR: accepting unknown packet");
            exception.printStackTrace();
        }
    }
}
