

/*import java.util.List;

import domain.Combattimento;
import domain.Mostro;
import domain.Personaggio;
import domain.Stanza;
import domain.Zaino;
import service.CombattimentoService;
import service.PersonaggioService;

public class CombattimentoServiceImpl implements CombattimentoService {

    private MostroServiceImpl mostroService = new MostroServiceImpl();
    private PersonaggioService personaggioService;

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

        // Caso: attacca il mostro -> bersaglio = personaggio
        if (attaccante == mostro) {
            int dannoBase = MostroServiceImpl.dannoBase(mostro, personaggio);
            int dannoApplicato = new MostroServiceImpl().attaccoDelMostro(mostro, personaggio, dannoBase);

            if (personaggio.getPuntiVita() <= 0) {
                combattimento.setVincitore(mostro);
                combattimento.setInCorso(false);
                System.out.println(personaggio.getNomePersonaggio() + " è stato sconfitto da " + mostro.getNomeMostro());
            }
            return dannoApplicato;
        }

        // Caso: attacca il personaggio -> bersaglio = mostro
        PersonaggioService ps = this.personaggioService;

        // Se non è stato iniettato un service, risolvo il corretto fallback
        if (ps == null) {
            if (personaggio instanceof domain.Guerriero) {
                ps = new GuerrieroServiceImpl();
            } else if (personaggio instanceof domain.Arciere) {
                ps = new ArciereServiceImpl();
            } else if (personaggio instanceof domain.Mago) {
                ps = new MagoServiceImpl();
            } else if (personaggio instanceof domain.Paladino) {
                ps = new PaladinoServiceImpl();
            } else {
                ps = new GuerrieroServiceImpl();
            }
        }

        int dannoApplicato = ps.attacca(personaggio, mostro, combattimento);

        if (mostro.getPuntiVitaMostro() <= 0) {
            combattimento.setVincitore(personaggio);
            combattimento.setInCorso(false);
            System.out.println(mostro.getNomeMostro() + " è stato sconfitto da " + personaggio.getNomePersonaggio());
            try {
                personaggio.setEsperienza(personaggio.getEsperienza() + 10);
            } catch (Exception ignored) {
            }
        }
        return dannoApplicato;
    }

    @Override
    public Mostro getMostro(Combattimento combattimento) {

        return combattimento.getMostroCoinvolto();
    }

    @Override
    public Personaggio getPersonaggio(Combattimento combattimento) {

        return combattimento.getPersonaggioCoinvolto();
    }

    @Override
    public Object getVincitore(Combattimento combattimento) {
        if (combattimento == null) {
            return null;
        }
        Object vincitore = new Object();
        if (combattimento.getMostroCoinvolto().getPuntiVitaMostro() <= 0) {
            return vincitore = combattimento.getPersonaggioCoinvolto();
        } else if (combattimento.getPersonaggioCoinvolto().getPuntiVita() <= 0) {
            return vincitore = combattimento.getMostroCoinvolto();
        }
        return vincitore;
    }

    @Override
    public void scegliAzioneCombattimento(Personaggio personaggio, Zaino zaino) {
        if (personaggio == null) {
            return;
        }
        java.util.Scanner sc = new java.util.Scanner(System.in);
        System.out.println("\nScegli azione in combattimento per " + personaggio.getNomePersonaggio());
        System.out.println("1) Attacca");
        System.out.println("2) Usa un oggetto dallo zaino");
        System.out.println("0) Annulla");

        String line = sc.nextLine().trim();
        int scelta;
        try {
            scelta = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("Scelta non valida.");
            return;
        }

        switch (scelta) {
            case 1:
                System.out.println("Hai scelto di attaccare.");
                break;
            case 2:
                if (zaino == null || zaino.getListaOggetti().isEmpty()) {
                    System.out.println("Zaino vuoto.");
                    break;
                }
                List<domain.Oggetto> utilizzabili = new java.util.ArrayList<>();
                for (domain.Oggetto o : zaino.getListaOggetti()) {
                    if (o != null && o.isUsabile()) {
                        utilizzabili.add(o);
                    }
                }
                if (utilizzabili.isEmpty()) {
                    System.out.println("Nessun oggetto utilizzabile nello zaino.");
                    break;
                }
                System.out.println("\nOggetti utilizzabili:");
                for (int i = 0; i < utilizzabili.size(); i++) {
                    System.out.println((i + 1) + ") " + utilizzabili.get(i).getNome());
                }
                System.out.println("0) Annulla");
                System.out.print("Scegli un oggetto da usare: ");
                int idx;
                try {
                    idx = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException ex) {
                    System.out.println("Input non valido.");
                    break;
                }
                if (idx == 0) {
                    System.out.println("Hai annullato.");
                    break;
                }
                if (idx < 1 || idx > utilizzabili.size()) {
                    System.out.println("Scelta non valida.");
                    break;
                }
                domain.Oggetto oggetto = utilizzabili.get(idx - 1);
                try {
                    boolean applicato = oggetto.eseguiEffetto(personaggio);
                    if (applicato) {
                        // rimuovi l'oggetto dallo zaino se consumabile
                        zaino.rimuoviOggettoDaZaino(oggetto);
                        System.out.println("Hai usato: " + oggetto.getNome());
                    } else {
                        System.out.println("Impossibile usare l'oggetto.");
                    }
                } catch (Exception ex) {
                    System.out.println("Errore nell'uso dell'oggetto: " + ex.getMessage());
                }
                break;
            case 0:
            default:
                System.out.println("Azione annullata.");
        }
    }

    @Override
    public boolean terminaCombattimento(Combattimento combattimento) {
        if (combattimento == null) {
            return false;
        }
        if (!combattimento.isInCorso()) {
            return false;
        }
        combattimento.setInCorso(false);
        // eventuale pulizia: rimuovi mostro dalla stanza se morto
        Stanza s = combattimento.getStanza();
        if (s != null && combattimento.getMostroCoinvolto() != null && combattimento.getMostroCoinvolto().èMortoilMostro() == true) {
            if (s.getListaEventiAttivi() != null) {
                s.getListaEventiAttivi().remove(combattimento.getMostroCoinvolto());
            }
        }
        return true;
    }

    @Override
    public boolean èInCorso(Combattimento combattimento) {
        return combattimento != null && combattimento.isInCorso();
    }

    @Override
    public Object iniziaCombattimento(Personaggio personaggio, Mostro mostro, Stanza stanza) {

        if (personaggio == null || mostro == null) {
            System.out.println("Combattimento non valido.");
            return false;
        }

        System.out.println("\nInizia il combattimento: " + personaggio.getNomePersonaggio() + " VS " + mostro.getNomeMostro());

        domain.Evento evento = new domain.Evento(0, true, false,
                "Incontro con " + mostro.getNomeMostro(),
                "Combattimento_" + mostro.getNomeMostro(), stanza);
        Combattimento combattimento = new Combattimento(null, 0, evento, 0, true, personaggio, stanza, 0, null, mostro);
        combattimento.setInCorso(true);

        // iniziativa: 0 = mostro, 1 = personaggio (memorizzata nel Combattimento)
        Integer iniziativa = combattimento.getIniziativa();
        if (iniziativa == null) {
            iniziativa = new java.util.Random().nextInt(2);
            combattimento.setIniziativa(iniziativa);
            System.out.println(" Iniziativa: " + (iniziativa == 0 ? mostro.getNomeMostro() : personaggio.getNomePersonaggio()) + " inizia per primo.");
        }
        // loop alternato 1vs1 finché uno non muore
        while (combattimento.isInCorso()) {

            if (iniziativa == 0) {
                applicaECalcolaDanno(combattimento, mostro);
            } else {
                applicaECalcolaDanno(combattimento, personaggio);
            }

            // controlla vittoria
            if (mostro.getPuntiVitaMostro() <= 0) {
                combattimento.setVincitore(personaggio);
                combattimento.setInCorso(false);
                System.out.println(personaggio.getNomePersonaggio() + " ha vinto!");
                if (stanza != null && stanza.getListaEventiAttivi() != null) {
                    stanza.getListaEventiAttivi().remove(mostro);
                }
                break;
            }
            if (personaggio.getPuntiVita() <= 0) {
                combattimento.setVincitore(mostro);
                combattimento.setInCorso(false);
                //System.out.println( personaggio.getNomePersonaggio() + " è stato sconfitto...");
                break;
            }

            // alterna turno
            iniziativa = 1 - iniziativa;
        }

        return combattimento.getVincitore();
    }

}*/

