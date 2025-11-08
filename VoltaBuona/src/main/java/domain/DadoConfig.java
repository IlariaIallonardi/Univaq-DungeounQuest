package domain;

import java.io.*;
import java.util.*;

public class DadoConfig {
    private static class RangeEffetto {
        int min, max;
        String effetto;
        RangeEffetto(int min, int max, String effetto) {
            this.min = min;
            this.max = max;
            this.effetto = effetto;
        }
        boolean contiene(int valore) {
            return valore >= min && valore <= max;
        }
    }

    private final List<RangeEffetto> effetti = new ArrayList<>();

    public DadoConfig(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(":");
                String[] range = parts[0].trim().split("-");
                int min = Integer.parseInt(range[0]);
                int max = Integer.parseInt(range[1]);
                effetti.add(new RangeEffetto(min, max, parts[1].trim()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getEffetto(int dado) {
        for (RangeEffetto re : effetti) {
            if (re.contiene(dado)) {
                return re.effetto;
            }
        }
        return "Nessun effetto.";
    }
}