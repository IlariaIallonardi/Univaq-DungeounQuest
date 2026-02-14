package service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ScannerSingleton implements AutoCloseable {

    private final Scanner scanner = new Scanner(System.in);
    private static final ScannerSingleton scannerGenerale = new ScannerSingleton();

    private ScannerSingleton() {
    }

    public static ScannerSingleton getInstance() {
        return scannerGenerale;
    }

    public Scanner getScanner() {
        return scanner;
    }

    @Override
    public void close() throws IOException {
        scanner.close();
    }

    public int leggiIntero() {
        while (true) {
            String line = scanner.nextLine();
            if (line == null) {
                continue;
            }
            //Rimozione spazi bianchi all'inizio e alla fine della stringa. 
            line = line.trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Valore non valido. Inserisci un numero intero.");
            }
        }
    }

    public int leggiInteroIntervallo(int minimo, int massimo) {
        while (true) {
            int valore = leggiIntero();
            if (valore >= minimo && valore <= massimo) {
                return valore;
            }
            System.out.println(String.format("Valore non valido. Inserisci un numero compreso tra %d e %d.", minimo, massimo));
        }
    }

    public <T> T leggiSceltaDallaLista(List<T> lista) {

        for (int i = 0; i < lista.size(); i++) {
            //formatta la stringa '%d' per gli interi '%s' per le stringhe.
            System.out.println(String.format("%d - %s", i, lista.get(i).toString()));
        }
        int scelta = leggiInteroIntervallo(0, lista.size() - 1);
        return lista.get(scelta);
    }

    public String leggiLinea() {
        return scanner.nextLine();
    }

}
