package service;

import domain.Arma;
import domain.Armatura;
import domain.Chiave;
import domain.Mostro;
import domain.Oggetto;
import domain.Personaggio;
import domain.Pozione;
import domain.Stanza;
import domain.Trappola;
import domain.Zaino;

public class PersonaggioService {
private final service.impl.MostroServiceImpl mostroService = new service.impl.MostroServiceImpl();
    private Personaggio personaggio;

    public Personaggio creaPersonaggio(String nome, Personaggio personaggio) {
        personaggio.setNomeP(nome);
        return personaggio;
    }

    // ...existing code...
    /**
     * Il personaggio p attacca il mostro m. penso sia abbastanza giusto Ritorna
     * il danno inflitto (0 se input non valido).
     *
     */
    public int attacca(Personaggio p, Mostro m) {
        if (p == null || m == null) {
            return 0;
        }

        int att = 0;
        int dif = 0;
        try {
            att = p.getAttacco();
        } catch (NoSuchMethodError | NullPointerException ignored) {
        }
        try {
            dif = m.getDifesaMostro();
        } catch (NoSuchMethodError | NullPointerException ignored) {
        }

        int danno = Math.max(1, att - dif); // almeno 1 punto di danno
        try {
            m.setPuntiVitaMostro(m.getPuntiVitaMostro() - danno);
        } catch (NoSuchMethodError | NullPointerException ignored) {
        }

        try {
            if (m.getPuntiVitaMostro() <= 0) {
                // esempio: aggiungi esperienza al personaggio se esiste il metodo
                try {
                    int xp = 9; // se esiste
                    p.setEsperienza(p.getEsperienza() + xp);
                    if (p.getEsperienza() >= 20) {
                        p.setLivello(p.getLivello() + 1);
                        p.setEsperienza(0);
                    }
                } catch (Exception ignored) {
                }

            }
        } catch (Exception ignored) {
        }

        return danno;
    }

