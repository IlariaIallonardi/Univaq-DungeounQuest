package service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import domain.Evento;
import domain.Oggetto;
import domain.Personaggio;
import domain.Stanza;
import service.EventoService;
import service.GiocoService;
import service.PersonaggioService;
import service.TurnoService;


public class TurnoServiceImpl implements TurnoService {

    private GiocoService giocoService;
    private PersonaggioService personaggioService;
    private EventoService eventoService;
    private List<Personaggio> ordineTurno = new ArrayList<>();

    public TurnoServiceImpl(GiocoService giocoService,
            PersonaggioService personaggioService,
            EventoService eventoService) {
        this.giocoService = giocoService;
        this.personaggioService = personaggioService;
        this.eventoService = eventoService;
    }

    public enum Direzione {
        NORD, SUD, EST, OVEST
    }

    public TurnoServiceImpl(PersonaggioService ps) {
        this.personaggioService = ps;
    }

    public void terminaTurnoCorrente(Personaggio personaggio) {

        for (Personaggio p : ordineTurno) {
            if (!p.èMorto(p)) {
                aggiornaEffettiFineTurno(personaggio);
            }
        }
    }

    public void aggiornaEffettiFineTurno(Personaggio personaggio) {

        if (personaggio.getTurniAvvelenato() > 0) {
            int dannoVeleno = 3; // puoi cambiare
            personaggio.subisciDanno(dannoVeleno);
            personaggio.setTurniAvvelenato(personaggio.getTurniAvvelenato() - 1);
            System.out.println("Il veleno infligge " + dannoVeleno + " danni a " + personaggio.getNomeP());

            if (personaggio.getTurniAvvelenato() == 0 && "AVVELENATO".equalsIgnoreCase(personaggio.getStatoPersonaggio())) {
                personaggio.setStatoPersonaggio("NORMALE");
                System.out.println("Il veleno ha perso effetto su " + personaggio.getNomeP());
            }
        }

        // 2 CONGELAMENTO: dura N turni, qui puoi gestire eventuali penalità
        if (personaggio.getTurniCongelato() > 0) {
            personaggio.setTurniCongelato(personaggio.getTurniCongelato() - 1);
            System.out.println("❄ " + personaggio.getNomeP() + " è ancora congelato ("
                    + personaggio.getTurniCongelato() + " turni rimanenti)");

            if (personaggio.getTurniCongelato() == 0 && "CONGELATO".equalsIgnoreCase(personaggio.getStatoPersonaggio())) {
                personaggio.setStatoPersonaggio("NORMALE");
                System.out.println(personaggio.getNomeP() + " si è scongelato.");
            }
        }

        // 3 STORDIMENTO: penalizza per N turni (es. non può attaccare)
        if (personaggio.getTurniStordito() > 0) {
            personaggio.setTurniStordito(personaggio.getTurniStordito() - 1);
            System.out.println(personaggio.getNomeP() + " è ancora stordito ("
                    + personaggio.getTurniStordito() + " turni rimanenti)");

            if (personaggio.getTurniStordito() == 0 && "STORDITO".equalsIgnoreCase(personaggio.getStatoPersonaggio())) {
                personaggio.setStatoPersonaggio("NORMALE");
                System.out.println(personaggio.getNomeP() + " non è più stordito.");
            }
        }
    }

    @Override
    public void eseguiTurnoGiocatore(Personaggio personaggio) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("\n===== TURNO DI " + personaggio.getNomeP() + " =====");

        Stanza stanzaCorrente = personaggio.getPosizioneCorrente();
        System.out.println("Ti trovi in: " + stanzaCorrente.getNomeStanza());

        // 1movimento opzionale
        System.out.println("Vuoi muoverti? (s/n)");
        String risposta = scanner.nextLine().trim().toLowerCase();

        if (risposta.equals("s")) {
            gestisciMovimento(personaggio, scanner);
            stanzaCorrente = personaggio.getPosizioneCorrente();
        }

