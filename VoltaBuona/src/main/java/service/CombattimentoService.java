package service;
import java.util.List;
import java.util.Optional;

import domain.Combattimento;
import domain.Mostro;
import domain.Personaggio;
import domain.Stanza;
public interface CombattimentoService {

    // lifecycle
    Combattimento creaCombattimento(int id, Stanza stanza);
    boolean iniziaCombattimento(Combattimento combattimento);
    boolean terminaCombattimento(Combattimento combattimento);
    boolean rimuoviPartecipante(Combattimento combattimento, Object partecipante);

    // interrogazioni
    boolean èInCorso(Combattimento combattimento);
    List<Personaggio> getPersonaggi(Combattimento combattimento);
    List<Mostro> getMostri(Combattimento combattimento);
    Optional<Object> getVincitore(Combattimento combattimento);

    // turno / ordine
    /**
     * Restituisce il partecipante che deve agire (Personaggio o Mostro) senza eseguire l'azione.
     */
    Object prossimoPartecipante(Combattimento combattimento);

    /**
     * Esegue l'azione specificata dall'attore (azioneId = "ATTACCA","USA_POZIONE", ecc.)
     * target può essere null o un Personaggio/Mostro/Oggetto a seconda dell'azione.
     */
    boolean eseguiAzione(Combattimento combattimento, Object attore, String azioneId, Object target);

    // danno / risoluzione
    int calcolaDanno(Combattimento combattimento, Object attaccante , Object difensore);
    void applicaDanno(Combattimento combattimento, Object attaccante, Object difensore, int danno);
   
    
}