package service;

import java.lang.reflect.Method;
import java.util.Random;

import domain.Computer;
import domain.Personaggio;
import service.impl.ArciereServiceImpl;
import service.impl.GuerrieroServiceImpl;
import service.impl.MagoServiceImpl;
import service.impl.PaladinoServiceImpl;

public class GiocatoreService {

    public Personaggio attribuisciPersonaggioAComputer(Personaggio personaggio) {
        // genera il personaggio "base" (classe scelta casuale)
        Random rnd = new Random();
        int scelta = rnd.nextInt(4);

        Personaggio botBase;
        switch (scelta) {
            case 0:
                botBase = new GuerrieroServiceImpl().creaPersonaggio(personaggio.getNomePersonaggio(), personaggio);
                break;
            case 1:
                botBase = new MagoServiceImpl().creaPersonaggio(personaggio.getNomePersonaggio(), personaggio);
                break;
            case 2:
                botBase = new ArciereServiceImpl().creaPersonaggio(personaggio.getNomePersonaggio(), personaggio);
                break;
            case 3:
                botBase = new PaladinoServiceImpl().creaPersonaggio(personaggio.getNomePersonaggio(), personaggio);
                break;
            default:
                botBase = new GuerrieroServiceImpl().creaPersonaggio(personaggio.getNomePersonaggio(), personaggio);
        }

        // tenta di impostare un nome descrittivo se la classe lo supporta
        try {
            Method setter = botBase.getClass().getMethod("setNome", String.class);
            setter.invoke(botBase, "Bot-" + botBase.getClass().getSimpleName());
        } catch (Exception ignored) {
            return null;
        }

        // converte il Personaggio in una vera istanza domain.Computer
        domain.Computer c = new Computer(null, null, null, scelta, scelta, scelta, scelta, null, null, false, scelta, scelta, null, scelta, scelta, scelta, scelta, null, scelta);

        System.out.println("🤖 Il computer giocherà come: " + botBase.getClass().getSimpleName() + " (nome: " + c.getNomePersonaggio() + ")");

        return c;
    }
}