    // 23-11-2025 dovrebbe essere giusto
    /**
     * Uso generale di un oggetto: - verifica che l'oggetto sia nello zaino del
     * personaggio, - delega l'effetto all'oggetto (Pozione) o chiama i metodi
     * del service per equip/uso, - rimuove l'oggetto se consumato e registra
     * l'evento.
     */
    public boolean usaOggetto(Personaggio personaggio, Oggetto oggetto) {
        if (personaggio == null || oggetto == null) {
            return false;
        }
        Zaino z = personaggio.getZaino();
        if (z == null || !z.getListaOggetti().contains(oggetto)) {
            return false;
        }

        // Pozione: l'oggetto applica l'effetto sul personaggio
        if (oggetto instanceof Pozione) {
            boolean risultato = ((Pozione) oggetto).eseguiEffetto(personaggio);
            if (risultato) {
                z.rimuovi(oggetto);
                z.setCapienza(z.getCapienza() + 1);
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
            zaino.setCapienza(zaino.getCapienza() + 1);
            return true;
        }

        // Armatura: indossa (metodo service indossaArmatura)
        if (oggetto instanceof Armatura) {
            ((Armatura) oggetto).eseguiEffetto(personaggio);
            z.setCapienza(z.getCapienza() + 1);
            return true;
        }

        // la chiave si deve fare.
        if (oggetto instanceof Chiave) {
            ((Chiave) oggetto).eseguiEffetto(personaggio);

            z.rimuovi(oggetto);
            z.setCapienza(z.getCapienza() + 1);
            return true;
        }

        // oggetto non gestito:  mettere eccezione
        return false;
    }

    //incompleto finire ultima parte
    public boolean raccogliereOggetto(Personaggio personaggio, Oggetto oggetto) {
        if (personaggio == null || oggetto == null) {
            return false;
        }

        Zaino zaino = personaggio.getZaino();
        if (zaino == null) {
            return false;
        }

        Stanza stanza = personaggio.getPosizioneCorrente();
        if (stanza == null) {
            return false;
        }

        // verifica che l'oggetto sia nella stanza
        if (stanza.getOggettiPresenti() == null || !stanza.getOggettiPresenti().contains(oggetto)) {
            return false;
        }

        // verifica lista e capienza (capienza intesa come posti disponibili)
        if (zaino.getListaOggetti() == null) {
            return false;
        }

        if (zaino.getCapienza() < 5) {
            // spazio disponibile: aggiungi direttamente
            zaino.getListaOggetti().add(oggetto);
            zaino.setCapienza(zaino.getCapienza() - 1);
            stanza.rimuoviOggetto(oggetto);
            System.out.println(personaggio.getNomeP() + " raccoglie " + oggetto.getNome());
            return true;
        }

        // zaino pieno: mostra lista e chiedi se rimuovere qualcosa
        System.out.println("Zaino pieno. Oggetti nello zaino:");
        for (int i = 0; i < zaino.getListaOggetti().size(); i++) {
            Oggetto o = zaino.getListaOggetti().get(i);
            System.out.println((i + 1) + ") " + (o != null ? o.getNome() : "<oggetto null>"));
        }

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.print("Vuoi eliminare qualcosa per fare spazio? (s/n): ");
        String risposta = scanner.nextLine().trim().toLowerCase();
        if (!risposta.equals("s") && !risposta.equals("y")) {
            System.out.println("Operazione annullata. Non raccogliere l'oggetto.");
            return false;
        }

        System.out.print("Inserisci il numero dell'oggetto da rimuovere (0 per annullare): ");
        String line = scanner.nextLine().trim();
        int indice;
        try {
            indice = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("Input non valido. Operazione annullata.");
            return false;
        }
        if (indice == 0) {
            System.out.println("Operazione annullata.");
            return false;
        }
        if (indice < 1 || indice > zaino.getListaOggetti().size()) {
            System.out.println("Indice non valido. Operazione annullata.");
            return false;
        }

        // rimuovi l'oggetto scelto dallo zaino e rimetti nella stanza
        Oggetto rimosso = zaino.getListaOggetti().remove(indice - 1);
        zaino.setCapienza(zaino.getCapienza() + 1);
        // aggiungi l'oggetto rimosso nella stanza (usa il metodo disponibile)
        stanza.aggiungiOggetto(rimosso);
        System.out.println("Rimosso: " + (rimosso != null ? rimosso.getNome() : "<oggetto null>"));
        // ora aggiungi il nuovo oggetto raccolto
        zaino.getListaOggetti().add(oggetto);
        zaino.setCapienza(zaino.getCapienza() - 1);
        stanza.rimuoviOggetto(oggetto);

        System.out.println(personaggio.getNomeP() + " raccoglie " + oggetto.getNome());
        return true;
    }

    /**
     * Applica al personaggio il danno calcolato dal mostro. Ritorna il danno
     * effettivamente inflitto.
     */
    public int subisciDannoDaMostro(Mostro.TipoAttaccoMostro attaccoMostro,int dannoBase, Personaggio personaggio) {
        if (attaccoMostro == null || personaggio == null) {
            return 0;
        }
        int danno = mostroService.calcolaDannoPerTipo(attaccoMostro, dannoBase, personaggio);
        personaggio.setPuntiVita(personaggio.getPuntiVita() - danno); // aggiorna lo stato del personaggio
        return danno;
    }

    /**
     * Applica al personaggio il danno calcolato dalla trappola. Ritorna il
     * danno effettivamente inflitto.
     */
    public void subisciDannoDaTrappola(Trappola trappola, Personaggio personaggio) {
        if (trappola != null) {
            System.out.println("Hai trovato una trappola!");

            // Check di disinnesco
            boolean disinnescata = trappola.checkDiDisinnesco(personaggio);

            if (!disinnescata) {
                // Trappola si attiva
                trappola.attiva(personaggio);
            } else {
                System.out.println("La trappola è stata disinnescata.");
            }
        }

    }

    public void esploraStanza(Personaggio personaggio) {

        if (personaggio == null) {
            System.out.println("Errore: personaggio nullo.");
            return;
        }

        Stanza stanza = personaggio.getPosizioneCorrente();
        if (stanza == null) {
            System.out.println("Errore: posizione del personaggio non valida.");
            return;
        }

        System.out.println("🔍 " + personaggio.getNomeP() + " esplora la stanza...");

    }

}
