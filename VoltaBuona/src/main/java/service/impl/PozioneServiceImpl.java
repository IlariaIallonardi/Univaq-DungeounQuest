package service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import domain.Oggetto;
import domain.Pozione;
import domain.Stanza;
import service.OggettoService;

public class PozioneServiceImpl implements  OggettoService {
    private static final AtomicInteger ID_CONTATORE= new AtomicInteger(400);
    @Override
      public void posizionaOggettoInStanza(Oggetto oggetto, Stanza stanza){
        
        stanza.aggiungiOggetto(oggetto);}


    @Override
    public List<Oggetto> getOggettiInStanza(Stanza stanza){
        return stanza.getOggettiPresenti();
    }


    @Override
    public void rimuoviOggettoDaStanza(Stanza stanza, Oggetto oggetto){
        if (stanza == null || oggetto == null) return;
    if (stanza.getOggettiPresenti() != null) {
        stanza.getOggettiPresenti().remove(oggetto);
    }
}

    @Override
    public void salvaOggettiSuFile(List<Oggetto> oggetti, String filePath){
        
    }
    

@Override
public Oggetto creaOggettoCasuale() {
    int id = ID_CONTATORE.getAndIncrement();
    var rnd = java.util.concurrent.ThreadLocalRandom.current();

    Pozione.TipoPozione tipo = Pozione.TipoPozione.values()[rnd.nextInt(Pozione.TipoPozione.values().length)];

    String nome = "Pozione " + tipo.name() ;
    String descrizione = switch (tipo) {
        case CURA -> "Pozione curativa";
        case MANA -> "Pozione di mana";
        case ANTIDOTO -> "Pozione antidoto";
    };
    Pozione p = new Pozione(tipo, id, nome, descrizione, false, false, false);
    p.setPrezzo(15);
    return p;
}
}
