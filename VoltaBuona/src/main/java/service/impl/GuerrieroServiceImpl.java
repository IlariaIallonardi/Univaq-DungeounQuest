package service.impl;


import domain.Combattimento;
import domain.Guerriero;
import domain.Mostro;
import domain.Oggetto;
import domain.Personaggio;
import domain.Trappola;
import service.PersonaggioService;

public class GuerrieroServiceImpl implements  PersonaggioService {
    
    /**
     * Metodo per proteggere un altro giocatore usando la forza del guerriero
     * @param guerriero Il guerriero che protegge
     * @param bersaglio Il personaggio da proteggere
     * @return true se la protezione è stata applicata con successo
     */
    public boolean protezioneGiocatoreG(Guerriero guerriero, Personaggio bersaglio) {
        if (guerriero == null || bersaglio == null) {
            return false;
        }

        // Verifica se il guerriero ha abbastanza punti vita
        if (guerriero.getPuntiVita() < 20) {
            return false;
        }

         // delega al dominio: solo Guerriero può applicare la protezione
        boolean ok = guerriero.proteggi(bersaglio);
        if (!ok) return false;

        System.out.println(guerriero.getNomeP() + " protegge per un turno " + bersaglio.getNomeP());
        return true;
    }

    @Override
    public int attacca(Personaggio personaggio, Mostro mostro, Combattimento combattimento){
         if (!(personaggio instanceof Guerriero)) {
        return 1;
    }

    Guerriero guerriero = (Guerriero) personaggio;
    if (guerriero == null || mostro == null) return 0;
    return 0;
    }

    @Override
    public Personaggio creaPersonaggio(String nome, Personaggio personaggio) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean usaOggetto(Personaggio personaggio, Oggetto oggetto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean raccogliereOggetto(Personaggio personaggio, Oggetto oggetto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int subisciDannoDaMostro(Mostro.TipoAttaccoMostro attaccoMostro, int dannoBase, Personaggio personaggio) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void subisciDannoDaTrappola(Trappola trappola, Personaggio personaggio) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void esploraStanza(Personaggio personaggio) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


       
    }
