/*package service.impl;

import domain.Arma;
import domain.Combattimento;
import domain.Guerriero;
import domain.Mostro;
import domain.Personaggio;
import domain.Stanza;
import domain.Zaino;
import service.PersonaggioService;
import util.ANSI;

public class GuerrieroServiceImpl implements PersonaggioService {

    /**
     * Il guerriero protegge un compagno aumentandogli la difesa per un turno (o
     * finché non gestisci la durata altrove).
     
    public boolean proteggiCompagno(Guerriero guerriero, Personaggio alleato) {
        if (guerriero == null || alleato == null) {
            return false;
        }

        // ad esempio: il guerriero deve avere almeno un po' di vita
        if (guerriero.getPuntiVita() <= 0) {
            System.out.println(" Il guerriero è troppo debole per proteggere qualcuno.");
            return false;
        }

        // puoi gestire in Personaggio un flag tipo "protetto" o aumento difesa temporaneo
        int bonusDifesa = 8;
        alleato.setDifesa(alleato.getDifesa() + bonusDifesa);
        alleato.setTurnoProtetto(alleato.getTurnoProtetto() + 1);

        System.out.println(" " + guerriero.getNomePersonaggio()
                + " si para davanti e protegge " + alleato.getNomePersonaggio()
                + " (+" + bonusDifesa + " difesa per il prossimo turno)");

        return true;
    }

    @Override
    public int attacca(Personaggio personaggio, Mostro mostro, Combattimento combattimento) {

        if (!(personaggio instanceof Guerriero guerriero)) {
            System.out.println(" Solo un Guerriero può usare questo tipo di attacco fisico potenziato.");
            return 0;
        }

        if (mostro == null) {
            System.out.println(" Nessun bersaglio valido da colpire.");
            return 0;
        }

        Stanza stanzaGuerriero = guerriero.getPosizioneCorrente();
        Stanza stanzaMostro = mostro.getPosizioneCorrenteMostro();

        if (stanzaGuerriero == null || stanzaMostro == null) {
            System.out.println(" Errore di posizione: Guerriero o Mostro non sono in una stanza valida.");
            return 0;
        }

        // Il guerriero può attaccare solo in corpo a corpo → stessa stanza
        if (!stanzaGuerriero.equals(stanzaMostro)) {
            System.out.println("⚠ Il Guerriero deve trovarsi nella stessa stanza del mostro per attaccare.");
            return 0;
        }

        return attaccoFisico(guerriero, mostro);
    }

  

    public int attaccoFisico(Guerriero guerriero, Mostro mostro) {

    int attacco = guerriero.getAttacco() + 15;
    int livello = guerriero.getLivello();
    int difesaMostro = mostro.getDifesaMostro();
    Arma arma = guerriero.getArmaEquippaggiata();
if (arma != null) {
    System.out.println("[ARMA] " + guerriero.getNomePersonaggio()
        + " ha equipaggiato: " + arma.getNome()
        + " (dannoBonus=" + arma.getDannoBonus()
        + ", tipo=" + arma.getTipoArma() + ")");
} else {
    System.out.println("[ARMA] " + guerriero.getNomePersonaggio() + " non ha arma equipaggiata.");
}

    int tiro = java.util.concurrent.ThreadLocalRandom.current().nextInt(1, 21);
   int bonusAttacco =java.util.concurrent.ThreadLocalRandom.current().nextInt(20, 150);
    int totale = tiro + bonusAttacco;   
    System.out.println(guerriero.getNomePersonaggio() + " tiro attacco: " + tiro + " + bonus " + bonusAttacco + " = " + totale + " (CA mostro: " + difesaMostro + ")");

    if (tiro == 1) {
        System.out.println("Tiro 1: fallimento critico!");
        return 0;
    }

    boolean critico = (tiro == 20);

    if (totale < difesaMostro && !critico) {
        System.out.println(guerriero.getNomePersonaggio() + " manca il bersaglio.");
        return 0;
    }

    int dannoBase = attacco + livello * 2;
    if (guerriero.getPuntiVita() <= 10) {
        dannoBase += 5;
       System.out.println(ANSI.BRIGHT_YELLOW + guerriero.getNomePersonaggio()
    + " entra in " + ANSI.BRIGHT_RED + "FURIA" + ANSI.RESET + ANSI.BRIGHT_YELLOW + " e colpisce più forte!" + ANSI.RESET);
    }

    int dannoNetto = Math.max(1, dannoBase - difesaMostro);
    if (critico) {
        dannoNetto *= 2;
       System.out.println(ANSI.BRIGHT_RED + ANSI.BOLD + "Colpo critico! Danno raddoppiato." + ANSI.RESET);

    }

    mostro.setPuntiVitaMostro(mostro.getPuntiVitaMostro() - dannoNetto);

    System.out.println(guerriero.getNomePersonaggio()
            + " sferra un potente colpo contro " + mostro.getNomeMostro()
            + " infliggendo " + dannoNetto + " danni (HP mostro rimasti: " + mostro.getPuntiVitaMostro() + ")");

    if (mostro.getPuntiVitaMostro() <= 0) {
        System.out.println(ANSI.BRIGHT_GREEN + mostro.getNomeMostro() + " è stato sconfitto dal Guerriero!" + ANSI.RESET);
        try {
            guerriero.setEsperienza(guerriero.getEsperienza() + 12);
            if (guerriero.getEsperienza() >= 100) {
                guerriero.setLivello(guerriero.getLivello() + 1);
                guerriero.setEsperienza(0);
                System.out.println(" " + guerriero.getNomePersonaggio() + " è salito al livello " + guerriero.getLivello() + "!");
            }
        } catch (Exception ignored) {
        }
    }
    return dannoNetto;
}

    public Personaggio creaPersonaggio(String nome, Personaggio personaggio) {
Stanza stanza = null;
Zaino zaino = new Zaino();
return new Guerriero("abilità", null, 100, 100,
 0, 2, nome, stanza, false, 50, 300, "normale", 
 0, 0, 0, 
 0, zaino, 0);
}
    
    public void usaAbilitàSpeciale(Personaggio personaggio, String abilitàSpeciale) {
    }

}*/
package service.impl;

