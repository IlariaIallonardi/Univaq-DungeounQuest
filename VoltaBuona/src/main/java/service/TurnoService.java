package service;

import domain.Gioco;
import domain.Giocatore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TurnoService {

    private List<Giocatore> ordineTurno = new ArrayList<>();
    private int indiceCorrente = 0;

    public void inizializzaTurno(List<Giocatore> giocatori) {
        ordineTurni.clear();
        ordineTurni.addAll(giocatori);
        Collections.shuffle(ordineTurni); // ordine casuale
        indiceCorrente = 0;
    }

    public Giocatore getTurnoCorrente() {
        return ordineTurni.isEmpty() ? null : ordineTurni.get(indiceCorrente);
    }

    public void passaProssimoTurno() {
        if (!ordineTurni.isEmpty()) {
            indiceCorrente = (indiceCorrente + 1) % ordineTurni.size();
        }
    }

    public List<Giocatore> getOrdineTurni() {
        return new ArrayList<>(ordineTurni);
    }

    public void terminaTurnoCorrente(Gioco gioco) {
        // logica di fine turno, es. aggiornamento stato
    }

    public void passaProssimoGiocatore(Gioco gioco) {
        passaProssimoTurno();
    }
}