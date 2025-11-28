package domain;

import java.util.ArrayList;
import java.util.List;

public class Combattimento {
    private int id;
    private int ordineDiIniziativa;
    private Stanza stanza;
    private int turnoCorrenteC;
    private boolean inCorso;
    private int danniInflitti;
    private Giocatore vincitore;
    private List<Effetto> effettiTemporanei;
    private Giocatore giocatoreAttivo;

 public Combattimento(int id, Stanza stanza) {
        this.id = id;
        this.stanza = stanza;
        this.ordineDiIniziativa = 0;
        this.turnoCorrenteC = 0;
        this.inCorso = false;
        this.danniInflitti = 0;
        this.vincitore = null;
        this.effettiTemporanei = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrdineDiIniziativa() {
        return ordineDiIniziativa;
    }

    public void setOrdineDiIniziativa(int ordineDiIniziativa) {
        this.ordineDiIniziativa = ordineDiIniziativa;
    }

    public Stanza getStanza() {
        return stanza;
    }

    public void setStanza(Stanza stanza) {
        this.stanza = stanza;
    }

    public int getTurnoCorrenteC() {
        return turnoCorrenteC;
    }

    public void setTurnoCorrenteC(int turnoCorrenteC) {
        this.turnoCorrenteC = turnoCorrenteC;
    }

    public boolean isInCorso() {
        return inCorso;
    }

    public void setInCorso(boolean inCorso) {
        this.inCorso = inCorso;
    }

    public int getDanniInflitti() {
        return danniInflitti;
    }

    public void setDanniInflitti(int danniInflitti) {
        this.danniInflitti = danniInflitti;
    }

    public Giocatore getVincitore() {
        return vincitore;
    }

    public void setVincitore(Giocatore vincitore) {
        this.vincitore = vincitore;
    }

    public List<Effetto> getEffettiTemporanei() {
        return effettiTemporanei;
    }

    public void setEffettiTemporanei(List<Effetto> effettiTemporanei) {
        this.effettiTemporanei = effettiTemporanei;
    }

    public Giocatore getGiocatoreAttivo() {
        return giocatoreAttivo;
    }

    public void setGiocatoreAttivo(Giocatore giocatoreAttivo) {
        this.giocatoreAttivo = giocatoreAttivo;
    }

    


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
        return m.getDannoMostro();
    }

    public String fineCombattimento() {
        inCorso = false;
        return "Combattimento concluso";
    }

    
}
