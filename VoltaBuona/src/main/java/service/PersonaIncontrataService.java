package service;

import domain.Evento;
import domain.Personaggio;
import domain.Stanza;

public interface PersonaIncontrataService extends EventoService {
    
  

  public Evento aggiungiEventoCasuale();

  public boolean attivaEvento(Personaggio personaggio, Evento e);

  public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza);
}
