package service;
import domain.Oggetto;
import domain.Stanza;
import domain.Zaino;

public interface ZainoService {


    public boolean scambiaOggetto(Stanza s, Zaino z, Oggetto o);

    public boolean èPieno(Zaino z) ;

    public void svuota(Zaino z) ;
}
