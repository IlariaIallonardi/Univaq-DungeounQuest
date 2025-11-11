package service.impl;

import domain.Oggetto;
import domain.Stanza;
import domain.Zaino;
import service.ZainoService;

public class ZainoServiceImpl extends  ZainoService {

    @Override
    public void svuota(Zaino z) {
        super.svuota(z);
    }

    @Override
    public boolean èPieno(Zaino z) {
        return super.èPieno(z);
    }

    @Override
    public boolean scambiaOggetto(Stanza s, Zaino z, Oggetto o) {
        return super.scambiaOggetto(s, z, o);
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
