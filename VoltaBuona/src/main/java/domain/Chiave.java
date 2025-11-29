package domain;

public class Chiave extends Oggetto {

    private String nomeChiave;
    public String getNomeChiave;
    public Chiave( int id, String nome, String descrizione, boolean usabile, boolean equipaggiabile, boolean trovato, String nomeChiave) {
        super(id, nome, descrizione, usabile, equipaggiabile, trovato);
        this.nomeChiave = nomeChiave;

    }


    public boolean unicaApertura() {
        // se la chiave si consuma
        return true;
    }
    
    public String getNomeChiave() {
        return nomeChiave;
    }
    public void setNomeChiave(String nomeChiave) {
        this.nomeChiave = nomeChiave;
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
        boolean corrisponde = false;

        // se la chiave ha targetStanzaName, confrontalo con il nome della stanza
        String nomeStanza = stanza.getNomeStanza();
        if (this.nomeChiave != null && nomeStanza != null) {
            if (this.nomeChiave.trim().toLowerCase().equals(nomeStanza)) {
                corrisponde = true;
                stanza.sblocca();
                System.out.println("Hai usato la chiave e hai sbloccato la stanza: " + (nomeStanza));
            }else {
                System.out.println("La chiave non corrisponde! Non puoi aprire la stanza.");
                return false;
            }
        }
        return corrisponde;
}
}
