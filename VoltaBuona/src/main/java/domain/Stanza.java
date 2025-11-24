package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.StanzaFactory.StatoStanza;

public class Stanza {

    private int id;
    private int[][] coordinate;
    private StatoStanza statoS; //visitata o non visitata ed è enum in GiocoService
    private List<Oggetto> oggettiPresenti = new ArrayList<>();
    private List<Personaggio> listaPersonaggi = new ArrayList<>();
    private Map<String, Stanza> stanzaAdiacente = new HashMap<>();
    private List<Evento> listaEventi = new ArrayList<>();
    private Chiave chiaveRichiesta;
    private boolean bloccata;

    public Stanza(int id, int[][] coordinate, StatoStanza statoS, List<Oggetto> inventario,
            List<Evento> listaEventi, Chiave chiaveRichiesta,boolean bloccata ) {
        this.id = id;
        this.coordinate = coordinate;
        this.statoS = statoS;
        this.oggettiPresenti = inventario != null ? new ArrayList<>(inventario) : new ArrayList<>();
        this.stanzaAdiacente = (stanzaAdiacente != null) ? stanzaAdiacente : new HashMap<>();
        this.listaEventi = listaEventi != null ? new ArrayList<>(listaEventi) : new ArrayList<>();
        this.chiaveRichiesta = chiaveRichiesta;
        this.bloccata=bloccata;
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

    public StatoStanza getStatoS() {
        return statoS;
    }

    public void setStatoS(StatoStanza statoStanza) {
        this.statoS = statoS;
    }

    public List<Oggetto> getOggettiPresenti() {
        return oggettiPresenti;
    }

    public void setInventario(List<Oggetto> oggettoPresenti) {
        this.oggettiPresenti = oggettiPresenti;
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

    public List<Personaggio> getListaPersonaggi() {
        return new ArrayList<>(listaPersonaggi);
    }

    public void setListaPersonaggi(List<Personaggio> listaPersonaggi) {
        this.listaPersonaggi = new ArrayList<>(listaPersonaggi);
    }

    public Chiave getChiaveRichiesta() {
        return chiaveRichiesta;
    }

    public void setChiaveRichiesta(Chiave chiaveRichiesta) {
        this.chiaveRichiesta = chiaveRichiesta;
    }

    public void rimuoviOggetto(Oggetto o) {
    }
    public void aggiungiOggetto(Oggetto o) {
        if (o != null) {
            oggettiPresenti.add(o);
        }
    }

    public void esploraStanza(Giocatore g) {
    }
    public boolean isBloccata() {
        return bloccata;
    }
    public boolean sblocca() {
    if (!bloccata) {
        System.out.println("La stanza è già sbloccata.");
        return false;
    }

    bloccata = false;
    chiaveRichiesta = null;

    System.out.println(" La stanza è stata sbloccata!");
    return true;
}
    

    @Override
    public String toString() {
        return "Stanza{id=" + id
                + ", stato=" + statoS
                //+ ", oggetti=" + inventario.size()
                + ", eventi=" + listaEventi.size()
                + ", chiave=" + (chiaveRichiesta != null) + "}";
    }


    /*public Map<String, Stanza> getStanzeAdiacenti() {
        throw new UnsupportedOperationException("Not supported yet.");
    }*/
}
