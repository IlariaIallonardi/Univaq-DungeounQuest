package service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import domain.Arma;
import domain.Effetto;
import domain.Evento;
import domain.Personaggio;
import domain.Stanza;
import domain.Trappola;
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
        System.out.println("[DEBUG] Hai trovato una trappola (id=" + trappola.getId() + ", descrizione=" + trappola.getDescrizione() + ")");
        boolean disinnescata = trappola.checkDiDisinnesco(personaggio);
        System.out.println("[DEBUG] Risultato disinnesco: " + disinnescata + " - personaggio=" + (personaggio != null ? personaggio.getNomePersonaggio() : "<null>") + " - stanzaId=" + (stanza != null ? stanza.getId() : -1));

        if (!disinnescata) {
            Effetto effettoTrappola = trappola.getEffetto();
            if (effettoTrappola == null || effettoTrappola.getTipo() == Effetto.TipoEffetto.NESSUN_EFFETTO) {
                Effetto.TipoEffetto tipoCasuale = tiraDado();
                effettoTrappola = new Effetto(tipoCasuale, "Effetto casuale", 0);
                System.out.println("[DEBUG] Nessun effetto definito; scelto effetto casuale: " + tipoCasuale);
            } else {
                System.out.println("[DEBUG] Effetto definito nella trappola: " + effettoTrappola.getTipo() + " (durata=" + effettoTrappola.getDurataTurni() + ")");
            }

                scattaTrappola(trappola, personaggio, effettoTrappola);

                System.out.println("[DEBUG] Stato personaggio dopo trappola -> stato=" + personaggio.getStatoPersonaggio()
                    + " | HP=" + personaggio.getPuntiVita()
                    + " | avvel=" + personaggio.getTurniAvvelenato()
                    + " | disarm=" + personaggio.isDisarmato()
                    + " | stord=" + personaggio.getTurniStordito()
                    + " | turniDaSaltare=" + personaggio.getTurniDaSaltare());
                System.out.println("[DEBUG] Rimuovo evento trappola id=" + e.getId() + " dalla stanza id=" + (stanza != null ? stanza.getId() : -1));
                stanza.rimuoviEvento(e);
            return true; // consuma turno
        } else {
                System.out.println("La trappola è stata disinnescata.");
                System.out.println("[DEBUG] Non attivata: rimuovo evento trappola id=" + e.getId() + " dalla stanza id=" + (stanza != null ? stanza.getId() : -1));
            stanza.rimuoviEvento(e);
            return false;
        }
    }

    public void scattaTrappola(Trappola trappola, Personaggio personaggio, Effetto effetto) {
        if (personaggio == null || effetto == null) {
            return;
        }

        System.out.println("[DEBUG] scattaTrappola -> trappolaId=" + trappola.getId() + " personaggio=" + (personaggio != null ? personaggio.getNomePersonaggio() : "<null>"));

        Effetto.TipoEffetto tipo = effetto.getTipo();
        int durata = effetto.getDurataTurni();

        System.out.println("[DEBUG] Scatta trappola -> effetto=" + tipo + " durata=" + durata);

        switch (tipo) {
            case DISARMA -> {
                personaggio.setStatoPersonaggio("DISARMATO");
                personaggio.setDisarmato(true);

                Arma arma = personaggio.getArmaEquippaggiata();
                System.out.println("[DEBUG] Arma equipaggiata: " + (arma != null ? arma.getNome() : "nessuna"));
                System.out.println("[DEBUG] Attacco attuale: " + personaggio.getAttacco());
                if (arma == null) {
                    System.out.println(" La trappola scatta, ma non hai armi equipaggiate.");
                    return;
                }
                personaggio.setArmaEquippaggiata(null);

                personaggio.setAttacco(personaggio.getAttacco() - arma.getDannoBonus());
                System.out.println("[DEBUG] Attacco ridotto di " + personaggio.getAttacco());
                System.out.println(" La trappola ti disarma! Hai perso definitivamente: " + arma.getNome());
            }
            //subisce solo il danno immediato
            case FURIA -> {
                personaggio.setStatoPersonaggio("FURIA");
                personaggio.subisciDanno(15);
                System.out.println(" La trappola causa un danno grave! -15 HP");
            }
            //ogni turno toglie dei punti vita finchè non finisce la durata
            case AVVELENAMENTO -> {
                personaggio.setStatoPersonaggio("AVVELENATO");

                // 3 turni totali
                personaggio.setTurniAvvelenato(2);

                //  DANNO IMMEDIATO (turno 1)
                personaggio.subisciDanno(5);

                System.out.println(
                        " Sei stato avvelenato! -5 HP (turni rimanenti: 3)"
                );
            }
            //ogni turno toglie dei punti difesa finchè non finisce la durata
            case STORDIMENTO -> {
                System.out.println("[DEBUG] Soldi attuali:" + personaggio.getDifesa());
                personaggio.setStatoPersonaggio("STORDITO");
                personaggio.setTurniStordito(2);
                personaggio.subisciDannoDifesa(5);
                System.out.println(" Sei stordito! -5 punti difesa");
                System.out.println("[DEBUG] Soldi attuali:" + personaggio.getDifesa());
            }

            case FURTO -> {
                personaggio.setStatoPersonaggio("DERUBATO");

                int soldiAttuali = personaggio.getPortafoglioPersonaggio();
                System.out.println("[DEBUG] Soldi attuali: " + soldiAttuali);
                if (soldiAttuali <= 0) {
                    System.out.println(" La trappola tenta di derubarti, ma non hai denaro!");
                    return;
                }

                //  Random tra 3 e 10
                int rubati = ThreadLocalRandom.current().nextInt(3, 11);

                // non può rubare più di quanto hai
                personaggio.setPortafoglioPersonaggio(soldiAttuali - rubati);
                System.out.println("[DEBUG] Soldi dopo furto: " + personaggio.getPortafoglioPersonaggio());
                System.out.println(
                        " Sei stato derubato! -" + rubati
                        + " monete (rimaste: " + personaggio.getPortafoglioPersonaggio() + ")"
                );

            }

            case SALTA_TURNO -> {
                personaggio.aggiungiTurniDaSaltare(1);
                System.out.println(" Una trappola ti colpisce! Salterai il prossimo turno.");
            }

            default -> {
                System.out.println(" La trappola non ha effetto visibile.");
            }
        }
    }

    /**
     * Applicare eventuali effetti di fine turno legati alle trappole (es: avvelenamento).
     * Questo permette di centralizzare il danno periodico nella logica della trappola.
     */
    public static void applicaEffettiFineTurno(Personaggio personaggio) {
        if (personaggio == null) return;

        if (personaggio.getTurniAvvelenato() > 0) {
            int dannoAvvel = 5;
            int applicato = personaggio.subisciDanno(dannoAvvel);

            personaggio.setTurniAvvelenato(personaggio.getTurniAvvelenato() - 1);

            if (personaggio.getTurniAvvelenato() == 0 && "AVVELENATO".equalsIgnoreCase(personaggio.getStatoPersonaggio())) {
                personaggio.setStatoPersonaggio("NORMALE");
                System.out.println("Il veleno ha perso effetto su " + personaggio.getNomePersonaggio());
            }
        }
        if (personaggio.getTurniStordito() > 0) {
            int dannoAvvel = 5;
                        int applicato = personaggio.subisciDannoDifesa(dannoAvvel);

                        personaggio.setTurniStordito(personaggio.getTurniStordito() - 1);

            if (personaggio.getTurniStordito() == 0 && "STORDITO".equalsIgnoreCase(personaggio.getStatoPersonaggio())) {
                personaggio.setStatoPersonaggio("NORMALE");
                System.out.println("Lo stordimento ha perso effetto su " + personaggio.getNomePersonaggio());
            }
        }
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
