package domain;

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
    private int furto;
    private int turniStordito;
    private int turniDaSaltare;
    private Arma armaEquippaggiata;
    private String abilitàSpeciale;
    private int portafoglioPersonaggio;

    public Personaggio(String abilitàSpeciale, Arma armaEquippaggiata, int puntiDifesa,int esperienza, int id, int livello, String nomePersonaggio, Stanza posizioneCorrente, boolean protetto, int puntiMana, int puntiVita, String statoPersonaggio, int turniAvvelenato, int turniCongelato, int turniStordito, int turnoProtetto, Zaino zaino, int portafoglioPersonaggio) {
        this.abilitàSpeciale = abilitàSpeciale;
        this.armaEquippaggiata = armaEquippaggiata;
        this.puntiDifesa= puntiDifesa;
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
        this.furto = furto;
        this.zaino = zaino;
        this.portafoglioPersonaggio = portafoglioPersonaggio;
    }

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

    public int getPuntiDifesa() { return puntiDifesa;}

    public void setPuntiDifesa(int puntiDifesa) {
        this.puntiDifesa= puntiDifesa;
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
     * Aggiunge esperienza al personaggio e gestisce l'eventuale level-up. Usa
     * una soglia di 100 XP per livello. Se l'esperienza supera la soglia si
     * effettua il level-up ripetutamente consumando la soglia.
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

    public int getFurto() {
        return furto;
    }

    public void setFurto(int furto) {
        this.furto = furto;
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

    /**
     * Chiamare all'inizio del turno del personaggio. Si occupa di consumare la
     * protezione (durata = 1) quando "tocca di nuovo" al personaggio.
     */
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

    /**
     * Aggiunge N turni da saltare per questo personaggio.
     */
    public void aggiungiTurniDaSaltare(int n) {
        if (n <= 0) {
            return;
        }
        this.turniDaSaltare += n;
    }

    /**
     * Restituisce quanti turni rimangono da saltare.
     */
    public int getTurniDaSaltare() {
        return this.turniDaSaltare;
    }

    /**
     * Consuma un turno di salto all'inizio del turno. Restituisce true se il
     * turno deve essere saltato (prima del normale flusso di azione).
     */
    public boolean consumaSaltoTurno() {
        if (this.turniDaSaltare > 0) {
            this.turniDaSaltare--;
            return true;
        }
        return false;
    }

    /**
     * Applica danno al personaggio rispettando la punti 
    De la protezione. - Se
     * il personaggio è protetto, il danno viene ignorato (la protezione NON
     * viene consumata qui). - Restituisce true se il personaggio è morto (PV <=
     * 0).
     */
    public int subisciDanno(int danno) {
        if (danno <= 0) {
            return 0;
        }

        // protezione attiva: annulla danno
        if (this.turnoProtetto > 0) {
            return 0;
        }

        this.puntiVita -= danno;

        // se gli HP scendono a zero o meno, aggiorna stato e azzera gli HP
        if (this.èMorto(this)) {
            this.statoPersonaggio = "MORTO";

            System.out.println("[DEBUG] Il personaggio " + this.getNomePersonaggio() + " è morto (HP=" + this.getPuntiVita() + ")");
        }

        return danno;
    }

    public int subisciDannoPuntiDifesa(int dannoDifesa) {
        if (dannoDifesa <= 0) {
            return 0;
        }

        // protezione attiva: annulla danno
        if (this.turnoProtetto > 0) {
            return 0;
        }
        if (this.puntiDifesa <=0) {
            this.puntiDifesa=0;
            return this.subisciDanno(dannoDifesa);
        } else {
            this.puntiDifesa -=dannoDifesa;
        //  return puntiDifesa;
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
        if ( !zaino.getListaOggetti().contains(oggetto)) { return false; }

        // Pozione: l'oggetto applica l'effetto sul personaggio
        if (oggetto instanceof Pozione) {
            System.out.println("[USO OGGETTO] " + personaggio.getNomePersonaggio() + " usa " + oggetto.getNome()
                    + " (HP prima: " + personaggio.getPuntiVita() + ", Mana prima: " + personaggio.getPuntiMana() + ")");
            boolean risultato = ((Pozione) oggetto).eseguiEffetto(personaggio);
            if (risultato) {
                zaino.rimuoviOggettoDaZaino(oggetto);
                System.out.println(personaggio.getNomePersonaggio() + " usa una pozione: " + ((Pozione) oggetto).getNome());
            }
            return risultato;
        }

        // Arma: equipaggia / usa (metodo service usa Oggetto)
        if (oggetto instanceof Arma) {
            if (zaino == null || !zaino.getListaOggetti().contains(oggetto)) {
                return false; // L'arma non è nello zaino
            }
            if (personaggio.puoEquipaggiare(((Arma) oggetto).getTipoArma())) {
                System.out.println("[EQUIP] " + personaggio.getNomePersonaggio()
                        + " sta per equipaggiare: " + oggetto.getNome()
                        + " (attacco prima: " + personaggio.getAttacco()
                        + ", dannoBonus arma=" + ((Arma) oggetto).getDannoBonus() + ")");

                ((Arma) oggetto).eseguiEffetto(personaggio);

                System.out.println("[EQUIP] " + personaggio.getNomePersonaggio()
                        + " ha equipaggiato: " + oggetto.getNome()
                        + " (attacco dopo: " + personaggio.getAttacco() + ")");
                // decidere se rimuovere dall'inventario o mantenerla come equip
                zaino.rimuoviOggettoDaZaino(oggetto);
                return true;
            }
            return false; // Non può equipaggiare questo tipo di arma
        }

        // Armatura: indossa (metodo service indossaArmatura)
        if (oggetto instanceof Armatura) {
          
            ((Armatura) oggetto).eseguiEffetto(personaggio);
            
            zaino.rimuoviOggettoDaZaino(oggetto);
            return true;
        }

        if (oggetto instanceof Chiave) {
            ((Chiave) oggetto).eseguiEffetto(personaggio);
            zaino.rimuoviOggettoDaZaino(oggetto);
            return true;
        }

        // oggetto non gestito:  mettere eccezione
        return false;
    }

    public boolean raccogliereOggetto(Personaggio personaggio, Oggetto oggetto) {

        Zaino zaino = personaggio.getZaino();
        Stanza stanza = personaggio.getPosizioneCorrente();

        

        if (!stanza.getOggettiPresenti().contains(oggetto)) {
            return false;
        }

        // TESORO: applica subito, non va nello zaino
        if (oggetto instanceof Tesoro) {
            boolean applicato = ((Tesoro) oggetto).eseguiEffetto(personaggio);
            if (applicato) {
                stanza.rimuoviOggetto(oggetto);
                System.out.println(personaggio.getNomePersonaggio()
                        + " raccoglie " + oggetto.getNome()
                        + " e ottiene " + ((Tesoro) oggetto).getValore() + " monete.");
                return true;
            }
            return false;
        }
       
      

        // ARMA: controllo se può raccoglierla
        if (oggetto instanceof Arma) {
            Arma.TipoArma tipo = ((Arma) oggetto).getTipoArma();
            if (!this.puoRaccogliere(tipo)) {
                System.out.println(this.getNomePersonaggio()
                        + " non può raccogliere questo tipo di arma.");
                return false;
            }
        }

        // CASO NORMALE: zaino NON pieno
        if (!zaino.isPieno()) {
            zaino.aggiungiOggettoAZaino(oggetto);
            stanza.rimuoviOggetto(oggetto);
            System.out.println(personaggio.getNomePersonaggio()
                    + " raccoglie " + oggetto.getNome());
            return true;
        }

        //  ZAINO PIENO
        System.out.println("Zaino pieno! Oggetti presenti:");
        for (int i = 0; i < zaino.getListaOggetti().size(); i++) {
            System.out.println((i + 1) + ") "
                    + zaino.getListaOggetti().get(i).getNome());
        }

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.print("Vuoi eliminare un oggetto per fare spazio? (s/n): ");
        String risposta = scanner.nextLine().trim().toLowerCase();

        if (!risposta.equals("s")) {
            System.out.println("Oggetto non raccolto.");
            return false;
        }

        System.out.print("Scegli il numero dell'oggetto da eliminare (0 annulla): ");
        int scelta;

        try {
            scelta = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Input non valido.");
            return false;
        }

        if (scelta <= 0 || scelta > zaino.getListaOggetti().size()) {
            System.out.println("Scelta annullata.");
            return false;
        }

        Oggetto daRimuovere = zaino.getListaOggetti().get(scelta - 1);

        zaino.rimuoviOggettoDaZaino(daRimuovere);
        stanza.aggiungiOggetto(daRimuovere);

        zaino.aggiungiOggettoAZaino(oggetto);
        stanza.rimuoviOggetto(oggetto);

        System.out.println(personaggio.getNomePersonaggio()
                + " lascia " + daRimuovere.getNome()
                + " e raccoglie " + oggetto.getNome());

        return true;
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
