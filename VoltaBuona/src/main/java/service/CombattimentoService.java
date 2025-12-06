package service;
import domain.Combattimento;
import domain.Mostro;
import domain.Personaggio;
import domain.Zaino;
public interface CombattimentoService {

    // lifecycle
    boolean iniziaCombattimento(Combattimento combattimento);
    boolean terminaCombattimento(Combattimento combattimento);

    // interrogazioni
    boolean èInCorso(Combattimento combattimento);
    Personaggio getPersonaggio(Combattimento combattimento);
    Mostro getMostro(Combattimento combattimento);

    // in CombattimentoService
Object getVincitore(Combattimento combattimento);

    /**
     * Esegue l'azione specificata dall'attore (azioneId = "ATTACCA","USA_POZIONE", ecc.)
     * target può essere null o un Personaggio/Mostro/Oggetto a seconda dell'azione.
     */
    void scegliAzione(Personaggio personaggio,Zaino zaino);

    // danno / risoluzione
    int applicaECalcolaDanno(Combattimento combattimento, Object attaccante);
   
    
}