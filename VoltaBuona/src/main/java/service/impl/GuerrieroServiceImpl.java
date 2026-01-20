package service.impl;

import java.util.concurrent.ThreadLocalRandom;

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

    private static final int bonusAttaccoGuerriero = 15;
    private static final int bonusDannoLivello = 4;
    private static final int sogliaFuria = 10;
    private static final int bonusFuria = 5;
    private static final int xpVittoria = 25;
    private static final int xpLivello = 100;

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
         if (!(personaggio instanceof Guerriero guerriero)) {
            System.out.println("Solo un Guerriero può usare questo attacco.");
            return 0;
        }

        if (mostro == null) {
            System.out.println("Nessun bersaglio valido.");
            return 0;
        }

        if (guerriero.getPosizioneCorrente() == null ||
            mostro.getPosizioneCorrente() == null) {
            System.out.println("Errore di posizione.");
            return 0;
        }

        if (!guerriero.getPosizioneCorrente()
                .equals(mostro.getPosizioneCorrente())) {
            System.out.println(" Devi essere nella stessa stanza del mostro.");
            return 0;
        }

        if (guerriero == null) return 0;

        return attaccoFisicoGuerriero(guerriero, mostro);
    }

   

    /* =======================
       ATTACCO FISICO
       ======================= */

    public int attaccoFisicoGuerriero(Guerriero guerriero, Mostro mostro) {

    ThreadLocalRandom random = ThreadLocalRandom.current();

    int tiro = random.nextInt(1, 21); // d20

    // bonus
    int bonusAttacco = guerriero.getAttacco() + (guerriero.getLivello() / 2);
    int totale = tiro + bonusAttacco;

    int difesaMostroCombattimento = mostro.getDifesaMostro(); // qui la usi come Classe Armatura

    System.out.println(
        guerriero.getNomePersonaggio() +
        " tiro: " + tiro + " + bonus " + bonusAttacco +
        " = " + totale + " (CA mostro: " + difesaMostroCombattimento + ")"
    );

    if (tiro == 1) {
        System.out.println("Fallimento!");
        return 0;
    }

    boolean critico = (tiro == 20);

    if (!critico && totale < difesaMostroCombattimento) {
        System.out.println("L'attacco manca il bersaglio.");
        return 0;
    }



    // Parte a dadi: es. 1d8 (puoi cambiarla in base all'arma)
    int dadoDanno = random.nextInt(1, 9); // 1..8

    // Bonus fissi: i tuoi bonus (restano uguali)
    int bonusFissi = bonusAttaccoGuerriero + guerriero.getLivello() * bonusDannoLivello;

    /* ?????
    Arma arma = guerriero.getArmaEquippaggiata();
    if (arma != null) {
        bonusFissi += arma.getDannoBonus();
        System.out.println("[ARMA] " + arma.getNome() + " (+" + arma.getDannoBonus() + " danni)");
    }*/

    if (guerriero.getPuntiVita() <= sogliaFuria) {
        bonusFissi += bonusFuria;
        System.out.println(
            ANSI.BRIGHT_YELLOW + guerriero.getNomePersonaggio() +
            " entra in " + ANSI.BRIGHT_RED + "FURIA" +
            ANSI.RESET
        );
    }

    int dadiTotali = dadoDanno;

    // Critico: raddoppia SOLO i dadi
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

    /* ===== APPLICA DANNO ===== */

    mostro.setPuntiVitaMostro(mostro.getPuntiVitaMostro() - dannoNetto);

    System.out.println(
        guerriero.getNomePersonaggio() +
        " infligge " + dannoNetto +
        " danni a " + mostro.getNomeMostro() +
        " (HP rimasti: " + mostro.getPuntiVitaMostro() + ")"
    );


    return dannoNetto;
}
    

    /* =======================
       CREAZIONE PERSONAGGIO
       ======================= */

    public Personaggio creaPersonaggio(String nome, Personaggio personaggio) {

        return new Guerriero(
                "abilità",
                null,
                10,
                0,
                0,
                0,
                nome,
                null,
                false,
                50,
                20,
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

