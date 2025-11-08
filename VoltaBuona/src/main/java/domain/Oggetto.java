package domain;

public abstract class Oggetto {
    protected int id;
    protected String nome;
    protected String descrizione;
    protected boolean usabile;
    protected boolean equipaggiabile;
    protected boolean trovato;

    public abstract boolean usare();

    public String getNome() { return nome; }
    public boolean isUsabile() { return usabile; }
    public boolean isEquipaggiabile() { return equipaggiabile; }
}
