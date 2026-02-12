package exception;

public class SerializationCompatibilityException extends GameException {
    public SerializationCompatibilityException() { super(); }
    public SerializationCompatibilityException(String message) { super(message); }
    public SerializationCompatibilityException(String message, Throwable cause) { super(message, cause); }
}
