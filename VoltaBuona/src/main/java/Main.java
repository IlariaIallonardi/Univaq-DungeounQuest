
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import domain.Dungeon;
import domain.Evento;
import domain.Gioco;
import domain.Oggetto;
import domain.Personaggio;
import domain.Stanza;
import domain.Stato.StatoGioco;
import service.DungeonFactory;
import service.FileService;
import service.GiocatoreService;
import service.GiocoService;
import service.StanzaFactory;
import service.TurnoService;
import service.impl.ArciereServiceImpl;
import service.impl.GuerrieroServiceImpl;
import service.impl.MagoServiceImpl;
import service.impl.PaladinoServiceImpl;
import util.ANSI;

public class Main {

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        FileService fileService = new FileService();
        fileService.setLogFileNome("partita.log");
        // FACTORY1

        StanzaFactory stanzaFactory = new StanzaFactory();
        DungeonFactory dungeonFactory = new DungeonFactory(stanzaFactory);
        List<Personaggio> partecipanti = new ArrayList<>();

        // SERVICE PRINCIPALE
        Gioco gioco = new Gioco(1, new ArrayList<>(), partecipanti, new ArrayList<>(), new ArrayList<>(), 0, StatoGioco.INIZIO, null, null);
        GiocatoreService giocatoreService = new GiocatoreService();
        // Imposta il file di log (puoi cambiare il nome se vuoi log separati per partita)
        fileService.setLogFileNome("partita.log");

        boolean loaded = false;
        System.out.println("--- Gestione salvataggi ---");
        System.out.println("1) Carica un salvataggio esistente");
        System.out.println("2) Inizia nuova partita");
        System.out.print("Scelta (1-2): ");
        String startChoice = scanner.nextLine().trim();
        if (startChoice.equals("1")) {
            java.util.List<String> salvataggi = fileService.listaSalvataggi();
            if (salvataggi.isEmpty()) {
                System.out.println("Nessun salvataggio trovato. Inizializzo nuova partita.");
            } else {
                System.out.println("Salvataggi disponibili:");
                for (int i = 0; i < salvataggi.size(); i++) {
                    System.out.println(" " + (i + 1) + ") " + salvataggi.get(i));
                }
                System.out.print("Seleziona il numero del salvataggio da caricare (0 per annullare): ");
                String sel = scanner.nextLine().trim();
                try {
                    int idx = Integer.parseInt(sel);
                    if (idx > 0 && idx <= salvataggi.size()) {
                        String fileName = salvataggi.get(idx - 1);
                        try {
                            gioco = fileService.caricaGioco(fileName);
                            System.out.println("Partita caricata con successo da " + fileName);
                            loaded = true;
                        } catch (exception.InizializzaPartitaException e) {
                            System.out.println("Errore durante il caricamento della partita: " + e.getMessage());
                            System.out.println("Inizializzazione di una nuova partita.");
                        }
                    } else {
                        System.out.println("Nessuna azione eseguita. Inizializzo nuova partita.");
                    }
                } catch (NumberFormatException nfe) {
                    System.out.print("Vuoi inserire il nome del file manualmente? (S/N): ");
                    String yn = scanner.nextLine().trim().toLowerCase();
                    if (yn.equals("s") || yn.equals("si")) {
                        System.out.print("Inserisci il nome del file da caricare (es. 'partita_salvata.sav'): ");
                        String fileName = scanner.nextLine().trim();
                        try {
                            gioco = fileService.caricaGioco(fileName);
                            System.out.println("Partita caricata con successo da " + fileName);
                            loaded = true;
                        } catch (exception.InizializzaPartitaException e) {
                            System.out.println("Errore durante il caricamento della partita: " + e.getMessage());
                            System.out.println("Inizializzazione di una nuova partita.");
                        }
                    } else {
                        System.out.println("Inizializzazione di una nuova partita.");
                    }
                }
            }
        }

        int maxTot = 6;
        int numReali = 0;
        int numBot = 0;
        List<Personaggio> giocatori = new ArrayList<>();
        Dungeon dungeon = null;

