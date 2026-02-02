package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import domain.Chiave;
import domain.Evento;
import domain.Oggetto;
import domain.Stanza;
import service.impl.ArmaServiceImpl;
import service.impl.ArmaturaServiceImpl;
import service.impl.NPCServiceImpl;
import service.impl.PozioneServiceImpl;
import service.impl.TesoroServiceImpl;
import service.impl.TrappolaServiceImpl;

public class StanzaFactory {

    private static final AtomicInteger OBJ_ID_COUNTER = new AtomicInteger(1);
    private static final AtomicInteger EVENTO_ID_COUNTER = new AtomicInteger(1);
    private static final AtomicInteger KEY_ID_COUNTER = new AtomicInteger(1);

    private final Random rnd = new Random();

    public Stanza creaStanza(int id, int x, int y) {

        int[][] coord = {{x, y}};
        boolean stato = false; // false = non visitata, true = visitata
        List<Oggetto> oggetti = generaOggettiCasuali();
        List<Evento> eventi = generaEventiCasuali();
        Chiave chiave = null;
        boolean bloccata = generaRichiestaChiave(x, y);
        String nomeStanza = "Stanza (" + x + "," + y + ")";

        Stanza stanza = new Stanza(id, coord, stato, oggetti, eventi, chiave, bloccata);
        if (stanza.getListaEventiAttivi() != null) {
            for (Evento evento : stanza.getListaEventiAttivi()) {
                if (evento != null) {
                    evento.setPosizioneCorrente(stanza);
                }
            }
        }
        return stanza;
    }

    public List<Oggetto> generaOggettiCasuali() {
        List<Oggetto> oggetti = new ArrayList<>();
        int coord[][] = {{0, 0}};
        if (coord[0][0] == 0 && coord[0][1] == 0) {
            // stanza di partenza, nessuna chiave
            oggetti.removeIf(o -> o instanceof Chiave);
        }
        int n = rnd.nextInt(1, 5); // 1..4 oggetti

        for (int i = 0; i < n; i++) {
            int tipo = rnd.nextInt(1, 6); // 1..5  
            switch (tipo) {
                case 1:
                    oggetti.add(new PozioneServiceImpl().creaOggettoCasuale());
                    break;
                case 2:
                    oggetti.add(new ArmaServiceImpl().creaOggettoCasuale());
                    break;
                case 3:
                    oggetti.add(new ArmaturaServiceImpl().creaOggettoCasuale());
                    break;
                case 4:
                    oggetti.add(new TesoroServiceImpl().creaOggettoCasuale());
                    break;
                case 5:
                    oggetti.add(new PozioneServiceImpl().creaOggettoCasuale());
                    break;
            }
        }
        return oggetti;
    }

    private List<Evento> generaEventiCasuali() {
        List<Evento> eventi = new ArrayList<>();
        int n = rnd.nextInt(1, 5); // 1..4 eventi
        for (int i = 0; i < n; i++) {
            int tipo = rnd.nextInt(5); // 0..4s
            //switch per tipi di evento da implementare
            switch (tipo) {
                case 0:
                    eventi.add(new TrappolaServiceImpl().aggiungiEventoCasuale());
                    break;
                case 1:
                    eventi.add(new NPCServiceImpl().aggiungiEventoCasuale());
                    break;
                case 2:
                    eventi.add(new TrappolaServiceImpl().aggiungiEventoCasuale());
                    break;
                case 3:
                    eventi.add(new TrappolaServiceImpl().aggiungiEventoCasuale());
                    break;
                default:
                    eventi.add(new TrappolaServiceImpl().aggiungiEventoCasuale());
                    break;
            }
        }
        return eventi;
    }

    // restituisce true se la stanza deve richiedere una chiave, false altrimenti
    private boolean generaRichiestaChiave(int x, int y) {
        // start (0,0) e le stanze adiacenti (distanza Manhattan = 1) NON devono essere bloccate
        if (x == 0 && y == 0) {
            return false;
        }
        if (Math.abs(x - 0) + Math.abs(y - 0) == 1) {
            return false;
        }
        // per le altre stanze random true/false
        //  return rnd.nextInt(2) == 1;
        double probBlocco = 0.25;
        return rnd.nextDouble() < probBlocco;
    }

    public Chiave creaChiavePerStanza(int stanzaId) {
        return new service.impl.ChiaveServiceImpl().creaChiavePerStanza(stanzaId);
    }

}
