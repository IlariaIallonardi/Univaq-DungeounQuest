package domain;

public class Mostro extends PersonaIncontrata {

    private int puntiVitaMostro;
    private int difesaMostro;
    private boolean aggiornamento=false; //aggornare i parametri del mostro dopo il combattimento
    private String nomeMostro;
    private TipoAttaccoMostro tipoAttaccoMostro;
    private int esperienza;
    private int livelloMostro;
    
    private int dannoTipoMostro;

    public Mostro(int id, boolean inizioEvento, boolean fineEvento, String descrizione, String tipoPersonaIncontrata, int puntiVitaMostro, int difesaMostro, String nomeMostro, TipoAttaccoMostro tipoAttaccoMostro, Stanza posizioneCorrenteMostro, int dannoTipoMostro) {
        super(id, inizioEvento, fineEvento, descrizione, tipoPersonaIncontrata);
        this.puntiVitaMostro = puntiVitaMostro;
        this.difesaMostro = difesaMostro;
        this.nomeMostro = nomeMostro;
        this.tipoAttaccoMostro = tipoAttaccoMostro;
        this.dannoTipoMostro = dannoTipoMostro;
    }

    public enum TipoAttaccoMostro {
        MORSO(30),
        RUGGITO_DI_FUOCO(30),
        URLO_ASSORDANTE(30),
        RAGNATELA(30),
        ARTIGLI_POSSENTI(30);

        public int dannoTipoMostro;
        public TipoAttaccoMostro tipoAttaccoMostro;

        TipoAttaccoMostro(int dannoTipoMostro) {
            this.dannoTipoMostro = dannoTipoMostro;
        }

        public int getDannoTipoMostro() {
            return dannoTipoMostro;
        }

        public void setDannoTipoMostro(int dannoTipoMostro) {
            this.dannoTipoMostro = dannoTipoMostro;
        }

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

    public Boolean alterareStato() {
        return true;
    }
     ///getter e setter aggiornamento
    public Boolean isAggiornamento() {
        return aggiornamento;
    }
    public void setAggiornamento(Boolean aggiornamento) {
        this.aggiornamento = aggiornamento;
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
    public void setEsperienza(int esperienza) {
        this.esperienza = esperienza;
    }
    public int getEsperienzaMostro() {
        return esperienza;
    }
    public void setLivelloMostro(int livelloMostro) {
        this.livelloMostro = livelloMostro;
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

    public int settareVitaeDifesaMostro() {
        if (this.nomeMostro == null) {
            return 0;
        }
        switch (this.nomeMostro) {
            case "Spiritello" -> {
                this.esperienza = 0;
                this.livelloMostro = 0;
                this.puntiVitaMostro = 5;
                this.difesaMostro = 5;
                this.tipoAttaccoMostro = TipoAttaccoMostro.MORSO;
                return 1;
            }
            case "Drago" -> {
                this.esperienza = 0;
                this.livelloMostro = 0;
                this.puntiVitaMostro = 5;
                this.difesaMostro = 5;
                this.tipoAttaccoMostro = TipoAttaccoMostro.RUGGITO_DI_FUOCO;
                return 1;
            }
            case "Golem" -> {
                this.esperienza = 0;
                this.livelloMostro = 0;
                this.puntiVitaMostro = 5;
                this.difesaMostro = 5;
                this.tipoAttaccoMostro = TipoAttaccoMostro.URLO_ASSORDANTE;
                return 1;
            }
            case "Ragno Gigante" -> {
                this.esperienza = 0;
                this.livelloMostro = 0;
                this.puntiVitaMostro = 5;
                this.difesaMostro = 5;
                this.tipoAttaccoMostro = TipoAttaccoMostro.RAGNATELA;
                return 1;
            }
            case "Troll" -> {
                this.esperienza = 0;
                this.livelloMostro = 0;
                this.puntiVitaMostro = 5;
                this.difesaMostro = 5;
                this.tipoAttaccoMostro = TipoAttaccoMostro.ARTIGLI_POSSENTI;
                return 1;
            }
            default -> {
                this.dannoTipoMostro = (this.tipoAttaccoMostro != null) ? this.tipoAttaccoMostro.getDannoTipoMostro() : this.dannoTipoMostro;
                return 0;
                // mantiene i valori passati nel costruttore
            }
        }

    }

    public int aggiungiEsperienzaMostro() {
        
        if (!this.èMortoilMostro()) {
            this.esperienza += 20;
            this.puntiVitaMostro += 5; 
            this.dannoTipoMostro += 4;    
            this.difesaMostro += 5;
            
        }
      return   normalizzaEsperienzaMostro();
        
    }

    public int normalizzaEsperienzaMostro() {

        int xpPerLivello = 10;
        while (this.esperienza >= xpPerLivello) {
            this.livelloMostro = this.livelloMostro + 1;
            this.puntiVitaMostro += 10; 
            this.dannoTipoMostro += 2;    
            this.difesaMostro += 1;     

            this.esperienza -= xpPerLivello;
            System.out.println(this.nomeMostro + " sale al livello " + this.livelloMostro + "!");
        }
        return this.livelloMostro;
    }



    public int getEsperienza() {
        return esperienza;
    }
    public int getLivelloMostro() {
        return livelloMostro;
    }



    @Override
    public String toString() {
        return "Mostro{"
                + "nome=" + getTipoPersonaIncontrata()
                + ", hp=" + puntiVitaMostro
                + ", difesa=" + difesaMostro
                + ", tipoAttacco=" + tipoAttaccoMostro
                + '}';
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
