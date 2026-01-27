package domain;

import java.util.HashMap;
import java.util.Map;

public class Dungeon {

    private int righe = 0;
    private int colonne = 0;

    private final Map<String, Stanza> stanzeMappa = new HashMap<>();

    public Dungeon(int righe, int colonne) {
        this.righe = righe;
        this.colonne = colonne;
    }

    public boolean esiste(int x, int y) {
        return x >= 0 && y >= 0 && x < colonne && y < righe;
    }

    private String key(int x, int y) {
        return x + "," + y;
    }

    public void collegaAdiacenti() {
        for (int y = 0; y < righe; y++) {
            for (int x = 0; x < colonne; x++) {

                Stanza corrente = getStanza(x, y);
                if (corrente == null) {
                    continue;
                }

                if (esiste(x, y - 1)) {
                    corrente.getStanzaAdiacente().put("NORD", getStanza(x, y - 1));
                }

                if (esiste(x, y + 1)) {
                    corrente.getStanzaAdiacente().put("SUD", getStanza(x, y + 1));
                }

                if (esiste(x + 1, y)) {
                    corrente.getStanzaAdiacente().put("EST", getStanza(x + 1, y));
                }

                if (esiste(x - 1, y)) {
                    corrente.getStanzaAdiacente().put("OVEST", getStanza(x - 1, y));
                }
            }
        }
    }

    public void setStanza(int x, int y, Stanza stanza) {  //è il metodo che costruisce la mappa.
        if (esiste(x, y)) { //→ controlla che la posizione sia dentro la mappa
            stanzeMappa.put(key(x, y), stanza); //key → converte le coordinate in una stringa tipo "2,3"
            //put → salva la stanza nella mappa nella posizione indicata
        }
    }

    public Stanza getStanza(int x, int y) { //è il metodo per leggere cosa c'è in una posizione del dungeon.
        if (!esiste(x, y)) {
            return null;
        }
        return stanzeMappa.get(key(x, y));
    }

    public Map<String, Stanza> getMappaStanze() {
        return stanzeMappa;
    }

    public void stampaMappa(int righe, int colonne) {
        System.out.println("\n--- MAPPA DUNGEON ---");
        for (int y = 0; y < righe; y++) {
            for (int x = 0; x < colonne; x++) {
                Stanza s = getStanza(x, y);
                if (s == null) {
                    System.out.print("   ");
                } else {
                    System.out.print(s.getStatoStanza() == true ? " . " : " ? ");
                }
            }
            System.out.println();
        }
        System.out.println("---------------------\n");
    }
}


