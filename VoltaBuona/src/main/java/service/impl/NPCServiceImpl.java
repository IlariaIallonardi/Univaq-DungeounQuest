/*package service.impl;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import domain.Evento;
import domain.NPC;
import domain.Oggetto;
import domain.Personaggio;
import domain.Stanza;
import domain.Zaino;
import service.PersonaIncontrataService;

public class NPCServiceImpl implements PersonaIncontrataService {

    private static final AtomicInteger ID_COUNTER = new AtomicInteger(1);
    private final Scanner scanner = new Scanner(System.in);
    private final NPC npc = null;

    public String parla(Personaggio personaggio, NPC npc) {

        if (npc.haInteragito()) {
            System.out.println("\nL'NPC " + npc.getNomeNPC() + " ti ha già parlato.");
            return null;
        }

        System.out.println(npc.proponiRebus());

        System.out.print("\nInserisci la tua risposta: ");
        String risposta = scanner.nextLine();

        boolean corretta = risolviRebus(npc, personaggio, risposta);

        if (corretta) {
            System.out.println("\n Risposta corretta!");
            // donaOggettoSingolo(npc, personaggio);
            TesoroServiceImpl tesoroService = new TesoroServiceImpl();
            Oggetto tesoro = tesoroService.creaOggettoCasuale();
            System.out.println("L’NPC ti dona: " + tesoro.getNome());
            aggiungiOggettoAZaino(personaggio, tesoro);
        } else {
            System.out.println("\n Risposta errata. L’NPC non ti dona nulla.");
        }

        npc.setHaInteragito(true);
        return risposta;
    }

    private void vendiConVenditore(Personaggio p, NPC venditore) {
        // genera fino a 3 oggetti se l'inventario è vuoto
        if (venditore.getArticoli().isEmpty()) {
            int n = ThreadLocalRandom.current().nextInt(1, 4); // 1..3
            for (int i = 0; i < n; i++) {
                int tipo = ThreadLocalRandom.current().nextInt(4);
                Oggetto o;
                switch (tipo) {
                    case 0 ->
                        o = new PozioneServiceImpl().creaOggettoCasuale();
                    case 1 ->
                        o = new ArmaServiceImpl().creaOggettoCasuale();
                    case 2 ->
                        o = new ArmaturaServiceImpl().creaOggettoCasuale();
                    default ->
                        o = new PozioneServiceImpl().creaOggettoCasuale();
                }
                venditore.getArticoli().add(o);

            }
        }

        // mostra offerte
        System.out.println("Il venditore offre:");
        for (int i = 0; i < venditore.getArticoli().size(); i++) {
            Oggetto oggetto = venditore.getArticoli().get(i);
            int prezzo = oggetto.getPrezzo();
            System.out.println((i + 1) + ") " + oggetto.getNome() + " - prezzo " + prezzo);
        }
        System.out.print("Scegli il numero (0 per uscire): ");
        String line = scanner.nextLine().trim();
        int scelta;
        try {
            scelta = Integer.parseInt(line);
        } catch (NumberFormatException ex) {
            System.out.println("Scelta non valida.");
            return;
        }
        if (scelta <= 0 || scelta > venditore.getArticoli().size()) {
            return;
        }

        int idx = scelta - 1;
        Oggetto scelto = venditore.getArticoli().get(idx);
        int prezzo = scelto.getPrezzo();
        if (p.getPortafoglioPersonaggio() < prezzo) {
            System.out.println("Non hai abbastanza monete.");
            return;
        }
        Zaino z = p.getZaino();
        if (z.getListaOggetti().size() >= z.getCapienza()) {
            // gestisciUsoOggettoDaZaino(p);
            System.out.println("Zaino pieno. Non puoi comprare.");
            return;
        }

        // esegui scambio
        p.setPortafoglioPersonaggio(p.getPortafoglioPersonaggio() - prezzo);
        aggiungiOggettoAZaino(p, scelto);
        venditore.getArticoli().remove(idx);
        System.out.println("Acquisto effettuato.");
    }

    public boolean risolviRebus(NPC npc, Personaggio personaggio, String risposta) {
        return npc.verificaRisposta(risposta);
    }

    public void donaOggettoSingolo(NPC npc, Personaggio personaggio) {

        if (!npc.haOggettiDaDare()) {
            System.out.println("L’NPC non ha oggetti da darti.");
            return;
        }

        Oggetto dono = npc.daOggetto();
        System.out.println("L’NPC ti dona: " + dono.getNome());

        aggiungiOggettoAZaino(personaggio, dono);

    }

    @Override
    public void rimuoviEventoDaStanza(Stanza stanza, Evento evento) {
        if (stanza == null || evento == null) {
            return;
        }
        if (stanza.getListaEventiAttivi() != null) {
            stanza.getListaEventiAttivi().remove(evento);
        }
    }

    ;

    

   /* @Override
    public boolean attivaEvento(Personaggio personaggio, Evento e) {
        if (e instanceof NPC npc) {
            Stanza stanza = ((NPC) e).getPosizioneCorrenteNPC();
            if(npc.getNomeNPC().equals("Il confuso") || npc.getNomeNPC().equals("Borin")){
                npc.setVenditore(true);
            }
            if(npc instanceof NPC && npc.isVenditore() ) {
                vendiConVenditore(personaggio, npc);
                return true; // Interazione NPC consuma il turno
            }
            System.out.println("Incontri un NPC: " + npc.getNomeNPC());
            
            parla(personaggio, npc);
            rimuoviEventoDaStanza(stanza, e);
            // Interazione NPC consuma il turno
            return true;
        }
        return false;
    }

    ;
    @Override
    public boolean attivaEvento(Personaggio personaggio, Evento e) {
        if (!(e instanceof NPC npc)) {
            return false;
        }

        Stanza stanza = npc.getPosizioneCorrenteNPC();
        System.out.println("Incontri un NPC: " + npc.getNomeNPC());

        String nome = (npc.getNomeNPC() == null) ? "" : npc.getNomeNPC().trim().toLowerCase();
        boolean isVenditore = nome.equals("il confuso") || nome.equals("borin");

        if (isVenditore) {
            vendiConVenditore(personaggio, npc);
        } else {
            parla(personaggio, npc);
        }

        rimuoviEventoDaStanza(stanza, e);
        return true;
    }

    @Override
    public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza) {
        for (Evento e : stanza.getListaEventiAttivi()) {
            boolean termina = attivaEvento(personaggio, e);
            if (termina) {
                return;
            }
        }
    }

    private void aggiungiOggettoAZaino(Personaggio personaggio, Oggetto oggetto) {

        Zaino zaino = personaggio.getZaino();

        if (zaino.getListaOggetti().size() >= zaino.getCapienza()) {
            System.out.println(" Il tuo zaino è pieno! Non puoi prendere " + oggetto.getNome());
            return;
        }

        zaino.getListaOggetti().add(oggetto);
        System.out.println(" Oggetto aggiunto allo zaino: " + oggetto.getNome());
    }

    @Override

    public Evento aggiungiEventoCasuale() {
        int id = ID_COUNTER.getAndIncrement();
        String[] nomiBot = {"Il confuso", "Nonno Rebus", "L'indeciso", "Borin", "La Saggia"};
        Random rngBot = new Random();

        String nomeNPC = "NPC" + " " + nomiBot[rngBot.nextInt(nomiBot.length)];

        // 5 domande semplici (domanda, risposta)
        List<QA> domande = List.of(
                new QA("Qual è la capitale d'Italia?", "Roma"),
                new QA("Quanto fa 2 + 2?", "4"),
                new QA("Di che colore è il cielo in una giornata serena?", "Blu"),
                new QA("Quanti giorni ci sono in una settimana?", "7"),
                new QA("Qual è il contrario di 'caldo'?", "Freddo")
        );

        // Pesca casuale
        QA scelta = domande.get(ThreadLocalRandom.current().nextInt(domande.size()));

        String rebus = scelta.domanda();
        String rispostaCorretta = scelta.risposta();

        NPC npc = new NPC(
                id,
                "NPC",
                rebus,
                rispostaCorretta,
                null,
                nomeNPC,
                null
        );

        return npc;
    }

// Piccola struttura per tenere insieme domanda/risposta
    private record QA(String domanda, String risposta) {

    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

} */
package service.impl;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import domain.Evento;
import domain.NPC;
import domain.Oggetto;
import domain.Personaggio;
import domain.Stanza;
import domain.Zaino;
import service.PersonaIncontrataService;

