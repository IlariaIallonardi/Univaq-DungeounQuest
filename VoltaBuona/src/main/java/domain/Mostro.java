package domain;

public class Mostro extends PersonaIncontrata {
    
    public Mostro(int danno, String messaggio, String nome) {
        super(danno, messaggio, nome);
    }


    public Boolean alterareStato(){
        return true;
    }
}
