package service.impl;

import domain.Combattimento;
import domain.Mostro;
import domain.Paladino;
import domain.Personaggio;
import service.PersonaggioService;

public class ComputerServiceImpl implements PersonaggioService {

    private final RandomSingleton randomGenerale = RandomSingleton.getInstance();

   
    /**
     * Sceglie un'azione per il computer in base a regole semplici e RNG.
     */


   
 

    @Override
    public Personaggio creaPersonaggio(String nome, Personaggio personaggio) {
        if(personaggio instanceof Paladino){
            return new PaladinoServiceImpl().creaPersonaggio(nome, personaggio);
        }
        if(personaggio instanceof domain.Arciere){
            return new ArciereServiceImpl().creaPersonaggio(nome, personaggio);
        }
        if(personaggio instanceof domain.Giocatore){
            return new GuerrieroServiceImpl().creaPersonaggio(nome, personaggio);
        }
        if(personaggio instanceof domain.Mago){
            return new MagoServiceImpl().creaPersonaggio(nome, personaggio);
        }
        return null;
    }

    @Override
    public int attacca(Personaggio personaggio, Mostro mostro, Combattimento combattimento) {
        if(personaggio instanceof Paladino){
            return new PaladinoServiceImpl().attacca(personaggio, mostro, combattimento);
        }
        if(personaggio instanceof domain.Arciere){
            return new ArciereServiceImpl().attacca(personaggio, mostro, combattimento);
        }
        if(personaggio instanceof domain.Giocatore){
            return new GuerrieroServiceImpl().attacca(personaggio, mostro, combattimento);
        }
        if(personaggio instanceof domain.Mago){
            return new MagoServiceImpl().attacca(personaggio, mostro, combattimento);
        }
        return 0;
    }

    @Override
    public void usaAbilitàSpeciale(Personaggio personaggio, String abilitàSpeciale) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}