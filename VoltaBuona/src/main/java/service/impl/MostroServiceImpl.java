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
    public static int dannoBase(Mostro mostro, Personaggio personaggio) {
        if (mostro == null || personaggio == null) return 0;
        int attaccoMostro = mostro.getDannoMostro(); 
        int difesaPersonaggio = personaggio.getDifesa();   
        return Math.max(1, attaccoMostro - difesaPersonaggio);
    }

    /**
     * Esecuzione dell'attacco del mostro
     * sul personaggio bersaglio.
     */
    public void attaccoDelMostro(Mostro mostro, Personaggio bersaglio) {


        Mostro.TipoAttaccoMostro tipoAttacco = mostro.getTipoAttaccoMostro();

        int dannoBase = dannoBase(mostro, bersaglio);
        int danno = calcolaDannoPerTipo(tipoAttacco, dannoBase,bersaglio);

        System.out.println(mostro.getNomeMostro() + " usa " + tipoAttacco +
                " infliggendo " + danno + " danni a " + bersaglio.getNomeP());

        bersaglio.subisciDanno(danno);
    }

    public  int calcolaDannoPerTipo(Mostro.TipoAttaccoMostro tipoAttacco, int base, Personaggio bersaglio) {
        if(tipoAttacco == Mostro.TipoAttaccoMostro.MORSO){
            return base + 2;
        } else if (tipoAttacco == Mostro.TipoAttaccoMostro.RUGGITO_DI_FUOCO) {
            bersaglio.setStatoPersonaggio("stordito");
            return base + 5; 
        } else if (tipoAttacco == Mostro.TipoAttaccoMostro.URLO_ASSORDANTE) {
            bersaglio.setStatoPersonaggio("stordito");
            return base + 3; 
        } else if (tipoAttacco == Mostro.TipoAttaccoMostro.RAGNATELA_IMMOBILIZZANTE) {
            bersaglio.setStatoPersonaggio("immobilizzato");
            return base + 4; 
        } else if (tipoAttacco == Mostro.TipoAttaccoMostro.ARTIGLI_POSSENTI) {
            bersaglio.setStatoPersonaggio("avvelenato");
            return base + 4; 
        } else {
            return base; 
        }

        
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