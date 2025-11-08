package service;

import domain.Personaggio;
import domain.Giocatore;

//import it.univaq.dungeon.personaggi.Giocatore;

public class PersonaggioService {

    public Giocatore creaGiocatore(String nome, Giocatore g) {
        g.setNome(nome);
        return g;
    }

    public StatoGioco aggiornaStatoGiocatore(Giocatore g) {
        return StatoGioco.IN_ESPLORAZIONE;
    }

    public void eliminaGiocatore(Giocatore g) {
        g.setPuntiVita(0);
    }

    public boolean muovi(Stanza s) {
        return s != null;
    }

    public void attacca(Mostro m) {
        m.setPuntiVita(m.getPuntiVita() - 10);
    }

    public void usaPozione(Pozione p) {
        p.usare();
    }

    public void indossaArmatura(Armatura a) {
        a.miglioraDifesa();
    }

    public void utilizzaArma(Arma a) {
        a.miglioraAttacco();
    }

    public void utilizzaChiave(Chiave c) {
        c.usare();
    }

    public boolean raccogliereOggetto() {
        return true;
    }

    public boolean morteGiocatore() {
        return true;
    }

    public int subisciDanno(int dannoMostro) {
        return Math.max(0, 100 - dannoMostro);
    }

    public boolean protezioneGiocatoreG(Giocatore g) {
        return true;
    }

    public boolean protezioneGiocatoreP(Giocatore g) {
        return true;
    }

    public boolean usareMagiaP(Mostro m) {
        return true;
    }

    public boolean usareMagia(Mostro m) {
        return true;
    }

    public String aggiornamentoStatoG() {
        return "Stato aggiornato";
    }

    public void colpireStanze(Stanza s) {
        // interazione con stanza
    }

    public Personaggio attribuisciPersonaggioAComputer() {
        return new Mostro();
    }
}
