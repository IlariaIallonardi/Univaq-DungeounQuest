package domain;

import it.univaq.dungeon.oggetti.Chiave;

import java.util.ArrayList;
import java.util.List;

public class PassaggioSegreto extends Evento {
    private int id;
    private Stanza destinazione;
    private boolean rebusApertura;
    private Chiave chiaveRichiesta;

    public PassaggioSegreto(int id, Stanza destinazione) {
        this.id = id;
        this.destinazione = destinazione;
    }

    public String risolviRebus() {
        // risoluzione rebus
        rebusApertura = true;
        return "Rebus risolto.";
    }

    public boolean aperturaStanza() {
        // apre se rebus o chiave soddisfatti
        return rebusApertura || chiaveRichiesta != null;
    }

    public boolean unicaApertura() {
        // se deve aprirsi una sola volta
        return true;
    }
}
