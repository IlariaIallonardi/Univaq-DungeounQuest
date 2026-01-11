package service.impl;

import java.util.concurrent.ThreadLocalRandom;

import domain.Combattimento;
import domain.Guerriero;
import domain.Mago;
import domain.Mago.TipoMagiaSacra;
import domain.Mostro;
import domain.Personaggio;
import domain.Stanza;
import domain.Zaino;
import service.PersonaggioService;

import util.ANSI;

public class MagoServiceImpl implements PersonaggioService {

    private static final int bonusDannoLivello = 4;

    /**
     * Metodo per utilizzare la magia del mago
     *
     * @param mago Il mago che usa la magia
     * @param bersaglio Il personaggio bersaglio della magia
     * @param tipo Il tipo di magia da utilizzare
     * @return true se la magia è stata lanciata con successo
     */
    public int lanciaIncantesimo(Mago mago, Mostro mostro, Mago.TipoMagiaSacra tipo) {

        ThreadLocalRandom random = ThreadLocalRandom.current();

        int costoMana = tipo.getCostoMana();

        // Se non hai mana, non puoi lanciare
        if (mago.getPuntiMana() < costoMana) {
            System.out.println(mago.getNomePersonaggio() + " non ha abbastanza mana per " + tipo + "!");
            return 0;
        }

        // ===== TIRO PER COLPIRE (come il guerriero) =====
        int tiro = random.nextInt(1, 21); // d20

        int bonusAttacco = mago.getAttacco() + (mago.getLivello() / 2);
        int totale = tiro + bonusAttacco;

        int CAmostro = mostro.getDifesaMostro(); // usata come CA

        System.out.println(
                mago.getNomePersonaggio()
                + " lancia " + tipo
                + " tiro: " + tiro + " + bonus " + bonusAttacco
                + " = " + totale + " (CA mostro: " + CAmostro + ")"
        );

        // 1 naturale: fallimento (ma consumo mana lo stesso? scelta tua)
        // Qui: consumo mana anche se fallisce, perché l'incantesimo è stato tentato.
        if (tiro == 1) {
            System.out.println("Fallimento magico!");
            mago.setPuntiMana(Math.max(0, mago.getPuntiMana() - costoMana));
            return 0;
        }

        boolean critico = (tiro == 20);

        // Se non critico e non supero la CA, manca
        if (!critico && totale < CAmostro) {
            System.out.println("L'incantesimo manca il bersaglio.");
            mago.setPuntiMana(Math.max(0, mago.getPuntiMana() - costoMana));
            return 0;
        }

        // ===== DANNO (dadi + bonus fissi, critico raddoppia SOLO i dadi) =====
        // Scegli il dado base: esempio 1d10
        int dadoDanno = random.nextInt(1, 11); // 1..10
        int bonusAttaccoMago = 15;

        // Bonus fissi (stile guerriero): qui puoi usare costanti analoghe alle tue
        // esempio: bonusAttaccoMago + livello * bonusDannoLivello
        int bonusFissi = bonusAttaccoMago + mago.getLivello() * bonusDannoLivello;

        int dadiTotali = dadoDanno;

        if (critico) {
            int dadoExtra = random.nextInt(1, 11);
            dadiTotali += dadoExtra;
            System.out.println(
                    ANSI.BRIGHT_RED + ANSI.BOLD
                    + "CRITICO MAGICO! Dadi danno raddoppiati!"
                    + ANSI.RESET
            );
        }

        int dannoNetto = Math.max(1, dadiTotali + bonusFissi);

        // ===== EFFETTI SPECIALI PER TIPO MAGIA (facoltativi) =====
        switch (tipo) {
            case RUBAVITA -> {
                int cura = Math.max(1, dannoNetto / 2);
                mago.setPuntiVita(mago.getPuntiVita() + cura);
                System.out.println(mago.getNomePersonaggio() + " assorbe " + cura + " PV!");
            }
            case AMMALIAMENTO -> {
                // esempio semplice: riduci attacco o difesa del mostro (se hai stat adatte)
                // mostro.setDifesaMostro(Math.max(0, mostro.getDifesaMostro() - 1));
                System.out.println("Il mostro è ammaliato! (effetto da definire)");
            }
            case MALATTIA -> {
                // esempio: danno extra fisso o DOT futuro
                int extra = 2 + (mago.getLivello() / 2);
                dannoNetto += extra;
                System.out.println("Malattia! Danno extra: +" + extra);
            }
        }

        // ===== CONSUMO MANA =====
        mago.setPuntiMana(Math.max(0, mago.getPuntiMana() - costoMana));

        // ===== APPLICA DANNO =====
        mostro.setPuntiVitaMostro(mostro.getPuntiVitaMostro() - dannoNetto);

        System.out.println(
                mago.getNomePersonaggio()
                + " infligge " + dannoNetto
                + " danni magici a " + mostro.getNomeMostro()
                + " (HP rimasti: " + mostro.getPuntiVitaMostro()
                + ", Mana rimasto: " + mago.getPuntiMana() + ")"
        );

        return dannoNetto;
    }

    @Override
    public int attacca(Personaggio personaggio, Mostro mostro, Combattimento combattimento) {
        if (!(personaggio instanceof Mago mago)) {
            System.out.println("Solo un Mago può usare questo attacco.");
            return 0;
        }

        if (mostro == null) {
            System.out.println("Nessun bersaglio valido.");
            return 0;
        }

        if (mago.getPosizioneCorrente() == null
                || mostro.getPosizioneCorrenteMostro() == null) {
            System.out.println("Errore di posizione.");
            return 0;
        }

        if (!mago.getPosizioneCorrente()
                .equals(mostro.getPosizioneCorrenteMostro())) {
            System.out.println(" Devi essere nella stessa stanza del mostro.");
            return 0;
        }

        if (mago == null) {
            return 0;
        }

        TipoMagiaSacra magiaScelta = mago.getMagiaSelezionata();

        // ✅ QUI: protezione se non è stata scelta alcuna magia
        if (magiaScelta == null) {
            System.out.println("Nessuna magia selezionata.");
            return 0;
        }

        // ✅ QUI: chiami il nuovo metodo con il terzo parametro
        return lanciaIncantesimo(mago, mostro, magiaScelta);
    }

    @Override
    public Personaggio creaPersonaggio(String nome, Personaggio personaggio) {
        Stanza stanza = null;
        Zaino zaino = new Zaino();
        return new Mago("abilità", null, 200, 300, 0, 2, nome, stanza, false, 100, 300, "normale", 0, 0, 0, 0, zaino, 0, null);
    }

    @Override
    public void usaAbilitàSpeciale(Personaggio personaggio, String abilitàSpeciale) {
        // futura estensione (Strategia / Command)
    }

}
