package service;

import domain.Stanza;
import domain.Mostro;
import domain.Giocatore;

public class GiocatoreService {

    public void muovi(Giocatore g, Stanza nuovaStanza) {
        g.setPosizione(nuovaStanza);
    }

    public void attacca(Giocatore g, Mostro m) {
        int danno = g.calcolaDannoBase();
        m.setPuntiVita(m.getPuntiVita() - danno);
    }

    public void usaPozione(Giocatore g, Pozione p) {
        p.usare();
    }

    public void indossaArmatura(Giocatore g, Armatura a) {
        a.miglioraDifesa();
    }

    public void utilizzaArma(Giocatore g, Arma a) {
        a.miglioraAttacco();
    }

    public void utilizzaChiave(Giocatore g, Chiave c) {
        c.usare();
    }

    public boolean raccogliOggetto(Giocatore g, Oggetto o) {
        return g.getZaino().aggiungi(o);
    }

    public boolean subisciDanno(Giocatore g, int danno) {
        int nuoviPV = g.getPuntiVita() - danno;
        g.setPuntiVita(Math.max(0, nuoviPV));
        return nuoviPV <= 0;
    }

    public boolean morteGiocatore(Giocatore g) {
        return g.getPuntiVita() <= 0;
    }

    public void eliminaGiocatore(Giocatore g) {
        g.setPuntiVita(0);
    }
}
