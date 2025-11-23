package domain;

public class Arma extends Oggetto {
    private int dannoBonus;
    private String tipo;
    //private int durabilitaArma;
    private Personaggio personaggio;

    public Arma(int dannoBonus, int durabilitaArma, int id, String nome, String descrizione, boolean usabile, boolean equipaggiabile, boolean trovato, String tipo) {
        super(id, nome, descrizione, usabile, equipaggiabile, trovato);
        this.dannoBonus = dannoBonus;
        this.tipo = tipo;
      //  this.durabilitaArma = durabilitaArma;
    }
    public int getDannoBonus() {
        return dannoBonus;
    }
    public void setDannoBonus(int dannoBonus) {
        this.dannoBonus = dannoBonus;
    }
    public String getTipo() {
        return tipo;
    }       
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
  /*   public int getDurabilitaArma() {
        return durabilitaArma;
    }
    public void setDurabilitaArma(int durabilitaArma) {
        this.durabilitaArma = durabilitaArma;}
    */


    @Override
     public boolean eseguiEffetto(Personaggio personaggio) {
        if (personaggio == null) return false;
        // Aumenta solo il danno/attacco del personaggio
        personaggio.setAttacco(personaggio.getAttacco() + this.dannoBonus);
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


    