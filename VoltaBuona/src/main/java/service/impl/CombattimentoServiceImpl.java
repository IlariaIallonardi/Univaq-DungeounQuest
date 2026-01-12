
package service.impl;

import java.util.Random;
import java.util.Scanner;

import domain.Combattimento;
import domain.Evento;
import domain.Mostro;
import domain.Personaggio;
import domain.Stanza;
import domain.Zaino;
import service.CombattimentoService;
import service.PersonaggioService;



/**
 * Versione completa + robusta di CombattimentoServiceImpl. - Risolve il
 * PersonaggioService corretto in base al tipo (Guerriero/Mago/Arciere/Paladino)
 * - Gestisce correttamente l'Evento del combattimento (chiusura e rimozione
 * opzionale) - Evita new sparsi e bug su remove() di Mostro dentro lista Eventi
 * - Tiene traccia di turni e danni inflitti - Supporta scelta azione reale per
 * il turno del personaggio (attacco / oggetto / annulla)
 */

public class CombattimentoServiceImpl implements CombattimentoService {

    private final MostroServiceImpl mostroService = new MostroServiceImpl();
    private PersonaggioService personaggioService; // opzionale: injection
 TurnoServiceImpl turnoService = new TurnoServiceImpl();
    private static final Random RNG = new Random();
    private static final Scanner scanner = new Scanner(System.in);


    /* =======================
       GETTER da interfaccia
       ======================= */
    @Override
    public Object getVincitore(Combattimento combattimento) {
        return combattimento == null ? null : combattimento.getVincitore();
    }//stessa cosa di mostro

    @Override
    public boolean èInCorso(Combattimento combattimento) {
        return combattimento != null && combattimento.isInCorso();
    }

    /* =======================
       INIZIA COMBATTIMENTO
       ======================= */
//inizio classe
    @Override
    public Object iniziaCombattimento(Personaggio personaggio, Mostro mostro, Stanza stanza) {
        if (personaggio == null || mostro == null) {
            System.out.println("Combattimento non valido.");
            return false;
        }

        System.out.println("\nInizia il combattimento: " + personaggio.getNomePersonaggio() + " VS " + mostro.getNomeMostro());

        Evento evento = new Evento(
                0, true, false,
                "Incontro con " + mostro.getNomeMostro(),
                "Combattimento_" + mostro.getNomeMostro(),
                stanza
        );

        // Aggancio l'evento alla stanza (così l'arciere può "percepirlo" nelle stanze adiacenti)
        if (stanza != null && stanza.getListaEventiAttivi() != null) {
            stanza.getListaEventiAttivi().add(evento);
        }

        Combattimento combattimento = new Combattimento(
                null, 0, evento, 0, true, personaggio, stanza, 0, null, mostro
        );
        combattimento.setInCorso(true);

        // Iniziativa: 0 = mostro, 1 = personaggio
        Integer iniziativa = combattimento.getIniziativa();
        if (iniziativa == null) {
            iniziativa = RNG.nextInt(2);
            combattimento.setIniziativa(iniziativa);
            System.out.println("Iniziativa: " + (iniziativa == 0 ? mostro.getNomeMostro() : personaggio.getNomePersonaggio()) + " inizia per primo.");
        }

        // Loop 1 vs 1
        while (combattimento.isInCorso()) {

            if (iniziativa == 0) {
                applicaECalcolaDanno(combattimento, mostro);
            } else {
                // Turno giocatore: qui la scelta influisce davvero
                Zaino zaino = (personaggio != null) ? personaggio.getZaino() : null; // se esiste getZaino()
                 scegliAzioneCombattimentoInterna(combattimento,personaggio, zaino);

                /*if (azione == Azione.ATTACCA) {
                    applicaECalcolaDanno(combattimento, personaggio);
                } else if (azione == Azione.USA_OGGETTO) {
                    boolean usato= TurnoServiceImpl.gestisciUsoOggettoDaZaino(personaggio, scanner);
                }*/
            }

            // se qualcuno ha vinto, chiudi e esci
            if (!combattimento.isInCorso()) {
                break;
            }

            // alterna turno
            iniziativa = 1 - iniziativa;
        }

        return combattimento.getVincitore();
    }

    /* =======================
       SCELTA AZIONE
       ======================= */
    @Override  
    public void scegliAzioneCombattimento(Combattimento combattimento, Personaggio personaggio, Zaino zaino) {
        // Mantengo la firma dell'interfaccia.
        // Internamente uso una versione che ritorna l'azione, ma qui la ignoro.
        scegliAzioneCombattimentoInterna(combattimento,personaggio, zaino);
    } //serve a gestire il turno del personaggio durante il combattimento, 
    //chiedendo al giocatore che azione vuole compiere e traducendo la scelta in qualcosa che il sistema possa usare

    public void scegliAzioneCombattimentoInterna(Combattimento combattimento, Personaggio personaggio, Zaino zaino) {
        if (personaggio == null||combattimento==null) {
            return ;
        }

        System.out.println("\nScegli azione in combattimento per " + personaggio.getNomePersonaggio());
        System.out.println("1) Attacca");
        System.out.println("2) Usa un oggetto");
        
        System.out.print("> ");

        int scelta = leggiIntero();

        if(scelta==1) {
                System.out.println("Hai scelto di attaccare.");
                applicaECalcolaDanno(combattimento, scanner);
            }
        else if (scelta==2) {
               
                 turnoService.gestisciUsoOggettoDaZaino(personaggio, scanner);
                
            }
        return;
        }

      

        