        // 2 esplorazione: mostro eventi e oggetti
        List<Evento> eventi = stanzaCorrente.getListaEventiAttivi();
        List<Oggetto> oggetti = stanzaCorrente.getOggettiPresenti();

        boolean ciSonoEventi = eventi != null && !eventi.isEmpty();
        boolean ciSonoOggetti = oggetti != null && !oggetti.isEmpty();

        System.out.println("\n--- Esplorazione stanza ---");

        if (!ciSonoEventi && !ciSonoOggetti) {
            System.out.println("La stanza è vuota.");
            return;
        }

        if (ciSonoEventi) {
            System.out.println("\nEventi presenti:");
            for (int i = 0; i < eventi.size(); i++) {
                System.out.println(" [" + (i + 1) + "] " + eventi.get(i).getDescrizione());
            }
        }

        if (ciSonoOggetti) {
            System.out.println("\nOggetti presenti:");
            for (int i = 0; i < oggetti.size(); i++) {
                System.out.println(" [" + (i + 1) + "] " + oggetti.get(i).getNome());
            }
        }

        // 3️⃣ scelta dell'azione
        System.out.println("\nCosa vuoi fare?");
        System.out.println("1) Fare un evento");
        System.out.println("2) Prendere un oggetto");
        if (ciSonoEventi && ciSonoOggetti) {
            System.out.println("3) Fare un evento E prendere un oggetto");
        }
        System.out.println("0) Passa il turno");

        int scelta = Integer.parseInt(scanner.nextLine());

        switch (scelta) {
            case 1 -> {
                if (ciSonoEventi) {
                    eseguiEvento(personaggio, stanzaCorrente, eventi, scanner);
                }
            }
            case 2 -> {
                if (ciSonoOggetti) {
                    raccogliUnOggetto(personaggio, stanzaCorrente, oggetti, scanner);
                }
            }
            case 3 -> {
                if (ciSonoEventi && ciSonoOggetti) {
                    eseguiEvento(personaggio, stanzaCorrente, eventi, scanner);
                    raccogliUnOggetto(personaggio, stanzaCorrente, oggetti, scanner);
                }
            }
            case 0 ->
                System.out.println("Turno terminato.");
            default ->
                System.out.println("Scelta non valida.");
        }

        System.out.println("===== FINE TURNO DI " + personaggio.getNomeP() + " =====");
    }

    // 🔧 Movimento gestito tramite input
    public void gestisciMovimento(Personaggio personaggio, Scanner scanner) {

        System.out.println("Direzioni disponibili: N, S, E, O");
        String dir = scanner.nextLine().trim().toUpperCase();

        Direzione direzione = switch (dir) {
            case "N" ->
                Direzione.NORD;
            case "S" ->
                Direzione.SUD;
            case "E" ->
                Direzione.EST;
            case "O" ->
                Direzione.OVEST;
            default ->
                null;
        };

        if (direzione == null) {
            System.out.println("Direzione non valida.");
            return;
        }

        GiocoServiceImpl giocoServiceImpl = new GiocoServiceImpl(null, null, 0);
        giocoServiceImpl.muovi(personaggio, direzione);
    }

    // 🔧 Metodo: esegue UN evento scelto
    public void eseguiEvento(Personaggio personaggio, Stanza stanza, List<Evento> eventi, Scanner scanner) {

        System.out.println("Scegli l'evento da eseguire:");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index < 0 || index >= eventi.size()) {
            System.out.println("Indice non valido.");
            return;
        }

        Evento e = eventi.get(index);
        eventoService.eseguiEventiInStanza(personaggio, stanza);

        // Se l’evento va eliminato dopo l’uso:
        // eventi.remove(index);
    }

    // 🔧 Metodo: raccoglie UN oggetto scelto
    public void raccogliUnOggetto(Personaggio p, Stanza stanza, List<Oggetto> oggetti, Scanner scanner) {

        System.out.println("Scegli l'oggetto da prendere:");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index < 0 || index >= oggetti.size()) {
            System.out.println("Indice non valido.");
            return;
        }

        Oggetto o = oggetti.get(index);
        boolean ok = personaggioService.raccogliereOggetto(p, o);

        if (!ok) {
            System.out.println("Non puoi raccogliere l'oggetto.");
        }
    }

