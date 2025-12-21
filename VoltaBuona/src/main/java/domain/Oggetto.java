package domain;

public abstract class Oggetto {

    private int id;
    private String nome;
    private String descrizione;
    private boolean usabile;
    private boolean equipaggiabile;
    private boolean trovato;
    private int prezzo;

    public Oggetto(int id, String nome, String descrizione, boolean usabile, boolean equipaggiabile, boolean trovato) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.usabile = usabile;
        this.equipaggiabile = equipaggiabile;
        this.trovato = trovato;
        
    }
    public int getPrezzo() {
        return prezzo;
    }
    public void setPrezzo(int prezzo) {
        this.prezzo = prezzo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public boolean isUsabile() {
        return usabile;
    }

    public void setUsabile(boolean usabile) {
        this.usabile = usabile;
    }

    public boolean isEquipaggiabile() {
        return equipaggiabile;
    }

    public void setEquipaggiabile(boolean equipaggiabile) {
        this.equipaggiabile = equipaggiabile;
    }

    public boolean isTrovato() {
        return trovato;
    }

    public void setTrovato(boolean trovato) {
        this.trovato = trovato;
    }

    public boolean usare(Personaggio personaggio) {
        if (!this.usabile) {
            throw new IllegalStateException("Questo oggetto non può essere usato");
        }

        if (!this.trovato) {
            throw new IllegalStateException("Questo oggetto non è stato ancora trovato");
        }

        return true;
    }
    // ogni oggetto concreto implementa il proprio effetto sul personaggio
    public abstract boolean eseguiEffetto(Personaggio personaggio);
    

}

