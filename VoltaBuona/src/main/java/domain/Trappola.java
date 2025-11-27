package domain;

public class Trappola extends Evento {

    private int danno;
    private Effetto effetto;

    public Trappola(int id, boolean inizioEvento, boolean fineEvento, String descrizione, int danno, Effetto effetto) {
        super(id, inizioEvento, fineEvento, descrizione);
        this.danno = danno;
        this.effetto = effetto;
    }

    
    public int getDanno() {
        return danno;
    }

    public boolean alterareStato() {
        // cambia stato stanza/giocatore
        return true;
    }

    public int getDannoTrappola() {
        return danno;
    }

    public void setDanno(int danno) {
        this.danno = danno;
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

        // difficoltà della trappola (puoi modificarla come attributo)
        int difficolta = 4; // esempio: serve un tiro >= 4

        int dado = (int) (Math.random() * 6) + 1;

        System.out.println(" Tiro per disinnescare: " + dado + " (CD = " + difficolta + ")");

        if (dado >= difficolta) {
            System.out.println(" Hai disinnescato la trappola!");
            return true;
        }

        System.out.println(" Disinnesco fallito! La trappola si attiva!");
        return false;
    }

    public void attiva(Personaggio personaggio) {

        Effetto.TipoEffetto effetto = tiraDado();

        switch (effetto) {

            case CONGELAMENTO -> {
                personaggio.setStatoPersonaggio("Congelato");
                personaggio.setTurniCongelato(3);
                System.out.println(" La trappola ti ferisce leggermente! -5 HP");
                break;
            }

            case FURIA -> {
                personaggio.setStatoPersonaggio("Furia");
                personaggio.subisciDanno(15);
                System.out.println(" La trappola causa un danno grave! -15 HP");
                break;
            }

            case AVVELENAMENTO -> {
                personaggio.setStatoPersonaggio("AVVELENATO");
                personaggio.setTurniAvvelenato(4);
                System.out.println(" Sei stato avvelenato!");
                break;
            }

            case STORDIMENTO -> {
                personaggio.setStatoPersonaggio("STORDITO");
                personaggio.setTurniStordito(2);
                System.out.println(" Sei stordito!");
                break;
            }

            case IMMOBILIZZATO -> {
                personaggio.setStatoPersonaggio("IMMOBILIZZATO");
                personaggio.subisciDanno(5);
                System.out.println(" Sei immobilizzato!");
                break;
            }
        }
    }

    private Effetto.TipoEffetto tiraDado() {
        int dado = (int) (Math.random() * 6) + 1;

        return switch (dado) {
            case 1 ->
                Effetto.TipoEffetto.CONGELAMENTO;
            case 2 ->
                Effetto.TipoEffetto.FURIA;
            case 3 ->
                Effetto.TipoEffetto.AVVELENAMENTO;
            case 4 ->
                Effetto.TipoEffetto.STORDIMENTO;
            case 5 ->
                Effetto.TipoEffetto.IMMOBILIZZATO;
            default ->
                Effetto.TipoEffetto.NESSUN_EFFETTO;
        };
    }

    //main funzionante per testare trappola
    /* public static void main(String[] args) {

        // 1️⃣ Creazione personaggio di test
        Personaggio p = new Personaggio(0, 0, 0, 0, 0, null, null, 0, 0, null, null);
        p.setNomeP("Eroe");
        p.setPuntiVita(40);
        p.setStatoPersonaggio("NORMALE");

        System.out.println("=== TEST TRAPPOLA ===");
        System.out.println("Personaggio: " + p.getNomeP());
        System.out.println("HP iniziali: " + p.getPuntiVita());
        System.out.println();

        // 2️⃣ Creazione Effetto (non usato direttamente nel tuo metodo)
        Effetto effetto = new Effetto(null, null, 0);  // Se usi costruttori diversi, dimmelo e lo correggo

        // 3️⃣ Creazione Trappola
        Trappola t = new Trappola(
                1, // id
                true, // inizioEvento
                false, // fineEvento
                "Trappola nascosta", // descrizione
                10, // danno
                effetto // effetto
        );

        // 4️⃣ Test: tentativi multipli per vedere esiti diversi
        boolean disinnescata = false;

        for (int i = 1; i <= 5 && !disinnescata; i++) {

            System.out.println("\n--- Tentativo " + i + " di disinnesco ---");

            boolean disinnescata1 = t.checkDiDisinnesco(p);

            if (!disinnescata1) {
                System.out.println("⚠ ATTIVAZIONE TRAPPOLA!");
                t.attiva(p);
            } else {
                disinnescata = true;
            }

            System.out.println("HP attuali: " + p.getPuntiVita());
            System.out.println("Stato attuale: " + p.getStatoPersonaggio());
        }

        System.out.println("\n=== TEST COMPLETATO ===");
    }*/

