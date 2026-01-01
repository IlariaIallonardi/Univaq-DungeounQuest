package service.impl;

import java.util.List;

import domain.Oggetto;
import domain.Stanza;
import service.OggettoService;

public class ChiaveServiceImpl implements OggettoService {

    @Override
    public void posizionaOggettoInStanza(Oggetto oggetto, Stanza stanza) {
        if (stanza == null || oggetto == null) {
            return;
        }
        stanza.aggiungiOggetto(oggetto);
    }

    @Override
    public List<Oggetto> getOggettiInStanza(Stanza stanza) {
        return stanza.getOggettiPresenti();
    }

    @Override
    public void rimuoviOggettoDaStanza(Stanza stanza, Oggetto oggetto) {
        stanza.getOggettiPresenti().remove(oggetto);
    }

    @Override
    public void salvaOggettiSuFile(List<Oggetto> oggetti, String filePath) {
        // serializzazione da implementare (JSON, XML, ecc.)
    }

    private static final java.util.concurrent.atomic.AtomicInteger KEY_ID_COUNTER = new java.util.concurrent.atomic.AtomicInteger(1);

    @Override
    public Oggetto creaOggettoCasuale() {
        int id = KEY_ID_COUNTER.getAndIncrement();
        String nome = "Chiave_" + id;
        String descrizione = "Chiave generica id=" + id;
        // usabile=true (può essere usata), equipaggiabile=false (non si equipaggia), trovato=false (appena creata)
        return new domain.Chiave(id, nome, descrizione, true, false, false);
    }
    public domain.Chiave creaChiavePerStanza(int stanzaId) {
        int keyId = KEY_ID_COUNTER.getAndIncrement();
        String descrizione = "Chiave che apre la stanza con id: " + stanzaId;
        return new domain.Chiave(keyId, "Chiave_"+ keyId, descrizione, true, false, false);
    }
}
