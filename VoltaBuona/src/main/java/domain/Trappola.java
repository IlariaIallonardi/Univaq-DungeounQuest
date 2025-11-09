package domain;

public class Trappola extends Evento {
    private int danno;

    public Trappola(int id, boolean inizioEvento, boolean fineEvento, int danno) {
        super(id, inizioEvento, fineEvento);
        this.danno = danno;
    }

    public String checkDiDisinnesco() {
        // ritorna esito test disinnesco
        return "Fallito";
    }

    public boolean alterareStato() {
        // cambia stato stanza/giocatore
        return true;
    }

    public int getDanno() {
        return danno;
    }
    public void setDanno(int danno) {
        this.danno = danno;
    }
       

}