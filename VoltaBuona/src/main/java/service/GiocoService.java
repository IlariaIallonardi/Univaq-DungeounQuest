
package service;

import domain.Gioco;

public class GiocoService {

    public Gioco creaNuovaPartita(Dungeon d, List<Giocatore> g) {
        Gioco gioco = new Gioco(1, d);
        g.forEach(gioco.getListaGiocatori()::add);
        return gioco;
    }

    public void finePartita(Gioco g) {
        g.setStato(StatoGioco.CONCLUSO);
    }

    public void pausa(Gioco g) {
        g.setStato(StatoGioco.IN_ATTESA);
    }

    public void riprendi(Gioco g) {
        g.setStato(StatoGioco.IN_ESPLORAZIONE);
    }

    public Oggetto creaOggetto() {
        return new Tesoro();
    }

    public void salvaOggettiSuFile(List<Oggetto> oggetti, String filePath) {
        // logica di salvataggio
    }

    public void posizionaOggettoInStanza(Oggetto o, Stanza s) {
        s.aggiungiOggetto(o);
    }

    public List<Oggetto> getOggettiInStanza(Stanza s) {
        return s.getInventario();
    }

    public List<Oggetto> oggettiDisponibili(Gioco g) {
        return new ArrayList<>();
    }

    public void usaOggetto(Giocatore g, Oggetto o) {
        o.usare();
    }

    public void rimuoviOggettoDaStanza() {
        // rimozione generica
    }

    public Dungeon generaDungeon() {
        return new Dungeon();
    }

    public void posizionaGiocatoriIniziali(Gioco g) {
        // distribuzione iniziale
    }

    public Gioco caricaPartita(String path) {
        return new Gioco(1, new Dungeon());
    }

    public void salvaPartita() {
        // logica di salvataggio
    }

    public int lanciaDado() {
        return new Random().nextInt(6) + 1;
    }
}