package exception;

public class InventoryFullException extends GameException {
    public InventoryFullException() { super(); }
    public InventoryFullException(String message) { super(message); }
}
