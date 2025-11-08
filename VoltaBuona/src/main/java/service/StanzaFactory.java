package it.univaq.dungeon.factory;

import domain.Stanza;

public class StanzaFactory {

    public Stanza creaStanzaVuota(int[][] mappa) {
        return new Stanza(0, 10); // id e capienza di default
    }

    public Stanza creaStanzaCasuale(int[][] mappa) {
        int id = (int) (Math.random() * 1000);
        int capienza = 5 + (int) (Math.random() * 10);
        return new Stanza(id, capienza);
    }

    public Stanza creaStanzaDaConfig(StanzaConfig cfg) {
        return new Stanza(cfg.getId(), cfg.getCapienza());
    }

    public Stanza creaStanzaIniziale() {
        return new Stanza(1, 15); // stanza iniziale con capienza maggiore
    }
}
