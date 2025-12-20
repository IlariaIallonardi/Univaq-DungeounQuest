package domain;

import java.util.ArrayList;
import java.util.List;

public class Zaino {
    private int id;
    private int capienza = 5;
    private List<Oggetto> listaOggetti = new ArrayList<>();
    public Zaino() {
        this.id = id;
        this.capienza = capienza;
        this.listaOggetti = new ArrayList<>();
    }

    public boolean aggiungiOggettoAZaino(Oggetto oggetto) {
        if (listaOggetti.size() >= capienza) return false;
        return listaOggetti.add(oggetto);
    }

    public boolean rimuoviOggettoDaZaino(Oggetto oggetto) {
        return listaOggetti.remove(oggetto);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapienza() {
        return capienza;
    }

    public void setCapienza(int capienza) {
        this.capienza = capienza;
    }

    public List<Oggetto> getListaOggetti() {
        return listaOggetti;
    }

    public void setListaOggetti(List<Oggetto> listaOggetti) {
        this.listaOggetti = listaOggetti;
    }
}
