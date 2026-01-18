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

public class MostroServiceImpl implements PersonaIncontrataService {

    private static final AtomicInteger ID_COUNTER = new AtomicInteger(1);

    private CombattimentoService combattimentoService;

    private static final String STATO_STORDITO = "stordito";
    private static final String STATO_IMMOBILIZZATO = "immobilizzato";
    private static final String STATO_AVVELENATO = "avvelenato";

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

    public MostroServiceImpl() {
        // crea un CombattimentoServiceImpl che usa questa istanza di MostroServiceImpl
        // in modo da evitare NPE quando il servizio viene costruito con il costruttore di default
        this.combattimentoService = new CombattimentoServiceImpl(this, null, new TurnoServiceImpl());
    }

    public void setCombattimentoService(CombattimentoService combattimentoService) {
        this.combattimentoService = combattimentoService;
    }

    /**
     * Esecuzione dell'attacco del mostro sul personaggio bersaglio.
     */
    /*   public int attaccoDelMostro(Mostro mostro, Personaggio bersaglio,int dannoBase) {


        Mostro.TipoAttaccoMostro tipoAttacco = mostro.getTipoAttaccoMostro();

         dannoBase = dannoBase(mostro, bersaglio);
        int danno = calcolaDannoPerTipo(tipoAttacco, dannoBase,bersaglio);

        System.out.println(mostro.getNomeMostro() + " usa " + tipoAttacco +
                " infliggendo " + danno + " danni a " + bersaglio.getNomePersonaggio());

        int dannoApplicato = bersaglio.subisciDanno(danno);
        return dannoApplicato;  
    }*/
    /**
     * Calcola solo il danno aggiuntivo in base al tipo di attacco.
     * L'applicazione degli effetti sul bersaglio è separata.
     */
    public int calcolaDannoPerTipo(Mostro.TipoAttaccoMostro tipoAttacco, int base) {
        if (tipoAttacco == null) {
            return base;
        }
        return switch (tipoAttacco) {
            case MORSO ->
                base + 2;
            case RUGGITO_DI_FUOCO ->
                base + 5;
            case URLO_ASSORDANTE ->
                base + 3;
            case RAGNATELA_FURTO ->
                base + 4;
            case ARTIGLI_POSSENTI ->
                base + 4;
        };
    }

    /**
     * Determina il tipo di effetto applicabile al bersaglio, se presente.
     * Restituisce null se non c'è effetto.
     */
    public Effetto.TipoEffetto effettoPerTipo(Mostro.TipoAttaccoMostro tipoAttacco) {
        if (tipoAttacco == null) {
            return null;
        }
        return switch (tipoAttacco) {
            case RUGGITO_DI_FUOCO, URLO_ASSORDANTE ->
                Effetto.TipoEffetto.STORDIMENTO;
            case RAGNATELA_FURTO ->
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
        int classeArmatura = bersaglio.getDifesa();

        System.out.println(mostro.getNomeMostro() + " tiro attacco: " + tiro + " + bonus " + bonusAttacco + " = " + totale + " (CA bersaglio: " + classeArmatura + ")");

        if (tiro == 1) {
            System.out.println("Tiro 1: fallimento critico del mostro!");
            return 0;
        }

        boolean critico = (tiro == 20);

        if (totale < classeArmatura && !critico) {
            System.out.println(mostro.getNomeMostro() + " manca il bersaglio.");
            return 0;
        }

        int baseDanno = Math.max(1, dannoBase(mostro, bersaglio));
        int dannoGrezzo = calcolaDannoPerTipo(tipoAttacco, baseDanno);

        if (critico) {
            dannoGrezzo = Math.max(1, dannoGrezzo * 2);
            System.out.println("Colpo critico del mostro! Danno raddoppiato.");
        }

        int dannoApplicato = bersaglio.subisciDanno(dannoGrezzo);

        // applica eventuale effetto (mappa su campi specifici del personaggio)
        Effetto.TipoEffetto effetto = effettoPerTipo(tipoAttacco);
        if (effetto != null) {
            switch (effetto) {
                case STORDIMENTO -> {
                    bersaglio.setTurniStordito(2);
                    bersaglio.setStatoPersonaggio(STATO_STORDITO);
                }
                case AVVELENAMENTO -> {
                    bersaglio.setTurniAvvelenato(3);
                    bersaglio.setStatoPersonaggio(STATO_AVVELENATO);
                }
                case FURTO -> {
                    bersaglio.setStatoPersonaggio(STATO_IMMOBILIZZATO);
                }
                default ->
                    bersaglio.setStatoPersonaggio(effetto.name().toLowerCase());
            }
            System.out.println(mostro.getNomeMostro() + " applica effetto " + effetto + " a " + bersaglio.getNomePersonaggio());
        }

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
