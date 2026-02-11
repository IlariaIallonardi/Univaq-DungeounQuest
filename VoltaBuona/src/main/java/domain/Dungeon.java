package domain;

import java.util.HashMap;
import java.util.Map;

public class Dungeon {

    private int righe = 0;
    private int colonne = 0;

    public Map<String, Stanza> stanzeMappa = new HashMap<>();

    public Dungeon(int righe, int colonne) {
        this.righe = righe;
        this.colonne = colonne;
    }

    public boolean esisteStanza(int x, int y) {
        return x >= 0 && y >= 0 && x < colonne && y < righe;
    }

    public String scriviCoordinate(int x, int y) {
        return x + "," + y;
    }

    public void collegaAdiacenti() {
        for (int y = 0; y < righe; y++) {
            for (int x = 0; x < colonne; x++) {

                Stanza corrente = getStanza(x, y);
                if (corrente == null) {
                    continue;
                }

                if (esisteStanza(x, y - 1)) {
                    corrente.getStanzaAdiacente().put("NORD", getStanza(x, y - 1));
                }

                if (esisteStanza(x, y + 1)) {
                    corrente.getStanzaAdiacente().put("SUD", getStanza(x, y + 1));
                }

                if (esisteStanza(x + 1, y)) {
                    corrente.getStanzaAdiacente().put("EST", getStanza(x + 1, y));
                }

                if (esisteStanza(x - 1, y)) {
                    corrente.getStanzaAdiacente().put("OVEST", getStanza(x - 1, y));
                }
            }
        }
    }

    public void setStanza(int x, int y, Stanza stanza) {  //è il metodo che costruisce la mappa
        if (esisteStanza(x, y)) { //controlla che la posizione sia dentro la mappa
            stanzeMappa.put(scriviCoordinate(x, y), stanza); //converte le coordinate in una stringa tipo "2,3"
            //salva la stanza nella mappa nella posizione indicata
        }
    }
   /**
    * Metodo per vedere cosa c'è in una posizione del Dungeon. Se la posizione è fuori dai limiti, restituisce null.
     * @param x coordinata x
     * @param y coordinata y
     * @return la stanza presente nella posizione (x,y) o null se non esiste
    */
    public Stanza getStanza(int x, int y) { 
        if (!esisteStanza(x, y)) {
            return null;
        }
        return stanzeMappa.get(scriviCoordinate(x, y));
    }

    public Map<String, Stanza> getMappaStanze() {
        return stanzeMappa;
    }

   
}
