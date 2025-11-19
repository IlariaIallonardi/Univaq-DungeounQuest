package service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import domain.Dungeon;

public class GiocoService {

    private final DungeonFactory dungeonFactory;
    private Dungeon dungeon;   // ✅ ora è un Dungeon, non una Map

    public GiocoService(DungeonFactory dungeonFactory) {
        this.dungeonFactory = dungeonFactory;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public Dungeon creaDungeon(int righe, int colonne) {
    this.dungeon = dungeonFactory.creaDungeon(righe, colonne);
    return dungeon;}
    

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

}
