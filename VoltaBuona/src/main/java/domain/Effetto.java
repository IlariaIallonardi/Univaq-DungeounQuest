package domain;

public class Effetto {

    public enum TipoEffetto {
        CURA,
        DANNO,
        DIFESA,
        ATTACCO,
        VELOCITÀ,
        INVISIBILITÀ,
        STORDIMENTO,
        AVVELENAMENTO
    }

    private TipoEffetto tipo;
    private String descrizione;
    private int durataTurni;
    private int valore;

    public Effetto(TipoEffetto tipo, String descrizione, int durataTurni, int valore) {
        this.tipo = tipo;
        this.descrizione = descrizione;
        this.durataTurni = durataTurni;
        this.valore = valore;
    }

    public boolean èAttivo() {
        return durataTurni > 0;
    }

    public void decrementaDurata() {
        if (durataTurni > 0) {
            durataTurni--;
        }
    }

    // Getters e Setters
    public TipoEffetto getTipo() {
        return tipo;
    }

    public void setTipo(TipoEffetto tipo) {
        this.tipo = tipo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getDurataTurni() {
        return durataTurni;
    }

    public void setDurataTurni(int durataTurni) {
        this.durataTurni = durataTurni;
    }

    public int getValore() {
        return valore;
    }

    public void setValore(int valore) {
        this.valore = valore;
    }

    @Override
    public String toString() {
        return tipo + " (" + descrizione + ") [" + valore + "] per " + durataTurni + " turni";
    }
}
