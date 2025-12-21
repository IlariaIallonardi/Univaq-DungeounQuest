package service.impl;

import domain.Combattimento;
import domain.Guerriero;
import domain.Mostro;
import domain.Personaggio;
import domain.Stanza;
import domain.Zaino;
import service.PersonaggioService;

public class GuerrieroServiceImpl implements PersonaggioService {

    /**
     * Il guerriero protegge un compagno aumentandogli la difesa per un turno (o
     * finché non gestisci la durata altrove).
     */
    public boolean proteggiCompagno(Guerriero guerriero, Personaggio alleato) {
        if (guerriero == null || alleato == null) {
            return false;
        }

        // ad esempio: il guerriero deve avere almeno un po' di vita
        if (guerriero.getPuntiVita() <= 0) {
            System.out.println(" Il guerriero è troppo debole per proteggere qualcuno.");
            return false;
        }

        // puoi gestire in Personaggio un flag tipo "protetto" o aumento difesa temporaneo
        int bonusDifesa = 8;
        alleato.setDifesa(alleato.getDifesa() + bonusDifesa);
        alleato.setTurnoProtetto(alleato.getTurnoProtetto() + 1);

        System.out.println(" " + guerriero.getNomePersonaggio()
                + " si para davanti e protegge " + alleato.getNomePersonaggio()
                + " (+" + bonusDifesa + " difesa per il prossimo turno)");

        return true;
    }

    @Override
    public int attacca(Personaggio personaggio, Mostro mostro, Combattimento combattimento) {

        if (!(personaggio instanceof Guerriero guerriero)) {
            System.out.println(" Solo un Guerriero può usare questo tipo di attacco fisico potenziato.");
            return 0;
        }

        if (mostro == null) {
            System.out.println(" Nessun bersaglio valido da colpire.");
            return 0;
        }

        Stanza stanzaGuerriero = guerriero.getPosizioneCorrente();
        Stanza stanzaMostro = mostro.getPosizioneCorrenteMostro();

        if (stanzaGuerriero == null || stanzaMostro == null) {
            System.out.println(" Errore di posizione: Guerriero o Mostro non sono in una stanza valida.");
            return 0;
        }

        // Il guerriero può attaccare solo in corpo a corpo → stessa stanza
        if (!stanzaGuerriero.equals(stanzaMostro)) {
            System.out.println("⚠ Il Guerriero deve trovarsi nella stessa stanza del mostro per attaccare.");
            return 0;
        }

        return attaccoFisico(guerriero, mostro);
    }

    private int attaccoFisico(Guerriero guerriero, Mostro mostro) {

        int attacco = guerriero.getAttacco() + 5;
        int livello = guerriero.getLivello();
        int difesaMostro = mostro.getDifesaMostro();

        // base: forte fisicamente
        int dannoBase = attacco + livello * 2;

        // piccola "furia": se ha pochi HP, picco di danno
        if (guerriero.getPuntiVita() <= 10) {
            dannoBase += 5;
            System.out.println("😤 " + guerriero.getNomePersonaggio() + " entra in FURIA e colpisce più forte!");
        }

        int dannoNetto = Math.max(1, dannoBase - difesaMostro);

        int nuoviPV = mostro.getPuntiVitaMostro() - dannoNetto;
        mostro.setPuntiVitaMostro(nuoviPV);

        System.out.println("⚔ " + guerriero.getNomePersonaggio()
                + " sferra un potente colpo contro " + mostro.getNomeMostro()
                + " infliggendo " + dannoNetto + " danni!");

        if (mostro.getPuntiVitaMostro() <= 0) {
            System.out.println("💀 " + mostro.getNomeMostro() + " è stato sconfitto dal Guerriero!");
            try {
                guerriero.setEsperienza(guerriero.getEsperienza() + 12);
                if (guerriero.getEsperienza() >= 100) {
                    guerriero.setLivello(guerriero.getLivello() + 1);
                    guerriero.setEsperienza(0);
                    System.out.println(" " + guerriero.getNomePersonaggio() + " è salito al livello " + guerriero.getLivello() + "!");
                }
            } catch (Exception ignored) {
            }

        }
        return dannoNetto;
    }

    public Personaggio creaPersonaggio(String nome, Personaggio personaggio) {
Stanza stanza = null;
Zaino zaino = new Zaino();
return new Guerriero("abilità", null, 200, 300, 0, 2, nome, stanza, false, 100, 300, "normale", 0, 0, 0, 0, zaino, 50);
}
    
    public void usaAbilitàSpeciale(Personaggio personaggio, String abilitàSpeciale) {
    }

}
