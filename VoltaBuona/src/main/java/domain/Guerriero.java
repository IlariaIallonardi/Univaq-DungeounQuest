package domain;

import it.univaq.dungeon.core.Azione;

public class Guerriero extends Giocatore {
    @Override
    public int calcolaDannoBase() { return attacco + 2; }

    @Override
    public Azione scegliAzione() { return Azione.ATTACCA; }

    @Override
    public void aggiornaTurno() { /* logica turno guerriero */ }
}
