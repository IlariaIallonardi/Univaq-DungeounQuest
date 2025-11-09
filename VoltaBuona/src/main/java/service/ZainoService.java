package service;
import domain.Giocatore;
import domain.Oggetto;
import domain.Stanza;
import domain.Zaino;

public class ZainoService {


    public boolean scambiaOggetto(Stanza s, Zaino z, Oggetto o) {
        return true;
    }

    public boolean raccogliDaStanza(Giocatore g, Stanza s, Oggetto o) {
        return true;
    }

    public boolean lasciaInStanza(Giocatore g, Stanza s, Oggetto o) {
        return true;
    }



    public void salvaZaino(Zaino z, String path) {
    }

    public Zaino caricaZaino(String path) {
        return new Zaino();
    }


    public boolean èPieno(Zaino z) {
        return z.getListaOggetti().size() >= 10;
    }

    public void svuota(Zaino z) {
        z.getListaOggetti().clear();
    }
}
