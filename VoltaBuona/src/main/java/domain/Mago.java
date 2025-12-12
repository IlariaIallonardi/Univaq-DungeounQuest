package domain;

public class Mago extends Personaggio {

    public Mago(String AbilitàSpeciale, Arma armaEquippaggiata, int difesa, int esperienza, int id, int livello, String nomePersonaggio, Stanza posizioneCorrente, boolean protetto, int puntiMana, int puntiVita, String statoPersonaggio, int turniAvvelenato, int turniCongelato, int turniStordito, int turnoProtetto, Zaino zaino) {
        super(AbilitàSpeciale, armaEquippaggiata, difesa, esperienza, id, livello, nomePersonaggio, posizioneCorrente, protetto, puntiMana, puntiVita, statoPersonaggio, turniAvvelenato, turniCongelato, turniStordito, turnoProtetto, zaino);

    }

    @Override
    public void setAbilitàSpeciale(String abilitàSpeciale) {

    }

    @Override
    public boolean puoEquipaggiare(Arma.TipoArma tipo) {
        if (tipo == null) {
            return false;
        }

        return tipo == Arma.TipoArma.BACCHETTA_MAGICA;
    }

}
