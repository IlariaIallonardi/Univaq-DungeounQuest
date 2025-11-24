package service.impl;

import domain.Personaggio;
import domain.Trappola;
import service.EventoService;



/// DA VEDERE COME LA DOBBIAMO GESTIRE 
public class TrappolaServiceImpl extends EventoService {

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


   
    public static String esitoDisinnesco(Trappola trappola, Personaggio personaggio) {
        // logica per calcolare esito disinnesco
        return trappola.checkDiDisinnesco();
    }   
}