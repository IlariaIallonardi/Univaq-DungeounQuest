package service.impl;

import domain.Combattimento;
import domain.Mostro;
import domain.Paladino;
import domain.Personaggio;
import domain.Stanza;
import domain.Zaino;
import service.PersonaggioService;
import util.ANSI;


public class PaladinoServiceImpl implements PersonaggioService {

    private static final int BONUS_ATTACCO_FISICO = 6;
    private static final int BONUS_ATTACCO_MAGICO = 3;
    private RandomSingleton randomGenerale = RandomSingleton.getInstance();

    /**
     * Metodo per proteggere un altro giocatore
     *
     * @param paladino Il paladino che usa la protezione
     * @param bersaglio Il personaggio da proteggere
     * @return true se la protezione è stata applicata con successo
     */
    public boolean proteggiCompagno(Paladino paladino, Personaggio alleato) {

        if (paladino.getPuntiVita() <= 10) {
            System.out.println("Il paladino è troppo debole per proteggere qualcuno.");
            return false;
        }

        if (alleato.prenotaProtezione()) {
            alleato.subisciDannoPuntiDifesa(0);
            alleato.prenotaProtezione();
             System.out.println(
                paladino.getNomePersonaggio() + " protegge "
                + alleato.getNomePersonaggio());
            
                   return true;
        
        }
            return false;
        }
    
    

    @Override
    public int attacca(Personaggio personaggio, Mostro mostro, Combattimento combattimento) {

        Paladino paladino = (Paladino) personaggio;

        if (paladino.getPosizioneCorrente().equals(mostro.getPosizioneCorrente())) {
    
        if (paladino.getMagiaSelezionata() != null) {
            return colpoSacroPaladino(paladino, mostro, paladino.getMagiaSelezionata());
        }

        return attaccoFisicoPaladino(paladino, mostro);
    }
            else {
                System.out.println(paladino.getNomePersonaggio() + " è troppo lontano per attaccare " + mostro.getNomeMostro() + "!");
                return 0;
            }
}

    /**
     * Metodo per lanciare una magia  del paladino
     * @param paladino Il paladino che esegue l'attacco
     * @param mostro Il mostro bersaglio
     * @param tipo Il tipo di magia sacra da usare
     * @return il danno totale inflitto al mostro
     */

    public int colpoSacroPaladino(Paladino paladino, Mostro mostro, Paladino.TipoMagiaSacra tipo) {

        

        int costoMana = tipo.getCostoMana();

        if (paladino.getPuntiMana() < costoMana) {
            System.out.println(paladino.getNomePersonaggio() + " non ha abbastanza mana per " + tipo + "!");
            return 0;
        }

        int tiro = randomGenerale.prossimoNumero(1, 21);

        int bonusAttacco = paladino.getAttacco() + (paladino.getLivello() * 2);
        int totale = tiro + bonusAttacco;

        int difesaMostro = mostro.getDifesaMostro();

        System.out.println(  paladino.getNomePersonaggio()   + " (sacro) lancia " + tipo + " tiro: " + tiro + " + bonus " + bonusAttacco + " = " + totale + " (difesa mostro: " + difesaMostro + ")"
        );



        if (tiro == 1) {
            System.out.println("Fallimento magico!");
            return 0;
        }

        boolean lancioMigliore = (tiro == 20);

        if (lancioMigliore || totale >= difesaMostro) {
        

        int dadoDanno = randomGenerale.prossimoNumero(1, 9);//dado da 8 facce

        int bonusFissi = BONUS_ATTACCO_MAGICO;

        

        if (lancioMigliore) {
            int dadoExtra = randomGenerale.prossimoNumero(1, 9);
            dadoDanno += dadoExtra;
            System.out.println(
                    ANSI.BRIGHT_RED + ANSI.BOLD
                    + "CRITICO SACRO! Dadi danno raddoppiati!"
                    + ANSI.RESET
            );
        }

        int dannoNetto = dadoDanno + bonusFissi;

        // Effetti per tipo minori danni rispetto al mago
        switch (tipo) {
            case RUBAVITA -> {
                int cura = Math.max(1, dannoNetto / 3); 
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
                + ", Mana rimasto: " + paladino.getPuntiMana() + ")" );

                 return dannoNetto;      

             }        else {
            System.out.println("L'incantesimo manca il bersaglio.");
            return 0;
        }
    }

    public int attaccoFisicoPaladino(Paladino paladino, Mostro mostro) {

        

        int tiro = randomGenerale.prossimoNumero(1, 21); // d20

        
        int bonusAttacco = paladino.getAttacco() + (paladino.getLivello() / 2);
        int totale = tiro + bonusAttacco;

        int difesaMostroCombattimento = mostro.getDifesaMostro(); //difesa del mostro usata come difficoltà

        System.out.println(  paladino.getNomePersonaggio()  + " tiro: " + tiro + " + bonus " + bonusAttacco  + " = " + totale + " (difesa mostro: " + difesaMostroCombattimento + ")" );

        if (tiro == 1) {
            System.out.println("Fallimento!");
            return 0;
        }

        boolean lancioMigliore = (tiro == 20);

        if (lancioMigliore || totale >= difesaMostroCombattimento) {
        
        


        int dadoDanno = randomGenerale.prossimoNumero(1, 9); // 1..8

    
        int bonusFissi = BONUS_ATTACCO_FISICO;

        

        
        if (lancioMigliore) {
            int dadoExtra = randomGenerale.prossimoNumero(1, 9);
            dadoDanno += dadoExtra;
            System.out.println(
                    ANSI.BRIGHT_RED + ANSI.BOLD
                    + "COLPO CRITICO! Dadi  raddoppiati!"
                    + ANSI.RESET
            );
        }

        int dannoNetto =  dadoDanno + bonusFissi;

        
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
        else {
            System.out.println("L'attacco manca il bersaglio.");
            return 0;
        }
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
