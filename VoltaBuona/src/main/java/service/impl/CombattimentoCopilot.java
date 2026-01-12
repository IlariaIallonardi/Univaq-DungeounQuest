package service.impl;

import java.util.Random;
import domain.Combattimento;
import domain.Evento;
import domain.Mostro;
import domain.Personaggio;
import domain.Stanza;
import service.CombattimentoService;
import service.PersonaggioService;

public class CombattimentoCopilot implements CombattimentoService {

    private final MostroServiceImpl mostroService = new MostroServiceImpl();
    private PersonaggioService personaggioService; // opzionale injection
    private final Random rng = new Random();

    // Helper per test/creazione
    public Combattimento creaCombattimento(Personaggio p, Mostro m, Stanza s) {
        if (p == null || m == null) return null;
        Evento ev = new Evento(0, true, false, "Incontro con " + m.getNomeMostro(), "Combattimento", s);
        return new Combattimento(null, 0, ev, 0, false, p, s, 0, null, m);
    }

    // Avvia il loop di combattimento (semplice, sincrono)
    @Override
    public Object iniziaCombattimento(Personaggio p, Mostro m, Stanza s) {
        if (p == null || m == null) return null;
        Combattimento c = creaCombattimento(p, m, s);
        c.setInCorso(true);
        c.setIniziativa(rng.nextInt(2)); // 0 mostro, 1 personaggio
        while (c.isInCorso()) {
            int init = c.getIniziativa();
            if (init == 0) applicaECalcolaDanno(c, m);
            else applicaECalcolaDanno(c, p);
            // alterna turno
            c.setIniziativa(1 - c.getIniziativa());
        }
        return c.getVincitore();
    }

    // Applica danno a seconda dell'attaccante
    @Override
    public int applicaECalcolaDanno(Combattimento c, Object attaccante) {
        if (c == null || attaccante == null) return 0;
        Personaggio p = c.getPersonaggioCoinvolto();
        Mostro m = c.getMostroCoinvolto();
        if (attaccante == m) {
            int base = MostroServiceImpl.dannoBase(m, p);
            int dmg = mostroService.attaccoDelMostro(m, p, base);
            if (p.getPuntiVita() <= 0) {
                c.setVincitore(m); c.setInCorso(false);
            }
            return dmg;
        } else {
            PersonaggioService ps = resolveService(p);
            int dmg = ps.attacca(p, m, c);
            if (m.getPuntiVitaMostro() <= 0) {
                c.setVincitore(p); c.setInCorso(false);
            }
            return dmg;
        }
    }

    @Override
    public boolean terminaCombattimento(Combattimento c) {
        if (c == null || !c.isInCorso()) return false;
        c.setInCorso(false);
        // pulizia minimale: rimuovi evento dalla stanza se necessario
        if (c.getStanza() != null && c.getEventoMostro() != null && !c.getEventoMostro().èRiutilizzabile()) {
            c.getStanza().getListaEventiAttivi().remove(c.getEventoMostro());
        }
        return true;
    }

    @Override
    public boolean èInCorso(Combattimento c) {
        return c != null && c.isInCorso();
    }

    // Helper per risolvere il service del personaggio (evita new sparsi ovunque)
    private PersonaggioService resolveService(Personaggio p) {
        if (this.personaggioService != null) return this.personaggioService;
        if (p instanceof domain.Arciere) return new ArciereServiceImpl();
        if (p instanceof domain.Mago) return new MagoServiceImpl();
        if (p instanceof domain.Paladino) return new PaladinoServiceImpl();
        return new GuerrieroServiceImpl();
    }

}