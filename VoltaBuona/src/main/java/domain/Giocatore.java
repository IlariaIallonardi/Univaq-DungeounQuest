package domain;

public abstract class Giocatore extends Personaggio {
    
    private TipoGiocatore tipo;

    public Giocatore(TipoGiocatore tipo, int attacco, int difesa, int esperienza, int id, int livello, String nomeP, Stanza posizioneCorrente, int puntiMana, int puntiVita, String statoG, Zaino zaino) {
        super(attacco, difesa, esperienza, id, livello, nomeP, posizioneCorrente, puntiMana, puntiVita, statoG, zaino);
        this.tipo = tipo;
    }

    
}