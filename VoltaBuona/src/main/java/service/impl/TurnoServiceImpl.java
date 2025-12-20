package service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import domain.Evento;
import domain.Oggetto;
import domain.Personaggio;
import domain.Stanza;
import domain.Zaino;
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



    @Override
    public List<Personaggio> calcolaOrdineIniziativa(List<Personaggio> partecipanti) {

        ordineTurno.clear();

        // Mappa: Personaggio → tiro dado
        Map<Personaggio, Integer> tiri = new HashMap<>();

        // 1️⃣ Ogni personaggio tira il dado
        for (Personaggio p : partecipanti) {
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

     /*    System.out.println("\nOrdine dei turni:");
        for (int i = 0; i < ordineTurno.size(); i++) {
            System.out.println((i + 1) + ") " + ordineTurno.get(i).getNomePersonaggio());*/
        
        return ordineTurno;
    }

    /**
     * Inizia un nuovo round di turni: calcola ordine e per ogni personaggio
     * mostra prima eventi e oggetti presenti nella stanza e poi chiede
     * l'azione tramite l'input fornito dallo scanner.
     */
    public void iniziaNuovoTurno(List<Personaggio> partecipanti, Scanner scanner) {
        if (partecipanti == null || partecipanti.isEmpty()) {
            System.out.println("Nessun partecipante al turno.");
            return;
        }

        List<Personaggio> ordine = calcolaOrdineIniziativa(partecipanti);
        //fino a qui è giusto 

        for (Personaggio p : ordine) {
            if (p == null) continue;
            if (p.èMorto(p)) {
                System.out.println(p.getNomePersonaggio() + " è morto. Salto il turno.");
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
            p.onTurnStart();

            // Se il personaggio deve saltare il turno, consumiamo il salto e passiamo oltre
            if (p.consumeSaltoTurno()) {
                System.out.println(p.getNomePersonaggio() + " salta questo turno.");
                // applichiamo comunque eventuali effetti di fine turno
                terminaTurnoCorrente(p);
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
            System.out.println("FINE TURNO DI " + p.getNomePersonaggio() );

        }
    }

    public void terminaTurnoCorrente(Personaggio personaggio) {
        if (personaggio == null) return;

        if (personaggio.èMorto(personaggio)) {
            System.out.println(personaggio.getNomePersonaggio() + " è morto. Nessun effetto da aggiornare.");
            ordineTurno.remove(personaggio);
            return;
        }

        // Applica gli effetti di fine turno solo al personaggio passato
        aggiornaEffettiFineTurno(personaggio);


    }

    public void aggiornaEffettiFineTurno(Personaggio personaggio) {
        if(personaggio.getStatoPersonaggio().equals("NESSUN_EFFETTO")) {
            System.out.println(personaggio.getNomePersonaggio() + " non ha effetti attivi.");
            return;
        }
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

      /*  if (!ciSonoEventi && !ciSonoOggetti) {
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
        } */

        // 3 scelta dell'azione
        System.out.println("\nCosa vuoi fare?");
        System.out.println("1) Fare un evento");
        System.out.println("2) Prendere un oggetto");
        if (ciSonoEventi && ciSonoOggetti) {
            System.out.println("3) Fare un evento E prendere un oggetto");
        }
        System.out.println("4) usare un oggetto dallo zaino");
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
                if (ciSonoEventi){ 
                    mostraEventi(eventi);
                    eventoService.eseguiEventiInStanza(personaggio, stanzaCorrente);
                }
                break;
            case 2:
                if (ciSonoOggetti) {
                    mostraOggetti(oggetti);
                    raccogliUnOggetto(personaggio, stanzaCorrente, oggetti, scanner);
                }
                break;
            case 3:
                if (ciSonoEventi && ciSonoOggetti){ 
                    mostraEventi(eventi);
                    mostraOggetti(oggetti);
                    eventoService.eseguiEventiInStanza(personaggio, stanzaCorrente);
                  raccogliUnOggetto(personaggio, stanzaCorrente, oggetti, scanner);
                }
                break;
            case 4:
                gestisciUsoOggettoDaZaino(personaggio, scanner);
                break;
            case 0:
                System.out.println("Turno terminato.");
                break;
            default:
                System.out.println("Scelta non valida.");
                break;
        }

        terminaTurnoCorrente(personaggio); 

     //   System.out.println("FINE TURNO DI " + personaggio.getNomePersonaggio() );
    }

    /// mostra gli eventi nella stanza 
    private void mostraEventi(List<Evento> eventi) {
        System.out.println("\nEventi disponibili:");
        for (int i = 0; i < eventi.size(); i++) {
            Evento e = eventi.get(i);
            System.out.println((i + 1) + ") " + e.getNomeEvento());
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
 /*    public void eseguiEvento(Personaggio personaggio, Stanza stanza, List<Evento> eventi, Scanner scanner) {

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
    } */

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
            System.out.println("Hai scelto: " + scelta);
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

    public boolean esploraStanza(Personaggio personaggio) {

        Stanza stanza = personaggio.getPosizioneCorrente();
        if (stanza == null) {
            return false;
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
                boolean termina = eventoService.attivaEvento(personaggio, e);
                if (termina) {
                    // se l'evento ha consumato il turno (es. combattimento), terminiamo il turno
                    terminaTurnoCorrente(personaggio);
                    return true;
                }
            }
        } else {
            System.out.println(" La stanza è tranquilla...");
        }

        // Segna la stanza come visitata
        stanza.setStatoS(StatoStanza.VISITATA);
        return false;
    }


}