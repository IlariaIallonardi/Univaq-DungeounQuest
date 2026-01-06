package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.Chiave;
import domain.Dungeon;
import domain.Stanza;

public class DungeonFactory {

    private Dungeon dungeon;
    private final StanzaFactory stanzaFactory;
    private int righe = 0;
    private int colonne = 0;

    public DungeonFactory(StanzaFactory stanzaFactory) {
        this.stanzaFactory = stanzaFactory;
    }

    private final Map<String, Stanza> stanzeMappa = new HashMap<>();

    public Dungeon creaDungeon(int righe, int colonne) {

        Dungeon d = new Dungeon(righe, colonne);

        int idCounter = 0;
        for (int y = 0; y < righe; y++) {
            for (int x = 0; x < colonne; x++) {
                Stanza stanza = stanzaFactory.creaStanza(idCounter++, x, y);
                d.setStanza(x, y, stanza);
            }
        }
        d.collegaAdiacenti();

        // posiziona le chiavi per le stanze bloccate in altre stanze casuali
        var allStanze = new ArrayList<>(d.getMappaStanze().values());
        java.util.Random rnd = new java.util.Random();

        for (Stanza stanzaBloccata : allStanze) {
            if (!stanzaBloccata.isBloccata()) continue;

            // crea la chiave per questa stanza bloccata
            //Chiave chiave = stanzaFactory.creaChiavePerStanza(stanzaBloccata.getId());
            Chiave chiave = new service.impl.ChiaveServiceImpl().creaChiavePerStanza(stanzaBloccata.getId());
            stanzaBloccata.setChiaveRichiesta(chiave);
            stanzaBloccata.setBloccata(true);

            // scegli una stanza diversa dove mettere la chiave
            List<Stanza> candidati = new ArrayList<>();
            for (Stanza s : allStanze) {
                if (s.getId() == stanzaBloccata.getId()) continue;

                // escludi la stanza di start (coordinate 0,0) se presenti
                int[][] coord = s.getCoordinate();
                if (coord != null && coord.length > 0 && coord[0][0] == 0 && coord[0][1] == 0) continue;

                // escludi stanze bloccate
                if (s.isBloccata()) continue;

                candidati.add(s);
            }

            if (candidati.isEmpty()) {
                // fallback: riapplica i filtri ma più permissivi (escludi solo stanza bloccata)
                for (Stanza s : allStanze) {
                    if (s.getId() == stanzaBloccata.getId()) continue;
                    int[][] coord = s.getCoordinate();
                    if (coord != null && coord.length > 0 && coord[0][0] == 0 && coord[0][1] == 0) continue;
                    if (s.isBloccata()) continue;
                    candidati.add(s);
                }
            }

            if (candidati.isEmpty()) {
                // ultima risorsa: qualsiasi stanza diversa dalla bloccata
                for (Stanza s : allStanze) if (s.getId() != stanzaBloccata.getId()) candidati.add(s);
            }

            if (candidati.isEmpty()) {
                // niente stanze disponibili (caso teorico)
                continue;
            }

            Stanza dove = candidati.get(rnd.nextInt(candidati.size()));
            if (dove.getId() == stanzaBloccata.getId()) {
                // sicurezza aggiuntiva: non posizionare nella stessa stanza
                continue;
            }
            dove.aggiungiOggetto(chiave);
        }

        this.dungeon = d;
        return d;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

}