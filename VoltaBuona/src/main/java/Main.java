
import domain.Dungeon;
import domain.Stanza;
import service.DungeonFactory;
import service.GiocoService;
import service.StanzaFactory;
import service.StanzaFactory.StatoStanza;  

public class Main {

    public static void main(String[] args) {

        StanzaFactory factory = new StanzaFactory();
        DungeonFactory dungeonFactory = new DungeonFactory(factory);

        
        GiocoService giocoService = new GiocoService(dungeonFactory);

        // 1️ Leggi la configurazione
        int[] dimensioni = giocoService.leggiConfigurazione(
                "C:/Users/asjav/Desktop/Dungeon/Univaq-DungeounQuest/VoltaBuona/config.txt"
        );

        int righe = dimensioni[0];
        int colonne = dimensioni[1];

        // 2️ Crea il dungeon tramite DungeonFactory
        Dungeon dungeon = dungeonFactory.creaDungeon(righe, colonne);

        // 3️ Segna la stanza iniziale come VISITATA
        Stanza stanzaStart = dungeon.getStanza(0, 0);
        if (stanzaStart != null) {
            stanzaStart.setStatoS(StatoStanza.VISITATA);
        }

        // 4️ Stampa la mappa del dungeon
        dungeon.stampaMappa(righe, colonne);

        // 5 (Facoltativo) Debug
        // giocoService.debugDungeon();
    }
}
