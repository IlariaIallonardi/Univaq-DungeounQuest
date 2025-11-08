package it.univaq.dungeon.personaggi;

import it.univaq.dungeon.core.Azione;
import it.univaq.dungeon.core.Stanza;
import it.univaq.dungeon.oggetti.Zaino;

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
}
