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

public interface OggettoService {

    

    public void posizionaOggettoInStanza(Oggetto oggetto, Stanza stanza);



    public List<Oggetto> getOggettiInStanza(Stanza stanza);


    public void rimuoviOggettoDaStanza(Stanza stanza, Oggetto oggetto); 

    public void salvaOggettiSuFile(List<Oggetto> oggetti, String filePath);

    public Oggetto creaOggetto();

}