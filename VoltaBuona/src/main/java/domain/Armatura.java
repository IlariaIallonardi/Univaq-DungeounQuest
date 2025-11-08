package domain;

public class Armatura extends Evento{
    private int difesa;
    private boolean equipaggiabile;

    public int getDifesa() {
        return difesa;
    }

    public void setDifesa(int difesa) {
        this.difesa = difesa;
    }

    public boolean isEquipaggiabile() {
        return equipaggiabile;
    }

    public void setEquipaggiabile(boolean equipaggiabile) {
        this.equipaggiabile = equipaggiabile;
    }
}
