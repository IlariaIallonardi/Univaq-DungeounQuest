package service;


import java.util.*;

import domain.*;

public class EventoService {

    
    public Evento generaEvento() {
        return new Evento(); // logica di generazione casuale
    }

    public void rimuoviEvento(Stanza stanza, Evento evento) {
        stanza.getListaEventi().remove(evento);
    }

    public List<Evento> getEventiAttivi(Stanza stanza) {
        return stanza.getListaEventi().stream()
                .filter(Evento::attivo)
                .toList();
    }

    public void attivaEvento(Giocatore g, Evento e) {
        // attiva effetto su giocatore
    }

    public void eseguiEventiInStanza(Giocatore g, Stanza s) {
        for (Evento e : getEventiAttivi(s)) {
            attivaEvento(g, e);
        }
    }

    public String parla(Personaggio g) {
        return "NPC: Benvenuto, " + g.getNomeP();
    }
}