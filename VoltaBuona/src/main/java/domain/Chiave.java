package domain;

public class Chiave extends Oggetto {

    public Chiave(int id, String nome, String descrizione, boolean usabile, boolean equipaggiabile, boolean trovato) {
        super(id, nome, descrizione, usabile, equipaggiabile, trovato);

    }

    public boolean unicaApertura() {
        // se la chiave si consuma
        return true;
    }

    /*  @Override
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
    } */
    @Override
    public boolean eseguiEffetto(Personaggio personaggio) {
        Stanza stanza = personaggio.getPosizioneCorrente();
        if (personaggio == null || stanza == null) {
            return false;
        }

        // 1 Se la stanza non è bloccata → inutile usare la chiave
        if (!stanza.isBloccata()) {
            System.out.println("La stanza non è bloccata.");
            return false;
        }
        // 2 Verifica che la chiave corrisponda: prima confronto targetStanzaName (se impostato), altrimenti confronto con la chiave richiesta della stanza (nome)
    

        Chiave richiesta = stanza.getChiaveRichiesta();
        if (richiesta == null) {
            return false;
        }

        if (this.getId() == richiesta.getId()) {
            stanza.sblocca();

            System.out.println("Hai sbloccato la stanza con la chiave id " + getId());
            return true;
        } else {
            System.out.println("La chiave non corrisponde (id " + getId() + ")");
            return false;
        }
    }
}
