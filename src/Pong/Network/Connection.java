package Pong.Network;

import Pong.App;
import Pong.Network.Exceptions.*;
import com.sun.istack.internal.NotNull;

import java.io.*;
import java.net.*;

public class Connection implements Runnable {

    private static final int SOCKET_TIMEOUT = 3000;
    private static final long INACTIVE_TIMEOUT = 5000;

    private App app;
    private Socket socket;
    private InetSocketAddress address;
    private BufferedWriter sender;
    private BufferedReader receiver;

    boolean shouldStop;
    boolean inactive;

    public Connection(App app, String ip, int port) throws IOException {
        this.app = app;
        this.address = new InetSocketAddress(ip, port);

        this.socket = new Socket();
        this.socket.connect(address);
        this.socket.setSoTimeout(SOCKET_TIMEOUT);

        this.sender = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        this.shouldStop = false;
    }

    public void send(@NotNull Packet packet) throws ConnectionClosedException {
        if (socket == null) {
            throw new ConnectionClosedException("can not write to closed connection");
        }

        sender.write(packet.serialize());
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
                    inactive = true;
                    //TODO: handleInactive;
                }

                // send poke packet
                send(new Packet("poke"));

                continue;
            }
            catch (IOException e) {
                System.out.println("ERROR: can't read from socket");
                // TODO: handle disconnected
                break;
            }

            if (bytesRead == 0) {
                // server disconnected
                // TODO: handle disconnected
                break;
            }

            if (inactive) {
                inactive = false;
                //TODO: handleInactive;
            }

            lastActiveAt = System.currentTimeMillis();
            data += new String(buffer, 0, bytesRead);

            while (true) {
                String[] result = data.split(Packet.TERMINATOR, 2);

                if (result.length == 1) {
                    break;
                }

                data = result[1];


                try {
                    Packet packet = Packet.parse(result[0]);
                    app.getPacketHandler().handleIncoming(packet);
                }
                catch (PacketException exception) {
                    // TODO
                }
            }

            if (data.length() > Packet.MAX_SIZE) {
                // corrupted stream
                // TODO disconnect
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

    private void close() {
        try {
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
}
