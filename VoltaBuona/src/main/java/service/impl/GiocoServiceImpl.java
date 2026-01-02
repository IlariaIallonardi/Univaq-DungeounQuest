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
        if (destinazione.getListaEventiAttivi() != null) {
    for (Evento e : new ArrayList<>(destinazione.getListaEventiAttivi())) {
        if (e instanceof Trappola) {
            boolean consumaTurno = new TrappolaServiceImpl().attivaEvento(personaggio, e);
            if (consumaTurno) {
                System.out.println("La trappola ha attivato un effetto che consuma il turno.");
            }
        }
    }
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

        return true;
    }

    /*  public boolean muoviPersonaggio(Personaggio personaggio, Direzione direzione) {
        if (personaggio == null) {
            return false;
        }

        Stanza corrente = personaggio.getPosizioneCorrente();
        if (corrente == null) {
            System.out.println("Il personaggio non è in nessuna stanza.");
            return false;
        }

        Stanza destinazione = null;
        switch (direzione) {
            case NORD ->
                destinazione = corrente.getStanzaAdiacente().get("NORD");
            case SUD ->
                destinazione = corrente.getStanzaAdiacente().get("SUD");
            case EST ->
                destinazione = corrente.getStanzaAdiacente().get("EST");
            case OVEST ->
                destinazione = corrente.getStanzaAdiacente().get("OVEST");
        }

        if (destinazione == null) {
            System.out.println("Non puoi andare in quella direzione.");
            return false;
        }

        if (destinazione.isBloccata()) {
            Chiave richiesta = destinazione.getChiaveRichiesta();
            if (richiesta == null) {
                System.out.println("La stanza in " + direzione + " è bloccata ma non ha chiave associata.");
                return false;
            }
            System.out.println("La stanza in " + direzione + " è bloccata. Chiave richiesta: id="
                    + richiesta.getId() + " nome=" + richiesta.getNome());

            Zaino zaino = personaggio.getZaino();
            boolean trovato = false;
            if (zaino != null && zaino.getListaOggetti() != null) {
                for (Oggetto o : new ArrayList<>(zaino.getListaOggetti())) {
                    if (o instanceof Chiave && ((Chiave) o).getId() == richiesta.getId()) {
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

        return true;
    }*/

    @Override
    public Dungeon getDungeon() {
        return null;
    }

    @Override
    public Dungeon creaDungeon(int righe, int colonne) {
        return null;
    }
;

/*
     * @Override
     * public void inizioPartita() {
     * 
     * Scanner sc = new Scanner(System.in);
     * 
     * int numGiocatori = 0;
     * 
     * // Creazione giocatori
     * while (true) {
     * System.out.print("Quanti giocatori parteciperanno? (2-6): ");
     * try {
     * numGiocatori = Integer.parseInt(sc.nextLine());
     * if (numGiocatori >= 2 && numGiocatori <= 6) {
     * break; // numero valido, esci dal ciclo
     * } else {
     * System.out.println("Il numero deve essere tra 2 e 6.");
     * }
     * } catch (NumberFormatException e) {
     * System.out.println("Inserisci un numero valido.");
     * }
     * }
     * // Ciclo per chiedere nome e tipo personaggio
     * for (int i = 1; i <= numGiocatori; i++) {
     * System.out.print("Giocatore " + i + ", inserisci il tuo nome: ");
     * String nome = sc.nextLine();
     * 
     * String tipo = "";
     * while (!tipo.equalsIgnoreCase("guerriero") &&
     * !tipo.equalsIgnoreCase("arciere") &&
     * !tipo.equalsIgnoreCase("paladino") &&
     * !tipo.equalsIgnoreCase("mago")) {
     * System.out.
     * print("Scegli il tuo personaggio (Guerriero/Arciere/Paladino/Mago): ");
     * tipo = sc.nextLine();
     * }
     * 
     * // 3Riepilogo giocatori
     * System.out.println("\nRiepilogo giocatori:");
     * for (Giocatore g : giocatori) {
     * System.out.println("👤 " + g.getNomeGiocatore() +
     * "  PV: " + g.getPuntiVita() +
     * "  Forza: " + g.getPuntiAttacco() +
     * "  Magia: " + g.getPuntiMana());
     * }
     * 
     * }
     * }
     * 
     * @Override
     * public void finePartita() {
     * 
     * }
     * 
     * @Override
     * public void caricaPartita() {
     * 
     * }
     * 
     * @Override
     * public void salvaPartita() {
     * 
     * }
     * 
     * @Override
     * public int lanciaDado() {
     * return 0;
     * }
 */
}
