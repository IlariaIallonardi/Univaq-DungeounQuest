package service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import domain.Effetto;
import domain.Evento;
import domain.Mostro;
import domain.Personaggio;
import domain.Stanza;
import service.CombattimentoService;
import service.PersonaIncontrataService;
import service.EffettoService;

public class MostroServiceImpl implements PersonaIncontrataService {

    private static final AtomicInteger ID_COUNTER = new AtomicInteger(200);

    private CombattimentoService combattimentoService;

   

    public MostroServiceImpl(CombattimentoService combattimentoService) {
        this.combattimentoService = combattimentoService;
    }

    /**
     * Compatibile: costruttore di default che usa l'implementazione concreta.
     * Mantiene retrocompatibilità ma favorisce l'injection via costruttore.
     */
    /**
     * Calcolo del danno che il mostro infligge al personaggio.
     */
    public static int dannoBase(Mostro mostro, Personaggio personaggio) {
        if (mostro == null || personaggio == null) {
            return 0;
        }
        int attaccoMostro = mostro.getDannoMostro();
        int difesaPersonaggio = personaggio.getDifesa();
        return Math.max(1, attaccoMostro - difesaPersonaggio);
    }
  public EffettoService effettoService;
    public MostroServiceImpl() {
        // crea un CombattimentoServiceImpl che usa questa istanza di MostroServiceImpl
        // in modo da evitare NPE quando il servizio viene costruito con il costruttore di default
        this.combattimentoService = new CombattimentoServiceImpl(this, null, new TurnoServiceImpl());
        this.effettoService = new EffettoService();
    }

    public void setCombattimentoService(CombattimentoService combattimentoService) {
        this.combattimentoService = combattimentoService;
    }


    /**
     * Determina il tipo di effetto applicabile al bersaglio, se presente.
     * Restituisce null se non c'è effetto.
     */
        //// da unire con il metodo attaccoDelMostro che si trova nel domain.Mostro
        /// //ricordiamoci che deve essere intero il tipo di ritorno
    public Effetto.TipoEffetto effettoPerTipo(Mostro.TipoAttaccoMostro tipoAttacco) {
        if (tipoAttacco == null) {
            return null;
        }
        
        return switch (tipoAttacco) {
            case RUGGITO_DI_FUOCO, URLO_ASSORDANTE ->
                Effetto.TipoEffetto.STORDIMENTO;
            case RAGNATELA ->
                Effetto.TipoEffetto.FURTO;
            case ARTIGLI_POSSENTI ->
                Effetto.TipoEffetto.AVVELENAMENTO;
            default ->
                null;
        };
    }

