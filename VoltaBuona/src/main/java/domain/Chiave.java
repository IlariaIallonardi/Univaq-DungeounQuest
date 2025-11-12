package domain;

/*public class Chiave extends Oggetto {
    private int idStanzaDestinazione;

    public Chiave(int idStanzaDestinazione, int id, String nome, String descrizione, boolean usabile, boolean equipaggiabile, boolean trovato) {
        super(id, nome, descrizione, usabile, equipaggiabile, trovato);
        this.idStanzaDestinazione = idStanzaDestinazione;
    }


    public boolean aperturaStanza() {
        return idStanzaDestinazione > 0;
    }

    public boolean unicaApertura() {
        // se la chiave si consuma
        return true;
    }

    @Override
    public boolean usare(Personaggio personaggio) {
        // usa la chiave per aprire una stanza/passaggio
        return aperturaStanza();
    }

    

    public int getIdStanzaDestinazione() {
        return idStanzaDestinazione;
    }

    public void setIdStanzaDestinazione(int idStanzaDestinazione) {
        this.idStanzaDestinazione = idStanzaDestinazione;
    }


    @Override
    public boolean eseguiEffetto(Personaggio personaggio) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eseguiEffetto'");
    }
}*/
public class Chiave extends Oggetto {

    public Chiave(String nome) {
        super(nome);
    }
}
