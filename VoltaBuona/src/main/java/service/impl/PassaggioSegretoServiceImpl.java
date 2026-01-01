package service.impl;

import java.util.concurrent.atomic.AtomicInteger;

import domain.Evento;
import domain.PassaggioSegreto;
import domain.Personaggio;
import domain.Stanza;
import service.EventoService;


public class PassaggioSegretoServiceImpl implements EventoService {
     private static final AtomicInteger ID_COUNTER = new AtomicInteger(1);

    @Override
    public void rimuoviEventoDaStanza(Stanza stanza, Evento evento){
        stanza.getListaEventiAttivi().remove(evento);
    };

    @Override
public boolean attivaEvento(Personaggio personaggio, Evento e) {
    if (personaggio == null || e == null) return false;
    if (e instanceof PassaggioSegreto ps) {
        Stanza stanzaCorrente = personaggio.getPosizioneCorrente();
        if (ps.isScoperto()) {
            System.out.println("Hai già scoperto un passaggio segreto qui.");
            return false; // non consuma il turno
        }

        System.out.println("Hai trovato un possibile passaggio segreto...");

        // se il passaggio richiede un rebus, prova un controllo casuale
        if (ps.isRebusApertura()) {
            var rnd = java.util.concurrent.ThreadLocalRandom.current();
            int tiro = rnd.nextInt(1, 7); // 1..6
            System.out.println("Tiro per aprire il rebus: " + tiro + " (CD 4)");
            if (tiro >= 4) {
                ps.setScoperto(true);
                System.out.println("Hai risolto il rebus: il passaggio segreto si apre!");
            } else {
                System.out.println("Non riesci a risolvere il rebus ora.");
                return false; // fallito, non consuma il turno
            }
        } else {
            ps.setScoperto(true);
            System.out.println("Hai scoperto un passaggio segreto!");
        }

        // se c'è una destinazione nota, collegala alla stanza corrente (chiave riportata per debug)
        if (stanzaCorrente != null && ps.getDestinazione() != null) {
            String key = "SEGRETO_" + ps.getId();
            stanzaCorrente.getStanzaAdiacente().put(key, ps.getDestinazione());
            System.out.println("Un varco verso '" + ps.getDestinazione().getId() + "' è ora visibile (chiave: " + key + ").");
        }

        return false; // di default i passaggi segreti non consumano il turno
    }
    return false;
}

    @Override   
    public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza){   
        for (Evento e : stanza.getListaEventiAttivi()) {
            boolean termina = attivaEvento(personaggio, e);
            if (termina) return;
        }
    };

   @Override
public Evento aggiungiEventoCasuale() {
    int id = ID_COUNTER.getAndIncrement();
    // il generator caller dovrà impostare stanzaOrigine/destinazione dopo la creazione
    Stanza destinazione = null;
    boolean rebusApertura = true; // il rebus d'apertura c'è sempre
    boolean scoperto = false;

    return new PassaggioSegreto(destinazione, rebusApertura, scoperto, id, true, false, null);
}
}
