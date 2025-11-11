package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stanza {
    private int id;
    private int[][] coordinate;
    private String statoS;
    private List<Oggetto> inventario = new ArrayList<>();
    private Map<String, Stanza> stanzaAdiacente = new HashMap<>();
    private List<Evento> listaEventi = new ArrayList<>();
    private Chiave  chiaveRichiesta;

    
    public Stanza(int id, int[][] coordinate, String statoS, List<Oggetto> inventario, 
              Map<String, Stanza> stanzaAdiacente, List<Evento> listaEventi, Chiave chiaveRichiesta) {
    this.id = id;
    this.coordinate = coordinate;
    this.statoS = statoS;
    this.inventario = inventario != null ? new ArrayList<>(inventario) : new ArrayList<>();
    this.stanzaAdiacente = stanzaAdiacente != null ? new HashMap<>(stanzaAdiacente) : new HashMap<>();
    this.listaEventi = listaEventi != null ? new ArrayList<>(listaEventi) : new ArrayList<>();
    this.chiaveRichiesta = chiaveRichiesta;
}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[][] getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(int[][] coordinate) {
        this.coordinate = coordinate;
    }

    public String getStatoS() {
        return statoS;
    }

    public void setStatoS(String statoS) {
        this.statoS = statoS;
    }

    public List<Oggetto> getInventario() {
        return inventario;
    }

    public void setInventario(List<Oggetto> inventario) {
        this.inventario = inventario;
    }

    public Map<String, Stanza> getStanzaAdiacente() {
        return stanzaAdiacente;
    }

    public void setStanzaAdiacente(Map<String, Stanza> stanzaAdiacente) {
        this.stanzaAdiacente = stanzaAdiacente;
    }

    public List<Evento> getListaEventiAttivi() {
        return listaEventi;
    }

    public void setListaEventi(List<Evento> listaEventi) {
        this.listaEventi = listaEventi;
    }

    public Chiave getChiaveRichiesta() {
        return chiaveRichiesta;
    }

    public void setChiaveRichiesta(Chiave chiaveRichiesta) {
        this.chiaveRichiesta = chiaveRichiesta;
    }

     public void prendiOggetto(Oggetto o) {
    }

    public boolean apriConChiave(domain.Chiave chiave) {
        // valida idStanzaDestinazione/chiaveRichiesta… (dipende dal design finale)
        return true;
    }

    public void esploraStanza(Giocatore g) {
    }
}
