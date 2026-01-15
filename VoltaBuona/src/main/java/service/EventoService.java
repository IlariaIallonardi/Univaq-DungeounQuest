package service;



import domain.Evento;
import domain.Personaggio;
import domain.Stanza;

public interface EventoService {

// rimuovi evento dalla stanza specificata quando non è più attivo
  //  public void rimuoviEventoDaStanza(Stanza stanza, Evento evento);

public Evento aggiungiEventoCasuale();

    /**
     * Attiva l'evento per il personaggio. Restituisce true se l'evento
     * consuma/termina immediatamente il turno del personaggio (es. combattimento).
     */
    public boolean attivaEvento(Personaggio personaggio, Evento e);

    public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza);
    
    
}
