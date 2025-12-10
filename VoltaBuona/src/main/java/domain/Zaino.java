package domain;

import java.util.ArrayList;
import java.util.List;

public class Zaino {
    private int id;
    private int capienza = 10;
    private List<Oggetto> listaOggetti = new ArrayList<>();
    public Zaino() {
        this.id = id;
        this.capienza = capienza;
        this.listaOggetti = new ArrayList<>();
    }

    public boolean aggiungi(Oggetto o) {
        if (listaOggetti.size() >= capienza) return false;
        return listaOggetti.add(o);
    }

    public boolean rimuoviOggettoDaZaino(Oggetto oggetto) {
        return listaOggetti.remove(oggetto);
    }


     //forse lo ha messo la chat
    public Oggetto prendiOggetto(String nome) {
        return listaOggetti.stream()
                .filter(o -> o.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
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
