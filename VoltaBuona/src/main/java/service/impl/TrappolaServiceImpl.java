package service.impl;

import domain.Evento;
import domain.Personaggio;
import domain.Stanza;
import domain.Trappola;
import service.EventoService;



/// DA VEDERE COME LA DOBBIAMO GESTIRE 
public class TrappolaServiceImpl implements  EventoService {

    /**
     * Calcolo del danno che la trappola 
     * infligge al personaggio.
     */
    public static  int calcolaDanno(int dannoTrappola ,Personaggio personaggio) {
    
        if (personaggio == null) return 0;
        int attaccoTrappola = dannoTrappola; 
        int difesaPersonaggio = personaggio.getDifesa();   
         return Math.max(1, attaccoTrappola - difesaPersonaggio);
    }


   
    public boolean  esitoDisinnesco(Trappola trappola, Personaggio personaggio) {
        // logica per calcolare esito disinnesco
        return trappola.checkDiDisinnesco(personaggio);
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