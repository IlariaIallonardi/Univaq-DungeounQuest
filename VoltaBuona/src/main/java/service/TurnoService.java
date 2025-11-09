package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import domain.Giocatore;
import domain.Gioco;

public class TurnoService {

    private List<Giocatore> ordineTurno = new ArrayList<>();
    private int indiceCorrente = 0;

    public void inizializzaTurno(List<Giocatore> giocatori) {
        ordineTurno.clear();
        ordineTurno.addAll(giocatori);
        Collections.shuffle(ordineTurno); // ordine casuale
        indiceCorrente = 0;
    }

    public Giocatore getTurnoCorrente() {
        return ordineTurno.isEmpty() ? null : ordineTurno.get(indiceCorrente);
    }

    public void passaProssimoTurno() {
        if (!ordineTurno.isEmpty()) {
            indiceCorrente = (indiceCorrente + 1) % ordineTurno.size();
        }
    }

    public List<Giocatore> getOrdineTurni() {
        return new ArrayList<>(ordineTurno);
    }

    public void terminaTurnoCorrente(Gioco gioco) {
        // logica di fine turno, es. aggiornamento stato
    }

    public void passaProssimoGiocatore(Gioco gioco) {
        passaProssimoTurno();
    }
}