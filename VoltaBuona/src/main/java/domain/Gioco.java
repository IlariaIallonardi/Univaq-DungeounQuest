package domain;

import java.util.List;

public class Gioco {
    public static void main(String[] args) {
        int righe = 5;
        int colonne = 5;

        // Stampa intestazione colonna (A B C D E)
        System.out.print("   "); // spazio per l'intestazione delle righe
        for (int c = 0; c < colonne; c++) {
            char letteraColonna = (char) ('A' + c);
            System.out.print(" " + letteraColonna + " ");
        }
        System.out.println();

        // Stampa righe con numeri e celle
        for (int r = 0; r < righe; r++) {
            System.out.print((r + 1) + " "); // numero riga
            if (r + 1 < 10) System.out.print(" "); // allineamento

            for (int c = 0; c < colonne; c++) {
                System.out.print(" . "); // simbolo della cella (vuoto per ora)
            }
            System.out.println();
        }
    }

    public List<Giocatore> getGiocatore() {
        List<Giocatore> Giocatore = null;
        return Giocatore;
    }
}
