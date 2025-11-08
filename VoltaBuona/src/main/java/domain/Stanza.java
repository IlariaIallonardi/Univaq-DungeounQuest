
package domain;

public class Stanza {
    private boolean chiave;
    private boolean visitata;

    private int [][] coordinate;

    public boolean isChiave() {
        return chiave;
    }

    public void setChiave(boolean chiave) {
        this.chiave = chiave;
    }

    public Stanza(boolean chiave, boolean visitata, int[][] coordinate) {
        this.chiave = chiave;
        this.visitata = visitata;
        this.coordinate = coordinate;
    }

    public boolean isVisitata() {
        return visitata;
    }

    public void setVisitata(boolean visitata) {
        this.visitata = visitata;
    }

    public int[][] getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(int[][] coordinate) {
        this.coordinate = coordinate;
    }
}

