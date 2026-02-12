package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Zaino implements Serializable {
    private int id;
    private int capienza; 
    private List<Oggetto> listaOggetti = new ArrayList<>();

    public Zaino() {
        this.id = 0;
        this.capienza = 5;
        this.listaOggetti = new ArrayList<>();
        
    }

    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapienza() {

        return capienza;
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
  

}