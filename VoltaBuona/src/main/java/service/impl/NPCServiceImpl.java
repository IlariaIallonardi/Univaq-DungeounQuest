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
import domain.Tesoro;
import domain.Zaino;
import service.PersonaIncontrataService;

public class NPCServiceImpl implements PersonaIncontrataService {

    private static final AtomicInteger ID_COUNTER = new AtomicInteger(1);
    private final Scanner scanner = new Scanner(System.in);

    private boolean isNomeVenditore(String nome) {
        if (nome == null) {
            return false;
        }
        String n = nome.trim().toLowerCase();
        return n.equals("il confuso") || n.equals("borin");
    }

    private void popolaArticoliVenditore(NPC venditore) {
        if (venditore == null) {
            return;
        }
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
        if (npc == null || personaggio == null) {
            return null;
        }
        if (npc.haInteragito()) {
            System.out.println("\nL'NPC " + npc.getNomeNPC() + " ti ha già parlato.");
            return null;
        }

        System.out.println(npc.proponiRebus());
        System.out.print("\nInserisci la tua risposta: ");
        String risposta = scanner.nextLine();

        boolean corretta = risolviRebus(npc, personaggio, risposta);

        if (corretta) {
            System.out.println("\nRisposta corretta!");
            TesoroServiceImpl tesoroService = new TesoroServiceImpl();
            Oggetto oggetto = tesoroService.creaOggettoCasuale();
            System.out.println("L’NPC ti dona: " + oggetto.getNome());

            if (oggetto instanceof Tesoro tesoro) {
                int prima = personaggio.getPortafoglioPersonaggio();
                System.out.println("Saldo prima della donazione: " + prima);
                boolean applicato = tesoro.eseguiEffetto(personaggio);
                if (applicato) {
                    System.out.println("Hai guadagnato " + tesoro.getValore() + " monete. Saldo ora: " + personaggio.getPortafoglioPersonaggio());
                } else {
                    System.out.println("Errore nell'applicazione del tesoro.");
                }
            } else {
                Zaino zaino = personaggio.getZaino();
                if (zaino != null) {
                    boolean added = zaino.aggiungiOggettoAZaino(oggetto);
                    if (!added) {
                        System.out.println("Zaino pieno: il dono non è stato aggiunto.");
                    }
                } else {
                    System.out.println("Errore: zaino non disponibile per il personaggio.");
                }
            }

            npc.setHaInteragito(true);
        } else {
            System.out.println("\nRisposta errata. L’NPC non ti dona nulla.");
        }

        return risposta;
    }

    private void vendiConVenditore(Personaggio p, NPC venditore) {
        if (p == null || venditore == null) {
            return;
        }
        popolaArticoliVenditore(venditore);

        while (true) {
            int portafoglio = p.getPortafoglioPersonaggio();

            if (venditore.getArticoli().isEmpty()) {
                System.out.println("Il venditore non ha nulla da vendere.");
                System.out.println("0) Esci");
                System.out.println("1) Visualizza saldo");
                String lineEmpty = scanner.nextLine().trim();
                if ("1".equals(lineEmpty)) {
                    System.out.println("Saldo: " + portafoglio + " monete.");
                    continue;
                }
                return;
            }

            System.out.println("Il venditore offre:");
            for (int i = 0; i < venditore.getArticoli().size(); i++) {
                Oggetto oggetto = venditore.getArticoli().get(i);
                int prezzo = oggetto.getPrezzo();
                String nota = (prezzo > portafoglio) ? " (troppo caro)" : "";
                System.out.println((i + 1) + ") " + oggetto.getNome() + " - prezzo " + prezzo + nota);
            }

            int viewIndex = venditore.getArticoli().size() + 1;
            System.out.println(viewIndex + ") Visualizza saldo");
            System.out.println("0) Esci");
            System.out.print("Scegli il numero: ");
            String line = scanner.nextLine().trim();
            int scelta;
            try {
                scelta = Integer.parseInt(line);
            } catch (NumberFormatException ex) {
                System.out.println("Scelta non valida.");
                continue;
            }

            if (scelta == 0) {
                return;
            }
            if (scelta == viewIndex) {
                System.out.println("Saldo: " + portafoglio + " monete.");
                continue;
            }
            if (scelta < 1 || scelta > venditore.getArticoli().size()) {
                System.out.println("Scelta fuori range.");
                continue;
            }

            int idx = scelta - 1;
            Oggetto scelto = venditore.getArticoli().get(idx);
            int prezzo = scelto.getPrezzo();
            if (prezzo > portafoglio) {
                System.out.println("Il tuo portafoglio: " + portafoglio);
                System.out.println("Non hai abbastanza monete per questo oggetto.");
                continue;
            }

            Zaino z = p.getZaino();
            if (z == null || z.getListaOggetti().size() >= z.getCapienza()) {
                System.out.println("Zaino pieno o non disponibile. Non puoi comprare.");
                continue;
            }

            boolean added = z.aggiungiOggettoAZaino(scelto);
            if (added) {
                p.setPortafoglioPersonaggio(portafoglio - prezzo);
                venditore.getArticoli().remove(idx);
                System.out.println("Acquisto effettuato. Saldo ora: " + p.getPortafoglioPersonaggio());
            } else {
                System.out.println("Errore aggiunta oggetto allo zaino.");
            }
        }
    }

