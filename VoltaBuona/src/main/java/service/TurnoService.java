package service;

import domain.Giocatore;

import java.util.List;

public interface TurnoService {

     void inizializzaTurno();

     Giocatore getTurnoCorrente();

     void passaProssimoTurno();

     List<Giocatore> getOrdineTurno();

     boolean fineTurno();
}
