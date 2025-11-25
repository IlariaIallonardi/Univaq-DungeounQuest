package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import domain.Giocatore;
import domain.Gioco;
import domain.Personaggio;

public class TurnoService {

    private List<Personaggio> ordineTurno = new ArrayList<>();
    private int indiceCorrente = 0;
    private PersonaggioService personaggioService;

    public void inizializzaTurno(List<Personaggio> personaggio) {
        ordineTurno.clear();
        ordineTurno.addAll(personaggio);
        Collections.shuffle(ordineTurno); // ordine casuale
        indiceCorrente = 0;
    }

    public Personaggio getTurnoCorrente() {
        return ordineTurno.isEmpty() ? null : ordineTurno.get(indiceCorrente);
    }

    public void passaProssimoTurno() {
        if (!ordineTurno.isEmpty()) {
            indiceCorrente = (indiceCorrente + 1) % ordineTurno.size();
        }
    }

    public List<Personaggio> getOrdineTurni() {
        return new ArrayList<>(ordineTurno);
    }

    public TurnoService(PersonaggioService ps) {
        this.personaggioService = ps;
    }

    public void terminaTurnoCorrente(Personaggio personaggio) {

        for (Personaggio p : ordineTurno) {
            if (!p.èMorto(p)) {
                aggiornaEffettiFineTurno(personaggio);
            }
        }
    }

    public void passaProssimoGiocatore(Personaggio personaggio) {
        passaProssimoTurno();
    }

    public void aggiornaEffettiFineTurno(Personaggio personaggio) {

        if (personaggio.getTurniAvvelenato() > 0) {
            int dannoVeleno = 3; // puoi cambiare
            personaggio.subisciDanno(dannoVeleno);
            personaggio.setTurniAvvelenato(personaggio.getTurniAvvelenato() - 1);
            System.out.println("Il veleno infligge " + dannoVeleno + " danni a " + personaggio.getNomeP());

            if (personaggio.getTurniAvvelenato() == 0 && "AVVELENATO".equalsIgnoreCase(personaggio.getStatoPersonaggio())) {
                personaggio.setStatoPersonaggio("NORMALE");
                System.out.println("Il veleno ha perso effetto su " + personaggio.getNomeP());
            }
        }

        // 2️⃣ CONGELAMENTO: dura N turni, qui puoi gestire eventuali penalità
        if (personaggio.getTurniCongelato() > 0) {
            personaggio.setTurniCongelato(personaggio.getTurniCongelato() - 1);
            System.out.println("❄ " + personaggio.getNomeP() + " è ancora congelato ("
                    + personaggio.getTurniCongelato() + " turni rimanenti)");

            if (personaggio.getTurniCongelato() == 0 && "CONGELATO".equalsIgnoreCase(personaggio.getStatoPersonaggio())) {
                personaggio.setStatoPersonaggio("NORMALE");
                System.out.println(personaggio.getNomeP() + " si è scongelato.");
            }
        }

        // 3️⃣ STORDIMENTO: penalizza per N turni (es. non può attaccare)
        if (personaggio.getTurniStordito() > 0) {
            personaggio.setTurniStordito(personaggio.getTurniStordito() - 1);
            System.out.println(personaggio.getNomeP() + " è ancora stordito ("
                    + personaggio.getTurniStordito() + " turni rimanenti)");

            if (personaggio.getTurniStordito() == 0 && "STORDITO".equalsIgnoreCase(personaggio.getStatoPersonaggio())) {
                personaggio.setStatoPersonaggio("NORMALE");
                System.out.println(personaggio.getNomeP() + " non è più stordito.");
            }
        }
    }
}
