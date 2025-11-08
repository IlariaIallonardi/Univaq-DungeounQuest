package domain;

public class Chiave extends Oggetto {
    private int idStanzaDestinazione;

    public boolean aperturaStanza() {
        return idStanzaDestinazione > 0;
    }

    public boolean unicaApertura() {
        // se la chiave si consuma
        return true;
    }

    @Override
    public boolean usare() {
        // usa la chiave per aprire una stanza/passaggio
        return aperturaStanza();
    }

    public int getIdStanzaDestinazione() {
        return idStanzaDestinazione;
    }

    public void setIdStanzaDestinazione(int idStanzaDestinazione) {
        this.idStanzaDestinazione = idStanzaDestinazione;
    }
}
