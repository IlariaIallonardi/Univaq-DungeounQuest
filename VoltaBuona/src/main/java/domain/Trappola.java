package domain;

public class Trappola extends Evento {
    private int danno;
    private Effetto effetto;

    public Trappola(int id, boolean inizioEvento, boolean fineEvento, String descrizione, int danno, Effetto effetto) {
         super(id, inizioEvento, fineEvento, descrizione);
        this.danno = danno;
        this.effetto = effetto;
    }
     
    public String checkDiDisinnesco() {
        // ritorna esito test disinnesco
        return "Fallito";
    }

    public int getDanno() {
        return danno;
    }

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
    public Effetto getEffettoTrappola() {
        return effetto;
    }
    public void setEffettoTrappola(Effetto effetto) {
        this.effetto = effetto;
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
    public void setFineEvento(boolean fineEvento) {
        super.setFineEvento(fineEvento);
    }

    @Override
    public boolean isFineEvento() {
        return super.isFineEvento();
    }

    @Override
    public void setInizioEvento(boolean inizioEvento) {
        super.setInizioEvento(inizioEvento);
    }

    @Override
    public boolean isInizioEvento() {
        return super.isInizioEvento();
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
    public boolean èRiutilizzabile() {
        return super.èRiutilizzabile();
    }

    @Override
    public boolean attivo() {
        return super.attivo();
    }

    @Override
    public String toString() {
        return super.toString();
    }



    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int getFineEvento() {
        return super.getFineEvento();
    }

    @Override
    public int getInizioEvento() {
        return super.getInizioEvento();
    }

    public Effetto getEffetto() {
        return effetto;
    }

    public void setEffetto(Effetto effetto) {
        this.effetto = effetto;
    }

    
    public boolean checkDiDisinnesco(Personaggio personaggio) {

    // difficoltà della trappola (puoi modificarla come attributo)
    int difficolta = 4; // esempio: serve un tiro >= 4

    int dado = (int) (Math.random() * 6) + 1;

    System.out.println(" Tiro per disinnescare: " + dado + " (CD = " + difficolta + ")");

    if (dado >= difficolta) {
        System.out.println(" Hai disinnescato la trappola!");
        return true;
    }

    System.out.println(" Disinnesco fallito! La trappola si attiva!");
    return false;
}

    public void attiva(Personaggio personaggio) {

    Effetto.TipoEffetto effetto = tiraDado();

    switch (effetto) {

        case CONGELAMENTO -> {
            personaggio.subisciDanno(5);
            System.out.println(" La trappola ti ferisce leggermente! -5 HP");
            break;
        }

        case FURIA -> {
            personaggio.subisciDanno(15);
            System.out.println(" La trappola causa un danno grave! -15 HP");
            break;
        }

        case AVVELENAMENTO -> {
            personaggio.setStatoPersonaggio("AVVELENATO");
            System.out.println(" Sei stato avvelenato!");
            break;
        }

        case STORDIMENTO -> {
            personaggio.setStatoPersonaggio("STORDITO");
            System.out.println(" Sei stordito!");
            break;
        }

        case IMMOBILIZZATO -> {
            personaggio.setStatoPersonaggio("IMMOBILIZZATO");
            System.out.println(" Sei immobilizzato!");
            break;
        }


        case NESSUN_EFFETTO -> {
            System.out.println("🍀 Hai evitato il peggio! Nessun effetto.");
            break;
        }
    }
}

    private Effetto.TipoEffetto tiraDado() {
        int dado = (int) (Math.random() * 6) + 1;

        return switch (dado) {
            case 1 -> Effetto.TipoEffetto.CONGELAMENTO;
            case 2 -> Effetto.TipoEffetto.FURIA;
            case 3 -> Effetto.TipoEffetto.AVVELENAMENTO;
            case 4 -> Effetto.TipoEffetto.STORDIMENTO;
            case 5 -> Effetto.TipoEffetto.IMMOBILIZZATO;
            default -> Effetto.TipoEffetto.NESSUN_EFFETTO;
        };
    }


       
  
}