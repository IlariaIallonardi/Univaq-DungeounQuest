package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.Chiave;
import domain.Dungeon;
import domain.Stanza;
import service.impl.RandomSingleton;

public class DungeonFactory {

    private Dungeon dungeon;
    private final StanzaFactory stanzaFactory;
    private RandomSingleton randomGenerale = RandomSingleton.getInstance();
    private int righe;
    private int colonne;

    public DungeonFactory(StanzaFactory stanzaFactory) {
        this.stanzaFactory = stanzaFactory;
    }

    public Map<String, Stanza> stanzeMappa = new HashMap<>();

    public Dungeon creaDungeon() {
        righe = randomGenerale.prossimoNumero(4, 7);
        colonne = randomGenerale.prossimoNumero(4, 7);
        Dungeon dungeon = new Dungeon(righe, colonne);

        int contatoreStanza = 0;
        for (int y = 0; y < righe; y++) {
            for (int x = 0; x < colonne; x++) {
                Stanza stanza = stanzaFactory.creaStanza(contatoreStanza++, x, y);
                dungeon.setStanza(x, y, stanza);
            }
        }
        dungeon.collegaAdiacenti();

        // Si posizionano le chiavi per le stanze bloccate in altre stanze casuali
        List<Stanza> tutteStanze = new ArrayList<>(dungeon.getMappaStanze().values());

        for (Stanza stanzaBloccata : tutteStanze) {
            if (!stanzaBloccata.isBloccata())
                continue;

            // Creiamo la chiave per la stanza bloccata.
            Chiave chiave = new service.impl.ChiaveServiceImpl().creaChiavePerStanza(stanzaBloccata.getId());
            stanzaBloccata.setChiaveRichiesta(chiave);
            stanzaBloccata.setBloccata(true);

            // Scegliamo una stanza diversa dove mettere la chiave.
            List<Stanza> stanzeSbloccate = new ArrayList<>();
            for (Stanza stanza : tutteStanze) {
                if (stanza.getId() == stanzaBloccata.getId())
                    continue;

                // Escludiamo la stanza iniziale (coordinate 0,0).
                int[][] coord = stanza.getCoordinate();
                if (coord != null && coord.length > 0 && coord[0][0] == 0 && coord[0][1] == 0)
                    continue;

                // Escludiamo le stanze bloccate.
                if (stanza.isBloccata())
                    continue;

                stanzeSbloccate.add(stanza);
            }

            // Controllo ulteriore.
            if (stanzeSbloccate.isEmpty()) {

                for (Stanza s : tutteStanze)
                    if (s.getId() != stanzaBloccata.getId())
                        stanzeSbloccate.add(s);
            }

        }

        this.dungeon = dungeon;
        return dungeon;
    }

    // Stampiamo la mappa.
    public void stampaMappa() {
        System.out.println("Mappa del Dungeon:");

        
        System.out.println("Dimensioni: " + righe + "x" + colonne);
        for (int y = 0; y < this.getRighe(); y++) {
            for (int x = 0; x < this.getColonne(); x++) {
        
                 Stanza s = dungeon.getStanza(x, y);
                if (s == null) {
                    System.out.print("   ");
                } else {
                    System.out.print(s.getStatoStanza() == true ? " V " : " F ");
                }
            
            }
            System.out.println();
        }
        System.out.println("---------------------\n");
        System.out.println();
                
            }
        


    

    public int getRighe() {
        return righe;
    }

    public int getColonne() {
        return colonne;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

}