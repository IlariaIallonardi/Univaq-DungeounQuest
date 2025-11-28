package domain;

public class PersonaIncontrata extends Evento {
    private String nome;
    



    public PersonaIncontrata(int id, boolean inizioEvento, boolean fineEvento, String descrizione, String nome) {
        super(id, inizioEvento, fineEvento, descrizione);

        this.nome = nome;
    }
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }


    
}
