package domain;

public class Personaggio {

    private int id;
    private String nomePersonaggio;
    private int puntiVita;
    private int puntiMana;
    private int difesa;
    private String statoPersonaggio;
    private Zaino zaino;
    private int attacco=5;
    private int livello;
    private int esperienza;
    private Stanza posizioneCorrente;
    private boolean protetto;
    private int turnoProtetto;
    private int turniAvvelenato;
    private int turniCongelato;
    private int turniStordito;

    public Personaggio(int attacco, int difesa, int esperienza, int id, int livello, String nomePersonaggio, Stanza posizioneCorrente, int puntiMana, int puntiVita, String statoPersonaggio, Zaino zaino) {
        this.attacco = attacco;
        this.difesa = difesa;
        this.esperienza = esperienza;
        this.id = id;
        this.livello = livello;
        this.nomePersonaggio = nomePersonaggio;
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
            this.turnoProtetto = 1;
        }
    }

    /**
     * Verifica se il personaggio è attualmente protetto.
     */
    boolean isProtetto() {
        return this.protetto && this.turnoProtetto > 0;
    }

    /**
     * Decrementa la protezione di un turno. Chiamare dopo che un attacco è
     * stato evitato (ossia quando la protezione ha impedito il danno) o alla
     * fine del turno.
     */
    public void decrementaProtezione() {
        if (this.turnoProtetto > 0) {
            this.turnoProtetto--;
            if (this.turnoProtetto <= 0) {
                this.protetto = false;
                this.turnoProtetto = 0;
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

     public boolean usaOggetto(Personaggio personaggio, Oggetto oggetto) {
        if (personaggio == null || oggetto == null) {
            return false;
        }
        Zaino z = personaggio.getZaino();
        if (z == null || !z.getListaOggetti().contains(oggetto)) {
            return false;
        }

        // Pozione: l'oggetto applica l'effetto sul personaggio
        if (oggetto instanceof Pozione) {
            boolean risultato = ((Pozione) oggetto).eseguiEffetto(personaggio);
            if (risultato) {
                z.rimuovi(oggetto);
                z.setCapienza(z.getCapienza() + 1);
                System.out.println(personaggio.getNomePersonaggio() + " usa una pozione: " + ((Pozione) oggetto).getTipo());
            }
            return risultato;
        }

        // Arma: equipaggia / usa (metodo service usa Oggetto)
        if (oggetto instanceof Arma) {
            Zaino zaino = personaggio.getZaino();
            if (zaino == null || !zaino.getListaOggetti().contains(oggetto)) {
                return false; // L'arma non è nello zaino
            }
            ((Arma) oggetto).eseguiEffetto(personaggio);
            // decidere se rimuovere dall'inventario o mantenerla come equip
            zaino.rimuovi(oggetto);
            zaino.setCapienza(zaino.getCapienza() + 1);
            return true;
        }

        // Armatura: indossa (metodo service indossaArmatura)
        if (oggetto instanceof Armatura) {
            ((Armatura) oggetto).eseguiEffetto(personaggio);
            z.setCapienza(z.getCapienza() + 1);
            return true;
        }

        
        if (oggetto instanceof Chiave) {
            ((Chiave) oggetto).eseguiEffetto(personaggio);

            z.rimuovi(oggetto);
            z.setCapienza(z.getCapienza() + 1);
            return true;
        }

        // oggetto non gestito:  mettere eccezione
        return false;
    }



      public boolean raccogliereOggetto(Personaggio personaggio, Oggetto oggetto) {
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

        if (zaino.getCapienza() < 5) {
            // spazio disponibile: aggiungi direttamente
            zaino.getListaOggetti().add(oggetto);
            zaino.setCapienza(zaino.getCapienza() - 1);
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

        // rimuovi l'oggetto scelto dallo zaino e rimetti nella stanza
        Oggetto rimosso = zaino.getListaOggetti().remove(indice - 1);
        zaino.setCapienza(zaino.getCapienza() + 1);
        // aggiungi l'oggetto rimosso nella stanza (usa il metodo disponibile)
        stanza.aggiungiOggetto(rimosso);
        System.out.println("Rimosso: " + (rimosso != null ? rimosso.getNome() : "<oggetto null>"));
        // ora aggiungi il nuovo oggetto raccolto
        zaino.getListaOggetti().add(oggetto);
        zaino.setCapienza(zaino.getCapienza() - 1);
        stanza.rimuoviOggetto(oggetto);

        System.out.println(personaggio.getNomePersonaggio() + " raccoglie " + oggetto.getNome());
        return true;
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

}
