package service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import domain.Arciere;
import domain.Evento;
import domain.Mostro;
import domain.NPC;
import domain.Oggetto;
import domain.Personaggio;
import domain.Stanza;
import domain.Trappola;
import domain.Zaino;
import service.Direzione;
import service.EffettoService;
import service.EventoService;
import service.GiocoService;
import service.PersonaggioService;
import service.TurnoService;
import util.ANSI;

public class TurnoServiceImpl implements TurnoService {

    private Personaggio personaggio;
    private GiocoService giocoService;
    private PersonaggioService personaggioService;
    private EventoService eventoService;
    // stanzaCorrente, oggetti ed eventi devono essere recuperati dal Personaggio al momento del turno
    private List<Personaggio> ordineTurno = new ArrayList<>();
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

    public TurnoServiceImpl() {
        return;
    }

    @Override
    public List<Personaggio> calcolaOrdineIniziativa(List<Personaggio> partecipanti) {

        ordineTurno.clear();

        // Mappa: Personaggio → tiro dado
        Map<Personaggio, Integer> tiri = new HashMap<>();

        // 1 Ogni personaggio tira il dado
        // Ogni personaggio tira il dado (escludo i morti)
        for (Personaggio p : partecipanti) {
            if (p == null || p.èMorto(p)) {
                continue;
            }
            int tiro = random.nextInt(20) + 1; // dado d20
            tiri.put(p, tiro);
        }

        // 2 Costruzione ordine turno SENZA sort
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

        return new ArrayList<>(ordineTurno);
    }

    /**
     * Inizia un nuovo round di turni: calcola ordine e per ogni personaggio
     * mostra prima eventi e oggetti presenti nella stanza e poi chiede l'azione
     * tramite l'input fornito dallo scanner.
     */
    public void iniziaNuovoTurno(List<Personaggio> partecipanti, Scanner scanner) {
        if (partecipanti == null || partecipanti.isEmpty()) {
            System.out.println("Nessun partecipante al turno.");
            return;
        }

        List<Personaggio> ordine = calcolaOrdineIniziativa(partecipanti);
        //fino a qui è giusto 

        for (Personaggio p : List.copyOf(ordine)) {
            if (p == null) {
                continue;
            }
            if (p.èMorto(p)) {
                System.out.println(p.getNomePersonaggio() + " è morto. Rimosso dalla partita.");
                // rimuovo il personaggio dalle liste per non ricomparire nei turni futuri
                try {
                    partecipanti.remove(p);
                } catch (Exception ignored) {
                }
                ordineTurno.remove(p);
                continue;
            }

            //1
            //   System.out.println("\n INIZIO TURNO DI " + p.getNomePersonaggio() );
            Stanza stanza = p.getPosizioneCorrente();
            if (stanza == null) {
                System.out.println("Posizione del personaggio non definita. Saltando.");
                continue;
            }

            // Inizio turno: gestioni generali del personaggio (es. consumo protezione)
            p.calcolaProtezione();

            // Stampa informazioni dello stato all'inizio del turno
            System.out.println("\n===== TURNO DI " + p.getNomePersonaggio() + " =====");
            System.out.println("Punti vita: " + p.getPuntiVita() + " | Difesa: " + p.getPuntiDifesa()+" | Punti Mana: " + p.getPuntiMana() + " | Attacco: " + p.getAttacco()
                    + " | Stato: " + p.getStatoPersonaggio()+ " | Turni avvelenato: " + p.getTurniAvvelenato()+ "Turni stordito: " + p.getTurniStordito()+ " | Salto turno rimanenti: " + p.getTurniDaSaltare()+ "\n"+" | Portafoglio: " + p.getPortafoglioPersonaggio()+ "\n"
                +"Livello: " + p.getLivello() + " | Esperienza: " + p.getEsperienza() + "\n");

            // Se il personaggio deve saltare il turno, consumiamo il salto e passiamo oltre
            if (p.consumaSaltoTurno()) {
                System.out.println(p.getNomePersonaggio() + " salta questo turno.");
                // applichiamo comunque eventuali effetti di fine turno
                terminaTurnoCorrente(p);
                continue;
            }

            String nome = p.getNomePersonaggio();
            if (nome != null && (nome.startsWith("BOT_") || nome.startsWith("Bot-") || nome.toLowerCase().contains("bot"))) {
                ComputerServiceImpl computerService = new ComputerServiceImpl();
                computerService.agisciAutomatico(p, this, giocoService, eventoService, personaggioService);
                continue;
            }

            // 1) passo : chiedere al personaggio se vuole muoversi in un 'altra stanza
            gestisciMovimento(p, scanner);
            // 2) passo : esplorare la stanza (mostra eventi e oggetti)
            boolean eventoHaConsumtoTurno = esploraStanza(p);
            if (eventoHaConsumtoTurno) {
                System.out.println("Turno consumato da un evento.");
                continue;
            }
            // 3) passo : scegliere azione (fare evento, prendere oggetto, usare oggetto)
            scegliAzione(p, scanner);
            System.out.println("FINE TURNO DI " + p.getNomePersonaggio());

        }
    }

