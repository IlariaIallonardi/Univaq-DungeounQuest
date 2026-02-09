package service;
import domain.Oggetto;
import domain.Personaggio;
import domain.Stanza;
import domain.Zaino;
import service.impl.RandomSingleton;
import service.impl.ScannerSingleton;

public class ZainoService {
        private RandomSingleton randomGenerale = RandomSingleton.getInstance();
        private ScannerSingleton scannerGenerale = ScannerSingleton.getInstance();



      public boolean aggiungiOggettoAZaino(Zaino zaino, Oggetto oggetto) {
        if (oggetto == null) return false;
            
      zaino.getListaOggetti().add(oggetto);
        return true;
    }

   public boolean rimuoviOggettoDaZaino(Zaino zaino, Oggetto oggetto) {
        if (oggetto == null) {

            return false;
        }
        boolean rimozioneOggetto= zaino.getListaOggetti().remove(oggetto);
        if (rimozioneOggetto) {
            return true;
        }
           
        return rimozioneOggetto;
    }


  
    public boolean èPieno(Zaino zaino, Stanza stanza, Oggetto oggetto, Personaggio personaggio) {

        //Zaino ancora non pieno.
         if (zaino.getListaOggetti().size() < zaino.getCapienza()) {
            aggiungiOggettoAZaino(zaino,oggetto);
            stanza.rimuoviOggetto(oggetto);
            System.out.println(personaggio.getNomePersonaggio() + " raccoglie " + oggetto.getNome());
            return true;
        }

        //  Zaino pieno.
        System.out.println("Zaino pieno! Oggetti presenti:");
        for (int i = 0; i < zaino.getCapienza(); i++) {
         System.out.println((i + 1) + ") "  + zaino.getListaOggetti().get(i).getNome());
        }

        
        System.out.print("Vuoi eliminare un oggetto per fare spazio? (si/no): ");
        String risposta = scannerGenerale.leggiLinea().trim().toLowerCase();

        if (risposta.equals("no")) {
            System.out.println("Oggetto non raccolto.");
            return false;
        }

        System.out.print("Scegli il numero dell'oggetto da eliminare (0 annulla): ");
        int scelta;

        try {
            scelta = Integer.parseInt(scannerGenerale.leggiLinea());
        } catch (NumberFormatException e) {
            System.out.println("Input non valido.");
            return false;
        }

        if (scelta <= 0 || scelta > zaino.getCapienza()) {
            System.out.println("Scelta annullata.");
            return false;
        }

        Oggetto daRimuovere = zaino.getListaOggetti().get(scelta - 1);

        rimuoviOggettoDaZaino(zaino, daRimuovere);
        stanza.aggiungiOggetto(daRimuovere);

        aggiungiOggettoAZaino(zaino, oggetto);
        stanza.rimuoviOggetto(oggetto);

        System.out.println(personaggio.getNomePersonaggio() + " lascia " + daRimuovere.getNome()  + " e raccoglie " + oggetto.getNome());

        return true;
    }

    
     
    
}