        /* 
    public static void main(String[] args) {

        // 1️⃣ Creo un personaggio di test
        Personaggio p = new Guerriero("Arthas");
        p.setPuntiVita(50);
        p.setStatoPersonaggio("NORMALE");

        // inizializzo a 0 gli effetti a turni
        p.setTurniAvvelenato(0);
        p.setTurniCongelato(0);
        p.setTurniStordito(0);

        // 2️⃣ Creo una trappola finta
        Trappola trappola = new Trappola(
                1,
                true,
                false,
                "Trappola magica",
                10,
                null
        );

        // 3️⃣ Creo TurnoService
        PersonaggioService ps = null;
        TurnoService turnoService = new TurnoService(ps);

        System.out.println("\n=== TEST TRAPPOLA ===");
        System.out.println("🚶 Il personaggio entra nella stanza...");
        System.out.println("Stato iniziale: " + p.getStatoPersonaggio());
        System.out.println("HP iniziali: " + p.getPuntiVita());

        // 4️⃣ Il personaggio prova a disinnescare la trappola
        boolean disinnesco = trappola.checkDiDisinnesco(p);

        if (!disinnesco) {
            System.out.println("➡ La trappola SI ATTIVA!");
            trappola.attiva(p);
        }

        // 5️⃣ Simuliamo più turni
        System.out.println("=== SIMULAZIONE TURNI ===");

        for (int turno = 1; turno <= 5; turno++) {

            System.out.println("--- TURNO " + turno + " ---");

            if (p.èMorto(p)) {
                System.out.println("💀 Il personaggio è morto. Fine test.");
                break;
            }

            // Azioni normali (fittizie)
            System.out.println("Il personaggio prova a compiere azioni...");

            // Fine turno → applica effetti
            turnoService.aggiornaEffettiFineTurno(p);

            System.out.println("HP attuali: " + p.getPuntiVita());
            System.out.println("Stato attuale: " + p.getStatoPersonaggio());
        }

        // 6️⃣ Test antidoto
        System.out.println("=== TEST POZIONE ANTIDOTO ===");

        Pozione antidoto = new Pozione(disinnesco, null, 0, 0, null, null, disinnesco, disinnesco, disinnesco);

        System.out.println("Usiamo l'antidoto...");
        antidoto.eseguiEffetto(p);

        System.out.println("Stato personaggio dopo antidoto: " + p.getStatoPersonaggio());
        System.out.println("Turni avvelenato: " + p.getTurniAvvelenato());
        System.out.println("Turni congelato: " + p.getTurniCongelato());
        System.out.println("Turni stordito: " + p.getTurniStordito());
    }*/

    
    // ...existing code...
    public static void main(String[] args) {
        // crea Personaggio con il costruttore che usi nel progetto
        Personaggio p = new Personaggio(0, 34, 0, 0, 1, "EroeTest", null, 10, 40, "NORMALE", null);

        System.out.println("HP iniziali: " + p.getPuntiVita());
        System.out.println("Stato iniziale: " + p.getStatoPersonaggio());
        System.out.println();

        // trappola di prova con effetto AVVELENAMENTO
        Effetto effetto = new Effetto(Effetto.TipoEffetto.AVVELENAMENTO, "Veleno", 3);
        Trappola tr = new Trappola(1, true, false, "Trappola di prova", 10, effetto);

        // tentativo di disinnesco e possibile attivazione
        boolean disinnescata = tr.checkDiDisinnesco(p);
        if (!disinnescata) {
            System.out.println("➡ La trappola SI ATTIVA!");
            tr.attiva(p);
        } else {
            System.out.println("➡ Trappola disinnescata.");
        }

        System.out.println();
        System.out.println("HP finali: " + p.getPuntiVita());
        System.out.println("Stato finale: " + p.getStatoPersonaggio());
    }
}