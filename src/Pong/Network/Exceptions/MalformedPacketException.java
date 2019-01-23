package Pong.Network.Exceptions;

public class MalformedPacketException extends PacketException {
    public MalformedPacketException() {
        super();
    }

    public MalformedPacketException(String s) {
        super(s);
    }

    public MalformedPacketException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public MalformedPacketException(Throwable throwable) {
        super(throwable);
    }

    protected MalformedPacketException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
