package service;

import java.nio.file.Path;
import java.util.*;
import domain.Stanza;

public class StanzaFactory {

    /**
     * Enum per le direzioni di collegamento tra stanze
     */
    public enum Direzione {
        SOPRA("sopra"),
        SOTTO("sotto"),
        SINISTRA("sinistra"),
        DESTRA("destra"),
        DENTRO("dentro"),
        FUORI("fuori");

        private final String nome;

        Direzione(String nome) {
            this.nome = nome;
        }

        public String getNome() {
            return nome;
        }
    }

    /**
     * Metodo unico per creare una stanza
     * @param id ID della stanza
     * @param descrizione Descrizione della stanza
     * @param tipoStanza Tipo di stanza (VUOTA, NORMALE, SPECIALE, ecc.)
     * @return Stanza creata
     */
    public Stanza creaStanza(int id, String descrizione, TipoStanza tipoStanza) {
        Stanza stanza = new Stanza(id, new int[2][2], descrizione);
        
        switch (tipoStanza) {
            case VUOTA:
                // Stanza vuota di default
                break;
            case NORMALE:
                aggiungiEventoCasuale(stanza);
                aggiungiOggettoCasuale(stanza);
                break;
            case SPECIALE:
                aggiungiEventoCasuale(stanza);
                aggiungiOggettoCasuale(stanza);
                aggiungiOggettoCasuale(stanza);
                break;
            case INIZIALE:
                stanza.setStatoS("Iniziale");
                break;
            case FINALE:
                stanza.setStatoS("Finale");
                aggiungiOggettoCasuale(stanza);
                break;
            default:
                break;
        }
        
        return stanza;
    }

    /**
     * Metodo per collegare due stanze in una determinata direzione
     * @param stanzaSorgente La stanza di partenza
     * @param direzione La direzione di collegamento
     * @param stanzaDestinazione La stanza di destinazione
     */
    public void collegaStanze(Stanza stanzaSorgente, Direzione direzione, Stanza stanzaDestinazione) {
        if (stanzaSorgente == null || stanzaDestinazione == null) {
            return;
        }

        Map<String, Stanza> adiacenti = stanzaSorgente.getStanzaAdiacente();
        if (adiacenti == null) {
            adiacenti = new HashMap<>();
            stanzaSorgente.setStanzaAdiacente(adiacenti);
        }

        // Collega nella direzione specificata
        adiacenti.put(direzione.getNome(), stanzaDestinazione);

        // Collega anche in direzione opposta automaticamente
        Direzione direzioneOpposta = getDirezioneOpposta(direzione);
        Map<String, Stanza> adiacentiDestinazione = stanzaDestinazione.getStanzaAdiacente();
        if (adiacentiDestinazione == null) {
            adiacentiDestinazione = new HashMap<>();
            stanzaDestinazione.setStanzaAdiacente(adiacentiDestinazione);
        }
        adiacentiDestinazione.put(direzioneOpposta.getNome(), stanzaSorgente);
    }

    /**
     * Metodo helper per ottenere la direzione opposta
     */
    private Direzione getDirezioneOpposta(Direzione direzione) {
        switch (direzione) {
            case SOPRA:
                return Direzione.SOTTO;
            case SOTTO:
                return Direzione.SOPRA;
            case SINISTRA:
                return Direzione.DESTRA;
            case DESTRA:
                return Direzione.SINISTRA;
            case DENTRO:
                return Direzione.FUORI;
            case FUORI:
                return Direzione.DENTRO;
            default:
                return null;
        }
    }

    public void aggiungiEventoCasuale(Stanza stanza) {
        // Implementazione per aggiungere evento casuale
    }

    public void aggiungiOggettoCasuale(Stanza stanza) {
        // Implementazione per aggiungere oggetto casuale
    }

    /**
     * Enum per i tipi di stanza
     */
    public enum TipoStanza {
        VUOTA,
        NORMALE,
        SPECIALE,
        INIZIALE,
        FINALE
    }
}