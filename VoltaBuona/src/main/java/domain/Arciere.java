package domain;

import domain.Azione;

public class Arciere extends Giocatore {

    @Override
    public int calcolaDannoBase() {
        return attacco + 1;
    }

    @Override
    public Azione scegliAzione() {
        return Azione.ATTACCA;
    }

    @Override
    public void aggiornaTurno() {
        /* logica turno arciere */ }

    @Override
    protected void setNome(String nome) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setNome'");
    }
}
