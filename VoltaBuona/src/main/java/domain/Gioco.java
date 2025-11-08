package domain;

import it.univaq.dungeon.personaggi.Giocatore;
import it.univaq.dungeon.personaggi.Mostro;
import it.univaq.dungeon.oggetti.Oggetto;
import it.univaq.dungeon.oggetti.Chiave;

import java.util.*;

public class Gioco {
    private int id;
    private String logGioco = "";
    private List<Giocatore> listaGiocatori = new ArrayList<>();
    private List<Evento> listaEventi = new ArrayList<>();
    private Dungeon dungeon;
    private StatoGioco stato = StatoGioco.IN_ATTESA;
    private int turnoCorrente = 0;
    private Giocatore vincitore;

    public Gioco(int id, Dungeon dungeon) {
        this.id = id;
        this.dungeon = dungeon;
    }

    public void eseguiTurno() {
        // orchestrazione del turno
        // 1. scegli azione del giocatore attivo
        // 2. risolvi eventi/combattimenti
        // 3. aggiorna turno
    }

    public void terminaTurno() {
        // gestione fine turno
    }

    public Giocatore prossimoGiocatore() {
        if (listaGiocatori.isEmpty())
            return null;
        turnoCorrente = (turnoCorrente + 1) % listaGiocatori.size();
        return listaGiocatori.get(turnoCorrente);
    }

    public String inizioCombattimento(Giocatore g, Mostro m) {
        stato = StatoGioco.IN_COMBATTIMENTO;
        return "Combattimento iniziato tra " + g.getNome() + " e " + m.getNome();
    }

    public String fineCombattimento() {
        stato = StatoGioco.IN_ESPLORAZIONE;
        return "Combattimento terminato.";
    }

    public void esplora(Giocatore g) {
        // logica di esplorazione stanza/adiacenze/eventi
    }

    // getters/setters omessi per brevità
}
