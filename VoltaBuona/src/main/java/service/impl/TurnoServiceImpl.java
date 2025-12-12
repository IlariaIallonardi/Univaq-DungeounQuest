package service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import domain.Evento;
import domain.Mostro;
import domain.Oggetto;
import domain.Personaggio;
import domain.Stanza;
import domain.Zaino;
import service.CombattimentoService;
import service.Direzione;
import service.EventoService;
import service.GiocoService;
import service.PersonaggioService;
import service.StanzaFactory.StatoStanza;
import service.TurnoService;

public class TurnoServiceImpl implements TurnoService {

    private Personaggio personaggio;
    private GiocoService giocoService;
    private PersonaggioService personaggioService;
    private EventoService eventoService;
    private Stanza stanzaCorrente = personaggio.getPosizioneCorrente();
    private List<Oggetto> oggetti = stanzaCorrente.getOggettiPresenti();
    private List<Evento> eventi = stanzaCorrente.getListaEventiAttivi();
    private List<Personaggio> ordineTurno = new ArrayList<>();
    private CombattimentoService combattimentoService = new CombattimentoServiceImpl();
    private Random random = new Random();

    public TurnoServiceImpl(GiocoService giocoService,
            PersonaggioService personaggioService,
            EventoService eventoService) {
        this.giocoService = giocoService;
        this.personaggioService = personaggioService;
        this.eventoService = eventoService;
    }

    public TurnoServiceImpl(PersonaggioService ps) {
        this.personaggioService = ps;
    }

    @Override
    public List<Personaggio> calcolaOrdineIniziativa(List<Personaggio> partecipanti) {

        ordineTurno.clear();

        // Mappa: Personaggio → tiro dado
        Map<Personaggio, Integer> tiri = new HashMap<>();

        // 1️⃣ Ogni personaggio tira il dado
        for (Personaggio p : partecipanti) {
            int tiro = random.nextInt(20) + 1; // dado d20
            tiri.put(p, tiro);
            System.out.println(p.getNomePersonaggio() + " tira iniziativa: " + tiro);
        }

        // 2️⃣ Costruzione ordine turno SENZA sort
        while (!tiri.isEmpty()) {

            Personaggio migliore = null;
            int max = -1;

            // cerco il tiro più alto rimasto
            for (Map.Entry<Personaggio, Integer> entry : tiri.entrySet()) {
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    migliore = entry.getKey();
                }
            }

            // aggiungo all'ordine
            ordineTurno.add(migliore);

            // rimuovo dai candidati
            tiri.remove(migliore);
        }

        System.out.println("\nOrdine dei turni:");
        for (int i = 0; i < ordineTurno.size(); i++) {
            System.out.println((i + 1) + ") " + ordineTurno.get(i).getNomePersonaggio());
        }
        return ordineTurno;
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
            System.out.println("Il veleno infligge " + dannoVeleno + " danni a " + personaggio.getNomePersonaggio());

