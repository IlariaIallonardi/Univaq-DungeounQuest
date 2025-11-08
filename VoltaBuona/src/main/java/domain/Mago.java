package domain;

import it.univaq.dungeon.core.Azione;

public class Mago extends Giocatore {
    @Override
    public int calcolaDannoBase() { return attacco; }

    @Override
    public Azione scegliAzione() { return Azione.USA_OGGETTO; }

    @Override
    public void aggiornaTurno() { /* logica turno mago */ }
}
