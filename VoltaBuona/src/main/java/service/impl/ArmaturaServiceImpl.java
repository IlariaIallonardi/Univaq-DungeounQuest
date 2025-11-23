package service.impl;

import domain.Personaggio;
import service.OggettoService;

public class ArmaturaServiceImpl extends  OggettoService {

    @Override
     public void eseguiEffetto(Personaggio personaggio) {
        if (personaggio == null) return;
        // Aumenta solo il danno/attacco del personaggio
        personaggio.setAttacco(personaggio.getAttacco() + this.dannoBonus);
}
