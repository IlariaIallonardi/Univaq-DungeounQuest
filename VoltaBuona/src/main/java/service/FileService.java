package service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import domain.Gioco;
import exception.SaveLoadException;

public class FileService {

 private static final String SALVA_GIOCO = "salvataggi/";
     /**
     * Salva lo stato del gioco su un file.
     * 
     * @param gioco l'oggetto gioco da salvare.
     * @param fileName il nome del file su cui salvare.
     * @throws IOException se si verifica un errore di I/O.
     */

    public void salvaGioco(Gioco gioco, String fileName) throws SaveLoadException {
        Path path = Paths.get(SALVA_GIOCO + fileName);
        Path parent = path.getParent();
        if (parent != null) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                System.out.println("Errore durante la creazione della cartella dei salvataggi: " + e.getMessage());
                throw new SaveLoadException("Errore durante la creazione della cartella dei salvataggi", e);
            }
        }
        try (FileOutputStream fileOut = new FileOutputStream(path.toFile());
                ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(gioco);
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio della partita: " + e.getMessage());
            throw new SaveLoadException("Errore durante il salvataggio della partita", e);
        }

    }
    /**
     * Carica una partita salvata da un file.
     * 
     * @param fileName il nome del file da cui caricare.
     * @return l'oggetto gioco caricato.
     * @throws IOException se si verifica un errore di I/O.
     * @throws ClassNotFoundException se la classe Gioco non viene trovata.
     */

    public Gioco caricaGioco(String fileName) throws SaveLoadException {
        Path path = Paths.get(SALVA_GIOCO + fileName);
        try (FileInputStream fileIn = new FileInputStream(path.toFile());
                ObjectInputStream in = new ObjectInputStream(fileIn)) {
            Gioco gioco = (Gioco) in.readObject();
            System.out.println("Partita caricata da " + path.toAbsolutePath());
            return gioco;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Errore durante il caricamento della partita: " + e.getMessage());
            throw new SaveLoadException("Errore durante il caricamento della partita", e);
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
         * Elimina un file di salvataggio esistente.
         * @param fileName nome del file da eliminare
         * @return true se eliminato, false se non esiste
         * @throws IOException se si verifica un errore di I/O.
         */
        public boolean eliminaSalvataggio(String fileName) throws IOException {
            Path path = Paths.get(SALVA_GIOCO + fileName);
            return Files.deleteIfExists(path);
        }

        /**
         * Salva il gioco con un nome univoco basato sul timestamp e restituisce il nome.
         * @param gioco oggetto gioco
         * @return nome del file creato
         * @throws IOException se si verifica un errore di I/O.
         */
        public String salvaGiocoConNomeUnico(Gioco gioco) throws SaveLoadException {
            String fileName = "partita_" + System.currentTimeMillis() + ".sav";
            salvaGioco(gioco, fileName);
            return fileName;
        }
    
}