public class NPCServiceImpl implements PersonaIncontrataService {

    private static final AtomicInteger ID_COUNTER = new AtomicInteger(1);
    private final Scanner scanner = new Scanner(System.in);
    private final NPC npc = null;

    private boolean isNomeVenditore(String nome) {
        if (nome == null) {
            return false;
        }
        String n = nome.trim().toLowerCase();
        return n.equals("il confuso") || n.equals("borin");
    }

    private void popolaArticoliVenditore(NPC venditore) {
        if (venditore.getArticoli().isEmpty()) {
            int n = ThreadLocalRandom.current().nextInt(1, 4);
            for (int i = 0; i < n; i++) {
                int tipo = ThreadLocalRandom.current().nextInt(3);
                Oggetto o;
                switch (tipo) {
                    case 0 ->
                        o = new PozioneServiceImpl().creaOggettoCasuale();
                    case 1 ->
                        o = new ArmaServiceImpl().creaOggettoCasuale();
                    default ->
                        o = new ArmaturaServiceImpl().creaOggettoCasuale();
                }
                venditore.getArticoli().add(o);
            }
        }
    }

    public String parla(Personaggio personaggio, NPC npc) {

        if (npc.haInteragito()) {
            System.out.println("\nL'NPC " + npc.getNomeNPC() + " ti ha già parlato.");
            return null;
        }

        System.out.println(npc.proponiRebus());

        System.out.print("\nInserisci la tua risposta: ");
        String risposta = scanner.nextLine();

        boolean corretta = risolviRebus(npc, personaggio, risposta);

        if (corretta) {
            System.out.println("\n Risposta corretta!");
            // donaOggettoSingolo(npc, personaggio);
            TesoroServiceImpl tesoroService = new TesoroServiceImpl();
            Oggetto tesoro = tesoroService.creaOggettoCasuale();
            System.out.println("L’NPC ti dona: " + tesoro.getNome());
            aggiungiOggettoAZaino1(personaggio, tesoro);
        } else {
            System.out.println("\n Risposta errata. L’NPC non ti dona nulla.");
        }

        npc.setHaInteragito(true);
        return risposta;
    }

