package service;


import java.util.*;

import domain.*;

public class EventoService {

    
    public Evento generaEvento() {
        return new Evento(); // logica di generazione casuale
    }
    // rimuovi evento dalla stanza specificata quando non è più attivo
    public void rimuoviEventoDaStanza(Stanza stanza, Evento evento) {
        stanza.getListaEventiAttivi().remove(evento);
    }

    public void attivaEvento(Giocatore g, Evento e) {
        // attiva effetto su giocatore
    }

    public void eseguiEventiInStanza(Giocatore g, Stanza s) {
        for (Evento e : getEventiAttivi(s)) {
            attivaEvento(g, e);
        }
    }
       //lo implementeremo insieme agli altri eventi
   /* public String parla(Personaggio g) {
        return "NPC: Benvenuto, " + g.getNomeP();
    }*/
}