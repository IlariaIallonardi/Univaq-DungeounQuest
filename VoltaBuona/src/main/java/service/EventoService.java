package service;

import it.univaq.dungeon.core.*;
import it.univaq.dungeon.personaggi.*;
import it.univaq.dungeon.oggetti.*;
import java.util.*;

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

    public String parla(Giocatore g) {
        return "NPC: Benvenuto, " + g.getNome();
    }
}