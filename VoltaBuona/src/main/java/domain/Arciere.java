package domain;

public class Arciere extends Personaggio {

    public Arciere(int attacco, int difesa, int esperienza, int id, int livello, String nomePersonaggio, Stanza posizioneCorrente, int puntiMana, int puntiVita, String statoPersonaggio, Zaino zaino) {
        super(attacco, difesa, esperienza, id, livello, nomePersonaggio, posizioneCorrente, puntiMana, puntiVita, statoPersonaggio, zaino);
    }

    @Override
    public boolean puoEquipaggiare(Arma.TipoArma tipo) {
        if(tipo == null) 
            return false;
        
        return tipo == Arma.TipoArma.FRECCIA_E_ARCO;
    }
 
    



    
}
