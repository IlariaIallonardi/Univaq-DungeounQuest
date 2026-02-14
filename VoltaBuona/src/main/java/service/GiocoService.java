package service;

import java.util.ArrayList;
import java.util.List;

import domain.Dungeon;
import domain.Evento;
import domain.Giocatore;
import domain.Gioco;
import domain.Oggetto;
import domain.Personaggio;
import domain.Stanza;
import domain.Stato.StatoGioco;
import domain.Trappola;
import domain.Zaino;
import service.impl.RandomSingleton;
import service.impl.TrappolaServiceImpl;
import util.ANSI;

public class GiocoService {

    private List<Giocatore> giocatori;
    private Dungeon dungeon;
    private final DungeonFactory dungeonFactory;
    private TurnoService turnoService;
    private GiocatoreService giocatoreService;
    private RandomSingleton randomGenerale = RandomSingleton.getInstance();
    private Gioco gioco;
    private FileService fileService;

    private int turnoCorrente = 0;

    public GiocoService(List<Giocatore> giocatori, GiocatoreService giocatoreService, int turnoCorrente, DungeonFactory dungeonFactory, Gioco gioco) {
        this.giocatori = giocatori;
        this.giocatoreService = giocatoreService;
        this.turnoCorrente = turnoCorrente;
        this.dungeonFactory = dungeonFactory;
        this.gioco = gioco;
    }

    public GiocoService(DungeonFactory dungeonFactory, Gioco gioco) {
        this.dungeonFactory = dungeonFactory;
        this.gioco = gioco;
    }

    // Costruttore per compatibilità PassaggioSegretoServiceImpl
    public GiocoService() {
        this.dungeonFactory = null;
    }

    public <T extends Personaggio> void avviaPartita(List<T> personaggi) {
        this.dungeon = dungeonFactory.creaDungeon();

        Stanza start = dungeon.getStanza(0, 0);
        if (start != null) {
            start.setStatoStanza(true);
        }

        for (Personaggio personaggio : personaggi) {
            personaggio.setPosizioneCorrente(start);
        }
    }

    public Dungeon getDungeon() {
        return dungeon;
    }
    public Gioco getGioco(){
        return this.gioco;
    }

    public List<Giocatore> getGiocatori() {
        return giocatori;
    }

    public void setGiocatori(List<Giocatore> giocatori) {
        this.giocatori = giocatori;
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
            System.out.println("Non esiste una direzione chiamata: " + direzione.name());
            return false;
        }
        // Controllo stanza bloccata.
        if (destinazione.isBloccata()) {
            domain.Chiave richiesta = destinazione.getChiaveRichiesta();
            if (richiesta == null) {

                return false;
            }

            Zaino zaino = personaggio.getZaino();
            boolean trovato = false;
            if (zaino != null && zaino.getListaOggetti() != null) {
                for (Oggetto oggetto : new ArrayList<>(zaino.getListaOggetti())) {
                    if (oggetto instanceof domain.Chiave && ((domain.Chiave) oggetto).getId() == richiesta.getId()) {
                        System.out.println("Hai la chiave richiesta (" + oggetto.getNome() + "). Sblocco la stanza e consumo la chiave.");
                        destinazione.sblocca();
                        FileService.getInstance().writeLog(personaggio.getNomePersonaggio() + " ha usato la chiave: " + oggetto.getNome() + " per sbloccare la stanza: " + destinazione.getId());
                        ZainoService zainoService = new ZainoService();
                        zainoService.rimuoviOggettoDaZaino(zaino, oggetto);
                        trovato = true;
                        break;
                    }
                }
            }
            if (!trovato) {
                System.out.println("La stanza è bloccata.Non possiedi la chiave richiesta nello zaino. Impossibile entrare." + " Chiave richiesta: " + richiesta.getId());
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
            for (Evento evento : new ArrayList<>(destinazione.getListaEventiAttivi())) {
                if (evento instanceof Trappola trappola) {
                    trappole.add(trappola);
                }
            }
            if (!trappole.isEmpty()) {
                StringBuilder stringaTrappola = new StringBuilder();
                for (Trappola trappola : trappole) {
                    stringaTrappola.append("[").append(trappola.getDescrizione()).append("]");
                }

                int sceltaIndice = randomGenerale.prossimoNumero(0, trappole.size() - 1);
                Trappola scelta = trappole.get(sceltaIndice);
                System.out.println(ANSI.BRIGHT_RED + ANSI.BOLD + "Sei caduto in una trappola:" + scelta.getDescrizione() + ANSI.RESET);
                boolean consumaTurno = new TrappolaServiceImpl().attivaEvento(personaggio, scelta);
                System.out.println(" " + "Attivazione trappola" + " " + consumaTurno);

            }

        }
        if (destinazione != null && destinazione.isUscitaVittoria()) {
            System.out.println("Hai raggiunto l'uscita! " + personaggio.getNomePersonaggio() + " sei il vincitore!");
            gioco.setStatoGioco(StatoGioco.CONCLUSO);
            personaggio.setVincitore(true);
            return false;

        }
        return true;
    }

}