    public void terminaTurnoCorrente(Personaggio personaggio) {
        if (personaggio == null) {
            return;
        }

        // Se è morto, rimuoviamo silenciosamente dall'ordine interno
        if (personaggio.èMorto(personaggio)) {
            ordineTurno.remove(personaggio);
            return;
        }

        // Applica gli effetti di fine turno solo al personaggio passato
        aggiornaEffettiFineTurno(personaggio);
    }

    public void aggiornaEffettiFineTurno(Personaggio personaggio) {
        if (personaggio.getStatoPersonaggio().equals("NESSUN_EFFETTO")) {
            System.out.println(personaggio.getNomePersonaggio() + " non ha effetti attivi.");
            return;
        }
        if (personaggio.getTurniAvvelenato() > 0) {
            // delega l'applicazione del danno periodico alle trappole
            EffettoService.applicaEffettiFineTurno(personaggio);
        }

        //  STORDIMENTO: penalizza per N turni (es. non può attaccare)
        if (personaggio.getTurniStordito() > 0) {
            EffettoService.applicaEffettiFineTurno(personaggio);
        }

    }

    // N.B. mantenuta solo l'implementazione che accetta uno Scanner esterno
    @Override
    public void scegliAzione(Personaggio personaggio, Scanner scanner) {

        //1 System.out.println("\n===== TURNO DI " + personaggio.getNomePersonaggio() + " =====");
        Stanza stanzaCorrente = personaggio.getPosizioneCorrente();
        //   System.out.println("Ti trovi in: " + stanzaCorrente.getNomeStanza());
        // 2 esplorazione: mostro eventi e oggetti
        List<Evento> eventi = stanzaCorrente.getListaEventiAttivi();
        List<Oggetto> oggetti = stanzaCorrente.getOggettiPresenti();

        boolean ciSonoEventi = eventi != null && !eventi.isEmpty();
        boolean ciSonoOggetti = oggetti != null && !oggetti.isEmpty();

        System.out.println("\n--- Esplorazione stanza ---");
        //System.out.println(ANSI.BOLD + ANSI.RED + "Scorri in su il terminale per capire se sei caduto in una trappola!" + ANSI.RESET);

        if (!stanzaCorrente.getOggettiPresenti().isEmpty()) {
            // System.out.println("Oggetti trovati:");
            mostraOggetti(stanzaCorrente.getOggettiPresenti());
            //  stanza.getOggettiPresenti().forEach(o -> System.out.println(" - " + o.getNome()));
        } else {
            System.out.println(" Nessun oggetto nella stanza.");
        }

        if (eventi != null && !eventi.isEmpty()) {
            //   System.out.println(" Eventi presenti:");
            mostraEventi(personaggio, eventi);
        } else {
            System.out.println(" La stanza è tranquilla...");
        }

        // 3 scelta dell'azione
        System.out.println("\nCosa vuoi fare?" + ANSI.BOLD + ANSI.RED + "Scorri in su il terminale per capire se sei caduto in una trappola!" + ANSI.RESET);
        System.out.println("1) Fare un evento");
        System.out.println("2) Prendere un oggetto");
        if (ciSonoOggetti && ciSonoEventi) {
            System.out.println("3) Prendere un oggetto e fare un evento");
        }
        System.out.println("4) usare un oggetto dallo zaino");
        System.out.println("5) Controlla il portafoglio");
        if (personaggio instanceof Arciere) {
            System.out.println("6) Sei un Arciere, puoi attaccare mostri in stanze adiacenti!");
        }
        System.out.println("0) Passa il turno");

        int scelta;
        try {
            scelta = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Scelta non valida.");
            return;
        }

        switch (scelta) {
            case 1:
                if (ciSonoEventi) {
                    if (eseguiSingoloEvento(personaggio, stanzaCorrente, eventi, scanner)) {
                        return; // turno consumato
                    }
                }
                break;
            case 2:
                if (ciSonoOggetti) {
                    mostraOggetti(oggetti);
                    raccogliUnOggetto(personaggio, stanzaCorrente, oggetti, scanner);
                }
                break;
            case 3:
                if (ciSonoOggetti && ciSonoEventi) {
                    mostraOggetti(oggetti);
                    raccogliUnOggetto(personaggio, stanzaCorrente, oggetti, scanner);
                    if (eseguiSingoloEvento(personaggio, stanzaCorrente, eventi, scanner)) // poi prendere oggetto
                    {
                        return; // turno consumato
                    }
                }
                break;
            case 4:
                gestisciUsoOggettoDaZaino(personaggio, scanner);
                break;
            case 5:
                personaggio.getPortafoglioPersonaggio();
                System.out.println("saldo attuale :" + personaggio.getPortafoglioPersonaggio());
                break;
            case 6:
                if (personaggio instanceof Arciere arciere) {
                    Map<String, Mostro> mostriAdiacenti = new ArciereServiceImpl().trovaMostriAdiacenti(personaggio.getPosizioneCorrente());
                    if (mostriAdiacenti.isEmpty()) {
                        System.out.println("Non ci sono mostri colpibili nelle stanze adiacenti.");
                        break;
                    } else {
                        // altrimenti fai scegliere quale colpire
                        System.out.println("Scegli il bersaglio:");
                        List<String> keys = new java.util.ArrayList<>(mostriAdiacenti.keySet());
                        for (int i = 0; i < keys.size(); i++) {
                            Mostro m = mostriAdiacenti.get(keys.get(i));
                            System.out.println(i + ") " + keys.get(i) + " -> " + m.getNomeMostro() + " (HP: " + m.getPuntiVitaMostro() + ")");
                        }
                        System.out.print("Inserisci il numero del bersaglio: ");
                        int sceltaB;
                        try {
                            sceltaB = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Input non valido.");
                            break;
                        }
                        Mostro bersaglio = mostriAdiacenti.get(keys.get(sceltaB - 1));

                        ArciereServiceImpl arciereService = new ArciereServiceImpl();
                         arciereService.attaccoDistanzaArciere(arciere, bersaglio);
                    }

                    // fine turno immediata
                    terminaTurnoCorrente(personaggio);
                }
                break;
            case 0:
                System.out.println("Turno terminato.");
                break;
            default:
                System.out.println("Scelta non valida.");
                break;
        }

        terminaTurnoCorrente(personaggio);
    }

