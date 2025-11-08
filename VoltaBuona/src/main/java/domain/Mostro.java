package domain;

public class Mostro extends Personaggio {
    private int danno;

    @Override
    public int calcolaDannoBase() {
        return danno;
    }

    public int getDanno() {
        return danno;
    }
}
