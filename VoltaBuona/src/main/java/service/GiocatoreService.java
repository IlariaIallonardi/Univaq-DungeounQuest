package service;

import domain.Combattimento;
import domain.Mostro;
import domain.Personaggio;
import service.impl.ArciereServiceImpl;
import service.impl.GuerrieroServiceImpl;
import service.impl.MagoServiceImpl;
import service.impl.PaladinoServiceImpl;
import service.impl.RandomSingleton;

public class GiocatoreService {
    private RandomSingleton randomGenerale = RandomSingleton.getInstance();

    public Personaggio attribuisciPersonaggioAComputer(Personaggio personaggio) {
        // genera il personaggio "base" (classe scelta casuale)
        int scelta = randomGenerale.prossimoNumero(0, 3);


        Personaggio botBase;
        switch (scelta) {
            case 0:
                botBase = new GuerrieroServiceImpl().creaPersonaggio(personaggio.getNomePersonaggio(), personaggio);
                break;
            case 1:
                botBase = new MagoServiceImpl().creaPersonaggio(personaggio.getNomePersonaggio(), personaggio);
                break;
            case 2:
                botBase = new ArciereServiceImpl().creaPersonaggio(personaggio.getNomePersonaggio(), personaggio);
                break;
            case 3:
                botBase = new PaladinoServiceImpl().creaPersonaggio(personaggio.getNomePersonaggio(), personaggio);
                break;
            default:
                botBase = new GuerrieroServiceImpl().creaPersonaggio(personaggio.getNomePersonaggio(), personaggio);
        }

        
        try {
            
            botBase.setNomePersonaggio("Bot-" + botBase.getClass().getSimpleName());
        } catch (Exception ignored) {
        
        }

        

        System.out.println("Il computer giocherà come: " + botBase.getClass().getSimpleName() + " (nome: " + botBase.getNomePersonaggio() + ")");

        return botBase;
    }

    // Deleghe per comportamento: instradano alle implementazioni specifiche
    public int attacca(Personaggio attacker, Mostro target, Combattimento combattimento) {
        if (attacker == null || target == null) return 0;
        if (attacker instanceof domain.Guerriero) {
            return new GuerrieroServiceImpl().attacca(attacker, target, combattimento);
        }
        if (attacker instanceof domain.Mago) {
            return new MagoServiceImpl().attacca(attacker, target, combattimento);
        }
        if (attacker instanceof domain.Arciere) {
            return new ArciereServiceImpl().attacca(attacker, target, combattimento);
        }
        if (attacker instanceof domain.Paladino) {
            return new PaladinoServiceImpl().attacca(attacker, target, combattimento);
        }
        // fallback generico
        return 0;
    }

    
    public boolean usaOggetto(Personaggio personaggio, domain.Oggetto oggetto) {
        if (personaggio == null || oggetto == null) return false;
        // la logica di uso oggetto è implementata in Personaggio. Manteniamo delega minimale.
        return personaggio.usaOggetto(personaggio, oggetto);
    }
}
