package service.impl;

import java.util.concurrent.ThreadLocalRandom;

import domain.Combattimento;
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
    private static final int bonusAttaccoMago = 15;

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

        int difesaMostro = mostro.getDifesaMostro(); // usata come CA

        System.out.println(
                mago.getNomePersonaggio()
                + " lancia " + tipo
                + " tiro: " + tiro + " + bonus " + bonusAttacco
                + " = " + totale + " (difesa  mostro: " + difesaMostro + ")"
        );

        // 1 naturale: fallimento (ma consumo mana lo stesso? scelta tua)
        // Qui: consumo mana anche se fallisce, perché l'incantesimo è stato tentato.
        if (tiro == 1) {
            System.out.println("Fallimento magico!");
            mago.setPuntiMana(Math.max(0, mago.getPuntiMana() - costoMana));
            return 0;
        }

        boolean critico = (tiro == 20);

        // Se non critico e non supero la difesa del mostro, manca
        if (!critico && totale < difesaMostro) {
            System.out.println("L'incantesimo manca il bersaglio.");
            mago.setPuntiMana(Math.max(0, mago.getPuntiMana() - costoMana));
            return 0;
        }

        // ===== DANNO (dadi + bonus fissi, critico raddoppia SOLO i dadi) =====
        // Scegli il dado base: esempio 1d10
        int dadoDanno = random.nextInt(1, 11); // 1..10

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

        int dannoPerMagia = calcolaDannoPerMagia(tipo, mago, mostro);
        int dannoNetto = Math.max(1, dadiTotali + bonusFissi + dannoPerMagia);

        // ===== EFFETTI SPECIALI PER TIPO MAGIA =====
        switch (tipo) {
            case RUBAVITA -> {
                int cura = 10;
                mostro.setPuntiVitaMostro(mostro.getPuntiVitaMostro() - cura);
                mago.setPuntiVita(mago.getPuntiVita() + cura);
                System.out.println(mago.getNomePersonaggio() + " assorbe " + cura + " PV!");
            }
            case AMMALIAMENTO -> {
                // esempio semplice: riduci attacco o difesa del mostro (se hai stat adatte)
                mostro.setDifesaMostro(Math.max(0, mostro.getDifesaMostro() - 3));
                System.out.println("Il mostro è ammaliato!");
            }
            case MALATTIA -> {
                // esempio: danno extra fisso o DOT futuro
                int extra = 4 + (mago.getLivello() / 2);
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
                + "danno base" + dadiTotali + " + bonus " + bonusFissi + " + danno magia " + dannoPerMagia
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
                || mostro.getPosizioneCorrente() == null) {
            System.out.println("Errore di posizione.");
            return 0;
        }

        if (!mago.getPosizioneCorrente()
                .equals(mostro.getPosizioneCorrente())) {
            System.out.println(" Devi essere nella stessa stanza del mostro.");
            return 0;
        }

        if (mago == null) {
            return 0;
        }

        TipoMagiaSacra magiaScelta = mago.getMagiaSelezionata();

        // QUI: protezione se non è stata scelta alcuna magia
        if (magiaScelta == null) {
            System.out.println("Nessuna magia selezionata.");
            return 0;
        }

        //  QUI: chiami il nuovo metodo con il terzo parametro
        return lanciaIncantesimo(mago, mostro, magiaScelta);
    }

    ///probabilmente utile per i bot
   /*  public TipoMagiaSacra scegliMagia(Mago mago ,Mostro mostro){
        if(mago.getPuntiMana() >= TipoMagiaSacra.MALATTIA.getCostoMana()){
            return TipoMagiaSacra.MALATTIA;
        } else if (mago.getPuntiMana() >= TipoMagiaSacra.RUBAVITA.getCostoMana()){
            return TipoMagiaSacra.RUBAVITA;
        } else if (mago.getPuntiMana() >= TipoMagiaSacra.AMMALIAMENTO.getCostoMana()){
            return TipoMagiaSacra.AMMALIAMENTO;
        }
        //logica per scegliere la magia
        return TipoMagiaSacra.RUBAVITA; //esempio
    }*/

     private int calcolaDannoPerMagia(TipoMagiaSacra tipo, Mago mago, Mostro mostro) {
        return switch (tipo) {
            case MALATTIA ->
                4;
            case RUBAVITA ->
                5;
            case AMMALIAMENTO ->
                6;
        };
    }

    @Override
    public Personaggio creaPersonaggio(String nome, Personaggio personaggio) {
        Stanza stanza = null;
        Zaino zaino = new Zaino();
        return new Mago("abilità", null,
                15,
                90,
                0,
                2,
                nome,
                stanza, 
                false, 
                50, 
                20, 
                "normale", 
                0, 
                0,
                 0,
                  0,
                   zaino,
                    0, 
                    null);
    }

    @Override
    public void usaAbilitàSpeciale(Personaggio personaggio, String abilitàSpeciale) {
        // futura estensione (Strategia / Command)
    }

}
