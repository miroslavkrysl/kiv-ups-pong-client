package Pong.Network;

import Pong.App;
import Pong.Network.Exceptions.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * The class Connection represent a connection to the server.
 */
public class Connection {

    enum ConnectingStatus {
        /**
         * Pending connecting status.
         */
        PENDING,
        /**
         * Logged connecting status.
         */
        LOGGED,
        /**
         * Not logged connecting status.
         */
        NOT_LOGGED,
        /**
         * Refused connecting status.
         */
        REFUSED,
    }

    private static final int SOCKET_TIMEOUT = 3000;
    private static final long INACTIVE_TIMEOUT = 5000;
    private static final long CONNECTING_WAITING_TIME = 10000;

    private App app;
    private PacketHandler packetHandler;
    private Thread receiveThread;
    private Socket socket;
    private InetSocketAddress address;
    private PrintWriter sender;
    private BufferedReader receiver;

    private BooleanProperty unavailable;
    private BooleanProperty closed;

    private ConnectingStatus connectingStatus;

    private long timeDifference;
    private long latency;

    /**
     * Instantiates a new Connection.
     *
     * @param app the app instance
     */
    public Connection(App app) {
        this.app = app;
        this.packetHandler = new PacketHandler(app, this);
        this.receiveThread = null;
        this.address = null;
        this.socket = null;
        this.unavailable = new SimpleBooleanProperty(false);
        this.closed = new SimpleBooleanProperty(true);

        this.timeDifference = 0;
        this.latency = 0;
    }

    /**
     * Creates a new socket for the connection, runs the receiving thread, send the login packet
     * and waits for the logged packet response.
     * It may takes a while because of sending a couple of time synchronization packets.
     *
     * @param address  the address
     * @param nickname the nickname
     */
    synchronized public void connect(InetSocketAddress address, String nickname) throws ConnectionException {
        if (socket != null) {
            close();
        }
        try {
            Socket socket = new Socket();
            socket.connect(address);
            socket.setSoTimeout(SOCKET_TIMEOUT);
            PrintWriter sender = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.address = address;
            this.socket = socket;
            this.sender = sender;
            this.receiver = receiver;
        }
        catch (IOException exception) {
            throw new CantConnectException("can not create socket for connection");
        }

        this.receiveThread = new Thread(this::receive);
        this.receiveThread.start();

        connectingStatus = ConnectingStatus.PENDING;
        send(new Packet("login", nickname));

        while (connectingStatus == ConnectingStatus.PENDING) {
            try {
                wait(CONNECTING_WAITING_TIME);
            } catch (InterruptedException e) {
                continue;
            }

            if (connectingStatus == ConnectingStatus.LOGGED) {
                break;
            }

            if (connectingStatus == ConnectingStatus.NOT_LOGGED) {
                close();
                throw new BadNicknameException("connecting is taking too much time");
            }

            if (connectingStatus == ConnectingStatus.REFUSED) {
                close();
                throw new ConnectingRefusedException("connecting is taking too much time");
            }

            close();
            throw new ConnectingTimeoutException("connecting is taking too much time");
        }

        for (int i = 0; i < 10; i++) {
            send(new Packet("time", Long.toString(System.currentTimeMillis())));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // nothing to do
            }
        }
    }

    /**
     * Send the packet to the server.
     *
     * @param packet the packet
     */
    synchronized public void send(Packet packet) {
        if (socket == null) {
            System.out.println("can not send data to the closed connection");
            return;
        }

        sender.write(packet.serialize());
        sender.flush();
    }

    /**
     * Disconnect the connection.
     */
    public void close() {
        try {
            this.socket.shutdownInput();
            this.socket.shutdownOutput();
            this.sender.close();
            this.receiver.close();
            socket.close();
            closed.set(true);
        }
        catch (IOException exception) {
            // already closed
        }

        socket = null;
        address = null;
        sender = null;
        receiver = null;
    }

    /**
     * Constantly receives packets from the server, passes them to the packet handler
     * and updates the connection state.
     */
    private void receive() {
        long lastActiveAt = System.currentTimeMillis();
        char[] buffer = new char[1024];
        String data = "";

        while (socket != null) {

            int bytesRead;

            try {
                bytesRead = receiver.read(buffer);
            }
            catch (SocketTimeoutException exception) {
                // receive timeout
                long now = System.currentTimeMillis();
                long inactiveFor = now - lastActiveAt;

                if (inactiveFor > INACTIVE_TIMEOUT) {
                    unavailable.set(true);
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

            if (isUnavailable()) {
                unavailable.set(false);
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
                packetHandler.handle(packet);
            }

            if (data.length() > Packet.MAX_SIZE) {
                System.out.println("too long data stream to be a valid packet");
                break;
            }
        }

        close();
    }


    /**
     * Sets the connection connecting status.
     *
     * @param status the status
     */
    synchronized void setConnectingStatus(ConnectingStatus status) {
        this.connectingStatus = status;
        notifyAll();
    }

    /**
     * Gets the current server time.
     *
     * @return the time
     */
    public long getTime() {
        return System.currentTimeMillis() + timeDifference;
    }

    /**
     * Updates the current server time.
     *
     * @param sendTime   the send time
     * @param serverTime the server time
     */
    public void updateTime(long sendTime, long serverTime) {
        long now = System.currentTimeMillis();

        this.latency = (3 * latency + (now - sendTime)) / 4;
        this.timeDifference = (serverTime - sendTime) + (latency / 2);
    }

    /**
     * Gets the connection address.
     *
     * @return the address
     */
    public InetSocketAddress getAddress() {
        return address;
    }

    /**
     * Is unavailable boolean.
     *
     * @return the boolean
     */
    public boolean isUnavailable() {
        return unavailable.get();
    }

    /**
     * Unavailable property read only boolean property.
     *
     * @return the read only boolean property
     */
    public ReadOnlyBooleanProperty unavailableProperty() {
        return unavailable;
    }

    /**
     * Is closed boolean.
     *
     * @return the boolean
     */
    public boolean isClosed() {
        return closed.get();
    }

    /**
     * Closed property read only boolean property.
     *
     * @return the read only boolean property
     */
    public ReadOnlyBooleanProperty closedProperty() {
        return closed;
    }
}
