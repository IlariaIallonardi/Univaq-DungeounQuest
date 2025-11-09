package domain;

import java.util.ArrayList;
import java.util.List;

public class Zaino {
    private int id;
    private int capienza = 10;
    private List<Oggetto> listaOggetti = new ArrayList<>();
    public Zaino(int id, int capienza) {
        this.id = id;
        this.capienza = capienza;
        this.listaOggetti = new ArrayList<>();
    }

    public boolean aggiungi(Oggetto o) {
        if (listaOggetti.size() >= capienza) return false;
        return listaOggetti.add(o);
    }

    public boolean rimuovi(Oggetto o) {
        return listaOggetti.remove(o);
    }


     //forse lo ha messo la chat
    public Oggetto prendiOggetto(String nome) {
        return listaOggetti.stream()
                .filter(o -> o.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }
}
