package service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import domain.Evento;
import domain.PassaggioSegreto;
import domain.Personaggio;
import domain.Stanza;
import service.EventoService;

public class PassaggioSegretoServiceImpl implements EventoService {

    private static final AtomicInteger ID_COUNTER = new AtomicInteger(1);

    @Override
    public void rimuoviEventoDaStanza(Stanza stanza, Evento evento) {
        stanza.getListaEventiAttivi().remove(evento);
    }

    ;

   /*  @Override
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
}*/
@Override
    public boolean attivaEvento(Personaggio personaggio, Evento e) {
        if (personaggio == null || e == null) {
            return false;
        }
        if (e instanceof PassaggioSegreto ps) {
            Stanza stanzaCorrente = personaggio.getPosizioneCorrente();
            if (ps.isScoperto()) {
                System.out.println("Hai già scoperto un passaggio segreto qui.");
                return false; // non consuma il turno
            }

            System.out.println("Hai trovato un possibile passaggio segreto...");

            if (ps.getRebusApertura() != null) {
                // assicurati che il rebus sia impostato
                if (ps.getRebusApertura() == null || ps.getRispostaRebus() == null) {
                    // fallback: crea un rebus semplice
                    ps.setRebusApertura("Qual è la capitale d'Italia?");
                    ps.setRispostaRebus("Roma");
                }

                System.out.println("Rebus: " + ps.getRebusApertura());

                // comportamento BOT: prova automaticamente con probabilità di successo
                if (personaggio instanceof domain.Computer) {
                    double chance = 0.75; // 75% chance di risolvere
                    double v = Math.random();
                    if (v <= chance) {
                        ps.setScoperto(true);
                        System.out.println(personaggio.getNomePersonaggio() + " (bot) risolve il rebus e apre il passaggio!");
                    } else {
                        System.out.println(personaggio.getNomePersonaggio() + " (bot) non risolve il rebus.");
                        return false;
                    }
                } else {
                    // giocatore umano: chiedo input
                    java.util.Scanner scanner = new java.util.Scanner(System.in);
                    System.out.print("Inserisci la soluzione: ");
                    String risposta = scanner.nextLine().trim();
                    if (risposta.equalsIgnoreCase(ps.getRispostaRebus().trim())) {
                        ps.setScoperto(true);
                        System.out.println("Hai risolto il rebus: il passaggio segreto si apre!");
                    } else {
                        System.out.println("Risposta errata. Non riesci a risolvere il rebus ora.");
                        return false;
                    }
                }
            } else {
                ps.setScoperto(true);
                System.out.println("Hai scoperto un passaggio segreto!");
            }

            // se c'è una destinazione nota, collegala alla stanza corrente con una chiave leggibile
            if (stanzaCorrente != null && ps.getDestinazione() != null) {
                String key = "SEGRETO_" + ps.getId();
                stanzaCorrente.getStanzaAdiacente().put(key, ps.getDestinazione());
                System.out.println("Un varco verso stanza id '" + ps.getDestinazione().getId() + "' è ora visibile (direzione: " + key + ").");
            }

            return false; // non consuma il turno
        }
        return false;
    }

    @Override
    public void  eseguiEventiInStanza(Personaggio personaggio, Stanza stanza) {
        for (Evento e : stanza.getListaEventiAttivi()) {
            boolean termina = attivaEvento(personaggio, e);
            if (termina) {
                return;
            }
        }
        return;
    }

    ;

  @Override
public Evento aggiungiEventoCasuale() {
    int id = ID_COUNTER.getAndIncrement();
    Stanza destinazione = null;
    boolean rebusApertura = true;
    boolean scoperto = false;

    PassaggioSegreto passaggioSegreto = new PassaggioSegreto(destinazione, "", scoperto, id, false, false, null, "");   

    // serie di rebus semplici - scegli uno casuale
    List<String[]> qa = List.of(
        new String[] {"Qual è la capitale d'Italia?", "Roma"},
        new String[] {"Quanto fa 2 + 2?", "4"},
        new String[] {"Di che colore è il cielo in una giornata serena?", "Blu"},
        new String[] {"Quanti giorni ci sono in una settimana?", "7"},
        new String[] {"Qual è il contrario di 'caldo'?", "Freddo"}
    );
    var rnd = java.util.concurrent.ThreadLocalRandom.current();
    var pair = qa.get(rnd.nextInt(qa.size()));
    passaggioSegreto.setRebusApertura(pair[0]);
    passaggioSegreto.setRispostaRebus(pair[1]);

    return passaggioSegreto;
}
}
