package service.impl;

import domain.Mostro;
import domain.Personaggio;
import service.EventoService;

public class MostroServiceImpl extends EventoService {
    /**
     * Calcolo del danno che il mostro 
     * infligge al personaggio.
     */
    public static int calcolaDanno(Mostro mostro, Personaggio personaggio) {
        if (mostro == null || personaggio == null) return 0;
        int attaccoMostro = mostro.getDanno(); 
        int difesaPersonaggio = personaggio.getDifesa();   
        return Math.max(1, attaccoMostro - difesaPersonaggio);
    }
}   