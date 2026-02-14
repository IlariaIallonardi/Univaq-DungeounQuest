package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import exception.DungeonException;

public class Stanza implements Serializable {

    private int id;
    private int[][] coordinate;
    private boolean statoStanza;
    private List<Oggetto> oggettiPresenti = new ArrayList<>();
    private List<Personaggio> listaPersonaggi = new ArrayList<>();
    private Map<String, Stanza> stanzaAdiacente = new HashMap<>();
    private List<Evento> listaEventi = new ArrayList<>();
    private Chiave chiaveRichiesta;
    private boolean bloccata;
    private boolean uscitaVittoria;

    public Stanza(int id, int[][] coordinate, boolean statoStanza, List<Oggetto> inventario,
            List<Evento> listaEventi, Chiave chiaveRichiesta, boolean bloccata, boolean uscitaVittoria) {
        this.id = id;
        this.coordinate = coordinate;
        this.statoStanza = statoStanza;
        this.oggettiPresenti = inventario;
        this.stanzaAdiacente = stanzaAdiacente;
        this.listaEventi = listaEventi;
        this.chiaveRichiesta = chiaveRichiesta;
        this.bloccata = bloccata;
        this.uscitaVittoria = uscitaVittoria;

    }
    public Stanza() {
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

    public void rimuoviOggetto(Oggetto o)throws DungeonException {
        if (o == null) {
            throw new DungeonException("Tentativo di rimuovere un oggetto nullo dalla stanza.");
        }
        oggettiPresenti.remove(o);
    }

    public void aggiungiOggetto(Oggetto o) throws DungeonException {
        if (o == null) {
            throw new DungeonException("Tentativo di aggiungere un oggetto nullo alla stanza.");
        }
        oggettiPresenti.add(o);
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

    public void setBloccata(boolean bloccata) {
        this.bloccata = bloccata;
    }

    public boolean isUscitaVittoria() {
        return uscitaVittoria;
    }

    public void setUscitaVittoria(boolean uscitaVittoria) {
        this.uscitaVittoria = uscitaVittoria;
    }

    @Override
    public String toString() {
        Stanza stanza = this;
        return "Stanza ID: " + stanza.getId();
    }

    public void aggiungiPersonaggio(Personaggio p) {
        listaPersonaggi.add(p);
    }

    public void rimuoviPersonaggio(Personaggio p) {
        listaPersonaggi.remove(p);
    }

    /**
     * Rimuove un evento dalla stanza. Restituisce true se l'evento è stato
     * rimosso con successo, false altrimenti.
     */
    public boolean rimuoviEvento(Evento evento)throws DungeonException {

        Iterator<Evento> iteratore = listaEventi.iterator();
        while (iteratore.hasNext()) {
            Evento eventoCorrente = iteratore.next();

            if (eventoCorrente == evento || eventoCorrente.getId() == evento.getId()) {
                Stanza posizioneEvento = evento.getPosizioneCorrente();
                if (posizioneEvento == null || posizioneEvento.getId() == this.id) {
                    iteratore.remove();
                    return true;
                } else {
                    throw new DungeonException("Evento non rimosso dalla stanza.");
                    

                }
            }
        }

        return false;
    }
}
