package domain;

public class Paladino extends Giocatore {
    int mana;

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    @Override
    public Azione scegliAzione() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'scegliAzione'");
    }

    @Override
    public void aggiornaTurno() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'aggiornaTurno'");
    }

    @Override
    protected void setNome(String nome) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setNome'");
    }
}
