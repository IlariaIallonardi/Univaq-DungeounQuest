package domain;

import it.univaq.dungeon.personaggi.Giocatore;
import it.univaq.dungeon.personaggi.Mostro;

public class Combattimento {
    private Giocatore giocatoreAttivo;
    private boolean inCorso;

    public String inizioCombattimento(Giocatore g) {
        this.giocatoreAttivo = g;
        inCorso = true;
        return "Combattimento avviato";
    }

    public int calcolaDannoG(Giocatore g) {
        // somma attacco base + arma + effetti temporanei
        return g.getAttacco();
    }

    public int calcolaDannoM(Mostro m) {
        return m.getDanno();
    }

    public String fineCombattimento() {
        inCorso = false;
        return "Combattimento concluso";
    }
}
