package service;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import domain.Gioco;
import exception.InizializzaPartitaException;



public class FileService {

 private static final String SALVA_GIOCO = "salvataggi/";
    private static final String CARTELLA_LOG = "logs/";

    public static Object getInstance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    private String logFileNome;
     /**
     * Salva lo stato del gioco su un file.
     * 
     * @param gioco l'oggetto gioco da salvare.
     * @param fileName il nome del file su cui salvare.
     * @throws IOException se si verifica un errore di I/O.
     */

    public void salvaGioco(Gioco gioco, String fileName) throws InizializzaPartitaException {
        Path path = Paths.get(SALVA_GIOCO + fileName);
        Path parent = path.getParent();
        if (parent != null) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                System.out.println("Errore durante la creazione della cartella dei salvataggi: " + e.getMessage());
                throw new InizializzaPartitaException("Errore durante la creazione della cartella dei salvataggi", e);
            }
        }
        try (FileOutputStream fileOut = new FileOutputStream(path.toFile());
                ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(gioco);
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio della partita: " + e.getMessage());
            throw new InizializzaPartitaException("Errore durante il salvataggio della partita", e);
        }

    }
    /**
     * Carica una partita salvata da un file.
     * 
     * @param fileName il nome del file da cui caricare.
     * @return l'oggetto gioco caricato.
     * @throws InizializzaPartitaException se si verifica un errore durante il caricamento della partita.
     * @throws ClassNotFoundException se la classe Gioco non viene trovata.
     */

    public Gioco caricaGioco(String fileName) throws InizializzaPartitaException {
        Path path = Paths.get(SALVA_GIOCO + fileName);
        try (FileInputStream fileIn = new FileInputStream(path.toFile());
                ObjectInputStream in = new ObjectInputStream(fileIn)) {
            Gioco gioco = (Gioco) in.readObject();
            System.out.println("Partita caricata da " + path.toAbsolutePath());
            return gioco;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Errore durante il caricamento della partita: " + e.getMessage());
            throw new InizializzaPartitaException("Errore durante il caricamento della partita", e);
        }
    }
    
        /**
         * Restituisce la lista dei file di salvataggio presenti nella cartella dei salvataggi.
         * @return lista di nomi file (es. partita_salvata.sav)
         * @throws IOException se si verifica un errore di I/O.
         */
        public List<String> listaSalvataggi() throws IOException {
            Path dir = Paths.get(SALVA_GIOCO);
            if (!Files.exists(dir)) {
                return Collections.emptyList();
            }
            try (Stream<Path> stream = Files.list(dir)) {
                return stream.filter(Files::isRegularFile)
                        .map(p -> p.getFileName().toString())
                        .collect(Collectors.toList());
            }
        }


         /**
     * Scrive un'entrata nel log corrente.
     * 
     * @param data i dati da scrivere nel log.
     */

    public void writeLog(String data) {
        if (logFileNome == null) {
            System.out.println("Errore: nessun file di log impostato per la scrittura.");
            return;
        }
        Path logFilePath = Paths.get(CARTELLA_LOG + logFileNome);
        try (FileWriter writer = new FileWriter(logFilePath.toFile(), true);
        BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String[] lines = data.split("\\r?\\n");
            for (String line : lines) {
                bufferedWriter.write("[" + timeStamp + "] " + line);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            System.out.println("Errore durante la scrittura del file di log: " + e.getMessage());
        }
    }


    
}
