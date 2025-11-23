package service.impl;

import domain.*;
import domain.Trappola;
import service.*;



/// DA VEDERE COME LA DOBBIAMO GESTIRE 
public class TrappolaServiceImpl extends EventoService {

    /**
     * Calcolo del danno che la trappola 
     * infligge al personaggio.
     */
    public static  int calcolaDanno(int dannoTrappola ,Personaggio p) {
        Trappola trappola;
        if (p == null) return 0;
        int att = dannoTrappola; 
        int dif = p.getDifesa();   
    
        if (trappola.alterareStato()) {
    
               Effetto tipo = trappola.getTipo();
                trappola.getValoreEffetto();
                trappola.getDurataEffetto();
            
            // aggiungi l'effetto al personaggio (usa il metodo reale presente in Personaggio)
            p.aggiungiEffettoTemporaneo(effetto);
        }

        return Math.max(1, att - dif);
    


}