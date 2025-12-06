package domain;

public class Personaggio {

    private int id;
    private String nomeP;
    private int puntiVita;
    private int puntiMana;
    private int difesa;
    private String statoPersonaggio;
    private Zaino zaino;
    private int attacco;
    private int livello;
    private int esperienza;
    private Stanza posizioneCorrente;
    private boolean protetto;
    private int turniProtetto;
    private int turniAvvelenato;
    private int turniCongelato;
    private int turniStordito;

    public Personaggio(int attacco, int difesa, int esperienza, int id, int livello, String nomeP, Stanza posizioneCorrente, int puntiMana, int puntiVita, String statoPersonaggio, Zaino zaino) {
        this.attacco = attacco;
        this.difesa = difesa;
        this.esperienza = esperienza;
        this.id = id;
        this.livello = livello;
        this.nomeP = nomeP;
        this.posizioneCorrente = posizioneCorrente;
        this.puntiMana = puntiMana;
        this.puntiVita = puntiVita;
        this.statoPersonaggio = statoPersonaggio;
        this.zaino = zaino;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeP() {
        return nomeP;
    }

    public void setNomeP(String nomeP) {
        this.nomeP = nomeP;
    }

    public int getPuntiVita() {
        return puntiVita;
    }

    public void setPuntiVita(int puntiVita) {
        this.puntiVita = puntiVita;
    }

    public int getPuntiMana() {
        return puntiMana;
    }

    public void setPuntiMana(int puntiMana) {
        this.puntiMana = puntiMana;
    }

    public int getDifesa() {
        return difesa;
    }

    public void setDifesa(int difesa) {
        this.difesa = difesa;
    }

    public String getStatoPersonaggio() {
        return statoPersonaggio;
    }

    public void setStatoPersonaggio(String statoPersonaggio) {
        this.statoPersonaggio = statoPersonaggio;
    }

    public Zaino getZaino() {
        return zaino;
    }

    public void setZaino(Zaino zaino) {
        this.zaino = zaino;
    }

    public void setAttacco(int attacco) {
        this.attacco = attacco;
    }

    public int getAttacco() {
        return attacco;
    }

    public int getLivello() {
        return livello;
    }

    public void setLivello(int livello) {
        this.livello = livello;
    }

    public int getEsperienza() {
        return esperienza;
    }

    public void setEsperienza(int esperienza) {
        this.esperienza = esperienza;
    }

    public Stanza getPosizioneCorrente() {
        return posizioneCorrente;
    }

    public void setPosizioneCorrente(Stanza posizioneCorrente) {
        this.posizioneCorrente = posizioneCorrente;
    }

    public int getTurniAvvelenato() {
        return turniAvvelenato;
    }

    public void setTurniAvvelenato(int turniAvvelenato) {
        this.turniAvvelenato = turniAvvelenato;
    }

    public int getTurniCongelato() {
        return turniCongelato;
    }

    public void setTurniCongelato(int turniCongelato) {
        this.turniCongelato = turniCongelato;
    }

    public int getTurniStordito() {
        return turniStordito;
    }

    public void setTurniStordito(int turniStordito) {
        this.turniStordito = turniStordito;
    }

    public boolean èMorto(Personaggio personaggio) {
        return personaggio.getPuntiVita() <= 0;
    }

    /**
     * Applica la protezione al personaggio per 1 turno. Se il personaggio è già
     * protetto la chiamata NON rinnova/estende la protezione. package-private:
     * solo classi nello stesso package domain possono invocarlo.
     */
    public void applicaProtezione() {
        if (!this.protetto) {
            this.protetto = true;
            this.turniProtetto = 1;
        }
    }

    /**
     * Verifica se il personaggio è attualmente protetto.
     */
    boolean isProtetto() {
        return this.protetto && this.turniProtetto > 0;
    }

    /**
     * Decrementa la protezione di un turno. Chiamare dopo che un attacco è
     * stato evitato (ossia quando la protezione ha impedito il danno) o alla
     * fine del turno.
     */
    public void decrementaProtezione() {
        if (this.turniProtetto > 0) {
            this.turniProtetto--;
            if (this.turniProtetto <= 0) {
                this.protetto = false;
                this.turniProtetto = 0;
            }
        }
    }

    /**
     * Chiamare all'inizio del turno del personaggio. Si occupa di consumare la
     * protezione (durata = 1) quando "tocca di nuovo" al personaggio.
     */
    public void onTurnStart() {
        // se era protetto, consumiamo la protezione ora (fine dell'effetto)
        if (isProtetto()) {
            decrementaProtezione();
        }
    }

    //formula base per calcolare danno: dobbiamo vedere se calcolarlo inquesto modo
    public int calcolaDanno() {
        return Math.max(0, this.attacco + this.livello * 2);
    }

    /**
     * Applica danno al personaggio rispettando la difesa e la protezione. - Se
     * il personaggio è protetto, il danno viene ignorato (la protezione NON
     * viene consumata qui). - Restituisce true se il personaggio è morto (PV <=
     * 0).
     */
    public int subisciDanno(int danno) {
        if (danno <= 0) {
            return 0;
        }
        if (isProtetto()) {
            // protezione impedisce il danno; verrà consumata in onTurnStart del personaggio
            return 0;
        }

        int dannoEffettivo = Math.max(0, danno - this.difesa);
        this.puntiVita -= dannoEffettivo;
        return dannoEffettivo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Personaggio{");
        sb.append("id=").append(id);
        sb.append(", nomeP=").append(nomeP);
        sb.append(", puntiVita=").append(puntiVita);
        sb.append(", puntiMana=").append(puntiMana);
        sb.append(", difesa=").append(difesa);
        sb.append(", statoPersonaggio=").append(statoPersonaggio);
        sb.append(", zaino=").append(zaino);
        sb.append(", attacco=").append(attacco);
        sb.append(", livello=").append(livello);
        sb.append(", esperienza=").append(esperienza);
        sb.append(", posizioneCorrente=").append(posizioneCorrente);
        sb.append('}');
        return sb.toString();
    }

}
