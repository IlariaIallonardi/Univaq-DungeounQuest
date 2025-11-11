package service;
import domain.Oggetto;
import domain.Stanza;
import domain.Zaino;

public class ZainoService {


    public boolean scambiaOggetto(Stanza s, Zaino z, Oggetto o) {
        return true;
    }



    public boolean èPieno(Zaino z) {
        return z.getListaOggetti().size() >= 5;
    }

    public void svuota(Zaino z) {
        z.getListaOggetti().clear();
    }
}
