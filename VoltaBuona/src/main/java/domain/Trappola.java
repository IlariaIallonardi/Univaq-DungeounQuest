package domain;

public class Trappola extends Evento {

    
    private Effetto effetto;
    private Stanza posizioneCorrenteTrappola;

    public Trappola( Effetto effetto, Stanza posizioneCorrenteTrappola, int id, boolean inizioEvento, boolean fineEvento, String descrizione) {
        super(id, inizioEvento, fineEvento, descrizione, "Trappola", posizioneCorrenteTrappola);
    
        this.effetto = effetto;
        this.posizioneCorrenteTrappola = posizioneCorrenteTrappola;
    }

   

    public boolean alterareStato() {
        // cambia stato stanza/giocatore
        return true;
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

    /**
 * Tenta di disinnescare la trappola per il `personaggio` passato.
 *
 * Comportamento corrente:
 *  - Usa un tiro d6 casuale confrontato con una difficoltà fissa (CD = 4).
 *  - Stampa il risultato del tiro e ritorna true se il disinnesco ha successo.
 */

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

    //main funzionante per testare trappola
    /*public static void main(String[] args) {

        // 1️⃣ Creazione personaggio di test
        Personaggio p = new Personaggio(0, 0, 0, 0, 0, null, null, 0, 0, null, null);
        p.setNomePersonaggio("Eroe");
        p.setPuntiVita(40);
        p.setStatoPersonaggio("NORMALE");

        System.out.println("=== TEST TRAPPOLA ===");
        System.out.println("Personaggio: " + p.getNomePersonaggio());
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
                attiva(p);
            } else {
                disinnescata = true;
            }

            System.out.println("HP attuali: " + p.getPuntiVita());
            System.out.println("Stato attuale: " + p.getStatoPersonaggio());
        }

        System.out.println("\n=== TEST COMPLETATO ===");
    }

    /* 
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
    } */
    public Stanza getPosizioneCorrenteTrappola() {
        return posizioneCorrenteTrappola;
    }

    public void setPosizioneCorrenteTrappola(Stanza posizioneCorrenteTrappola) {
        this.posizioneCorrenteTrappola = posizioneCorrenteTrappola;
    }
}
