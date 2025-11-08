package it.univaq.dungeon.oggetti;

public class Armatura extends Oggetto {
    public enum TipoArmatura { DEBOLE, MEDIA, FORTE }
    private TipoArmatura tipoArmatura;
    private int difesaBonus;
    private int durabilitaArmatura;

    public int aumentoDifesa() { return difesaBonus; }

    public boolean miglioraDifesa() {
        // logica di equipaggiamento
        return true;
    }

    @Override
    public boolean usare() { return false; }
}