    private int leggiIntero() {
        String line = scanner.nextLine().trim();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    ///è implementato in personaggio e l'ho mdificato in modo che lo richiami alla fine
   /*  private boolean provaUsaOggetto(Personaggio personaggio, Zaino zaino) {
        if (personaggio == null || zaino == null || zaino.getListaOggetti().isEmpty()) {
            System.out.println("Zaino vuoto.");
            return false;
        }

        List<Oggetto> utilizzabili = new ArrayList<>();
        for (Oggetto o : zaino.getListaOggetti()) {
            if (o != null && o.isUsabile()) {
                utilizzabili.add(o);
            }
        }

        if (utilizzabili.isEmpty()) {
            System.out.println("Nessun oggetto utilizzabile.");
            return false;
        }

        System.out.println("\nOggetti utilizzabili:");
        for (int i = 0; i < utilizzabili.size(); i++) {
            System.out.println((i + 1) + ") " + utilizzabili.get(i).getNome());
        }
        System.out.println("0) Annulla");

        int scelta = leggiIntero();
        if (scelta <= 0 || scelta > utilizzabili.size()) {
            System.out.println("Annullato.");
            return false;
        }

        Oggetto oggetto = utilizzabili.get(scelta - 1);

        boolean usato = personaggio.usaOggetto(personaggio, oggetto);

        if (!usato) {
            System.out.println("Impossibile usare l'oggetto.");
        }

        return usato;
    }*/


    /* =======================
       TERMINA COMBATTIMENTO
       ======================= */
    ///controllare con chiudi combattimento --> li ho uniti
    @Override
    public boolean terminaCombattimento(Combattimento combattimento, Object vincitore, String messaggio) {
        if (combattimento == null || !combattimento.isInCorso()) {
            return false;
        }

        combattimento.setVincitore(vincitore);
        combattimento.setInCorso(false);

        if (messaggio != null && !messaggio.isBlank()) {
            System.out.println(messaggio);
        }

        chiudiEventoMostro(combattimento);
        return true;
    }

    private void chiudiEventoMostro(Combattimento combattimento) {
        if (combattimento == null) {
            return;
        } //serve perché l’evento mostro non risulti più attivo in stanza

        Evento evento = combattimento.getEventoMostro();
        if (evento == null) {
            return;
        }

        // Chiudo l'evento
        evento.setFineEvento(true);
        evento.setInizioEvento(false);

        // Se non è riutilizzabile (nel tuo Evento ritorna false), lo rimuovo dalla stanza per "ripulire"
        Stanza stanza = combattimento.getStanza();
        if (stanza != null && stanza.getListaEventiAttivi() != null) {
            if (!evento.èRiutilizzabile()) {
                stanza.getListaEventiAttivi().remove(evento);
            }
        }
    }


    /* =======================
       CORE: applica danno
       ======================= */
    @Override
    public int applicaECalcolaDanno(Combattimento combattimento, Object attaccante) {
        if (combattimento == null || attaccante == null) {
            return 0;
        }

        Personaggio personaggio = combattimento.getPersonaggioCoinvolto();
        Mostro mostro = combattimento.getMostroCoinvolto();
        if (personaggio == null || mostro == null) {
            return 0;
        }

        int dannoApplicato = 0;

        // ===== TURNO MOSTRO =====
        if (attaccante == mostro) {
            int dannoBase = MostroServiceImpl.dannoBase(mostro, personaggio);
            dannoApplicato = mostroService.attaccoDelMostro(mostro, personaggio, dannoBase);

            aggiornaStatistiche(combattimento, dannoApplicato);

            if (personaggio.getPuntiVita() <= 0) {
                terminaCombattimento(combattimento, mostro,
                        personaggio.getNomePersonaggio() + " è stato sconfitto da " + mostro.getNomeMostro());
            }
            return dannoApplicato;
        }

        // ===== TURNO PERSONAGGIO =====
        PersonaggioService ps = resolveService(personaggio);
        dannoApplicato = ps.attacca(personaggio, mostro, combattimento);

        aggiornaStatistiche(combattimento, dannoApplicato);

        if (mostro.getPuntiVitaMostro() <= 0) {
            terminaCombattimento(combattimento, personaggio,
                    mostro.getNomeMostro() + " è stato sconfitto da " + personaggio.getNomePersonaggio());
            // XP qui o nei service dei personaggi: qui lo lascio minimale
            try {
                personaggio.setEsperienza(personaggio.getEsperienza() + 10);
            } catch (Exception ignored) {
            }
        }

        return dannoApplicato;
    }

    private void aggiornaStatistiche(Combattimento c, int danno) {
        if (c == null) {
            return;
        }
        c.setDanniInflittiCombattimento(c.getDanniInflittiCombattimento() + Math.max(0, danno));
        c.setTurnoCorrenteCombattimento(c.getTurnoCorrenteCombattimento() + 1);
    }

    /* =======================
       Risoluzione service personaggio
       ======================= */
    ///???
    private PersonaggioService resolveService(Personaggio personaggio) {
        if (this.personaggioService != null) {
            return this.personaggioService;
        }

        if (personaggio instanceof domain.Guerriero) {
            return new GuerrieroServiceImpl();
        }
        if (personaggio instanceof domain.Arciere) {
            return new ArciereServiceImpl();
        }
        if (personaggio instanceof domain.Mago) {
            return new MagoServiceImpl();
        }
        if (personaggio instanceof domain.Paladino) {
            return new PaladinoServiceImpl();
        }

        return new GuerrieroServiceImpl(); // fallback
    }
}
