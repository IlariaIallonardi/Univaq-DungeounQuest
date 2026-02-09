package service.impl;

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

    private static final int BONUS_ATTACCO_MAGO = 15;
    private RandomSingleton randomGenerale = RandomSingleton.getInstance();

    /**
     * Metodo per utilizzare la magia del mago
     *
     * @param mago Il mago che usa la magia
     * @param bersaglio Il personaggio bersaglio della magia
     * @param tipo Il tipo di magia da utilizzare
     * @return true se la magia è stata lanciata con successo
     */
    public int lanciaIncantesimo(Mago mago, Mostro mostro, Mago.TipoMagiaSacra tipo) {

        

        int costoMana = tipo.getCostoMana();

        // Controllo manna prima di lanciare la magia.
        if (mago.getPuntiMana() < costoMana) {
            System.out.println(mago.getNomePersonaggio() + " non ha abbastanza mana per " + tipo + "!");
            return 0;
        }

        int tiro = randomGenerale.prossimoNumero(1, 20); // dado di 20 facce

        int bonusAttacco = mago.getAttacco() + (mago.getLivello() / 2);
        int totale = tiro + bonusAttacco;

        int difesaMostro = mostro.getDifesaMostro(); // usata come difficoltà 

        System.out.println(mago.getNomePersonaggio() + " lancia " + tipo + " tiro: " + tiro + " + bonus " + bonusAttacco + " = " + totale + " (difesa  mostro: " + difesaMostro + ")");

        if (tiro == 1) {
            System.out.println("Fallimento magico!");
            return 0;
        }

        boolean lancioMigliore = (tiro == 20);

        if (lancioMigliore || totale >= difesaMostro) {

            int dadoDanno = randomGenerale.prossimoNumero(1, 8); // dado da 8 facce

            if (lancioMigliore) {
                int dadoExtra = randomGenerale.prossimoNumero(1, 8);
                dadoDanno += dadoExtra;
                System.out.println(ANSI.BRIGHT_RED + ANSI.BOLD + "CRITICO MAGICO! Dadi danno raddoppiati!" + ANSI.RESET
                );
            }

            int dannoNetto = dadoDanno + BONUS_ATTACCO_MAGO;

            TipoMagiaSacra magiaScelta = mago.getMagiaSelezionata();

            switch (magiaScelta) {
                case RUBAVITA -> {
                    int cura = 10;
                    mostro.setPuntiVitaMostro(mostro.getPuntiVitaMostro() - cura);
                    mago.setPuntiVita(mago.getPuntiVita() + cura);
                    System.out.println(mago.getNomePersonaggio() + " assorbe " + cura + " PV!");
                }
                case AMMALIAMENTO -> {

                    mostro.setDifesaMostro(Math.max(0, mostro.getDifesaMostro() - 3));
                    System.out.println("Il mostro è ammaliato!");
                }
                case MALATTIA -> {
                    int extra = 4 + (mago.getLivello() / 2);
                    dannoNetto += extra;
                    System.out.println("Malattia! Danno extra: +" + extra);
                }
            }
            mago.setPuntiMana((mago.getPuntiMana() - costoMana));
            mostro.setPuntiVitaMostro(mostro.getPuntiVitaMostro() - dannoNetto);

            System.out.println(mago.getNomePersonaggio() + " infligge " + dannoNetto + " danni magici a " + mostro.getNomeMostro() + "danno base" + dadoDanno + " + bonus " + BONUS_ATTACCO_MAGO + " + danno magia " + "" + " (HP rimasti: " + mostro.getPuntiVitaMostro() + ", Mana rimasto: " + mago.getPuntiMana() + ")");

            return dannoNetto;
        }
        return 1;
    }

    /**
     * Attacca il mostro con la magia selezionata dal mago.
     *
     * @param personaggio
     * @param mostro
     * @param combattimento
     * @return il danno inflitto al mostro
     */
    @Override
    public int attacca(Personaggio personaggio, Mostro mostro, Combattimento combattimento) {

        Mago mago = (Mago) personaggio;

        if (mago.getPosizioneCorrente().equals(mostro.getPosizioneCorrente())) {

            TipoMagiaSacra magiaScelta = mago.getMagiaSelezionata();

            if (magiaScelta == null) {
                System.out.println("Nessuna magia selezionata.");
                return 0;
            }
            return lanciaIncantesimo(mago, mostro, magiaScelta);
        }
        return 0;
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
                60,
                100,
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
