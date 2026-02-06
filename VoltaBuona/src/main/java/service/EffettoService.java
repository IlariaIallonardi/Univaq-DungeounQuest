package service;

import java.util.concurrent.ThreadLocalRandom;

import domain.Arma;
import domain.Effetto;
import domain.Personaggio;
import domain.Arma.TipoArma;

public class EffettoService {

    public void applicaEffetto(Personaggio personaggio, Effetto effetto) {
        if (personaggio != null && effetto != null) {

            Effetto.TipoEffetto tipo = effetto.getTipo();
            int durata = effetto.getDurataTurni();

            if (tipo == null) {
                return;
            }

            switch (tipo) {
                case DISARMA:
                    applicaDisarma(personaggio);
                    break;
                case AVVELENAMENTO:
                    applicaAvvelenamento(personaggio, durata);
                    break;
                case STORDIMENTO:
                    applicaStordimento(personaggio, durata);
                    break;
                case FURTO:
                    applicaFurto(personaggio);
                    break;
                case SALTA_TURNO:
                    applicaSaltaTurno(personaggio);
                    break;
                default:
                    // comportamento di default (nessuna azione)
                    break;
            }

        }

    }

    public void applicaDisarma(Personaggio personaggio) {
        personaggio.setStatoPersonaggio("DISARMATO");
        personaggio.setDisarmato(true);

        Arma arma = personaggio.getArmaEquippaggiata();
        System.out.println(" Arma equipaggiata: " + (arma != null ? arma.getNome() : "nessuna"));
      //  System.out.println(" Attacco attuale: " + personaggio.getAttacco());
        if (arma == null) {
            System.out.println("Non hai armi equipaggiate.");
            return;
        }
        personaggio.setArmaEquippaggiata(null);
        
        TipoArma tipoArma = arma.getTipoArma();

        int dannoArma = tipoArma.getDannoBonus();
        personaggio.setAttacco(personaggio.getAttacco() - dannoArma);
        System.out.println(dannoArma + " Attacco ridotto di ");
        System.out.println("Hai perso definitivamente: " + arma.getNome());
    }

    public void applicaAvvelenamento(Personaggio personaggio, int durata) {
        personaggio.setStatoPersonaggio("AVVELENATO");
        // 3 turni totali
        personaggio.setTurniAvvelenato(2);
        //  DANNO IMMEDIATO (turno 1)
        personaggio.subisciDanno(5);
        System.out.println(" Sei stato avvelenato! -5 punti vita.Sarai avvelenato per altri due turni.");
    }

    public void applicaStordimento(Personaggio personaggio, int durata) {
        personaggio.setStatoPersonaggio("STORDITO");
        personaggio.setTurniStordito(2);
        personaggio.subisciDannoPuntiDifesa(5);
        System.out.println(" Sei stordito! -5 punti difesa.Sarai stordito per altri due turni.");
       // System.out.println("Difesa Attuale:" + personaggio.getDifesa());
    }

    public void applicaFurto(Personaggio personaggio) {
        //  personaggio.setStatoPersonaggio("DERUBATO");

        int soldiAttuali = personaggio.getPortafoglioPersonaggio();
        System.out.println("Soldi attuali: " + soldiAttuali);
        if (soldiAttuali <= 0) {
            System.out.println("Non perdi nessuna moneta!");
            personaggio.setPortafoglioPersonaggio(0);
            return;
        }

        //  Random tra 3 e 10
        int rubati = ThreadLocalRandom.current().nextInt(3, 11);

        // non può rubare più di quanto hai
        personaggio.setPortafoglioPersonaggio(soldiAttuali - rubati);
        System.out.println("Soldi dopo furto: " + personaggio.getPortafoglioPersonaggio());
        System.out.println(
                " Sei stato derubato! -" + rubati
                + " monete (rimaste: " + personaggio.getPortafoglioPersonaggio() + ")"
        );

    }

    private void applicaSaltaTurno(Personaggio personaggio) {
        personaggio.aggiungiTurniDaSaltare(1);
     //   System.out.println("Una trappola ti colpisce! Salterai il prossimo turno.");
    }

    public static void applicaEffettiFineTurno(Personaggio personaggio) {
        if (personaggio == null) {
            return;
        }

        if (personaggio.getTurniAvvelenato() > 0) {
           // int dannoAvvel = 5;
            //int applicato = personaggio.subisciDanno(dannoAvvel);

            personaggio.setTurniAvvelenato(personaggio.getTurniAvvelenato() - 1);

            if (personaggio.getTurniAvvelenato() == 0 && "AVVELENATO".equalsIgnoreCase(personaggio.getStatoPersonaggio())) {
                personaggio.setStatoPersonaggio("NORMALE");
                System.out.println("Il veleno ha perso effetto su " + personaggio.getNomePersonaggio());
            }
        }
        if (personaggio.getTurniStordito() > 0) {
            //int dannoAvvel = 5;
            //int applicato = personaggio.subisciDannoDifesa(dannoAvvel);

            personaggio.setTurniStordito(personaggio.getTurniStordito() - 1);

            if (personaggio.getTurniStordito() == 0 && "STORDITO".equalsIgnoreCase(personaggio.getStatoPersonaggio())) {
                personaggio.setStatoPersonaggio("NORMALE");
                System.out.println("Lo stordimento ha perso effetto su " + personaggio.getNomePersonaggio());
            }
        }
    }

}
