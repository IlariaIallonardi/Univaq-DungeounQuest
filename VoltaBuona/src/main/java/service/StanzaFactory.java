package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import domain.Chiave;
import domain.Evento;
import domain.Oggetto;
import domain.Stanza;
import service.impl.ArmaServiceImpl;
import service.impl.ArmaturaServiceImpl;
import service.impl.MostroServiceImpl;
import service.impl.NPCServiceImpl;
import service.impl.PassaggioSegretoServiceImpl;
import service.impl.PozioneServiceImpl;
import service.impl.RandomSingleton;
import service.impl.TesoroServiceImpl;
import service.impl.TrappolaServiceImpl;

public class StanzaFactory {

    private Random random = new Random();
    private RandomSingleton randomGenerale = RandomSingleton.getInstance();
    private FileService fileService;

    public StanzaFactory(FileService fileService) {
        this.fileService = fileService;
    }
    public StanzaFactory() {
    }

    public Stanza creaStanza(int id, int x, int y) {
        Stanza stanza = new Stanza();
        int[][] coord = {{x, y}};
        boolean stato = false; // false = non visitata, true = visitata
        List<Oggetto> oggetti = generaOggettiCasuali();
        List<Evento> eventi = generaEventiCasuali();
        Chiave chiave = null;
        boolean bloccata = generaRichiestaChiave(x, y);
        boolean uscitaVittoria = stanza.isUscitaVittoria();

        stanza = new Stanza(id, coord, stato, oggetti, eventi, chiave, bloccata, uscitaVittoria);
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
        int n = randomGenerale.prossimoNumero(1, 5); // 1..5 oggetti

        for (int i = 0; i < n; i++) {
            int tipo = randomGenerale.prossimoNumero(1, 5); // 1..5  
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
        int n = randomGenerale.prossimoNumero(1, 5); // 1..4 eventi
        for (int i = 0; i < n; i++) {
            int tipo = randomGenerale.prossimoNumero(0, 3); // 0..3
            //switch per tipi di evento da implementare
            switch (tipo) {
                case 0:
                    eventi.add(new TrappolaServiceImpl().aggiungiEventoCasuale());
                    break;
                case 1:
                    MostroServiceImpl mostroService = new MostroServiceImpl();
                    mostroService.setCombattimentoService(new CombattimentoService(mostroService, null, null));
                    eventi.add(mostroService.aggiungiEventoCasuale());
                    break;
                case 2:
                    eventi.add(new NPCServiceImpl().aggiungiEventoCasuale());
                    break;
                case 3:
                    eventi.add(new PassaggioSegretoServiceImpl().aggiungiEventoCasuale());
                    break;
                default:
                    eventi.add(new PassaggioSegretoServiceImpl().aggiungiEventoCasuale());
                    break;
            }
        }
        return eventi;
    }

    // restituisce true se la stanza deve richiedere una chiave, false altrimenti
    private boolean generaRichiestaChiave(int x, int y) {
        // start (0,0) e le stanze adiacenti (distanza Manhattan = 1) NON devono essere bloccate
        if ((x == 0 && y == 0) || (x == 1 && y == 0) || (x == 0 && y == 1)) {
            return false;
        }

        // per le altre stanze random true/false
        //  return rnd.nextInt(2) == 1;
        double probabilitaBloccata = 0.25;
        return random.nextDouble() < probabilitaBloccata;

    }

    public Chiave creaChiavePerStanza(int stanzaId) {
        return new service.impl.ChiaveServiceImpl().creaChiavePerStanza(stanzaId);
    }

}
