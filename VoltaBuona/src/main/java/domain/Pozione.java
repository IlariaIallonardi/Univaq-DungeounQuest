package domain;


public class Pozione extends Oggetto {


    private int valoreBonus;
    private TipoPozione tipoPozione;

 public Pozione(TipoPozione tipoPozione, int id, String nome, String descrizione, boolean usabile, boolean equipaggiabile, boolean trovato,int valoreBonus) {
                super(id, nome, descrizione, usabile, equipaggiabile, trovato);
            
                this.tipoPozione = tipoPozione;
                this.valoreBonus = valoreBonus;
            
            }

    
    public enum TipoPozione {
        CURA(25),
        MANA(30),
        ANTIDOTO(20);

          private final int valoreBonus;
        private TipoPozione tipoPozione;

        TipoPozione(int valoreBonus) {
            this.valoreBonus = valoreBonus;
        }

        public int getValoreBonus() {
            return valoreBonus;
        }

    }



   

            

   

    @Override
    public boolean eseguiEffetto(Personaggio personaggio) {
        if (personaggio == null) {
            return false;
        }

        switch (tipoPozione) {
            case CURA:
                personaggio.setPuntiVita(personaggio.getPuntiVita() + tipoPozione.getValoreBonus());
                break;
            case MANA:
                personaggio.setPuntiMana(personaggio.getPuntiMana() + tipoPozione.getValoreBonus());
                break;
            case ANTIDOTO:
                personaggio.setPuntiVita(personaggio.getPuntiVita() + tipoPozione.getValoreBonus());
                personaggio.setStatoPersonaggio("NORMALE");
                // azzera eventuali contatori (se usi turni)
                personaggio.setTurniAvvelenato(0);

                break;
            default:
                return false;
        }
        return true;
    }

}
