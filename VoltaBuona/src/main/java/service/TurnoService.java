package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.Arciere;
import domain.Evento;
import domain.Mostro;
import domain.NPC;
import domain.Oggetto;
import domain.Personaggio;
import domain.Stanza;
import domain.Trappola;
import domain.Zaino;
import service.impl.ArciereServiceImpl;
import service.impl.MostroServiceImpl;
import service.impl.NPCServiceImpl;
import service.impl.PassaggioSegretoServiceImpl;
import service.impl.RandomSingleton;
import service.impl.ScannerSingleton;
import service.impl.TrappolaServiceImpl;
import util.ANSI;

public class TurnoService {

    private GiocoService giocoService;
    private DungeonFactory dungeonFactory;
    private PersonaggioService personaggioService;
    private EventoService eventoService;
    private List<String> ordineTurno = new ArrayList<>();
    private List<Personaggio> ordineIniziativa = null;
    private final RandomSingleton randomGenerale = RandomSingleton.getInstance();
    private final ScannerSingleton scannerGenerale = ScannerSingleton.getInstance();

    public TurnoService(GiocoService giocoService,
            PersonaggioService personaggioService,
            EventoService eventoService) {
        this.giocoService = giocoService;
        this.personaggioService = personaggioService;
        this.eventoService = eventoService;
    }

    public TurnoService(DungeonFactory dungeonFactory) {
        this.dungeonFactory = dungeonFactory;
    }

    public TurnoService(PersonaggioService personaggioService) {
        this.personaggioService = personaggioService;
    }

    public void setGiocoService(GiocoService giocoService) {
        this.giocoService = giocoService;
    }

    public void setDungeonFactory(DungeonFactory dungeonFactory) {
        this.dungeonFactory = dungeonFactory;
    }

    public TurnoService() {
        return;
    }

    /**
     * Calcola l'ordine di iniziativa dei giocatori in base ad un tiro randomico (d20).
     * Versione senza parametri: avvisa e restituisce lista vuota.
     */
  public List<Personaggio> calcolaOrdineIniziativa(List<Personaggio> partecipanti) {
    Map<Personaggio, Integer> iniziative = new HashMap<>();

    for (Personaggio p : partecipanti) {
        iniziative.put(p, randomGenerale.prossimoNumero(1, 20));
    }

    List<Personaggio> ordine = new ArrayList<>(partecipanti);
    ordine.sort((a, b) -> Integer.compare(iniziative.get(b), iniziative.get(a)));

    System.out.println("\nOrdine di iniziativa: Turno Service");
    for (Personaggio p : ordine) {
        System.out.println(" - " + p.getNomePersonaggio() + " (iniz.: " + iniziative.get(p) + ")");
    }

    return ordine;
} 
    



    /**
     * @param partecipanti
     *
     */

