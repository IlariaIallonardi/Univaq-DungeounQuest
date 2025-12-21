package service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import domain.Arma;
import domain.Oggetto;
import domain.Stanza;
import service.OggettoService;

public class ArmaServiceImpl implements OggettoService {
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
    Arma.TipoArma tipo = Arma.TipoArma.values()[rnd.nextInt(Arma.TipoArma.values().length)];
    int danno = tipo.getDannoBonus() ; 
    String nome = tipo.name() ;
    String descrizione = switch (tipo) {
        case FRECCIA_E_ARCO -> "Arma con freccia e arco";
        case BACCHETTA_MAGICA -> "Bacchetta_magica potente";
        case SPADA -> "Spada affilata";
        case BALESTRA_PESANTE -> "Balestra pesante";
    };
   Arma a = new Arma(danno, id, nome, descrizione, true, true, false, tipo);
   a.setPrezzo(25); // <--- imposta qui il prezzo che desideri per le armi
   return a;
}
}