            if (personaggio.getTurniAvvelenato() == 0 && "AVVELENATO".equalsIgnoreCase(personaggio.getStatoPersonaggio())) {
                personaggio.setStatoPersonaggio("NORMALE");
                System.out.println("Il veleno ha perso effetto su " + personaggio.getNomePersonaggio());
            }
        }

        // 2 CONGELAMENTO: dura N turni, qui puoi gestire eventuali penalità
        if (personaggio.getTurniCongelato() > 0) {
            personaggio.setTurniCongelato(personaggio.getTurniCongelato() - 1);
            System.out.println("❄ " + personaggio.getNomePersonaggio() + " è ancora congelato ("
                    + personaggio.getTurniCongelato() + " turni rimanenti)");

            if (personaggio.getTurniCongelato() == 0 && "CONGELATO".equalsIgnoreCase(personaggio.getStatoPersonaggio())) {
                personaggio.setStatoPersonaggio("NORMALE");
                System.out.println(personaggio.getNomePersonaggio() + " si è scongelato.");
            }
        }

        // 3 STORDIMENTO: penalizza per N turni (es. non può attaccare)
        if (personaggio.getTurniStordito() > 0) {
            personaggio.setTurniStordito(personaggio.getTurniStordito() - 1);
            System.out.println(personaggio.getNomePersonaggio() + " è ancora stordito ("
                    + personaggio.getTurniStordito() + " turni rimanenti)");

            if (personaggio.getTurniStordito() == 0 && "STORDITO".equalsIgnoreCase(personaggio.getStatoPersonaggio())) {
                personaggio.setStatoPersonaggio("NORMALE");
                System.out.println(personaggio.getNomePersonaggio() + " non è più stordito.");
            }
        }
    }

    @Override
    public void scegliAzione(Personaggio personaggio) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("\n===== TURNO DI " + personaggio.getNomePersonaggio() + " =====");

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

        // 3 scelta dell'azione
        System.out.println("\nCosa vuoi fare?");
        System.out.println("1) Fare un evento");
        System.out.println("2) Prendere un oggetto");
        if (ciSonoEventi && ciSonoOggetti) {
            System.out.println("3) Fare un evento E prendere un oggetto");
        }
        System.out.println("4) usare un oggetto dallo zaino");
        System.out.println("0) Passa il turno");

        int scelta = Integer.parseInt(scanner.nextLine());

        switch (scelta) {
            case 1:
                if (ciSonoEventi) {
                    mostraEventi(eventi);
                }
                eseguiEvento(personaggio, stanzaCorrente, eventi, scanner);
                break;
            case 2:
                if (ciSonoOggetti) {
                    mostraOggetti(oggetti);
                }
                raccogliUnOggetto(personaggio, stanzaCorrente, oggetti, scanner);
                break;
            case 3:
                if (ciSonoEventi && ciSonoOggetti) {
                    mostraEventi(eventi);
                }
                mostraOggetti(oggetti);
                eseguiEvento(personaggio, stanzaCorrente, eventi, scanner);
                raccogliUnOggetto(personaggio, stanzaCorrente, oggetti, scanner);
                break;
            case 4:
                gestisciUsoOggettoDaZaino(personaggio, scanner);
                System.out.println("Oggetto usato dallo zaino.");
                break;
            case 0:
                System.out.println("Turno terminato.");
                break;
            default:
                System.out.println("Scelta non valida.");
                break;
        }

        terminaTurnoCorrente(personaggio); // da  implementare

        System.out.println("===== FINE TURNO DI " + personaggio.getNomePersonaggio() + " =====");
    }

    /// mostra gli eventi nella stanza 
    private void mostraEventi(List<Evento> eventi) {
        System.out.println("\nEventi disponibili:");
        for (int i = 0; i < eventi.size(); i++) {
            Evento e = eventi.get(i);
            System.out.println((i + 1) + ") " + e.getDescrizione());
        }
    }
    // mostra gli oggetti nella stanza 

    private void mostraOggetti(List<Oggetto> oggetti) {
        System.out.println("\nOggetti presenti nella stanza:");
        for (int i = 0; i < oggetti.size(); i++) {
            Oggetto o = oggetti.get(i);
            System.out.println((i + 1) + ") " + o.getNome());
        }
    }

    // 🔧 Movimento gestito tramite input
    public void gestisciMovimento(Personaggio personaggio, Scanner scanner) {

        System.out.println("Direzioni disponibili: N, S, E, O");
        String dir = scanner.nextLine().trim().toUpperCase();

        Direzione direzione = Direzione.fromString(dir);

        if (direzione == null) {
            System.out.println("Direzione non valida.");
            return;
        }

        boolean mosso = giocoService.muoviPersonaggio(personaggio, direzione);

        if (mosso) {
            System.out.println("Ti sei mosso verso " + direzione);
        } else {
            System.out.println("Non puoi muoverti in quella direzione.");
        }
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
        if (e instanceof Mostro mostro) {
            System.out.println("Stai per affrontare il mostro: " + mostro.getNomeMostro());
            gestisciUsoOggettoDaZaino(personaggio, scanner);
            combattimentoService.iniziaCombattimento(personaggio, mostro, stanza);
        }
        eventoService.eseguiEventiInStanza(personaggio, stanza);
        eventi.remove(index);
    }

    //  Metodo: raccoglie UN oggetto scelto
    public void raccogliUnOggetto(Personaggio personaggio, Stanza stanza, List<Oggetto> oggetti, Scanner scanner) {

        System.out.println("Scegli l'oggetto da prendere:");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index < 0 || index >= oggetti.size()) {
            System.out.println("Indice non valido.");
            return;
        }

        Oggetto o = oggetti.get(index);
        boolean ok = personaggio.raccogliereOggetto(personaggio, o);

        if (!ok) {
            System.out.println("Non puoi raccogliere l'oggetto.");
        }
    }

    //metodo nuovo 
    public void gestisciUsoOggettoDaZaino(Personaggio personaggio, Scanner scanner) {

        Zaino zaino = personaggio.getZaino();
        if (zaino == null || zaino.getListaOggetti().isEmpty()) {
            System.out.println("Lo zaino è vuoto.");
            return;
        }

        List<Oggetto> inventario = zaino.getListaOggetti();

        System.out.println("\n--- Zaino ---");
        for (int i = 0; i < inventario.size(); i++) {
            System.out.println((i + 1) + ") " + inventario.get(i).getNome());
        }
        System.out.println("0) Annulla");

        System.out.print("Scegli un oggetto da usare: ");
        int scelta;

        try {
            scelta = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Input non valido.");
            return;
        }

        if (scelta == 0) {
            System.out.println("Hai annullato.");
            return;
        }

        if (scelta < 1 || scelta > inventario.size()) {
            System.out.println("Scelta non valida.");
            return;
        }

        Oggetto oggetto = inventario.get(scelta - 1);

        boolean ok = personaggio.usaOggetto(personaggio, oggetto);

        if (ok) {
            System.out.println("Hai usato: " + oggetto.getNome());
        } else {
            System.out.println("Non puoi usare questo oggetto.");
        }
    }

    public void esploraStanza(Personaggio personaggio) {

        Stanza stanza = personaggio.getPosizioneCorrente();
        if (stanza == null) {
            return;
        }

        System.out.println("\n Stanza: " + stanza.getNomeStanza());

        // Mostra oggetti visibili
        if (!stanza.getOggettiPresenti().isEmpty()) {
            System.out.println("Oggetti trovati:");
            stanza.getOggettiPresenti().forEach(o -> System.out.println(" - " + o.getNome()));
        } else {
            System.out.println(" Nessun oggetto nella stanza.");
        }

        // Gestione eventi
        if (!stanza.getListaEventiAttivi().isEmpty()) {
            System.out.println(" Eventi attivi:");
            for (Evento e : stanza.getListaEventiAttivi()) {
                eventoService.attivaEvento(personaggio, e);
            }
        } else {
            System.out.println(" La stanza è tranquilla...");
        }

        // Segna la stanza come visitata
        stanza.setStatoS(StatoStanza.VISITATA);
    }

}
