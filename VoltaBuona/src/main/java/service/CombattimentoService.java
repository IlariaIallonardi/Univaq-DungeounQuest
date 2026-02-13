package service;

import java.util.Map;

import domain.Arciere;
import domain.Combattimento;
import domain.Evento;
import domain.Mago;
import domain.Mostro;
import domain.Paladino;
import domain.Personaggio;
import domain.Stanza;
import domain.Zaino;
import service.impl.ArciereServiceImpl;
import service.impl.GuerrieroServiceImpl;
import service.impl.MagoServiceImpl;
import service.impl.MostroServiceImpl;
import service.impl.PaladinoServiceImpl;
import service.impl.RandomSingleton;
import service.impl.ScannerSingleton;
import util.ANSI;

public class CombattimentoService {

    private MostroServiceImpl mostroService;
    private PersonaggioService personaggioService;
    TurnoService turnoService;
    private RandomSingleton randomGenerale = RandomSingleton.getInstance();
    private ScannerSingleton scannerGenerale = ScannerSingleton.getInstance();
    private static int difficoltaMostro = 0;
    private FileService fileService= FileService.getInstance();

    public CombattimentoService(MostroServiceImpl mostroService, PersonaggioService personaggioService, TurnoService turnoService) {
        this.mostroService = mostroService;
        this.personaggioService = personaggioService;
        this.turnoService = turnoService;
    }

    public Object getVincitore(Combattimento combattimento) {
        return combattimento.getVincitore();
    }

    public boolean èInCorso(Combattimento combattimento) {
        return combattimento.isInCorso();
    }

    public int getDifficoltaMostro() {
        return difficoltaMostro;
    }

    public void setDifficoltaMostro(int difficoltaMostro) {
        this.difficoltaMostro = difficoltaMostro;
    }

    public Object iniziaCombattimento(Personaggio personaggio, Mostro mostro, Stanza stanza) {
        if (personaggio == null) {
            throw new exception.DungeonException("Personaggio nullo passato a iniziaCombattimento.");
        }
        if (mostro == null) {
            throw new exception.DungeonException("Mostro nullo passato a iniziaCombattimento.");
        }

        System.out.println("\nInizia il combattimento: " + personaggio.getNomePersonaggio() + " VS " + mostro.getNomeMostro());

        Evento evento = null;
        if (mostro instanceof Evento) {
            evento = (Evento) mostro;

            evento = new Evento(0, true, false, "Incontro con " + mostro.getNomeMostro(),
                    "Combattimento_" + mostro.getNomeMostro(), stanza);
            if (stanza != null && stanza.getListaEventiAttivi() != null) {
                stanza.getListaEventiAttivi().add(evento);
                evento.setPosizioneCorrente(stanza);
            }
        }

        /**
         * Controlliamo se il personaggio e il mostro si trovano nella stessa
         * stanza, tranne nel caso dell'arciere che può fare un attacco nelle
         * stanze adiacenti.
         */
        Stanza posizionePersonaggio = personaggio.getPosizioneCorrente();
        Stanza posizioneMostro = mostro.getPosizioneCorrente();

        boolean stessaStanza;
        if (posizionePersonaggio != null && posizioneMostro != null) {
            stessaStanza = posizionePersonaggio == posizioneMostro;
        } else {
            stessaStanza = false;
        }

        //Se l'arciere si trova nella stessa stanza del mostro controlla normalmente
        //altrimenti usa il controllo su stanze adiacenti.
        boolean arciereAdiacente = false;

        if (!stessaStanza && posizionePersonaggio != null && posizioneMostro != null) {

            if (personaggio instanceof Arciere arciere) {
                Map<String, Stanza> stanzeAdiacenti = posizionePersonaggio.getStanzaAdiacente();
                if (stanzeAdiacenti != null && stanzeAdiacenti.containsValue(posizioneMostro)) {

                    arciereAdiacente = true;
                    return arciere;
                }
            }
        }

        if (!stessaStanza && !arciereAdiacente) {

            System.out.println("Impossibile iniziare il combattimento: il mostro non è nella stessa stanza. Solo l'Arciere può attaccare da stanza adiacente.");
            return false;
        }

        ///Inizia il vero e proprio combattimento
        Combattimento combattimento = new Combattimento(null, 0, evento, 0, true, personaggio, stanza, 0, null, mostro);
        combattimento.setInCorso(true);

        if (personaggio instanceof Arciere arciere) {
            if (arciereAdiacente) {
                ArciereServiceImpl arciereService = new ArciereServiceImpl();
                arciereService.attaccoDistanzaArciere(arciere, mostro);
                combattimento.setInCorso(false);
                return arciere;
            }
        }

        // Iniziativa: 0 = mostro, 1 = personaggio
        Integer iniziativa = combattimento.getIniziativa();
        if (iniziativa == null) {
            iniziativa = randomGenerale.prossimoNumero(0, 1);
            combattimento.setIniziativa(iniziativa);
            if (iniziativa == 0) {
                System.out.println("Iniziativa: " + mostro.getNomeMostro() + " inizia per primo.");
            } else {
                System.out.println("Iniziativa: " + personaggio.getNomePersonaggio() + " inizia per primo.");
            }

        }

        // Loop del combattimento 1vs 1
        while (combattimento.isInCorso()) {

            if (iniziativa == 0) {

                applicaECalcolaDanno(combattimento, mostro);
            } else {
                Zaino zaino = personaggio.getZaino();
                scegliAzioneCombattimento(combattimento, personaggio, zaino);
            }

            if (!combattimento.isInCorso()) {
                break;
            }

            // alterna turno
            iniziativa = 1 - iniziativa;
        }

        return combattimento.getVincitore();
    }

