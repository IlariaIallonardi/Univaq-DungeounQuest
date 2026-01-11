package domain;

import domain.Mago.TipoMagiaSacra;

public class Combattimento {

    private int id;
    //private int ordineDiIniziativa; --> service
    private Stanza stanza;
    private int turnoCorrenteCombattimento;//durata combattimento
    private boolean inCorso;
    private int danniInflittiCombattimento;
    private Object vincitore;
    private Mostro.TipoAttaccoMostro attaccoMostro;//causati dal mostro
    private Personaggio personaggioCoinvolto;
    private Evento eventoMostro;
    private Mostro mostroCoinvolto; // --> eventoMostro
    private Integer iniziativa;
    private TipoMagiaSacra magiaSelezionata;

    public Combattimento(Mostro.TipoAttaccoMostro attaccoMostro, int danniInflittiCombattimento, Evento eventoMostro, int id, boolean inCorso, Personaggio personaggioCoinvolto, Stanza stanza, int turnoCorrenteCombattimento, Object vincitore, Mostro mostroCoinvolto, TipoMagiaSacra magiaSelezionata) {
        this.attaccoMostro = attaccoMostro;
        this.danniInflittiCombattimento = danniInflittiCombattimento;
        this.eventoMostro = eventoMostro;
        this.id = id;
        this.inCorso = inCorso;
        this.personaggioCoinvolto = personaggioCoinvolto;
        this.stanza = stanza;
        this.turnoCorrenteCombattimento = turnoCorrenteCombattimento;
        this.vincitore = vincitore;
        this.mostroCoinvolto = mostroCoinvolto;
        this.magiaSelezionata = magiaSelezionata;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Stanza getStanza() {
        return stanza;
    }

    public void setStanza(Stanza stanza) {
        this.stanza = stanza;
    }

    public int getTurnoCorrenteCombattimento() {
        return turnoCorrenteCombattimento;
    }

    public void setTurnoCorrenteCombattimento(int turnoCorrenteCombattimento) {
        this.turnoCorrenteCombattimento = turnoCorrenteCombattimento;
    }

    public boolean isInCorso() {
        return inCorso;
    }

    public void setInCorso(boolean inCorso) {
        this.inCorso = inCorso;
    }

    public int getDanniInflittiCombattimento() {
        return danniInflittiCombattimento;
    }

    public void setDanniInflittiCombattimento(int danniInflittiCombattimento) {
        this.danniInflittiCombattimento = danniInflittiCombattimento;
    }

    public Object getVincitore() {
        return vincitore;
    }

    public void setVincitore(Object vincitore) {
        this.vincitore = vincitore;
    }

    public Mostro.TipoAttaccoMostro getAttaccoMostro() {
        return attaccoMostro;
    }

    public void setAttaccoMostro(Mostro.TipoAttaccoMostro attaccoMostro) {
        this.attaccoMostro = attaccoMostro;
    }

    public Personaggio getPersonaggioCoinvolto() {
        return personaggioCoinvolto;
    }

    public void setPersonaggioCoinvolto(Personaggio personaggioCoinvolto) {
        this.personaggioCoinvolto = personaggioCoinvolto;
    }

    public Evento getEventoMostro() {
        return eventoMostro;
    }

    public void setEventoMostro(Evento eventoMostro) {
        this.eventoMostro = eventoMostro;
    }

    public Mostro getMostroCoinvolto() {
        return mostroCoinvolto;
    }

    public void setMostroCoinvolto(Mostro mostroCoinvolto) {
        this.mostroCoinvolto = mostroCoinvolto;
    }

    public Integer getIniziativa() {
        return iniziativa;
    }

    public void setIniziativa(Integer iniziativa) {
        this.iniziativa = iniziativa;
    }

    public TipoMagiaSacra getMagiaSelezionata() {
        return magiaSelezionata;
    }

    public void setMagiaSelezionata(TipoMagiaSacra magiaSelezionata) {
        this.magiaSelezionata = magiaSelezionata;
    }
}
