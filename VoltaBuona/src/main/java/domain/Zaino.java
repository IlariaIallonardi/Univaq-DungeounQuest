package domain;

import java.util.ArrayList;
import java.util.List;

public class Zaino {
    private int id;
    private int capienza = 5; // posti disponibili
    private List<Oggetto> listaOggetti = new ArrayList<>();

    public Zaino() {
        this.id = 0;
        this.capienza = 5;
        this.listaOggetti = new ArrayList<>();
        System.out.println("[ZAINO] creato id=" + this.id + " capienza=" + this.capienza);
    }

      public boolean aggiungiOggettoAZaino(Oggetto oggetto) {
        if (oggetto == null) return false;
        System.out.println("[ZAINO] aggiungi richiesto: " + (oggetto.getNome() == null ? "<senza nome>" : oggetto.getNome())
            + " | capienza prima=" + getCapienza() + " | elementi=" + listaOggetti.size());
        if (getCapienza() <= 0) {
            System.out.println("[ZAINO] impossibile aggiungere: zaino pieno (capienza=" + getCapienza() + ")");
            return false;
        }
        listaOggetti.add(oggetto);
        System.out.println("[ZAINO] aggiunto: " + (oggetto.getNome() == null ? "<senza nome>" : oggetto.getNome())
            + " | capienza dopo=" + getCapienza() + " | elementi=" + listaOggetti.size());
        return true;
    }

   public boolean rimuoviOggettoDaZaino(Oggetto oggetto) {
        if (oggetto == null) {
            System.out.println("[ZAINO] rimozione fallita: oggetto nullo");
            return false;
        }
        boolean removed = listaOggetti.remove(oggetto);
        if (removed) {
            // non toccare capacitaTotale: getCapienza() è derivata da capacitaTotale - lista.size()
            System.out.println("[ZAINO] rimosso: " + (oggetto.getNome() == null ? "<senza nome>" : oggetto.getNome())
                + " | capienza ora=" + getCapienza() + " | elementi=" + listaOggetti.size());
        } else {
            System.out.println("[ZAINO] rimozione fallita: oggetto non trovato");
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
        return Math.max(0, capienza - (listaOggetti == null ? 0 : listaOggetti.size()));
    }

    public int setCapienza(int nuovaCapacitaTotale) {
        if (nuovaCapacitaTotale < 0) nuovaCapacitaTotale = 0;
        this.capienza = nuovaCapacitaTotale;
        return getCapienza();
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