package service;

import java.nio.file.Path;
import java.util.List;

import domain.Dungeon;
import domain.*;

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
 public void collegaStanze(Stanza stanza, Direzione direzione) {
        // Logica per collegare una stanza ad un'altra in una direzione specifica
    }

    public List<Stanza> caricaStanzeDaFile(Path file) {
        // Logica per caricare stanze da un file
        return null;
    }

    public void popolaDungeon(Dungeon dungeon) {
        // Logica per popolare il dungeon con stanze
    }

    public void aggiungiEventoCasuale(Stanza stanza) {
        // Logica per aggiungere un evento casuale alla stanza
    }

    public void aggiungiOggettoCasuale(Stanza stanza) {
        // Logica per aggiungere un oggetto casuale public void collegaStanze(Stanza stanza, Direzione direzione) {
        // Logica per collegare una stanza ad un'altra in una direzione specifica
    }

    public List<Stanza> caricaStanzeDaFile(Path file) {
        // Logica per caricare stanze da un file
        return null;
    }

    public void popolaDungeon(Dungeon dungeon) {
        // Logica per popolare il dungeon con stanze
    }

    public void aggiungiEventoCasuale(Stanza stanza) {
        // Logica per aggiungere un evento casuale alla stanza
    }

    public void aggiungiOggettoCasuale(Stanza stanza) {
        // Logica per aggiungere un oggetto casuale alla stanza
    } 
    


}