package service.impl;

import java.util.ArrayList;
import java.util.List;
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

import service.CombattimentoService;

/**
 * Versione completa + robusta di CombattimentoServiceImpl.
 * - Risolve il PersonaggioService corretto in base al tipo (Guerriero/Mago/Arciere/Paladino)
 * - Gestisce correttamente l'Evento del combattimento (chiusura e rimozione opzionale)
 * - Evita new sparsi e bug su remove() di Mostro dentro lista Eventi
 * - Tiene traccia di turni e danni inflitti
 * - Supporta scelta azione reale per il turno del personaggio (attacco / oggetto / annulla)
 */
public class CombattimentoServiceImpl implements CombattimentoService {

    private final MostroServiceImpl mostroService = new MostroServiceImpl();
    private PersonaggioService personaggioService; // opzionale: injection

    private static final Random RNG = new Random();
    private static final Scanner SC = new Scanner(System.in);
  // lo prendiamo dal'enumerazione interna
    private enum AzioneCombattimento { ATTACCA, USA_OGGETTO, ANNULLA }

    /* =======================
       CORE: applica danno
       ======================= */

    @Override
    public int applicaECalcolaDanno(Combattimento combattimento, Object attaccante) {
        if (combattimento == null || attaccante == null) return 0;

        Personaggio personaggio = combattimento.getPersonaggioCoinvolto();
        Mostro mostro = combattimento.getMostroCoinvolto();
        if (personaggio == null || mostro == null) return 0;

        int dannoApplicato = 0;

        // ===== TURNO MOSTRO =====
        if (attaccante == mostro) {
            int dannoBase = MostroServiceImpl.dannoBase(mostro, personaggio);
            dannoApplicato = mostroService.attaccoDelMostro(mostro, personaggio, dannoBase);

            aggiornaStatistiche(combattimento, dannoApplicato);

            if (personaggio.getPuntiVita() <= 0) {
                chiudiCombattimento(combattimento, mostro,
                        personaggio.getNomePersonaggio() + " è stato sconfitto da " + mostro.getNomeMostro());
            }
            return dannoApplicato;
        }

        // ===== TURNO PERSONAGGIO =====
        PersonaggioService ps = resolveService(personaggio);
        dannoApplicato = ps.attacca(personaggio, mostro, combattimento);

        aggiornaStatistiche(combattimento, dannoApplicato);

        if (mostro.getPuntiVitaMostro() <= 0) {
            chiudiCombattimento(combattimento, personaggio,
                    mostro.getNomeMostro() + " è stato sconfitto da " + personaggio.getNomePersonaggio());
            // XP qui o nei service dei personaggi: qui lo lascio minimale
            try {
                personaggio.setEsperienza(personaggio.getEsperienza() + 10);
            } catch (Exception ignored) { }
        }

        return dannoApplicato;
    }

