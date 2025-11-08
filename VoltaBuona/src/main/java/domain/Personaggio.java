package domain;

import domain.Stanza;

public abstract class Personaggio {
    protected int id;
    protected String nome;
    protected int puntiVita;
    protected int attacco;
    protected int livello;
    protected int esperienza;
    protected Stanza posizioneCorrente;

    public abstract int calcolaDannoBase();

    // getters/setters
    public String getNome() {
        return nome;
    }

    public int getAttacco() {
        return attacco;
    }
}
