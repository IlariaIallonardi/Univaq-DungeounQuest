package service;


import java.util.List;

import domain.Oggetto;
import domain.Stanza;

public interface OggettoService {

    

    public void posizionaOggettoInStanza(Oggetto oggetto, Stanza stanza);



    public List<Oggetto> getOggettiInStanza(Stanza stanza);


    public void rimuoviOggettoDaStanza(Stanza stanza, Oggetto oggetto); 

    public void salvaOggettiSuFile(List<Oggetto> oggetti, String filePath);

    public Oggetto creaOggettoCasuale();

}