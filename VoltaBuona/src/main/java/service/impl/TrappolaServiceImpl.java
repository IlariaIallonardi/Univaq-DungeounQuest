package service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import domain.Effetto;
import domain.Evento;
import domain.Personaggio;
import domain.Stanza;
import domain.Trappola;
import service.EventoService;


public class TrappolaServiceImpl implements EventoService {
     private static final AtomicInteger ID_COUNTER = new AtomicInteger(1);

   /*@Override
public boolean attivaEvento(Personaggio personaggio, Evento e) {
    if (!(e instanceof Trappola trappola)) return false;

    Stanza stanza = trappola.getPosizioneCorrenteTrappola();
    if (stanza == null && personaggio != null) {
        stanza = personaggio.getPosizioneCorrente();
    }

    System.out.println("Hai trovato una trappola!");
    boolean disinnescata = trappola.checkDiDisinnesco(personaggio);

    if (!disinnescata) {
        scattaTrappola(personaggio);
        if (stanza != null) rimuoviEventoDaStanza(stanza, e);
        return true; // consuma turno
    } else {
        System.out.println("La trappola è stata disinnescata.");
        if (stanza != null) rimuoviEventoDaStanza(stanza, e);
        return false;
    }
}*/

/**
 * Esegue l'evento trappola per il `personaggio`.
 *
 * Flusso:
 *  1. Verifica che `e` sia una `Trappola`.
 *  2. Chiama `trappola.checkDiDisinnesco(personaggio)`.
 *  3. Se il disinnesco fallisce:
 *     - prende l'`Effetto` definito nella trappola (se presente) altrimenti
 *       sceglie un effetto di fallback casuale;
 *     - invoca `scattaTrappola(trappola, personaggio, effetto)` per applicare
 *       stato/danno/durata al personaggio;
 *     - rimuove l'evento dalla stanza (se presente) e ritorna true (turno consumato).
 *  4. Se il disinnesco riesce: rimuove l'evento  e ritorna false.
 */

@Override
public boolean attivaEvento(Personaggio personaggio, Evento e) {
    if (!(e instanceof Trappola trappola)) return false;

    Stanza stanza = trappola.getPosizioneCorrenteTrappola();
    if (stanza == null && personaggio != null) {
        stanza = personaggio.getPosizioneCorrente();
    }

    System.out.println("[DEBUG] Hai trovato una trappola (id=" + trappola.getId() + ", descrizione=" + trappola.getDescrizione() + ")");
    boolean disinnescata = trappola.checkDiDisinnesco(personaggio);

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
            + " | cong=" + personaggio.getTurniCongelato()
            + " | stord=" + personaggio.getTurniStordito()
            + " | turniDaSaltare=" + personaggio.getTurniDaSaltare());

       /*  if (stanza != null) rimuoviEventoDaStanza(stanza, e);
        return true; // consuma turno
    } else {
        System.out.println("La trappola è stata disinnescata.");
        if (stanza != null) rimuoviEventoDaStanza(stanza, e);
        return false;
    }*/
    
    rimuoviEventoDaStanza(stanza, e);
    return true; // consuma turno
} else{
    System.out.println("La trappola è stata disinnescata.");
    rimuoviEventoDaStanza(stanza, e);
    return false;
}}




    public void scattaTrappola(Trappola trappola, Personaggio personaggio, Effetto effetto) {
    if (personaggio == null || effetto == null) return;

    Effetto.TipoEffetto tipo = effetto.getTipo();
    int durata = effetto.getDurataTurni();

    System.out.println("[DEBUG] Scatta trappola -> effetto=" + tipo + " durata=" + durata);

    switch (tipo) {
        case CONGELAMENTO -> {
            personaggio.setStatoPersonaggio("CONGELATO");
            personaggio.setTurniCongelato(Math.max(durata, 3));
            personaggio.subisciDanno(5);
            System.out.println(" La trappola ti congela! -5 HP");
        }
        case FURIA -> {
            personaggio.setStatoPersonaggio("FURIA");
            personaggio.subisciDanno(15);
            System.out.println(" La trappola causa un danno grave! -15 HP");
        }
        case AVVELENAMENTO -> {
            personaggio.setStatoPersonaggio("AVVELENATO");
            personaggio.setTurniAvvelenato(Math.max(durata, 4));
            personaggio.subisciDanno(5);
            System.out.println(" Sei stato avvelenato! -5 HP");
        }
        case STORDIMENTO -> {
            personaggio.setStatoPersonaggio("STORDITO");
            personaggio.setTurniStordito(Math.max(durata, 2));
            personaggio.subisciDanno(5);
            System.out.println(" Sei stordito! -5 HP");
        }
        case IMMOBILIZZATO -> {
            personaggio.setStatoPersonaggio("IMMOBILIZZATO");
            personaggio.subisciDanno(5);
            System.out.println(" Sei immobilizzato! -5 HP");
        }
        case SALTA_TURNO -> {
            personaggio.aggiungiTurniDaSaltare(Math.max(durata, 1));
            System.out.println(" Un effetto ti impedisce di agire al prossimo turno!");
        }
        default -> {
            System.out.println(" La trappola non ha effetto visibile.");
        }
    }
}
    private Effetto.TipoEffetto tiraDado() {
        int dado = (int) (Math.random() * 6) + 1;

        return switch (dado) {
            case 1 ->
                Effetto.TipoEffetto.CONGELAMENTO;
            case 2 ->
                Effetto.TipoEffetto.FURIA;
            case 3 ->
                Effetto.TipoEffetto.AVVELENAMENTO;
            case 4 ->
                Effetto.TipoEffetto.STORDIMENTO;
            case 5 ->
                Effetto.TipoEffetto.IMMOBILIZZATO;
            default ->
                Effetto.TipoEffetto.NESSUN_EFFETTO;
        };
    }

    public boolean esitoDisinnesco(Trappola trappola, Personaggio personaggio) {
        // logica per calcolare esito disinnesco
        return trappola.checkDiDisinnesco(personaggio);
    }

 /*@Override
public void rimuoviEventoDaStanza(Stanza stanza, Evento evento) {
    if (stanza == null || evento == null) return;
    List<Evento> lista = stanza.getListaEventiAttivi();
    if (lista == null || lista.isEmpty()) return;

    Evento toRemove = null;
    for (Evento e : new ArrayList<>(lista)) {
        if (e == evento || (e.getId() == evento.getId())) {
            toRemove = e;
            break;
        }
    }
    if (toRemove != null) {
        lista.remove(toRemove);
        System.out.println("[DEBUG] Evento rimosso dalla stanza id=" + stanza.getId() + " eventoId=" + toRemove.getId());
    } else {
        // fallback: prova a rimuovere qualsiasi trappola con lo stesso id nella lista
        boolean removed = lista.removeIf(ev -> ev != null && ev.getId() == evento.getId());
        if (removed) {
            System.out.println("[DEBUG] Evento rimosso per id fallback stanza id=" + stanza.getId() + " eventoId=" + evento.getId());
        } else {
            System.out.println("[DEBUG] Evento non trovato in stanza id=" + stanza.getId() + " eventoId=" + evento.getId());
        }
    }
}*/


