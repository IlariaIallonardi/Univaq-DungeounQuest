package service;

import domain.Evento;
import domain.NPC;
import domain.Personaggio;
import domain.Stanza;

public interface PersonaIncontrataService extends EventoService {
    
  public void rimuoviEventoDaStanza(Stanza stanza, Evento evento);

  

  public boolean attivaEvento(Personaggio personaggio, Evento e);

  public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza);
}
