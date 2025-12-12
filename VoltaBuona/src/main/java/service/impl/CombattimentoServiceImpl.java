package service.impl;

import domain.Combattimento;
import domain.Mostro;
import domain.Personaggio;
import domain.Stanza;
import domain.Zaino;
import service.CombattimentoService;
import service.PersonaggioService;

public class CombattimentoServiceImpl implements CombattimentoService {

    private MostroServiceImpl mostroService = new MostroServiceImpl();
    private PersonaggioService personaggioService;

    /**
     * Calcola e applica il danno generato dall'attaccante sul bersaglio
     * implicito memorizzato in Combattimento. Ritorna il danno effettivamente
     * applicato.
     *
     */
    @Override
    public int applicaECalcolaDanno(Combattimento combattimento, Object attaccante) {
        if (combattimento == null || attaccante == null) {
            return 0;
        }

        //scelta equipaggiamento personaggio
        Personaggio personaggio = combattimento.getPersonaggioCoinvolto();
        Mostro mostro = combattimento.getMostroCoinvolto();

        // Attaccante = Mostro -> bersaglio = Personaggio
        // Verifichiamo che l'attaccante sia esattamente il mostro coinvolto nel combattimento
        if (combattimento.getMostroCoinvolto() != null && attaccante == combattimento.getMostroCoinvolto()) {

            if (mostro == null || personaggio == null) {
                return 0;
            }

            // calcola danno base evitando metodi che applicano già il danno internamente
            int dannoBase = MostroServiceImpl.dannoBase(mostro, personaggio);

            // applica il danno tramite PersonaggioService (centrale per l'applicazione e gli effetti)
            int dannoApplicato = mostroService.attaccoDelMostro(mostro, personaggio, dannoBase);

            System.out.println(mostro.getNomeMostro() + " infligge " + dannoApplicato + " danni a " + personaggio.getNomePersonaggio()
                    + " (HP rimasti: " + personaggio.getPuntiVita() + ")");

            if (personaggio.getPuntiVita() <= 0) {
                combattimento.setVincitore(mostro);
                combattimento.setInCorso(false);
                System.out.println(personaggio.getNomePersonaggio() + " è stato sconfitto da " + mostro.getNomeMostro());
            }

            return dannoApplicato;
        }

        // Attaccante = Personaggio -> bersaglio = Mostro
        // Verifichiamo che l'attaccante sia esattamente il personaggio coinvolto nel combattimento
        if (combattimento.getPersonaggioCoinvolto() != null && attaccante == combattimento.getPersonaggioCoinvolto()) {

            if (personaggio == null || mostro == null) {
                return 0;
            }

            // già applica la difesa e sottrae gli HP al mostro, e ritorna il danno applicato.
            int dannoApplicato = personaggioService.attacca(personaggio, mostro, combattimento);

            System.out.println(personaggio.getNomePersonaggio() + " infligge " + dannoApplicato + " danni a " + mostro.getNomeMostro()
                    + " (HP rimasti: " + mostro.getPuntiVitaMostro() + ")");
            if (mostro.getPuntiVitaMostro() <= 0) {
                combattimento.setVincitore(personaggio);
                combattimento.setInCorso(false);
                System.out.println(mostro.getNomeMostro() + " è stato sconfitto da " + personaggio.getNomePersonaggio());
                try {
                    personaggio.setEsperienza(personaggio.getEsperienza() + 10);
                } catch (Exception ignored) {
                }
            }

            return dannoApplicato;
        }

        // tipo non gestito
        System.out.println("Tipo attaccante non gestito in applicaECalcolaDanno: " + attaccante.getClass().getName());
        return 0;
    }

    @Override
    public Mostro getMostro(Combattimento combattimento) {

        return combattimento.getMostroCoinvolto();
    }

    @Override
    public Personaggio getPersonaggio(Combattimento combattimento) {

        return combattimento.getPersonaggioCoinvolto();
    }

    @Override
    public Object getVincitore(Combattimento combattimento) {
        if (combattimento == null) {
            return null;
        }
        Object vincitore = new Object();
        if (combattimento.getMostroCoinvolto().getPuntiVitaMostro() <= 0) {
            return vincitore = combattimento.getPersonaggioCoinvolto();
        } else if (combattimento.getPersonaggioCoinvolto().getPuntiVita() <= 0) {
            return vincitore = combattimento.getMostroCoinvolto();
        }
        return vincitore;
    }

    @Override
    public void scegliAzione(Personaggio personaggio, Zaino zaino) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean terminaCombattimento(Combattimento combattimento) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean èInCorso(Combattimento combattimento) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Object iniziaCombattimento(Personaggio personaggio, Mostro mostro, Stanza stanza) {

        if (personaggio == null || mostro == null) {
            System.out.println(" Combattimento non valido.");
            return false;
        }

        System.out.println("\n Inizia il combattimento: "
                + personaggio.getNomePersonaggio() + " VS " + mostro.getNomeMostro());

        Combattimento combattimento = new Combattimento(null, 0, mostro, 0, false, personaggio, stanza, 0, stanza, mostro);
        combattimento.setInCorso(true);

        // 🎲 iniziativa random: 0 = mostro, 1 = personaggio
        int iniziativa = new java.util.Random().nextInt(2);
        System.out.println("🎲 Iniziativa: " + (iniziativa == 0 ? "inizia il MOSTRO" : "inizia il PERSONAGGIO"));

        // Imposta chi attacca per primo
        //Object attaccante = (iniziativa == 0) ? mostro : personaggio;
        combattimento.setInCorso(true);

        // ciclo di combattimento semplice: alterna finché uno non muore
        while (combattimento.isInCorso()) {
            if (iniziativa == 1) {
                System.out.println(personaggio.getNomePersonaggio() + " inizia il combattimento.");
                applicaECalcolaDanno(combattimento, personaggio);
            } else {
                System.out.println(mostro.getNomeMostro() + " inizia il combattimento.");
                applicaECalcolaDanno(combattimento, mostro);
            }
            //  fine combattimento: rimozione mostro dalla stanza se morto
            if (mostro.getPuntiVitaMostro() <= 0) {
                combattimento.setVincitore(personaggio);
                System.out.println("🏆 " + personaggio.getNomePersonaggio() + " ha vinto!");

                // se il mostro è nella lista eventi della stanza, lo rimuovi
                if (stanza != null && stanza.getListaEventiAttivi() != null) {
                    stanza.getListaEventiAttivi().remove(mostro);
                }

            } else if (personaggio.getPuntiVita() <= 0) {
                combattimento.setVincitore(mostro);
                System.out.println("💀 " + personaggio.getNomePersonaggio() + " è stato sconfitto...");
            }

            combattimento.setInCorso(false);
            // Se qualcuno è morto, applicaECalcolaDanno avrà settato inCorso=false
            if (!combattimento.isInCorso()) {
                break;
            }

            // switch dell’attaccante (alternanza turni)
            iniziativa = 0;
        }

        return combattimento.getVincitore();

    }

}
