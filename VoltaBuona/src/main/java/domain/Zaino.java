package domain;

import java.util.ArrayList;
import java.util.List;

public class Zaino {
    private int id;
    private int capienza; 
    private List<Oggetto> listaOggetti = new ArrayList<>();

    public Zaino() {
        this.id = 0;
        this.capienza = 5;
        this.listaOggetti = new ArrayList<>();
        
    }

      public boolean aggiungiOggettoAZaino(Oggetto oggetto) {
        if (oggetto == null) return false;
        if (getCapienza() <= 0) { return false;}
        listaOggetti.add(oggetto);
        return true;
    }

   public boolean rimuoviOggettoDaZaino(Oggetto oggetto) {
        if (oggetto == null) {

            return false;
        }
        boolean removed = listaOggetti.remove(oggetto);
        if (removed) {
            return true;
        }
           
        return removed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapienza() {
        if(listaOggetti.size() >= capienza){
            return 0;
        }
        return  capienza - listaOggetti.size();
    }

    public void setCapienza(int nuovaCapacitaTotale) {
        this.capienza = nuovaCapacitaTotale;
    }
    
    public List<Oggetto> getListaOggetti() {
        return listaOggetti;
    }

    public void setListaOggetti(List<Oggetto> listaOggetti) {
        this.listaOggetti = listaOggetti;
    }
    public boolean isPieno() {
    return listaOggetti.size() >= capienza;
}

}