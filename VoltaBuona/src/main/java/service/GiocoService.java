package service;

import domain.Dungeon;
import domain.*;
import service.*;
import service.impl.*;

public interface GiocoService {

    



    public Dungeon getDungeon();

    public Dungeon creaDungeon(int righe, int colonne);
    public boolean muoviPersonaggio(Personaggio personaggio, Direzione direzione);

    /**
     * Legge dimensioni dal file di configurazione
     */
   /*  public int[] leggiConfigurazione(String percorsoFile) {
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

    public boolean muovi(Personaggio personaggio, Direzione direzione) {
        return true;
    }*/
}
