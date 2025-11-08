package it.univaq.dungeon.npc;

import it.univaq.dungeon.oggetti.Oggetto;

import java.util.ArrayList;
import java.util.List;

public class NPC {
    private int id;
    private String nome;
    private String descrizione;
    private String rebus;
    private List<Oggetto> oggettoDaDare = new ArrayList<>();

    public String risolviRebus() {
        // risolve rebus e sblocca premio
        return "Rebus risolto";
    }

    public boolean daiOggetto(Oggetto o) {
        return oggettoDaDare.add(o);
    }
}
