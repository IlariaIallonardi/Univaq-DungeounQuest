package service;


import domain.Combattimento;
import domain.Mostro;
import domain.Personaggio;


public interface  PersonaggioService {


     Personaggio creaPersonaggio(String nome, Personaggio personaggio);
    int attacca(Personaggio personaggio, Mostro mostro, Combattimento combattimento);
  


/*

    // ...existing code...
    /**
     * Il personaggio p attacca il mostro m. penso sia abbastanza giusto Ritorna
     * il danno inflitto (0 se input non valido).
     *


    public void esploraStanza(Personaggio personaggio) {

        if (personaggio == null) {
            System.out.println("Errore: personaggio nullo.");
            return;
        }

        Stanza stanza = personaggio.getPosizioneCorrente();
        if (stanza == null) {
            System.out.println("Errore: posizione del personaggio non valida.");
            return;
        }

        System.out.println("🔍 " + personaggio.getNomeP() + " esplora la stanza...");

    } */

}
