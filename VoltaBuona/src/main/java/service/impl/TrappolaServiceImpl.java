package service.impl;

import domain.Effetto;
import domain.Evento;
import domain.Personaggio;
import domain.Stanza;
import domain.Trappola;
import service.EventoService;



/// DA VEDERE COME LA DOBBIAMO GESTIRE 
public class TrappolaServiceImpl implements  EventoService {

    public void subisciDannoDaTrappola(Trappola trappola, Personaggio personaggio) {
        if (trappola != null) {
            System.out.println("Hai trovato una trappola!");

            // Check di disinnesco
            boolean disinnescata = trappola.checkDiDisinnesco(personaggio);

            if (!disinnescata) {
                // Trappola si attiva
                attiva(personaggio);
            } else {
                System.out.println("La trappola è stata disinnescata.");
            }
        }
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

   
    public boolean  esitoDisinnesco(Trappola trappola, Personaggio personaggio) {
        // logica per calcolare esito disinnesco
        return trappola.checkDiDisinnesco(personaggio);
    }  
    @Override
    public void rimuoviEventoDaStanza(Stanza stanza, Evento evento){
        stanza.getListaEventiAttivi().remove(evento);
    };

    @Override
    public void attivaEvento(Personaggio personaggio, Evento e){

    };

    @Override   
    public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza){   
        for (Evento e : stanza.getListaEventiAttivi()) {
            attivaEvento(personaggio, e);
        }
    } 
}