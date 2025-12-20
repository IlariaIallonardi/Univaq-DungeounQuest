package service;

import java.util.HashMap;
import java.util.Map;

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
            Stanza stanza = stanzaFactory.creaStanza(idCounter++,x,y);
            d.setStanza(x, y, stanza);
        }
    }

    d.collegaAdiacenti();
    this.dungeon = d;

    return d;
}

    public Dungeon getDungeon() {
        return dungeon;
    }
    
}



// prima prova grafica dungeon
/*public class Dungeon {
    static final int RIGHE = 5;
    static final int COLONNE = 5;
    static String[][] eventi = new String[RIGHE][COLONNE];
    static boolean[][] esplorata = new boolean[RIGHE][COLONNE];

    static class Giocatore {
        String nome;
        int hp = 40;
        List<String> zaino = new ArrayList<>(List.of("Pozione"));
        int riga = -1, col = -1;

        Giocatore(String nome) { this.nome = nome; }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Genera eventi randomici
        generaEventi();

        // Inizializza celle esplorate
        for (int r = 0; r < RIGHE; r++) Arrays.fill(esplorata[r], false);

        // Setup giocatori
        List<Giocatore> giocatori = new ArrayList<>();
        System.out.print("Quanti giocatori? (2-4): ");
        int n = Integer.parseInt(scanner.nextLine());
        if (n < 2 || n > 4) {
            System.out.println("Numero non valido.");
            return;
        }
        for (int i = 1; i <= n; i++) {
            System.out.print("Nome giocatore " + i + ": ");
            giocatori.add(new Giocatore(scanner.nextLine()));
        }

        // Loop dei turni
        while (true) {
            stampaMappaCoperta();
            for (Giocatore g : giocatori) {
                System.out.println("\nTurno di " + g.nome + " (HP: " + g.hp + ")");
                System.out.print("Inserisci una cella (es. B3) o 'exit': ");
                String input = scanner.nextLine().toUpperCase();
                if (input.equals("EXIT")) return;
                if (input.length() < 2) {
                    System.out.println("Input non valido.");
                    continue;
                }
                char colChar = input.charAt(0);
                int col = colChar - 'A';
                int row;
                try {
                    row = Integer.parseInt(input.substring(1)) - 1;
                } catch (Exception e) {
                    System.out.println("Coordinate non valide.");
                    continue;
                }
                if (col < 0 || col >= COLONNE || row < 0 || row >= RIGHE) {
                    System.out.println("Coordinate fuori mappa.");
                    continue;
                }
                // Esplora la cella
                if (!esplorata[row][col]) {
                    esplorata[row][col] = true;
                    String evento = eventi[row][col];
                    if (evento.equals("M")) {
                        System.out.println("Un mostro ti attacca! Perdi 10 HP.");
                        g.hp -= 10;
                    } else if (evento.equals("T")) {
                        System.out.println("Sei finito su una trappola! Perdi 5 HP.");
                        g.hp -= 5;
                    } else if (evento.equals("O")) {
                        System.out.println("Hai trovato un oggetto! Ottieni una Pozione.");
                        g.zaino.add("Pozione");
                    } else {
                        System.out.println("La cella è vuota.");
                    }
                } else {
                    System.out.println("Hai già esplorato questa cella.");
                }
                g.riga = row;
                g.col = col;
            }
        }
    }

    static void generaEventi() {
        Random rand = new Random();
        for (int r = 0; r < RIGHE; r++) {
            for (int c = 0; c < COLONNE; c++) {
                int x = rand.nextInt(100);
                if (x < 15) eventi[r][c] = "M";
                else if (x < 25) eventi[r][c] = "T";
                else if (x < 35) eventi[r][c] = "O";
                else eventi[r][c] = ".";
            }
        }
    }

    static void stampaMappaCoperta() {
        System.out.print("   ");
        for (int c = 0; c < COLONNE; c++) System.out.print(" " + (char)('A'+c) + " ");
        System.out.println();
        for (int r = 0; r < RIGHE; r++) {
            System.out.printf("%2d ", r+1);
            for (int c = 0; c < COLONNE; c++) {
                if (esplorata[r][c]) System.out.print(" " + eventi[r][c] + " ");
                else System.out.print(" ? ");
            }
            System.out.println();
        }
    }
}*/
