package domain;

public class Evento {
    private int id;
    private boolean inizioEvento;
    private boolean fineEvento;
    private String descrizione;

    public boolean attivo() {
        return inizioEvento && !fineEvento;
    }

    public boolean èRiutilizzabile() {
        // regola di riutilizzo
        return false;
    }
}
