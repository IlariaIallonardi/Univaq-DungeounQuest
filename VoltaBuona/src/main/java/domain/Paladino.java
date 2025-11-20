package domain;

public class Paladino extends Personaggio {

    public Paladino(int attacco, int difesa, int esperienza, int id, int livello, String nomeP, Stanza posizioneCorrente, int puntiMana, int puntiVita, String statoG, Zaino zaino) {
        super(attacco, difesa, esperienza, id, livello, nomeP, posizioneCorrente, puntiMana, puntiVita, statoG, zaino);
    }

    public Paladino(String nome) {
        this(12, 0, 0, 0, 1, "nome", null, 50, 120, "OK", null);
    }
}
