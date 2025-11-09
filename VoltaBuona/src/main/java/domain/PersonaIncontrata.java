package domain;

public class PersonaIncontrata {
    private String nome;
    private String messaggio;
    private int danno;

    public PersonaIncontrata(int danno, String messaggio, String nome) {
        this.danno = danno;
        this.messaggio = messaggio;
        this.nome = nome;
    }
}
