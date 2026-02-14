package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import domain.Stato.StatoGioco;

public class Gioco implements Serializable {

    private static final long serialVersionUID = -3528203625917821226L;
    private int id;
    private List<Stanza> listaStanze = new ArrayList<>();
    private List<Personaggio> listaPersonaggi = new ArrayList<>();
    private List<Evento> listaEventi = new ArrayList<>();
    private List<Oggetto> listaOggetti = new ArrayList<>();
    private int turnoCorrente = 0;
    private StatoGioco statoGioco;
    private Dungeon dungeon;
    private Turno turno;

    public Gioco(int id, List<Stanza> listaStanze, List<Personaggio> listaPersonaggi, List<Evento> listaEventi, List<Oggetto> listaOggetti, int turnoCorrente, StatoGioco statoGioco, Dungeon dungeon, Turno turno) {
        this.id = id;
        this.listaStanze = listaStanze;
        this.listaPersonaggi = listaPersonaggi;
        this.listaEventi = listaEventi;
        this.listaOggetti = listaOggetti;
        this.turnoCorrente = turnoCorrente;
        this.statoGioco = statoGioco;
        this.dungeon = dungeon;
        this.turno = turno;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public List<Stanza> getListaStanze() {
        return new ArrayList<>(listaStanze);
    }

    public void setListaStanze(List<Stanza> listaStanze) {
        this.listaStanze = new ArrayList<>(listaStanze);
    }

    public List<Personaggio> getListaPersonaggi() {
        return new ArrayList<>(listaPersonaggi);
    }

    public void setListaPersonaggi(List<Personaggio> listaPersonaggi) {
        this.listaPersonaggi = new ArrayList<>(listaPersonaggi);
    }

    public List<Evento> getListaEventi() {
        return new ArrayList<>(listaEventi);
    }

    public void setListaEventi(List<Evento> listaEventi) {
        this.listaEventi = new ArrayList<>(listaEventi);
    }

    public List<Oggetto> getListaOggetti() {
        return new ArrayList<>(listaOggetti);
    }

    public void setListaOggetti(List<Oggetto> listaOggetti) {
        this.listaOggetti = new ArrayList<>(listaOggetti);
    }

    public int getTurnoCorrente() {
        return turnoCorrente;
    }

    public void setTurnoCorrente(int turnoCorrente) {
        this.turnoCorrente = turnoCorrente;
    }

    public StatoGioco getStatoGioco() {
        return statoGioco;
    }

    public void setStatoGioco(StatoGioco statoGioco) {
        this.statoGioco = statoGioco;
    }

}
