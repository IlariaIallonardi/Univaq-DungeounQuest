package service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import domain.Stanza;
import service.GiocoService.Coordinate;
import service.StanzaFactory.StatoStanza;

public class GiocoService {

    private final StanzaFactory stanzaFactory;
    private final Map<Coordinate, Stanza> dungeon = new HashMap<>();

    public GiocoService(StanzaFactory stanzaFactory) {
        this.stanzaFactory = stanzaFactory;
    }

    /**
     * Legge dimensioni dal file di configurazione
     */
    public int[] leggiConfigurazione(String percorsoFile) {
        int righe = 5; // default
        int colonne = 5;
        try (BufferedReader reader = new BufferedReader(new FileReader(percorsoFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("righe=")) {
                    righe = Integer.parseInt(line.split("=")[1].trim());
                } else if (line.startsWith("colonne=")) {
                    colonne = Integer.parseInt(line.split("=")[1].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("⚠️ Errore nella lettura del file di configurazione, uso valori di default 5x5.");
        }
        return new int[]{righe, colonne};
    }

    /**
     * Crea il dungeon
     */
    public void creaDungeon(int righe, int colonne) {
        System.out.println(" Creazione del dungeon " + righe + "x" + colonne + "...");

        int id = 1;
        for (int y = 0; y < righe; y++) {
            for (int x = 0; x < colonne; x++) {
                Stanza stanza = stanzaFactory.creaStanza(id++);
                dungeon.put(new Coordinate(x, y), stanza);
            }
        }

        collegaStanze(righe, colonne);
        System.out.println(" Dungeon creato con " + dungeon.size() + " stanze!");
    }

    /**
     * Collega le stanze adiacenti
     */
    private void collegaStanze(int righe, int colonne) {
        for (Map.Entry<Coordinate, Stanza> entry : dungeon.entrySet()) {
            Coordinate c = entry.getKey();
            Stanza stanza = entry.getValue();

            Stanza nord = dungeon.get(new Coordinate(c.x(), c.y() - 1));
            Stanza sud = dungeon.get(new Coordinate(c.x(), c.y() + 1));
            Stanza est = dungeon.get(new Coordinate(c.x() + 1, c.y()));
            Stanza ovest = dungeon.get(new Coordinate(c.x() - 1, c.y()));

            if (nord != null) {
                stanza.getStanzaAdiacente().put("NORD", nord);
            }
            if (sud != null) {
                stanza.getStanzaAdiacente().put("SUD", sud);
            }
            if (est != null) {
                stanza.getStanzaAdiacente().put("EST", est);
            }
            if (ovest != null) {
                stanza.getStanzaAdiacente().put("OVEST", ovest);
            }
        }
    }

    /**
     * Stampa la mappa senza spoiler
     */
    public void stampaMappa(int righe, int colonne) {
        System.out.println("\n--- MAPPA DEL DUNGEON ---");
        for (int y = 0; y < righe; y++) {
            for (int x = 0; x < colonne; x++) {
                Stanza s = dungeon.get(new Coordinate(x, y));
                if (s == null) {
                    System.out.print("   ");
                } else if (s.getStatoS() == StatoStanza.VISITATA) {
                    System.out.print(" . ");
                } else {
                    System.out.print(" ? ");
                }
            }
            System.out.println();
        }
        System.out.println("--------------------------\n");
    }

    public Map<Coordinate, Stanza> getDungeon() {
        return Collections.unmodifiableMap(dungeon);
    }

    // Chiave stringa
     private String coordinateKey(int x, int y) { return x + "," + y; }
       Map<String, Stanza> dungeon1 = new HashMap<>();
       dungeon1.get(coordinateKey(x,y));
}
