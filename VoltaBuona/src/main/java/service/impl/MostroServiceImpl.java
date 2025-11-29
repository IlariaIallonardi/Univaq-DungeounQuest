package service.impl;

import domain.Evento;
import domain.Mostro;
import domain.Personaggio;
import domain.Stanza;
import service.PersonaIncontrataService;

public class MostroServiceImpl implements  PersonaIncontrataService {
    /**
     * Calcolo del danno che il mostro 
     * infligge al personaggio.
     */
    public static int calcolaDanno(Mostro mostro, Personaggio personaggio) {
        if (mostro == null || personaggio == null) return 0;
        int attaccoMostro = mostro.getDannoMostro(); 
        int difesaPersonaggio = personaggio.getDifesa();   
        return Math.max(1, attaccoMostro - difesaPersonaggio);
    }

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
    }
}   