    public int attaccoDelMostro(Mostro mostro, Personaggio bersaglio) {
        if (mostro == null || bersaglio == null) {
            return 0;
        }

        Mostro.TipoAttaccoMostro tipoAttacco = mostro.getTipoAttaccoMostro();

        int tiro = java.util.concurrent.ThreadLocalRandom.current().nextInt(1, 21);
        int bonusAttacco = Math.max(0, mostro.getDannoMostro() / 2);
        int totale = tiro + bonusAttacco;
        int difesaP = bersaglio.getDifesa();

        System.out.println(mostro.getNomeMostro() + " tiro attacco: " + tiro + " + bonus " + bonusAttacco + " = " + totale + " (CA bersaglio: " + difesaP + ")");

        if (tiro == 1) {
            System.out.println("Tiro 1: fallimento critico del mostro!");
            return 0;
        }

        boolean critico = (tiro == 20);

        if (totale < difesaP && !critico) {
            System.out.println(mostro.getNomeMostro() + " manca il bersaglio.");
            return 0;
        }

        int baseDanno = Math.max(1, dannoBase(mostro, bersaglio));

        //ci richiamiamo il metodo che dobbiamo fare
        int dannoGrezzo = effettoPerTipo(tipoAttacco);

        if (critico) {
            dannoGrezzo = Math.max(1, dannoGrezzo * 2);
            System.out.println("Colpo critico del mostro! Danno raddoppiato.");
        }

        int dannoApplicato = bersaglio.subisciDanno(dannoGrezzo);

        // applica eventuale effetto (mappa su campi specifici del personaggio)
      /*  Effetto.TipoEffetto tipoEffetto = switch (tipoAttacco) {
    case RUGGITO_DI_FUOCO -> Effetto.TipoEffetto.STORDIMENTO;
    case URLO_ASSORDANTE -> Effetto.TipoEffetto.STORDIMENTO;
    case RAGNATELA-> Effetto.TipoEffetto.STORDIMENTO;
    case MORSO -> Effetto.TipoEffetto.FURTO;
    case ARTIGLI_POSSENTI -> Effetto.TipoEffetto.AVVELENAMENTO;
    
    default -> null;
};*/


    System.out.println(mostro.getNomeMostro() + " usa " + tipoAttacco
            + " infliggendo " + dannoGrezzo + " danni a " + bersaglio.getNomePersonaggio()
            + " (HP rimanenti: " + bersaglio.getPuntiVita() + ")");

    return dannoApplicato;
}
    @Override
    public boolean attivaEvento(Personaggio personaggio, Evento e) {
        if (personaggio == null || e == null) {
            return false;
        }

        if (e instanceof Mostro mostro) {
            Stanza stanza = mostro.getPosizioneCorrente() != null ? mostro.getPosizioneCorrente() : personaggio.getPosizioneCorrente();
            mostro.setPosizioneCorrente(stanza);
            System.out.println("Hai incontrato: " + mostro.getNomeMostro());

            try {
                Object vincitore = combattimentoService.iniziaCombattimento(personaggio, mostro, stanza);

                if (vincitore instanceof Personaggio) {
                    System.out.println("Hai sconfitto " + mostro.getNomeMostro() + "!");
                }
            } catch (RuntimeException ex) {
                System.out.println("Errore durante il combattimento: " + ex.getMessage());
                ex.printStackTrace();
            }

            return true;
        }
        return false;
    }

    @Override
    public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza) {
        List<Evento> snapshot = List.copyOf(stanza.getListaEventiAttivi());
        for (Evento e : snapshot) {
            boolean termina = attivaEvento(personaggio, e);
            if (termina) {
                return; // interrompe la catena di eventi e termina il turno

            }
        }
    }

    @Override
    public Evento aggiungiEventoCasuale() {
        int id = ID_COUNTER.getAndIncrement();
        var rnd = java.util.concurrent.ThreadLocalRandom.current();

        String[] nomi = {"Spiritello", "Drago", "Golem", "Ragno Gigante", "Troll"};
        String nomeMostro = nomi[rnd.nextInt(nomi.length)];

        String descrizione = switch (nomeMostro) {
            case "Spiritello" ->
                "un pericoloso " + nomeMostro + " ti attacca con morso";
            case "Drago" ->
                "un pericoloso " + nomeMostro + " ti attacca con ruggito di fuoco";
            case "Golem" ->
                "un pericoloso " + nomeMostro + " ti attacca con urlo assordante";
            case "Ragno Gigante" ->
                "un pericoloso " + nomeMostro + " ti attacca con ragnatela immobilizzante";
            case "Troll" ->
                "un pericoloso " + nomeMostro + " ti attacca con artigli possenti";
            default ->
                "un pericoloso mostro ti attacca";
        };

        Mostro mostro = new Mostro(id, false, false, descrizione, "mostro", 0, 0, nomeMostro, null, null, 0);
        // imposta vita/difesa/tipo in base al nome
        mostro.settareVitaeDifesaMostro();
        // assegna danno in modo semplice e riproducibile
        int dannoMostro = Math.max(1, rnd.nextInt(5, 21)); // 5..20
        mostro.setDannoMostro(dannoMostro);
        System.out.println("Creato mostro " + nomeMostro + " con danno=" + dannoMostro);
        return mostro;
    }
}