        if (!loaded) {
            // *** 2 SCELTA NUMERO GIOCATORI REALI E BOT (somma <= 6) ***
            numReali = -1;
            while (numReali < 0 || numReali > maxTot) {
                System.out.print(ANSI.RESET + "Quanti giocatori reali? (0 - " + maxTot + "): ");
                String line = scanner.nextLine().trim();
                try {
                    numReali = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    System.out.println("Numero non valido, riprova.");
                }
            }

            int botRimanenti = maxTot - numReali;
            numBot = -1;
            while (numBot < 0 || (numReali + numBot) > maxTot || numBot > botRimanenti) {
                System.out.print(ANSI.RESET + "Quanti BOT vuoi aggiungere? (0 - " + botRimanenti + "): ");
                String line = scanner.nextLine().trim();
                try {
                    numBot = Integer.parseInt(line);
                    if ((numReali + numBot) > maxTot) {
                        System.out.println("La somma di giocatori reali e BOT non può superare " + maxTot + ". Riprova.");
                        numBot = -1;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Numero non valido, riprova.");
                }
            }

            // crea i giocatori reali chiedendo nome e classe
            for (int i = 1; i <= numReali; i++) {
                System.out.print(ANSI.RESET + "Inserisci il nome del giocatore " + i + ": ");
                String nome = scanner.nextLine().trim();
                while (nome.isEmpty()) {
                    System.out.print(ANSI.RESET + "Nome vuoto. Inserisci il nome del giocatore " + i + ": ");
                    nome = scanner.nextLine().trim();
                }

                int sceltaClasse = scegliClasse(scanner, nome);

                Personaggio personaggio = creaPersonaggioDaScelta(sceltaClasse, nome);
                giocatori.add(personaggio);
                fileService.writeLog("Personaggio " + personaggio.getNomePersonaggio().toUpperCase() + " ha scelto il tipo  " + sceltaClasse);
            }

            // crea i bot
            String[] nomiBot = {"Aldo", "Bea", "Ciro", "Dora", "Ezio", "Fiona", "Gino", "Hana"};
            Random rngBot = new Random();

            for (int i = 1; i <= numBot; i++) {
                String nome = "BOT_" + nomiBot[rngBot.nextInt(nomiBot.length)] + "_" + i;
                int sceltaClasse = rngBot.nextInt(4) + 1;
                Personaggio base = creaPersonaggioDaScelta(sceltaClasse, nome);
                Personaggio bot = giocatoreService.attribuisciPersonaggioAComputer(base);
                giocatori.add(bot);
                System.out.println("BOT creato: " + bot.getNomePersonaggio() + " (classe: " + bot.getClass().getSimpleName() + ")");
            }
            System.out.println(" Totale personaggi in partita: " + giocatori.size());

            // *** 4 CREAZIONE DUNGEON ***
            dungeon = dungeonFactory.creaDungeon();

            //stampa enorme
            System.out.println("--- STANZE BLOCCATE E CHIAVI ---");
            for (Stanza s : dungeon.getMappaStanze().values()) {
                String chiaveInfo = (s.getChiaveRichiesta() != null) ? String.valueOf(s.getChiaveRichiesta().getId()) : "nessuna";
                StringBuilder objs = new StringBuilder();
                for (int i = 0; i < s.getOggettiPresenti().size(); i++) {
                    Object o = s.getOggettiPresenti().get(i);
                    objs.append(o == null ? "<null>" : ((Oggetto) o).getNome());
                    if (i < s.getOggettiPresenti().size() - 1) {
                        objs.append(", ");
                    }
                }
                StringBuilder eventi = new StringBuilder();
                for (int i = 0; i < s.getListaEventiAttivi().size(); i++) {
                    Evento e = s.getListaEventiAttivi().get(i);
                    eventi.append(e == null ? "<null>" : e.getNomeEvento());
                    if (i < s.getListaEventiAttivi().size() - 1) {
                        eventi.append(", ");
                    }
                }
                String idColorato = ANSI.BOLD + ANSI.BRIGHT_RED + s.getId() + ANSI.RESET;
                String bloccataColorato = ANSI.BOLD + ANSI.BRIGHT_BLUE + s.isBloccata() + ANSI.RESET;
                String oggetti = ANSI.BOLD + ANSI.GREEN + objs + ANSI.RESET;
                System.out.println("Stanza id " + idColorato + " bloccata=" + bloccataColorato
                        + " chiaveId=" + chiaveInfo + "\n" + " oggetti=[" + oggetti + "]\n" + " eventi=[" + eventi + "]");
            }

            // *** 5 POSIZIONA GIOCATORI NELLA STANZA INIZIALE ***
            Stanza stanzaStart = dungeon.getStanza(0, 0);

            if (stanzaStart != null) {
                stanzaStart.setStatoStanza(false);
                System.out.println(" Posizionamento giocatori nella stanza iniziale (0,0)");

                for (Personaggio p : giocatori) {
                    p.setPosizioneCorrente(stanzaStart);
                    stanzaStart.getListaPersonaggi().add(p);
                }
            }

            // *** 7 DEBUG ***
            System.out.println("\nPersonaggi");
            giocatori.forEach(p -> System.out.println(" - " + p.getNomePersonaggio() + " in " + p.getPosizioneCorrente()));

            System.out.println("\n Partita inizializzata correttamente!");
            fileService.writeLog("Partita inizializzata con " + numReali + " giocatori reali e " + numBot + " BOT. Totale personaggi: " + giocatori.size());

            gioco.setListaPersonaggi(giocatori);
            gioco.setDungeon(dungeon);
            gioco.setListaStanze(new java.util.ArrayList<>(dungeon.getMappaStanze().values()));
        } else {
            fileService.writeLog("Partita caricata con " + gioco.getListaPersonaggi().size() + " personaggi. Stato gioco: " + gioco.getStatoGioco());
            // gioco caricato: riusa i personaggi salvati
            giocatori = gioco.getListaPersonaggi();
            // se il gioco caricato contiene il dungeon, usiamolo e ripristiniamo nella factory
            if (gioco.getDungeon() != null) {
                dungeon = gioco.getDungeon();
                dungeonFactory.setDungeon(dungeon);
            }
            if (giocatori == null) {
                giocatori = new ArrayList<>();
            }
            for (Personaggio p : giocatori) {
                String nome = p.getNomePersonaggio() == null ? "" : p.getNomePersonaggio();
                if (nome.startsWith("BOT_") || nome.toLowerCase().contains("bot")) {
                    numBot++;
                } else {
                    numReali++;
                }
            }
            System.out.println("\n Partita caricata correttamente! Totale personaggi: " + giocatori.size());
        }

        TurnoService turnoService = new TurnoService(dungeonFactory, fileService);
        GiocoService giocoService = new GiocoService(dungeonFactory, gioco);
        turnoService.setGiocoService(giocoService);

      
        // se abbiamo caricato un gioco con turno, comunichiamolo al TurnoService
        if (loaded && gioco.getTurno() != null) {
            turnoService.ripristinaTurnoSalvato(gioco.getTurno());
        }

        boolean continua = true;
        //  boolean autoMode = (numReali == 0);

        while (continua) {
            if (gioco.getStatoGioco() == StatoGioco.CONCLUSO) {
                continua = false;
                break;
            }
            turnoService.iniziaNuovoTurno(giocatori);

             if (gioco.getStatoGioco() == StatoGioco.CONCLUSO) {
        break;
    }
            
            // chiedi se continuare solo se ci sono giocatori reali; altrimenti prosegui automaticamente finché c'è almeno un personaggio vivo
            if (numReali > 0) {
                System.out.print(ANSI.RESET + "Continuare altri turni? (S/N): ");
                String c = scanner.nextLine().trim().toLowerCase();
                if (!c.equals("s") && !c.equals("si")) {
                    System.out.print("Vuoi salvare la partita prima di uscire? (S/N): ");
                    String saveChoice = scanner.nextLine().trim().toLowerCase();
                    if (saveChoice.equals("s") || saveChoice.equals("si")) {

                        System.out.println("Opzioni salvataggio: 1) Inserisci nome file  2) Salva con nome automatico (o scrivi direttamente il nome file)");
                        System.out.print("Scelta (1-2 o nome file): ");
                        String opt = scanner.nextLine().trim();
                        try {
                            // aggiorna l'oggetto gioco con lo stato corrente prima di salvare
                            gioco.setListaPersonaggi(giocatori);
                            if (dungeon != null) {
                                gioco.setDungeon(dungeon);
                                gioco.setListaStanze(new java.util.ArrayList<>(dungeon.getMappaStanze().values()));
                            }
                            String nomeDaUsare = null;
                            if (opt.equals("1")) {
                                System.out.print("Inserisci nome file (es: slot1.sav): ");
                                nomeDaUsare = scanner.nextLine().trim();
                            } else if (opt.equals("2") || opt.isEmpty()) {
                                // user chose automatic or left blank -> use automatic
                                nomeDaUsare = null;
                            } else {
                                // se l'utente ha inserito direttamente un nome (es. partita2.sav)
                                nomeDaUsare = opt;
                            }

                            if (nomeDaUsare != null) {
                                if (!nomeDaUsare.endsWith(".sav")) {
                                    nomeDaUsare = nomeDaUsare + ".sav";
                                }
                                fileService.salvaGioco(gioco, nomeDaUsare);
                                System.out.println("Partita salvata come '" + nomeDaUsare + "'. Percorso: " + Paths.get("salvataggi/" + nomeDaUsare).toAbsolutePath());
                            }
                        } catch (exception.InizializzaPartitaException e) {
                            System.out.println("Errore durante il salvataggio: " + e.getMessage());
                        }
                    }
                    continua = false;
                }
            } else {
                boolean almenoVivo = false;
                /*  for (Personaggio p : ordine) {
                    if (p != null && !p.èMorto(p)) { almenoVivo = true; break; }
                }*/
                continua = almenoVivo;
            }
        }
    }

