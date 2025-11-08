package domain;

public class Pozione extends Evento{
    private String nome;
    private String valore;
    private boolean consumabile;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValore() {
        return valore;
    }

    public void setValore(String valore) {
        this.valore = valore;
    }

    public boolean isConsumabile() {
        return consumabile;
    }

    public void setConsumabile(boolean consumabile) {
        this.consumabile = consumabile;
    }
}
