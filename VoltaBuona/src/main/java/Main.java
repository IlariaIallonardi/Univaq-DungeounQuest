
import domain.Stanza;
import service.GiocoService;
import service.StanzaFactory;
import service.StanzaFactory.StatoStanza;

public class Main {

    public static void main(String[] args) {
        StanzaFactory factory = new StanzaFactory();
        GiocoService giocoService = new GiocoService(factory);

        // 1 Legge la configurazione da file
        int[] dimensioni = giocoService.leggiConfigurazione("config.txt");
        int righe = dimensioni[0];
        int colonne = dimensioni[1];

        // 2 Crea il dungeon
        giocoService.creaDungeon(righe, colonne);
        GiocoService.Coordinate start = new GiocoService.Coordinate(0, 0);
        Stanza stanzaStart = giocoService.getDungeon().get(start);
        if (stanzaStart != null) {
            stanzaStart.setStatoS(StatoStanza.VISITATA);
        }

        // 3️ Stampa la mappa (non spoilerata)
        giocoService.stampaMappa(righe, colonne);

        // 4️ Mostra info di debug (facoltativo)
        

    }
}
