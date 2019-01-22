package Pong.Network.Exceptions;

public class PacketException extends Exception {
    public PacketException() {
        super();
    }

    public PacketException(String s) {
        super(s);
    }

    public PacketException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PacketException(Throwable throwable) {
        super(throwable);
    }

    protected PacketException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
