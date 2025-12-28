package domain;


public class Effetto {

    /**
 * Tipi di effetto che una sorgente (es. Trappola, Pozione, Incantesimo) può applicare.
 *
 * Significato dei tipi:
 *  - CONGELAMENTO: imposta stato CONGELATO; durata predefinita 3 turni; applica danno immediato (es. -5 HP).
 *  - FURIA: imposta stato FURIA; può applicare danno maggiore o buff temporanei.
 *  - STORDIMENTO: imposta stato STORDITO; penalizza azioni/attacchi; durata predefinita 2 turni.
 *  - AVVELENAMENTO: imposta stato AVVELENATO; infligge danno periodico (es. 3 HP/turno); durata predefinita 3 turni.
 *  - IMMOBILIZZATO: imposta stato IMMOBILIZZATO; impedisce movimento finché attivo.
 *  - SALTA_TURNO: causa turni da saltare (gestiti tramite `turniDaSaltare`).
 *  - NESSUN_EFFETTO: nessuna conseguenza.
 */

    public enum TipoEffetto {
        CONGELAMENTO,
        FURIA,
        STORDIMENTO,
        AVVELENAMENTO,
        IMMOBILIZZATO,
        SALTA_TURNO,
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
   /**
 * Restituisce true se la durata dell'effetto è ancora > 0.
 */
    public boolean èAttivo() {
        return durataTurni > 0;
    }
    /**
 * Decrementa la durata rimanente di questo effetto di 1 turno (se > 0).
 */
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
