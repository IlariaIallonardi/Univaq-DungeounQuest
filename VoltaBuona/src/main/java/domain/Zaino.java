package domain;

import java.util.ArrayList;
import java.util.List;

public class Zaino {
    private int capienza = 10;
    private List<Oggetto> listaOggetti = new ArrayList<>();
    private List<Effetto> effettiTemporanei = new ArrayList<>();

    public boolean aggiungi(Oggetto o) {
        if (listaOggetti.size() >= capienza) return false;
        return listaOggetti.add(o);
    }

    public boolean rimuovi(Oggetto o) {
        return listaOggetti.remove(o);
    }

    public Oggetto prendiOggetto(String nome) {
        return listaOggetti.stream()
                .filter(o -> o.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }
}