    public <T extends Personaggio> void iniziaNuovoTurno(List<T> partecipanti) {
        if (partecipanti == null || partecipanti.isEmpty()) {
            System.out.println("Nessun partecipante al turno.");
            return;
        }
         dungeonFactory.stampaMappa();
         //riga 116 cambiata ricontrollare
        // Calcola l'ordine di iniziativa solo se non è già stato calcolato in partita
        if (this.ordineIniziativa == null || this.ordineIniziativa.isEmpty()) {
            this.ordineIniziativa = calcolaOrdineIniziativa((List<Personaggio>) partecipanti);
        }

        List<T> ordine = (List<T>) new ArrayList<>(this.ordineIniziativa);

        for (Personaggio personaggio : ordine) {
            if (personaggio == null) {
                continue;
            }
            if (personaggio.èMorto(personaggio)) {
                System.out.println(personaggio.getNomePersonaggio() + " è morto. Rimosso dalla partita.");

                partecipanti.remove(personaggio);
                ordineTurno.remove(personaggio);
                if (this.ordineIniziativa != null) this.ordineIniziativa.remove(personaggio);
                continue;
            }
            System.out.println("DEBUG " + personaggio.getNomePersonaggio()
    + " class=" + personaggio.getClass().getSimpleName()
    + " stanza=" + (personaggio.getPosizioneCorrente() == null ? "NULL" : personaggio.getPosizioneCorrente().getId()));

            Stanza stanza = personaggio.getPosizioneCorrente();
            if (stanza == null) {
                System.out.println("Posizione del personaggio non definita.");
                continue;
            }

            ///testare
            personaggio.calcolaProtezione();

            // Stampa informazioni dello stato all'inizio del turno
            System.out.println("\nTurno di: " + personaggio.getNomePersonaggio());
            System.out.println("Punti vita: " + personaggio.getPuntiVita() + " | Difesa: " + personaggio.getPuntiDifesa() + " | Punti Mana: " + personaggio.getPuntiMana() + " | Attacco: " + personaggio.getAttacco() + " | Stato: " + personaggio.getStatoPersonaggio() + "\n" + " | Turni avvelenato: " + personaggio.getTurniAvvelenato() + "Turni stordito: " + personaggio.getTurniStordito() + " | Salto turno rimanenti: " + personaggio.getTurniDaSaltare() + "\n" + " | Portafoglio: " + personaggio.getPortafoglioPersonaggio() + "\n"
                    + "Livello: " + personaggio.getLivello() + " | Esperienza: " + personaggio.getEsperienza() + "\n");

            // Se il personaggio deve saltare il turno, consumiamo il turno e si passa al prossimo giocatore
            if (personaggio.consumaSaltoTurno()) {
                System.out.println(personaggio.getNomePersonaggio() + " salta questo turno.");
                terminaTurnoCorrente(personaggio);
                continue;
            }

            String nome = personaggio.getNomePersonaggio();
            if (nome != null && (nome.startsWith("BOT_") || nome.startsWith("Bot-") || nome.toLowerCase().contains("bot"))) {
                gestisciMovimento(personaggio);
                stanza.setStatoStanza(true);
                scegliAzione(personaggio);
                System.out.println("Fine turno di: " + personaggio.getNomePersonaggio());
                continue;
            }

        
            gestisciMovimento(personaggio);

            stanza.setStatoStanza(true);
            // 3) passo : scegliere azione da fare.
            scegliAzione(personaggio);
            System.out.println("Fine turno di: " + personaggio.getNomePersonaggio());

        }
    }

    
    public void terminaTurnoCorrente(Personaggio personaggio) {
        if (personaggio == null) {
            return;
        }

        // Se è morto, rimuoviamo dall'ordine dei giocatori
        if (personaggio.èMorto(personaggio)) {
            ordineTurno.remove(personaggio);
            return;
        }

        // Applica gli effetti di fine turno solo al personaggio passato
        aggiornaEffettiFineTurno(personaggio);
    }

