package service.impl;

import domain.Arciere;
import domain.Stanza;
import service.GiocatoreService;

public class ArciereServiceImpl extends GiocatoreService {


     public boolean colpireStanza(Arciere arciere, Stanza stanzaBersaglio) {
         // Controlli di validità
         
        return !(arciere == null || stanzaBersaglio == null);
    }
}
