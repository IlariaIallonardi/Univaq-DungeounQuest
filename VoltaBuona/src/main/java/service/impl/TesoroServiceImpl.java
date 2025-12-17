package service.impl;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import domain.Oggetto;
import domain.Stanza;
import domain.Tesoro;
import service.OggettoService;

public class TesoroServiceImpl implements OggettoService {
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
    int valore = rnd.nextInt(10, 201); // 10..200
    String nome = "Tesoro " + id;
    String descrizione = "Un tesoro dal valore di " + valore;
    return new Tesoro(valore, id, nome, descrizione, false, false, false);
}
}
