package service.impl;


import domain.Combattimento;
import domain.Guerriero;
import domain.Mostro;
import domain.Personaggio;
import service.PersonaggioService;

public class GuerrieroServiceImpl extends PersonaggioService {
    
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
        return super.attacca(personaggio, mostro, combattimento);
    }

    Guerriero guerriero = (Guerriero) personaggio;
    if (guerriero == null || mostro == null) return 0;
    return 0;
    }


       
    }
