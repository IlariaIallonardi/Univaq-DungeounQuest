package domain;

import service.ZainoService;

public class Personaggio {

    private int id;
    private String nomePersonaggio;
    private int puntiVita;
    private int puntiMana;
    private int puntiDifesa;
    private String statoPersonaggio;
    private Zaino zaino;
    private int attacco;
    private int livello;
    private int esperienza;
    private Stanza posizioneCorrente;
    private boolean protettoProssimoTurno;
    private int turnoProtetto;
    private int turniAvvelenato;
    private boolean disarmato;
    private int turniStordito;
    private int turniDaSaltare;
    private Arma armaEquippaggiata;
    private String abilitàSpeciale;
    private int portafoglioPersonaggio;

    public Personaggio(String abilitàSpeciale, Arma armaEquippaggiata, int puntiDifesa, int esperienza, int id, int livello, String nomePersonaggio, Stanza posizioneCorrente, boolean protetto, int puntiMana, int puntiVita, String statoPersonaggio, int turniAvvelenato, int turniCongelato, int turniStordito, int turnoProtetto, Zaino zaino, int portafoglioPersonaggio) {
        this.abilitàSpeciale = abilitàSpeciale;
        this.armaEquippaggiata = armaEquippaggiata;
        this.puntiDifesa = puntiDifesa;
        this.esperienza = esperienza;
        this.id = id;
        this.livello = livello;
        this.nomePersonaggio = nomePersonaggio;
        this.posizioneCorrente = posizioneCorrente;
        this.protettoProssimoTurno = protetto;
        this.puntiMana = puntiMana;
        this.puntiVita = puntiVita;
        this.statoPersonaggio = statoPersonaggio;
        this.turniAvvelenato = turniAvvelenato;
        this.disarmato = disarmato;
        this.turniStordito = turniStordito;
        this.turnoProtetto = turnoProtetto;
        this.zaino = zaino;
        this.portafoglioPersonaggio = portafoglioPersonaggio;
    }

    private ZainoService zainoService = new ZainoService();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomePersonaggio() {
        return nomePersonaggio;
    }

