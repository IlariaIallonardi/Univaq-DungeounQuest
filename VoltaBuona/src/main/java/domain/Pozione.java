package it.univaq.dungeon.oggetti;

public class Pozione extends Oggetto {
    public enum Tipo { CURA, MANA }
    private Tipo tipo;
    private int valorePozione;

    public int aumentoPuntiVita() {
        return tipo == Tipo.CURA ? valorePozione : 0;
    }

    public int aumentoMana() {
        return tipo == Tipo.MANA ? valorePozione : 0;
    }

    @Override
    public boolean usare() {
        // applica effetto al personaggio
        return true;
    }
}
