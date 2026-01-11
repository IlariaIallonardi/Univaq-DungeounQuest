package service.impl;

import java.util.concurrent.ThreadLocalRandom;

import domain.Arciere;
import domain.Combattimento;
import domain.Mostro;
import domain.Personaggio;
import domain.Stanza;
import domain.Zaino;
import service.PersonaggioService;
import util.ANSI;

public class ArciereServiceImpl implements PersonaggioService {

    private static final int bonusAttaccoArciere = 10;
    private static final int bonusDannoLivello = 3;

   @Override
    public int attacca(Personaggio personaggio, Mostro mostro, Combattimento combattimento) {

        if (!(personaggio instanceof Arciere arciere)) {
            System.out.println("Solo un Arciere può usare questo attacco.");
            return 0;
        }

        if (mostro == null) {
            System.out.println("Nessun bersaglio valido.");
            return 0;
        }

        Stanza stanzaArciere = arciere.getPosizioneCorrente();
        Stanza stanzaMostro  = mostro.getPosizioneCorrenteMostro();

        if (stanzaArciere == null || stanzaMostro == null) {
            System.out.println("Errore di posizione.");
            return 0;
        }

        //  Regola Arciere: stessa stanza O stanza adiacente
        if (!stanzaArciere.equals(stanzaMostro) && !sonoAdiacenti(stanzaArciere, stanzaMostro)) {
            System.out.println("Bersaglio troppo lontano: puoi colpire solo stanza corrente o adiacenti.");
            return 0;
        }

        return attaccoDistanzaArciere(arciere, mostro);
    }

    /**
     * Attacco a distanza: l'arciere può colpire solo stanze adiacenti; Se nella
     * stanza bersaglio ci sono personaggi, attacca il primo valido incontrato.
     *
     * Nota: questo metodo assume che: - Arciere abbia getPosizioneCorrente() -
     * Stanza esponga getStanzaAdiacente(): Map<String, Stanza>
     * - Stanza esponga getPersonaggi(): List<Personaggio>
     *
     * Adatta i nomi dei metodi se nella tua implementazione differiscono.
     */
   public int attaccoDistanzaArciere(Arciere arciere, Mostro mostro) {

        ThreadLocalRandom random = ThreadLocalRandom.current();

        int tiro = random.nextInt(1, 21); // d20

        int bonusAttacco = arciere.getAttacco() + (arciere.getLivello() / 2);
        int totale = tiro + bonusAttacco;

        int CA = mostro.getDifesaMostro();

        System.out.println(
            arciere.getNomePersonaggio() +
            " (tiro a distanza) tiro: " + tiro + " + bonus " + bonusAttacco +
            " = " + totale + " (CA mostro: " + CA + ")"
        );

        if (tiro == 1) {
            System.out.println("Il colpo fallisce!");
            return 0;
        }

        boolean critico = (tiro == 20);

        if (!critico && totale < CA) {
            System.out.println("La freccia manca il bersaglio.");
            return 0;
        }

        // Danno: 1d8 (freccia), puoi fare 1d6 se vuoi nerfarlo
        int dadoDanno = random.nextInt(1, 9); // 1..8

        int bonusFissi = bonusAttaccoArciere + arciere.getLivello() * bonusDannoLivello;

        int dadiTotali = dadoDanno;

        if (critico) {
            int dadoExtra = random.nextInt(1, 9);
            dadiTotali += dadoExtra;
            System.out.println(
                ANSI.BRIGHT_RED + ANSI.BOLD +
                "COLPO CRITICO! Dadi danno raddoppiati!" +
                ANSI.RESET
            );
        }

        int dannoNetto = Math.max(1, dadiTotali + bonusFissi);

        mostro.setPuntiVitaMostro(mostro.getPuntiVitaMostro() - dannoNetto);

        System.out.println(
            arciere.getNomePersonaggio() +
            " infligge " + dannoNetto +
            " danni a " + mostro.getNomeMostro() +
            " (HP rimasti: " + mostro.getPuntiVitaMostro() + ")"
        );


        return dannoNetto;
    }
    private boolean sonoAdiacenti(Stanza a, Stanza b) {

        // --- Variante A: se Stanza ha una lista/set di adiacenti ---
        // return a.getStanzeAdiacenti() != null && a.getStanzeAdiacenti().contains(b);

        // --- Variante B: se Stanza ha una mappa uscite (direzione -> stanza) ---
        // return a.getUscite() != null && a.getUscite().containsValue(b);

        // Fallback "sicuro" se non so la tua API: niente adiacenza
        return false;
    }

 

    @Override
   public Personaggio creaPersonaggio(String nome, Personaggio personaggio) {
Stanza stanza = null;
Zaino zaino = new Zaino();
return new Arciere("abilità", null, 200, 300, 0, 2, nome, stanza, false, 100, 300, "normale", 0, 0, 0, 0, zaino, 0);
}


    public void usaAbilitàSpeciale(Personaggio personaggio, String abilitàSpeciale) {
    }

  
}
