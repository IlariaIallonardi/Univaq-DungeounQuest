package service.impl;


import domain.Combattimento;
import domain.Mago;
import domain.Mostro;
import domain.Oggetto;
import domain.Personaggio;
import domain.Trappola;
import service.PersonaggioService;

public class MagoServiceimpl implements  PersonaggioService {
    
    /**
     * Metodo per utilizzare la magia del mago
     * @param mago Il mago che usa la magia
     * @param bersaglio Il personaggio bersaglio della magia
     * @param tipoMagia Il tipo di magia da utilizzare
     * @return true se la magia è stata lanciata con successo
     */
     /**
     * Usa la magia unica del mago.
     * Comportamento:
     *  - la magia è unica (non ci sono tipi): consuma sia punti mana che "punti attacco"
     *  - può colpire solo i mostri
     *
     * 
     */
    @Override
    public int attacca(Personaggio personaggio,Mostro bersaglio,Combattimento combattimento) {
        
        if (!(personaggio instanceof Mago)) {
        return 1;
    }

    Mago mago = (Mago) personaggio;
    if (mago == null || bersaglio == null) return 0;
        // costi fissi della magia (adatta i valori al bilanciamento)
        final int COSTO_MANA = 10;
        final int COSTO_ATTACCO = 5;

        // verifica che il bersaglio sia un mostro
        if (!(bersaglio instanceof Mostro)) {
            return 0;
        }
        Mostro m = (Mostro) bersaglio;

        // verifica risorse del mago
        if (mago.getPuntiMana() < COSTO_MANA) return 0;
        if (mago.getAttacco() < COSTO_ATTACCO) return 0;

        // consumo risorse: non so se va qui e poi non credo con questa formula matematica
        mago.setPuntiMana(Math.max(0, mago.getPuntiMana() - COSTO_MANA));
        mago.setAttacco(Math.max(0, mago.getAttacco() - COSTO_ATTACCO));

        // calcolo danno questo lo dobbiamo decidere noi: esempio semplice basato sull'attacco residuo + livello
        int danno = Math.max(0, mago.getAttacco() + mago.getLivello() * 2);
        return danno;
    }

    @Override
    public Personaggio creaPersonaggio(String nome, Personaggio personaggio) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean usaOggetto(Personaggio personaggio, Oggetto oggetto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean raccogliereOggetto(Personaggio personaggio, Oggetto oggetto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int subisciDannoDaMostro(Mostro.TipoAttaccoMostro attaccoMostro, int dannoBase, Personaggio personaggio) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void subisciDannoDaTrappola(Trappola trappola, Personaggio personaggio) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void esploraStanza(Personaggio personaggio) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
        /*/ applica difesa del mostro se presente...stessa cosa qua dell'operazione matematica
        int difesaMostro = 0;
        try { difesaMostro = m.getDifesaMostro(); } catch (Exception ignored) { }
        int dannoNetto = Math.max(0, danno - difesaMostro);

        // sottrai punti vita al mostro
        try {
            m.setPuntiVitaMostro(m.getPuntiVitaMostro() - dannoNetto);
        } catch (Exception ignored) { }

        System.out.println(mago.getNomeP() + " lancia la magia e infligge " + dannoNetto + " al mostro " + m.getNome());

        // eventuale gestione morte del mostro (se PV <= 0)
        if (m.getPuntiVitaMostro() <= 0) {
            System.out.println("Il mostro " + m.getNome() + " è stato sconfitto.");
            // qui puoi aggiungere rimozione dal contesto/stanza, assegnazione XP, loot, ecc.
        }

        return true;
    }*/

      /*   // Applica l'effetto della magia
        switch (tipoMagia) {
            case FULMINE:
                bersaglio.setPuntiVita(bersaglio.getPuntiVita() - 20);
                mago.setPuntiMana(mago.getPuntiMana() - 10);
                break;
            case CURA:
                bersaglio.setPuntiVita(Math.min(bersaglio.getPuntiVita() + 15, 100));
                mago.setPuntiMana(mago.getPuntiMana() - 8);
                break;
            case SCUDO:
                bersaglio.setDifesa(bersaglio.getDifesa() + 5);
                mago.setPuntiMana(mago.getPuntiMana() - 5);
                break;
            default:
                return false;
        }

        return true;
    }

    // Enum per i tipi di magia disponibili
    public enum TipoMagia {
        FULMINE(10),
        CURA(8),
        SCUDO(5);

        private final int costoMana;

        TipoMagia(int costoMana) {
            this.costoMana = costoMana;
        } */

        
