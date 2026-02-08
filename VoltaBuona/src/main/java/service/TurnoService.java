package service;

import java.util.List;

import domain.Oggetto;
import domain.Personaggio;
import domain.Stanza;

public interface TurnoService {

   
    public void iniziaNuovoTurno(List<Personaggio> partecipanti);

    public List<Personaggio> calcolaOrdineIniziativa(List<Personaggio> partecipanti);

    public void terminaTurnoCorrente(Personaggio personaggio);

    
    public void aggiornaEffettiFineTurno(Personaggio personaggio);

    public void scegliAzione(Personaggio personaggio);

    public void gestisciMovimento(Personaggio personaggio);

    
    public void raccogliUnOggetto(Personaggio p, Stanza stanza, List<Oggetto> oggetti);
}
