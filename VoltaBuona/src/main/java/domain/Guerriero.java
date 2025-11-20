package domain;

public class Guerriero extends Personaggio {

    public Guerriero(String nome) {
        this(12, 6, 0, 0, 1, "nome", null, 0, 100, "OK", null);
    }

    public Guerriero(int attacco, int difesa, int esperienza, int id, int livello, String nomeP, Stanza posizioneCorrente, int puntiMana, int puntiVita, String statoG, Zaino zaino) {
        super(attacco, difesa, esperienza, id, livello, nomeP, posizioneCorrente, puntiMana, puntiVita, statoG, zaino);
    }
     public boolean proteggi(Personaggio target) {
        if (target == null) return false;
        // esempio di controllo: richiedi che il guerriero abbia almeno 1 PV
        if (this.getPuntiVita() <= 0) return false;
        target.applicaProtezione();
        return true;
    }

}
