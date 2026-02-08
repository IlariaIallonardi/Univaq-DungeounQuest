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

    private static final AtomicInteger ID_CONTATORE = new AtomicInteger(200);

    private final RandomSingleton random = RandomSingleton.getInstance();

    private CombattimentoService combattimentoService;

    

    /**
     * Calcolo del danno che il mostro infligge al personaggio.
     */
    public static int dannoBase(Mostro mostro, Personaggio personaggio) {
        if (mostro == null || personaggio == null) {
            return 0;
        }
        int attaccoMostro = mostro.getTipoAttaccoMostro().getDannoTipoMostro();

        return attaccoMostro;
    }

    public EffettoService effettoService;

    public MostroServiceImpl() {
        // Crea un CombattimentoServiceImpl che usa questa istanza di MostroServiceImpl,per evitare che il combattimento sia null.
        this.combattimentoService = new CombattimentoServiceImpl(this, null, new TurnoServiceImpl());
        this.effettoService = new EffettoService();
    }

    

    /**
     * Determina il tipo di effetto del mostro sul personaggio.
     * @param mostro Il mostro che attacca.
     * @param personaggio Il personaggio bersaglio dell'attacco.
     * 
     */

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
                    tipo = TipoAttaccoMostro.MORSO;
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

        Effetto effetto = new Effetto(tipoEffetto, " " );
        System.out.println(mostro.getNomeMostro() + " applica " + tipoEffetto + " a " + personaggio.getNomePersonaggio());
        this.effettoService.applicaEffetto(personaggio, effetto);
    }

    /**
     * Attacco del mostro generale.
     * @param mostro
     * @param bersaglio
     * 
     */

    public int attaccoDelMostro(Mostro mostro, Personaggio bersaglio) {

        int tipoAttacco = mostro.getTipoAttaccoMostro().getDannoTipoMostro();
        int tiro = random.prossimoNumero(1, 20);
        int bonusAttacco =tipoAttacco / 2;
        int totale = tiro + bonusAttacco;
        int difesaPersonaggio = bersaglio.getPuntiDifesa();

        System.out.println("Inizio attacco del mostro: " + mostro.getNomeMostro() + ", Bersaglio: " + bersaglio.getNomePersonaggio());
        System.out.println("Tiro:" + tiro + ", bonus attacco:" + bonusAttacco
                + ", totale:" + totale + ", difesa bersaglio:" + difesaPersonaggio
                + ", tipo attacco:" + mostro.getTipoAttaccoMostro()+",danni attacco:" + tipoAttacco);

        if (tiro == 1) {
            System.out.println("Tiro 1: fallimento critico.");
            return 0;
        }

        boolean lancioMigliore = (tiro == 20);

        if (totale < difesaPersonaggio && !lancioMigliore) {
            System.out.println("Mancato:" + totale + " < " + difesaPersonaggio + " e non critico");
            return 0;
        }

        // Applica il tipo effetto relativo del mostro sul personaggio bersaglio.
        impostaTipoAttaccoEApplicaEffetto(mostro, bersaglio);
        

        int dannoGrezzo = dannoBase(mostro, bersaglio);

        if (lancioMigliore) {
            dannoGrezzo *= 2;
            System.out.println("Colpo critico!Danni raddoppiati" + dannoGrezzo);
        }

        
        int dannoApplicato = bersaglio.subisciDanno(dannoGrezzo);
        System.out.println(mostro.getNomeMostro() + " usa " + mostro.getTipoAttaccoMostro()
                + " infliggendo " + dannoApplicato + " danni a " + bersaglio.getNomePersonaggio()
                + " (HP rimanenti: " + bersaglio.getPuntiVita() + ")");

        return dannoApplicato;
    }

    @Override
     public boolean attivaEvento(Personaggio personaggio, Evento evento) {
    

    if (evento instanceof Mostro mostro) {
        Stanza stanza = mostro.getPosizioneCorrente();
        mostro.setPosizioneCorrente(stanza);
           ///forse da cancellare
        System.out.println("Hai incontrato: " + mostro.getNomeMostro()+ "\nPunti vita mostro:" + mostro.getPuntiVitaMostro()
                + " difesa:" + mostro.getDifesaMostro()
                + " danno:" + mostro.getTipoAttaccoMostro().getDannoTipoMostro()
                + " esperienza:" + mostro.getEsperienzaMostro()
                + " livello:" + mostro.getLivelloMostro());

        try {
                    
            
           applicaDifficoltaMostro(mostro);
            System.out.println("Dopo applicazione difficoltà: punti vita:" + mostro.getPuntiVitaMostro()
                    + " difesa:" + mostro.getDifesaMostro()
                    + " danno:" + mostro.getTipoAttaccoMostro().getDannoTipoMostro()
                    + " esperienza:" + mostro.getEsperienzaMostro()
                    + " livello:" + mostro.getLivelloMostro());

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
    public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza ) {
        for (Evento evento : stanza.getListaEventiAttivi()) {
            boolean termina = attivaEvento(personaggio, evento);
            if (termina) {
                return;
            }
        }
        return;
    }


/**
 * Aumenta i parametri del mostro quando muore per renderlo leggermente più forte.
 * @param mostro
 */
    public void applicaDifficoltaMostro(Mostro mostro) {
        if (mostro == null) {
            return;
        }
        //prendiamo la difficoltà dal combattimento
        int difficolta = ((CombattimentoServiceImpl) combattimentoService).getDifficoltaMostro();
         System.out.println("Applicazione difficoltà al mostro:"+ difficolta);
        mostro.setPuntiVitaMostro(mostro.getPuntiVitaMostro() + difficolta * 2);
        mostro.setDifesaMostro(mostro.getDifesaMostro() +  difficolta * 2);
        mostro.getTipoAttaccoMostro().setDannoTipoMostro(mostro.getTipoAttaccoMostro().getDannoTipoMostro() +  difficolta * 2);
        mostro.setEsperienza(mostro.getEsperienza() +  difficolta * 2);
        mostro.normalizzaEsperienzaMostro();
        mostro.setAggiornamento(true);
    }

    @Override
    public Evento aggiungiEventoCasuale() {
        int id = ID_CONTATORE.getAndIncrement();

        List<String> nomi = List.of("Spiritello", "Drago", "Golem", "Ragno Gigante", "Troll");
        String nomeMostro = random.scegliRandomicamente(nomi);

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
        mostro.settareVitaeDifesaMostro();

        return mostro;
    }
}
