package service.impl;

import service.*;
import domain.*;


public class PassaggioSegretoServiceImpl implements EventoService {

    @Override
    public void rimuoviEventoDaStanza(Stanza stanza, Evento evento){
        stanza.getListaEventiAttivi().remove(evento);
    };

    @Override
    public boolean attivaEvento(Personaggio personaggio, Evento e){
        // Default: i passaggi segreti non consumano il turno automaticamente
        return false;
    };

    @Override   
    public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza){   
        for (Evento e : stanza.getListaEventiAttivi()) {
            boolean termina = attivaEvento(personaggio, e);
            if (termina) return;
        }
    };
}
