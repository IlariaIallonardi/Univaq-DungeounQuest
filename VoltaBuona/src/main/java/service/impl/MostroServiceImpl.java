package service.impl;

import java.util.concurrent.atomic.AtomicInteger;

import domain.Evento;
import domain.Mostro;
import domain.Personaggio;
import domain.Stanza;
import service.PersonaIncontrataService;

public class MostroServiceImpl implements  PersonaIncontrataService {
      private static final AtomicInteger ID_COUNTER = new AtomicInteger(1);
    /**
     * Calcolo del danno che il mostro 
     * infligge al personaggio.
     */
    public static int dannoBase(Mostro mostro, Personaggio personaggio) {
        if (mostro == null || personaggio == null) return 0;
        int attaccoMostro = mostro.getDannoMostro(); 
        int difesaPersonaggio = personaggio.getDifesa();   
        return Math.max(1, attaccoMostro - difesaPersonaggio);
    }

    /**
     * Esecuzione dell'attacco del mostro
     * sul personaggio bersaglio.
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

    public  int calcolaDannoPerTipo(Mostro.TipoAttaccoMostro tipoAttacco, int base, Personaggio bersaglio) {
        if(tipoAttacco == Mostro.TipoAttaccoMostro.MORSO){
            return base + 2;
        } else if (tipoAttacco == Mostro.TipoAttaccoMostro.RUGGITO_DI_FUOCO) {
            bersaglio.setStatoPersonaggio("stordito");
            return base + 5; 
        } else if (tipoAttacco == Mostro.TipoAttaccoMostro.URLO_ASSORDANTE) {
            bersaglio.setStatoPersonaggio("stordito");
            return base + 3; 
        } else if (tipoAttacco == Mostro.TipoAttaccoMostro.RAGNATELA_IMMOBILIZZANTE) {
            bersaglio.setStatoPersonaggio("immobilizzato");
            return base + 4; 
        } else if (tipoAttacco == Mostro.TipoAttaccoMostro.ARTIGLI_POSSENTI) {
            bersaglio.setStatoPersonaggio("avvelenato");
            return base + 4; 
        } else {
            return base; 
        }

        

    } 

       public int attaccoDelMostro(Mostro mostro, Personaggio bersaglio, int dannoBase) {
    if (mostro == null || bersaglio == null) return 0;

    Mostro.TipoAttaccoMostro tipoAttacco = mostro.getTipoAttaccoMostro();

    int tiro = java.util.concurrent.ThreadLocalRandom.current().nextInt(1, 21);
    int bonusAttacco = Math.max(0, mostro.getDannoMostro() / 2);
    int totale = tiro + bonusAttacco;
    int ca = bersaglio.getDifesa();

    System.out.println(mostro.getNomeMostro() + " tiro attacco: " + tiro + " + bonus " + bonusAttacco + " = " + totale + " (CA bersaglio: " + ca + ")");

    if (tiro == 1) {
        System.out.println("Tiro 1: fallimento critico del mostro!");
        return 0;
    }

    boolean critico = (tiro == 20);

    if (totale < ca && !critico) {
        System.out.println(mostro.getNomeMostro() + " manca il bersaglio.");
        return 0;
    }

    int baseDanno = Math.max(1, mostro.getDannoMostro());
    int dannoGrezzo = calcolaDannoPerTipo(tipoAttacco, baseDanno, bersaglio);

    if (critico) {
        dannoGrezzo = Math.max(1, dannoGrezzo * 2);
        System.out.println("Colpo critico del mostro! Danno raddoppiato.");
    }

    int dannoApplicato = bersaglio.subisciDanno(dannoGrezzo);

    System.out.println(mostro.getNomeMostro() + " usa " + tipoAttacco
        + " infliggendo " + dannoGrezzo + " danni a " + bersaglio.getNomePersonaggio()
        + " (HP rimanenti: " + bersaglio.getPuntiVita() + ")");

    return dannoApplicato;
}

    @Override
    public void rimuoviEventoDaStanza(Stanza stanza, Evento evento){
        if(evento== null || stanza==null) return;
        if(evento instanceof Mostro mostro){
            if(mostro.èMortoilMostro())
           stanza.getListaEventiAttivi().remove(evento);
        }
    }

    @Override
    public boolean attivaEvento(Personaggio personaggio, Evento e){
        if (personaggio == null || e == null) return false;

        if (e instanceof Mostro mostro) {
            Stanza stanza = mostro.getPosizioneCorrenteMostro() != null ? mostro.getPosizioneCorrenteMostro() : personaggio.getPosizioneCorrente();
            mostro.setPosizioneCorrenteMostro(stanza);
            System.out.println("Hai incontrato: " + mostro.getNomeMostro());

            // Avvia il combattimento (usa il service di combattimento)
            CombattimentoServiceImpl combattimento = new CombattimentoServiceImpl();
            Object vincitore = combattimento.iniziaCombattimento(personaggio, mostro, stanza);

            // Se il mostro è stato sconfitto, rimuovilo dalla stanza
            if (vincitore instanceof Personaggio) {
                System.out.println("Hai sconfitto " + mostro.getNomeMostro() + "!");
                rimuoviEventoDaStanza(stanza, mostro);
            }

            // Il combattimento consuma il turno del giocatore
            return true;
        }
        return false;
    }

    @Override   
    public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza){   
        for (Evento e : stanza.getListaEventiAttivi()) {
            boolean termina = attivaEvento(personaggio, e);
            if (termina) return; // interrompe la catena di eventi e termina il turno
        }
    }

    @Override
    public Evento aggiungiEventoCasuale() {
        int id = ID_COUNTER.getAndIncrement();
        var rnd = java.util.concurrent.ThreadLocalRandom.current();

        String[] nomi = {"Spiritello", "Drago", "Golem", "Ragno Gigante", "Troll"};
        String nomeMostro = nomi[rnd.nextInt(nomi.length)];
        
    
        String descrizione = switch (nomeMostro) {
        case "Spiritello" -> "un pericoloso " + nomeMostro + " ti attacca con morso";
        case "Drago" -> "un pericoloso " + nomeMostro + " ti attacca con ruggito di fuoco";
        case "Golem" -> "un pericoloso " + nomeMostro + " ti attacca con urlo assordante";
        case "Ragno Gigante" -> "un pericoloso " + nomeMostro + " ti attacca con ragnatela immobilizzante";
        case "Troll" -> "un pericoloso " + nomeMostro + " ti attacca con artigli possenti";
        default -> "un pericoloso mostro ti attacca";
        };
       
        Mostro mostro = new Mostro(id,false,false,descrizione,"mostro",0,0,nomeMostro,null,null,0);
        int dannoMostro = rnd.nextInt(5, 21); // 5..20
        mostro.settareVitaeDifesaMostro();
        return mostro;
    }
}