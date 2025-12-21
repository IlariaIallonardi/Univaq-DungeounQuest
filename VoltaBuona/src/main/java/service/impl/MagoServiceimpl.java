package service.impl;

import domain.Combattimento;
import domain.Mago;
import domain.Mostro;
import domain.Personaggio;
import domain.Stanza;
import domain.Zaino;
import service.PersonaggioService;

public class MagoServiceimpl implements PersonaggioService {

    /**
     * Metodo per utilizzare la magia del mago
     *
     * @param mago Il mago che usa la magia
     * @param bersaglio Il personaggio bersaglio della magia
     * @param tipoMagia Il tipo di magia da utilizzare
     * @return true se la magia è stata lanciata con successo
     */
    /**
     * Usa la magia unica del mago. Comportamento: - la magia è unica (non ci
     * sono tipi): consuma sia punti mana che "punti attacco" - può colpire solo
     * i mostri
     *
     *
     */
    public final int COSTO_MANA = 10;

    @Override
    public int attacca(Personaggio personaggio, Mostro mostro, Combattimento combattimento) {

        if (!(personaggio instanceof Mago)) {
            return 1;
        }

        Mago mago = (Mago) personaggio;
        if (mago == null || mostro == null) {
            return 0;
        }
        // costi fissi della magia (adatta i valori al bilanciamento)

        // verifica risorse del mago
        if (mago.getPuntiMana() < COSTO_MANA) {
            System.out.println("Mana insufficiente per lanciare la magia.");
            return 0;
        }

        return lanciaIncantesimoBase(mago, mostro);

    }

    private int lanciaIncantesimoBase(Mago mago, Mostro mostro) {

        // Semplice formula danno: attacco + livello * 3
        int potereMagico = mago.getAttacco();
        int livello = mago.getLivello();
        int difesaMostro = mostro.getDifesaMostro();

        int dannoBase = potereMagico + livello * 3;

        // Consideriamo che la difesa del mostro riduca solo in parte il danno magico
        int dannoNetto = Math.max(1, dannoBase - (difesaMostro / 2));

        // Applica danno
        int nuoviPV = mostro.getPuntiVitaMostro() - dannoNetto;
        mostro.setPuntiVitaMostro(nuoviPV);

        // Consuma mana
        mago.setPuntiMana(Math.max(0, mago.getPuntiMana() - COSTO_MANA));

        System.out.println(" " + mago.getNomePersonaggio()
                + " lancia un incantesimo su " + mostro.getNomeMostro()
                + " infliggendo " + dannoNetto + " danni!"
                + " (Mana rimanente: " + mago.getPuntiMana() + ")");

        // Gestione morte mostro + XP
        if (mostro.getPuntiVitaMostro() <= 0) {
            System.out.println("💀 " + mostro.getNomeMostro() + " è stato sconfitto dal Mago!");
            try {
                mago.setEsperienza(mago.getEsperienza() + 10);
                if (mago.getEsperienza() >= 100) {
                    mago.setLivello(mago.getLivello() + 1);
                    mago.setEsperienza(0);
                    System.out.println(" " + mago.getNomePersonaggio() + " è salito al livello " + mago.getLivello() + "!");
                }
            } catch (Exception ignored) {
            }

        }

        return dannoNetto;
    }

    public void usaAbilitàSpeciale(Personaggio personaggio, String abilitàSpeciale) {
    }
@Override
      public Personaggio creaPersonaggio(String nome, Personaggio personaggio) {
Stanza stanza = null;
Zaino zaino = new Zaino();
return new Mago("abilità", null, 200, 300, 0, 2, nome, stanza, false, 100, 300, "normale", 0, 0, 0, 0, zaino, 50);
}

}
