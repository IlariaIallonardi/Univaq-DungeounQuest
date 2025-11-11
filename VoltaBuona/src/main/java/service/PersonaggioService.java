package service;

import domain.*;

//import it.univaq.dungeon.personaggi.Giocatore;

public class PersonaggioService {

    public Personaggio creaPersonaggio(String nome, Personaggio g) {
        g.setNomeP(nome);
        return g;
    }

    public String aggiornaStatoPersonaggio(Personaggio g) {
        return null;
    }

    public boolean mortePersonaggio(Personaggio g) {
        return g.getPuntiVita() <= 0;
    }

    public boolean muovi(Stanza s) {
        return s != null;
    }

    public void attacca(Mostro m) {
        m.setPuntiVita(m.getPuntiVita() - 10);
    }
         
      //idea implementativa per usare oggetti,
      //switch case in base al tipo di oggetto
    public void usaPozione(Pozione p) {
        
    }
   //metodo in Armatura nel domain
    public void indossaArmatura(Armatura a) {
        a.miglioraDifesa();
    }
       //metodo in Arma nel domain
    public void utilizzaArma(Arma a) {
        a.miglioraAttacco();
    }

    public void utilizzaChiave(Chiave c) {
        
    }

    public boolean raccogliereOggetto() {
        return true;
    }

    
    //metodi per gestire danni e protezioni gestiti da Mostro e trappola
    //da implementare in base alla logica di gioco
    public int subisciDannoDaMostro(int dannoMostro ,Personaggio g) {
        return 0;
    }
    public int subisciDannoDaTrappola(int dannoTrappola ,Personaggio g) {
        return 0;
    }


   

    public String aggiornamentoStatoPersonaggio() {
        return "Stato aggiornato";
    }

   
    
}
