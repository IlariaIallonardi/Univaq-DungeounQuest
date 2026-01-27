package service.impl;

import java.util.concurrent.ThreadLocalRandom;

import domain.Combattimento;
import domain.Mostro;
import domain.Paladino;
import domain.Personaggio;
import domain.Stanza;
import domain.Zaino;
import service.PersonaggioService;
import util.ANSI;

public class PaladinoServiceImpl implements PersonaggioService {

    private static final int bonusAttaccoFisico = 6;
    private static final int bonusAttaccoMagico = 3;
    private static final int bonusAttaccoPaladino = 7;
    private static final int bonusDannoLivello = 2;

    /**
     * Metodo per proteggere un altro giocatore
     *
     * @param paladino Il paladino che usa la protezione
     * @param bersaglio Il personaggio da proteggere
     * @return true se la protezione è stata applicata con successo
     */
    public boolean proteggiCompagno(Paladino paladino, Personaggio alleato) {

        if (paladino == null || alleato == null) {
            return false;
        }

        if (paladino.getPuntiVita() <= 10) {
            System.out.println("Il paladino è troppo debole per proteggere qualcuno.");
            return false;
        }

        if (!alleato.prenotaProtezione()) {

            System.out.println(alleato.getNomePersonaggio() + " è già protetto.");
            return false;
        }

        System.out.println(
                paladino.getNomePersonaggio() + " protegge "
                + alleato.getNomePersonaggio()
        );

        return true;
    }

    @Override
    public int attacca(Personaggio personaggio, Mostro mostro, Combattimento combattimento) {

        if (!(personaggio instanceof Paladino paladino)) {
            System.out.println("Solo un Paladino può attaccare.");
            return 0;
        }

        if (mostro == null) {
            System.out.println("Nessun bersaglio valido.");
            return 0;
        }

        if (paladino.getPosizioneCorrente() == null
                || mostro.getPosizioneCorrente() == null) {
            System.out.println("Errore di posizione.");
            return 0;
        }

        if (!paladino.getPosizioneCorrente()
                .equals(mostro.getPosizioneCorrente())) {
            System.out.println("Devi essere nella stessa stanza del mostro.");
            return 0;
        }

        // IF SEMPLICE: decide lo stile
        if (paladino.getMagiaSelezionata() != null) {
            return colpoSacroPaladino(paladino, mostro, paladino.getMagiaSelezionata());
        }

        return attaccoFisicoPaladino(paladino, mostro);
    }

