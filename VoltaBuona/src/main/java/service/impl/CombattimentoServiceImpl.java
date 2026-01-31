package service.impl;

import java.util.Random;
import java.util.Scanner;

import domain.Combattimento;
import domain.Evento;
import domain.Mago;
import domain.Mostro;
import domain.Paladino;
import domain.Personaggio;
import domain.Stanza;
import domain.Zaino;
import service.CombattimentoService;
import service.PersonaggioService;

public class CombattimentoServiceImpl implements CombattimentoService {

    private MostroServiceImpl mostroService;
    private PersonaggioService personaggioService; // opzionale: injection
    TurnoServiceImpl turnoService;
    private static final Random RNG = new Random();
    private static final Scanner scanner = new Scanner(System.in);
    private static int difficoltaMostro = 0;

    public CombattimentoServiceImpl(MostroServiceImpl mostroService, PersonaggioService personaggioService, TurnoServiceImpl turnoService) {
        this.mostroService = mostroService;
        this.personaggioService = personaggioService;
        this.turnoService = turnoService;
    }

    /* =======================
       GETTER da interfaccia
       ======================= */
    @Override
    public Object getVincitore(Combattimento combattimento) {
        return combattimento.getVincitore();
    }//stessa cosa di mostro

    @Override
    public boolean èInCorso(Combattimento combattimento) {
        return combattimento.isInCorso();
    }

    public int getDifficoltaMostro() {
        return difficoltaMostro;
    }
    public void setDifficoltaMostro(int difficoltaMostro) {
        this.difficoltaMostro = difficoltaMostro;
    }

