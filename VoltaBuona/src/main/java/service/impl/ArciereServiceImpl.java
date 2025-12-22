package service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import domain.Arciere;
import domain.Combattimento;
import domain.Evento;
import domain.Mostro;
import domain.Personaggio;
import domain.Stanza;
import domain.Zaino;
import service.PersonaggioService;

public class ArciereServiceImpl implements PersonaggioService {

    /**
     * Attacco a distanza: l'arciere può colpire solo stanze adiacenti; Se nella
     * stanza bersaglio ci sono personaggi, attacca il primo valido incontrato.
     *
     * Nota: questo metodo assume che: - Arciere abbia getPosizioneCorrente() -
     * Stanza esponga getStanzaAdiacente(): Map<String, Stanza>
     * - Stanza esponga getPersonaggi(): List<Personaggio>
     *
     * Adatta i nomi dei metodi se nella tua implementazione differiscono.
     */
    public boolean colpireStanza(Arciere arciere, Stanza stanzaBersaglio) {
        if (arciere == null || stanzaBersaglio == null) {
            return false;
        }

        Stanza posizione = arciere.getPosizioneCorrente();
        if (posizione == null) {
            return false;
        }

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
                if (target == null) {
                    continue;
                }
                // colpisci solo mostri, non i compagni
                if (!(target instanceof Mostro)) {
                    continue;
                }
                Mostro m = (Mostro) target;
                if (m.getPuntiVitaMostro() <= 0) {
                    continue;
                }
                // usare l'overload che accetta Mostro se presente
                attacca(arciere, m, null);
                System.out.println("Arciere " + arciere.getNomePersonaggio() + " ha colpito il mostro " + m.getTipoPersonaIncontrata() + " in stanza adiacente.");
                return true;
            }
            // stanza occupata ma nessun bersaglio valido
            return false;
        }

        // stanza vuota: l'attacco "va a vuoto" ma è comunque valido
        System.out.println("Arciere " + arciere.getNomePersonaggio() + " spara in una stanza adiacente vuota.");
        return true;
    }

    @Override
    public int attacca(Personaggio personaggio, Mostro mostro, Combattimento combattimento) {

        if (!(personaggio instanceof Arciere arciere)) {
            System.out.println(" Solo un arciere può usare attacco a distanza!");
            return 0;
        }
        if (mostro == null) {
            return 0;
        }

        Stanza stanzaAttuale = arciere.getPosizioneCorrente();
        Stanza stanzaMostro = mostro.getPosizioneCorrenteMostro();

        if (stanzaAttuale == null || stanzaMostro == null) {
            return 0;
        }

        // Caso semplice → mostro nella stessa stanza
        if (stanzaAttuale.equals(stanzaMostro)) {
            return infliggiDanno(arciere, mostro);
        }

        // 🔍 Cerco tutte le stanze adiacenti che contengono il mostro
        Map<String, Stanza> adiacenti = stanzaAttuale.getStanzaAdiacente();
        List<Stanza> stanzeConMostro = new ArrayList<>();

        if (adiacenti != null) {
            for (Stanza s : adiacenti.values()) {
                if (s.getListaEventiAttivi() != null && s.getListaEventiAttivi().contains(mostro)) {
                    stanzeConMostro.add(s);
                }
            }
        }

        //  Mostro non raggiungibile
        if (stanzeConMostro.isEmpty()) {
            System.out.println("Il mostro è troppo lontano per essere colpito!");
            return 0;
        }

        // 🎯 Se c’è solo una stanza → attacco automatico
        Stanza sceltaStanza = stanzeConMostro.get(0);

        // 🗳 Se più stanze → scelta giocatore
        if (stanzeConMostro.size() > 1) {
            Scanner scan = new Scanner(System.in);
            System.out.println(" Il mostro è in più stanze adiacenti! Scegli quale colpire:");

            for (int i = 0; i < stanzeConMostro.size(); i++) {
                System.out.println((i + 1) + ") " + stanzeConMostro.get(i).getNomeStanza());
            }

            int scelta = scan.nextInt() - 1;
            if (scelta < 0 || scelta >= stanzeConMostro.size()) {
                System.out.println(" Scelta non valida, attacco annullato!");
                return 0;
            }
            sceltaStanza = stanzeConMostro.get(scelta);
        }

        System.out.println("🏹 Attacco a distanza verso " + sceltaStanza.getNomeStanza());
        return infliggiDanno(arciere, mostro);
    }

    private int infliggiDanno(Arciere arciere, Mostro mostro) {
        int attacco = arciere.getAttacco();
        int livello = arciere.getLivello();
        int difesaMostro = mostro.getDifesaMostro();

        int dannoBase = attacco + livello * 2;
        int dannoNetto = Math.max(0, dannoBase - difesaMostro);

        mostro.setPuntiVitaMostro(mostro.getPuntiVitaMostro() - dannoNetto);

        System.out.println(" " + arciere.getNomePersonaggio()
                + " infligge " + dannoNetto
                + " danni a " + mostro.getTipoPersonaIncontrata());

        if (mostro.getPuntiVitaMostro() <= 0) {
            arciere.setEsperienza(arciere.getEsperienza() + 20);
            if (arciere.getEsperienza() >= 100) {
                arciere.setLivello(arciere.getLivello() + 1);
                arciere.setEsperienza(0);
                System.out.println(" Complimenti! " + arciere.getNomePersonaggio() + " è salito al livello " + arciere.getLivello());
            }
            System.out.println(" Mostro sconfitto!");
        }
        return dannoNetto;
    }

    @Override
   public Personaggio creaPersonaggio(String nome, Personaggio personaggio) {
Stanza stanza = null;
Zaino zaino = new Zaino();
return new Arciere("abilità", null, 200, 300, 0, 2, nome, stanza, false, 100, 300, "normale", 0, 0, 0, 0, zaino, 0);
}


    public void usaAbilitàSpeciale(Personaggio personaggio, String abilitàSpeciale) {
    }

    /* 
    public static void main(String[] args) {

        // === Service necessari ===
        ArciereServiceImpl arciereService = new ArciereServiceImpl();

        // === Creo 4 stanze in croce ===
        Stanza stanzaCentro = new Stanza(1, null, null, new ArrayList<>(), new ArrayList<>(), null, false, "Centro");
        Stanza stanzaNord = new Stanza(2, null, null, new ArrayList<>(), new ArrayList<>(), null, false, "Nord");
        Stanza stanzaEst = new Stanza(3, null, null, new ArrayList<>(), new ArrayList<>(), null, false, "Est");
        Stanza stanzaSud = new Stanza(4, null, null, new ArrayList<>(), new ArrayList<>(), null, false, "Sud");

        // Collego le stanze al centro
        stanzaCentro.getStanzaAdiacente().put("NORD", stanzaNord);
        stanzaCentro.getStanzaAdiacente().put("EST", stanzaEst);
        stanzaCentro.getStanzaAdiacente().put("SUD", stanzaSud);

        stanzaNord.getStanzaAdiacente().put("SUD", stanzaCentro);
        stanzaEst.getStanzaAdiacente().put("OVEST", stanzaCentro);
        stanzaSud.getStanzaAdiacente().put("NORD", stanzaCentro);

        // === Creo Arciere ===
        Personaggio arciere = new Arciere(
                1, 40, 12, 3, 15,
                "Legolas", stanzaCentro,
                1, 0, "Normale",
                new Zaino()
        );
        stanzaCentro.getListaPersonaggi().add(arciere);

        // === Creo 3 Mostri in posizioni diverse ===
        Mostro mostroCentro = new Mostro(1, false, false, "Mostro al centro", "Goblin", 20, 2, "Goblin", Mostro.TipoAttaccoMostro.MORSO, stanzaCentro);
        Mostro mostroNord = new Mostro(2, false, false, "Mostro a nord", "Orco", 30, 5, "Orco", Mostro.TipoAttaccoMostro.RUGGITO_DI_FUOCO, stanzaNord);
        Mostro mostroEst = new Mostro(3, false, false, "Mostro a est", "Scheletro", 25, 3, "Scheletro", Mostro.TipoAttaccoMostro.URLO_ASSORDANTE, stanzaEst);

        stanzaCentro.getListaEventiAttivi().add(mostroCentro);
        stanzaNord.getListaEventiAttivi().add(mostroNord);
        stanzaEst.getListaEventiAttivi().add(mostroEst);

        // === TEST CASE ===

        Scanner scanner = new Scanner(System.in);
        boolean continua = true;

        System.out.println("=== TEST ATTACCO ARCIERE ===");

        while (continua) {

            System.out.println("\nDove vuoi colpire?");
            System.out.println("1) Mostro nella STESSA stanza (Goblin)");
            System.out.println("2) Mostro a NORD (Orco)");
            System.out.println("3) Mostro a EST (Scheletro)");
            System.out.println("4) Forzare SCELTA tra più adiacenti");
            System.out.println("0) Esci test");

            int scelta = Integer.parseInt(scanner.nextLine());

            switch (scelta) {
                case 1 -> arciereService.attacca(arciere, mostroCentro, null);
                case 2 -> arciereService.attacca(arciere, mostroNord, null);
                case 3 -> arciereService.attacca(arciere, mostroEst, null);
                case 4 -> {
                    System.out.println("⚠ Sposto un mostro anche a Sud per attivare scelta bersaglio...");
                    Mostro mostroSud = new Mostro(4, false, false, "Mostro a sud", "Demone", 35, 4, "Demone", Mostro.TipoAttaccoMostro.ARTIGLI_POSSENTI, stanzaSud);
                    stanzaSud.getListaEventiAttivi().add(mostroSud);

                    // ora il giocatore deve scegliere Nord o Sud
                    arciereService.attacca(arciere, mostroSud, null);

                }
                case 0 -> continua = false;
                default -> System.out.println("Scelta non valida!");
            }
        }

        System.out.println("\nTest completato!");
    } */
}