@Override
public void rimuoviEventoDaStanza(Stanza stanza, Evento evento) {
    if (stanza == null || evento == null) return;

    List<Evento> lista = stanza.getListaEventiAttivi();
    if (lista != null && !lista.isEmpty()) {
        java.util.Iterator<Evento> it = lista.iterator();
        while (it.hasNext()) {
            Evento e = it.next();
            if (e == null) continue;
            if (e == evento || e.getId() == evento.getId()) {
                if (!(evento instanceof Trappola) ||
                    ((Trappola) evento).getPosizioneCorrenteTrappola() == null ||
                    ((Trappola) evento).getPosizioneCorrenteTrappola().getId() == stanza.getId()) {
                    it.remove();
                    System.out.println("[DEBUG] Evento rimosso dalla stanza id=" + stanza.getId() + " eventoId=" + e.getId());
                    return;
                } else {
                    System.out.println("[DEBUG] Non rimuovere: posizione trappola diversa dalla stanza corrente (stanzaId="
                        + stanza.getId() + " posTrappolaId=" + ((Trappola) evento).getPosizioneCorrenteTrappola().getId() + ")");
                    return;
                }
            }
        }
    }

    // Se l'evento è una trappola con posizione interna, rimuovila solo dalla posizione dichiarata
    if (evento instanceof Trappola t && t.getPosizioneCorrenteTrappola() != null) {
        Stanza pos = t.getPosizioneCorrenteTrappola();
        List<Evento> lst2 = pos.getListaEventiAttivi();
        if (lst2 != null && !lst2.isEmpty()) {
            java.util.Iterator<Evento> it2 = lst2.iterator();
            while (it2.hasNext()) {
                Evento e2 = it2.next();
                if (e2 != null && e2.getId() == evento.getId()) {
                    it2.remove();
                    System.out.println("[DEBUG] Evento rimosso dalla posizione trappola stanza id=" + pos.getId() + " eventoId=" + evento.getId());
                    return;
                }
            }
        }
    }

    // Cerca nelle stanze adiacenti, con lo stesso controllo sulla posizione della trappola
    if (stanza.getStanzaAdiacente() != null) {
        for (Stanza sAdj : stanza.getStanzaAdiacente().values()) {
            if (sAdj == null) continue;
            List<Evento> lstAdj = sAdj.getListaEventiAttivi();
            if (lstAdj == null || lstAdj.isEmpty()) continue;
            java.util.Iterator<Evento> itAdj = lstAdj.iterator();
            while (itAdj.hasNext()) {
                Evento eAdj = itAdj.next();
                if (eAdj != null && eAdj.getId() == evento.getId()) {
                    if (!(evento instanceof Trappola) ||
                        ((Trappola) evento).getPosizioneCorrenteTrappola() == null ||
                        ((Trappola) evento).getPosizioneCorrenteTrappola().getId() == sAdj.getId()) {
                        itAdj.remove();
                        System.out.println("[DEBUG] Evento rimosso dalla stanza adiacente id=" + sAdj.getId() + " eventoId=" + evento.getId());
                        return;
                    } else {
                        System.out.println("[DEBUG] Non rimuovere dalla adiacente: posizione trappola diversa");
                        return;
                    }
                }
            }
        }
    }

    System.out.println("[DEBUG] Evento non trovato in stanza id=" + (stanza != null ? stanza.getId() : "null") + " eventoId=" + evento.getId());
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

    String descrizione =
    switch (tipoEffetto) {
        case CONGELAMENTO -> "Trappola di congelamento";
        case AVVELENAMENTO -> "Trappola di avvelenamento";
        case STORDIMENTO -> "Trappola di stordimento";
        case IMMOBILIZZATO -> "Trappola di immobilizzazione";
        case SALTA_TURNO -> "Trappola che fa saltare il turno";
        default -> "Trappola senza effetto";
    };

    Effetto effetto = new Effetto(tipoEffetto, descrizione, 0);
    Trappola trappola = new Trappola( effetto, null, id, true, false, descrizione);
    return trappola;
}

}