    public void aggiornaEffettiFineTurno(Personaggio personaggio) {
        String stato = personaggio.getStatoPersonaggio();
        if (stato == null || stato.equals("NESSUN_EFFETTO")) {
            System.out.println(personaggio.getNomePersonaggio() + " non ha effetti attivi.");
            return;
        }
        if (personaggio.getTurniAvvelenato() > 0 || personaggio.getTurniStordito() > 0) {
            EffettoService.applicaEffettiFineTurno(personaggio);
        }

    }

    
    public void scegliAzione(Personaggio personaggio) {
        Stanza stanzaCorrente = personaggio.getPosizioneCorrente();
        System.out.println("Ti trovi in: " + stanzaCorrente.getId());
        // 2 esplorazione: mostro eventi e oggetti
        List<Evento> eventi = stanzaCorrente.getListaEventiAttivi();
        List<Oggetto> oggetti = stanzaCorrente.getOggettiPresenti();

        boolean ciSonoEventi = eventi != null && !eventi.isEmpty();
        boolean ciSonoOggetti = oggetti != null && !oggetti.isEmpty();

        System.out.println("\nEsplorazione stanza:");

        if (!stanzaCorrente.getOggettiPresenti().isEmpty()) {
            mostraOggetti(stanzaCorrente.getOggettiPresenti());
        } else {
            System.out.println("Nessun oggetto nella stanza.");
        }

        if (!stanzaCorrente.getListaEventiAttivi().isEmpty()) {
        
            mostraEventi(personaggio, eventi);
        } else {
            System.out.println("La stanza è tranquilla,non ci sono eventi!");
        }

        // 3 scelta dell'azione
        System.out.println("\nCosa vuoi fare?" + ANSI.BOLD + ANSI.RED + "Scorri in su il terminale per capire se sei caduto in una trappola!" + ANSI.RESET);
        System.out.println("1) Fare un evento");
        System.out.println("2) Prendere un oggetto");
        if (ciSonoOggetti && ciSonoEventi) {
            System.out.println("3) Prendere un oggetto e fare un evento");
        }
        System.out.println("4) Usare un oggetto dallo zaino");
        System.out.println("5) Controlla il portafoglio");
        if (personaggio instanceof Arciere) {
            System.out.println("6) Sei un Arciere, puoi attaccare mostri in stanze adiacenti!");
        }
        System.out.println("0) Passa il turno");

        int scelta;
        String nomePersonaggio = personaggio.getNomePersonaggio();
        boolean isBot = nomePersonaggio != null && (nomePersonaggio.startsWith("BOT_") || nomePersonaggio.startsWith("Bot-") || nomePersonaggio.toLowerCase().contains("bot"));
        if (isBot) {
            scelta = randomGenerale.prossimoNumero(0, 5);
            System.out.println(personaggio.getNomePersonaggio() + " (bot) sceglie: " + scelta);
        } else {
            scelta = scannerGenerale.leggiInteroIntervallo(0, 5);
        }

        switch (scelta) {
            case 1:
                if (ciSonoEventi) {
                    if (eseguiSingoloEvento(personaggio, stanzaCorrente, eventi)) {

                        return; // turno consumato

                    }
                }
                break;
            case 2:
                if (ciSonoOggetti) {
                    mostraOggetti(oggetti);
                    raccogliUnOggetto(personaggio, stanzaCorrente, oggetti);
                }
                break;
            case 3:
                if (ciSonoOggetti && ciSonoEventi) {
                    mostraOggetti(oggetti);
                    raccogliUnOggetto(personaggio, stanzaCorrente, oggetti);
                    // dopo aver preso un oggetto, se si vuole eseguire un evento, consideriamo solo gli eventi di stanza
                    if (eseguiSingoloEvento(personaggio, stanzaCorrente, eventi)) {
                        return;
                    }
                }
                break;
            case 4:

                gestisciUsoOggettoDaZaino(personaggio);
                break;
            case 5:
                personaggio.getPortafoglioPersonaggio();
                System.out.println("saldo attuale :" + personaggio.getPortafoglioPersonaggio());
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


    // Restituisce il service corretto per il tipo di evento
    public EventoService servicePerEvento(Evento evento) {
        if (evento == null) {
            return eventoService;
        }
        if (evento instanceof domain.Mostro) {
            return new MostroServiceImpl();
        }
        if (evento instanceof domain.Trappola) {
            return new TrappolaServiceImpl();
        }
        if (evento instanceof domain.PassaggioSegreto) {
            return new PassaggioSegretoServiceImpl();
        }
        if (evento instanceof domain.PersonaIncontrata) {
            return new NPCServiceImpl();
        }
        return eventoService = new PassaggioSegretoServiceImpl();
    }

    /**
     * @param personaggio
     * @param stanza
     * @param eventi
     */
    public boolean eseguiSingoloEvento(Personaggio personaggio, Stanza stanza, List<Evento> eventi) {

        // 1) Eventi visibili (escluse le trappole).
        List<Evento> eventiVisibili = new ArrayList<>();
        if (eventi != null) {
            for (Evento evento : eventi) {
                if (evento == null || evento instanceof Trappola) {
                    continue;
                }
                eventiVisibili.add(evento);
            }

         
            // 2) Mostri adiacenti per Arciere per l'attacco a distanza (opzionale)
            List<Evento> eventiArciere = new ArrayList<>();
            if (personaggio instanceof Arciere arciere) {
                Map<String, Mostro> adiacenti = new ArciereServiceImpl().trovaMostriAdiacenti(arciere.getPosizioneCorrente());
                for (Mostro mostro : adiacenti.values()) {
                    if (mostro != null) {
                        eventiArciere.add(mostro);
                    }
                }
            }

            // 3) Uniamo 1 e 2 senza duplicati
            List<Evento> risultatoEventi = new ArrayList<>();
            // Aggiungi prima i mostri adiacenti (se richiesto)
            for (Evento ea : eventiArciere) {
                risultatoEventi.add(ea);
            }

            // Aggiungi gli eventi visibili evitando duplicati di mostri (confronto per id)
            for (Evento ev : eventiVisibili) {
                boolean giaPresente = false;
                if (ev instanceof Mostro mv) {
                    for (Evento p : risultatoEventi) {
                        if (p instanceof Mostro mp && mp.getId() == mv.getId()) {
                            giaPresente = true;
                            break;
                        }
                    }
                    if (giaPresente) {
                        continue;
                    }
                }
                // Se non è già presente (o non è un mostro duplicato), aggiungilo
                risultatoEventi.add(ev);
            }

            // Mostra la lista finale di eventi (inclusi i mostri adiacenti per l'arciere)
            System.out.println("\nEventi disponibili:");
            for (int i = 0; i < risultatoEventi.size(); i++) {
                Evento e = risultatoEventi.get(i);
                if (e == null) {
                    System.out.println((i + 1) + ") Evento null");
                    continue;
                }
                if (e instanceof Mostro mostro) {
                    System.out.println((i + 1) + ") Mostro: " + mostro.getNomeMostro() + " (stanza " + mostro.getPosizioneCorrente() + ")");
                    continue;
                }
                if (e instanceof NPC npc) {
                    System.out.println((i + 1) + ") NPC: " + npc.getNomeNPC());
                    continue;
                }
                System.out.println((i + 1) + ") " + e.getNomeEvento());
            }

            if (risultatoEventi.isEmpty()) {
                System.out.println("Nessun evento disponibile.");
                return false;
            }

            // 4) Mostra eventi
            System.out.print("Scegli l'evento da eseguire (0 = annulla): ");
            int scelta;
        String nomePersonaggio = personaggio.getNomePersonaggio();
        boolean isBot = nomePersonaggio != null && (nomePersonaggio.startsWith("BOT_") || nomePersonaggio.startsWith("Bot-") || nomePersonaggio.toLowerCase().contains("bot"));
        if (isBot) {
            scelta = randomGenerale.prossimoNumero(0, risultatoEventi.size())-1;
            if(scelta==0){
                terminaTurnoCorrente(personaggio);}
            System.out.println(personaggio.getNomePersonaggio() + " (bot) sceglie: " + (scelta+1));
        } else {
            scelta = scannerGenerale.leggiInteroIntervallo(0, risultatoEventi.size())-1;
        }

        if( scelta==0){terminaTurnoCorrente(personaggio);}

            if (scelta == -1) {
                return false;
            }

            Evento eventoScelto = risultatoEventi.get(scelta);

            if (personaggio instanceof Arciere arciere && eventoScelto instanceof Mostro mostro) {
                if (mostro.getPosizioneCorrente() != null
                        && !mostro.getPosizioneCorrente().equals(personaggio.getPosizioneCorrente())) {
                    new ArciereServiceImpl().attaccoDistanzaArciere(arciere, mostro);
                    terminaTurnoCorrente(personaggio);
                    return true;
                }
            }

            // 5) Attiva evento
            EventoService service = servicePerEvento(eventoScelto);
            boolean terminaTurno = service.attivaEvento(personaggio, eventoScelto);

            // 6) Gestione fine evento
            if (stanza != null && eventoScelto != null && eventoScelto != null) {
                if (eventoScelto.isFineEvento() || !eventoScelto.èRiutilizzabile()) {
                    eventoScelto.setFineEvento(true);
                    eventoScelto.setInizioEvento(false);
                }
            }

            if (terminaTurno) {
                terminaTurnoCorrente(personaggio);
            }

            return terminaTurno;
        }
        return false;
    }

    /// mostra gli eventi nella stanza 
   public void mostraEventi(Personaggio personaggio, List<Evento> eventi) {
        System.out.println("\nEventi disponibili:");
        int sceltaIndex = 1;
        Map<String, Mostro> mostriArciere = new HashMap<>();

        if (personaggio instanceof Arciere arciere) {
            Map<String, Mostro> mostriAdiacenti = new ArciereServiceImpl().trovaMostriAdiacenti(arciere.getPosizioneCorrente());
            for (Map.Entry<String, Mostro> mappa : mostriAdiacenti.entrySet()) {
                Mostro mostro = mappa.getValue();

                if (mostro == null) {
                    continue;
                }

                mostriArciere.put(String.valueOf(mostro.getId()), mostro);
                System.out.println(sceltaIndex + ") Mostro avvistato in " + mappa.getKey()
                        + " (stanza " + mostro.getPosizioneCorrente() + " " + mostro.getNomeMostro() + ")");
                sceltaIndex++;
                continue;
            }
        }

        for (Evento e : eventi) {
            if (e == null) {
                System.out.println(sceltaIndex + ") Evento null");
                sceltaIndex++;
                continue;
            }
            if (e instanceof Mostro mostro) {
                if (mostriArciere.containsKey(String.valueOf(mostro.getId()))) {
                    continue;
                }
                System.out.println(sceltaIndex + ") Hai incontrato un mostro: " + mostro.getNomeMostro());
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

    }
    // mostra gli oggetti nella stanza 

    public void mostraOggetti(List<Oggetto> oggetti) {
        System.out.println("\nOggetti presenti nella stanza:");

        for (int i = 0; i < oggetti.size(); i++) {
            Oggetto oggetto = oggetti.get(i);
            String nome = oggetto.getNome();
            System.out.println((i + 1) + ") " + nome);
        }
    }

    public void gestisciMovimento(Personaggio personaggio) {

        Stanza stanza = personaggio.getPosizioneCorrente();
        if (stanza == null) {
            System.out.println("Posizione del personaggio non definita.");
            return;
        }

        Map<String, Stanza> adiacenti = stanza.getStanzaAdiacente();
        if (adiacenti == null || adiacenti.isEmpty()) {
            System.out.println("Nessuna direzione disponibile da questa stanza.");
            return;
        }

        System.out.println("Direzioni disponibili:");
        for (Map.Entry<String, Stanza> entry : adiacenti.entrySet()) {
            String direzione = entry.getKey();
            Stanza s = entry.getValue();

            System.out.println(direzione + " -> stanza id " + s.getId());
        }

        System.out.print("Scegli una direzione (nome direzione)");
        String input;
        String nomePersonaggio = personaggio.getNomePersonaggio();
        boolean isBot = nomePersonaggio != null && (nomePersonaggio.startsWith("BOT_") || nomePersonaggio.startsWith("Bot-") || nomePersonaggio.toLowerCase().contains("bot"));
        if (isBot) {
            input = randomGenerale.scegliRandomicamente(new ArrayList<>(adiacenti.keySet()));
            System.out.println(personaggio.getNomePersonaggio() + " (bot) sceglie direzione: " + input);
        } else {
            input = scannerGenerale.leggiLinea();
        }

        Direzione direzione = Direzione.fromString(input);

        
        boolean mosso = false;
        if (direzione != null) {
            mosso = giocoService.muoviPersonaggio(personaggio, direzione);
            System.out.println("Hai scelto la direzione: " + direzione);
        } else {
            System.out.println("La stanza è bloccata,ti serve una chiave (con lo stesso id ) per aprirla");
            return;
        }
    }

    
    //  Metodo: raccoglie UN oggetto scelto

    public void raccogliUnOggetto(Personaggio personaggio, Stanza stanza, List<Oggetto> oggetti) {

        System.out.println("Scegli l'oggetto da prendere:");
             int indice;
        String nomePersonaggio = personaggio.getNomePersonaggio();
        boolean isBot = nomePersonaggio != null && (nomePersonaggio.startsWith("BOT_") || nomePersonaggio.startsWith("Bot-") || nomePersonaggio.toLowerCase().contains("bot"));
        if (isBot) {
            // scegli un indice tra 1 e oggetti.size(), poi convertilo a 0-based
            indice = randomGenerale.prossimoNumero(1, oggetti.size()) - 1;
            System.out.println(personaggio.getNomePersonaggio() + " (bot) sceglie: " + (indice + 1));
        } else {
            indice = scannerGenerale.leggiInteroIntervallo(1, oggetti.size())-1;
        }

        Oggetto oggetto = oggetti.get(indice);
        boolean puoRaccogliere = personaggio.raccogliereOggetto(oggetto);
        if (!puoRaccogliere) {
            System.out.println("Non puoi raccogliere l'oggetto.");
        }
    }

    //metodo nuovo 
    public  boolean gestisciUsoOggettoDaZaino(Personaggio personaggio) {

        Zaino zaino = personaggio.getZaino();
        if (zaino == null) {
            System.out.println("Lo zaino è vuoto.");
            return false;
        }
        List<Oggetto> inventario = zaino.getListaOggetti();
        if (inventario == null || inventario.isEmpty()) {
            System.out.println("Lo zaino è vuoto.");
            return false;
        }

        ////stampa zaino
        System.out.println("\nZaino");
        for (int i = 0; i < inventario.size(); i++) {
            System.out.println((i + 1) + ") " + inventario.get(i).getNome());
        }
        System.out.println("0) Annulla");

        System.out.print("Scegli un oggetto da usare: ");
             int scelta;
        String nomePersonaggio = personaggio.getNomePersonaggio();
        boolean isBot = nomePersonaggio != null && (nomePersonaggio.startsWith("BOT_") || nomePersonaggio.startsWith("Bot-") || nomePersonaggio.toLowerCase().contains("bot"));
        if (isBot) {
            scelta = randomGenerale.prossimoNumero(1, inventario.size())-1;
            System.out.println(personaggio.getNomePersonaggio() + " (bot) sceglie: " + (scelta + 1));
        } else {
            scelta = scannerGenerale.leggiInteroIntervallo(1,inventario.size())-1;
        }

        
        if (scelta == 0) {
            System.out.println("Hai annullato.");
            return false;
        }

        Oggetto oggetto = inventario.get(scelta - 1);

        boolean usareOggetto = personaggio.usaOggetto(personaggio, oggetto);

        if (usareOggetto) {
            System.out.println("Hai usato: " + oggetto.getNome());
        } else {
            System.out.println("Non puoi usare questo oggetto.");
        }
        return true;
    }

}
