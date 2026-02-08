package service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import domain.Evento;
import domain.PassaggioSegreto;
import domain.Personaggio;
import domain.Stanza;
import service.Direzione;
import service.EventoService;

public class PassaggioSegretoServiceImpl implements EventoService {

    private static final AtomicInteger ID_CONTATORE = new AtomicInteger(1);

    @Override
    public boolean attivaEvento(Personaggio personaggio, Evento evento) {

        if ((evento instanceof PassaggioSegreto passaggioSegreto)) {

            Stanza stanzaCorrente = personaggio.getPosizioneCorrente();

            if (passaggioSegreto.isScoperto()) {
                System.out.println("Hai già scoperto un passaggio segreto qui.");
            } else {
                System.out.println("Hai trovato un possibile passaggio segreto...");

              

                System.out.println("Rebus: " + passaggioSegreto.getRebusApertura());

                if (personaggio instanceof domain.Computer) {
                    double possibilita = 0.75;
                    if (Math.random() <= possibilita) {
                        passaggioSegreto.setScoperto(true);
                        System.out.println(personaggio.getNomePersonaggio() + " (bot) risolve il rebus e apre il passaggio!");
                    } else {
                        System.out.println(personaggio.getNomePersonaggio() + " (bot) non risolve il rebus.");
                        return false;
                    }
                } else {
                    java.util.Scanner scanner = new java.util.Scanner(System.in);
                    System.out.print("Inserisci la soluzione: ");
                    String risposta = scanner.nextLine().trim();
                    if (risposta.equalsIgnoreCase(passaggioSegreto.getRispostaRebus().trim())) {
            
                        System.out.println("Hai risolto il rebus: il passaggio segreto si apre!");
                    } else {
                        System.out.println("Risposta errata. Non riesci a risolvere il rebus ora.");
                        return false;
                    }
                    passaggioSegreto.setScoperto(true);
                    
                }
            }

            if (stanzaCorrente != null && stanzaCorrente.getStanzaAdiacente() != null && !stanzaCorrente.getStanzaAdiacente().isEmpty()) {

               /*  List<Map.Entry<String, Stanza>> entries = new ArrayList<>(stanzaCorrente.getStanzaAdiacente().entrySet());
                entries.removeIf(en -> en.getValue() == null || en.getValue().getId() == stanzaCorrente.getId());
                   
                if (!entries.isEmpty()) {
                    if (personaggio instanceof domain.Computer) {
                    
                        if (Math.random() < 0.5) {
                            var rnd = java.util.concurrent.ThreadLocalRandom.current();
                            var en = entries.get(rnd.nextInt(entries.size()));
                            personaggio.setPosizioneCorrente(en.getValue());
                            System.out.println(personaggio.getNomePersonaggio() + " (bot) si sposta verso " + en.getKey() + " (stanza id=" + en.getValue().getId() + ") e la esplora.");
                            new TurnoServiceImpl((service.PersonaggioService) null).esploraStanza(personaggio);
                        } else {
                            System.out.println(personaggio.getNomePersonaggio() + " (bot) rimane nella stanza corrente.");
                        }
                    } else {*/
                     if(personaggio instanceof domain.Personaggio) {
                        System.out.println("\nVuoi muoverti in una delle stanze adiacenti adesso?");
                        System.out.println("0) Annulla");
                        stanzaCorrente.getStanzaAdiacente().forEach((chiave, s) -> {
                            if (s != null && s != stanzaCorrente) {
                                String stato = (s.isBloccata() ? " Bloccata" : "");
                                System.out.println(chiave + " " + s.getId() + stato);
                            }

                        });

                        java.util.Scanner scanner = new java.util.Scanner(System.in);
                        System.out.print("Scegli (direzione): ");
                        String sceltaKey = scanner.nextLine().trim().toUpperCase();

                        if ("0".equals(sceltaKey)) {
                            System.out.println("Hai annullato lo spostamento.");
                            return false;
                        }

                        Direzione direzione;
                        try {
                            direzione = Direzione.valueOf(sceltaKey);
                        } catch (IllegalArgumentException ex) {
                            System.out.println("Direzione non valida. Nessuno spostamento effettuato.");
                            return false;
                        }

                        
                        boolean mosso = new GiocoServiceImpl().muoviPersonaggio(personaggio, direzione);
                        if (!mosso) {
                            System.out.println("Non riesci a muoverti verso " + direzione.name() + ".");
                            return false;
                        }

                        System.out.println("Ti sei spostato. La stanza viene esplorata:");
                        new TurnoServiceImpl((service.PersonaggioService) null).scegliAzione(personaggio);
                    }
                
                return true;
            }

        }

        return false;
    }


    @Override
    public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza ) {
        for (Evento evento : stanza.getListaEventiAttivi()) {
            boolean termina = attivaEvento(personaggio, evento);
            if (termina) {
                return;
            }
        }
        return;
    }

        @Override
    public Evento aggiungiEventoCasuale() {
        int id = ID_CONTATORE.getAndIncrement();
        Stanza destinazione = null;
        boolean scoperto = false;

        PassaggioSegreto passaggioSegreto = new PassaggioSegreto(destinazione, "", scoperto, id, false, false, null, "");

        // serie di rebus semplici - scegli uno casuale
        List<String[]> domandaRisposta = List.of(
                new String[]{"L'inizio dell'eternità. La fine di ogni fine.L'inizio di un'epoca. E la fine di ogni paese.", "E"},
                new String[]{"Se lo alimenti vive,se gli dai da bere muore", "Fuoco"},
                new String[]{"Chi ha ucciso l'uomo ragno?", "non si sa"},
                new String[]{"Quanti giorni ci sono in una settimana?", "7"},
                new String[]{"Qual è il contrario di 'caldo'?", "Freddo"}
        );
        var random = java.util.concurrent.ThreadLocalRandom.current();
       var risposta = domandaRisposta.get(random.nextInt(domandaRisposta.size()));
        /// Primo indice dell'array è la domanda.
        passaggioSegreto.setRebusApertura(risposta[0]);
        //Secondo indice dell' array è la risposta.
        passaggioSegreto.setRispostaRebus(risposta[1]);

        return passaggioSegreto;
    }
}
