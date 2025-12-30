package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Stanza {

    private int id;
    private int[][] coordinate;
    private boolean statoStanza; //visitata o non visitata ed è enum in GiocoService
    private List<Oggetto> oggettiPresenti = new ArrayList<>();
    private List<Personaggio> listaPersonaggi = new ArrayList<>();
    private Map<String, Stanza> stanzaAdiacente = new HashMap<>();
    private List<Evento> listaEventi = new ArrayList<>();
    private Chiave chiaveRichiesta;
    private boolean bloccata;
    

    public Stanza(int id, int[][] coordinate, boolean statoStanza, List<Oggetto> inventario,
            List<Evento> listaEventi, Chiave chiaveRichiesta, boolean bloccata) {
        this.id = id;
        this.coordinate = coordinate;
        this.statoStanza = statoStanza;
        this.oggettiPresenti = inventario != null ? new ArrayList<>(inventario) : new ArrayList<>();
        this.stanzaAdiacente = (stanzaAdiacente != null) ? stanzaAdiacente : new HashMap<>();
        this.listaEventi = listaEventi != null ? new ArrayList<>(listaEventi) : new ArrayList<>();
        this.chiaveRichiesta = chiaveRichiesta;
        this.bloccata = bloccata;
    
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

    public boolean getStatoStanza() {
        return statoStanza;
    }

    public void setStatoStanza(boolean statoStanza) {
        this.statoStanza = statoStanza;
    }

    public List<Oggetto> getOggettiPresenti() {
        return oggettiPresenti;
    }

    public void setOggettiPresenti(List<Oggetto> oggettiPresenti) {
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
        if(o != null)
            oggettiPresenti.remove(o);
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
        Stanza stanza=this;
        return "Stanza ID: " + stanza.getId();
    }

    public void aggiungiPersonaggio(Personaggio p) {
        listaPersonaggi.add(p);
    }
    public void rimuoviPersonaggio(Personaggio p) {
        listaPersonaggi.remove(p);}


    
}
