package service.impl;


import domain.Guerriero;
import domain.Personaggio;
import service.PersonaggioService;

public class GuerrieroServiceImpl extends PersonaggioService {
    
    /**
     * Metodo per proteggere un altro giocatore usando la forza del guerriero
     * @param guerriero Il guerriero che protegge
     * @param bersaglio Il personaggio da proteggere
     * @return true se la protezione è stata applicata con successo
     */
    public boolean protezioneGiocatoreG(Guerriero guerriero, Personaggio bersaglio) {
        if (guerriero == null || bersaglio == null) {
            return false;
        }

        // Verifica se il guerriero ha abbastanza punti vita
        if (guerriero.getPuntiVita() < 20) {
            return false;
        }

        // Il guerriero sacrifica parte dei suoi punti vita per aumentare la difesa del bersaglio
        int sacrificioVita = 10;
        guerriero.setPuntiVita(guerriero.getPuntiVita() - sacrificioVita);
        
        // Bonus di difesa basato sulla forza del guerriero
        int bonusDifesa = guerriero.getAttacco() / 2;
        bersaglio.setDifesa(bersaglio.getDifesa() + bonusDifesa);

        return true;
    }

    /**
     * Enum per le posizioni di combattimento del guerriero
     */
    public enum PosizioneCombattimento {
        OFFENSIVA(2, -1),    // più attacco, meno difesa
        DIFENSIVA(-1, 2),    // più difesa, meno attacco
        BILANCIATA(1, 1);    // bonus bilanciato

        private final int modificatoreAttacco;
        private final int modificatoreDifesa;

        PosizioneCombattimento(int modAttacco, int modDifesa) {
            this.modificatoreAttacco = modAttacco;
            this.modificatoreDifesa = modDifesa;
        }

        public int getModificatoreAttacco() {
            return modificatoreAttacco;
        }

        public int getModificatoreDifesa() {
            return modificatoreDifesa;
        }
    }
}