package service.impl;

import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import domain.Arciere;
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

    private  MostroServiceImpl mostroService;
    private   PersonaggioService personaggioService;
    TurnoServiceImpl turnoService;
    private static final Random RNG = new Random();
    private static final Scanner scanner = new Scanner(System.in);
    private static int difficoltaMostro = 0;

    public CombattimentoServiceImpl(MostroServiceImpl mostroService, PersonaggioService personaggioService, TurnoServiceImpl turnoService) {
        this.mostroService = mostroService;
        this.personaggioService = personaggioService;
        this.turnoService = turnoService;
    }


    @Override
    public Object getVincitore(Combattimento combattimento) {
        return combattimento.getVincitore();
    }

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

    @Override
    public Object iniziaCombattimento(Personaggio personaggio, Mostro mostro, Stanza stanza) {
        if (personaggio == null || mostro == null) {
            System.out.println("Combattimento non valido.");
            return false;
        }

        System.out.println("\nInizia il combattimento: " + personaggio.getNomePersonaggio() + " VS " + mostro.getNomeMostro());
        
        


        Evento evento = null;
        if (mostro instanceof Evento) {
            evento = (Evento) mostro;
           
            evento = new Evento(0, true, false, "Incontro con " + mostro.getNomeMostro(),
                    "Combattimento_" + mostro.getNomeMostro(),stanza );
            if (stanza != null && stanza.getListaEventiAttivi() != null) {
                stanza.getListaEventiAttivi().add(evento);
                evento.setPosizioneCorrente(stanza);
            }
        }

    /**
     * Controlliamo se il personaggio e il mostro si trovano nella stessa stanza,
     * tranne nel caso dell'arciere che può fare un attacco nelle stanze adiacenti.
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
        Combattimento combattimento = new Combattimento( null, 0, evento, 0, true, personaggio, stanza, 0, null, mostro );
        combattimento.setInCorso(true);
        
        if (personaggio instanceof Arciere arciere) {
            if(arciereAdiacente){
                    ArciereServiceImpl arciereService = new ArciereServiceImpl();
                    arciereService.attaccoDistanzaArciere(arciere, mostro);
                    combattimento.setInCorso(false);
                        return arciere;}}


        // Iniziativa: 0 = mostro, 1 = personaggio
        Integer iniziativa = combattimento.getIniziativa();
        if (iniziativa == null) {
            iniziativa = RNG.nextInt(2);
            combattimento.setIniziativa(iniziativa);
            if(iniziativa==0) {
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



    
    @Override
    public void scegliAzioneCombattimento(Combattimento combattimento, Personaggio personaggio, Zaino zaino) {
    
            if (personaggio == null || combattimento == null) {
            return;
        }

        System.out.println("\nScegli azione in combattimento per " + personaggio.getNomePersonaggio());
        System.out.println("1) Attacca");
        System.out.println("2) Usa un oggetto");

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

            // Se sei un mago ti verrà chiesto quale magia vuoi usare 
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

            // Se sei un Paladino ti verrà chiesto se vuoi fare un attacco fisico o magico.
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
                    System.out.println("Attacco fisico selezionato.");
                     applicaECalcolaDanno(combattimento, personaggio);
                }
            }

        } else if (scelta == 2) {

            turnoService.gestisciUsoOggettoDaZaino(personaggio);

        }
        return;
    }

    
    @Override
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
            System.out.println("HA VINTOOOO!! IL VINCITORE è:" + vincitore);
        }

        return true;
    }

    
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
       System.out.println("Attacco personaggio" + personaggioAttaccante.getNomePersonaggio() +" "+ "danni inflitti=" + danno);

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

        return new GuerrieroServiceImpl(); 
    }
}