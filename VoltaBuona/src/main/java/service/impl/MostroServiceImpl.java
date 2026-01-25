package service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import domain.Effetto;
import domain.Evento;
import domain.Mostro;
import domain.Mostro.TipoAttaccoMostro;
import domain.Personaggio;
import domain.Stanza;
import service.CombattimentoService;
import service.EffettoService;
import service.PersonaIncontrataService;

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
        int attaccoMostro = mostro.getTipoAttaccoMostro().getDannoTipoMostro();

        return Math.max(1, attaccoMostro);
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
    public void impostaTipoAttaccoEApplicaEffetto(Mostro mostro, Personaggio personaggio) {
        if (mostro == null) {
            return;
        }

        String nome = mostro.getNomeMostro();
        Mostro.TipoAttaccoMostro tipo = null;
        if (nome != null) {
            switch (nome) {
                case "Spiritello" ->
                    tipo = TipoAttaccoMostro.MORSO;
                case "Drago" ->
                    tipo = TipoAttaccoMostro.RUGGITO_DI_FUOCO;
                case "Golem" ->
                    tipo = TipoAttaccoMostro.URLO_ASSORDANTE;
                case "Ragno Gigante" ->
                    tipo = TipoAttaccoMostro.RAGNATELA;
                case "Troll" ->
                    tipo = TipoAttaccoMostro.ARTIGLI_POSSENTI;
                default ->
                    tipo = null;
            }
        }
        mostro.setTipoAttaccoMostro(tipo);

        Effetto.TipoEffetto tipoEffetto = null;
        if (tipo != null) {
            switch (tipo) {
                case RUGGITO_DI_FUOCO, URLO_ASSORDANTE ->
                    tipoEffetto = Effetto.TipoEffetto.STORDIMENTO;
                case RAGNATELA ->
                    tipoEffetto = Effetto.TipoEffetto.FURTO;
                case ARTIGLI_POSSENTI ->
                    tipoEffetto = Effetto.TipoEffetto.AVVELENAMENTO;
                case MORSO ->
                    tipoEffetto = Effetto.TipoEffetto.FURTO;
                default ->
                    tipoEffetto = null;
            }
        }

        Effetto effetto = new Effetto(tipoEffetto, "Effetto da " + mostro.getNomeMostro());
        System.out.println(mostro.getNomeMostro() + " applica " + tipoEffetto + " a " + personaggio.getNomePersonaggio());
        this.effettoService.applicaEffetto(personaggio, effetto);
    }

    public int attaccoDelMostro(Mostro mostro, Personaggio bersaglio) {
        if (mostro == null || bersaglio == null) {

            return 0;
        }

        int tipoAttacco = mostro.getTipoAttaccoMostro().getDannoTipoMostro();

        int tiro = java.util.concurrent.ThreadLocalRandom.current().nextInt(1, 21);
        int bonusAttacco = Math.max(0, tipoAttacco / 2);
        int totale = tiro + bonusAttacco;
        int difesaP = bersaglio.getDifesa();

        System.out.println("[DEBUG] Inizio attaccoDelMostro - Mostro: " + mostro.getNomeMostro()
                + ", Bersaglio: " + bersaglio.getNomePersonaggio()
                + ", HP bersaglio prima: " + bersaglio.getPuntiVita());
        System.out.println("[DEBUG] tiro=" + tiro + ", bonusAttacco=" + bonusAttacco
                + ", totale=" + totale + ", difesaBersaglio=" + difesaP
                + ", tipoAttaccoPrecedente=" + tipoAttacco);

        if (tiro == 1) {
            System.out.println("[DEBUG] Tiro 1: fallimento critico del mostro!");
            return 0;
        }

        boolean critico = (tiro == 20);

        if (totale < difesaP && !critico) {
            System.out.println("[DEBUG] Mancato: totale < difesa e non critico");
            return 0;
        }

        // applica tipo/effect (side-effect: imposta tipo sul mostro e applica effetto al bersaglio)
        System.out.println("[DEBUG] chiamata a impostaTipoAttaccoEApplicaEffetto...");
        impostaTipoAttaccoEApplicaEffetto(mostro, bersaglio);
        System.out.println("[DEBUG] dopo impostaTipoAttaccoEApplicaEffetto - tipoAttacco attuale = " + mostro.getTipoAttaccoMostro());

        int dannoGrezzo = Math.max(1, dannoBase(mostro, bersaglio));

        if (critico) {
            dannoGrezzo = Math.max(1, dannoGrezzo * 2);
            System.out.println("[DEBUG] Colpo critico! dannoGrezzo raddoppiato a " + dannoGrezzo);
        }

        System.out.println("[DEBUG] prima subisciDanno: dannoGrezzo=" + dannoGrezzo);
        int dannoApplicato = bersaglio.subisciDanno(dannoGrezzo);
        System.out.println("[DEBUG] dopo subisciDanno: dannoApplicato=" + dannoApplicato
                + ", HP rimanenti bersaglio=" + bersaglio.getPuntiVita());

        System.out.println(mostro.getNomeMostro() + " usa " + mostro.getTipoAttaccoMostro()
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

        System.out.println("Creato mostro " + nomeMostro);
        return mostro;
    }
}
