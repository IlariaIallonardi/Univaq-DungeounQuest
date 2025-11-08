package domain;

public class Trappola extends Evento{
    private int danni;
    private String difficoltàDissinesco;

    public Trappola(int i) {
        super();
    }

    public int getDanni() {
        return danni;
    }

    public void setDanni(int danni) {
        this.danni = danni;
    }

    public String getDifficoltàDissinesco() {
        return difficoltàDissinesco;
    }

    public void setDifficoltàDissinesco(String difficoltàDissinesco) {
        this.difficoltàDissinesco = difficoltàDissinesco;
    }

    public Trappola(int danni, String difficoltàDissinesco) {
        this.danni = danni;
        this.difficoltàDissinesco = difficoltàDissinesco;
    }
}
