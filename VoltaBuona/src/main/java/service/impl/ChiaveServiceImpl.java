package service.impl;

import java.util.List;

import domain.Chiave;
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
        return null;
    }

    public Oggetto creaOggettoCasuale(Integer stanzaId) {
        if (stanzaId == null) {
            return new domain.Chiave(-1, "Chiave_generica", "Chiave generica", true, false, false, -1);
        } else {
            int keyId = stanzaId;
            String nome = "Chiave_" + keyId;
            String descrizione = "Chiave che apre la stanza con id: " + stanzaId;
            return new Chiave(keyId, nome, descrizione, true, false, false, stanzaId);
        }
    }

    public Chiave creaChiavePerStanza(int stanzaId) {
        return (Chiave) creaOggettoCasuale(stanzaId);
    }
}
