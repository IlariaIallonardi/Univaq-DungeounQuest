package domain;

import domain.Azione;

public class Mago extends Giocatore {
    @Override
    public int calcolaDannoBase() {
        return attacco;
    }

    @Override
    public Azione scegliAzione() {
        return Azione.USA_OGGETTO;
    }

    @Override
    public void aggiornaTurno() {
        /* logica turno mago */ }

    @Override
    protected void setNome(String nome) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setNome'");
    }
}
