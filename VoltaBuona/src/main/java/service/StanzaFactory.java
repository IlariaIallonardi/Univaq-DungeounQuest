package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import domain.Arma;
import domain.Armatura;
import domain.Chiave;
import domain.Evento;
import domain.Oggetto;
import domain.Pozione;
import domain.Stanza;
import domain.Tesoro;


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
                    
                    oggetti.add(creaPozioneCasuale());
                    break;
                case 1:
                    oggetti.add(creaArmaCasuale());
                    break;
                case 2:
                    oggetti.add(creaArmaturaCasuale());
                    break;
                case 3:
                    oggetti.add(creaTesoroCasuale());
                    break;
                default:
                    oggetti.add(creaPozioneCasuale());
                    break;
            }
        }
        return oggetti;
    }

   private List<Evento> generaEventiCasuali() {
    List<Evento> eventi = new ArrayList<>();

    int n = rnd.nextInt(3); // 0..2 eventi
    String descrizione = evento.getDescrizione(); // descrizione unica per tutti gli eventi
    for (int i = 0; i < n; i++) {
        int eventoId = EVENTO_ID_COUNTER.getAndIncrement();
        eventi.add(new Evento(eventoId, true, false, descrizione, "EventoCasuale"));
    }
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

   private Pozione creaPozioneCasuale() {
    int id = OBJ_ID_COUNTER.getAndIncrement();
    String nome = "Pozione " + id;
    String descrizione = "Una pozione magica.";
    return new Pozione(false, null, potenza, id, nome, descrizione, false, false, false);
}

    private Arma creaArmaCasuale() {
        int id = OBJ_ID_COUNTER.getAndIncrement();
        String nome = "Spada " + id;
        String descrizione = "Una spada affilata.";
        int danno = rnd.nextInt(30) + 5;
        return new Arma(danno, id, nome, descrizione, false, false, false, null);
    }

    private Armatura creaArmaturaCasuale() {
        int id = OBJ_ID_COUNTER.getAndIncrement();
        String nome = "Armatura " + id;
        String descrizione = "Un'armatura resistente.";
        int difesa = rnd.nextInt(20) + 5;
        return new Armatura(difesa, 0, Armatura.TipoArmatura.DEBOLE, id, "armatura", "armatura", false, false, false);
    }

    private Tesoro creaTesoroCasuale() {
        int id = OBJ_ID_COUNTER.getAndIncrement();
        String nome = "Tesoro " + id;
        String descrizione = "Un tesoro prezioso.";
        int valore = rnd.nextInt(100) + 50;
        return new Tesoro(valore, id, nome, descrizione, false, false, false);
    }
}