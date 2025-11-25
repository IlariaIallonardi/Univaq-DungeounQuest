package domain;

public class Effetto {

    public enum TipoEffetto {
        CONGELAMENTO,
        FURIA,
        STORDIMENTO,
        AVVELENAMENTO,
        IMMOBILIZZATO,
        NESSUN_EFFETTO
    }

    private TipoEffetto tipo;
    private String descrizione;
    private int durataTurni;

    public Effetto(TipoEffetto tipo, String descrizione, int durataTurni) {
        this.tipo = tipo;
        this.descrizione = descrizione;
        this.durataTurni = durataTurni;

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

    @Override
    public String toString() {
        return tipo + " (" + descrizione + "+ " + durataTurni + " turni";
    }
}
