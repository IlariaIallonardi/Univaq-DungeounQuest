package exception;

public class SaveLoadException extends Exception {
    public SaveLoadException() { super(); }
    public SaveLoadException(String message) { super(message); }
    public SaveLoadException(String message, Throwable cause) { super(message, cause); }
}
