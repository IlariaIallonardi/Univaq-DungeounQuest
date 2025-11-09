package service;

import domain.*;

//import it.univaq.dungeon.personaggi.Giocatore;

public class PersonaggioService {

    public Personaggio creaPersonaggio(String nome, Personaggio g) {
        g.setNomeP(nome);
        return g;
    }

    public StatoGioco aggiornaStatoPersonaggio(Personaggio g) {
        return StatoGioco.IN_ESPLORAZIONE;
    }

    public void eliminaPersonaggio(Personaggio g) {
        g.setPuntiVita(0);
    }

    public boolean muovi(Stanza s) {
        return s != null;
    }

    public void attacca(Mostro m) {
        m.setPuntiVita(m.getPuntiVita() - 10);
    }

    public void usaPozione(Pozione p) {
        
    }

    public void indossaArmatura(Armatura a) {
        a.miglioraDifesa();
    }

    public void utilizzaArma(Arma a) {
        a.miglioraAttacco();
    }

    public void utilizzaChiave(Chiave c) {
        
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
        
        return new Giocatore(); // Assuming Giocatore is a subclass of Personaggio
    }
}
