package service.impl;

import java.util.List;
import java.util.Optional;

import domain.Combattimento;
import domain.Mostro;
import domain.Personaggio;
import domain.Stanza;
import service.CombattimentoService;

public class CombattimentoServiceImpl implements CombattimentoService {

    @Override
    public void applicaDanno(Combattimento combattimento, Object attaccante, Object difensore, int danno) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int calcolaDanno(Combattimento combattimento, Object attaccante, Object difensore) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Combattimento creaCombattimento(int id, Stanza stanza) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean eseguiAzione(Combattimento combattimento, Object attore, String azioneId, Object target) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<Mostro> getMostri(Combattimento combattimento) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Personaggio> getPersonaggi(Combattimento combattimento) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Object> getVincitore(Combattimento combattimento) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public boolean iniziaCombattimento(Combattimento combattimento) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Object prossimoPartecipante(Combattimento combattimento) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean rimuoviPartecipante(Combattimento combattimento, Object partecipante) {
        // TODO Auto-generated method stub
        return false;
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
    