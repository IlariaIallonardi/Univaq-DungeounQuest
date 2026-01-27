package service;

import domain.Dungeon;
import domain.Personaggio;

public interface GiocoService {

    



    public Dungeon getDungeon();

    public Dungeon creaDungeon(int righe, int colonne);
    public boolean muoviPersonaggio(Personaggio personaggio, Direzione direzione);

   
}
