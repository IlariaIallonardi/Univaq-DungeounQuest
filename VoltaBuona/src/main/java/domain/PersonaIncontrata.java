package domain;

public class PersonaIncontrata extends Evento {
    private String nome;
    private String messaggio;
    private int danno;



    public PersonaIncontrata(int id, boolean inizioEvento, boolean fineEvento, String descrizione,int danno, String messaggio, String nome) {
        super(id, inizioEvento, fineEvento, descrizione);
        this.danno = danno;
        this.messaggio = messaggio;
        this.nome = nome;
    }
    public String getNome() {
        return nome;
    }
    public String getMessaggio() {
        return messaggio;
    }
    public int getDanno() {
        return danno;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public void setDanno(int danno) {
        this.danno = danno;
    }


    
}
