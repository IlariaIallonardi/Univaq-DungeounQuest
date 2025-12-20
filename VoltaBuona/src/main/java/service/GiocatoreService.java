package service;

import java.lang.reflect.Method;
import java.util.Random;

import domain.Personaggio;
import service.impl.ArciereServiceImpl;
import service.impl.GuerrieroServiceImpl;
import service.impl.MagoServiceimpl;
import service.impl.PaladinoServiceImpl;

public class GiocatoreService {

    public Personaggio attribuisciPersonaggioAComputer(Personaggio personaggio) {
        Random rnd = new Random();
        int scelta = rnd.nextInt(4);

        Personaggio bot;
        switch (scelta) {
            case 0:
                bot = new GuerrieroServiceImpl().creaPersonaggio("Bot-Guerriero", personaggio);
                break;
            case 1:
                bot = new MagoServiceimpl().creaPersonaggio("Bot-Mago", personaggio);
                break;
            case 2:
                bot = new ArciereServiceImpl().creaPersonaggio("Bot-Arciere", personaggio);
                break;
            case 3:
                bot = new PaladinoServiceImpl().creaPersonaggio("Bot-Paladino", personaggio);
                break;
            default:
                throw new IllegalStateException("Errore nella generazione del bot");
        }

        // tenta di impostare un nome descrittivo se la classe lo supporta
        try {
            Method setter = bot.getClass().getMethod("setNome", String.class);
            setter.invoke(bot, "Bot-" + bot.getClass().getSimpleName());
        } catch (Exception ignored) {
            // se non esiste setNome, ignora
        }

        System.out.println("🤖 Il computer giocherà come: " + bot.getClass().getSimpleName());
        return bot;
    }

}
