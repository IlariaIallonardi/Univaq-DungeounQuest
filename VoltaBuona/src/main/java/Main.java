
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import domain.Dungeon;
import domain.Personaggio;
import domain.Stanza;
import service.DungeonFactory;
import service.GiocatoreService;
import service.GiocoService;
import service.StanzaFactory;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // FACTORY
        StanzaFactory stanzaFactory = new StanzaFactory();
        DungeonFactory dungeonFactory = new DungeonFactory(stanzaFactory);

        // SERVICE PRINCIPALE
        GiocoService giocoService = new GiocoService(dungeonFactory);
        GiocatoreService giocatoreService = new GiocatoreService();

        // *** 1 LETTURA CONFIGURAZIONE ***
        int[] dimensioni = giocoService.leggiConfigurazione(
                "C:/Usersh/p/Desktop/Univaq-DungeounQuest/VoltaBuona/config.txt"
        );

        int righe = dimensioni[0];
        int colonne = dimensioni[1];

        System.out.println(" Configurazione caricata: " + righe + "x" + colonne);

        // *** 2 SCELTA NUMERO GIOCATORI REALI ***
        System.out.print(" Quanti giocatori reali? (1 - 4): ");
        int numReali = scanner.nextInt();
        scanner.nextLine();

        List<Personaggio> giocatori = new ArrayList<>();

       // ...existing code...
        for (int i = 1; i <= numReali; i++) {
            System.out.print(" Inserisci il nome del giocatore " + i + ": ");
            String nome = scanner.nextLine();

            // scelta della classe
            int sceltaClasse = 0;
            while (sceltaClasse < 1 || sceltaClasse > 4) {
                System.out.println(" Scegli la classe per " + nome + ":");
                System.out.println("  1) Guerriero");
                System.out.println("  2) Mago");
                System.out.println("  3) Arciere");
                System.out.println("  4) Paladino");
                System.out.print("Quale eroe vuoi essere?(1-4): ");
                String input = scanner.nextLine().trim();
                try {
                    sceltaClasse = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    // accetta anche parole chiave iniziali
                    String s = input.toLowerCase();
                    if (s.startsWith("g")) sceltaClasse = 1;
                    else if (s.startsWith("m")) sceltaClasse = 2;
                    else if (s.startsWith("a")) sceltaClasse = 3;
                    else if (s.startsWith("p")) sceltaClasse = 4;
                    else {
                        System.out.println(" Scelta non valida, riprova.");
                        continue;
                    }
                }
                if (sceltaClasse < 1 || sceltaClasse > 4) {
                    System.out.println(" Scelta non valida, riprova.");
                }
            }

            // crea l'istanza della classe scelta (adatta i costruttori se necessario)
            domain.Personaggio p;
            switch (sceltaClasse) {
                case 1:
                    p = new domain.Guerriero(nome);
                    break;
                case 2:
                    p = new domain.Mago(nome);
                    break;
                case 3:
                    p = new domain.Arciere(nome);
                    break;
                case 4:
                    p = new domain.Paladino(nome);
                    break;
                default:
                    p = new domain.Guerriero(nome);
            }

            giocatori.add(p);
        }
// ...existing code...
        // *** 3 SCELTA NUMERO BOT ***
        System.out.print(" Quanti BOT vuoi aggiungere? (0 - 4): ");
        int numBot = scanner.nextInt();

        for (int i = 0; i < numBot; i++) {
            Personaggio bot = giocatoreService.attribuisciPersonaggioAComputer(null);
            giocatori.add(bot);
            System.out.println(" BOT creato: " + bot.getNomeP());
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
        giocatori.forEach(p -> System.out.println(" - " + p.getNomeP() + " in " + p.getPosizioneCorrente()));

        System.out.println("\n Partita inizializzata correttamente!");
    }
}
