package domain;
import service.impl.RandomSingleton;

public class Trappola extends Evento {

    private Effetto effetto;
    private RandomSingleton randomGenerale= RandomSingleton.getInstance();

    public Trappola(Effetto effetto, int id, boolean inizioEvento, boolean fineEvento, String descrizione, String nomeEvento, Stanza posizioneCorrente) {
        super(id, inizioEvento, fineEvento, descrizione, nomeEvento, posizioneCorrente);
        this.effetto = effetto;
    }

    public boolean alterareStato() {
    
        return true;
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

    
        int difficolta = 4; 

        int dado = randomGenerale.prossimoNumero(1, 6);

        System.out.println("Tiro per disinnescare: " + dado + " (Difficoltà = " + difficolta + ")");

        if (dado >= difficolta) {
            System.out.println(" "+"Hai disinnescato la trappola!");
            return true;
        }

        System.out.println("Disinnesco fallito! La trappola si attiva!\nRicorda se sei avvelenato o stordito e decidi di usare un passaggio segreto\ngli effetti saranno accelerati!\n");
        return false;
    }

}
