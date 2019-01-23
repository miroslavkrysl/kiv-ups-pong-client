package Pong.Network.Exceptions;

public class BadNicknameException extends ConnectionException {
    public BadNicknameException() {
        super();
    }

    public BadNicknameException(String s) {
        super(s);
    }

    public BadNicknameException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BadNicknameException(Throwable throwable) {
        super(throwable);
    }

    protected BadNicknameException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
