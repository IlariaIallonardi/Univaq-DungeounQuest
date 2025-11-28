package service.impl;

import java.util.*;

import domain.*;

import service.*;

public class ArmaServiceImpl implements OggettoService {
    @Override
      public void posizionaOggettoInStanza(Oggetto oggetto, Stanza stanza){
        if (stanza == null || oggetto == null) return;
        stanza.aggiungiOggetto(oggetto);}


    @Override
    public List<Oggetto> getOggettiInStanza(Stanza stanza){
        return stanza.getOggettiPresenti();
    }


    @Override
    public void rimuoviOggettoDaStanza(Stanza stanza, Oggetto oggetto){
        stanza.getOggettiPresenti().remove(oggetto);
    }

    @Override
    public void salvaOggettiSuFile(List<Oggetto> oggetti, String filePath){
        // serializzazione da implementare (JSON, XML, ecc.)
    }

    @Override
    public Oggetto creaOggetto(){
        // Factory temporanea: implementare creazione specifica in base ai tipi concreti
        return null;
    }
}
