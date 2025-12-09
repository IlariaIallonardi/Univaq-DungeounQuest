package domain;

public class Mostro extends PersonaIncontrata {
 private int puntiVitaMostro=100;
 private int difesaMostro=100;
 private int dannoMostro;
 private String nomeMostro;
 private TipoAttaccoMostro tipoAttaccoMostro;
 private Stanza posizioneCorrenteMostro;

    public Stanza getPosizioneCorrenteMostro() {
        return posizioneCorrenteMostro;
    }

    public void setPosizioneCorrenteMostro(Stanza posizioneCorrenteMostro) {
        this.posizioneCorrenteMostro = posizioneCorrenteMostro;
    }

   public enum TipoAttaccoMostro {
        MORSO,
        RUGGITO_DI_FUOCO,
        URLO_ASSORDANTE,
        RAGNATELA_IMMOBILIZZANTE,  
        ARTIGLI_POSSENTI
    }


    public Mostro(int id, boolean inizioEvento, boolean fineEvento, String descrizione, String tipoPersonaIncontrata, int puntiVitaMostro, int difesaMostro, String nomeMostro, TipoAttaccoMostro tipoAttaccoMostro,Stanza posizioneCorrenteMostro) {
         super(id, inizioEvento, fineEvento, descrizione, tipoPersonaIncontrata);
        this.puntiVitaMostro = puntiVitaMostro;
        this.difesaMostro = difesaMostro;
        this.nomeMostro = nomeMostro;
        this.tipoAttaccoMostro = tipoAttaccoMostro;
        this.posizioneCorrenteMostro = posizioneCorrenteMostro;
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
    public String getNomeMostro() {
        return nomeMostro;
    }
    public void setNomeMostro(String nomeMostro) {
        this.nomeMostro = nomeMostro;                



    }
     public boolean èMortoilMostro() {
        return this.puntiVitaMostro <= 0;
    }
    public TipoAttaccoMostro getTipoAttaccoMostro() {
        return tipoAttaccoMostro;
    }
    public void setTipoAttaccoMostro(TipoAttaccoMostro tipoAttaccoMostro) {
        this.tipoAttaccoMostro = tipoAttaccoMostro;
    }
    


    @Override
    public void setTipoPersonaIncontrata(String tipoPersonaIncontrata) {
        super.setTipoPersonaIncontrata(tipoPersonaIncontrata);
    }
    @Override
    public String getTipoPersonaIncontrata() {
        return super.getTipoPersonaIncontrata();
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



    public void attaccoDelMostro(Personaggio personaggio) {
        Mostro mostro= this;
         
    if (mostro.getNomeMostro().equals("Spiritello")) {
        tipoAttaccoMostro = TipoAttaccoMostro.MORSO;
    } else if (mostro.getNomeMostro().equals("Drago")) {
        tipoAttaccoMostro = TipoAttaccoMostro.RUGGITO_DI_FUOCO;
    } else if (mostro.getNomeMostro().equals("Golem")) {
        tipoAttaccoMostro = TipoAttaccoMostro.URLO_ASSORDANTE;
    } else if (mostro.getNomeMostro().equals("Ragno Gigante")) {
        tipoAttaccoMostro = TipoAttaccoMostro.RAGNATELA_IMMOBILIZZANTE;
    } else if (mostro.getNomeMostro().equals("Troll")) {
        tipoAttaccoMostro = TipoAttaccoMostro.ARTIGLI_POSSENTI;
    } else {
        tipoAttaccoMostro = null; 
    }}
    

    @Override
    public String toString() {
        return "Mostro{" +
                "nome=" + getTipoPersonaIncontrata() +
                ", hp=" + puntiVitaMostro +
                ", danno=" + dannoMostro +
                ", difesa=" + difesaMostro +
                ", tipoAttacco=" + tipoAttaccoMostro +
                '}';
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
