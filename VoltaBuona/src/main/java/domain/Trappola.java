package domain;

public class Trappola extends Evento {
    private int danno;

    public Trappola(int id, boolean inizioEvento, boolean fineEvento, int danno) {
        super(id, inizioEvento, fineEvento, descrizione);
        this.danno = danno;
    }
     /* 
    public String checkDiDisinnesco() {
        // ritorna esito test disinnesco
        return "Fallito";
    }*/

    public boolean alterareStato() {
        // cambia stato stanza/giocatore
        return true;
    }

    public int getDannoTrappola() {
        return danno;
    }
    public void setDanno(int danno) {
        this.danno = danno;
    }
       

}