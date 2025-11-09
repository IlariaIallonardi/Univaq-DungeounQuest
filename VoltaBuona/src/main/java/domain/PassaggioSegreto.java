package domain;

public class PassaggioSegreto extends Evento {
    private Stanza destinazione;
    private boolean rebusApertura;
    private boolean scoperto;

    public PassaggioSegreto(Stanza destinazione, boolean rebusApertura, boolean scoperto, int id, boolean inizioEvento, boolean fineEvento) {
        super(id, inizioEvento, fineEvento);
        this.destinazione = destinazione;
        this.rebusApertura = rebusApertura;
        this.scoperto = scoperto;
    }

    

   
    public Stanza getDestinazione() {
        return destinazione;
    }

    public void setDestinazione(Stanza destinazione) {
        this.destinazione = destinazione;
    }

    public boolean isRebusApertura() {
        return rebusApertura;
    }

    public void setRebusApertura(boolean rebusApertura) {
        this.rebusApertura = rebusApertura;
    }

    public boolean isScoperto() {
        return scoperto;
    }

    public void setScoperto(boolean scoperto) {
        this.scoperto = scoperto;
    }





    public boolean attivo() {
       return true;
    }

    public boolean èRiutilizzabile() {
        // se deve aprirsi una sola volta
        return true;
    }






}
