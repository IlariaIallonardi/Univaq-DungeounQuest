package exception;

public class InvalidActionException extends GameException {
    public InvalidActionException() { super(); }
    public InvalidActionException(String message) { super(message); }
}
