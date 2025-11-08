package domain;

public class Main {
    public static void main(String[] args) {
        String configFilePath = "src/main/resources/dungeon_config";
        DungeonGenerator generator = new DungeonGenerator(configFilePath);
        String[][] mappa = generator.getMappa();

        // Inizializza tutte le celle come non esplorate
        boolean[][] esplorata = new boolean[mappa.length][mappa[0].length];
        for (int r = 0; r < esplorata.length; r++) {
            for (int c = 0; c < esplorata[0].length; c++) {
                esplorata[r][c] = false;
            }
        }

        // Stampa la mappa coperta con lettere sopra e numeri di lato
        System.out.print("   ");
        for (int c = 0; c < mappa[0].length; c++) {
            System.out.print(" " + (char)('A' + c) + " ");
        }
        System.out.println();
        for (int r = 0; r < mappa.length; r++) {
            System.out.printf("%2d ", r + 1);
            for (int c = 0; c < mappa[0].length; c++) {
                System.out.print(" ? ");
            }
            System.out.println();
        }
    }
}