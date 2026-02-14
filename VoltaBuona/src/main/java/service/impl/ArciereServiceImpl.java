package service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.Arciere;
import domain.Combattimento;
import domain.Evento;
import domain.Mostro;
import domain.Personaggio;
import domain.Stanza;
import domain.Turno;
import domain.Zaino;
import exception.DungeonException;
import service.PersonaggioService;
import util.ANSI;

public class ArciereServiceImpl implements PersonaggioService  {

    private static final int BONUS_ATTACCO_ARCIERE = 10;
    private RandomSingleton randomGenerale = RandomSingleton.getInstance();

    private Turno turno;

    @Override
    public int attacca(Personaggio personaggio, Mostro mostro, Combattimento combattimento) throws DungeonException {

        Arciere arciere = (Arciere) personaggio;

        Stanza stanzaArciere = arciere.getPosizioneCorrente();
        Stanza stanzaMostro = mostro.getPosizioneCorrente();
        if(stanzaMostro == null || stanzaArciere == null) {
            throw new DungeonException("La stanza del mostro o dell'arciere è null.");
        }

        if (stanzaMostro == stanzaArciere) {
            
            return attaccoArciere(arciere, mostro);
        } else {
            return attaccoDistanzaArciere(arciere, mostro);
        }

    }

    /**
     * Attacco dell'arciere che si trova nella stessa stanza del mostro.
     *
     * @param mostro
     * @param arciere
     */
    public int attaccoArciere(Arciere arciere, Mostro mostro) {

        int tiro = randomGenerale.prossimoNumero(1, 20); //dado di 20 facce per il tiro 

        int bonusAttacco = arciere.getAttacco() + (arciere.getLivello() / 2);
        int totale = tiro + bonusAttacco;

        int difesaMostro = mostro.getDifesaMostro();
        System.out.println(ANSI.BRIGHT_CYAN + ANSI.BOLD + "Tiro dell'arciere totale: " + totale + " = tiro: " + tiro + " + bonus attacco: " + bonusAttacco + ANSI.RESET + "Difesa del mostro: " + difesaMostro);

        if (tiro == 1) {
            System.out.println("Il colpo fallisce!");
            return 0;
        }

        boolean lancioMigliore = (tiro == 20);

        if (lancioMigliore || totale >= difesaMostro) {

            int dadoDanno = randomGenerale.prossimoNumero(1, 8); // danno con un dado da 8 facce.

            int danniTotali = dadoDanno + BONUS_ATTACCO_ARCIERE;
            // Se il lancio è 20 (il massimo),aggiungiamo dei danni in più.
            if (lancioMigliore) {
                int dadoExtra = randomGenerale.prossimoNumero(1, 8);
                danniTotali += dadoExtra;
                System.out.println(ANSI.BRIGHT_RED + ANSI.BOLD + "COLPO CRITICO! Dadi raddoppiati!" + ANSI.RESET);
            }
            mostro.setPuntiVitaMostro(mostro.getPuntiVitaMostro() - danniTotali);
            return danniTotali;
        }
        return 1;
    }

    /**
     * L'arciere può attaccare un mostro in una qualsiasi stanza adiacente,però
     * non fa un vero e proprio combattimento ma toglie solo dei punti vita al
     * mostro per indebolirlo.
     *
     * @param arciere
     * @param mostro
     */
    public int attaccoDistanzaArciere(Arciere arciere, Mostro mostro) {

        int puntiVitaMostro = mostro.getPuntiVitaMostro();
        mostro.setPuntiVitaMostro(puntiVitaMostro - BONUS_ATTACCO_ARCIERE);
        if (mostro.èMortoilMostro()) {
            Stanza stanza = mostro.getPosizioneCorrente();
            stanza.rimuoviEvento(mostro);
        }
        System.out.println(ANSI.BOLD + ANSI.BRIGHT_CYAN + "Attacco a distanza! Il mostro perde " + BONUS_ATTACCO_ARCIERE + " punti vita.Punti vita rimanenti del mostro: " + mostro.getPuntiVitaMostro() + ANSI.RESET);
        return puntiVitaMostro - BONUS_ATTACCO_ARCIERE;
    }

    public Map<String, Mostro> trovaMostriAdiacenti(Stanza stanzaArciere) {
        Map<String, Mostro> mostriAdiacenti = new HashMap<>();

        Map<String, Stanza> adiacenti = stanzaArciere.getStanzaAdiacente();

        for (Map.Entry<String, Stanza> stanzaAggiunta : adiacenti.entrySet()) {
            Stanza stanza = stanzaAggiunta.getValue();

            List<Evento> eventiStanza = stanza.getListaEventiAttivi();

            for (Evento eventoMostro : eventiStanza) {
                if (eventoMostro instanceof Mostro mostro) {
                    mostriAdiacenti.put(stanzaAggiunta.getKey(), mostro);

                }
            }
        }
        return mostriAdiacenti;
    }

    @Override
    public Personaggio creaPersonaggio(String nome, Personaggio personaggio) {
        Stanza stanza = null;
        Zaino zaino = new Zaino();
        return new Arciere(null, 15,
                300,
                0, 2, nome, stanza, false, 100, 40,
                "normale", 0, 0, 0, 0, zaino, 0);
    }

}
