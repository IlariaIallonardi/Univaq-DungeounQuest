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
    Zaino zaino=this;
    if (oggetto == null) return false;
    if (listaOggetti.size() >= capienza) return false;
    listaOggetti.add(oggetto);
    capienza = zaino.setCapienza(zaino.getCapienza()-1);
    return true;
}

public boolean rimuoviOggettoDaZaino(Oggetto oggetto) {
    boolean removed = listaOggetti.remove(oggetto);
    if (removed) capienza++;
    return removed;
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

    public int setCapienza(int capienza) {
        this.capienza = capienza;
        return this.capienza;
    }

    public List<Oggetto> getListaOggetti() {
        return listaOggetti;
    }

    public void setListaOggetti(List<Oggetto> listaOggetti) {
        this.listaOggetti = listaOggetti;
    }
}
