package Pong.Network;

import Pong.App;
import java.io.IOException;
import java.net.Socket;

public class Connection implements Runnable {

    private static final int SOCKET_TIMEOUT = 3000;

    private App app;
    private Socket socket;

    public Connection(String ip, int port) throws IOException {
       socket = new Socket(ip, port);
       socket.setSoTimeout(SOCKET_TIMEOUT);
    }

    public void send(Packet packet) {
        // TODO: send
    }

    @Override
    public void run() {
        // TODO: recvloop
        // TODO: conectivity checking
    }
}
