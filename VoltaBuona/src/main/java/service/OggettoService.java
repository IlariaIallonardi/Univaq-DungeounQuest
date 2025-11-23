package service;

import domain.Arma;
import domain.Armatura;
import domain.Chiave;
import domain.Gioco;
import domain.Stanza;
import domain.Zaino;
import domain.Oggetto;
import domain.Personaggio;
import domain.Pozione;

import java.util.ArrayList;
import java.util.List;

public class OggettoService {

    

    public void posizionaOggettoInStanza(Oggetto oggetto, Stanza stanza) {
        if (stanza == null || oggetto == null) return;
        stanza.aggiungiOggetto(oggetto);
    }



    public List<Oggetto> getOggettiInStanza(Stanza stanza) {
        if (stanza == null) return new ArrayList<>();
        return stanza.getInventario();
    }

    public List<Oggetto> oggettiDisponibili(Gioco gioco) {
        // placeholder: raccogli tutti gli oggetti dal gioco (da implementare secondo modello Gioco)
        return new ArrayList<>();
    }

    public void rimuoviOggettoDaStanza(Stanza stanza, Oggetto oggetto) {
        if (stanza == null || oggetto == null) return;
        stanza.rimuoviOggetto(oggetto);
    }

    public void salvaOggettiSuFile(List<Oggetto> oggetti, String filePath) {
        // serializzazione da implementare (JSON, XML, ecc.)
    }

    public Oggetto creaOggetto() {
        // Factory temporanea: implementare creazione specifica in base ai tipi concreti
        return null;
    }

}