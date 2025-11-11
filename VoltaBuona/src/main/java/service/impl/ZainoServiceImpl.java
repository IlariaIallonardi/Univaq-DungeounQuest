package service.impl;

import domain.Oggetto;
import domain.Stanza;
import domain.Zaino;
import service.ZainoService;

public class ZainoServiceImpl implements  ZainoService {

    @Override
    public void svuota(Zaino z) {
        return;
    }

    @Override
    public boolean èPieno(Zaino z) {
        return true;
    }

    @Override
    public boolean scambiaOggetto(Stanza s, Zaino z, Oggetto o) {
        return true;
    }   

    @Override
    public String toString() {
        return super.toString();
    }

    

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
