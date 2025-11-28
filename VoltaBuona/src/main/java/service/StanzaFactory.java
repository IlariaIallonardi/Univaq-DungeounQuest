package service;

import java.util.*;

import domain.*;

public class StanzaFactory {

    private final Random rnd = new Random();

    public Stanza creaStanza(int id) {
        int x = rnd.nextInt(100);
        int y = rnd.nextInt(100);
        int[][] coord = {{x, y}};
        String stato = "NON_VISITATA";

        List<Oggetto> oggetti = generaOggettiCasuali();
        List<Evento> eventi = generaEventiCasuali();
        
        // ora decidiamo se la stanza richiede una chiave usando 1 (serve) o 0 (non serve)
        int chiaveRichiestaFlag = generaRichiestaChiave(); // 0 o 1
        Chiave chiave = (chiaveRichiestaFlag == 1) ? new Chiave("ChiaveReq_" + id) : null;

       
        return new Stanza(id, coord, StatoStanza.NON_VISITATA, oggetti, eventi, chiave);
    }

    private List<Oggetto> generaOggettiCasuali() {
        List<Oggetto> oggetti = new ArrayList<>();
        int n = rnd.nextInt(3); // 0–2 oggetti

        for (int i = 0; i < n; i++) {
            int tipo = rnd.nextInt(5); // 5 tipi di oggetti

            switch (tipo) {
                case 0 ->
                    oggetti.add(new Pozione("Pozione_" + rnd.nextInt(100), rnd.nextInt(20) + 10));
                case 1 ->
                    oggetti.add(new Arma("Spada_" + rnd.nextInt(100), rnd.nextInt(10) + 5));
                case 2 ->
                    oggetti.add(new Armatura("Armatura_" + rnd.nextInt(100), rnd.nextInt(5) + 1));
                case 3 ->
                    oggetti.add(new Chiave("Chiave_" + rnd.nextInt(10)));

                case 4 ->
                    oggetti.add(new Tesoro("Tesoro_" + rnd.nextInt(100), rnd.nextInt(200) + 50));
            }
        }

        return oggetti;
    }

    private List<Evento> generaEventiCasuali() {
        List<Evento> eventi = new ArrayList<>();
        int n = rnd.nextInt(3);
        String[] descrizioni = {
            "Un rumore sinistro si avvicina...",
            "Hai trovato un messaggio inciso nella pietra.",
            "Una trappola scatta improvvisamente!",
            "Appare un NPC misterioso...",
            "Una luce magica illumina la stanza."
        };
        for (int i = 0; i < n; i++) {
            eventi.add(new Evento(rnd.nextInt(1000), false, false, descrizioni[rnd.nextInt(descrizioni.length)]));
        }
        return eventi;
    }

   // restituisce 1 se la stanza deve richiedere una chiave, 0 altrimenti
    private int generaRichiestaChiave() {
        return rnd.nextInt(2); // 0 o 1
    }

    public enum StatoStanza {
        VISITATA,
        NON_VISITATA
    }
}
