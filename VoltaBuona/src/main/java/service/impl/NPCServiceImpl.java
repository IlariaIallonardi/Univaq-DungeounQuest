package service.impl;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import domain.Arma;
import domain.Evento;
import domain.NPC;
import domain.Oggetto;
import domain.Personaggio;
import domain.Stanza;
import domain.Tesoro;
import domain.Zaino;
import service.PersonaIncontrataService;
import service.ZainoService;

public class NPCServiceImpl implements PersonaIncontrataService {

    private static final AtomicInteger ID_CONTATORE = new AtomicInteger(300);
    private final Scanner scanner = new Scanner(System.in);

    public boolean nomeVenditore(String nome) {
        if (nome == null) {
            return false;
        }
        String nomeNpc = nome.trim().toLowerCase();
        return nomeNpc.equals("il confuso") || nomeNpc.equals("borin");
    }

    /**
     * Lista di oggetti che potrà vendere l'npc
     */
    public void popolaArticoliVenditore(NPC venditore) {

        if (venditore.getArticoli().isEmpty()) {
            int numeroCasualeOggetti = ThreadLocalRandom.current().nextInt(1, 4);
            for (int i = 0; i < numeroCasualeOggetti; i++) {
                int tipoOggetto = ThreadLocalRandom.current().nextInt(3);
                Oggetto oggettoVenditore;
                switch (tipoOggetto) {
                    case 0 ->
                        oggettoVenditore = new PozioneServiceImpl().creaOggettoCasuale();
                    case 1 ->
                        oggettoVenditore = new ArmaServiceImpl().creaOggettoCasuale();
                    default ->
                        oggettoVenditore = new ArmaturaServiceImpl().creaOggettoCasuale();
                }
                venditore.getArticoli().add(oggettoVenditore);
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
            System.out.println("\nRisposta corretta!");
            TesoroServiceImpl tesoroService = new TesoroServiceImpl();
            Oggetto oggetto = tesoroService.creaOggettoCasuale();
            System.out.println("L’NPC ti dona: " + oggetto.getNome());

            if (oggetto instanceof Tesoro tesoro) {

                boolean applicato = tesoro.eseguiEffetto(personaggio);
                if (applicato) {
                    System.out.println("Hai guadagnato " + tesoro.getValore() + " monete. Saldo ora: " + personaggio.getPortafoglioPersonaggio());
                } else {
                    System.out.println("Errore nell'applicazione del tesoro.");
                }
            } else {
                Zaino zaino = personaggio.getZaino();
                if (zaino != null) {
                    ZainoService zainoService = new ZainoService();
                    boolean oggettoAggiunto = zainoService.aggiungiOggettoAZaino(zaino, oggetto);
                    if (!oggettoAggiunto) {
                        System.out.println("Zaino pieno: il dono non è stato aggiunto.");
                        Stanza stanza = npc.getPosizioneCorrente();
                        zainoService.èPieno(zaino, stanza, oggetto, personaggio);
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

    public void vendiConVenditore(Personaggio personaggio, NPC venditore, Oggetto oggetto) {
        if (personaggio == null || venditore == null) {
            return;
        }
        popolaArticoliVenditore(venditore);

        ZainoService zainoService = new ZainoService();
        while (true) {
            int portafoglio = personaggio.getPortafoglioPersonaggio();

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
                oggetto = venditore.getArticoli().get(i);
                int prezzo = oggetto.getPrezzo();
                String nota = "";
                if (prezzo > portafoglio) {
                    nota = "Non hai abbastanza monete per questo acquisto";
                }
                System.out.println((i + 1) + ") " + oggetto.getNome() + " - prezzo " + prezzo + nota);
            }

            int indice = venditore.getArticoli().size() + 1;
            System.out.println(indice + ") Visualizza saldo");
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
            Arma.TipoArma tipo = ((Arma) oggetto).getTipoArma();
            if (!personaggio.puoRaccogliere(tipo)) {
                System.out.println("Non puoi comprare" + tipo);
            }
            if (scelta == 0) {
                return;
            }
            if (scelta == indice) {
                System.out.println("Saldo: " + portafoglio + " monete.");
                continue;
            }
            int indiceScelta = scelta - 1;
            Oggetto oggettoScelto = venditore.getArticoli().get(indiceScelta);
            int prezzo = oggettoScelto.getPrezzo();
            if (prezzo > portafoglio) {
                System.out.println("Il tuo portafoglio: " + portafoglio);
                continue;
            }

            Zaino zaino = personaggio.getZaino();
            if (zaino.getListaOggetti().size() >= zaino.getCapienza()) {
                System.out.println("Zaino pieno o non disponibile. Non puoi comprare.");
                continue;
            }
            boolean aggiungi = zainoService.aggiungiOggettoAZaino(zaino, oggettoScelto);
            if (aggiungi) {
                personaggio.setPortafoglioPersonaggio(portafoglio - prezzo);
                venditore.getArticoli().remove(indiceScelta);
                System.out.println("Acquisto effettuato. Saldo ora: " + personaggio.getPortafoglioPersonaggio());
            } else {
                System.out.println("Errore aggiunta oggetto allo zaino.");
            }
        }
    }

    public boolean risolviRebus(NPC npc, Personaggio personaggio, String risposta) {
        return npc.verificaRisposta(risposta);
    }

    public void donaTesoro(NPC npc, Personaggio personaggio, Oggetto oggetto) {
        if (npc == null || personaggio == null || oggetto == null) {
            System.out.println("Parametri non validi per donaTesoro.");
            return;
        }

        Tesoro tesoro = npc.daOggetto(oggetto);

        System.out.println("Dona Tesoro: " + tesoro);

        if (tesoro != null) {
            int saldoPrima = personaggio.getPortafoglioPersonaggio();
            System.out.println("Saldo prima: " + saldoPrima);

            boolean applicato = tesoro.eseguiEffetto(personaggio);
            if (applicato) {
                System.out.println("L’NPC ti dona: " + tesoro.getNome());
                System.out.println("Hai guadagnato " + tesoro.getValore() + " monete. Saldo ora: " + personaggio.getPortafoglioPersonaggio());

                npc.setHaInteragito(true);
            }
        }

    }

    @Override
    public boolean attivaEvento(Personaggio personaggio, Evento evento) {

        if ((evento instanceof NPC npc)) {

            Stanza stanza = npc.getPosizioneCorrente();
            System.out.println("Incontri un NPC: " + npc.getNomeNPC());

            List<Oggetto> oggetto = (((NPC) personaggio).getArticoli());
            if (npc.isVenditore() || nomeVenditore(npc.getNomeNPC())) {
                System.out.println("Questo NPC è un venditore.");
                vendiConVenditore(personaggio, npc, oggetto);
            } else {
                parla(personaggio, npc);
            }
        }
        return true;
    }

    @Override
    public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza) {
        if (stanza == null || stanza.getListaEventiAttivi() == null) {
            return;
        }
        for (Evento evento : List.copyOf(stanza.getListaEventiAttivi())) {
            boolean termina = attivaEvento(personaggio, evento);
            if (termina) {
                return;
            }
        }
        return;
    }

    @Override
    public Evento aggiungiEventoCasuale() {
        int id = ID_CONTATORE.getAndIncrement();
        String[] nomiBot = {"Il confuso", "Nonno Rebus", "L'indeciso", "Borin", "La Saggia"};
        Random rngBot = new Random();
        String nomeNPC = nomiBot[rngBot.nextInt(nomiBot.length)];

        List<DomandaRisposta> domande = List.of(
                new DomandaRisposta("Qual è la capitale d'Italia?", "Roma"),
                new DomandaRisposta("Quanto fa 2 + 2?", "4"),
                new DomandaRisposta("Di che colore è il cielo in una giornata serena?", "Blu"),
                new DomandaRisposta("Quanti giorni ci sono in una settimana?", "7"),
                new DomandaRisposta("Qual è il contrario di 'caldo'?", "Freddo")
        );

        DomandaRisposta scelta = domande.get(ThreadLocalRandom.current().nextInt(domande.size()));
        String rebus = scelta.domanda();
        String rispostaCorretta = scelta.risposta();

        NPC npc = new NPC(id, "NPC", rebus, rispostaCorretta, null, nomeNPC);
        if (nomeVenditore(nomeNPC)) {
            npc.setVenditore(true);
            popolaArticoliVenditore(npc);
        }
        return npc;
    }

    public record DomandaRisposta(String domanda, String risposta) {

    }
}
