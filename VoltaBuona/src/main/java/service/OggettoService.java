package service;

import domain.Gioco;
import domain.Stanza;
import domain.Oggetto;

import java.util.ArrayList;
import java.util.List;

public class OggettoService {

    public void posizionaOggettoInStanza(Oggetto oggetto, Stanza stanza) {
        stanza.aggiungiOggetto(oggetto);
    }

    public List<Oggetto> getOggettiInStanza(Stanza stanza) {
        return stanza.getInventario();
    }

    public List<Oggetto> oggettiDisponibili(Gioco gioco) {
        // logica per raccogliere tutti gli oggetti disponibili nel dungeon
        return new ArrayList<>();
    }

    public void usaOggetto(Gioco gioco, Oggetto oggetto) {
        oggetto.usare();
    }

    public void rimuoviOggettoDaStanza(Stanza stanza, Oggetto oggetto) {
        stanza.rimuoviOggetto(oggetto);
    }

    public void salvaOggettiSuFile(List<Oggetto> oggetti, String filePath) {
        // logica di serializzazione su file
    }

    public Oggetto creaOggetto() {
        return new Oggetto() {
            @Override
            public boolean usare() {
                return true;
            }
        };
    }
}