    private void aggiornaStatistiche(Combattimento c, int danno) {
        if (c == null) return;
        c.setDanniInflittiCombattimento(c.getDanniInflittiCombattimento() + Math.max(0, danno));
        c.setTurnoCorrenteCombattimento(c.getTurnoCorrenteCombattimento() + 1);
    }

    private void chiudiCombattimento(Combattimento c, Object vincitore, String messaggio) {
        if (c == null) return;
        c.setVincitore(vincitore);
        c.setInCorso(false);
        if (messaggio != null && !messaggio.isBlank()) {
            System.out.println(messaggio);
        }
        chiudiEventoMostro(c);
    }

    /* =======================
       GETTER da interfaccia
       ======================= */
//???
    @Override
    public Mostro getMostro(Combattimento combattimento) {
        return combattimento == null ? null : combattimento.getMostroCoinvolto();
    }
//???
    @Override
    public Personaggio getPersonaggio(Combattimento combattimento) {
        return combattimento == null ? null : combattimento.getPersonaggioCoinvolto();
    }
///???
    @Override
    public Object getVincitore(Combattimento combattimento) {
        return combattimento == null ? null : combattimento.getVincitore();
    }

    @Override
    public boolean èInCorso(Combattimento combattimento) {
        return combattimento != null && combattimento.isInCorso();
    }

    /* =======================
       SCELTA AZIONE (interfaccia)
       ======================= */

    @Override
    public void scegliAzioneCombattimento(Personaggio personaggio, Zaino zaino) {
        // Mantengo la firma dell'interfaccia.
        // Internamente uso una versione che ritorna l'azione, ma qui la ignoro.
        scegliAzioneCombattimentoInterna(personaggio, zaino);
    }