    public static int scegliClasse(Scanner scanner, String who) {
        int sceltaClasse = 0;
        while (sceltaClasse < 1 || sceltaClasse > 4) {
            System.out.println("Scegli la classe per " + who + ":");
            System.out.println(" 1) Guerriero");
            System.out.println(" 2) Mago");
            System.out.println(" 3) Arciere");
            System.out.println(" 4) Paladino");
            System.out.print(ANSI.RESET + "Quale eroe vuoi essere? (1-4): ");
            String input = scanner.nextLine().trim();
            try {
                sceltaClasse = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                String s = input.toLowerCase();
                if (s.startsWith("g")) {
                    sceltaClasse = 1;
                } else if (s.startsWith("m")) {
                    sceltaClasse = 2;
                } else if (s.startsWith("a")) {
                    sceltaClasse = 3;
                } else if (s.startsWith("p")) {
                    sceltaClasse = 4;
                } else {
                    System.out.println("Scelta non valida, riprova.");
                }
            }
            if (sceltaClasse < 1 || sceltaClasse > 4) {
                System.out.println("Scelta non valida, riprova.");
            }
        }
        return sceltaClasse;
    }

    private static Personaggio creaPersonaggioDaScelta(int sceltaClasse, String nome) {
        Personaggio p;
        switch (sceltaClasse) {
            case 1:
                p = new GuerrieroServiceImpl().creaPersonaggio(nome, null);
                break;
            case 2:
                p = new MagoServiceImpl().creaPersonaggio(nome, null);
                break;
            case 3:
                p = new ArciereServiceImpl().creaPersonaggio(nome, null);
                break;
            case 4:
                p = new PaladinoServiceImpl().creaPersonaggio(nome, null);
                break;
            default:
                p = new GuerrieroServiceImpl().creaPersonaggio(nome, null);
        }
        return p;
    }

}