    private void vendiConVenditore(Personaggio p, NPC venditore) {
        popolaArticoliVenditore(venditore);

        int portafoglio = p.getPortafoglioPersonaggio();

        if (venditore.getArticoli().isEmpty()) {
            System.out.println("Il venditore non ha nulla da vendere.");
            return;
        }

        System.out.println("Il venditore offre:");
        for (int i = 0; i < venditore.getArticoli().size(); i++) {
            Oggetto oggetto = venditore.getArticoli().get(i);
            int prezzo = oggetto.getPrezzo();
            String nota = (prezzo > portafoglio) ? " (troppo caro)" : "";
            System.out.println((i + 1) + ") " + oggetto.getNome() + " - prezzo " + prezzo + nota);
        }

        boolean anyAffordable = venditore.getArticoli().stream().anyMatch(o -> o.getPrezzo() <= portafoglio);
        if (!anyAffordable) {
            System.out.println("Non hai abbastanza monete per comprare nessun oggetto.");
            return;
        }

        System.out.print("Scegli il numero (0 per uscire): ");
        String line = scanner.nextLine().trim();
        int scelta;
        try {
            scelta = Integer.parseInt(line);
        } catch (NumberFormatException ex) {
            System.out.println("Scelta non valida.");
            return;
        }
        if (scelta <= 0 || scelta > venditore.getArticoli().size()) {
            return;
        }

        int idx = scelta - 1;
        Oggetto scelto = venditore.getArticoli().get(idx);
        int prezzo = scelto.getPrezzo();
        if (prezzo > portafoglio) {
            System.out.println("il tuo portafoglio"+portafoglio);
            System.out.println("Non hai abbastanza monete per questo oggetto.");
            return;
        }

        Zaino z = p.getZaino();
        if (z.getListaOggetti().size() >= z.getCapienza()) {
            System.out.println("Zaino pieno. Non puoi comprare.");
            return;
        }

        p.setPortafoglioPersonaggio(portafoglio - prezzo);
        //aggiungiOggettoAZaino(p, scelto);
        venditore.getArticoli().remove(idx);
        System.out.println("Acquisto effettuato.");
    }

