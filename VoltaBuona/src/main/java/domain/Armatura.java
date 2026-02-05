package domain;

public class Armatura extends Oggetto {

    private TipoArmatura tipoArmatura;


    public Armatura(int difesaBonus, TipoArmatura tipoArmatura, int id, String nome, String descrizione, boolean usabile, boolean equipaggiabile, boolean trovato) {
        super(id, nome, descrizione, usabile, equipaggiabile, trovato);
        
    
        this.tipoArmatura = tipoArmatura;
    }

    public enum TipoArmatura {
        DEBOLE(3),
        MEDIA(5),
        FORTE(8);

        private final int difesaBonus;
    

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

    @Override
     public boolean eseguiEffetto(Personaggio personaggio) {
    if (personaggio == null || tipoArmatura == null) {
        return false;
    }
    personaggio.setPuntiDifesa(personaggio.getPuntiDifesa() + tipoArmatura.getDifesaBonus());
    return true;
}
        
    
}
