package service;

import java.util.ArrayList;
import java.util.List;

import domain.Evento;
import domain.NPC;
import domain.Personaggio;
import domain.Stanza;

public interface EventoService {

// rimuovi evento dalla stanza specificata quando non è più attivo
    public void rimuoviEventoDaStanza(Stanza stanza, Evento evento);

    public void attivaEvento(Personaggio personaggio, Evento e);

    public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza);
    
    
}
