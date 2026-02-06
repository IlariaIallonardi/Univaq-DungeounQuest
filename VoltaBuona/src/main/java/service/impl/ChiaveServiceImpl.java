package service.impl;

import java.util.List;

import domain.Chiave;
import domain.Oggetto;
import domain.Stanza;
import service.OggettoService;

public class ChiaveServiceImpl implements OggettoService {

    @Override
    public void posizionaOggettoInStanza(Oggetto oggetto, Stanza stanza) {
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
    
    }

    @Override
    public Oggetto creaOggettoCasuale() {
        return creaOggettoCasualeinStanza(null);
    }

    public Oggetto creaOggettoCasualeinStanza(Integer stanzaId) {
        
        if (stanzaId == null) {
            return new domain.Chiave(-1, "Chiave_generica", "Chiave generica", true, false, false, -1);
        } else {
            int chiaveId = stanzaId;
            String nome = "Chiave_" + chiaveId;
            String descrizione = "Chiave che apre la stanza con id: " + stanzaId;
            return new Chiave(chiaveId, nome, descrizione, true, false, false, stanzaId);
        }
    }

    public Chiave creaChiavePerStanza(int stanzaId) {
        return (Chiave) creaOggettoCasualeinStanza(stanzaId);
    }
}
