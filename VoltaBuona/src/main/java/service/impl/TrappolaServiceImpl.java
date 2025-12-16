package service.impl;

import domain.Effetto;
import domain.Evento;
import domain.Personaggio;
import domain.Stanza;
import domain.Trappola;
import service.EventoService;


public class TrappolaServiceImpl implements EventoService {

    @Override
    public boolean attivaEvento(Personaggio personaggio, Evento e) {
        if (e instanceof Trappola) {
            Stanza stanza = ((Trappola) e).getPosizioneCorrenteTrappola();
            if (e != null) {
                System.out.println("Hai trovato una trappola!");

                // Check di disinnesco
                boolean disinnescata = ((Trappola) e).checkDiDisinnesco(personaggio);

                if (!disinnescata) {
                    // Trappola si attiva
                    attiva(personaggio);
                    rimuoviEventoDaStanza(stanza, e);
                    // l'attivazione della trappola consuma il turno
                    return true;
                } else {
                    System.out.println("La trappola è stata disinnescata.");
                    rimuoviEventoDaStanza(stanza, e);
                    return false;
                }
            }
        }
        return false;
    }

    public void attiva(Personaggio personaggio) {
        Effetto.TipoEffetto effetto = tiraDado();

        switch (effetto) {

            case CONGELAMENTO -> {
                personaggio.setStatoPersonaggio("Congelato");
                personaggio.setTurniCongelato(3);
                personaggio.subisciDanno(5);
                System.out.println(" La trappola ti ferisce leggermente! -5 HP");
                break;
            }

            case FURIA -> {
                personaggio.setStatoPersonaggio("Furia");
                personaggio.subisciDanno(15);
                System.out.println(" La trappola causa un danno grave! -15 HP");
                break;
            }

            case AVVELENAMENTO -> {
                personaggio.setStatoPersonaggio("AVVELENATO");
                personaggio.setTurniAvvelenato(4);
                personaggio.subisciDanno(5);
                System.out.println(" Sei stato avvelenato!-5 HP");
                break;
            }

            case STORDIMENTO -> {
                personaggio.setStatoPersonaggio("STORDITO");
                personaggio.setTurniStordito(2);
                personaggio.subisciDanno(5);
                System.out.println(" Sei stordito!-5 HP");
                break;
            }

            case IMMOBILIZZATO -> {
                personaggio.setStatoPersonaggio("IMMOBILIZZATO");
                personaggio.subisciDanno(5);
                System.out.println(" Sei immobilizzato!-5 HP");
                break;
            }

            case SALTA_TURNO -> {
                // Imposta il personaggio per saltare il prossimo turno
                personaggio.aggiungiTurniDaSaltare(1);
                System.out.println("Un effetto ti impedisce di agire al prossimo turno!");
                break;
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

    @Override
    public void rimuoviEventoDaStanza(Stanza stanza, Evento evento) {
        stanza.getListaEventiAttivi().remove(evento);
    }

    ;

    @Override
    public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza) {
        for (Evento e : stanza.getListaEventiAttivi()) {
            boolean termina = attivaEvento(personaggio, e);
            if (termina) return;

        }
    }
}
