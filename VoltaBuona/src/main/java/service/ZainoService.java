package service;

public class ZainoService {

    public void salvaStatoPartita() {
        // salvataggio stato
    }

    public void inizializzaTurno() {
    }

    public Giocatore getTurnoCorrente() {
        return new Guerriero();
    }

    public void passaProssimoTurno() {
    }

    public List<Giocatore> getOrdineTurni() {
        return List.of(new Guerriero(), new Mago());
    }

    public void terminaTurnoCorrente(Gioco g) {
    }

    public void passaProssimoGiocatore(Gioco g) {
    }

    public boolean scambiaOggetto(Stanza s, Zaino z, Oggetto o) {
        return true;
    }

    public boolean raccogliDaStanza(Giocatore g, Stanza s, Oggetto o) {
        return true;
    }

    public boolean lasciaInStanza(Giocatore g, Stanza s, Oggetto o) {
        return true;
    }

    public void collegaStanze(Stanza s, Direzione d) {
    }

    public List<Stanza> caricaStanzeDaFile(String path) {
        return new ArrayList<>();
    }

    public void salvaZaino(Zaino z, String path) {
    }

    public Zaino caricaZaino(String path) {
        return new Zaino();
    }

    public void popolaDungeon(Dungeon d) {
    }

    public void aggiungiEventoCasuale(Stanza s) {
    }

    public void aggiungiOggettoCasuale(Stanza s) {
    }

    public boolean èPieno(Zaino z) {
        return z.getListaOggetti().size() >= 10;
    }

    public void svuota(Zaino z) {
        z.getListaOggetti().clear();
    }
}