    public void scegliAzioneCombattimento(Combattimento combattimento, Personaggio personaggio, Zaino zaino) {

        if (personaggio == null || combattimento == null) {
            return;
        }

        System.out.println("\nScegli azione in combattimento per(scrivi il numero) " + personaggio.getNomePersonaggio());
        System.out.println("0) Annulla combattimento");
        System.out.println("1) Attacca");
        System.out.println("2) Usa un oggetto");

        int scelta;

        String nomePersonaggio = personaggio.getNomePersonaggio();
        boolean isBot = nomePersonaggio != null && (nomePersonaggio.startsWith("BOT_") || nomePersonaggio.startsWith("Bot-") || nomePersonaggio.toLowerCase().contains("bot"));
        if (isBot) {
            scelta = randomGenerale.prossimoNumero(0, 2);
            System.out.println(personaggio.getNomePersonaggio() + " (bot) sceglie: " + (scelta));
        } else {
            scelta = scannerGenerale.leggiIntero();
        }

     if (scelta == 0) {
    Stanza stanza = personaggio.getPosizioneCorrente();
    Evento evento = combattimento.getEventoMostro();

    System.out.println("Combattimento annullato.");

    if (evento != null) {
        stanza.rimuoviEvento(evento); 
    }

    combattimento.setInCorso(false);
    turnoService.terminaTurnoCorrente(personaggio);
    return;
}


        if (scelta == 1) {
            System.out.println("Hai scelto di attaccare.");
            System.out.println("0)Se premi zero annulli l'attacco e passi il turno. ");

            // Se sei un mago ti verrà chiesto quale magia vuoi usare 
            if (personaggio instanceof Mago mago) {
                System.out.println("Scegli la magia da lanciare:");
                Mago.TipoMagiaSacra[] magie = Mago.TipoMagiaSacra.values();
                for (int i = 0; i < magie.length; i++) {
                    System.out.println((i + 1) + ") " + magie[i] + " (costo mana: " + magie[i].getCostoMana() + ")");
                }

                int sceltaMagia;
                if (isBot) {
                    sceltaMagia = randomGenerale.prossimoNumero(0, magie.length) - 1;
                    System.out.println(personaggio.getNomePersonaggio() + " (bot) sceglie: " + (sceltaMagia + 1));
                } else {
                    sceltaMagia = scannerGenerale.leggiInteroIntervallo(0, magie.length) - 1;
                }

                if (scelta == 0) {
                    turnoService.terminaTurnoCorrente(personaggio);
                    combattimento.setInCorso(false);
                }
                if (sceltaMagia < 1 || sceltaMagia > magie.length) {
                    System.out.println("Scelta magia non valida. Attacco annullato.");
                    return;
                }

                mago.setMagiaSelezionata(magie[sceltaMagia - 1]);
            }

            // Se sei un Paladino ti verrà chiesto se vuoi fare un attacco fisico o magico.
            if (personaggio instanceof Paladino paladino) {
                System.out.println("Scegli come attaccare:");
                System.out.println("1) Attacco fisico");
                System.out.println("2) Colpo sacro");

                int sceltaAttacco;
                if (isBot) {
                    sceltaAttacco = randomGenerale.prossimoNumero(1, 2);
                    System.out.println(personaggio.getNomePersonaggio() + " (bot) sceglie: " + sceltaAttacco);
                } else {
                    sceltaAttacco = scannerGenerale.leggiInteroIntervallo(1, 2);
                }

                if (sceltaAttacco == 2) {
                    System.out.println("Scegli il colpo sacro:");
                    System.out.println("0)Se premi zero annulli l'attacco e passi il turno.");


                    Paladino.TipoMagiaSacra[] magie = Paladino.TipoMagiaSacra.values();
                    for (int i = 0; i < magie.length; i++) {
                        System.out.println((i + 1) + ") " + magie[i] + " (costo mana: " + magie[i].getCostoMana() + ")");
                    }

                   int sceltaMagia;
                if (isBot) {
                    sceltaMagia = randomGenerale.prossimoNumero(0, magie.length) - 1;
                    System.out.println(personaggio.getNomePersonaggio() + " (bot) sceglie: " + (sceltaMagia + 1));
                } else {
                    sceltaMagia = scannerGenerale.leggiInteroIntervallo(0, magie.length) - 1;
                }

                if (sceltaMagia == 0) {
                    turnoService.terminaTurnoCorrente(personaggio);
                    combattimento.setInCorso(false);
                }

                    if (sceltaMagia < 1 || sceltaMagia > magie.length) {
                        System.out.println("Scelta magia non valida. Attacco annullato.");
                        return;
                    }

                    paladino.setMagiaSelezionata(magie[sceltaMagia - 1]);
                } else {
                    System.out.println("Attacco fisico selezionato.");
                    applicaECalcolaDanno(combattimento, personaggio);
                }
            }

        } else if (scelta == 2) {

            turnoService.gestisciUsoOggettoDaZaino(personaggio);

        }
        try {
            fileService.writeLog(personaggio.getNomePersonaggio() + " ha scelto l'azione: " + scelta);
        } catch (Exception e) {
            throw new exception.DungeonException("Errore durante la scrittura del log azione combattimento", e);
        }
        return;
    }

