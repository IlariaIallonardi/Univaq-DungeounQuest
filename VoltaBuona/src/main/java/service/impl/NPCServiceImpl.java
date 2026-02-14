package service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import domain.Evento;
import domain.NPC;
import domain.Oggetto;
import domain.Personaggio;
import domain.Stanza;
import domain.Tesoro;
import domain.Zaino;
import exception.DungeonException;
import service.FileService;
import service.PersonaIncontrataService;
import service.TurnoService;
import service.ZainoService;

public class NPCServiceImpl implements PersonaIncontrataService {

    private static final AtomicInteger ID_CONTATORE = new AtomicInteger(300);
    private final ScannerSingleton scannerGenerale = ScannerSingleton.getInstance();
    private RandomSingleton randomGenerale = RandomSingleton.getInstance();
    private TurnoService turnoService;
    private FileService fileService;

    public NPCServiceImpl() { }

  

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
    public void popolaArticoliVenditore(NPC venditore)throws DungeonException {
        if (venditore == null) {
            throw new DungeonException("NPC venditore nullo passato a popolaArticoliVenditore.");
        }
        if (venditore.getArticoli().isEmpty()) {
            int numeroCasualeOggetti = randomGenerale.prossimoNumero(1, 4);
            for (int i = 0; i < numeroCasualeOggetti; i++) {
                int tipoOggetto = randomGenerale.prossimoNumero(0, 3);
                Oggetto oggettoVenditore;
                switch (tipoOggetto) {
                    case 0 ->
                        oggettoVenditore = new PozioneServiceImpl().creaOggettoCasuale();
                    case 1 ->
                        oggettoVenditore = new ArmaturaServiceImpl().creaOggettoCasuale();
                    default ->
                        oggettoVenditore = new PozioneServiceImpl().creaOggettoCasuale();
                }
                if (oggettoVenditore == null) {
                    throw new DungeonException("Oggetto nullo generato per il venditore.");
                }
                venditore.getArticoli().add(oggettoVenditore);
            }
        }
    }

    public String parla(Personaggio personaggio, NPC npc) throws DungeonException {
        if (personaggio == null || npc == null) {
            throw new DungeonException("Personaggio o NPC nullo passato a parla().");
        }
        if (npc.haInteragito()) {
            System.out.println("" + npc.getNomeNPC() + " ti ha già parlato.");
            return null;
        }
        System.out.println(npc.proponiRebus());

        System.out.print("\nInserisci la tua risposta: ");
        String risposta;
        boolean isBot = personaggio.getNomePersonaggio() != null && (personaggio.getNomePersonaggio().startsWith("BOT_") || personaggio.getNomePersonaggio().startsWith("Bot-") || personaggio.getNomePersonaggio().toLowerCase().contains("bot"));
        if (isBot) {
            List<String> rispostePossibili = List.of("Roma", "4", "Blu", "7", "Freddo");
            risposta = randomGenerale.scegliRandomicamente(rispostePossibili);
            System.out.println("Il computer risponde: " + risposta);
        } else {
            risposta = scannerGenerale.leggiLinea();
        }

        boolean rispostaCorretta = npc.verificaRisposta(risposta);

        if (rispostaCorretta) {
            System.out.println("\nRisposta corretta!");
            TesoroServiceImpl tesoroService = new TesoroServiceImpl();
            Oggetto oggetto = tesoroService.creaOggettoCasuale();
            if (oggetto == null) {
                throw new DungeonException("Oggetto nullo generato come ricompensa dall'NPC.");
            }
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
                    throw new DungeonException("Zaino non disponibile per il personaggio.");
                }
            }

            npc.setHaInteragito(true);
            FileService.getInstance().writeLog(personaggio.getNomePersonaggio() + " ha risposto correttamente al rebus di " + npc.getNomeNPC() + " e ha ricevuto un dono.");
        } else {
            System.out.println("\nRisposta errata. L’NPC non ti dona nulla.");
        }

        return risposta;
    }

    /**
     * Gli NPC 'borin e 'il confuso' sono dei venditori di pozioni e armature.
     */
    public void vendiConVenditore(Personaggio personaggio, NPC venditore) {
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
                boolean isBot = personaggio.getNomePersonaggio() != null && (personaggio.getNomePersonaggio().startsWith("BOT_") || personaggio.getNomePersonaggio().startsWith("Bot-") || personaggio.getNomePersonaggio().toLowerCase().contains("bot"));
                if (isBot) {
                    turnoService.terminaTurnoCorrente(personaggio);
                } else {
                    String lineaEmpty = scannerGenerale.leggiLinea().trim();
                    if ("1".equals(lineaEmpty)) {
                        System.out.println("Saldo: " + portafoglio + " monete.");
                        continue;
                    }
                    return;
                }
            }

            System.out.println("Il venditore offre:");
            for (int i = 0; i < venditore.getArticoli().size(); i++) {
                Oggetto oggetto = venditore.getArticoli().get(i);
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
            int scelta;
            boolean isBot = personaggio.getNomePersonaggio() != null && (personaggio.getNomePersonaggio().startsWith("BOT_") || personaggio.getNomePersonaggio().startsWith("Bot-") || personaggio.getNomePersonaggio().toLowerCase().contains("bot"));
           if(isBot){
            scelta = randomGenerale.prossimoNumero(0, indice);
           }else{
            scelta = scannerGenerale.leggiIntero();
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
                FileService.getInstance().writeLog(personaggio.getNomePersonaggio() + " ha scelto di acquistare: " + oggettoScelto.getNome() + " per " + prezzo + " monete - Acquisto riuscito: " + aggiungi);
        }
    }

    /**
     * *
     * Per gli NPC che se rispondi ad una domanda giusta ti dona delle monete.
     */
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

            System.out.println("Incontri un NPC: " + npc.getNomeNPC());

            if (npc.isVenditore() || nomeVenditore(npc.getNomeNPC())) {
                System.out.println("Questo NPC è un venditore.");
                vendiConVenditore(personaggio, npc);
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
        for (Evento evento : stanza.getListaEventiAttivi()) {
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
        List<String> nomiBot = new ArrayList<>(List.of("Il confuso", "Nonno Rebus", "L'indeciso", "Borin", "La Saggia"));
        String nomeNPC = randomGenerale.scegliRandomicamente(nomiBot);

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
