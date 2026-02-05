package domain;

public class Arma extends Oggetto {

    private int dannoBonus;
    private TipoArma tipoArma;

    public Arma(int dannoBonus, int id, String nome, String descrizione, boolean usabile, boolean equipaggiabile, boolean trovato, TipoArma tipoArma) {
        super(id, nome, descrizione, usabile, equipaggiabile, trovato);
        this.dannoBonus = dannoBonus;
        this.tipoArma = tipoArma;

    }

    public int getDannoBonus() {
        return dannoBonus;
    }

    public void setDannoBonus(int dannoBonus) {
        this.dannoBonus = dannoBonus;
    }

    public TipoArma getTipoArma() {
        return tipoArma;
    }

    public void setTipoArma(TipoArma tipoArma) {
        this.tipoArma = tipoArma;
    }

   @Override
     public boolean eseguiEffetto(Personaggio personaggio) {
    if (personaggio == null || tipoArma == null) {
        return false;
    }
    personaggio.setAttacco(personaggio.getAttacco() + tipoArma.getDannoBonus());
    return true;
}



    @Override
    public boolean usare(Personaggio personaggio) {
        return super.usare(personaggio);
    }

    @Override
    public void setTrovato(boolean trovato) {
        super.setTrovato(trovato);
    }

    @Override
    public boolean èTrovato() {
        return super.èTrovato();
    }

    @Override
    public void setEquipaggiabile(boolean equipaggiabile) {
        super.setEquipaggiabile(equipaggiabile);
    }

    @Override
    public boolean èEquipaggiabile() {
        return super.èEquipaggiabile();
    }

    @Override
    public void setUsabile(boolean usabile) {
        super.setUsabile(usabile);
    }

    @Override
    public boolean èUsabile() {
        return super.èUsabile();
    }

    @Override
    public void setDescrizione(String descrizione) {
        super.setDescrizione(descrizione);
    }

    @Override
    public String getDescrizione() {
        return super.getDescrizione();
    }

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
    }

    @Override
    public String getNome() {
        return super.getNome();
    }

    @Override
    public void setId(int id) {
        super.setId(id);
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public enum TipoArma {
        FRECCIA_E_ARCO(1),
        BACCHETTA_MAGICA(2),
        SPADA(3),
        BALESTRA_PESANTE(4);

        private final int dannoBonus;

        TipoArma(int dannoBonus) {
            this.dannoBonus = dannoBonus;
        }

        public int getDannoBonus() {
            return dannoBonus;
        }

    }

}
