package service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import domain.Oggetto;
import domain.Pozione;
import domain.Stanza;
import service.OggettoService;

public class PozioneServiceImpl implements  OggettoService {
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
    Pozione.Tipo tipo = Pozione.Tipo.values()[rnd.nextInt(Pozione.Tipo.values().length)];
    int valore = rnd.nextInt(20, 71); // 20..70
    boolean durata = false;
    String nome = "Pozione " + id;
    String descrizione = switch (tipo) {
        case CURA -> "Pozione curativa";
        case MANA -> "Pozione di mana";
        case ANTIDOTO -> "Pozione antidoto";
    };
    return new Pozione(durata, tipo, valore, id, nome, descrizione, true, false, false);
}
}