    public boolean risolviRebus(NPC npc, Personaggio personaggio, String risposta) {
        if (npc == null) {
            return false;
        }
        return npc.verificaRisposta(risposta);
    }

    public void donaTesoro(NPC npc, Personaggio personaggio, Oggetto o) {
        if (npc == null || personaggio == null || o == null) {
            System.out.println("Parametri non validi per donaTesoro.");
            return;
        }

        System.out.println("DEBUG donaTesoro: npc identity=" + System.identityHashCode(npc) + " nome=" + npc.getNomeNPC());
        System.out.println("DEBUG donaTesoro: personaggio identity=" + System.identityHashCode(personaggio));
        System.out.println("DEBUG donaTesoro: oggetto param 'o' identity=" + System.identityHashCode(o) + " classe=" + (o == null ? "null" : o.getClass().getName()));

        Tesoro tesoro = npc.daOggetto(o);

        System.out.println("DEBUG donaTesoro: dono restituito = " + tesoro + " | identity=" + (tesoro == null ? "null" : System.identityHashCode(tesoro)) + " classe=" + (tesoro == null ? "null" : tesoro.getClass().getName()));

        if (tesoro != null) {
            int saldoPrima = personaggio.getPortafoglioPersonaggio();
            System.out.println("Saldo prima: " + saldoPrima);

            boolean applicato = tesoro.eseguiEffetto(personaggio);
            if (applicato) {
                System.out.println("L’NPC ti dona: " + tesoro.getNome());
                System.out.println("Hai guadagnato " + tesoro.getValore() + " monete. Saldo ora: " + personaggio.getPortafoglioPersonaggio());
                if (npc.getOggettiDaDonare() != null) {
                    npc.getOggettiDaDonare().remove(o);
                }
                npc.setHaInteragito(true);
            } else {
                System.out.println("Errore: effetto del tesoro non applicato.");
            }
        } else {
            System.out.println("L’NPC non ti può donare questo oggetto.");
        }
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

    @Override
    public boolean attivaEvento(Personaggio personaggio, Evento e) {
        if (!(e instanceof NPC npc)) {
            return false;
        }

        Stanza stanza = npc.getPosizioneCorrenteNPC();
        System.out.println("Incontri un NPC: " + npc.getNomeNPC());

        if (npc.isVenditore() || isNomeVenditore(npc.getNomeNPC())) {
            System.out.println("Questo NPC è un venditore.");
            vendiConVenditore(personaggio, npc);
        } else {
            parla(personaggio, npc);
        }

        rimuoviEventoDaStanza(stanza, e);
        return true;
    }

    @Override
    public void  eseguiEventiInStanza(Personaggio personaggio, Stanza stanza) {
        if (stanza == null || stanza.getListaEventiAttivi() == null) {
            return ;
        }
        for (Evento e : List.copyOf(stanza.getListaEventiAttivi())) {
            boolean termina = attivaEvento(personaggio, e);
            if (termina) {
                return ;
            }
        }
        return ;
    }

    @Override
    public Evento aggiungiEventoCasuale() {
        int id = ID_COUNTER.getAndIncrement();
        String[] nomiBot = {"Il confuso", "Nonno Rebus", "L'indeciso", "Borin", "La Saggia"};
        Random rngBot = new Random();
        String nomeNPC = nomiBot[rngBot.nextInt(nomiBot.length)];

        List<QA> domande = List.of(
                new QA("Qual è la capitale d'Italia?", "Roma"),
                new QA("Quanto fa 2 + 2?", "4"),
                new QA("Di che colore è il cielo in una giornata serena?", "Blu"),
                new QA("Quanti giorni ci sono in una settimana?", "7"),
                new QA("Qual è il contrario di 'caldo'?", "Freddo")
        );

        QA scelta = domande.get(ThreadLocalRandom.current().nextInt(domande.size()));
        String rebus = scelta.domanda();
        String rispostaCorretta = scelta.risposta();

        NPC npc = new NPC(id, "NPC", rebus, rispostaCorretta, null, nomeNPC, null);
        if (isNomeVenditore(nomeNPC)) {
            npc.setVenditore(true);
            popolaArticoliVenditore(npc);
        }
        return npc;
    }

    private record QA(String domanda, String risposta) {

    }
}
