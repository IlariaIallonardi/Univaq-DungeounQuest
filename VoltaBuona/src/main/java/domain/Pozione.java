package domain;

public class Pozione extends Oggetto {

    public enum Tipo {
        CURA, MANA, ANTIDOTO
    }

    private boolean durata;
    private Tipo tipo;
    private int valorePozione;

    public boolean isDurata() {
        return durata;
    }

    public void setDurata(boolean durata) {
        this.durata = durata;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public int getValorePozione() {
        return valorePozione;
    }

    public void setValorePozione(int valorePozione) {
        this.valorePozione = valorePozione;
    }

    public Pozione(boolean durata, Tipo tipo, int valorePozione, int id, String nome, String descrizione, boolean usabile, boolean equipaggiabile, boolean trovato) {
        super(id, nome, descrizione, usabile, equipaggiabile, trovato);
        this.durata = durata;
        this.tipo = tipo;
        this.valorePozione = valorePozione;
    }

    public int aumentoPuntiVita() {
        return tipo == Tipo.CURA ? valorePozione : 0;
    }

    public int aumentoMana() {
        return tipo == Tipo.MANA ? valorePozione : 0;
    }

    public int antidotoVeleno() {
        return tipo == Tipo.ANTIDOTO ? valorePozione : 0;
    }

    @Override
    public boolean eseguiEffetto(Personaggio personaggio) {
        if (personaggio == null) {
            return false;
        }

        switch (tipo) {
            case CURA:
                personaggio.setPuntiVita(personaggio.getPuntiVita() + valorePozione);
                break;
            case MANA:
                personaggio.setPuntiMana(personaggio.getPuntiMana() + valorePozione);
                break;
            case ANTIDOTO:
                personaggio.setStatoPersonaggio("NORMALE");
                // azzera eventuali contatori (se usi turni)
                personaggio.setTurniAvvelenato(0);

                break;
            default:
                return false;
        }
        return true;
    }

}
