package domain;

import java.util.ArrayList;
import java.util.List;

public class NPC extends PersonaIncontrata {

    
    private String rebus;
    private List<Oggetto> oggettoDaDare = new ArrayList<>();

    
    public NPC(int danno, String messaggio, String nome, String rebus) {
        super(danno, messaggio, nome);
        this.rebus = rebus;
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


     public String risolviRebus() {
        // risolve rebus e sblocca premio
        return "Rebus risolto";
    }

    public boolean daiOggetto(Oggetto o) {
        return oggettoDaDare.add(o);
    }

}
