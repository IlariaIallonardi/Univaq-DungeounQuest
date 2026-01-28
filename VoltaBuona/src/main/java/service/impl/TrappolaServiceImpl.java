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

    private static final AtomicInteger ID_COUNTER = new AtomicInteger(600);

    @Override
    public boolean attivaEvento(Personaggio personaggio, Evento e) {
        if (!(e instanceof Trappola trappola)) {
            return false;
        }

        Stanza stanza = trappola.getPosizioneCorrente();
        if (stanza == null && personaggio != null) {
            stanza = personaggio.getPosizioneCorrente();
        }
      //  System.out.println(" Sei caduto in una trappola" + trappola.getDescrizione() + "!");
        boolean disinnescata = trappola.checkDiDisinnesco(personaggio);

        if (!disinnescata) {
            Effetto effettoTrappola = trappola.getEffetto();
            if (effettoTrappola == null || effettoTrappola.getTipo() == Effetto.TipoEffetto.NESSUN_EFFETTO) {
                Effetto.TipoEffetto tipoCasuale = tiraDado();
                effettoTrappola = new Effetto(tipoCasuale, "Effetto casuale", 0);
                System.out.println("Effetto della trappola: " + effettoTrappola.getTipo() + " (durata=" + effettoTrappola.getDurataTurni() + ")");
            }

            scattaTrappola(trappola, personaggio, effettoTrappola);

            System.out.println("Stato personaggio dopo trappola:"+" " 
                    + "\n Stato=" + personaggio.getStatoPersonaggio()
                    + "\n Punti vita=" + personaggio.getPuntiVita()
                    + "\n Punti difesa=" + personaggio.getDifesa()
                    + "\n Soldi=" + personaggio.getPortafoglioPersonaggio()
                    + "\n Avvelenato=" + personaggio.getTurniAvvelenato()
                    + "\n Disarmato=" + personaggio.isDisarmato()
                    + "\n Stordito=" + personaggio.getTurniStordito()
                    + "\n Turni da saltare=" + personaggio.getTurniDaSaltare());
            //  System.out.println("[DEBUG] Rimuovo evento trappola id=" + e.getId() + " dalla stanza id=" + (stanza != null ? stanza.getId() : -1));
            stanza.rimuoviEvento(e);
            return true; // consuma turno
        } else {
           // System.out.println("La trappola è stata disinnescata.");
            // System.out.println("[DEBUG] Non attivata: rimuovo evento trappola id=" + e.getId() + " dalla stanza id=" + (stanza != null ? stanza.getId() : -1));
            stanza.rimuoviEvento(e);
            return false;
        }
    }

    public void scattaTrappola(Trappola trappola, Personaggio personaggio, Effetto effetto) {
        if (personaggio == null || effetto == null) {
            return;
        }

        /*   System.out.println("[DEBUG] scattaTrappola -> trappolaId=" + trappola.getId()
                + " personaggio=" + (personaggio != null ? personaggio.getNomePersonaggio() : "<null>"));

        System.out.println("[DEBUG] Scatta trappola -> effetto=" + effetto.getTipo() + " durata=" + effetto.getDurataTurni());*/
        EffettoService effettoService = new EffettoService();
        effettoService.applicaEffetto(personaggio, effetto);
    }

    private Effetto.TipoEffetto tiraDado() {
        int dado = (int) (Math.random() * 6) + 1;

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

    public boolean esitoDisinnesco(Trappola trappola, Personaggio personaggio) {
        // logica per calcolare esito disinnesco
        return trappola.checkDiDisinnesco(personaggio);
    }

    @Override
    public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza) {
        if (stanza == null || stanza.getListaEventiAttivi() == null || stanza.getListaEventiAttivi().isEmpty()) {
            return;
        }

        List<Trappola> trappole = new ArrayList<>();
        for (Evento e : new ArrayList<>(stanza.getListaEventiAttivi())) {
            if (e instanceof Trappola t) {
                trappole.add(t);
            }
        }

        if (trappole.isEmpty()) {
            return;
        }

        // Scegliere una trappola a caso tra quelle presenti
        Trappola scelta = trappole.get(java.util.concurrent.ThreadLocalRandom.current().nextInt(trappole.size()));
        attivaEvento(personaggio, scelta);

    }

    @Override
    public Evento aggiungiEventoCasuale() {
        int id = ID_COUNTER.getAndIncrement();
        var rnd = java.util.concurrent.ThreadLocalRandom.current();

        // scegli un effetto casuale
        Effetto.TipoEffetto[] tipiEffetto = Effetto.TipoEffetto.values();
        Effetto.TipoEffetto tipoEffetto = tipiEffetto[rnd.nextInt(tipiEffetto.length)];

        String descrizione
                = switch (tipoEffetto) {
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
