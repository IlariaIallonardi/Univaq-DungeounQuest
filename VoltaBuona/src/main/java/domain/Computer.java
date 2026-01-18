package domain;

public class Computer extends Giocatore {

    public Computer(TipoGiocatore tipo, String abilitàSpeciale, Arma armaEquippaggiata, int difesa, int esperienza, int id, int livello, String nomePersonaggio, Stanza posizioneCorrente, boolean protetto, int puntiMana, int puntiVita, String statoPersonaggio, int turniAvvelenato, int turniCongelato, int turniStordito, int turnoProtetto, Zaino zaino, int portafoglioPersonaggio) {
        super(tipo, abilitàSpeciale, armaEquippaggiata, difesa, esperienza, id, livello, nomePersonaggio, posizioneCorrente, protetto, puntiMana, puntiVita, statoPersonaggio, turniAvvelenato, turniCongelato, turniStordito, turnoProtetto, zaino, portafoglioPersonaggio);
    }

}
