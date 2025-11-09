package domain;

import java.util.ArrayList;
import java.util.List;

public class Gioco {
    private int id;
    private int dado;
    private List<Stanza> listaStanze = new ArrayList<>();
    private List<Giocatore> listaGiocatori = new ArrayList<>();
    private List<Evento> listaEventi = new ArrayList<>();
    private List<Oggetto> listaOggetti = new ArrayList<>();
    private int turnoCorrente = 0;
    private StatoGioco statoG = StatoGioco.IN_ATTESA;
    private String logGioco = "";

    

// ...existing code...
    public Gioco(int id,
                 int dado,
                 List<Stanza> listaStanze,
                 List<Giocatore> listaGiocatori,
                 List<Evento> listaEventi,
                 List<Oggetto> listaOggetti,
                 int turnoCorrente,
                 StatoGioco statoG,
                 String logGioco) {
        this.id = id;
        this.dado = dado;
        this.listaStanze = listaStanze != null ? new ArrayList<>(listaStanze) : new ArrayList<>();
        this.listaGiocatori = listaGiocatori != null ? new ArrayList<>(listaGiocatori) : new ArrayList<>();
        this.listaEventi = listaEventi != null ? new ArrayList<>(listaEventi) : new ArrayList<>();
        this.listaOggetti = listaOggetti != null ? new ArrayList<>(listaOggetti) : new ArrayList<>();
        this.turnoCorrente = turnoCorrente;
        this.statoG = statoG != null ? statoG : StatoGioco.IN_ATTESA;
        this.logGioco = logGioco != null ? logGioco : "";
    }
// ...existing code...



    

    // Getter e Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDado() {
        return dado;
    }

    public void setDado(int dado) {
        this.dado = dado;
    }

    public List<Stanza> getListaStanze() {
        return new ArrayList<>(listaStanze);
    }

    public void setListaStanze(List<Stanza> listaStanze) {
        this.listaStanze = new ArrayList<>(listaStanze);
    }

    public List<Giocatore> getListaGiocatori() {
        return new ArrayList<>(listaGiocatori);
    }

    public void setListaGiocatori(List<Giocatore> listaGiocatori) {
        this.listaGiocatori = new ArrayList<>(listaGiocatori);
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

    public StatoGioco getStatoG() {
        return statoG;
    }

    public void setStatoG(StatoGioco statoG) {
        this.statoG = statoG;
    }

    public String getLogGioco() {
        return logGioco;
    }

    public void setLogGioco(String logGioco) {
        this.logGioco = logGioco;
    }


}