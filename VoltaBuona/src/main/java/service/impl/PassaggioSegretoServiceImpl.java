package service.impl;

import service.*;
import domain.*;


public class PassaggioSegretoServiceImpl implements EventoService {

    @Override
    public void rimuoviEventoDaStanza(Stanza stanza, Evento evento){
        stanza.getListaEventiAttivi().remove(evento);
    };

    @Override
    public void attivaEvento(Personaggio personaggio, Evento e){

    };

    @Override   
    public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza){   
        for (Evento e : stanza.getListaEventiAttivi()) {
            attivaEvento(personaggio, e);
        }
    };
}
