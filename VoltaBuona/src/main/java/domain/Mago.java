package domain;

public class Mago extends Personaggio {

  

    public Mago(int attacco, int difesa, int esperienza, int id, int livello, String nomePersonaggio, Stanza posizioneCorrente, int puntiMana, int puntiVita, String statoPersonaggio, Zaino zaino) {
        super(attacco, difesa, esperienza, id, livello, nomePersonaggio, posizioneCorrente, puntiMana, puntiVita, statoPersonaggio, zaino);
    }

      @Override
    public boolean puoEquipaggiare(Arma.TipoArma tipo) {
        if(tipo == null) 
            return false;
        
        return tipo == Arma.TipoArma.BACCHETTA_MAGICA;
    }

    

    

}
