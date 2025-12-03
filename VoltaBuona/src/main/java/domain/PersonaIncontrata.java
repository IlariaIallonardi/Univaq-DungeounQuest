package domain;

public class PersonaIncontrata extends Evento {
    private String nomePersonaIncontrata;
    



    public PersonaIncontrata(int id, boolean inizioEvento, boolean fineEvento, String descrizione, String nomePersonaIncontrata) {
        super(id, inizioEvento, fineEvento, descrizione);

        this.nomePersonaIncontrata = nomePersonaIncontrata;
    }
    public String getNomePersonaIncontrata() {
        return nomePersonaIncontrata;
    }
    
    public void setNomePersonaIncontrata(String nomePersonaIncontrata) {
        this.nomePersonaIncontrata = nomePersonaIncontrata;
    }


    
}
