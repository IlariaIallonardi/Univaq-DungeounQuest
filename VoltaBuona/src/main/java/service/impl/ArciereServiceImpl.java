package service.impl;

import java.util.List;
import java.util.Map;

import domain.Arciere;
import domain.Evento;
import domain.Mostro;
import domain.Stanza;
import service.PersonaggioService;

public class ArciereServiceImpl extends PersonaggioService {


    /**
     * Attacco a distanza: l'arciere può colpire solo stanze adiacenti;
     * Se nella stanza bersaglio ci sono personaggi, attacca il primo valido incontrato.
     *
     * Nota: questo metodo assume che:
     * - Arciere abbia getPosizioneCorrente()
     * - Stanza esponga getStanzaAdiacente(): Map<String, Stanza>
     * - Stanza esponga getPersonaggi(): List<Personaggio>
     *
     * Adatta i nomi dei metodi se nella tua implementazione differiscono.
     */
    public boolean colpireStanza(Arciere arciere, Stanza stanzaBersaglio) {
        if (arciere == null || stanzaBersaglio == null) return false;

        Stanza posizione = arciere.getPosizioneCorrente();
        if (posizione == null) return false;

        // verifica che la stanza bersaglio sia adiacente alla posizione dell'arciere
        Map<String, Stanza> adiacenti = posizione.getStanzaAdiacente();
        boolean èAdiacente = false;
        if (adiacenti != null) {
            for (Stanza s : adiacenti.values()) {
                if (s == stanzaBersaglio) {
                    èAdiacente = true;
                    break;
                }
            }
        }

        if (!èAdiacente) {
            // non è possibile colpire stanza non adiacente
            return false;
        }

        // prova a recuperare i personaggi nella stanza bersaglio e colpirne il primo valido
        List<Evento> mostri = stanzaBersaglio.getListaEventiAttivi();
        if (mostri != null && !mostri.isEmpty()) {
            for (Evento target : mostri) {
                if (target == null) continue;
                // colpisci solo mostri, non i compagni
                if (!(target instanceof Mostro)) continue;
                Mostro m = (Mostro) target;
                if (m.getPuntiVitaMostro() <= 0) continue;
                // usare l'overload che accetta Mostro se presente
                eseguiAttacco(arciere, m);
                System.out.println("Arciere " + arciere.getNomeP() + " ha colpito il mostro " + m.getNome() + " in stanza adiacente.");
                return true;
            }
            // stanza occupata ma nessun bersaglio valido
            return false;
        }

        // stanza vuota: l'attacco "va a vuoto" ma è comunque valido
        System.out.println("Arciere " + arciere.getNomeP() + " spara in una stanza adiacente vuota.");
        return true;
    }

    private void eseguiAttacco(Arciere arciere, Mostro mostro) {
    if (arciere == null || mostro == null) return;

    // calcolo danno base (adatta la formula al tuo bilanciamento)
    int attacco = arciere.getAttacco(); // presuppone getter
    int livello = arciere.getLivello(); // presuppone getter
    int dannoBase = Math.max(0, attacco + livello * 2);

    // applica difesa del mostro
    int difesaMostro = mostro.getDifesaMostro(); // presuppone getter
    int dannoNetto = Math.max(0, dannoBase - difesaMostro);

    // sottrai punti vita al mostro
    int nuoviPV = mostro.getPuntiVitaMostro() - dannoNetto; // presuppone getter
    mostro.setPuntiVitaMostro(nuoviPV); // presuppone setter

    System.out.println(arciere.getNomeP() + " infligge " + dannoNetto + " al mostro " + mostro.getNome());

    if (mostro.getPuntiVitaMostro() <= 0) {
        System.out.println("Il mostro " + mostro.getNome() + " è stato sconfitto.");
        // Qui puoi aggiungere rimozione dal contesto/stanza, drop oggetti, assegnazione XP, ecc.
    }
}
}