package service;

import java.lang.reflect.Method;
import java.util.Random;

import domain.Arciere;
import domain.Guerriero;
import domain.Mago;
import domain.Paladino;
import domain.Personaggio;

public class GiocatoreService {

    public Personaggio attribuisciPersonaggioAComputer(Personaggio personaggio) {
        Random rnd = new Random();
        int scelta = rnd.nextInt(4);

        Personaggio bot;
        switch (scelta) {
            case 0:
                bot = new Guerriero("Bot-Guerriero");
                break;
            case 1:
                bot = new Mago("Bot-Mago");
                break;
            case 2:
                bot = new Arciere("Bot-Arciere");
                break;
            case 3:
                bot = new Paladino("Bot-Paladino");
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
