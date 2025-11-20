
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

        // *** 1️⃣ LETTURA CONFIGURAZIONE ***
        int[] dimensioni = giocoService.leggiConfigurazione(
                "C:/Usersh/p/Desktop/Univaq-DungeounQuest/VoltaBuona/config.txt"
        );

        int righe = dimensioni[0];
        int colonne = dimensioni[1];

        System.out.println("🔧 Configurazione caricata: " + righe + "x" + colonne);

        // *** 2️⃣ SCELTA NUMERO GIOCATORI REALI ***
        System.out.print("👥 Quanti giocatori reali? (1 - 4): ");
        int numReali = scanner.nextInt();
        scanner.nextLine();

        List<Personaggio> giocatori = new ArrayList<>();

        for (int i = 1; i <= numReali; i++) {
            System.out.print("👉 Inserisci il nome del giocatore " + i + ": ");
            String nome = scanner.nextLine();

            // TODO: in futuro potrai far scegliere la CLASSE (mago, guerriero, ecc.)
            // Per ora mettiamo un guerriero human di default:
            giocatori.add(new domain.Guerriero(
                    100, 15, 10, 0,
                    1, nome,
                    null, 0, 0,
                    "NORMALE", new domain.Zaino()
            ));
        }

        // *** 3️⃣ SCELTA NUMERO BOT ***
        System.out.print("🤖 Quanti BOT vuoi aggiungere? (0 - 4): ");
        int numBot = scanner.nextInt();

        for (int i = 0; i < numBot; i++) {
            Personaggio bot = giocatoreService.attribuisciPersonaggioAComputer(null);
            giocatori.add(bot);
            System.out.println("🤖 BOT creato: " + bot.getNomeP());
        }

        System.out.println("\n🎮 Totale personaggi in partita: " + giocatori.size());

        // *** 4️⃣ CREAZIONE DUNGEON ***
        Dungeon dungeon = dungeonFactory.creaDungeon(righe, colonne);

        // *** 5️⃣ POSIZIONA GIOCATORI NELLA STANZA INIZIALE ***
        Stanza stanzaStart = dungeon.getStanza(0, 0);

        if (stanzaStart != null) {
            stanzaStart.setStatoS(StanzaFactory.StatoStanza.VISITATA);
            System.out.println("🚪 Posizionamento giocatori nella stanza iniziale (0,0)");

            for (Personaggio p : giocatori) {
                p.setPosizioneCorrente(stanzaStart);
                stanzaStart.getListaPersonaggi().add(p);
            }
        }

        // *** 6️⃣ STAMPA MAPPA ***
        dungeon.stampaMappa(righe, colonne);

        // *** 7️⃣ DEBUG ***
        System.out.println("\n--- DEBUG PERSONAGGI ---");
        giocatori.forEach(p -> System.out.println(" - " + p.getNomeP() + " in " + p.getPosizioneCorrente()));

        System.out.println("\n🎉 Partita inizializzata correttamente!");
    }
}
