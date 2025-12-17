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
import service.impl.PozioneServiceImpl;
import service.impl.*;


public class StanzaFactory {

    private static final AtomicInteger OBJ_ID_COUNTER = new AtomicInteger(1);
    private static final AtomicInteger EVENTO_ID_COUNTER = new AtomicInteger(1);
    private static final AtomicInteger KEY_ID_COUNTER = new AtomicInteger(1);

    private final Random rnd = new Random();

    public Stanza creaStanza(int id) {
        int x = rnd.nextInt(100);
        int y = rnd.nextInt(100);
        int[][] coord = {{x, y}};
        StatoStanza stato = StatoStanza.NON_VISITATA;

        List<Oggetto> oggetti = generaOggettiCasuali();
        List<Evento> eventi = generaEventiCasuali();

        boolean richiedeChiave = generaRichiestaChiave();
        String nomeStanza = "Stanza " + id;
        Chiave chiave = richiedeChiave ? creaChiavePerStanza(nomeStanza) : null;

        return new Stanza(id, coord, stato, oggetti, eventi, chiave, false, nomeStanza);
    }

    public List<Oggetto> generaOggettiCasuali() {
        List<Oggetto> oggetti = new ArrayList<>();
        int n = rnd.nextInt(3); // 0..2 oggetti

        for (int i = 0; i < n; i++) {
            int tipo = rnd.nextInt(4); // 0..3
            switch (tipo) {
                case 0:
                    oggetti.add(new PozioneServiceImpl().creaOggettoCasuale());
                    break;
                case 1:
                    oggetti.add(new ArmaServiceImpl().creaOggettoCasuale());
                    break;
                case 2:
                    oggetti.add(new ArmaturaServiceImpl().creaOggettoCasuale());
                    break;
                case 3:
                    oggetti.add(new TesoroServiceImpl().creaOggettoCasuale());
                    break;
                default:
                    oggetti.add(new PozioneServiceImpl().creaOggettoCasuale());
                    break;
            }
        }
        return oggetti;
    }

   private List<Evento> generaEventiCasuali() {
   List<Evento> eventi = new ArrayList<>();
        int n = rnd.nextInt(3); // 0..2 eventi
        for (int i = 0; i < n; i++) {
            int tipo = rnd.nextInt(4); // 0..3
            //switch per tipi di evento da implementare
             switch (tipo) {
                case 0:
                    eventi.add(new MostroServiceImpl().aggiungiEventoCasuale());
                    break;
                case 1:
                    eventi.add(new ArmaServiceImpl().creaOggettoCasuale());
                    break;
                case 2:
                    eventi.add(new ArmaturaServiceImpl().creaOggettoCasuale());
                    break;
                case 3:
                    eventi.add(new TesoroServiceImpl().creaOggettoCasuale());
                    break;
                default:
                    eventi.add(new PozioneServiceImpl().creaOggettoCasuale());
                    break;
        }}
    return eventi;
}

    // restituisce true se la stanza deve richiedere una chiave, false altrimenti
    private boolean generaRichiestaChiave() {
        return rnd.nextInt(2) == 1; // true o false
    }

    public enum StatoStanza {
        VISITATA,
        NON_VISITATA
    }

    private Chiave creaChiavePerStanza(String nomeStanza) {
        int keyId = KEY_ID_COUNTER.getAndIncrement();
        String nomeChiave = nomeStanza;
        String descrizione = "Chiave che apre la stanza: " + nomeStanza;
        int livello = 1;
        return new Chiave(keyId, nomeChiave, descrizione, true, true, false, nomeChiave);
    }

  
}