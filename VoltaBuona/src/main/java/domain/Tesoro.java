package domain;


public class Tesoro extends Oggetto {

    private int valore;

    public Tesoro(int valore,  int id, String nome, String descrizione, boolean usabile, boolean equipaggiabile, boolean trovato) {
        super(id, nome, descrizione, usabile, equipaggiabile, trovato);
        this.valore = valore;
    } 

   





    public int getValore() {
        return valore;
    }

    public void setValore(int valore) {
        this.valore = valore;
    }

   @Override
   public boolean eseguiEffetto(Personaggio personaggio) {
    if (personaggio == null) return false;
    personaggio.addMonete(this.valore);
    return true;
}
}
