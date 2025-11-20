package domain;

public class Mago extends Personaggio {

    public Mago(String nome) {
        this(15, 0, 0, 1, 0, "nome", null, 100, 60, "OK", null);
    }

    public Mago(int attacco, int difesa, int esperienza, int id, int livello, String nomeP, Stanza posizioneCorrente, int puntiMana, int puntiVita, String statoG, Zaino zaino) {
        super(attacco, difesa, esperienza, id, livello, nomeP, posizioneCorrente, puntiMana, puntiVita, statoG, zaino);
    }
}
