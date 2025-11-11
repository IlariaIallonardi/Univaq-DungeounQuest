package service;

import domain.Gioco;
import domain.Stanza;
import domain.*;

import java.util.ArrayList;
import java.util.*;

public class OggettoService {


    public void usaOggetto(Gioco gioco, Oggetto oggetto) {
        oggetto.usare( );
    }
// questo metodo rimuove un oggetto dalla stanza specificata 
//dopo il suo utilizzo da parte del personaggio
    public void rimuoviOggettoDaStanza(Stanza stanza, Oggetto oggetto) {
        stanza.rimuoviOggetto(oggetto);
    }


    public Oggetto creaOggetto(String tipo, String nome) {
        switch (tipo.toLowerCase()) {
            case "chiave":
                return new Chiave(nome);
            case "arma":
                return new Arma(nome, 10); // esempio con danno
            case "armatura":
                return new Armatura(nome, 5);
            case "pozione":
                return new Pozione(nome, 50);
            case "tesoro":
                return new Tesoro(nome, 100);
            default:
                throw new IllegalArgumentException("Tipo oggetto non riconosciuto: " + tipo);
        }
    }
}