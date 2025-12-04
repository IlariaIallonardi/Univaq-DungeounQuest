package service.impl;

import domain.Combattimento;
import domain.Mostro;
import domain.Personaggio;
import domain.Zaino;
import service.CombattimentoService;
import service.PersonaggioService;



public class CombattimentoServiceImpl implements CombattimentoService {
    private final PersonaggioService personaggioService = new PersonaggioService();

    @Override
    public void applicaDanno(Combattimento combattimento, Object attaccante) {
    if (combattimento == null || attaccante == null) {
        return;
    }
    if(attaccante instanceof Mostro){
        Personaggio personaggio = combattimento.getPersonaggioCoinvolto();
        Mostro mostro = combattimento.getMostroCoinvolto();
        int danno = calcolaDanno(combattimento, mostro, personaggio);
        personaggioService.subisciDannoDaMostro(null, danno, personaggio);

    }
        
    }

    @Override
    public int calcolaDanno(Combattimento combattimento, Object attaccante, Object difensore) {
        // TODO Auto-generated method stub
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
    