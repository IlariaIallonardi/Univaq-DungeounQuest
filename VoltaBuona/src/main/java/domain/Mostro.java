package domain;

public class Mostro extends PersonaIncontrata {
 private int puntiVitaMostro;
 private int difesaMostro;




    public Mostro(int id, boolean inizioEvento, boolean fineEvento, String descrizione, int danno, String messaggio, String nome) {
        super(id, inizioEvento, fineEvento, descrizione, danno, messaggio, nome);
    }

    public Mostro(int id, boolean inizioEvento, boolean fineEvento, String descrizione, int danno, String messaggio, String nome, int puntiVita, int puntiDifesa) {
        super(id, inizioEvento, fineEvento, descrizione, danno, messaggio, nome);
        this.puntiVitaMostro = puntiVita;
        this.difesaMostro = puntiDifesa;
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

    @Override
    public void setDanno(int danno) {
        super.setDanno(danno);
    }

    @Override
    public void setMessaggio(String messaggio) {
        super.setMessaggio(messaggio);
    }

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
    }

    @Override
    public int getDanno() {
        return super.getDanno();
    }

    @Override
    public String getMessaggio() {
        return super.getMessaggio();
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
