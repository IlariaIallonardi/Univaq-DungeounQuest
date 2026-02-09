package service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import domain.Effetto;
import domain.Evento;
import domain.Personaggio;
import domain.Stanza;
import domain.Trappola;
import service.EffettoService;
import service.EventoService;

public class TrappolaServiceImpl implements EventoService {

    private static final AtomicInteger ID_CONTATORE= new AtomicInteger(600);
    private final RandomSingleton randomGenerale = RandomSingleton.getInstance();

    @Override
    public boolean attivaEvento(Personaggio personaggio, Evento e) {
        if (!(e instanceof Trappola trappola)) {
            return false;
        }

        Stanza stanza = trappola.getPosizioneCorrente();
        boolean disinnescata = trappola.checkDiDisinnesco(personaggio);
        EffettoService effettoService = new EffettoService();
        
        if (!disinnescata) {
            Effetto effettoTrappola = trappola.getEffetto();
            if (effettoTrappola == null || effettoTrappola.getTipo() == Effetto.TipoEffetto.NESSUN_EFFETTO) {
                Effetto.TipoEffetto tipoCasuale = tiraDado();
                effettoTrappola = new Effetto(tipoCasuale, "Effetto casuale", 0);
                System.out.println("Effetto della trappola: " + effettoTrappola.getTipo() + " (durata=" + effettoTrappola.getDurataTurni() + ")");
            }

             effettoService.applicaEffetto(personaggio, effettoTrappola);

            System.out.println("Stato personaggio dopo trappola:"+" " 
                    + "\n Stato=" + personaggio.getStatoPersonaggio()
                    + "\n Punti vita=" + personaggio.getPuntiVita()
                    + "\n Punti difesa=" + personaggio.getPuntiDifesa()
                    + "\n Soldi=" + personaggio.getPortafoglioPersonaggio()
                    + "\n Avvelenato=" + personaggio.getTurniAvvelenato()
                    + "\n Disarmato=" + personaggio.isDisarmato()
                    + "\n Stordito=" + personaggio.getTurniStordito()
                    + "\n Turni da saltare=" + personaggio.getTurniDaSaltare());
        
            stanza.rimuoviEvento(e);
            return true; 
        } else {
            stanza.rimuoviEvento(e);
            return false;
        }
    }

    

    public Effetto.TipoEffetto tiraDado() {
        int dado = randomGenerale.prossimoNumero(1, 6);

        return switch (dado) {
            case 1 ->
                Effetto.TipoEffetto.DISARMA;
            case 2 ->
                Effetto.TipoEffetto.FURIA;
            case 3 ->
                Effetto.TipoEffetto.AVVELENAMENTO;
            case 4 ->
                Effetto.TipoEffetto.STORDIMENTO;
            case 5 ->
                Effetto.TipoEffetto.FURTO;
            default ->
                Effetto.TipoEffetto.AVVELENAMENTO;
        };
    }

   

    @Override
    public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza) {
    
        //Controlla se ci sono trappole nella stanza e le aggiunge ad una lista 'trappole'.
        List<Trappola> trappole = new ArrayList<>();
        for (Evento evento : stanza.getListaEventiAttivi()) {
            if (evento instanceof Trappola trappola) {
                trappole.add(trappola);
            }
        }

        if (trappole.isEmpty()) {
            return;
        }

        // Si sceglie una trappola a caso tra quelle presenti nella lista creata.
        Trappola scelta = randomGenerale.scegliRandomicamente(trappole);
        attivaEvento(personaggio, scelta);

    }

    @Override
    public Evento aggiungiEventoCasuale() {
        int id = ID_CONTATORE.getAndIncrement();
        
        // scegli un effetto casuale
        Effetto.TipoEffetto[] tipiEffetto = Effetto.TipoEffetto.values();
        Effetto.TipoEffetto tipoEffetto = tipiEffetto[randomGenerale.prossimoNumero(0, tipiEffetto.length - 1)];

        String descrizione = switch (tipoEffetto) {
            case DISARMA ->
                "Trappola di disarmo";
            case AVVELENAMENTO ->
                "Trappola di avvelenamento";
            case STORDIMENTO ->
                "Trappola di stordimento";
            case FURTO ->
                "Trappola di furto";
            case SALTA_TURNO ->
                "Trappola che fa saltare il turno";
            default ->
                "Trappola senza effetto";
        };

        Effetto effetto = new Effetto(tipoEffetto, descrizione, 0);
        Trappola trappola = new Trappola(effetto, id, false, false, descrizione, "Trappola", null);
        return trappola;
    }

}
