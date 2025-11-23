package service;



import domain.*;
import service.impl.*;



public class PersonaggioService {

    private Personaggio personaggio;

    public Personaggio creaPersonaggio(String nome, Personaggio g) {
        g.setNomeP(nome);
        return g;
    }

    public String aggiornaStatoPersonaggio(Personaggio g) {
        return null;
    }

    public boolean mortePersonaggio(Personaggio g) {
        return g.getPuntiVita() <= 0;
    }

   // ...existing code...
    /**
     * Il personaggio p attacca il mostro m.
     * penso sia abbastanza giusto
     * Ritorna il danno inflitto (0 se input non valido).
     *
     */
    public int attacca(Personaggio p, Mostro m) {
        if (p == null || m == null) return 0;

        int att = 0;
        int dif = 0;
        try { att = p.getAttacco(); } catch (NoSuchMethodError | NullPointerException ignored) {}
        try { dif = m.getDifesaMostro(); } catch (NoSuchMethodError | NullPointerException ignored) {}

        int danno = Math.max(1, att - dif); // almeno 1 punto di danno
        try {
            m.setPuntiVitaMostro(m.getPuntiVitaMostro() - danno);
        } catch (NoSuchMethodError | NullPointerException ignored) {}

        
        try {
            if (m.getPuntiVitaMostro() <= 0) {
                // esempio: aggiungi esperienza al personaggio se esiste il metodo
                try {
                    int xp = 9; // se esiste
                    p.setEsperienza(p.getEsperienza() + xp);
                    if(p.getEsperienza() >= 20){
                        p.setLivello(p.getLivello() + 1);
                        p.setEsperienza(0); 
                    }
                } catch (Exception ignored) {}
            
            }
        } catch (Exception ignored) {}

        return danno;
    }

         
    // 23-11-2025 dovrebbe essere giusto

    /**
     * Uso generale di un oggetto:
     * - verifica che l'oggetto sia nello zaino del personaggio,
     * - delega l'effetto all'oggetto (Pozione) o chiama i metodi del service per equip/uso,
     * - rimuove l'oggetto se consumato e registra l'evento.
     */
    public boolean usaOggetto(Personaggio personaggio, Oggetto oggetto) {
        if (personaggio == null || oggetto == null) return false;
        Zaino z = personaggio.getZaino();
        if (z == null || !z.getListaOggetti().contains(oggetto)) return false;

        // Pozione: l'oggetto applica l'effetto sul personaggio
        if (oggetto instanceof Pozione) {
            boolean risultato = ((Pozione) oggetto).eseguiEffetto(personaggio);
            if (risultato) {
                z.rimuovi(oggetto);
                System.out.println(personaggio.getNomeP() + " usa una pozione: " + ((Pozione) oggetto).getTipo());
            }
            return risultato;
        }

        // Arma: equipaggia / usa (metodo service usa Oggetto)
        if (oggetto instanceof Arma) {
            Zaino zaino = personaggio.getZaino();
            if (zaino == null || !zaino.getListaOggetti().contains(oggetto)) {
                return false; // L'arma non è nello zaino
            }
            ((Arma) oggetto).eseguiEffetto(personaggio);
            // decidere se rimuovere dall'inventario o mantenerla come equip
            zaino.rimuovi(oggetto);
            return true;
        }

        // Armatura: indossa (metodo service indossaArmatura)
        if (oggetto instanceof Armatura) {
            ((Armatura) oggetto).eseguiEffetto(personaggio);
            return true;
        }

        // la chiave si deve fare.
        if (oggetto instanceof Chiave) {
        ((Chiave) oggetto).eseguiEffetto(personaggio);
        
            z.rimuovi(oggetto);
            return true;
        }

        // oggetto non gestito:  mettere eccezione
        return false;
    }


   //incompleto finire ultima parte
    public boolean raccogliereOggetto(Personaggio personaggio, Oggetto oggetto) {
        if (personaggio == null || oggetto == null) return false;

        Zaino zaino = personaggio.getZaino();
        if (zaino == null) return false;

        Stanza stanza = personaggio.getPosizioneCorrente();
        if (stanza == null) return false;

        // verifica che l'oggetto sia nella stanza
        if (stanza.getOggettiPresenti() == null || !stanza.getOggettiPresenti().contains(oggetto)) return false;

        // aggiungi allo zaino e rimuovi dalla stanza
        if (zaino.getListaOggetti() == null) return false;
        if(zaino.getCapienza()<5){
        zaino.getListaOggetti().add(oggetto);
        zaino.setCapienza(zaino.getCapienza() + 1);
        }
        else{
            System.out.println("Zaino pieno, non puoi raccogliere altri oggetti.");
            //rimozione oggetto dallo zaino se è pieno e aggiunto nella stanza

            return  false;
        }
        stanza.rimuoviOggetto(oggetto);

        System.out.println(personaggio.getNomeP() + " raccoglie " + oggetto.getNome());
        return true;
    }
    
     /** 
     * Applica al personaggio il danno calcolato dal mostro.
     * Ritorna il danno effettivamente inflitto.
     */
    public int subisciDannoDaMostro(Mostro m, Personaggio g) {
        if (m == null || g == null) return 0;
        int danno = MostroServiceImpl.calcolaDanno(m, g);
        g.setPuntiVita(g.getPuntiVita() - danno); // aggiorna lo stato del personaggio
        return danno;
    }

    
    /** 
     * Applica al personaggio il danno calcolato dalla trappola.
     * Ritorna il danno effettivamente inflitto.
     */
    public int subisciDannoDaTrappola(int dannoTrappola ,Personaggio g) {
        if (g == null) return 0;
        int danno = TrappolaServiceImpl.calcolaDanno(dannoTrappola ,g);
        g.setPuntiVita(g.getPuntiVita() - danno); // aggiorna lo stato del personaggio
        return danno;
    }


   

    public String aggiornamentoStatoPersonaggio() {
        return "Stato aggiornato";
    }
  
   
    
}
