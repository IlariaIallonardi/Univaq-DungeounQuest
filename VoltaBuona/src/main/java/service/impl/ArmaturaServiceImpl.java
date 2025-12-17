package service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import domain.Armatura;
import domain.Oggetto;
import domain.Stanza;
import service.OggettoService;

public class ArmaturaServiceImpl implements   OggettoService {
    private static final AtomicInteger ID_COUNTER = new AtomicInteger(1);
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
public Oggetto creaOggettoCasuale() {
    int id = ID_COUNTER.getAndIncrement();
    var rnd = java.util.concurrent.ThreadLocalRandom.current();
    Armatura.TipoArmatura tipo = Armatura.TipoArmatura.values()[rnd.nextInt(Armatura.TipoArmatura.values().length)];
    int difesa = tipo.getDifesaBonus();
  
    String nome = "Armatura " + tipo.name() ;
    String descrizione = switch (tipo) {
        case DEBOLE -> "Armatura debole";
        case MEDIA -> "Armatura media";
        case FORTE -> "Armatura forte";
        
    };
    return new Armatura(difesa,tipo, id, nome, descrizione,true , true, false);
}
}
