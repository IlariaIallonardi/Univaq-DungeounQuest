package exception;

/**
 * Eccezione personalizzata per il gioco Dungeon.
 * 
 * Viene sollevata quando si verificano condizioni di errore specifiche
 * all'interno del flusso di esecuzione del gioco, consentendo di gestire
 * in modo concentrato problemi imprevisti o situazioni anomale.
 */

public class DungeonException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DungeonException() {
        super();
    }

    public DungeonException(String message) {
        super(message);
    }

    public DungeonException(String message, Throwable cause) {
        super(message, cause);
    }

    public DungeonException(Throwable cause) {
        super(cause);
    }

}