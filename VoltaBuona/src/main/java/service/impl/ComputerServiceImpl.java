package service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import domain.Combattimento;
import domain.Evento;
import domain.Mostro;
import domain.Oggetto;
import domain.Paladino;
import domain.Personaggio;
import domain.Pozione;
import domain.Stanza;
import domain.Tesoro;
import service.Direzione;
import service.EventoService;
import service.GiocoService;
import service.PersonaggioService;

public class ComputerServiceImpl implements PersonaggioService {

    private final Random random = new Random();

    public void agisciAutomatico(Personaggio p, TurnoServiceImpl turnoService, GiocoService giocoService,
            EventoService eventoService, PersonaggioService personaggioService) {
        if (p == null) return;

        // 1) Movimento: prova direzioni casuali
        List<Direzione> dirs = Arrays.asList(Direzione.NORD, Direzione.SUD, Direzione.EST, Direzione.OVEST);
        Collections.shuffle(dirs, random);
        boolean mosso = false;
        for (Direzione d : dirs) {
            try {
                if (giocoService.muoviPersonaggio(p, d)) {
                    System.out.println(p.getNomePersonaggio() + " (bot) si muove verso " + d);
                    mosso = true;
                    break;
                }
            } catch (Exception ignored) {
            }
        }
        if (!mosso) {
            System.out.println(p.getNomePersonaggio() + " (bot) resta nella stanza.");
        }

        // 2) Esplora stanza (segna visitata)
        turnoService.esploraStanza(p);

        Stanza stanza = p.getPosizioneCorrente();
        if (stanza == null) {
            turnoService.terminaTurnoCorrente(p);
            return;
        }

        List<Evento> eventi = stanza.getListaEventiAttivi();
        List<Oggetto> oggetti = stanza.getOggettiPresenti();

        // 3) Priorità: gestisci primo evento se presente (es. combattimento)
        if (eventi != null && !eventi.isEmpty()) {
            Evento e = eventi.get(0);
            EventoService svc = servicePerEvento(e);
            boolean termina = svc.attivaEvento(p, e);
            try {
                if (e.isFineEvento() || !e.èRiutilizzabile()) {
                    svc.rimuoviEventoDaStanza(stanza, e);
                }
            } catch (Exception ignored) {
            }
            if (termina) {
                turnoService.terminaTurnoCorrente(p);
                return;
            }
        }

        // 4) Raccogli oggetti: cerca prima un Tesoro, altrimenti primo oggetto
        if (oggetti != null && !oggetti.isEmpty()) {
            Oggetto scelta = null;
            for (Oggetto o : oggetti) {
                if (o instanceof Tesoro) { scelta = o; break; }
            }
            if (scelta == null) scelta = oggetti.get(0);
            boolean ok = p.raccogliereOggetto(p, scelta);
            if (ok) {
                System.out.println(p.getNomePersonaggio() + " (bot) raccoglie " + scelta.getNome());
            }
        }

        // 5) Usa pozione se salute bassa
        if (p.getPuntiVita() < 10 && p.getZaino() != null) {
            for (Oggetto o : p.getZaino().getListaOggetti()) {
                if (o instanceof Pozione) {
                    boolean used = p.usaOggetto(p, o);
                    if (used) {
                        System.out.println(p.getNomePersonaggio() + " (bot) usa pozione " + o.getNome());
                        break;
                    }
                }
            }
        }

        // 6) Fine turno
        turnoService.terminaTurnoCorrente(p);
    }

    private EventoService servicePerEvento(Evento e) {
        if (e == null) return new PassaggioSegretoServiceImpl();
        if (e instanceof domain.Mostro) return new MostroServiceImpl();
        if (e instanceof domain.Trappola) return new TrappolaServiceImpl();
        if (e instanceof domain.PassaggioSegreto) return new PassaggioSegretoServiceImpl();
        if (e instanceof domain.PersonaIncontrata) return new NPCServiceImpl();
        return new PassaggioSegretoServiceImpl();
    }

    @Override
    public Personaggio creaPersonaggio(String nome, Personaggio personaggio) {
        if(personaggio instanceof Paladino){
            return new PaladinoServiceImpl().creaPersonaggio(nome, personaggio);
        }
        if(personaggio instanceof domain.Arciere){
            return new ArciereServiceImpl().creaPersonaggio(nome, personaggio);
        }
        if(personaggio instanceof domain.Giocatore){
            return new GuerrieroServiceImpl().creaPersonaggio(nome, personaggio);
        }
        if(personaggio instanceof domain.Mago){
            return new MagoServiceImpl().creaPersonaggio(nome, personaggio);
        }
        return null;
    }

    @Override
    public int attacca(Personaggio personaggio, Mostro mostro, Combattimento combattimento) {
        if(personaggio instanceof Paladino){
            return new PaladinoServiceImpl().attacca(personaggio, mostro, combattimento);
        }
        if(personaggio instanceof domain.Arciere){
            return new ArciereServiceImpl().attacca(personaggio, mostro, combattimento);
        }
        if(personaggio instanceof domain.Giocatore){
            return new GuerrieroServiceImpl().attacca(personaggio, mostro, combattimento);
        }
        if(personaggio instanceof domain.Mago){
            return new MagoServiceImpl().attacca(personaggio, mostro, combattimento);
        }
        return 0;
    }

    @Override
    public void usaAbilitàSpeciale(Personaggio personaggio, String abilitàSpeciale) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}