    public int colpoSacroPaladino(Paladino paladino, Mostro mostro, Paladino.TipoMagiaSacra tipo) {

        ThreadLocalRandom random = ThreadLocalRandom.current();

        int costoMana = tipo.getCostoMana();

        if (paladino.getPuntiMana() < costoMana) {
            System.out.println(paladino.getNomePersonaggio() + " non ha abbastanza mana per " + tipo + "!");
            return 0;
        }

        int tiro = random.nextInt(1, 21);

        int bonusAttacco = paladino.getAttacco() + (paladino.getLivello() / 2);
        int totale = tiro + bonusAttacco;

        int difesaMostro = mostro.getDifesaMostro();

        System.out.println(
                paladino.getNomePersonaggio()
                + " (sacro) lancia " + tipo
                + " tiro: " + tiro + " + bonus " + bonusAttacco
                + " = " + totale + " (difesa mostro: " + difesaMostro + ")"
        );

        // Consuma mana comunque, anche se fallisce (coerente col tuo mago)
        paladino.setPuntiMana(Math.max(0, paladino.getPuntiMana() - costoMana));

        if (tiro == 1) {
            System.out.println("Fallimento magico!");
            return 0;
        }

        boolean critico = (tiro == 20);

        if (!critico && totale < difesaMostro) {
            System.out.println("L'incantesimo manca il bersaglio.");
            return 0;
        }

        // Paladino: 1d6 o 1d8 sacro (io farei 1d8 per feeling, ma bilanciamento tuo)
        int dadoDanno = random.nextInt(1, 9);

        int bonusFissi = bonusAttaccoMagico + paladino.getLivello() * bonusDannoLivello;

        int dadiTotali = dadoDanno;

        if (critico) {
            int dadoExtra = random.nextInt(1, 9);
            dadiTotali += dadoExtra;
            System.out.println(
                    ANSI.BRIGHT_RED + ANSI.BOLD
                    + "CRITICO SACRO! Dadi danno raddoppiati!"
                    + ANSI.RESET
            );
        }

        int dannoNetto = Math.max(1, dadiTotali + bonusFissi);

        // Effetti per tipo 
        switch (tipo) {
            case RUBAVITA -> {
                int cura = Math.max(1, dannoNetto / 3); // paladino cura meno del mago
                paladino.setPuntiVita(paladino.getPuntiVita() + cura);
                System.out.println(paladino.getNomePersonaggio() + " recupera " + cura + " PV (luce sacra).");
            }
            case AMMALIAMENTO -> {
                mostro.setDifesaMostro(Math.max(0, mostro.getDifesaMostro() - 2));
                System.out.println("Il mostro è indebolito dalla luce!");
            }
            case MALATTIA -> {
                int extra = 2 + (paladino.getLivello() / 2);
                dannoNetto += extra;
                System.out.println("Piaga sacra! Danno extra: +" + extra);
            }
        }

        mostro.setPuntiVitaMostro(mostro.getPuntiVitaMostro() - dannoNetto);

        System.out.println(
                paladino.getNomePersonaggio()
                + " infligge " + dannoNetto
                + " danni sacri a " + mostro.getNomeMostro()
                + " (HP rimasti: " + mostro.getPuntiVitaMostro()
                + ", Mana rimasto: " + paladino.getPuntiMana() + ")"
        );

        return dannoNetto;
    }

    
        public int attaccoFisicoPaladino(Paladino paladino, Mostro mostro) {

        ThreadLocalRandom random = ThreadLocalRandom.current();

        int tiro = random.nextInt(1, 21); // d20

        // bonus
        int bonusAttacco = paladino.getAttacco() + (paladino.getLivello() / 2);
        int totale = tiro + bonusAttacco;

        int difesaMostroCombattimento = mostro.getDifesaMostro(); // qui la usi come Classe Armatura

        System.out.println(
                paladino.getNomePersonaggio()
                + " tiro: " + tiro + " + bonus " + bonusAttacco
                + " = " + totale + " (difesa mostro: " + difesaMostroCombattimento + ")"
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
        int bonusFissi = bonusAttaccoFisico + paladino.getLivello() * bonusDannoLivello;

  
        int dadiTotali = dadoDanno;

        // Critico: raddoppia SOLO i dadi
        if (critico) {
            int dadoExtra = random.nextInt(1, 9);
            dadiTotali += dadoExtra;
            System.out.println(
                    ANSI.BRIGHT_RED + ANSI.BOLD
                    + "COLPO CRITICO! Dadi danno raddoppiati!"
                    + ANSI.RESET
            );
        }

        int dannoNetto = Math.max(1, dadiTotali + bonusFissi);

        /* ===== APPLICA DANNO ===== */
        mostro.setPuntiVitaMostro(mostro.getPuntiVitaMostro() - dannoNetto);

        System.out.println(
                paladino.getNomePersonaggio()
                + " infligge " + dannoNetto
                + " danni a " + mostro.getNomeMostro()
                + " (HP rimasti: " + mostro.getPuntiVitaMostro() + ")"
        );

        if (mostro.getPuntiVitaMostro() <= 0) {
            return 0;
        }

        return dannoNetto;
    }

    @Override
    public Personaggio creaPersonaggio(String nome, Personaggio personaggio) {
        Stanza stanza = null;
        Zaino zaino = new Zaino();
        return new Paladino("abilità", null, 15, 300, 0, 2,
         nome, stanza, false, 100, 20, "normale", 0, 0, 0, 0, zaino, 0, null);
    }

    public void usaAbilitàSpeciale(Personaggio personaggio, String abilitàSpeciale) {
    }

}
