package domain;

import java.io.Serializable;
import java.util.List;

public class Turno implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private Personaggio giocatoreAttivo;
    private List<Personaggio> giocatori;
    private int indiceGiocatoreAttuale;
    private int turnoCorrente;

    public Turno(Personaggio giocatoreAttivo, List<Personaggio> giocatori, int id, int indiceGiocatoreAttuale, int turnoCorrente) {
        this.giocatoreAttivo = giocatoreAttivo;
        this.giocatori = giocatori;
        this.id = id;
        this.indiceGiocatoreAttuale = indiceGiocatoreAttuale;
        this.turnoCorrente = turnoCorrente;
    }

    public Turno() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Personaggio getGiocatoreAttivo() { return giocatoreAttivo; }
    public void setGiocatoreAttivo(Personaggio giocatoreAttivo) { this.giocatoreAttivo = giocatoreAttivo; }

    public List<Personaggio> getGiocatori() { return giocatori; }
    public void setGiocatori(List<Personaggio> giocatori) { this.giocatori = giocatori; }

    public int getIndiceGiocatoreAttuale() { return indiceGiocatoreAttuale; }
    public void setIndiceGiocatoreAttuale(int indiceGiocatoreAttuale) { this.indiceGiocatoreAttuale = indiceGiocatoreAttuale; }

    public void eseguiTurno() { if (giocatoreAttivo != null) giocatoreAttivo.setStatoPersonaggio("Attivo"); }

    public void terminaTurno() { if (giocatoreAttivo != null) { giocatoreAttivo.setStatoPersonaggio("InAttesa"); prossimoGiocatore(); turnoCorrente++; } }

    public String scegliAzione() {
        if (giocatoreAttivo == null || !"Attivo".equals(giocatoreAttivo.getStatoPersonaggio())) return "Nessuna azione disponibile";
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
        if (giocatori == null || giocatori.isEmpty()) return;
        indiceGiocatoreAttuale++;
        if (indiceGiocatoreAttuale >= giocatori.size()) indiceGiocatoreAttuale = 0;
        giocatoreAttivo = giocatori.get(indiceGiocatoreAttuale);
        giocatoreAttivo.setStatoPersonaggio("Attivo");
    }

    public void aggiungiGiocatore(Personaggio giocatore) { if (giocatore != null) giocatori.add(giocatore); }
    public int getTurnoCorrente() { return turnoCorrente; }
    public void setTurnoCorrente(int turnoCorrente) { this.turnoCorrente = turnoCorrente; }

}