    /* =======================
       crea e inizia combattimento
       ======================= */
//inizio classe
    @Override
    public Object iniziaCombattimento(Personaggio personaggio, Mostro mostro, Stanza stanza) {
        if (personaggio == null || mostro == null) {
            System.out.println("Combattimento non valido.");
            return false;
        }

        System.out.println("\nInizia il combattimento: " + personaggio.getNomePersonaggio() + " VS " + mostro.getNomeMostro());
        // stampa XP del personaggio prima del combattimento
        try {
            System.out.println("Esperienza prima combattimento (" + personaggio.getNomePersonaggio() + "): " + personaggio.getEsperienza());
        } catch (Exception ignored) {
        }

        // se il Mostro è un Evento (tipico quando è stato creato come evento in stanza),
        // riusalo invece di crearne uno nuovo per evitare mismatch di id.
        Evento evento;
        if (mostro instanceof Evento) {
            evento = (Evento) mostro;
            // assicurati che la posizione sia impostata
            if (evento.getPosizioneCorrente() == null && stanza != null) {
                evento.setPosizioneCorrente(stanza);
            }
            // se non è già presente nella lista eventi della stanza, aggiungilo
            if (stanza != null && stanza.getListaEventiAttivi() != null && !stanza.getListaEventiAttivi().contains(evento)) {
                stanza.getListaEventiAttivi().add(evento);
            }
        } else {
            evento = new Evento(
                    0, true, false,
                    "Incontro con " + mostro.getNomeMostro(),
                    "Combattimento_" + mostro.getNomeMostro(),
                    stanza
            );
            if (stanza != null && stanza.getListaEventiAttivi() != null) {
                stanza.getListaEventiAttivi().add(evento);
                evento.setPosizioneCorrente(stanza);
            }
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
                Zaino zaino = personaggio.getZaino(); // se esiste getZaino()
                scegliAzioneCombattimentoInterna(combattimento, personaggio, zaino);
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
        scegliAzioneCombattimentoInterna(combattimento, personaggio, zaino);
    } //serve a gestire il turno del personaggio durante il combattimento, 
    //chiedendo al giocatore che azione vuole compiere e traducendo la scelta in qualcosa che il sistema possa usare

    public void scegliAzioneCombattimentoInterna(Combattimento combattimento, Personaggio personaggio, Zaino zaino) {
        if (personaggio == null || combattimento == null) {
            return;
        }

        System.out.println("\nScegli azione in combattimento per " + personaggio.getNomePersonaggio());
        System.out.println("1) Attacca");
        System.out.println("2) Usa un oggetto");

        // System.out.print("> ");
        int scelta;
        {
            String line = scanner.nextLine().trim();
            try {
                scelta = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                scelta = -1;
            }
        }

        if (scelta == 1) {
            System.out.println("Hai scelto di attaccare.");

            // Se il personaggio è un Mago, chiedi quale magia usare
            if (personaggio instanceof Mago mago) {
                System.out.println("Scegli la magia da lanciare:");
                Mago.TipoMagiaSacra[] magie = Mago.TipoMagiaSacra.values();
                for (int i = 0; i < magie.length; i++) {
                    System.out.println((i + 1) + ") " + magie[i] + " (costo mana: " + magie[i].getCostoMana() + ")");
                }

                String line = scanner.nextLine().trim();
                int sceltaMagia;
                try {
                    sceltaMagia = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    System.out.println("Scelta non valida. Attacco annullato.");
                    return;
                }

                if (sceltaMagia == 0) {
                    System.out.println("Attacco annullato.");
                    return;
                }

                if (sceltaMagia < 1 || sceltaMagia > magie.length) {
                    System.out.println("Scelta magia non valida. Attacco annullato.");
                    return;
                }

                mago.setMagiaSelezionata(magie[sceltaMagia - 1]);
            }

            // Se il personaggio è un Paladino, chiedi se attacco fisico o colpo sacro
            if (personaggio instanceof Paladino paladino) {
                System.out.println("Scegli come attaccare:");
                System.out.println("1) Attacco fisico");
                System.out.println("2) Colpo sacro");

                String lineP = scanner.nextLine().trim();
                int sceltaStile;
                try {
                    sceltaStile = Integer.parseInt(lineP);
                } catch (NumberFormatException e) {
                    System.out.println("Scelta non valida. Attacco fisico di default.");
                    sceltaStile = 1;
                }

                if (sceltaStile == 2) {
                    System.out.println("Scegli il colpo sacro:");
                    Paladino.TipoMagiaSacra[] magie = Paladino.TipoMagiaSacra.values();
                    for (int i = 0; i < magie.length; i++) {
                        System.out.println((i + 1) + ") " + magie[i] + " (costo mana: " + magie[i].getCostoMana() + ")");
                    }

                    String line = scanner.nextLine().trim();
                    int sceltaMagia;
                    try {
                        sceltaMagia = Integer.parseInt(line);
                    } catch (NumberFormatException e) {
                        System.out.println("Scelta non valida. Attacco annullato.");
                        return;
                    }

                    if (sceltaMagia == 0) {
                        System.out.println("Attacco annullato.");
                        return;
                    }

                    if (sceltaMagia < 1 || sceltaMagia > magie.length) {
                        System.out.println("Scelta magia non valida. Attacco annullato.");
                        return;
                    }

                    paladino.setMagiaSelezionata(magie[sceltaMagia - 1]);
                } else {
                    // stile fisico: nessuna magia selezionata
                    paladino.setMagiaSelezionata(null);
                }

                
            }

            applicaECalcolaDanno(combattimento, personaggio);
        } else if (scelta == 2) {

            turnoService.gestisciUsoOggettoDaZaino(personaggio, scanner);

        }
        return;
    }

    /* =======================
       TERMINA COMBATTIMENTO
       ======================= */
    //controllare con chiudi combattimento --> li ho uniti
    @Override
    public boolean terminaCombattimento(Combattimento combattimento, Object vincitore) {
        if (combattimento == null || !combattimento.isInCorso()) {
            return false;
        }

        combattimento.setVincitore(vincitore);
        combattimento.setInCorso(false);
        Mostro mostro = combattimento.getMostroCoinvolto();
        Evento evento = combattimento.getEventoMostro();
        if (evento != null) {
            evento.setFineEvento(true);
            evento.setInizioEvento(false);
            System.out.println("[DEBUG] HA VINTOOOO!! IL VINCITORE è:" + vincitore);
        }

        return true;
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

        int danno = 0;

        if (attaccante instanceof Mostro) {
            danno = mostroService.attaccoDelMostro(mostro, personaggio);
            System.out.println("[DEBUG] Attacco mostro -> " + mostro.getNomeMostro() + " danno=" + danno);
            if (personaggio.èMorto(personaggio)) {
                mostro.aggiungiEsperienzaMostro();
                terminaCombattimento(combattimento, mostro);
            }
            return danno;
        }

        if (attaccante instanceof Personaggio) {
            Personaggio personaggioAttaccante = (Personaggio) attaccante;
            PersonaggioService personaggioService = getServicePerPersonaggio(personaggioAttaccante);
            danno = personaggioService.attacca(personaggioAttaccante, mostro, combattimento);

            System.out.println("[DEBUG] Attacco player -> " + personaggioAttaccante.getNomePersonaggio() + " inflitti=" + danno);

            if (mostro.èMortoilMostro()) {
                System.out.println("[DEBUG] Mostro ucciso: id=" + mostro.getId() + " nome=" + mostro.getNomeMostro());
                System.out.println("[DEBUG] difficoltaMostro prima = " + difficoltaMostro);
                difficoltaMostro++;
              //  CombattimentoServiceImpl.setDifficoltaMostro(difficoltaMostro);
                System.out.println("[DEBUG] difficoltaMostro dopo = " + difficoltaMostro);

                Evento evento = combattimento.getEventoMostro();
                Stanza stanza = combattimento.getStanza();

                if (stanza != null) {
                    System.out.println("[DEBUG] Aggiorno mostri in stanza id=" + stanza.getId());
                    for (Evento ev : new java.util.ArrayList<>(stanza.getListaEventiAttivi())) {
                        if (ev instanceof Mostro m && !m.èMortoilMostro() && m.getId() != mostro.getId()) {
                            int primaPv = m.getPuntiVitaMostro();
                            int primaDif = m.getDifesaMostro();
                            int primaDanno = (m.getTipoAttaccoMostro() != null) ? m.getTipoAttaccoMostro().getDannoTipoMostro() : 0;
                            int primaExp = m.getEsperienza();

                            System.out.println("[DEBUG] Prima update -> id=" + m.getId() + " nome=" + m.getNomeMostro()
                                    + " pv=" + primaPv + " dif=" + primaDif + " danno=" + primaDanno + " exp=" + primaExp);

                            try {
                                mostroService.applicaDifficoltaMostro(m);
                            } catch (Exception ex) {
                                System.out.println("[DEBUG] Errore applicaDifficoltaMostro su mostro id=" + m.getId() + ": " + ex.getMessage());
                            }

                            int dopoPv = m.getPuntiVitaMostro();
                            int dopoDif = m.getDifesaMostro();
                            int dopoDanno = (m.getTipoAttaccoMostro() != null) ? m.getTipoAttaccoMostro().getDannoTipoMostro() : 0;
                            int dopoExp = m.getEsperienza();

                            System.out.println("[DEBUG] Dopo update -> id=" + m.getId() + " nome=" + m.getNomeMostro()
                                    + " pv=" + dopoPv + " dif=" + dopoDif + " danno=" + dopoDanno + " exp=" + dopoExp);
                        }
                    }
                } else {
                    System.out.println("[DEBUG] Stanza null: non posso aggiornare i mostri in stanza.");
                }

                if (stanza != null && evento != null) {
                    stanza.rimuoviEvento(evento);
                }

                personaggioAttaccante.aggiungiEsperienza();
                System.out.println("[DEBUG] Player XP dopo vittoria: " + personaggioAttaccante.getEsperienza()
                        + " livello:" + personaggioAttaccante.getLivello());

                terminaCombattimento(combattimento, personaggioAttaccante);
                aggiornaStatistiche(combattimento, danno);
            }
            return danno;
        }

        return 0;
    }

    // aggiornare personaggi e mostri vedere in seguito
    public void aggiornaStatistiche(Combattimento c, int danno) {
        if (c == null) {
            return;
        }
        c.setDanniInflittiCombattimento(c.getDanniInflittiCombattimento() + danno);
        c.setTurnoCorrenteCombattimento(c.getTurnoCorrenteCombattimento() + 1);
    }

    /* =======================
       Risoluzione service personaggio
       ======================= */
    //ritorna il service corretto in base al tipo di personaggio
    private PersonaggioService getServicePerPersonaggio(Personaggio personaggio) {
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
