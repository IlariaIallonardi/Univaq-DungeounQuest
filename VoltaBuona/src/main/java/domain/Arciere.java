package domain;

public class Arciere extends Personaggio {

    public Arciere(int attacco, int difesa, int esperienza, int id, int livello, String nomeP, Stanza posizioneCorrente, int puntiMana, int puntiVita, String statoG, Zaino zaino) {
        super(attacco, difesa, esperienza, id, livello, nomeP, posizioneCorrente, puntiMana, puntiVita, statoG, zaino);
    }

    public Arciere(String nome) {
        this(14, 8, 0, 0, 1, nome, null, 30, 80, "OK", null);
    }
}
