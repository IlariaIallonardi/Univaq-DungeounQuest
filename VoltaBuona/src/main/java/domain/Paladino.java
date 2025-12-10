package domain;

public class Paladino extends Personaggio {

     public Paladino(int attacco, int difesa, int esperienza, int id, int livello, String nomePersonaggio, Stanza posizioneCorrente, int puntiMana, int puntiVita, String statoPersonaggio, Zaino zaino) {
        super(attacco, difesa, esperienza, id, livello, nomePersonaggio, posizioneCorrente, puntiMana, puntiVita, statoPersonaggio, zaino);
    }

    
     public boolean proteggi(Personaggio target) {
        if (target == null) return false;
        // esempio di controllo: richiedi che il guerriero abbia almeno 1 PV
        if (this.getPuntiVita() <= 0) return false;
        target.applicaProtezione();
        return true;
    }

      @Override
    public boolean puoEquipaggiare(Arma.TipoArma tipo) {
        if(tipo == null) 
            return false;
        
        return tipo == Arma.TipoArma.SPADA;
    }
}
