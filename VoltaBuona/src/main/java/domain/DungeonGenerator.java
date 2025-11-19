/*package domain;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class DungeonGenerator {
    private int righe;
    private int colonne;
    private String[][] mappa;
    private int minMostri = 0, maxMostri = 0;
    private int minTrappole = 0, maxTrappole = 0;
    private int minOggetti = 0, maxOggetti = 0;

    public DungeonGenerator(String configFilePath) {
        leggiConfigurazione(configFilePath);
        mappa = new String[righe][colonne];
        generaMappa();
    }

    private void leggiConfigurazione(String configFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(configFilePath))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(" ");
                righe = Integer.parseInt(parts[0]);
                colonne = Integer.parseInt(parts[1]);
            }
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("mostri")) {
                    String[] parts = line.split(" ");
                    minMostri = Integer.parseInt(parts[1]);
                    maxMostri = Integer.parseInt(parts[2]);
                } else if (line.startsWith("trappole")) {
                    String[] parts = line.split(" ");
                    minTrappole = Integer.parseInt(parts[1]);
                    maxTrappole = Integer.parseInt(parts[2]);
                } else if (line.startsWith("oggetti")) {
                    String[] parts = line.split(" ");
                    minOggetti = Integer.parseInt(parts[1]);
                    maxOggetti = Integer.parseInt(parts[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generaMappa() {
        for (int r = 0; r < righe; r++) {
            for (int c = 0; c < colonne; c++) {
                mappa[r][c] = ".";
            }
        }
        Random rand = new Random();
        int numMostri = minMostri + rand.nextInt(maxMostri - minMostri + 1);
        int numTrappole = minTrappole + rand.nextInt(maxTrappole - minTrappole + 1);
        int numOggetti = minOggetti + rand.nextInt(maxOggetti - minOggetti + 1);

        inserisciElementiCasuali("M", numMostri, rand);
        inserisciElementiCasuali("T", numTrappole, rand);
        inserisciElementiCasuali("O", numOggetti, rand);
    }

    private void inserisciElementiCasuali(String simbolo, int quanti, Random rand) {
        int inseriti = 0;
        while (inseriti < quanti) {
            int r = rand.nextInt(righe);
            int c = rand.nextInt(colonne);
            if (mappa[r][c].equals(".")) {
                mappa[r][c] = simbolo;
                inseriti++;
            }
        }
    }

    public String[][] getMappa() {
        return mappa;
    }
}*/