    //   System.out.println("FINE TURNO DI " + personaggio.getNomePersonaggio() );
    // restituisce il service corretto per il tipo di evento
    public EventoService servicePerEvento(Evento e) {
        if (e == null) {
            return eventoService;
        }
        if (e instanceof domain.Mostro) {
            return new MostroServiceImpl();
        }
        if (e instanceof domain.Trappola) {
            return new TrappolaServiceImpl();
        }
        if (e instanceof domain.PassaggioSegreto) {
            return new PassaggioSegretoServiceImpl();
        }
        if (e instanceof domain.PersonaIncontrata) {
            return new NPCServiceImpl();
        }
        return eventoService != null ? eventoService : new PassaggioSegretoServiceImpl();
    }

    public boolean eseguiSingoloEvento(Personaggio personaggio, Stanza stanza, List<Evento> eventi, Scanner scanner) {
        if (eventi == null || eventi.isEmpty()) {
            System.out.println("Nessun evento disponibile.");
            return false;
        }

        List<Evento> visibili = new ArrayList<>();
        for (Evento ev : eventi) {
            if (ev == null) {
                continue;
            }
            if (ev instanceof domain.Trappola) {
                continue;
            }
            visibili.add(ev);
        }

        List<Evento> display = new ArrayList<>();
        // aggiungo mostri adiacenti (per Arciere) evitando duplicati
        if (personaggio instanceof Arciere arciere) {
            Map<String, Mostro> adj = new ArciereServiceImpl().trovaMostriAdiacenti(arciere.getPosizioneCorrente());
            for (Mostro m : adj.values()) {
                if (m != null) {
                    display.add(m);
                }
            }
        }
        // aggiungo eventi visibili saltando eventuali mostri già inseriti (stesso id)
        for (Evento ev : visibili) {
            if (ev instanceof Mostro m) {
                boolean found = false;
                for (Evento d : display) {
                    if (d instanceof Mostro dm && dm.getId() == m.getId()) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    continue;
                }
            }
            display.add(ev);
        }

        mostraEventi(personaggio, display);

        System.out.print("Scegli l'evento da eseguire (0 = annulla): ");
        int index;
        try {
            index = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException ex) {
            System.out.println("Input non valido.");
            return false;
        }

        if (index < 0) {
            System.out.println("Operazione annullata.");
            return false;
        }
        if (index >= display.size()) {
            System.out.println("Indice non valido.");
            return false;
        }

        Evento e = display.get(index);
        EventoService svc = servicePerEvento(e);
        boolean termina = svc.attivaEvento(personaggio, e);

        try {
            if (e.isFineEvento() || !e.èRiutilizzabile()) {
                if (stanza != null) {
                    if (e instanceof Mostro m) {
                        if (m.èMortoilMostro()) {
                            stanza.rimuoviEvento(e);
                        } else {
                            e.setFineEvento(false);
                            e.setInizioEvento(true);
                        }
                    } else {
                        if (e.isFineEvento() || !e.èRiutilizzabile()) {
                            stanza.rimuoviEvento(e);
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }

        if (termina) {
            terminaTurnoCorrente(personaggio);
            return true;
        }
        return false;
    }

    /// mostra gli eventi nella stanza 
   public void mostraEventi(Personaggio personaggio, List<Evento> eventi) {
        System.out.println("\nEventi disponibili:");
        int sceltaIndex = 1;

        java.util.Set<Integer> printedIds = new java.util.HashSet<>();

        if (personaggio instanceof Arciere arciere) {
            Map<String, Mostro> adj = new ArciereServiceImpl().trovaMostriAdiacenti(arciere.getPosizioneCorrente());
            for (Map.Entry<String, Mostro> en : adj.entrySet()) {
                Mostro m = en.getValue();
                if (m == null) {
                    continue;
                }
                System.out.println(sceltaIndex + ") Mostro avvistato in " + en.getKey()
                        + " (stanza " + (m.getPosizioneCorrente() != null ? m.getPosizioneCorrente().getId() : "n/a")
                        + "): " + m.getNomeMostro());
                printedIds.add(m.getId());
                sceltaIndex++;
            }
        }

        for (Evento e : eventi) {
            if (e instanceof Mostro mostro) {
                if (printedIds.contains(mostro.getId())) {
                    continue; // evita duplicato
                }
                System.out.println(sceltaIndex + ") Hai incontrato un mostro: " + mostro.getNomeMostro());
                printedIds.add(mostro.getId());
                sceltaIndex++;
                continue;
            }
            if (e instanceof NPC npc) {
                System.out.println(sceltaIndex + ") Hai incontrato un NPC: " + npc.getNomeNPC());
                sceltaIndex++;
                continue;
            }
            if (e instanceof Trappola) {
                continue;
            }
            System.out.println(sceltaIndex + ") " + e.getNomeEvento());
            sceltaIndex++;
        }

        if (sceltaIndex == 1) {
            System.out.println("(Nessun evento selezionabile in questa stanza)");
        }
    }
    // mostra gli oggetti nella stanza 

    public void mostraOggetti(List<Oggetto> oggetti) {
        System.out.println("\nOggetti presenti nella stanza:");

        for (int i = 0; i < oggetti.size(); i++) {
            Oggetto o = oggetti.get(i);
            String nome = (o != null && o.getNome() != null) ? o.getNome() : "<oggetto null>";
            System.out.println((i + 1) + ") " + nome);
        }
    }

    public void gestisciMovimento(Personaggio personaggio, Scanner scanner) {

        Stanza stanza = personaggio.getPosizioneCorrente();
        if (stanza == null) {
            System.out.println("Posizione del personaggio non definita. Saltando movimento.");
            return;
        }

        Map<String, Stanza> adiacenti = stanza.getStanzaAdiacente();
        if (adiacenti == null || adiacenti.isEmpty()) {
            System.out.println("Nessuna direzione disponibile da questa stanza.");
            return;
        }

        System.out.println("Direzioni disponibili:");
        for (Map.Entry<String, Stanza> entry : adiacenti.entrySet()) {
            String nomeDir = entry.getKey();
            Stanza s = entry.getValue();
            String display = nomeDir.length() > 1 ? nomeDir : nomeDir;
            System.out.println(" - " + display + " -> stanza id " + (s != null ? s.getId() : "null"));
        }

        System.out.print("Scegli una direzione (lettera, nome o chiave segreta): ");
        String input = scanner.nextLine().trim().toUpperCase();

        // prova prima a interpretare come direzione standard
        Direzione direzione = Direzione.fromString(input);

        boolean mosso = false;
        if (direzione != null) {
            mosso = giocoService.muoviPersonaggio(personaggio, direzione);
        } else {
            // se non è una direzione standard, verifica se è una chiave personalizzata presente nella mappa
            if (adiacenti.containsKey(input)) {
                // usa il metodo specifico se disponibile (cast sicuro)
                if (giocoService instanceof service.impl.GiocoServiceImpl) {
                    mosso = ((service.impl.GiocoServiceImpl) giocoService).muoviPersonaggio(personaggio, Direzione.valueOf(input));
                } else {
                    System.out.println("Impossibile muoversi: servizio di gioco non supporta movimenti custom.");
                }
            } else {
                System.out.println("Direzione non valida.");
                return;
            }
        }

        if (mosso) {
            System.out.println("Ti sei mosso verso " + input);
        } else {
            System.out.println("Non puoi muoverti in quella direzione.");
        }
    }

    //  Metodo: raccoglie UN oggetto scelto
    @Override
    public void raccogliUnOggetto(Personaggio personaggio, Stanza stanza, List<Oggetto> oggetti, Scanner scanner) {

        System.out.println("Scegli l'oggetto da prendere:");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index < 0 || index >= oggetti.size()) {
            System.out.println("Indice non valido.");
            return;
        }

        Oggetto o = oggetti.get(index);
        boolean ok = personaggio.raccogliereOggetto( o);

        if (!ok) {
            System.out.println("Non puoi raccogliere l'oggetto.");
        }
    }

    //metodo nuovo 
    public boolean gestisciUsoOggettoDaZaino(Personaggio personaggio, Scanner scanner) {
        System.out.println("[DEBUG] gestisciUsoOggettoDaZaino called for: " + (personaggio != null ? personaggio.getNomePersonaggio() : "null"));
        Zaino zaino =  personaggio.getZaino();
        if (zaino == null) {
            System.out.println("[DEBUG] zaino == null");
            System.out.println("Lo zaino è vuoto.");
            return false;
        }

        List<Oggetto> inventario = zaino.getListaOggetti();
        if (inventario == null) {
            System.out.println("[DEBUG] zaino.getListaOggetti() == null");
            System.out.println("Lo zaino è vuoto.");
            return false;
        }

        if (inventario.isEmpty()) {
            System.out.println("[DEBUG] inventario.isEmpty() == true");
            System.out.println("Lo zaino è vuoto.");
            return false;
        }

        System.out.println("[DEBUG] inventario.size() = " + inventario.size());
        System.out.println("\n--- Zaino ---");
        for (int i = 0; i < inventario.size(); i++) {
            System.out.println((i + 1) + ") " + inventario.get(i).getNome());
        }
        System.out.println("0) Annulla");

        System.out.print("Scegli un oggetto da usare: ");
        int scelta;
        try {
            scelta = Integer.parseInt(scanner.nextLine());
            System.out.println("[DEBUG] scelta utente = " + scelta);
        } catch (NumberFormatException e) {
            System.out.println("Input non valido.");
            return false;
        }

        if (scelta == 0) {
            System.out.println("Hai annullato.");
            return false;
        }

        if (scelta < 1 || scelta > inventario.size()) {
            System.out.println("Scelta non valida.");
            return false;
        }

        Oggetto oggetto = inventario.get(scelta - 1);

        boolean ok = personaggio.usaOggetto(personaggio, oggetto);

        if (ok) {
            System.out.println("Hai usato: " + oggetto.getNome());
        } else {
            System.out.println("Non puoi usare questo oggetto.");
        }
        return true;
    }

    public boolean esploraStanza(Personaggio personaggio) {

        Stanza stanza = personaggio.getPosizioneCorrente();
        if (stanza == null) {
            return false;
        }

        System.out.println("\n Stanza: " + stanza.getId());

        // Segna la stanza come visitata
        stanza.setStatoStanza(true);
        return false;
    }

}