    private AzioneCombattimento scegliAzioneCombattimentoInterna(Personaggio personaggio, Zaino zaino) {
        if (personaggio == null) return AzioneCombattimento.ANNULLA;

        System.out.println("\nScegli azione in combattimento per " + personaggio.getNomePersonaggio());
        System.out.println("1) Attacca");
        System.out.println("2) Usa un oggetto dallo zaino");
        System.out.println("0) Annulla");
        System.out.print("> ");

        int scelta = leggiIntero();

        switch (scelta) {
            case 1:
                System.out.println("Hai scelto di attaccare.");
                return AzioneCombattimento.ATTACCA;

            case 2:
                boolean usato = provaUsaOggetto(personaggio, zaino);
                // Se ha usato un oggetto, consideriamo consumato il turno.
                return usato ? AzioneCombattimento.USA_OGGETTO : AzioneCombattimento.ANNULLA;

            case 0:
            default:
                System.out.println("Azione annullata.");
                return AzioneCombattimento.ANNULLA;
        }
    }
  ///vedere dove è implementato
    private boolean provaUsaOggetto(Personaggio personaggio, Zaino zaino) {
        if (zaino == null || zaino.getListaOggetti() == null || zaino.getListaOggetti().isEmpty()) {
            System.out.println("Zaino vuoto.");
            return false;
        }

        List<domain.Oggetto> utilizzabili = new ArrayList<>();
        for (domain.Oggetto o : zaino.getListaOggetti()) {
            if (o != null && o.isUsabile()) utilizzabili.add(o);
        }

        if (utilizzabili.isEmpty()) {
            System.out.println("Nessun oggetto utilizzabile nello zaino.");
            return false;
        }

        System.out.println("\nOggetti utilizzabili:");
        for (int i = 0; i < utilizzabili.size(); i++) {
            System.out.println((i + 1) + ") " + utilizzabili.get(i).getNome());
        }
        System.out.println("0) Annulla");
        System.out.print("Scegli un oggetto da usare: ");

        int idx = leggiIntero();
        if (idx == 0) {
            System.out.println("Hai annullato.");
            return false;
        }
        if (idx < 1 || idx > utilizzabili.size()) {
            System.out.println("Scelta non valida.");
            return false;
        }

        domain.Oggetto oggetto = utilizzabili.get(idx - 1);

        try {
            boolean applicato = oggetto.eseguiEffetto(personaggio);
            if (applicato) {
                zaino.rimuoviOggettoDaZaino(oggetto);
                System.out.println("Hai usato: " + oggetto.getNome());
                return true;
            } else {
                System.out.println("Impossibile usare l'oggetto.");
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Errore nell'uso dell'oggetto: " + ex.getMessage());
            return false;
        }
    }

    private int leggiIntero() {
        String line = SC.nextLine().trim();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /* =======================
       TERMINA COMBATTIMENTO
       ======================= */
///controllare con chiudi combattimento
    @Override
    public boolean terminaCombattimento(Combattimento combattimento) {
        if (combattimento == null || !combattimento.isInCorso()) return false;

        combattimento.setInCorso(false);
        chiudiEventoMostro(combattimento);
        return true;
    }

    private void chiudiEventoMostro(Combattimento combattimento) {
        if (combattimento == null) return;

        Evento evento = combattimento.getEventoMostro();
        if (evento == null) return;

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
                AzioneCombattimento azione = scegliAzioneCombattimentoInterna(personaggio, zaino);

                if (azione == AzioneCombattimento.ATTACCA) {
                    applicaECalcolaDanno(combattimento, personaggio);
                } else if (azione == AzioneCombattimento.USA_OGGETTO) {
                    // oggetto usato: turno consumato, nessun attacco
                } else {
                    // annulla: turno consumato (o potresti riproporre il menu)
                }
            }

            // se qualcuno ha vinto, chiudi e esci
            if (!combattimento.isInCorso()) break;

            // alterna turno
            iniziativa = 1 - iniziativa;
        }

        return combattimento.getVincitore();
    }

    /* =======================
       Risoluzione service personaggio
       ======================= */
///???
    private PersonaggioService resolveService(Personaggio personaggio) {
        if (this.personaggioService != null) return this.personaggioService;

        if (personaggio instanceof domain.Guerriero) return new GuerrieroServiceImpl();
        if (personaggio instanceof domain.Arciere)   return new ArciereServiceImpl();
        if (personaggio instanceof domain.Mago)      return new MagoServiceImpl();
        if (personaggio instanceof domain.Paladino)  return new PaladinoServiceImpl();

        return new GuerrieroServiceImpl(); // fallback
    }
}
