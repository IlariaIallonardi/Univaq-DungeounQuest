package service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import domain.Evento;
import domain.PassaggioSegreto;
import domain.Personaggio;
import domain.Stanza;
import service.Direzione;
import service.EventoService;
import service.FileService;
import service.GiocoService;
import service.TurnoService;

public class PassaggioSegretoServiceImpl implements EventoService {

    private static final AtomicInteger ID_CONTATORE = new AtomicInteger(1);
    private RandomSingleton randomGenerale = RandomSingleton.getInstance();
    private ScannerSingleton scannerGenerale = ScannerSingleton.getInstance();
   private FileService fileService;
    @Override
    public boolean attivaEvento(Personaggio personaggio, Evento evento) {

        if ((evento instanceof PassaggioSegreto passaggioSegreto)) {

            Stanza stanzaCorrente = personaggio.getPosizioneCorrente();

            if (passaggioSegreto.isScoperto()) {
                System.out.println("Hai già scoperto un passaggio segreto qui.");
            } else {
                System.out.println("Hai trovato un possibile passaggio segreto...");

                System.out.println("Rebus: " + passaggioSegreto.getRebusApertura());
                boolean isBot = personaggio.getNomePersonaggio() != null && (personaggio.getNomePersonaggio().startsWith("BOT_") || personaggio.getNomePersonaggio().startsWith("Bot-") || personaggio.getNomePersonaggio().toLowerCase().contains("bot"));
                if (isBot) {
                    int difficolta = randomGenerale.prossimoNumero(1, 50);
                    if (difficolta >= 25) {
                        passaggioSegreto.setScoperto(true);
                        System.out.println(personaggio.getNomePersonaggio() + "Il numero" + difficolta + " apre il passaggio!");
                    } else {
                        System.out.println(personaggio.getNomePersonaggio() + "Il numero " + difficolta + "non apre il passaggio.)");
                        return false;
                    }
                } else {

                    System.out.print("Inserisci la soluzione: ");
                    String risposta = scannerGenerale.leggiLinea().trim();
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
                System.out.println("\nVuoi muoverti in una delle stanze adiacenti adesso?");
                System.out.println("0) Annulla");
                Map<String, Stanza> adiacenti = stanzaCorrente.getStanzaAdiacente();
                List<String> direzioniValide = new ArrayList<>();
                adiacenti.forEach((chiave, s) -> {
                    if (s != null && s != stanzaCorrente) {
                        String stato = (s.isBloccata() ? " Bloccata" : "");
                        System.out.println(chiave + " " + s.getId() + stato);
                        if (!s.isBloccata()) {
                            direzioniValide.add(chiave);
                        }
                    }
                });

                System.out.print("Scegli una direzione (nome direzione): ");
                String input;
                boolean isBot = personaggio.getNomePersonaggio() != null && (personaggio.getNomePersonaggio().startsWith("BOT_") || personaggio.getNomePersonaggio().startsWith("Bot-") || personaggio.getNomePersonaggio().toLowerCase().contains("bot"));
                if (isBot) {
                    if (direzioniValide.isEmpty()) {
                        System.out.println("Non ci sono direzioni valide per muoverti.");
                        return false;
                    }
                    input = randomGenerale.scegliRandomicamente(direzioniValide);
                    System.out.println(personaggio.getNomePersonaggio() + " sceglie di muoversi verso: " + input);
                } else {
                    input = scannerGenerale.leggiLinea();
                }

                if (input == null || input.equals("0")) {
                    System.out.println("Hai annullato lo spostamento.");
                    return false;
                }

                Direzione direzione = Direzione.fromString(input);
                if (direzione == null) {
                    System.out.println("Direzione non valida: " + input);
                    return false;
                }

                boolean mosso = new GiocoService().muoviPersonaggio(personaggio, direzione);
                if (!mosso) {
                    System.out.println("Non riesci a muoverti verso " + direzione.name() + ".");
                    return false;
                }

                System.out.println("Ti sei spostato. La stanza viene esplorata:");
                new TurnoService().scegliAzione(personaggio);
                return true;
            }

        }

        return false;
    }

    @Override
    public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza) {
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
        var risposta = randomGenerale.scegliRandomicamente(domandaRisposta);

        /// Primo indice dell'array è la domanda.
        passaggioSegreto.setRebusApertura(risposta[0]);
        //Secondo indice dell' array è la risposta.
        passaggioSegreto.setRispostaRebus(risposta[1]);

        return passaggioSegreto;
    }
}
