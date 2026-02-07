package domain;

import java.util.ArrayList;
import java.util.List;

public class NPC extends PersonaIncontrata {

    private String rebus;
    private String rispostaCorretta;
    private boolean haInteragito = false;
    private String nomeNPC;

    private boolean venditore = false;
    private List<Oggetto> articoli;

    public NPC(int id, String tipoPersonaIncontrata, String rebus, String rispostaCorretta, List<Oggetto> oggetti, String nomeNPC) {
        super(id, false, false, rispostaCorretta, tipoPersonaIncontrata);
        this.rebus = rebus;
        this.rispostaCorretta = rispostaCorretta;
        this.nomeNPC = nomeNPC;

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

    public boolean haInteragito() {
        return haInteragito;
    }

    public void setHaInteragito(boolean haInteragito) {
        this.haInteragito = haInteragito;
    }

    public boolean isVenditore() {
        return venditore;
    }

    public void setVenditore(boolean venditore) {
        this.venditore = venditore;
    }

    public List<Oggetto> getArticoli() {
        if (articoli == null) {
            articoli = new ArrayList<>();
        }
        return articoli;
    }

    public void setArticoli(List<Oggetto> articoli) {
        this.articoli = articoli;
    }

    public String proponiRebus() {
        return "\nNPC " + getTipoPersonaIncontrata() + " ti chiede: \n " + rebus;
    }

    public boolean verificaRisposta(String rispostaGiocatore) {
        if (rispostaGiocatore == null) {
            return false;
        }
        return rispostaGiocatore.trim().equalsIgnoreCase(rispostaCorretta);
    }

    public Tesoro daOggetto(Oggetto oggetto) {
        if (oggetto instanceof Tesoro) {
            this.haInteragito = true;
            return (Tesoro) oggetto;
        }
        return null;
    }

    public String getNomeNPC() {
        return nomeNPC;
    }

    public void setNomeNPC(String nomeNPC) {
        this.nomeNPC = nomeNPC;
    }

    @Override
    public String toString() {
        return "NPC" + "nome='" + getNomeNPC() + " " + ", rebus='" + rebus;
    }
}
