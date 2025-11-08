package domain;

public class Zaino {
    private int id;
    private int capienza;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapienza() {
        return capienza;
    }

    public void setCapienza(int capienza) {
        this.capienza = capienza;
    }

    public Zaino(int id, int capienza) {
        this.id = id;
        this.capienza = capienza;
    }
}