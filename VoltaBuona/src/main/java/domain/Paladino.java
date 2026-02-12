package domain;

public class Paladino extends Personaggio {

    private TipoMagiaSacra magiaSelezionata;

    public Paladino(Arma armaEquippaggiata, int difesa, int esperienza, int id, int livello, String nomePersonaggio, Stanza posizioneCorrente, boolean protetto, int puntiMana, int puntiVita, String statoPersonaggio, int turniAvvelenato, int turniCongelato, int turniStordito, int turnoProtetto, Zaino zaino, int portafoglioPersonaggio, TipoMagiaSacra magiaSelezionata) {
        super(armaEquippaggiata, difesa, esperienza, id, livello, nomePersonaggio, posizioneCorrente, protetto, puntiMana, puntiVita, statoPersonaggio, turniAvvelenato, turniCongelato, turniStordito, turnoProtetto, zaino, portafoglioPersonaggio);
        this.magiaSelezionata = magiaSelezionata;
    }

    public TipoMagiaSacra getMagiaSelezionata() {
        return magiaSelezionata;
    }

    public void setMagiaSelezionata(TipoMagiaSacra magiaSelezionata) {
        this.magiaSelezionata = magiaSelezionata;
    }

    public enum TipoMagiaSacra {
        RUBAVITA(4),
        AMMALIAMENTO(5),
        MALATTIA(7);

        private final int costoMana;

        TipoMagiaSacra(int costoMana) {
            this.costoMana = costoMana;
        }

        public int getCostoMana() {
            return costoMana;
        }
    }

    @Override
    public boolean puoRaccogliere(Arma.TipoArma tipo) {

        return tipo == Arma.TipoArma.SPADA || tipo == Arma.TipoArma.BACCHETTA_MAGICA;
    }
}