    public void setNomePersonaggio(String nomePersonaggio) {
        this.nomePersonaggio = nomePersonaggio;
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

    public int getPuntiDifesa() {
        return puntiDifesa;
    }

    public void setPuntiDifesa(int puntiDifesa) {
        this.puntiDifesa = puntiDifesa;
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

    /**
     * Aumenta esperienza al personaggio per aumentare di livello, se raggiunge
     * esperienza =100 passa al livello successivo incrementando i paramentri
     * vitali,di difesa,attacco,i mana
     *
     */
    public int aggiungiEsperienza() {
        this.esperienza += 25;
        int xpPerLivello = 100;
        while (this.esperienza >= xpPerLivello) {
            this.livello = this.livello + 1;
            this.puntiVita += 10;
            this.attacco += 2;
            this.puntiDifesa += 1;
            this.puntiMana += 5;
            this.esperienza -= xpPerLivello;
            System.out.println(this.nomePersonaggio + " sale al livello " + this.livello + "!");
        }
        return this.livello;
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

    public boolean isDisarmato() {
        return disarmato;
    }

    public void setDisarmato(boolean disarmato) {
        this.disarmato = disarmato;
    }

    public int getTurniStordito() {
        return turniStordito;
    }

    public void setTurniStordito(int turniStordito) {
        this.turniStordito = turniStordito;
    }

    public int getPortafoglioPersonaggio() {
        return portafoglioPersonaggio;
    }

    public void setPortafoglioPersonaggio(int portafoglioPersonaggio) {
        this.portafoglioPersonaggio = portafoglioPersonaggio;
    }

    public boolean èMorto(Personaggio personaggio) {

        return personaggio.getPuntiVita() <= 0;
    }

    public boolean isProtetto() {
        return this.protettoProssimoTurno && this.turnoProtetto > 0;
    }

    public boolean prenotaProtezione() {
        if (this.turnoProtetto > 0 || this.protettoProssimoTurno) {
            return false;
        }
        this.protettoProssimoTurno = true;
        return true;
    }

    public void calcolaProtezione() {

        if (this.protettoProssimoTurno) {
            this.protettoProssimoTurno = false;
            this.turnoProtetto = 1;
            return;
        }

        if (this.turnoProtetto > 0) {
            this.turnoProtetto--;
            if (this.turnoProtetto <= 0) {
                this.protettoProssimoTurno = false;
                this.turnoProtetto = 0;
            }
        }
    }

    public void aggiungiTurniDaSaltare(int n) {
        if (n <= 0) {
            return;
        }
        this.turniDaSaltare += n;
    }

    public int getTurniDaSaltare() {
        return this.turniDaSaltare;
    }

    public boolean consumaSaltoTurno() {
        if (this.turniDaSaltare > 0) {
            this.turniDaSaltare--;
            return true;
        }
        return false;
    }

    public int subisciDanno(int danno) {
        if (danno <= 0) {
            return 0;
        }

        if (this.turnoProtetto > 0) {
            return 0;
        }

        this.puntiVita -= danno;

        if (this.èMorto(this)) {
            this.statoPersonaggio = "MORTO";

            System.out.println("Il personaggio " + this.getNomePersonaggio() + " è morto (HP=" + this.getPuntiVita() + ")");
        }

        return danno;
    }

    public int subisciDannoPuntiDifesa(int dannoDifesa) {
        if (dannoDifesa <= 0) {
            return 0;
        }

        if (this.turnoProtetto > 0) {
            return 0;
        }
        if (this.puntiDifesa <= 0) {
            this.puntiDifesa = 0;
            return this.subisciDanno(dannoDifesa);
        } else {
            this.puntiDifesa -= dannoDifesa;

        }
        return puntiDifesa;
    }

    public Arma getArmaEquippaggiata() {
        return armaEquippaggiata;
    }

    public void setArmaEquippaggiata(Arma armaEquippaggiata) {
        this.armaEquippaggiata = armaEquippaggiata;
    }

    public boolean puoRaccogliere(Arma.TipoArma tipo) {

        return true;
    }

    public boolean puoEquipaggiare(Arma.TipoArma tipo) {
        return true;
    }

    public boolean usaOggetto(Personaggio personaggio, Oggetto oggetto) {

        Zaino zaino = personaggio.getZaino();
        if (!zaino.getListaOggetti().contains(oggetto)) {
            return false;
        }

        if (oggetto instanceof Pozione) {

            boolean risultato = ((Pozione) oggetto).eseguiEffetto(personaggio);
            System.out.println(personaggio.getNomePersonaggio() + " usa " + oggetto.getNome()
                    + " (Punti vita:" + personaggio.getPuntiVita() + ", Mana: " + personaggio.getPuntiMana() + ")");
            if (risultato) {
                zainoService.rimuoviOggettoDaZaino(zaino, oggetto);

            }
            return risultato;
        }

        if (oggetto instanceof Arma) {
            if (zaino == null || !zaino.getListaOggetti().contains(oggetto)) {
                return false;
            }
            if (personaggio.puoEquipaggiare(((Arma) oggetto).getTipoArma())) {

                ((Arma) oggetto).eseguiEffetto(this);

                System.out.println(this.getNomePersonaggio() + " ha equipaggiato: " + oggetto.getNome() + " (attacco dopo: " + this.getAttacco() + ")");

                zainoService.rimuoviOggettoDaZaino(zaino, oggetto);
                return true;
            }
            return false;
        }

        if (oggetto instanceof Armatura) {

            ((Armatura) oggetto).eseguiEffetto(personaggio);

            zainoService.rimuoviOggettoDaZaino(zaino, oggetto);
            return true;
        }

        if (oggetto instanceof Chiave) {
            ((Chiave) oggetto).eseguiEffetto(personaggio);
            zainoService.rimuoviOggettoDaZaino(zaino, oggetto);
            return true;
        }

        return false;
    }

    public boolean raccogliereOggetto(Oggetto oggetto) {

        Zaino zaino = this.getZaino();
        Stanza stanza = this.getPosizioneCorrente();

        if (!stanza.getOggettiPresenti().contains(oggetto)) {
            return false;
        }

        // Tesoro non va nello zaino ma direttamente nel portafoglio.
        if (oggetto instanceof Tesoro) {
            boolean applicato = ((Tesoro) oggetto).eseguiEffetto(this);
            if (applicato) {
                stanza.rimuoviOggetto(oggetto);
                System.out.println(this.getNomePersonaggio()
                        + " raccoglie " + oggetto.getNome()
                        + " e ottiene " + ((Tesoro) oggetto).getValore() + " monete.");
                return true;
            }
            return false;
        }

        // L'arma controlliamo prima se può raccoglierla.
        if (oggetto instanceof Arma) {
            Arma.TipoArma tipo = ((Arma) oggetto).getTipoArma();
            if (!this.puoRaccogliere(tipo)) {
                System.out.println(this.getNomePersonaggio()
                        + " non può raccogliere questo tipo di arma.");
                return false;
            }
        }

        return new ZainoService().èPieno(zaino, stanza, oggetto, this);
    }

    public void aggiungiMonete(int sommaMonete) {
        if (sommaMonete <= 0) {
            return;
        }
        this.portafoglioPersonaggio += sommaMonete;
    }

    public boolean rimuoviMonete(int sommaMonete) {
        if (sommaMonete <= 0) {
            return false;
        }
        if (this.portafoglioPersonaggio < sommaMonete) {
            return false;
        }
        this.portafoglioPersonaggio -= sommaMonete;
        return true;
    }

    public boolean haMonete(int sommaMonete) {
        return this.portafoglioPersonaggio >= sommaMonete;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Personaggio{");
        sb.append("id=").append(id);
        sb.append(", nomePersonaggio=").append(nomePersonaggio);
        sb.append(", puntiVita=").append(puntiVita);
        sb.append(", puntiMana=").append(puntiMana);
        sb.append(", puntiDifesa=").append(puntiDifesa);
        sb.append(", statoPersonaggio=").append(statoPersonaggio);
        sb.append(", zaino=").append(zaino);
        sb.append(", attacco=").append(attacco);
        sb.append(", livello=").append(livello);
        sb.append(", esperienza=").append(esperienza);
        sb.append(", posizioneCorrente=").append(posizioneCorrente);
        sb.append('}');
        return sb.toString();
    }

    public int getTurnoProtetto() {
        return turnoProtetto;
    }

    public void setTurnoProtetto(int turnoProtetto) {
        this.turnoProtetto = turnoProtetto;
    }

    public String getAbilitàSpeciale() {
        return abilitàSpeciale;
    }

    public void setAbilitàSpeciale(String abilitàSpeciale) {
        this.abilitàSpeciale = abilitàSpeciale;
    }

}