import java.util.concurrent.ThreadLocalRandom;

import domain.Arma;
import domain.Combattimento;
import domain.Guerriero;
import domain.Mostro;
import domain.Personaggio;
import domain.Zaino;
import service.PersonaggioService;
import util.ANSI;

public class GuerrieroServiceImpl implements PersonaggioService {

    /* =======================
       COSTANTI DI BILANCIAMENTO
       ======================= */

    private static final int BONUS_ATTACCO_GUERRIERO = 15;
    private static final int BONUS_DANNO_LIVELLO = 2;
    private static final int SOGLIA_FURIA_HP = 10;
    private static final int BONUS_FURIA = 5;

    private static final int BONUS_DIFESA_PROTEZIONE = 8;
    

    private static final int XP_VITTORIA = 12;
    private static final int XP_LIVELLO = 100;

    /* =======================
       ABILITÀ: PROTEGGI COMPAGNO
       ======================= */

    public boolean proteggiCompagno(Guerriero guerriero, Personaggio alleato) {

        if (guerriero == null || alleato == null) return false;

        if (guerriero.getPuntiVita() <= 10) {
            System.out.println("Il guerriero è troppo debole per proteggere qualcuno.");
            return false;
        }

        if (!alleato.prenotaProtezione()) {
        
            System.out.println(alleato.getNomePersonaggio() + " è già protetto.");
            return false;
        }

        

        System.out.println(
                guerriero.getNomePersonaggio() + " protegge " +
                alleato.getNomePersonaggio()
        );

        return true;
    }

    /* =======================
       ATTACCO (INTERFACCIA)
       ======================= */

    @Override
    public int attacca(Personaggio personaggio, Mostro mostro, Combattimento combattimento) {

        Guerriero guerriero = validaAttaccante(personaggio, mostro);
        if (guerriero == null) return 0;

        return attaccoFisico(guerriero, mostro);
    }

    /* =======================
       VALIDAZIONI
       ======================= */

    private Guerriero validaAttaccante(Personaggio p, Mostro m) {

        if (!(p instanceof Guerriero guerriero)) {
            System.out.println("Solo un Guerriero può usare questo attacco.");
            return null;
        }

        if (m == null) {
            System.out.println("Nessun bersaglio valido.");
            return null;
        }

        if (guerriero.getPosizioneCorrente() == null ||
            m.getPosizioneCorrenteMostro() == null) {
            System.out.println("Errore di posizione.");
            return null;
        }

        if (!guerriero.getPosizioneCorrente()
                .equals(m.getPosizioneCorrenteMostro())) {
            System.out.println(" Devi essere nella stessa stanza del mostro.");
            return null;
        }

        return guerriero;
    }

