package domain;

import java.util.HashMap;
import java.util.Map;

public class DungeonIlaria {

    private  int righe=0;
    private  int colonne=0;

    // Mappa delle stanze: chiave = "x,y"
    private final Map<String, Stanza> mappaStanze = new HashMap<>();

    public DungeonIlaria(int righe, int colonne) {
        this.righe = righe;
        this.colonne = colonne;
    }

    // -------------------------
    // METODI UTILI PER LE CHIAVI
    // -------------------------

    private String key(int x, int y) {
        return x + "," + y;
    }

    public boolean esiste(int x, int y) {
        return x >= 0 && y >= 0 && x < colonne && y < righe;
    }

    // -------------------------
    // GET / SET DELLE STANZE
    // -------------------------

    public void setStanza(int x, int y, Stanza stanza) {
        if (esiste(x, y)) {
            mappaStanze.put(key(x, y), stanza);
        }
    }

    public Stanza getStanza(int x, int y) {
        if (!esiste(x, y)) return null;
        return mappaStanze.get(key(x, y));
    }

    public Map<String, Stanza> getMappaStanze() {
        return mappaStanze;
    }

    // -------------------------
    // STANZA INIZIALE
    // -------------------------

    public Stanza getStanzaIniziale() {
        return getStanza(0, 0);
    }

    // -------------------------
    // COLLEGAMENTO STANZE
    // -------------------------

    public void collegaAdiacenti() {
        for (int y = 0; y < righe; y++) {
            for (int x = 0; x < colonne; x++) {

                Stanza corrente = getStanza(x, y);
                if (corrente == null) continue;

                if (esiste(x, y - 1))
                    corrente.getStanzeAdiacenti().put("NORD", getStanza(x, y - 1));

                if (esiste(x, y + 1))
                    corrente.getStanzeAdiacenti().put("SUD", getStanza(x, y + 1));

                if (esiste(x + 1, y))
                    corrente.getStanzeAdiacenti().put("EST", getStanza(x + 1, y));

                if (esiste(x - 1, y))
                    corrente.getStanzeAdiacenti().put("OVEST", getStanza(x - 1, y));
            }
        }
    }

    // -------------------------
    // STAMPA MAPPA
    // -------------------------

    public void stampaMappa() {
        System.out.println("\n--- MAPPA DEL DUNGEON ---");
        for (int y = 0; y < righe; y++) {
            for (int x = 0; x < colonne; x++) {
                Stanza s = getStanza(x, y);

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
}