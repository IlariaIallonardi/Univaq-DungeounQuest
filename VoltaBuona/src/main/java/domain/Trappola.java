package domain;

public class Trappola extends Oggetto {
    private String messaggio;
    private boolean durata; // se persiste

    public String checkDiDisinnesco() {
        // ritorna esito test disinnesco
        return "Fallito";
    }

    public boolean alterareStato() {
        // cambia stato stanza/giocatore
        return true;
    }

    @Override
    public boolean usare() { return false; }
}
