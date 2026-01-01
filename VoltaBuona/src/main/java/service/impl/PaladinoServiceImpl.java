package service.impl;

import domain.Combattimento;
import domain.Mostro;
import domain.Paladino;
import domain.Personaggio;
import domain.Stanza;
import domain.Zaino;
import service.PersonaggioService;

public class PaladinoServiceImpl implements PersonaggioService {

    /**
     * Metodo per proteggere un altro giocatore
     *
     * @param paladino Il paladino che usa la protezione
     * @param bersaglio Il personaggio da proteggere
     * @return true se la protezione è stata applicata con successo
     */
    public boolean protezionePersonaggio(Paladino paladino, Personaggio bersaglio) {
        if (paladino == null || bersaglio == null) {
            return false;
        }

        // Verifica se il paladino ha abbastanza punti vita
        if (paladino.getPuntiVita() < 20) {
            return false;
        }

        // delega al dominio: solo 
        boolean ok = paladino.proteggi(bersaglio);
        bersaglio.setTurnoProtetto(bersaglio.getTurnoProtetto() + 1);
        if (!ok) {
            return false;
        }

        System.out.println(paladino.getNomePersonaggio() + " protegge per un turno " + bersaglio.getNomePersonaggio());
        return true;
    }

    /**
     * Metodo per utilizzare la magia sacra del paladino
     *
     * @param paladino Il paladino che usa la magia
     * @param bersaglio Il personaggio bersaglio
     * @param tipoMagiaSacra Il tipo di magia da utilizzare
     * @return true se la magia è stata lanciata con successo
     */
    @Override
    public int attacca(Personaggio personaggio, Mostro mostro, Combattimento combattimento) {
        if (!(personaggio instanceof Paladino)) {
            return 0;

        }
       Paladino paladino = (Paladino) personaggio;
if (paladino == null || mostro == null) {
    return 0;
}

domain.Arma arma = paladino.getArmaEquippaggiata();
if (arma != null) {
    System.out.println("[ARMA] " + paladino.getNomePersonaggio()
        + " ha equipaggiato: " + arma.getNome()
        + " (dannoBonus=" + arma.getDannoBonus()
        + ", tipo=" + arma.getTipoArma() + ")");
}
TipoMagiaSacra tipoMagiaSacra = scegliMagiaPerPaladino(paladino, mostro, combattimento);
int tiro = java.util.concurrent.ThreadLocalRandom.current().nextInt(1, 21);
if (tiro == 1) {
    System.out.println("Tiro 1: fallimento critico!");
    return 0;
}
boolean critico = (tiro == 20);

        // Applica l'effetto: qui esempi negativi/offensivi contro il mostro
        switch (tipoMagiaSacra) {

            case MALATTIA:
                // esempio: riduce danno del mostro
                try {
                    mostro.setDannoMostro(Math.max(0, mostro.getDannoMostro() - 2));
                } catch (Exception ignored) {
                }
                System.out.println(paladino.getNomePersonaggio() + " indebolisce l'attacco di " + mostro.getNomeMostro());
                break;
            case AMMALIAMENTO:
                try {
                    mostro.setDifesaMostro(Math.max(0, mostro.getDifesaMostro() - 5));
                } catch (Exception ignored) {
                }
                System.out.println(paladino.getNomePersonaggio() + " ammalia " + mostro.getNomeMostro());
                break;

            case RUBAVITA:
                int danno = 10;
                mostro.setPuntiVitaMostro(Math.max(0, mostro.getPuntiVitaMostro() - danno));
                System.out.println(paladino.getNomePersonaggio() + " ruba vita a " + mostro.getNomeMostro() + " (" + danno + " danno).");
                break;
            default:
                // fallback: danno normale
                int attacco = paladino.getAttacco();
                int livello = paladino.getLivello();
                int difesaMostro = mostro.getDifesaMostro();

                int dannoBase = attacco + livello * 2;
                int dannoNetto = Math.max(0, dannoBase - difesaMostro);

                mostro.setPuntiVitaMostro(mostro.getPuntiVitaMostro() - dannoNetto);

                System.out.println(paladino.getNomePersonaggio() + " colpisce " + mostro.getNomeMostro() + " per " + dannoNetto + " danno.");
                break;
        }

        paladino.setPuntiMana(Math.max(0, paladino.getPuntiMana() - tipoMagiaSacra.getCostoMana()));

        // gestione morte / XP (semplice)
        if (mostro.getPuntiVitaMostro() <= 0) {

            paladino.setEsperienza(paladino.getEsperienza() + 20);
            if (paladino.getEsperienza() >= 100) {
                paladino.setLivello(paladino.getLivello() + 1);
                paladino.setEsperienza(0);
                System.out.println(" Complimenti! " + paladino.getNomePersonaggio() + " è salito al livello " + paladino.getLivello());
            }
            System.out.println(mostro.getNomeMostro() + " è stato sconfitto da " + paladino.getNomePersonaggio());
        }

        return 1; // ritorna 1 = magia eseguita (potresti restituire danno effettivo se preferisci)
    }

// Helper che sceglie la magia internamente (heuristica semplice)
    public TipoMagiaSacra scegliMagiaPerPaladino(Paladino paladino, Mostro mostro, Combattimento combattimento) {
        // esempio di regole:
        // - se HP mostro bassi -> RUBAVITA per finire e curarsi
        // - se paladino ha poca vita -> RUBAVITA
        // - se mostro ha molta difesa -> CORROSIONE
        // - altrimenti AVVELENAMENTO
        try {
            if (paladino.getPuntiVita() < 15 && paladino.getPuntiMana() >= TipoMagiaSacra.RUBAVITA.getCostoMana()) {
                return TipoMagiaSacra.RUBAVITA;
            }
            if (mostro.getPuntiVitaMostro() <= 12 && paladino.getPuntiMana() >= TipoMagiaSacra.MALATTIA.getCostoMana()) {
                return TipoMagiaSacra.MALATTIA;
            }
            if (mostro.getDifesaMostro() > 5 && paladino.getPuntiMana() >= TipoMagiaSacra.AMMALIAMENTO.getCostoMana()) {
                return TipoMagiaSacra.AMMALIAMENTO;
            }

        } catch (Exception ignored) {
        }
        // fallback

        return TipoMagiaSacra.AMMALIAMENTO;
    }

    @Override
      public Personaggio creaPersonaggio(String nome, Personaggio personaggio) {
Stanza stanza = null;
Zaino zaino = new Zaino();
return new Paladino("abilità", null, 200, 300, 0, 2, nome, stanza, false, 100, 300, "normale", 0, 0, 0, 0, zaino, 0);
}

    public enum TipoMagiaSacra {
        RUBAVITA(4),
        AMMALIAMENTO(5),
        MALATTIA(7);

        private final int costoMana;

        TipoMagiaSacra(int costoMana) {
            this.costoMana = costoMana;
        }

        public int getCostoMana() {
            return costoMana;
        }
    }

    public void usaAbilitàSpeciale(Personaggio personaggio, String abilitàSpeciale) {
    }
    
}
