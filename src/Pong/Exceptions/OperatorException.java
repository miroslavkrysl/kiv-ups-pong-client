package Pong.Exceptions;

public class OperatorException extends Exception {
    public OperatorException() {
        super();
    }

    public OperatorException(String s) {
        super(s);
    }

    public OperatorException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public OperatorException(Throwable throwable) {
        super(throwable);
    }

    protected OperatorException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
