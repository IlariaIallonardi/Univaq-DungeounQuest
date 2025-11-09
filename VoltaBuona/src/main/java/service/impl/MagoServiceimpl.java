package service.impl;

import domain.Mago;
import domain.Personaggio;
import service.GiocatoreService;

public class MagoServiceimpl extends GiocatoreService {
    
    /**
     * Metodo per utilizzare la magia del mago
     * @param mago Il mago che usa la magia
     * @param bersaglio Il personaggio bersaglio della magia
     * @param tipoMagia Il tipo di magia da utilizzare
     * @return true se la magia è stata lanciata con successo
     */
    public boolean usareMagia(Mago mago, Personaggio bersaglio, TipoMagia tipoMagia) {
        // Controlli di validità
        if (mago == null || bersaglio == null || tipoMagia == null) {
            return false;
        }

        // Verifica se il mago ha abbastanza mana
        if (mago.getPuntiMana() < tipoMagia.getCostoMana()) {
            return false;
        }

        // Applica l'effetto della magia
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
        }

        public int getCostoMana() {
            return costoMana;
        }
    }
}