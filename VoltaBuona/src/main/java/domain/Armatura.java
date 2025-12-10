package domain;

public class Armatura extends Oggetto {

    private TipoArmatura tipoArmatura;
    private int difesaBonus;
    private int durabilitaArmatura;
    private Armatura armaturaEquippaggiata;

    public Armatura(int difesaBonus, int durabilitaArmatura, TipoArmatura tipoArmatura, int id, String nome, String descrizione, boolean usabile, boolean equipaggiabile, boolean trovato) {
        super(id, nome, descrizione, usabile, equipaggiabile, trovato);
        this.difesaBonus = difesaBonus;
        this.durabilitaArmatura = durabilitaArmatura;
        this.tipoArmatura = tipoArmatura;
    }

    public enum TipoArmatura {
        DEBOLE(3),
        MEDIA(5),
        FORTE(8);

        private final int difesaBonus;
        private TipoArmatura tipoArmatura;

        TipoArmatura(int difesaBonus) {
            this.difesaBonus = difesaBonus;
        }

        public int getDifesaBonus() {
            return difesaBonus;
        }

    }

    public void setTipoArmatura(TipoArmatura tipoArmatura) {
        this.tipoArmatura = tipoArmatura;
    }

    public TipoArmatura getTipoArmatura() {
        return tipoArmatura;
    }

    public void setDifesaBonus(int difesaBonus) {
        this.difesaBonus = difesaBonus;
    }

    public int getDurabilitaArmatura() {
        return durabilitaArmatura;
    }

    public void setDurabilitaArmatura(int durabilitaArmatura) {
        this.durabilitaArmatura = durabilitaArmatura;
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
    public boolean isTrovato() {
        return super.isTrovato();
    }

    @Override
    public void setEquipaggiabile(boolean equipaggiabile) {
        super.setEquipaggiabile(equipaggiabile);
    }

    @Override
    public boolean isEquipaggiabile() {
        return super.isEquipaggiabile();
    }

    @Override
    public void setUsabile(boolean usabile) {
        super.setUsabile(usabile);
    }

    @Override
    public boolean isUsabile() {
        return super.isUsabile();
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

    @Override
     public boolean eseguiEffetto(Personaggio personaggio) {
    if (personaggio == null || tipoArmatura == null) {
        return false;
    }
    personaggio.setDifesa(personaggio.getDifesa() + tipoArmatura.getDifesaBonus());
    return true;
}
        
    
}
