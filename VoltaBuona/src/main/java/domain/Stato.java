package domain;

import java.io.Serializable;

public class Stato implements Serializable {

    public enum StatoGioco {
        IN_ATTESA,
        IN_ESPLORAZIONE,
        IN_COMBATTIMENTO,
        CONCLUSO,
        INIZIO

    }

    private StatoGioco statoGioco;

    public Stato(StatoGioco statoGioco) {
        this.statoGioco = statoGioco;
    }

    public StatoGioco getStatoGioco() {
        return statoGioco;
    }

    public void setStatoGioco(StatoGioco statoGioco) {
        this.statoGioco = statoGioco;
    }
}
