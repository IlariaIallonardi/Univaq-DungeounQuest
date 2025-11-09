package service.impl;

import domain.Arciere;
import domain.Stanza;
import service.PersonaggioService;

public class ArciereServiceImpl extends PersonaggioService {


     public boolean colpireStanza(Arciere arciere, Stanza stanzaBersaglio) {
         // Controlli di validità
         
        return !(arciere == null || stanzaBersaglio == null);
    }
}
