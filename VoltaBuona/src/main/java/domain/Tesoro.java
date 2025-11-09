package domain;

public class Tesoro extends Oggetto {
    private Boolean trovato;
    private int valore;


public Tesoro(int id, String nome, String descrizione, int valore, boolean usabile, boolean equipaggiabile, boolean trovato) {
    super(id, nome, descrizione, usabile, equipaggiabile, trovato);
    this.valore = valore;
    this.trovato = trovato;
}

    
   

    public int getValore() {
        return valore;
    }

    public Boolean getTrovato() {
        return trovato;
    }

    public void setTrovato(Boolean trovato) {
        this.trovato = trovato;
    }

  
    @Override
    public boolean eseguiEffetto(Personaggio personaggio) {
        if (personaggio == null) return false;
        // se già raccolto, non fare nulla
        if (isTrovato() || Boolean.TRUE.equals(this.trovato)) return false;

        // segna il tesoro come trovato (sincronizza il campo locale se presente)
        setTrovato(true);
        this.trovato = true;

        // applica l'effetto: aggiunge il valore al TESORO del personaggio
        personaggio.setEsperienza(personaggio.getEsperienza() + this.valore);//DA CAMBIARE

        return true;
    }

}
