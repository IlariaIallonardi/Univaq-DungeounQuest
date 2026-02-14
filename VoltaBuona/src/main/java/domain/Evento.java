package domain;

import java.io.Serializable;

import exception.DungeonException;

public class Evento implements Serializable{

    private int id;
    private boolean inizioEvento;
    private boolean fineEvento;
    private String descrizione;
    private String nomeEvento;
    private Stanza posizioneCorrente;

    public Evento(int id, boolean inizioEvento, boolean fineEvento, String descrizione, String nomeEvento, Stanza posizioneCorrente) {
        this.id = id;
        this.inizioEvento = inizioEvento;
        this.fineEvento = fineEvento;
        this.descrizione = descrizione;
        this.nomeEvento = nomeEvento;
        this.posizioneCorrente = posizioneCorrente;
    }


    public boolean attivo() {
        return inizioEvento && !fineEvento;
    }

    public boolean èRiutilizzabile() {
        // regola di riutilizzo
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isInizioEvento() {
        return inizioEvento;
    }

    public void setInizioEvento(boolean inizioEvento) {
        this.inizioEvento = inizioEvento;
    }

    public boolean isFineEvento() {
        return fineEvento;
    }

    public void setFineEvento(boolean fineEvento) {
        this.fineEvento = fineEvento;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    public int getInizioEvento(){
        return 1;
    }
    public int getFineEvento(){
        return 0;
    }
    public String getNomeEvento() {
        return nomeEvento;
    }
    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }
    public Stanza getPosizioneCorrente() {
        return posizioneCorrente;
    }
    public void setPosizioneCorrente(Stanza posizioneCorrente)throws DungeonException {
        if (posizioneCorrente == null) {
            throw new DungeonException("Posizione corrente nulla per l'evento.");
        }
        this.posizioneCorrente = posizioneCorrente;
    }



}
