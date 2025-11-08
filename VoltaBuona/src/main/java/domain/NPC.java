package domain;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getRebus() {
        return rebus;
    }

    public void setRebus(String rebus) {
        this.rebus = rebus;
    }

    public List<Oggetto> getOggettoDaDare() {
        return oggettoDaDare;
    }

    public void setOggettoDaDare(List<Oggetto> oggettoDaDare) {
        this.oggettoDaDare = oggettoDaDare;
    }
}
