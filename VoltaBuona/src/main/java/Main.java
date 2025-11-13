
import domain.Dungeon;
import domain.Stanza;
import service.GiocoService;
import service.StanzaFactory;
import service.StanzaFactory.StatoStanza;

public class Main {

    public static void main(String[] args) {

        StanzaFactory factory = new StanzaFactory();
        GiocoService giocoService = new GiocoService(factory);

        // 1️ Leggi la configurazione
        int[] dimensioni = giocoService.leggiConfigurazione(
                "C:/Users/hp/Desktop/Univaq-DungeounQuest/VoltaBuona/config.txt"
        );

        int righe = dimensioni[0];
        int colonne = dimensioni[1];

        // 2️ Crea il dungeon tramite GiocoService
        Dungeon dungeon = giocoService.creaDungeon(righe, colonne);

        // 3️ Segna la stanza iniziale come VISITATA
        Stanza stanzaStart = dungeon.getStanza(0, 0);
        if (stanzaStart != null) {
            stanzaStart.setStatoS(StatoStanza.VISITATA);
        }

        // 4️ Stampa la mappa del dungeon
        dungeon.stampaMappa(righe, colonne);

        // 5️⃣ (Facoltativo) Debug
        // giocoService.debugDungeon();
    }
}
