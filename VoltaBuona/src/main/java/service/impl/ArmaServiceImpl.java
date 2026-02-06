package service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import domain.Arma;
import domain.Oggetto;
import domain.Stanza;
import service.OggettoService;

public class ArmaServiceImpl implements OggettoService {
       private static final AtomicInteger ID_CONTATORE = new AtomicInteger(1);

    @Override
      public void posizionaOggettoInStanza(Oggetto oggetto, Stanza stanza){
        stanza.aggiungiOggetto(oggetto);}


    @Override
    public List<Oggetto> getOggettiInStanza(Stanza stanza){
        return stanza.getOggettiPresenti();}


    @Override
    public void rimuoviOggettoDaStanza(Stanza stanza, Oggetto oggetto){
        stanza.getOggettiPresenti().remove(oggetto);}

    /// DA IMPLEMENTARE
    @Override
    public void salvaOggettiSuFile(List<Oggetto> oggetti, String filePath){ }

   

@Override
public Oggetto creaOggettoCasuale() {
    int id = ID_CONTATORE.getAndIncrement();
    var random = java.util.concurrent.ThreadLocalRandom.current();
    Arma.TipoArma tipo = Arma.TipoArma.values()[random.nextInt(Arma.TipoArma.values().length)];
    String nome = tipo.name() ;
    String descrizione = switch (tipo) {
        case FRECCIA_E_ARCO -> "Arma con freccia e arco";
        case BACCHETTA_MAGICA -> "Bacchetta_magica potente";
        case SPADA -> "Spada affilata";
        case BALESTRA_PESANTE -> "Balestra pesante";
    };
   Arma a = new Arma(id, nome, descrizione, true, true, false, tipo);
   a.setPrezzo(25);
   return a;
}
}
