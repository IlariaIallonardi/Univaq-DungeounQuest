
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import domain.Arciere;
import domain.Dungeon;
import domain.Guerriero;
import domain.Mago;
import domain.Paladino;
import domain.Personaggio;
import domain.Stanza;
import service.DungeonFactory;
import service.GiocatoreService;
import service.PersonaggioService;
import service.StanzaFactory;
import service.TurnoService;
import service.impl.GiocoServiceImpl;
import service.impl.PassaggioSegretoServiceImpl;
import service.impl.TurnoServiceImpl;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // FACTORY
        StanzaFactory stanzaFactory = new StanzaFactory();
        DungeonFactory dungeonFactory = new DungeonFactory(stanzaFactory);

        // SERVICE PRINCIPALE
      //  GiocoService giocoService = new GiocoServiceImpl(dungeonFactory);
        GiocatoreService giocatoreService = new GiocatoreService();

        // *** 1 LETTURA CONFIGURAZIONE ***
     //   int[] dimensioni = giocoService.leggiConfigurazione(
     //           "Univaq-DungeounQuest\\VoltaBuona\\config.txt"
     //   );
int [] dimensioni = {4, 4}; // valori di esempio
        int righe = dimensioni[0];
        int colonne = dimensioni[1];

        System.out.println(" Configurazione caricata: " + righe + "x" + colonne);

        // *** 2 SCELTA NUMERO GIOCATORI REALI E BOT (somma <= 6) ***
        int maxTot = 6;
        int numReali = -1;
        while (numReali < 0 || numReali > maxTot) {
            System.out.print("Quanti giocatori reali? (0 - " + maxTot + "): ");
            String line = scanner.nextLine().trim();
            try {
                numReali = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Numero non valido, riprova.");
            }
        }

        int numBot = -1;
        int botRimanenti=maxTot-numReali;
        while (numBot < 0 || (numReali + numBot) > maxTot || numBot > botRimanenti) {
            System.out.print("Quanti BOT vuoi aggiungere? (0 - " + botRimanenti + "): ");
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

        List<Personaggio> giocatori = new ArrayList<>();

        // crea i giocatori reali chiedendo nome e classe
        for (int i = 1; i <= numReali; i++) {
            System.out.print("Inserisci il nome del giocatore " + i + ": ");
            String nome = scanner.nextLine().trim();
            while (nome.isEmpty()) {
                System.out.print("Nome vuoto. Inserisci il nome del giocatore " + i + ": ");
                nome = scanner.nextLine().trim();
            }

            int sceltaClasse = scegliClasse(scanner, nome);
            Personaggio p = creaPersonaggioDaScelta(sceltaClasse, nome);
            giocatori.add(p);
        }

        // crea i bot chiedendo nome e classe (anche i bot scelgono nome e classe)
        for (int i = 1; i <= numBot; i++) {
            System.out.print("Inserisci il nome del BOT " + i + ": ");
            String nome = scanner.nextLine().trim();
            while (nome.isEmpty()) {
                System.out.print("Nome vuoto. Inserisci il nome del BOT " + i + ": ");
                nome = scanner.nextLine().trim();
            }
            int sceltaClasse = scegliClasse(scanner, nome + " (BOT)");
            Personaggio p = creaPersonaggioDaScelta(sceltaClasse, nome);
            giocatori.add(p);
            System.out.println("BOT creato: " + p.getNomePersonaggio());
        }
    System.out.println(" Totale personaggi in partita: " + giocatori.size());

        // *** 4 CREAZIONE DUNGEON ***
        Dungeon dungeon = dungeonFactory.creaDungeon(righe, colonne);

        // *** 5 POSIZIONA GIOCATORI NELLA STANZA INIZIALE ***
        Stanza stanzaStart = dungeon.getStanza(0, 0);

        if (stanzaStart != null) {
            stanzaStart.setStatoS(StanzaFactory.StatoStanza.VISITATA);
            System.out.println(" Posizionamento giocatori nella stanza iniziale (0,0)");

            for (Personaggio p : giocatori) {
                p.setPosizioneCorrente(stanzaStart);
                stanzaStart.getListaPersonaggi().add(p);
            }
        }

        // *** 6 STAMPA MAPPA ***
        dungeon.stampaMappa(righe, colonne);

        // *** 7 DEBUG ***
        System.out.println("\n--- DEBUG PERSONAGGI ---");
        giocatori.forEach(p -> System.out.println(" - " + p.getNomePersonaggio() + " in " + p.getPosizioneCorrente()));

        System.out.println("\n Partita inizializzata correttamente!");

        // semplice loop di turni per scegliere azioni e continuare la partita
        PersonaggioService ps = new PersonaggioService() {
            @Override
            public Personaggio creaPersonaggio(String nome, Personaggio personaggio) {
                return personaggio;
            }

            @Override
            public int attacca(Personaggio personaggio, domain.Mostro mostro, domain.Combattimento combattimento) {
                return 0;
            }

            @Override
            public void usaAbilitàSpeciale(Personaggio personaggio, String abilitàSpeciale) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'usaAbilitàSpeciale'");
            }
        };

        TurnoService turnoService = new TurnoServiceImpl(new GiocoServiceImpl(), ps, new PassaggioSegretoServiceImpl());
        System.out.println("\nInizio turni di prova. Digita 'q' quando richiesto per uscire.");
        boolean continua = true;
        while (continua) {
            for (Personaggio p : giocatori) {
                System.out.println("\nTurno di: " + p.getNomePersonaggio());
                turnoService.iniziaNuovoTurno(giocatori, scanner);
            }
            System.out.print("Continuare altri turni? (S/N): ");
            String c = scanner.nextLine().trim().toLowerCase();
            if (!c.equals("s") && !c.equals("si")) {
                continua = false;
            }
        }
    }

    private static int scegliClasse(Scanner scanner, String who) {
        int sceltaClasse = 0;
        while (sceltaClasse < 1 || sceltaClasse > 4) {
            System.out.println("Scegli la classe per " + who + ":");
            System.out.println(" 1) Guerriero");
            System.out.println(" 2) Mago");
            System.out.println(" 3) Arciere");
            System.out.println(" 4) Paladino");
            System.out.print("Quale eroe vuoi essere? (1-4): ");
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
                p = new Guerriero(nome, null, sceltaClasse, sceltaClasse, sceltaClasse, sceltaClasse, nome, null, false, sceltaClasse, sceltaClasse, nome, sceltaClasse, sceltaClasse, sceltaClasse, sceltaClasse, null);
                break;
            case 2:
                p = new Mago(nome, null, sceltaClasse, sceltaClasse, sceltaClasse, sceltaClasse, nome, null, false, sceltaClasse, sceltaClasse, nome, sceltaClasse, sceltaClasse, sceltaClasse, sceltaClasse, null);
                break;
            case 3:
                p = new Arciere(nome, null, sceltaClasse, sceltaClasse, sceltaClasse, sceltaClasse, nome, null, false, sceltaClasse, sceltaClasse, nome, sceltaClasse, sceltaClasse, sceltaClasse, sceltaClasse, null);
                break;
            case 4:
                p = new Paladino(nome, null, sceltaClasse, sceltaClasse, sceltaClasse, sceltaClasse, nome, null, false, sceltaClasse, sceltaClasse, nome, sceltaClasse, sceltaClasse, sceltaClasse, sceltaClasse, null);
                break;
            default:
                p = new Guerriero(nome, null, sceltaClasse, sceltaClasse, sceltaClasse, sceltaClasse, nome, null, false, sceltaClasse, sceltaClasse, nome, sceltaClasse, sceltaClasse, sceltaClasse, sceltaClasse, null);
        }
        return p;
    }

  

}
