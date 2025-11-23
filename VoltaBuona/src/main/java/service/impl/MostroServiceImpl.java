package service.impl;

import domain.Mostro;
import domain.Personaggio;
import service.EventoService;

public class MostroServiceImpl extends EventoService {
    /**
     * Calcolo del danno che il mostro 
     * infligge al personaggio.
     */
    public static int calcolaDanno(Mostro m, Personaggio p) {
        if (m == null || p == null) return 0;
        int att = m.getDanno(); 
        int dif = p.getDifesa();   
        return Math.max(1, att - dif);
    }
}   