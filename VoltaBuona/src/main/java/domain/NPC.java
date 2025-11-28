package domain;

import java.util.ArrayList;
import java.util.List;

public class NPC extends PersonaIncontrata {

    private String rebus;
    private String rispostaCorretta;
    private List<Oggetto> oggettiDaDonare;
    private boolean haInteragito = false;

    public NPC(int id, String nome, String rebus, String rispostaCorretta, List<Oggetto> oggetti) {
        super(id, false, false, "NPC: " + nome, nome);
        this.rebus = rebus;
        this.rispostaCorretta = rispostaCorretta;
        this.oggettiDaDonare = (oggetti != null) ? new ArrayList<>(oggetti) : new ArrayList<>();
    }


    public String getRebus() {
        return rebus;
    }

    public void setRebus(String rebus) {
        this.rebus = rebus;
    }

    public String getRispostaCorretta() {
        return rispostaCorretta;
    }

    public void setRispostaCorretta(String rispostaCorretta) {
        this.rispostaCorretta = rispostaCorretta;
    }

    public List<Oggetto> getOggettiDaDonare() {
        return oggettiDaDonare;
    }

    public void setOggettiDaDonare(List<Oggetto> oggettiDaDonare) {
        this.oggettiDaDonare = oggettiDaDonare;
    }

    public boolean haInteragito() {
        return haInteragito;
    }

    public void setHaInteragito(boolean haInteragito) {
        this.haInteragito = haInteragito;
    }

    

    /** NPC pone il rebus al giocatore */
    public String proponiRebus() {
        return "\nNPC " + getNome() + " ti chiede: \n❓ " + rebus;
    }

    /** Verifica la risposta del giocatore */
    public boolean verificaRisposta(String rispostaGiocatore) {
        if (rispostaGiocatore == null) return false;
        return rispostaGiocatore.trim().equalsIgnoreCase(rispostaCorretta);
    }

    /** Controlla se l’NPC ha oggetti da dare */
    public boolean haOggettiDaDare() {
        return !oggettiDaDonare.isEmpty();
    }

    /** Restituisce un oggetto e lo rimuove dalla lista */
    public Oggetto daOggetto() {
        if (oggettiDaDonare.isEmpty()) return null;
        return oggettiDaDonare.remove(0);
    }

    @Override
    public String toString() {
        return "NPC{" +
                "nome='" + getNome() + '\'' +
                ", rebus='" + rebus + '\'' +
                ", doni=" + oggettiDaDonare.size() +
                '}';
    }
}