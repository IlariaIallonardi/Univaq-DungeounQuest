package domain;

public class Arma extends Evento{
    private int danni;
    private boolean usabile;

    public int getDanni() {
        return danni;
    }

    public void setDanni(int danni) {
        this.danni = danni;
    }

    public boolean isUsabile() {
        return usabile;
    }

    public void setUsabile(boolean usabile) {
        this.usabile = usabile;
    }
}
