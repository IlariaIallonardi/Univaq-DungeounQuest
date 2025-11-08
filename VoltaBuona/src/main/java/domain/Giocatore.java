package domain;

import domain.Azione;
import domain.Stanza;
import domain.Zaino;

public abstract class Giocatore extends Personaggio {
    protected Zaino zaino = new Zaino();
    protected TipoGiocatore tipo;
    protected String statoG; // oppure enum StatoGiocatore

    public abstract Azione scegliAzione();

    public abstract void aggiornaTurno();

    public Zaino getZaino() {
        return zaino;
    }

    public void setPosizione(Stanza stanza) {
        this.posizioneCorrente = stanza;
    }

    protected abstract void setNome(String nome);

    public int calcolaDannoBase() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calcolaDannoBase'");
    }
}
