package service.impl;

import domain.Combattimento;
import domain.Guerriero;
import domain.Mostro;
import domain.Personaggio;
import domain.Zaino;
import exception.DungeonException;
import service.PersonaggioService;
import util.ANSI;

public class GuerrieroServiceImpl implements PersonaggioService {

    private static final int BONUS_ATTACCO_GUERRIERO = 15;
    private static final int SOGLIA_FURIA = 10;
    private static final int BONUS_FURIA = 5;
    private RandomSingleton randomGenerale = RandomSingleton.getInstance();

    /**
     * Il guerriero può proteggere un suo compagno.
     *
     * @param guerriero
     * @param alleato
     */
    public boolean proteggiCompagno(Guerriero guerriero, Personaggio alleato) {

        if (guerriero.getPuntiVita() <= 10) {
            System.out.println("Il guerriero è troppo debole per proteggere qualcuno.");
            return false;
        }

        if (alleato.prenotaProtezione()) {
            alleato.subisciDannoPuntiDifesa(0);
            alleato.prenotaProtezione();
            System.out.println(
                    guerriero.getNomePersonaggio() + " protegge "
                    + alleato.getNomePersonaggio());

            return false;

        }

        return true;
    }

    /**
     * @param personaggio
     * @param mostro
     * @param combattimento
     */
    @Override
    public int attacca(Personaggio personaggio, Mostro mostro, Combattimento combattimento) throws DungeonException {

        Guerriero guerriero = (Guerriero) personaggio;
        if (guerriero.getPosizioneCorrente() == null
                || mostro.getPosizioneCorrente() == null) {
            throw new DungeonException("Il guerriero o il mostro non hanno una posizione valida.");
            
        }

        if (guerriero.getPosizioneCorrente().equals(mostro.getPosizioneCorrente())) {
            return attaccoFisicoGuerriero(guerriero, mostro);
        } else {
            System.out.println("Il mostro non è nella stessa stanza del guerriero.");
            return 0;
        }
    }

    /**
     *
     * @param guerriero
     * @param mostro
     * @return
     */
    public int attaccoFisicoGuerriero(Guerriero guerriero, Mostro mostro) {

        int tiro = randomGenerale.prossimoNumero(1, 20); // dado da 20 facce 

        int bonusAttacco = guerriero.getAttacco() + (guerriero.getLivello() / 2);
        int totale = tiro + bonusAttacco;

        int difesaMostroCombattimento = mostro.getDifesaMostro(); // usata come difficoltà per andare a segno

        System.out.println(guerriero.getNomePersonaggio() + " tiro: " + tiro + " + bonus " + bonusAttacco + " = " + totale + " (difesa mostro: " + difesaMostroCombattimento + ")");

        if (tiro == 1) {
            System.out.println("Fallimento!");
            return 0;
        }

        boolean lancioMigliore = (tiro == 20);

        if (lancioMigliore || totale >= difesaMostroCombattimento) {

            int dadoDanno = randomGenerale.prossimoNumero(1, 8); // dado da 8 facce

            int bonusFissi = BONUS_ATTACCO_GUERRIERO;

            if (guerriero.getPuntiVita() <= SOGLIA_FURIA) {
                bonusFissi += BONUS_FURIA;
                System.out.println(ANSI.BRIGHT_YELLOW + guerriero.getNomePersonaggio() + " entra in " + ANSI.BRIGHT_RED + "FURIA" + ANSI.RESET);
            }

            //se il lancio è 20 (il massimo), aggiungiamo dei danni in più.
            if (lancioMigliore) {
                int dadoExtra = randomGenerale.prossimoNumero(1, 8);
                dadoDanno += dadoExtra;
                System.out.println(ANSI.BRIGHT_RED + ANSI.BOLD + "COLPO CRITICO! Dadi  raddoppiati!" + ANSI.RESET);
            }

            int dannoNetto = dadoDanno + bonusFissi;

            mostro.setPuntiVitaMostro(mostro.getPuntiVitaMostro() - dannoNetto);

            System.out.println(guerriero.getNomePersonaggio() + " infligge " + dannoNetto + " danni a " + mostro.getNomeMostro() + " (HP rimasti: " + mostro.getPuntiVitaMostro() + ")");

            return dannoNetto;
        }
        return 1;
    }

    public Personaggio creaPersonaggio(String nome, Personaggio personaggio) {

        return new Guerriero(
                null,
                50,
                0,
                0,
                0,
                nome,
                null,
                false,
                50,
                50,
                "normale",
                0,
                0,
                0,
                0,
                new Zaino(),
                150
        );
    }

}
