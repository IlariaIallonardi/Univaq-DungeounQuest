package domain;

public class PassaggioSegreto extends Evento {
    private Stanza destinazione;
    private String rebusApertura;
    private boolean scoperto;
    private Stanza stanzaOrigine;
    private String rispostaRebus;

    public PassaggioSegreto(Stanza destinazione, String rebusApertura, boolean scoperto, int id, boolean inizioEvento, boolean fineEvento, Stanza stanzaOrigine, String rispostaRebus) {
        super(id, inizioEvento, fineEvento, "Passaggio Segreto", "PassaggioSegreto");
        this.destinazione = destinazione;
        this.rebusApertura = rebusApertura;
        this.scoperto = scoperto;
        this.stanzaOrigine = stanzaOrigine;
        this.rispostaRebus = rispostaRebus;
    }

    

   
    public Stanza getDestinazione() {
        return destinazione;
    }

    public void setDestinazione(Stanza destinazione) {
        this.destinazione = destinazione;
    }

    public String getRebusApertura() {
        return rebusApertura;
    }

    public void setRebusApertura(String rebusApertura) {
        this.rebusApertura = rebusApertura;
    }

    public boolean isScoperto() {
        return scoperto;
    }

    public void setScoperto(boolean scoperto) {
        this.scoperto = scoperto;
    }

    public boolean attivo() {
       if (!scoperto) return false;
        if (!èRiutilizzabile() && !isScoperto()) return false;
        return true;
    }

    public boolean èRiutilizzabile() {
        // se deve aprirsi una sola volta
        return true;
    }

    public Stanza getStanzaOrigine() {
        return stanzaOrigine;
    }
    public void setStanzaOrigine(Stanza stanzaOrigine) {
        this.stanzaOrigine = stanzaOrigine;
    }
    public void setRispostaRebus(String rispostaRebus) {
        this.rispostaRebus = rispostaRebus;
    }
    public String getRispostaRebus() {
        return rispostaRebus;
    }

    

    @Override
    public int getFineEvento() {
        return super.getFineEvento();
    }

    @Override
    public int getInizioEvento() {
        return super.getInizioEvento();
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
    public String toString() {
        return super.toString();
    }
    

    



}
