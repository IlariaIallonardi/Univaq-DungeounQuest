package service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import domain.Evento;
import domain.Guerriero;
import domain.Oggetto;
import domain.Personaggio;
import domain.Pozione;
import domain.Stanza;
import domain.Zaino;
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

        // 2️⃣ CONGELAMENTO: dura N turni, qui puoi gestire eventuali penalità
        if (personaggio.getTurniCongelato() > 0) {
            personaggio.setTurniCongelato(personaggio.getTurniCongelato() - 1);
            System.out.println("❄ " + personaggio.getNomeP() + " è ancora congelato ("
                    + personaggio.getTurniCongelato() + " turni rimanenti)");

            if (personaggio.getTurniCongelato() == 0 && "CONGELATO".equalsIgnoreCase(personaggio.getStatoPersonaggio())) {
                personaggio.setStatoPersonaggio("NORMALE");
                System.out.println(personaggio.getNomeP() + " si è scongelato.");
            }
        }

        // 3️⃣ STORDIMENTO: penalizza per N turni (es. non può attaccare)
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

        // 1️⃣ movimento opzionale
        System.out.println("Vuoi muoverti? (s/n)");
        String risposta = scanner.nextLine().trim().toLowerCase();

        if (risposta.equals("s")) {
            gestisciMovimento(personaggio, scanner);
            stanzaCorrente = personaggio.getPosizioneCorrente();
        }

        // 2️⃣ esplorazione: mostro eventi e oggetti
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

        giocoService.muovi(personaggio, direzione);
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

    public static void main(String[] args) {

        // =========================
        // 1) CREO I SERVICE
        // =========================
        // TODO: usa i tuoi costruttori reali
        GiocoService giocoService = new GiocoService(/* eventuali parametri */);

        PersonaggioService personaggioService = new PersonaggioService(); // se è una classe concreta
        EventoService eventoService = new EventoService();               // idem

        TurnoService turnoService = new TurnoServiceImpl(
                giocoService,
                personaggioService,
                eventoService
        );

        // =========================
        // 2) CREO DUE STANZE DI TEST
        // =========================
        // TODO: adatta al tuo costruttore di Stanza
        Stanza stanza1 = new Stanza(
                1,
                /* coordinate */ null,
                "Stanza iniziale",
                new ArrayList<>(), // lista oggetti
                new ArrayList<>(), // lista eventi
                null // chiaveRichiesta o simile
        );

        Stanza stanza2 = new Stanza(
                2,
                /* coordinate */ null,
                "Stanza a nord",
                new ArrayList<>(),
                new ArrayList<>(),
                null
        );

        // imposto le mappe delle stanze adiacenti
        stanza1.getStanzaAdiacente().put("NORD", stanza2);
        stanza2.getStanzaAdiacente().put("SUD", stanza1);

        // =========================
        // 3) AGGIUNGO EVENTI E OGGETTI A stanza1
        // =========================
        // TODO: adatta ai tuoi costruttori di Evento e Oggetto/Pozione/Arma ecc.
        Evento evento1 = new Evento(100, true, false, "Un vecchio altare misterioso");
        stanza1.getListaEventi().add(evento1);

        Oggetto oggetto1 = new Pozione("Pozione di cura", Pozione.Tipo.CURA, 10);
        stanza1.getOggettiPresenti().add(oggetto1);

        // =========================
        // 4) CREO UN PERSONAGGIO CONCRETO
        // =========================
        // Personaggio è astratta → uso una sottoclasse, ad esempio Guerriero
        // TODO: adatta al tuo costruttore di Guerriero / Mago / ecc.
        Personaggio giocatore = new Guerriero(
                /* id */1,
                /* pv */ 40,
                /* attacco */ 10,
                /* difesa */ 5,
                /* mana */ 10,
                /* nome */ "Eroe",
                /* stanza */ stanza1,
                /* livello */ 1,
                /* exp */ 0,
                /* stato */ "NORMALE",
                /* zaino */ new Zaino()
        );

        // mi assicuro che stanza1 contenga il giocatore nella sua lista
        stanza1.getListaPersonaggi().add(giocatore);

        // =========================
        // 5) LOOP DEI TURNI
        // =========================
        Scanner scanner = new Scanner(System.in);

        boolean continua = true;

        while (continua) {

            if (personaggio.èMorto()) {
                System.out.println("💀 Il personaggio è morto. Fine partita di test.");
                break;
            }

            // eseguo un turno completo del giocatore
            turnoService.eseguiTurnoGiocatore(personaggio);

            // eventualmente, aggiorno effetti a fine turno
            // se hai un TurnoServiceImpl con aggiornaEffettiFineTurno, puoi chiamarlo qui
            // ((TurnoServiceImpl) turnoService).aggiornaEffettiFineTurno(giocatore);
            System.out.println("\nVuoi fare un altro turno? (s/n)");
            String risposta = scanner.nextLine().trim().toLowerCase();
            if (!risposta.equals("s")) {
                continua = false;
            }
        }

        System.out.println("Test TurnoServiceImpl terminato.");
    }

}
