package domain;

import java.util.List;

public class Turno {
     private int id;
    private Giocatore giocatoreAttivo;
    private List<Giocatore> giocatori;
    private int indiceGiocatoreAttuale;
    
    public Turno(Giocatore giocatoreAttivo, List<Giocatore> giocatori, int id, int indiceGiocatoreAttuale) {
        this.giocatoreAttivo = giocatoreAttivo;
        this.giocatori = giocatori;
        this.id = id;
        this.indiceGiocatoreAttuale = indiceGiocatoreAttuale;
    }
    public Turno() {
    }
 public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Giocatore getGiocatoreAttivo() {
        return giocatoreAttivo;
    }

    public void setGiocatoreAttivo(Giocatore giocatoreAttivo) {
        this.giocatoreAttivo = giocatoreAttivo;
    }

    public List<Giocatore> getGiocatori() {
        return giocatori;
    }

    public void setGiocatori(List<Giocatore> giocatori) {
        this.giocatori = giocatori;
    }

    public int getIndiceGiocatoreAttuale() {
        return indiceGiocatoreAttuale;
    }

    public void setIndiceGiocatoreAttuale(int indiceGiocatoreAttuale) {
        this.indiceGiocatoreAttuale = indiceGiocatoreAttuale;
    }
     
    

    public void eseguiTurno() {
        if (giocatoreAttivo != null) {
            giocatoreAttivo.setStatoPersonaggio("Attivo");
            String azioniDisponibili = scegliAzione();
            // Logica per gestire le azioni del giocatore
        }
    }

    public void terminaTurno() {
        if (giocatoreAttivo != null) {
            giocatoreAttivo.setStatoPersonaggio("InAttesa");
            prossimoGiocatore();
        }
    }

    public String scegliAzione() {
        if (giocatoreAttivo == null || !"Attivo".equals(giocatoreAttivo.getStatoPersonaggio())) {
            return "Nessuna azione disponibile";
        }
        return """
               Azioni disponibili:
               1. Muovi
               2. Combatti
               3. Usa oggetto
               4. Esplora
               5. Termina turno
               """;
    }

    public void prossimoGiocatore() {
        if (giocatori.isEmpty()) return;

        indiceGiocatoreAttuale = (indiceGiocatoreAttuale + 1) % giocatori.size();
        giocatoreAttivo = giocatori.get(indiceGiocatoreAttuale);
        giocatoreAttivo.setStatoPersonaggio("Attivo");
    }

    // Metodi per gestire la lista dei giocatori
    public void aggiungiGiocatore(Giocatore giocatore) {
        if (giocatore != null) {
            giocatori.add(giocatore);
        }
    }   

   
}
