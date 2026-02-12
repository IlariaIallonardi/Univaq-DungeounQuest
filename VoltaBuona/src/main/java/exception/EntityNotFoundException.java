package exception;

public class EntityNotFoundException extends GameException {
    public EntityNotFoundException() { super(); }
    public EntityNotFoundException(String message) { super(message); }
}
