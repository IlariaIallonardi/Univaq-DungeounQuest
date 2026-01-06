package domain;

public class PersonaIncontrata extends Evento {
    private String tipoPersonaIncontrata;
    



    public PersonaIncontrata(int id, boolean inizioEvento, boolean fineEvento, String descrizione, String tipoPersonaIncontrata) {
        super(id, inizioEvento, fineEvento, descrizione, "PersonaIncontrata", null);

        this.tipoPersonaIncontrata = tipoPersonaIncontrata;
    }
    public String getTipoPersonaIncontrata() {
        return tipoPersonaIncontrata;
    }
    
    public void setTipoPersonaIncontrata(String tipoPersonaIncontrata) {
        this.tipoPersonaIncontrata = tipoPersonaIncontrata;
    }


    
}
