package domain;

import domain.Azione;
import domain.Guerriero;

public class Guerriero extends Giocatore {
    @Override
    public int calcolaDannoBase() {
        return attacco + 2;
    }

    @Override
    public Azione scegliAzione() {
        return Azione.ATTACCA;
    }

    @Override
    public void aggiornaTurno() {
        /* logica turno guerriero */ }

    @Override
    protected void setNome(String nome) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setNome'");
    }
}
