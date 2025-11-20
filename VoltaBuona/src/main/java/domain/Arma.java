package domain;

public class Arma extends Oggetto {
    private int dannoBonus;
    private int durabilitaArma;
    private Personaggio personaggio;

    public Arma(int dannoBonus, int durabilitaArma, int id, String nome, String descrizione, boolean usabile, boolean equipaggiabile, boolean trovato) {
        super(id, nome, descrizione, usabile, equipaggiabile, trovato);
        this.dannoBonus = dannoBonus;
        this.durabilitaArma = durabilitaArma;
    }
    public int getDannoBonus() {
        return dannoBonus;
    }
    public void setDannoBonus(int dannoBonus) {
        this.dannoBonus = dannoBonus;
    }
    public int getDurabilitaArma() {
        return durabilitaArma;
    }
    public void setDurabilitaArma(int durabilitaArma) {
        this.durabilitaArma = durabilitaArma;}
    



   // non so se questo metodo va bene qui
    @Override
     public boolean eseguiEffetto(Personaggio personaggio) {
        if (personaggio == null) return false;
        // l'arma deve essere equipaggiabile e non rotta
        if (!isEquipaggiabile() || durabilitaArma <= 0) return false;

        // se già equipaggiata da un altro, opzionalmente rimuovere il bonus precedente
        if (this.personaggio != null && this.personaggio != personaggio) {
            // rimuove il bonus dal precedente proprietario
            this.personaggio.setAttacco(this.personaggio.getAttacco() - this.dannoBonus);
        }

        // assegna la nuova owner e applica il bonus
        this.personaggio = personaggio;
        personaggio.setAttacco(personaggio.getAttacco() + this.dannoBonus);

        // consumiamo un punto di durabilità all'uso/equip
        this.durabilitaArma = Math.max(0, this.durabilitaArma - 1);

        return true;
    }


}


   /*  public enum Tipo {
        ASCIA, SPADA, PUGNALE, ARCO
    }

    private Tipo tipo;
    private int dannoBonus;
    private int durabilitaArma;
    private Personaggio personaggio;


    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public int getDannoBonus() {
        return dannoBonus;
    }

    public void setDannoBonus(int dannoBonus) {
        this.dannoBonus = dannoBonus;
    }

    public int getDurabilitaArma() {
        return durabilitaArma;
    }

    public void setDurabilitaArma(int durabilitaArma) {
        this.durabilitaArma = durabilitaArma;
    }
    public Arma(Tipo tipo, int dannoBonus, int durabilitaArma, int id, String nome, String descrizione, boolean usabile, boolean equipaggiabile, boolean trovato) {
        super(id, nome, descrizione, usabile, equipaggiabile, trovato);
        this.tipo = tipo;
        this.dannoBonus = dannoBonus;
        this.durabilitaArma = durabilitaArma;
    }



   public int miglioraAttacco() {
    // Controlla la durabilità dell'arma
    if (durabilitaArma <= 0) {
        return 0; // L'arma è rotta, nessun bonus
    }
    
    // Calcola il bonus base in base al tipo di arma
    int bonusBase = switch (tipo) {
        case SPADA -> 10;
        case ASCIA -> 12;
        case PUGNALE -> 6;
        case ARCO -> 8;
    };
    
    return personaggio.getAttacco() + dannoBonus;
} */


    