package service.impl;

import java.util.List;
import java.util.Random;

public class RandomSingleton {

    private final Random random= new Random();
    private static final RandomSingleton randomGenerale = new RandomSingleton();

    

    public static RandomSingleton getInstance() {
        return randomGenerale;
    }

    public int nextInt() {
        return random.nextInt();
    }
     public Random getRandom() {
        return random;
    }

    
/**
 * Genera un numero randomicamente tra il minimo e il massimo inclusi.
 * @param minimo
 * @param massimo
 */  
    public int prossimoNumero(int minimo, int massimo) {
        if (minimo > massimo) {
            throw new IllegalArgumentException("minimo dovrebbe essere minore del  massimo");
        }
        return minimo + random.nextInt(massimo - minimo + 1);
    }



    public <T> T scegliRandomicamente(List<T> lista) {
        if (lista == null || lista.isEmpty()) return null;
        return lista.get(prossimoNumero(0, lista.size() - 1));
    }

   

    

    
}