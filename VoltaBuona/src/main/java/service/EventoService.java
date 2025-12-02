package service;



import domain.Evento;
import domain.Personaggio;
import domain.Stanza;

public interface EventoService {

// rimuovi evento dalla stanza specificata quando non è più attivo
    public void rimuoviEventoDaStanza(Stanza stanza, Evento evento);

    public void attivaEvento(Personaggio personaggio, Evento e);

    public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza);
    
    
}
