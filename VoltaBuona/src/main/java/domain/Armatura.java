package domain;

public class Armatura extends Oggetto {

    public Armatura(int difesaBonus, int durabilitaArmatura, TipoArmatura tipoArmatura, int id, String nome, String descrizione, boolean usabile, boolean equipaggiabile, boolean trovato) {
        super(id, nome, descrizione, usabile, equipaggiabile, trovato);
        this.difesaBonus = difesaBonus;
        this.durabilitaArmatura = durabilitaArmatura;
        this.tipoArmatura = tipoArmatura;
    }

     public enum TipoArmatura {
        DEBOLE, MEDIA, FORTE
    }

    private TipoArmatura tipoArmatura;
    private int difesaBonus;
    private int durabilitaArmatura;

    public TipoArmatura getTipoArmatura() {
        return tipoArmatura;
    }

    public void setTipoArmatura(TipoArmatura tipoArmatura) {
        this.tipoArmatura = tipoArmatura;
    }

    public int getDifesaBonus() {
        return difesaBonus;
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

    
    public int miglioraDifesa() {
        // logica di equipaggiamento
        return difesaBonus;
    }

@Override
public boolean eseguiEffetto(Personaggio personaggio) {
    // Logica per applicare l'effetto dell'armatura al personaggio
    int nuovaDifesa = personaggio.getDifesa() + this.difesaBonus;
    personaggio.setDifesa(nuovaDifesa);
    return true; // Indica che l'effetto è stato applicato con successo


   
}
