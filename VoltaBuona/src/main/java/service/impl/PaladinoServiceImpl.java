package service.impl;


import domain.Paladino;
import domain.Personaggio;
import service.PersonaggioService;

public class PaladinoServiceImpl extends PersonaggioService {
    
    /**
     * Metodo per proteggere un altro giocatore
     * @param paladino Il paladino che usa la protezione
     * @param bersaglio Il personaggio da proteggere
     * @return true se la protezione è stata applicata con successo
     */
    public boolean protezioneGiocatoreP(Paladino paladino, Personaggio bersaglio) {
        if (paladino == null || bersaglio == null) {
            return false;
        }

        // Verifica se il paladino ha abbastanza punti mana
        if (paladino.getPuntiMana() < 5) {
            return false;
        }

        // Applica bonus di difesa al bersaglio
        bersaglio.setDifesa(bersaglio.getDifesa() + 10);
        paladino.setPuntiMana(paladino.getPuntiMana() - 5);

        return true;
    }

    /**
     * Metodo per utilizzare la magia sacra del paladino
     * @param paladino Il paladino che usa la magia
     * @param bersaglio Il personaggio bersaglio
     * @param tipoMagiaSacra Il tipo di magia da utilizzare
     * @return true se la magia è stata lanciata con successo
     */
    public boolean usareMagiaP(Paladino paladino, Personaggio bersaglio, TipoMagiaSacra tipoMagiaSacra) {
        if (paladino == null || bersaglio == null || tipoMagiaSacra == null) {
            return false;
        }

        if (paladino.getPuntiMana() < tipoMagiaSacra.getCostoMana()) {
            return false;
        }

        switch (tipoMagiaSacra) {
            case BENEDIZIONE:
                bersaglio.setPuntiVita(bersaglio.getPuntiVita() + 15);
                bersaglio.setDifesa(bersaglio.getDifesa() + 3);
                break;
            case PURIFICAZIONE:
                bersaglio.setPuntiVita(bersaglio.getPuntiVita() + 25);
                break;
            case CONSACRAZIONE:
                bersaglio.setAttacco(bersaglio.getAttacco() + 8);
                bersaglio.setDifesa(bersaglio.getDifesa() + 5);
                break;
            default:
                return false;
        }

        paladino.setPuntiMana(paladino.getPuntiMana() - tipoMagiaSacra.getCostoMana());
        return true;
    }

    // Enum per i tipi di magia sacra disponibili
    public enum TipoMagiaSacra {
        BENEDIZIONE(8),
        PURIFICAZIONE(12),
        CONSACRAZIONE(15);

        private final int costoMana;

        TipoMagiaSacra(int costoMana) {
            this.costoMana = costoMana;
        }

        public int getCostoMana() {
            return costoMana;
        }
    }
}