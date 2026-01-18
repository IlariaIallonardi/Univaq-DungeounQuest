

package domain;

import java.util.List;

public class Personaggio {

    private int id;
    private String nomePersonaggio;
    private int puntiVita;
    private int puntiMana;
    private int difesa;
    private String statoPersonaggio;
    private Zaino zaino;
    private int attacco ;
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
    // private Armatura armaturaEquippaggiata;
    private String abilitàSpeciale;

    private int portafoglioPersonaggio;

    public Personaggio(String abilitàSpeciale, Arma armaEquippaggiata, int difesa, int esperienza, int id, int livello, String nomePersonaggio, Stanza posizioneCorrente, boolean protetto, int puntiMana, int puntiVita, String statoPersonaggio, int turniAvvelenato, int turniCongelato, int turniStordito, int turnoProtetto, Zaino zaino, int portafoglioPersonaggio) {
        this.abilitàSpeciale = abilitàSpeciale;
        this.armaEquippaggiata = armaEquippaggiata;
        this.difesa = difesa;
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

   

    /**
     * Verifica se il personaggio è attualmente protetto.
     */
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
   public void onTurnStart() {
    // se era stata programmata una protezione per il prossimo turno,
    // assicurati che abbia durata minima
    if (this.protettoProssimoTurno) {
        this.protettoProssimoTurno = false;
        this.turnoProtetto = 1;
        return;
    }

    // consuma la protezione attiva
    if (this.turnoProtetto > 0) {
        this.turnoProtetto--;
        if (this.turnoProtetto <= 0) {
            this.protettoProssimoTurno = false;
            this.turnoProtetto = 0;
        }
    }

    // qui puoi mettere altre cose da eseguire all'inizio del turno, mantenute centralizzate
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
    public boolean consumeSaltoTurno() {
        if (this.turniDaSaltare > 0) {
            this.turniDaSaltare--;
            return true;
        }
        return false;
    }

    /**
     * Applica danno al personaggio rispettando la difesa e la protezione. - Se
     * il personaggio è protetto, il danno viene ignorato (la protezione NON
     * viene consumata qui). - Restituisce true se il personaggio è morto (PV <=
     * 0).
     */
    public int subisciDanno(int danno) {
    if (danno <= 0) return 0;

    // protezione attiva: annulla danno
    if (this.turnoProtetto > 0) return 0;


    this.puntiVita -= danno;
    return danno;
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
        if (personaggio == null || oggetto == null) {
            return false;
        }
        Zaino zaino = personaggio.getZaino();
        if (zaino == null || !zaino.getListaOggetti().contains(oggetto)) {
            return false;
        }

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
            System.out.println("[EQUIP] " + personaggio.getNomePersonaggio()
                    + " sta per indossare: " + oggetto.getNome()
                    + " (difesa prima: " + personaggio.getDifesa() + ")");
            ((Armatura) oggetto).eseguiEffetto(personaggio);
            System.out.println("[EQUIP] " + personaggio.getNomePersonaggio()
                    + " ha indossato: " + oggetto.getNome()
                    + " (difesa dopo: " + personaggio.getDifesa() + ")");
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

  /*   public boolean raccogliereOggetto(Personaggio personaggio, Oggetto oggetto) {
        if (personaggio == null || oggetto == null) {
            return false;
        }

        Zaino zaino = personaggio.getZaino();
        if (zaino == null) {
            return false;
        }

        Stanza stanza = personaggio.getPosizioneCorrente();
        if (stanza == null) {
            return false;
        }

        // verifica che l'oggetto sia nella stanza
        if (stanza.getOggettiPresenti() == null || !stanza.getOggettiPresenti().contains(oggetto)) {
            return false;
        }

        // verifica lista e capienza (capienza intesa come posti disponibili)
        if (zaino.getListaOggetti() == null) {
            return false;
        }
        // se è un Tesoro, applica l'effetto subito (non va nello zaino)
        if (oggetto instanceof Tesoro) {
            System.out.println("DEBUG raccogliereOggetto: personaggio identity=" + System.identityHashCode(personaggio));
            int saldoPrima = personaggio.getPortafoglioPersonaggio();
            System.out.println("Saldo prima raccolta: " + saldoPrima);

            boolean applicato = ((Tesoro) oggetto).eseguiEffetto(personaggio);
            if (applicato) {
                stanza.rimuoviOggetto(oggetto);
                System.out.println(personaggio.getNomePersonaggio() + " raccoglie " + oggetto.getNome() + " e ottiene " + ((Tesoro) oggetto).getValore() + " monete.");
                System.out.println("Saldo dopo raccolta: " + personaggio.getPortafoglioPersonaggio() + " monete. Identity: " + System.identityHashCode(personaggio));
                return true;
            } else {
                return false;
            }
        }
        if (oggetto instanceof Arma) {
            Arma.TipoArma tipo = ((Arma) oggetto).getTipoArma();
            if (!this.puoRaccogliere(tipo)) {
                System.out.println(this.getNomePersonaggio() + " non può raccogliere questo tipo di arma.");
                return false;
            }
        }

        //fino a qui tutto ok, provo ad aggiungere
        if (zaino.aggiungiOggettoAZaino(oggetto)) {
            stanza.rimuoviOggetto(oggetto);
            System.out.println(personaggio.getNomePersonaggio() + " raccoglie " + oggetto.getNome());
            return true;
        }

        // zaino pieno: mostra lista e chiedi se rimuovere qualcosa
        System.out.println("Zaino pieno. Oggetti nello zaino:");
        for (int i = 0; i < zaino.getListaOggetti().size(); i++) {
            Oggetto o = zaino.getListaOggetti().get(i);
            System.out.println((i + 1) + ") " + (o != null ? o.getNome() : "<oggetto null>"));
        }

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.print("Vuoi eliminare qualcosa per fare spazio? (s/n): ");
        String risposta = scanner.nextLine().trim().toLowerCase();
        if (!risposta.equals("s") && !risposta.equals("y")) {
            System.out.println("Operazione annullata. Non raccogliere l'oggetto.");
            return false;
        }

        System.out.print("Inserisci il numero dell'oggetto da rimuovere (0 per annullare): ");
        String line = scanner.nextLine().trim();
        int indice;
        try {
            indice = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("Input non valido. Operazione annullata.");
            return false;
        }
        if (indice == 0) {
            System.out.println("Operazione annullata.");
            return false;
        }
        if (indice < 1 || indice > zaino.getListaOggetti().size()) {
            System.out.println("Indice non valido. Operazione annullata.");
            return false;
        }

        Oggetto rimosso = zaino.getListaOggetti().get(indice - 1);
        if (zaino.rimuoviOggettoDaZaino(rimosso)) {
            stanza.aggiungiOggetto(rimosso);
        //    zaino.setCapienza(zaino.getCapienza()+1);
            System.out.println("Rimosso: " + (rimosso != null ? rimosso.getNome() : "<oggetto null>"));
            // prova ad aggiungere il nuovo oggetto (dovrebbe riuscire)
            if (zaino.aggiungiOggettoAZaino(oggetto)) {
                stanza.rimuoviOggetto(oggetto);
                System.out.println(personaggio.getNomePersonaggio() + " raccoglie " + oggetto.getNome());
                return true;
            } else {
                // rollback minimale: rimetti l'oggetto rimosso nello zaino
                System.out.println("Impossibile aggiungere l'oggetto dopo la rimozione, ripristino.");
                zaino.aggiungiOggettoAZaino(rimosso);
                stanza.rimuoviOggetto(rimosso);
                return false;
            }
        } else {
            System.out.println("Errore rimozione oggetto dallo zaino.");
            return false;
        }
    }*/
   public boolean raccogliereOggetto(Personaggio personaggio, Oggetto oggetto) {

    if (personaggio == null || oggetto == null) {
        return false;
    }

    Zaino zaino = personaggio.getZaino();
    Stanza stanza = personaggio.getPosizioneCorrente();

    if (zaino == null || stanza == null) {
        return false;
    }

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


    public void addMonete(int amount) {
        if (amount <= 0) {
            return;
        }
        this.portafoglioPersonaggio += amount;
    }

    public boolean removeMonete(int amount) {
        if (amount <= 0) {
            return false;
        }
        if (this.portafoglioPersonaggio < amount) {
            return false;
        }
        this.portafoglioPersonaggio -= amount;
        return true;
    }

    public boolean hasMonete(int amount) {
        return this.portafoglioPersonaggio >= amount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Personaggio{");
        sb.append("id=").append(id);
        sb.append(", nomePersonaggio=").append(nomePersonaggio);
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

    public int getTurnoProtetto() {
        return turnoProtetto;
    }

    public void setTurnoProtetto(int turnoProtetto) {
        this.turnoProtetto = turnoProtetto;
    }

    public void scegliEquipaggiamento(Personaggio personaggio) {
        Zaino zaino = personaggio.getZaino();
        List<Oggetto> oggetti = zaino.getListaOggetti();

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getAbilitàSpeciale() {
        return abilitàSpeciale;
    }

    public void setAbilitàSpeciale(String abilitàSpeciale) {
        this.abilitàSpeciale = abilitàSpeciale;
    }

}