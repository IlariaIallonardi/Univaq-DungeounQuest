package service;

public interface GiocoService  {

    void inizioPartita();
    void finePartita();
    void caricaPartita();

    void salvaPartita();

    public int lanciaDado();
}