    public boolean terminaCombattimento(Combattimento combattimento, Object vincitore) {
        if (combattimento == null || !combattimento.isInCorso()) {
            return false;
        }

        combattimento.setVincitore(vincitore);
        combattimento.setInCorso(false);
        Evento evento = combattimento.getEventoMostro();
        if (evento != null) {
            evento.setFineEvento(true);
            evento.setInizioEvento(false);
            if(vincitore instanceof Personaggio personaggioVincitore) {
                try {
                    fileService.writeLog("HA VINTOOOO!! IL VINCITORE è:" + personaggioVincitore.getNomePersonaggio());
                } catch (Exception e) {
                    throw new exception.DungeonException("Errore durante la scrittura del log del vincitore (personaggio)", e);
                }
                System.out.println(ANSI.RED + ANSI.BOLD+"Il vincitore è:" + personaggioVincitore.getNomePersonaggio()+ ANSI.RESET);
            } else if (vincitore instanceof Mostro mostroVincitore) {
                try {
                    fileService.writeLog("Ha vinto il mostro: " + mostroVincitore.getNomeMostro());
                } catch (Exception e) {
                    throw new exception.DungeonException("Errore durante la scrittura del log del vincitore (mostro)", e);
                }
                System.out.println(ANSI.RED + ANSI.BOLD+"Il vincitore è:" + mostroVincitore.getNomeMostro()+ ANSI.RESET);
            } 
        }

        return true;
    }

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
            System.out.println("Attacco mostro -> " + mostro.getNomeMostro() + " danno=" + danno);
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
            System.out.println("Attacco personaggio" + personaggioAttaccante.getNomePersonaggio() + " " + "danni inflitti=" + danno);

            if (mostro.èMortoilMostro()) {
                System.out.println("Mostro ucciso nome:" + mostro.getNomeMostro());
                difficoltaMostro++;
                System.out.println("Difficolta Mostro: " + difficoltaMostro);

                Evento evento = combattimento.getEventoMostro();
                Stanza stanza = combattimento.getStanza();
                stanza.rimuoviEvento(evento);

                personaggioAttaccante.aggiungiEsperienza();
                System.out.println("Parametri personaggio dopo la vittoria: " + personaggioAttaccante.getEsperienza()
                        + " livello:" + personaggioAttaccante.getLivello());

                terminaCombattimento(combattimento, personaggioAttaccante);
            }
            return danno;
        }

        return 0;
    }

    //ritorna il service corretto in base al tipo di personaggio
    public PersonaggioService getServicePerPersonaggio(Personaggio personaggio) {
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
        throw new exception.DungeonException("Tipo di personaggio non riconosciuto in getServicePerPersonaggio.");
    }
}