    public boolean risolviRebus(NPC npc, Personaggio personaggio, String risposta) {
        return npc.verificaRisposta(risposta);
    }

    public void donaTesoro(NPC npc, Personaggio personaggio) {

        if (!npc.haOggettiDaDare()) {
            System.out.println("L’NPC non ha oggetti da darti.");
            return;
        }

        Oggetto dono = npc.daOggetto();
        System.out.println("L’NPC ti dona: " + dono.getNome());

        aggiungiOggettoAZaino1(personaggio, dono);

    }

    @Override
    public void rimuoviEventoDaStanza(Stanza stanza, Evento evento) {
        if (stanza == null || evento == null) {
            return;
        }
        if (stanza.getListaEventiAttivi() != null) {
            stanza.getListaEventiAttivi().remove(evento);
        }
    }

    ;

    @Override
    public boolean attivaEvento(Personaggio personaggio, Evento e) {
        if (!(e instanceof NPC npc)) {
            return false;
        }

        Stanza stanza = npc.getPosizioneCorrenteNPC();
        System.out.println("Incontri un NPC: " + npc.getNomeNPC());

        if (npc.isVenditore()) {
            System.out.println("Questo NPC è un venditore.");
            vendiConVenditore(personaggio, npc);
        } else {
            parla(personaggio, npc);
        }

        rimuoviEventoDaStanza(stanza, e);
        return true;
    }

    @Override
    public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza) {
        for (Evento e : stanza.getListaEventiAttivi()) {
            boolean termina = attivaEvento(personaggio, e);
            if (termina) {
                return;
            }
        }
    }

    private void aggiungiOggettoAZaino1(Personaggio personaggio, Oggetto oggetto) {
        if (personaggio == null || oggetto == null) {
            return;
        }
        Zaino z = personaggio.getZaino();
        if (z == null) {
            return;
        }

        if (z.aggiungiOggettoAZaino(oggetto)) {
            System.out.println("Oggetto aggiunto allo zaino: " + oggetto.getNome());
        } else {
            System.out.println("Il tuo zaino è pieno! Non puoi prendere " + oggetto.getNome());
        }
    }

    @Override
    public Evento aggiungiEventoCasuale() {
        int id = ID_COUNTER.getAndIncrement();
        String[] nomiBot = {"Il confuso", "Nonno Rebus", "L'indeciso", "Borin", "La Saggia"};
        Random rngBot = new Random();

        String nomeNPC = nomiBot[rngBot.nextInt(nomiBot.length)]; // senza prefisso "NPC "

        // 5 domande semplici (domanda, risposta)
        List<QA> domande = List.of(
                new QA("Qual è la capitale d'Italia?", "Roma"),
                new QA("Quanto fa 2 + 2?", "4"),
                new QA("Di che colore è il cielo in una giornata serena?", "Blu"),
                new QA("Quanti giorni ci sono in una settimana?", "7"),
                new QA("Qual è il contrario di 'caldo'?", "Freddo")
        );

        // Pesca casuale
        QA scelta = domande.get(ThreadLocalRandom.current().nextInt(domande.size()));

        String rebus = scelta.domanda();
        String rispostaCorretta = scelta.risposta();

        NPC npc = new NPC(
                id,
                "NPC",
                rebus,
                rispostaCorretta,
                null,
                nomeNPC,
                null
        );

        if (isNomeVenditore(nomeNPC)) {
            npc.setVenditore(true);
            popolaArticoliVenditore(npc);
        }

        return npc;
    }

    // Piccola struttura per tenere insieme domanda/risposta
    private record QA(String domanda, String risposta) {

    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
