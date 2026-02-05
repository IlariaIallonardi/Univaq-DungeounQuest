package domain;

public class Chiave extends Oggetto {

   private int idStanzaDaAprire;

    public Chiave(int id, String nome, String descrizione, boolean usabile, boolean equipaggiabile, boolean trovato, int idStanzaDaAprire) {
        super(id, nome, descrizione, usabile, equipaggiabile, trovato);
        this.idStanzaDaAprire = idStanzaDaAprire;
    }

   

    
    public int getIdStanzaDaAprire() {
        return idStanzaDaAprire;
    }

    public void setIdStanzaDaAprire(int idStanzaDaAprire) {
        this.idStanzaDaAprire = idStanzaDaAprire;
    }

    @Override
    public boolean eseguiEffetto(Personaggio personaggio) {
        Stanza stanza = personaggio.getPosizioneCorrente();
        if (personaggio == null || stanza == null) {
            return false;
        }

        
        if (!stanza.isBloccata()) {
            System.out.println("La stanza non è bloccata.");
            return false;
        }

        Chiave richiesta = stanza.getChiaveRichiesta();
        if (richiesta == null) {
            return false;
        }

        if (this.getIdStanzaDaAprire() == stanza.getId()) {
            stanza.sblocca();
            System.out.println("Hai sbloccato la stanza con la chiave id " + getId());
            return true;

        } else {
            System.out.println("La chiave non corrisponde (id " + getId() + ")");
            return false;
        }
    }
}
