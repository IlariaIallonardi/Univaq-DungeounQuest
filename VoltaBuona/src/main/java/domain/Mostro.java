package domain;

public class Mostro extends PersonaIncontrata {
 private int puntiVitaMostro;
 private int difesaMostro;
 private int dannoMostro;
 private String tipoMostro;

   public enum TipoAttaccoMostro {
        MORSO,
        FURIA,
        STORDIMENTO,
        AVVELENAMENTO,
        IMMOBILIZZATO,
        NESSUN_EFFETTO
    }


    public Mostro(int id, boolean inizioEvento, boolean fineEvento, String descrizione, int danno, String messaggio, String nome, int puntiVita, int puntiDifesa, String tipoMostro) {
         super(id, false, false, "NPC: " + nome, nome);
        this.puntiVitaMostro = puntiVita;
        this.difesaMostro = puntiDifesa;
        this.tipoMostro = tipoMostro;
    }
    
     public int getPuntiVitaMostro() {
        return puntiVitaMostro;
    }
     public void setPuntiVitaMostro(int puntiVita) {
        this.puntiVitaMostro = puntiVita;
    }

    public int getDifesaMostro() {
        return difesaMostro;
    }
    public void setDifesaMostro(int difesa) {
        this.difesaMostro = difesa;
    }
    


    public Boolean alterareStato(){
        return true;
    }

    
    public void setDannoMostro(int danno) {
        this.dannoMostro = danno;
    }
    public int getDannoMostro() {
        return dannoMostro;
    }
    public String getTipoMostro() {
        return tipoMostro;
    }
    public void setTipoMostro(String tipoMostro) {
        this.tipoMostro = tipoMostro;
    }

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
    }
    @Override
    public String getNome() {
        return super.getNome();
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
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
