package service;

import java.lang.reflect.Method;
import java.util.Random;

import domain.Personaggio;
import domain.TipoGiocatore;
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
        }

        // converte il Personaggio in una vera istanza domain.Computer
        domain.Computer c = new domain.Computer(
            TipoGiocatore.COMPUTER,
            botBase.getAbilitàSpeciale(),
            botBase.getArmaEquippaggiata(),
            botBase.getDifesa(),
            botBase.getEsperienza(),
            botBase.getId(),
            botBase.getLivello(),
            botBase.getNomePersonaggio(),
            botBase.getPosizioneCorrente(),
            false, // protetto
            botBase.getPuntiMana(),
            botBase.getPuntiVita(),
            botBase.getStatoPersonaggio(),
            botBase.getTurniAvvelenato(),
            botBase.getTurniCongelato(),
            botBase.getTurniStordito(),
            botBase.getTurnoProtetto(),
            botBase.getZaino(),
            botBase.getPortafoglioPersonaggio()
        );

        System.out.println("🤖 Il computer giocherà come: " + botBase.getClass().getSimpleName() + " (nome: " + c.getNomePersonaggio() + ")");

        return c;
    }

}