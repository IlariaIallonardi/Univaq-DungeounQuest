package domain;

public class Chiave extends Oggetto {

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

    public int getIdStanzaDestinazione() {
        return idStanzaDestinazione;
    }

    public void setIdStanzaDestinazione(int idStanzaDestinazione) {
        this.idStanzaDestinazione = idStanzaDestinazione;
    }

    @Override
    public boolean eseguiEffetto(Personaggio personaggio) {
        if (personaggio == null) {
            return false;
        }

        Stanza stanza = personaggio.getPosizioneCorrente();
        if (stanza == null) {
            return false;
        }

        // 1 Se la stanza non è bloccata → inutile usare la chiave
        if (!stanza.isBloccata()) {
            System.out.println("La stanza non è bloccata.");
            return false;
        }

        // 2 Verifica che la chiave corrisponda a quella richiesta
        Chiave richiesta = stanza.getChiaveRichiesta();
        if (richiesta == null) {
            System.out.println("Questa stanza non richiede chiavi per essere aperta.");
            return false;
        }

        if (!this.getNome().equals(richiesta.getNome())) {
            System.out.println("La chiave non corrisponde! Non puoi aprire la stanza.");
            return false;
        }

        // 3 Sblocca la stanza
        stanza.sblocca();
        System.out.println(" Hai usato la chiave e hai sbloccato la stanza!");
        return true;
    }

}
