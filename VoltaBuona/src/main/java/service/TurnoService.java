package service;

import java.util.List;
import java.util.Scanner;

import domain.Oggetto;
import domain.Personaggio;
import domain.Stanza;
public interface TurnoService {

    //public Personaggio getTurnoCorrente(); -> da implementare in turnoserviceimpl
    //public void passaProssimoTurno(); -> da implementare in turnoserviceimpl
    public void iniziaNuovoTurno(List<Personaggio> partecipanti,Scanner scanner);
    public List<Personaggio> calcolaOrdineIniziativa(List<Personaggio> partecipanti);

    public void terminaTurnoCorrente(Personaggio personaggio);

    //public void passaProssimoGiocatore(Personaggio personaggio); -> da implementare in turnoserviceimpl
    public void aggiornaEffettiFineTurno(Personaggio personaggio);

    public void scegliAzione(Personaggio personaggio, java.util.Scanner scanner);

    public void gestisciMovimento(Personaggio personaggio, Scanner scanner);

   // public void eseguiEvento(Personaggio p, Stanza stanza, List<Evento> eventi, Scanner scanner);

    public void raccogliUnOggetto(Personaggio p, Stanza stanza, List<Oggetto> oggetti, Scanner scanner);
}
