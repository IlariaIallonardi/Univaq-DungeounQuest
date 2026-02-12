package service;

import domain.Combattimento;
import domain.Mostro;
import domain.Personaggio;

public interface PersonaggioService {

    Personaggio creaPersonaggio(String nome, Personaggio personaggio);

    int attacca(Personaggio personaggio, Mostro mostro, Combattimento combattimento);

}
