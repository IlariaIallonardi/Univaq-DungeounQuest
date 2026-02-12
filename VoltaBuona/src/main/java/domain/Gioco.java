package domain;

import java.util.ArrayList;
import java.util.List;

public class Gioco {
    private int id;
    private List<Stanza> listaStanze = new ArrayList<>();
    private List<Giocatore> listaGiocatori = new ArrayList<>();
    private List<Evento> listaEventi = new ArrayList<>();
    private List<Oggetto> listaOggetti = new ArrayList<>();
    private int turnoCorrente = 0;
    private StatoGioco statoGioco;


    

    public Gioco(int id,List<Stanza> listaStanze,List<Giocatore> listaGiocatori,List<Evento> listaEventi,List<Oggetto> listaOggetti,int turnoCorrente,StatoGioco statoGioco) {
        this.id = id;
        this.listaStanze = listaStanze; 
        this.listaGiocatori = listaGiocatori; 
        this.listaEventi = listaEventi; 
        this.listaOggetti = listaOggetti; 
        this.turnoCorrente = turnoCorrente;
        this.statoGioco = statoGioco; 
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public StatoGioco getStatoGioco() {
        return statoGioco;
    }

    public void setStatoGioco(StatoGioco statoGioco) {
        this.statoGioco = statoGioco;
    }

   






    
}