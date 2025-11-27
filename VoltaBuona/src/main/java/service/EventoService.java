package service;

import java.util.ArrayList;
import java.util.List;

import domain.Evento;
import domain.Personaggio;
import domain.Stanza;

public class EventoService {

    private List<Evento> listaEventi = new ArrayList<>();

    /*public Evento generaEvento() {
        return new Evento(); // logica di generazione casuale
    }*/
    // rimuovi evento dalla stanza specificata quando non è più attivo
    public void rimuoviEventoDaStanza(Stanza stanza, Evento evento) {
        stanza.getListaEventiAttivi().remove(evento);
    }

    public void attivaEvento(Personaggio personaggio, Evento e) {
        // attiva effetto su giocatore
    }

    public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza) {
        for (Evento e : stanza.getListaEventiAttivi()) {
            attivaEvento(personaggio, e);
        }
    }
    //lo implementeremo insieme agli altri eventi
    /* public String parla(Personaggio g) {
        return "NPC: Benvenuto, " + g.getNomeP();
    }*/
}
