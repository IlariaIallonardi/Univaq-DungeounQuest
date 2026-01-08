package domain;

public class Paladino extends Personaggio {

    public Paladino(String abilitàSpeciale, Arma armaEquippaggiata, int difesa, int esperienza, int id, int livello, String nomePersonaggio, Stanza posizioneCorrente, boolean protetto, int puntiMana, int puntiVita, String statoPersonaggio, int turniAvvelenato, int turniCongelato, int turniStordito, int turnoProtetto, Zaino zaino, int portafoglioPersonaggio) {
        super(abilitàSpeciale, armaEquippaggiata, difesa, esperienza, id, livello, nomePersonaggio, posizioneCorrente, protetto, puntiMana, puntiVita, statoPersonaggio, turniAvvelenato, turniCongelato, turniStordito, turnoProtetto, zaino, portafoglioPersonaggio);
    }


    @Override
    public void setAbilitàSpeciale(String abilitàSpeciale) {

    }



    @Override
    public boolean puoRaccogliere(Arma.TipoArma tipo) {
        if (tipo == null) {
            return false;
        }

        return tipo == Arma.TipoArma.SPADA;
    }
}