    /* =======================
       ATTACCO FISICO
       ======================= */

    public int attaccoFisico(Guerriero guerriero, Mostro mostro) {

        ThreadLocalRandom random = ThreadLocalRandom.current();

        int tiro = random.nextInt(1, 21); // d20
        int bonusAttacco = guerriero.getAttacco() + guerriero.getLivello();
        int totale = tiro + bonusAttacco;

        int difesaMostro = mostro.getDifesaMostro();

        System.out.println(
                guerriero.getNomePersonaggio() +
                " tiro: " + tiro + " + bonus " + bonusAttacco +
                " = " + totale + " (CA mostro: " + difesaMostro + ")"
        );

        if (tiro == 1) {
            System.out.println(" Fallimento critico!");
            return 0;
        }

        boolean critico = tiro == 20;

        if (totale < difesaMostro && !critico) {
            System.out.println("L'attacco manca il bersaglio.");
            return 0;
        }

        /* ===== CALCOLO DANNO ===== */

        int dannoBase = guerriero.getAttacco()
                + BONUS_ATTACCO_GUERRIERO
                + guerriero.getLivello() * BONUS_DANNO_LIVELLO;

        Arma arma = guerriero.getArmaEquippaggiata();
        if (arma != null) {
            dannoBase += arma.getDannoBonus();
            System.out.println("[ARMA] " + arma.getNome()
                    + " (+" + arma.getDannoBonus() + " danni)");
        }

        if (guerriero.getPuntiVita() <= SOGLIA_FURIA_HP) {
            dannoBase += BONUS_FURIA;
            System.out.println(
                    ANSI.BRIGHT_YELLOW + guerriero.getNomePersonaggio() +
                    " entra in " + ANSI.BRIGHT_RED + "FURIA" +
                    ANSI.RESET
            );
        }

        int dannoNetto = Math.max(1, dannoBase - difesaMostro);

        if (critico) {
            dannoNetto *= 2;
            System.out.println(
                    ANSI.BRIGHT_RED + ANSI.BOLD +
                    "COLPO CRITICO! Danno raddoppiato!" +
                    ANSI.RESET
            );
        }

        /* ===== APPLICA DANNO ===== */

        mostro.setPuntiVitaMostro(
                mostro.getPuntiVitaMostro() - dannoNetto
        );

        System.out.println(
                guerriero.getNomePersonaggio() +
                " infligge " + dannoNetto +
                " danni a " + mostro.getNomeMostro() +
                " (HP rimasti: " + mostro.getPuntiVitaMostro() + ")"
        );

        /* ===== MORTE MOSTRO ===== */

        if (mostro.getPuntiVitaMostro() <= 0) {
            gestisciVittoria(guerriero, mostro);
        }

        return dannoNetto;
    }

    /* =======================
       GESTIONE VITTORIA
       ======================= */

    private void gestisciVittoria(Guerriero guerriero, Mostro mostro) {

        System.out.println(
                ANSI.BRIGHT_GREEN +
                mostro.getNomeMostro() + " è stato sconfitto!" +
                ANSI.RESET
        );

        guerriero.setEsperienza(
                guerriero.getEsperienza() + XP_VITTORIA
        );

        if (guerriero.getEsperienza() >= XP_LIVELLO) {
            guerriero.setLivello(guerriero.getLivello() + 1);
            guerriero.setEsperienza(0);

            System.out.println(
                    guerriero.getNomePersonaggio() +
                    " sale al livello " + guerriero.getLivello() + "!"
            );
        }
    }

    /* =======================
       CREAZIONE PERSONAGGIO
       ======================= */

    public Personaggio creaPersonaggio(String nome, Personaggio personaggio) {

        return new Guerriero(
                "abilità",
                null,
                100,
                100,
                0,
                2,
                nome,
                null,
                false,
                50,
                30,
                "normale",
                0,
                0,
                0,
                0,
                new Zaino(),
                0
        );
    }

    @Override
    public void usaAbilitàSpeciale(Personaggio personaggio, String abilitàSpeciale) {
        // futura estensione (Strategia / Command)
    }
}

