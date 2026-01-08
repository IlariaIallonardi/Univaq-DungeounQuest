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
        if (!(e instanceof PassaggioSegreto ps)) {
            return false;
        }

        Stanza stanzaCorrente = personaggio.getPosizioneCorrente();

        if (ps.isScoperto()) {
            System.out.println("Hai già scoperto un passaggio segreto qui.");
            return false;
        }

        System.out.println("Hai trovato un possibile passaggio segreto...");

        // gestione rebus (se presente)
        if (ps.getRebusApertura() != null) {
            // fallback se mancanti
            if (ps.getRebusApertura() == null || ps.getRispostaRebus() == null) {
                ps.setRebusApertura("Qual è la capitale d'Italia?");
                ps.setRispostaRebus("Roma");
            }

            System.out.println("Rebus: " + ps.getRebusApertura());

            if (personaggio instanceof domain.Computer) {
                double chance = 0.75;
                if (Math.random() <= chance) {
                    ps.setScoperto(true);
                    System.out.println(personaggio.getNomePersonaggio() + " (bot) risolve il rebus e apre il passaggio!");
                } else {
                    System.out.println(personaggio.getNomePersonaggio() + " (bot) non risolve il rebus.");
                    return false;
                }
            } else {
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

        // stampa stanze adiacenti alla stanza del passaggio (debug/guida)
        /*   if (stanzaCorrente != null && stanzaCorrente.getStanzaAdiacente() != null && !stanzaCorrente.getStanzaAdiacente().isEmpty()) {
        System.out.println("Stanze adiacenti a stanza id=" + stanzaCorrente.getId() + ":");
        stanzaCorrente.getStanzaAdiacente().forEach((chiave, s) -> {
             if (s != null && s != stanzaCorrente) {
                 System.out.println(" - " + chiave + " -> stanza id=" + s.getId());
             }  
            
           });
    } else {
        System.out.println("[DEBUG] Nessuna stanza adiacente da mostrare per stanza id=" + (stanzaCorrente != null ? stanzaCorrente.getId() : "null"));
    }

    // se non è stata impostata una destinazione, collega una stanza adiacente casuale (ma NON attivare eventi)
    if (ps.getDestinazione() == null && stanzaCorrente != null && stanzaCorrente.getStanzaAdiacente() != null && !stanzaCorrente.getStanzaAdiacente().isEmpty()) {
        List<Stanza> adiacenti = new java.util.ArrayList<>();
        for (Stanza s : stanzaCorrente.getStanzaAdiacente().values()) {
            if (s != null && s.getId() != stanzaCorrente.getId()) adiacenti.add(s);
        }
        if (!adiacenti.isEmpty()) {
            var rnd = java.util.concurrent.ThreadLocalRandom.current();
            Stanza scelta = adiacenti.get(rnd.nextInt(adiacenti.size()));
            ps.setDestinazione(scelta);
          //  String key = "SEGRETO_" + ps.getId();
           // stanzaCorrente.getStanzaAdiacente().put(key, scelta);
           // System.out.println("[DEBUG] Passaggio segreto collegato a stanza id=" + scelta.getId() + " (chiave: " + key + ")");
        } else {
            System.out.println("[DEBUG] Nessuna stanza adiacente valida per collegare il passaggio.");
        }
    }*/
        // Offri al giocatore la possibilità di spostarsi e esplorare immediatamente
        if (stanzaCorrente != null && stanzaCorrente.getStanzaAdiacente() != null && !stanzaCorrente.getStanzaAdiacente().isEmpty()) {

            List<java.util.Map.Entry<String, Stanza>> entries = new java.util.ArrayList<>(stanzaCorrente.getStanzaAdiacente().entrySet());
            // filtra eventuali null o riferimenti alla stessa stanza
            entries.removeIf(en -> en.getValue() == null || en.getValue().getId() == stanzaCorrente.getId());

            if (!entries.isEmpty()) {
                if (personaggio instanceof domain.Computer) {
                    // comportamento semplice per bot: 50% chance muoversi, scelta casuale
                    if (Math.random() < 0.5) {
                        var rnd = java.util.concurrent.ThreadLocalRandom.current();
                        var en = entries.get(rnd.nextInt(entries.size()));
                        personaggio.setPosizioneCorrente(en.getValue());
                        System.out.println(personaggio.getNomePersonaggio() + " (bot) si sposta verso " + en.getKey() + " (stanza id=" + en.getValue().getId() + ") e la esplora.");
                        new TurnoServiceImpl((service.PersonaggioService) null).esploraStanza(personaggio);
                    } else {
                        System.out.println(personaggio.getNomePersonaggio() + " (bot) rimane nella stanza corrente.");
                    }
                } else {
                    System.out.println("\nVuoi muoverti in una delle stanze adiacenti adesso?");
                    System.out.println("0) Annulla");
                    stanzaCorrente.getStanzaAdiacente().forEach((chiave, s) -> {
                        if (s != null && s != stanzaCorrente) {
                            System.out.println( chiave + " " + s.getId());
                        }
                        
                    });

                    java.util.Scanner scanner = new java.util.Scanner(System.in);
                    System.out.print("Scegli (direzione): ");
                    String sceltaKey =   scanner.nextLine().trim().toUpperCase();
                    
                    if ("0".equals(sceltaKey)) {
                        System.out.println("Hai annullato lo spostamento.");
                        return false;
                    }

// lookup case-insensitive della chiave direzione
                    Stanza destinazioneScelta = null;
                    for (java.util.Map.Entry<String, Stanza> en : stanzaCorrente.getStanzaAdiacente().entrySet()) {
                        String key = en.getKey();
                        Stanza s = en.getValue();
                        if (key != null && s != null && !s.equals(stanzaCorrente) && key.equalsIgnoreCase(sceltaKey)) {
                            destinazioneScelta = s;
                            break;
                        }
                    }

                    if (destinazioneScelta == null) {
                        System.out.println("Scelta non valida. Nessuno spostamento effettuato.");
                        return false;
                    }

                    personaggio.setPosizioneCorrente(destinazioneScelta);
                    System.out.println("Ti sei spostato nella stanza id=" + destinazioneScelta.getId() + ". La stanza viene esplorata:");
                    new TurnoServiceImpl((service.PersonaggioService) null).scegliAzione(personaggio, scanner);
                }
            }
        }

        // il passaggio segreto non consuma il turno di default
        return false;
    }

    @Override
    public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza
    ) {
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
                new String[]{"L'inizio dell'eternità. La fine di ogni fine.L'inizio di un'epoca. E la fine di ogni paese.","E"},
                new String[]{"Se lo alimenti vive,se gli dai da bere muore", "Fuoco"},
                new String[]{"Chi ha ucciso l'uomo ragno?", "non si sa"},
                new String[]{"Quanti giorni ci sono in una settimana?", "7"},
                new String[]{"Qual è il contrario di 'caldo'?", "Freddo"}
        );
        var rnd = java.util.concurrent.ThreadLocalRandom.current();
        var pair = qa.get(rnd.nextInt(qa.size()));
        passaggioSegreto.setRebusApertura(pair[0]);
        passaggioSegreto.setRispostaRebus(pair[1]);

        return passaggioSegreto;
    }
}
