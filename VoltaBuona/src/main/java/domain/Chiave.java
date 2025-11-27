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


  /*  @Override
public boolean eseguiEffetto(Personaggio personaggio) {
    if (personaggio == null) return false;

    Stanza stanza = personaggio.getPosizioneCorrente();
    if (stanza == null) return false;

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

    if (!this.getNome().equalsIgnoreCase(richiesta.getNome())) {
        System.out.println("La chiave non corrisponde! Non puoi aprire la stanza.");
        return false;
    }

    // 3 Sblocca la stanza
    stanza.sblocca();
    System.out.println(" Hai usato la chiave e hai sbloccato la stanza!");
    return true;
}*/
// ...existing code...
   @Override
   public boolean eseguiEffetto(Personaggio personaggio) {
       if (personaggio == null) return false;

       Stanza stanza = personaggio.getPosizioneCorrente();
       if (stanza == null) return false;

       if (!stanza.isBloccata()) {
           System.out.println("La stanza non è bloccata.");
           return false;
       }

       Chiave richiesta = stanza.getChiaveRichiesta();
       if (richiesta == null) {
           System.out.println("Questa stanza non richiede chiavi per essere aperta.");
           return false;
       }

       boolean match = false;
       // 1) confronta idStanzaDestinazione se impostato
       if (this.idStanzaDestinazione > 0 && this.idStanzaDestinazione == richiesta.getIdStanzaDestinazione()) {
           match = true;
       }
       // 2) confronta id della chiave (ereditato da Oggetto)
       if (!match) {
           try {
               if (this.getId() == richiesta.getId()) match = true;
           } catch (Throwable ignored) {}
       }
       // 3) confronto per nome (null-safe)
       if (!match) {
           String n1 = this.getNome();
           String n2 = richiesta.getNome();
           if (n1 != null && n2 != null && n1.equalsIgnoreCase(n2)) match = true;
       }

       if (!match) {
           System.out.println("La chiave non corrisponde! Non puoi aprire la stanza.");
           return false;
       }

       // sblocca la stanza
       stanza.sblocca();
       System.out.println("Hai usato la chiave e hai sbloccato la stanza!");

       // se la chiave è monouso la rimuoviamo dallo zaino del personaggio
       if (this.unicaApertura()) {
           try {
               Zaino z = personaggio.getZaino();
               if (z != null) {
                   z.rimuovi(this);
               }
               this.setTrovato(false);
           } catch (Throwable ignored) {}
       }

       return true;
    }

}
