
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import domain.Dungeon;
import domain.Gioco;
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
import service.impl.RandomSingleton;
import service.impl.ScannerSingleton;
import util.ANSI;

public class Main {

    public static void main(String[] args) throws IOException {

        ScannerSingleton scannerGenerale = ScannerSingleton.getInstance();
        FileService fileService = new FileService();
        RandomSingleton randomGenerale = RandomSingleton.getInstance();

        StanzaFactory stanzaFactory = new StanzaFactory();
        DungeonFactory dungeonFactory = new DungeonFactory(stanzaFactory);
        List<Personaggio> partecipanti = new ArrayList<>();

        Gioco gioco = new Gioco(1, new ArrayList<>(), partecipanti, new ArrayList<>(), new ArrayList<>(), 0, StatoGioco.INIZIO, null, null);
        GiocatoreService giocatoreService = new GiocatoreService();
        fileService.setLogFileNome("partita.log");

        boolean caricato = false;
        System.out.println("Gestione salvataggi");
        System.out.println("1) Carica un salvataggio esistente");
        System.out.println("2) Inizia nuova partita");
        System.out.print("Scelta (1-2): ");
        String sceltaIniziale = scannerGenerale.leggiLinea().trim();
        if (sceltaIniziale.equals("1")) {
            List<String> salvataggi = fileService.listaSalvataggi();
            if (salvataggi.isEmpty()) {
                System.out.println("Nessun salvataggio trovato. Inizializzo nuova partita.");
            } else {
                System.out.println("Salvataggi disponibili:");
                for (int i = 0; i < salvataggi.size(); i++) {
                    System.out.println(" " + (i + 1) + ") " + salvataggi.get(i));
                }
                System.out.print("Seleziona il numero del salvataggio da caricare (0 per annullare): ");
                String seleziona = scannerGenerale.leggiLinea().trim();

                int indice = Integer.parseInt(seleziona);
                if (indice > 0 && indice <= salvataggi.size()) {
                    String fileName = salvataggi.get(indice - 1);
                    try {
                        gioco = fileService.caricaGioco(fileName);
                        System.out.println("Partita caricata con successo da " + fileName);
                        caricato = true;
                    } catch (exception.InizializzaPartitaException e) {
                        System.out.println("Errore durante il caricamento della partita: " + e.getMessage());
                        System.out.println("Inizializzazione di una nuova partita.");
                    }
                } else {
                    System.out.println("Partita inizializzata correttamente.");

                }
            }
        }
        int giocatoriTotali = 6;
        int giocatoriReali = 0;
        int giocatoriBot = 0;
        List<Personaggio> giocatori = new ArrayList<>();
        Dungeon dungeon = null;

        if (!caricato) {
            // Il numero di giocatori totali è massimo 6
            giocatoriReali = -1;
            while (giocatoriReali < 0 || giocatoriReali > giocatoriTotali) {
                System.out.print(ANSI.RESET + "Quanti giocatori reali? (0 - " + giocatoriTotali + "): ");
                String line = scannerGenerale.leggiLinea().trim();
                try {
                    giocatoriReali = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    System.out.println("Numero non valido, riprova.");
                }
            }

            int botRimanenti = giocatoriTotali - giocatoriReali;
            giocatoriBot = -1;
            while (giocatoriBot < 0 || (giocatoriReali + giocatoriBot) > giocatoriTotali || giocatoriBot > botRimanenti) {
                System.out.print(ANSI.RESET + "Quanti BOT vuoi aggiungere? (0 - " + botRimanenti + "): ");
                String line = scannerGenerale.leggiLinea().trim();
                try {
                    giocatoriBot = Integer.parseInt(line);
                    if ((giocatoriReali + giocatoriBot) > giocatoriTotali) {
                        System.out.println("La somma di giocatori reali e BOT non può superare " + giocatoriTotali + ". Riprova.");
                        giocatoriBot = -1;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Numero non valido, riprova.");
                }
            }

            // creiamo i giocatori reali chiedendo nome e classe
            for (int i = 1; i <= giocatoriReali; i++) {
                System.out.print(ANSI.RESET + "Inserisci il nome del giocatore " + i + ": ");
                String nome = scannerGenerale.leggiLinea().trim();
                while (nome.isEmpty()) {
                    System.out.print(ANSI.RESET + "Nome vuoto. Inserisci il nome del giocatore " + i + ": ");
                    nome = scannerGenerale.leggiLinea().trim();
                }

                int sceltaClasse = scegliClasse(scannerGenerale, nome);

                Personaggio personaggio = creaPersonaggioDaScelta(sceltaClasse, nome);
                giocatori.add(personaggio);
                FileService.getInstance().writeLog("Personaggio " + personaggio.getNomePersonaggio().toUpperCase() + " ha scelto il tipo  " + sceltaClasse);
            }

            // crea i bot
            String[] nomiBot = {"Aldo", "Bea", "Ciro", "Dora", "Ezio", "Fiona", "Gino", "Hana"};
            Random rngBot = new Random();

            for (int i = 1; i <= giocatoriBot; i++) {
                String nome = "BOT_" + nomiBot[rngBot.nextInt(nomiBot.length)] + "_" + i;
                int sceltaClasse = rngBot.nextInt(4) + 1;
                Personaggio base = creaPersonaggioDaScelta(sceltaClasse, nome);
                Personaggio bot = giocatoreService.attribuisciPersonaggioAComputer(base);
                giocatori.add(bot);
                //System.out.println("BOT creato: " + bot.getNomePersonaggio() + " (classe: " + bot.getClass().getSimpleName() + ")");
            }
            System.out.println(" Totale personaggi in partita: " + giocatori.size());

            // Creazione Dungeon
            dungeon = dungeonFactory.creaDungeon();

            Stanza stanzaStart = dungeon.getStanza(0, 0);

            if (stanzaStart != null) {
                stanzaStart.setStatoStanza(false);
                System.out.println(" Posizionamento giocatori nella stanza iniziale (0,0)");

                for (Personaggio personaggio : giocatori) {
                    personaggio.setPosizioneCorrente(stanzaStart);
                    stanzaStart.getListaPersonaggi().add(personaggio);
                }
            }

            System.out.println("\nPersonaggi");
            giocatori.forEach(p -> System.out.println(" - " + p.getNomePersonaggio() + " in " + p.getPosizioneCorrente()));

            System.out.println("\n Partita inizializzata correttamente!");
            FileService.getInstance().writeLog("Partita inizializzata con " + giocatoriReali + " giocatori reali e " + giocatoriBot + " BOT. Totale personaggi: " + giocatori.size());

            gioco.setListaPersonaggi(giocatori);
            gioco.setDungeon(dungeon);
            gioco.setListaStanze(new ArrayList<>(dungeon.getMappaStanze().values()));
        } else {
            FileService.getInstance().writeLog("Partita caricata con " + gioco.getListaPersonaggi().size() + " personaggi. Stato gioco: " + gioco.getStatoGioco());
            // gioco caricato: riusa i personaggi salvati
            giocatori = gioco.getListaPersonaggi();

            if (gioco.getDungeon() != null) {
                dungeon = gioco.getDungeon();
                dungeonFactory.setDungeon(dungeon);
            }
            if (giocatori == null) {
                giocatori = new ArrayList<>();
            }
            for (Personaggio personaggio : giocatori) {
                String nome = personaggio.getNomePersonaggio() == null ? "" : personaggio.getNomePersonaggio();
                if (nome.startsWith("BOT_") || nome.toLowerCase().contains("bot")) {
                    giocatoriBot++;
                } else {
                    giocatoriReali++;
                }
            }
            System.out.println("\n Partita caricata correttamente! Totale personaggi: " + giocatori.size());
        }

        GiocoService giocoService = new GiocoService(dungeonFactory, gioco);
        TurnoService turnoService = new TurnoService(dungeonFactory, fileService, giocoService);

        turnoService.setGiocoService(giocoService);

        // se abbiamo caricato un gioco con turno, comunichiamolo al TurnoService
        if (caricato && gioco.getTurno() != null) {
            turnoService.ripristinaTurnoSalvato(gioco.getTurno());
        }

        boolean continua = true;
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
            if (giocatoriReali > 0) {
                System.out.print(ANSI.RESET + "Continuare altri turni? (S/N): ");
                String risposta = scannerGenerale.leggiLinea().trim().toLowerCase();
                if (!risposta.equals("s") && !risposta.equals("si")) {
                    System.out.print("Vuoi salvare la partita prima di uscire? (S/N): ");
                    String salvaScelta = scannerGenerale.leggiLinea().trim().toLowerCase();
                    if (salvaScelta.equals("s") || salvaScelta.equals("si")) {
                        System.out.println("Inserisci nome file aggiungendo .sav alla fine (se lo dimentichi lo aggiunge in automatico):");
                        String nomeDaUsare = scannerGenerale.leggiLinea().trim();
                        try {
                            // aggiorna l'oggetto gioco con lo stato corrente prima di salvare
                            gioco.setListaPersonaggi(giocatori);
                            if (dungeon != null) {
                                gioco.setDungeon(dungeon);
                                gioco.setListaStanze(new java.util.ArrayList<>(dungeon.getMappaStanze().values()));
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
                continua = almenoVivo;
            }
        }
    }

    public static int scegliClasse(ScannerSingleton scannerGenerale, String personaggio) {
        int sceltaClasse = 0;
        while (sceltaClasse < 1 || sceltaClasse > 4) {
            System.out.println("Scegli la classe per " + personaggio + ":");
            System.out.println(" 1) Guerriero");
            System.out.println(" 2) Mago");
            System.out.println(" 3) Arciere");
            System.out.println(" 4) Paladino");
            System.out.print(ANSI.RESET + "Quale eroe vuoi essere? (1-4): ");
            String input = scannerGenerale.leggiLinea().trim();
            try {
                sceltaClasse = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                String stringa = input.toLowerCase();
                if (stringa.startsWith("g")) {
                    sceltaClasse = 1;
                } else if (stringa.startsWith("m")) {
                    sceltaClasse = 2;
                } else if (stringa.startsWith("a")) {
                    sceltaClasse = 3;
                } else if (stringa.startsWith("p")) {
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
        Personaggio personaggio;
        switch (sceltaClasse) {
            case 1:
                personaggio = new GuerrieroServiceImpl().creaPersonaggio(nome, null);
                break;
            case 2:
                personaggio = new MagoServiceImpl().creaPersonaggio(nome, null);
                break;
            case 3:
                personaggio = new ArciereServiceImpl().creaPersonaggio(nome, null);
                break;
            case 4:
                personaggio = new PaladinoServiceImpl().creaPersonaggio(nome, null);
                break;
            default:
                personaggio = new GuerrieroServiceImpl().creaPersonaggio(nome, null);
        }
        return personaggio;
    }

}
