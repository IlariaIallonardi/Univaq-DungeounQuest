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
            System.out.println("\n Risposta corretta! L’NPC ti ricompensa.");
            donaOggettoSingolo(npc, personaggio);
        } else {
            System.out.println("\n Risposta errata. L’NPC non ti dona nulla.");
        }

        npc.setHaInteragito(true);
        return risposta;
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
        stanza.getListaEventiAttivi().remove(evento);
    }

    ;

    @Override
    public boolean attivaEvento(Personaggio personaggio, Evento e) {
        if (e instanceof NPC npc) {
            Stanza stanza = ((NPC) e).getPosizioneCorrenteNPC();
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
            System.out.println("⚠ Il tuo zaino è pieno! Non puoi prendere " + oggetto.getNome());
            return;
        }

        zaino.getListaOggetti().add(oggetto);
        System.out.println("👉 Oggetto aggiunto allo zaino: " + oggetto.getNome());
    }

    @Override

    public Evento aggiungiEventoCasuale() {
        int id = ID_COUNTER.getAndIncrement();
          String[] nomiBot = {"Il confuso", "Nonno Rebus", "L'indeciso", "Borin", "La Saggia"};
        Random rngBot = new Random();

        String nomeNPC = "NPC_" + nomiBot[rngBot.nextInt(nomiBot.length)];

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

}
