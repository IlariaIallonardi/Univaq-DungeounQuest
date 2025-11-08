package domain;

import it.univaq.dungeon.core.Azione;

public class Arciere extends Giocatore {
    @Override
    public int calcolaDannoBase() { return attacco + 1; }

    @Override
    public Azione scegliAzione() { return Azione.ATTACCA; }

    @Override
    public void aggiornaTurno() { /* logica turno arciere */ }
}