/* main abbastanza funzionante: problemi oerchè ci sono paramentri null
  public static void main(String[] args) {

    // ============================
    // 1) INIZIALIZZO I SERVICE
    // ============================

    GiocoService giocoService = new GiocoService(null); 
    PersonaggioService personaggioService = new PersonaggioService(); 
    EventoService eventoService = new EventoService();               

    TurnoService turnoService = new TurnoServiceImpl(
            giocoService,
            personaggioService,
            eventoService
    );

    // ============================
    // 2) CREO DUE STANZE DI TEST
    // ============================

    // ⚠ ADATTATO al costruttore a 6 parametri:
    // Stanza(int id, int[][] coordinate, String statoS,
    //        List<Oggetto> oggettiPresenti,
    //        List<Evento> listaEventiAttivi,
    //        Chiave chiaveRichiesta)

    Stanza stanza1 = new Stanza(0, new int[][]{{0,1}}, null, new ArrayList<>(), new ArrayList<>(), null, false,"stanza uno");

    Stanza stanza2 = new Stanza(1, new int[][]{{0,2}}, null, new ArrayList<>(), new ArrayList<>(), null, false, "stanza due");

    // QUI presumiamo che in Stanza tu abbia:
    // private final Map<String, Stanza> stanzaAdiacente = new HashMap<>();
    // e un getter: getStanzaAdiacente()
    stanza1.getStanzaAdiacente().put("NORD", stanza2);
    stanza2.getStanzaAdiacente().put("SUD", stanza1);

    // ============================
    // 3) CREO UN PERSONAGGIO CON HP VALIDI
    // ============================

    Personaggio personaggio = new Guerriero(
            1,            // id
            40,           // punti vita
            10,           // attacco
            5,            // difesa
            10,           // mana
            "Eroe",       // nome
            stanza1,      // posizione corrente
            1,            // livello
            0,            // exp
            "NORMALE",    // stato
            new Zaino()   // zaino
    );

    stanza1.getListaPersonaggi().add(personaggio);

    // ============================
    // 4) AGGIUNGO EVENTO E OGGETTO ALLA STANZA 1
    // ============================

    Evento evento = new Evento(101, true, false, "Un misterioso altare antico");
    stanza1.getListaEventiAttivi().add(evento);

    // Adatto il costruttore della pozione alla tua firma:
    // Pozione(boolean riutilizzabile, String nome,
    //         int valorePozione, int id,
    //         Effetto effetto, String descrizione,
    //         boolean inizioEvento, boolean fineEvento, boolean attivo)

    Oggetto pozione = new Pozione(false,null, 40, 0, "mana", null, false, false, false);
    stanza1.getOggettiPresenti().add(pozione);

    // ============================
    // 5) LOOP DI TURNI
    // ============================

    Scanner scanner = new Scanner(System.in);
    boolean continua = true;

    while (continua) {

        System.out.println("\n========================");
        System.out.println("   NUOVO TURNO TEST");
        System.out.println("========================");

        turnoService.eseguiTurnoGiocatore(personaggio);

        if (personaggio.èMorto(personaggio)) {
            System.out.println("💀 Il personaggio è morto. Test finito.");
            break;
        }

        System.out.println("\nVuoi fare un altro turno? (s/n)");
        String risposta = scanner.nextLine().toLowerCase();

        if (!risposta.equals("s")) {
            continua = false;
        }
    }

    System.out.println("Test TurnoServiceImpl terminato.");
} */
}

