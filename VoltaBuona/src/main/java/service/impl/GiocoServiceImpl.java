package service.impl;

import java.util.ArrayList;
import java.util.List;

import domain.Dungeon;
import domain.Evento;
import domain.Giocatore;
import domain.Oggetto;
import domain.Personaggio;
import domain.Stanza;
import domain.Trappola;
import domain.Zaino;
import service.Direzione;
import service.GiocatoreService;
import service.GiocoService;
import service.TurnoService;

public class GiocoServiceImpl implements GiocoService {

    private List<Giocatore> giocatori;
    private List<Stanza> dungeon;
    private TurnoService turnoService;
    private GiocatoreService giocatoreService;

    private int turnoCorrente = 0;

    public GiocoServiceImpl(List<Giocatore> giocatori, GiocatoreService giocatoreService, int turnoCorrente) {
        this.giocatori = giocatori;
        this.giocatoreService = giocatoreService;
        this.turnoCorrente = turnoCorrente;
    }

    public GiocoServiceImpl() {

    }

    public List<Giocatore> getGiocatori() {
        return giocatori;
    }

    public void setGiocatori(List<Giocatore> giocatori) {
        this.giocatori = giocatori;
    }

    public void setDungeon(List<Stanza> dungeon) {
        this.dungeon = dungeon;
    }

    public TurnoService getTurnoService() {
        return turnoService;
    }

    public void setTurnoService(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    public GiocatoreService getGiocatoreService() {
        return giocatoreService;
    }

    public void setGiocatoreService(GiocatoreService giocatoreService) {
        this.giocatoreService = giocatoreService;
    }

    public int getTurnoCorrente() {
        return turnoCorrente;
    }

    public void setTurnoCorrente(int turnoCorrente) {
        this.turnoCorrente = turnoCorrente;
    }

    @Override
    public boolean muoviPersonaggio(Personaggio personaggio, Direzione direzione) {
        if (personaggio == null || direzione == null) {
            return false;
        }

        Stanza corrente = personaggio.getPosizioneCorrente();
        if (corrente == null) {
            System.out.println("Il personaggio non è in nessuna stanza.");
            return false;
        }

        Stanza destinazione = corrente.getStanzaAdiacente().get(direzione.name());

        if (destinazione == null) {
            System.out.println("Non esiste una direzione/varco chiamato: " + direzione.name());
            return false;
        }
        // controllo stanza bloccata come nel metodo esistente
        if (destinazione.isBloccata()) {
            domain.Chiave richiesta = destinazione.getChiaveRichiesta();
            if (richiesta == null) {
                System.out.println("La stanza in " + direzione.name() + " è bloccata ma non ha chiave associata.");
                return false;
            }
            System.out.println("La stanza in " + direzione.name() + " è bloccata. Chiave richiesta: id="
                    + richiesta.getId() + " nome=" + richiesta.getNome());

            Zaino zaino = personaggio.getZaino();
            boolean trovato = false;
            if (zaino != null && zaino.getListaOggetti() != null) {
                for (Oggetto o : new ArrayList<>(zaino.getListaOggetti())) {
                    if (o instanceof domain.Chiave && ((domain.Chiave) o).getId() == richiesta.getId()) {
                        System.out.println("Hai la chiave richiesta (" + o.getNome() + "). Sblocco la stanza e consumo la chiave.");
                        destinazione.sblocca();
                        zaino.rimuoviOggettoDaZaino(o);
                        trovato = true;
                        break;
                    }
                }
            }
            if (!trovato) {
                System.out.println("Non possiedi la chiave richiesta nello zaino. Impossibile entrare.");
                return false;
            }
        }
        if (corrente.getListaPersonaggi() != null) {
            corrente.getListaPersonaggi().remove(personaggio);
        }
        if (destinazione.getListaPersonaggi() != null) {
            destinazione.getListaPersonaggi().add(personaggio);
        }

        personaggio.setPosizioneCorrente(destinazione);
        destinazione.setStatoStanza(true);

        if (destinazione.getListaEventiAttivi() != null) {
            List<Trappola> trappole = new ArrayList<>();
            for (Evento e : new ArrayList<>(destinazione.getListaEventiAttivi())) {
                if (e instanceof Trappola t) {
                    trappole.add(t);
                }
            }
            if (!trappole.isEmpty()) {
                // debug: elenco trappole trovate
                StringBuilder sb = new StringBuilder();
                for (Trappola t : trappole) {
                   // sb.append("[").append(t.getId()).append(":").append(t.getDescrizione()).append("]");
                }
              //  System.out.println("[DEBUG] Entrata stanza: " + destinazione + " - Personaggio: " + (personaggio != null ? personaggio.getNomePersonaggio() : "<null>"));
                // System.out.println("[DEBUG] Trappole presenti: " + trappole.size() + " -> " + sb.toString());

                var rnd = java.util.concurrent.ThreadLocalRandom.current();
                Trappola scelta = trappole.get(rnd.nextInt(trappole.size()));
             //   System.out.println("[DEBUG] Trappola scelta id=" + scelta.getId() + " descrizione=" + scelta.getDescrizione());
                boolean consumaTurno = new TrappolaServiceImpl().attivaEvento(personaggio, scelta);
             //   System.out.println("[DEBUG] attivaEvento -> consumaTurno=" + consumaTurno + " per trappolaId=" + scelta.getId());
               // System.out.println("[DEBUG] Stato personaggio dopo trappola: nome=" + (personaggio != null ? personaggio.getNomePersonaggio() : "<null>") + " | HP=" + (personaggio != null ? personaggio.getPuntiVita() : 0) + " | stato=" + (personaggio != null ? personaggio.getStatoPersonaggio() : "<null>"));
                if (consumaTurno) {
                    System.out.println("La trappola ha attivato un effetto che consuma il turno.");
                }
            }

        }
        return true;
    }

    @Override
    public Dungeon getDungeon() {
        return null;
    }

    @Override
    public Dungeon creaDungeon(int righe, int colonne) {
        return null;
    }
;}
