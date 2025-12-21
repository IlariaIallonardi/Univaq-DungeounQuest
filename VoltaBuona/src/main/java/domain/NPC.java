package domain;

import java.util.ArrayList;
import java.util.List;

public class NPC extends PersonaIncontrata {

    private String rebus;// domanda posta dall'NPC
    private String rispostaCorretta;
    private List<Oggetto> oggettiDaDonare;
    private boolean haInteragito = false;
    private String nomeNPC;
    private Stanza posizioneCorrenteNPC;

    private boolean venditore = false;
    private List<Oggetto> articoli;


    public NPC(int id, String tipoPersonaIncontrata, String rebus, String rispostaCorretta, List<Oggetto> oggetti, String nomeNPC, Stanza posizioneCorrenteNPC) {
        super(id, false, false, rispostaCorretta, tipoPersonaIncontrata);
        this.rebus = rebus;
        this.rispostaCorretta = rispostaCorretta;
        this.oggettiDaDonare = (oggetti != null) ? new ArrayList<>(oggetti) : new ArrayList<>();
        this.nomeNPC = nomeNPC;
        this.posizioneCorrenteNPC = posizioneCorrenteNPC;
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

    /**
     * NPC pone il rebus al giocatore
     */
    public String proponiRebus() {
        return "\nNPC " + getTipoPersonaIncontrata() + " ti chiede: \n " + rebus;
    }

    /**
     * Verifica la risposta del giocatore
     */
    public boolean verificaRisposta(String rispostaGiocatore) {
        if (rispostaGiocatore == null) {
            return false;
        }
        return rispostaGiocatore.trim().equalsIgnoreCase(rispostaCorretta);
    }

    /**
     * Controlla se l’NPC ha oggetti da dare
     */
    public boolean haOggettiDaDare() {
        return !oggettiDaDonare.isEmpty();
    }

    /**
     * Restituisce un oggetto e lo rimuove dalla lista
     */
    public Oggetto daOggetto() {
        if (oggettiDaDonare.isEmpty()) {
            return null;
        }
        return oggettiDaDonare.remove(0);
    }

    public String getNomeNPC() {
        return nomeNPC;
    }

    public void setNomeNPC(String nomeNPC) {
        this.nomeNPC = nomeNPC;
    }

    public Stanza getPosizioneCorrenteNPC() {
        return posizioneCorrenteNPC;
    }

    public void setPosizioneCorrenteNPC(Stanza posizioneCorrenteNPC) {
        this.posizioneCorrenteNPC = posizioneCorrenteNPC;
    }

    @Override
    public String toString() {
        return "NPC{"
                + "nome='" + getNomeNPC() + '\''
                + ", rebus='" + rebus + '\''
                + ", doni=" + oggettiDaDonare.size()
                + '}';
    }
}
