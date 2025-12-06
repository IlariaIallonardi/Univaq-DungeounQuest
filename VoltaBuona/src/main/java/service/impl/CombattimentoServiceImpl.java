package service.impl;

import domain.Combattimento;
import domain.Mostro;
import domain.Personaggio;
import domain.Zaino;
import service.CombattimentoService;
import service.PersonaggioService;



public class CombattimentoServiceImpl implements CombattimentoService {
    private  MostroServiceImpl mostroService = new MostroServiceImpl();
    private  PersonaggioService personaggioService;


   
    /**
     * Calcola e applica il danno generato dall'attaccante sul bersaglio implicito
     * memorizzato in Combattimento. Ritorna il danno effettivamente applicato.
     */
    @Override
    public int applicaECalcolaDanno(Combattimento combattimento, Object attaccante) {
        if (combattimento == null || attaccante == null) {
            return 0;
        }

        // Attaccante = Mostro -> bersaglio = Personaggio
        // Verifichiamo che l'attaccante sia esattamente il mostro coinvolto nel combattimento
        if (combattimento.getMostroCoinvolto() != null && attaccante == combattimento.getMostroCoinvolto()) {
            Mostro mostro = combattimento.getMostroCoinvolto();
            Personaggio personaggio = combattimento.getPersonaggioCoinvolto();
            if (mostro == null || personaggio == null) return 0;

            // calcola danno base evitando metodi che applicano già il danno internamente
            int dannoBase = MostroServiceImpl.dannoBase(mostro, personaggio);

            // applica il danno tramite PersonaggioService (centrale per l'applicazione e gli effetti)
            int dannoApplicato = personaggioService.subisciDannoDaMostro(mostro.getTipoAttaccoMostro(), dannoBase, personaggio);

            System.out.println(mostro.getNomeMostro() + " infligge " + dannoApplicato + " danni a " + personaggio.getNomeP()
                    + " (HP rimasti: " + personaggio.getPuntiVita() + ")");

            if (personaggio.getPuntiVita() <= 0) {
                combattimento.setVincitore(mostro);
                combattimento.setInCorso(false);
                System.out.println(personaggio.getNomeP() + " è stato sconfitto da " + mostro.getNomeMostro());
            }

            return dannoApplicato;
        }

        // Attaccante = Personaggio -> bersaglio = Mostro
        // Verifichiamo che l'attaccante sia esattamente il personaggio coinvolto nel combattimento
        if (combattimento.getPersonaggioCoinvolto() != null && attaccante == combattimento.getPersonaggioCoinvolto()) {
            Personaggio personaggio= combattimento.getPersonaggioCoinvolto();
            Mostro mostro = combattimento.getMostroCoinvolto();
            if (personaggio == null || mostro == null) return 0;

        
        // già applica la difesa e sottrae gli HP al mostro, e ritorna il danno applicato.
        int dannoApplicato = personaggioService.attacca(personaggio, mostro, combattimento);

        System.out.println(personaggio.getNomeP() + " infligge " + dannoApplicato + " danni a " + mostro.getNomeMostro()
        + " (HP rimasti: " + mostro.getPuntiVitaMostro() + ")");
            if (mostro.getPuntiVitaMostro() <= 0) {
                combattimento.setVincitore(personaggio);
                combattimento.setInCorso(false);
                System.out.println(mostro.getNomeMostro() + " è stato sconfitto da " + personaggio.getNomeP());
                try { personaggio.setEsperienza(personaggio.getEsperienza() + 10); } catch (Exception ignored) {}
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
    if(combattimento.getMostroCoinvolto().getPuntiVitaMostro()<=0) {
        return  vincitore = combattimento.getPersonaggioCoinvolto();
    } else if(combattimento.getPersonaggioCoinvolto().getPuntiVita()<=0) {
        return  vincitore =combattimento.getMostroCoinvolto();
    }
     return vincitore;
}

    @Override
    public boolean iniziaCombattimento(Combattimento combattimento) {
        // TODO Auto-generated method stub
        return false;
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

    

   

 

    
    
}
    