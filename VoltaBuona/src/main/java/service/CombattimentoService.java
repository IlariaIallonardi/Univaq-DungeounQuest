package service;

import domain.Combattimento;
import domain.Mostro;
import domain.Personaggio;
import domain.Stanza;
import domain.Zaino;

public interface CombattimentoService {

    // lifecycle
    Object iniziaCombattimento(Personaggio personaggio, Mostro mostro, Stanza stanza);

    boolean terminaCombattimento(Combattimento combattimento, Object vincitore);

    // interrogazioni
    boolean èInCorso(Combattimento combattimento);

    // in CombattimentoService
    Object getVincitore(Combattimento combattimento);

    /**
     * Esegue l'azione specificata dall'attore (azioneId =
     * "ATTACCA","USA_POZIONE", ecc.) target può essere null o un
     * Personaggio/Mostro/Oggetto a seconda dell'azione.
     */
    void scegliAzioneCombattimento(Combattimento combattimento,Personaggio personaggio, Zaino zaino);

    // danno / risoluzione
    int applicaECalcolaDanno(Combattimento combattimento, Object attaccante);

}
