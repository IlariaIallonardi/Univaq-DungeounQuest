package domain;

public class Arma extends Oggetto {
    public enum Tipo {
        ASCIA, SPADA, PUGNALE, ARCO
    }

    private Tipo tipo;
    private int danno;
    private int dannoBonus;
    private int durabilitaArma;

    public int aumentaAttacco() {
        return danno + dannoBonus;
    }

    public boolean miglioraAttacco() {
        return true;
    }

    @Override
    public boolean usare() {
        return false;
    }
}
