package domain;

import it.univaq.dungeon.oggetti.Oggetto;

import java.util.*;

public class Stanza {
    private int id;
    private int capienza;
    private List<Oggetto> inventario = new ArrayList<>();
    private Map<String, Stanza> stanzaAdiacente = new HashMap<>();
    private List<Evento> listaEventi = new ArrayList<>();
    private boolean rebusApertura = false;

    public Stanza(int id, int capienza) {
        this.id = id;
        this.capienza = capienza;
    }

    public void aggiungiOggetto(Oggetto o) {
        if (inventario.size() < capienza)
            inventario.add(o);
    }

    public void rimuoviOggetto(Oggetto o) {
        inventario.remove(o);
    }

    public boolean apriConChiave(it.univaq.dungeon.oggetti.Chiave chiave) {
        // valida idStanzaDestinazione/chiaveRichiesta… (dipende dal design finale)
        return true;
    }

    // getters/setters omessi
}
