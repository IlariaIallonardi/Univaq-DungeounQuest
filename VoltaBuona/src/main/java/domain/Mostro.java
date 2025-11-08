package it.univaq.dungeon.personaggi;

public class Mostro extends Personaggio {
    private int danno;

    @Override
    public int calcolaDannoBase() {
        return danno;
    }

    public int getDanno() { return danno; }
}
