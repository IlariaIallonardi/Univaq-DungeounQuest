package service.impl;


import domain.Combattimento;
import domain.Guerriero;
import domain.Mostro;
import domain.Paladino;
import domain.Personaggio;
import service.PersonaggioService;


public class PaladinoServiceImpl extends PersonaggioService {
    
    /**
     * Metodo per proteggere un altro giocatore
     * @param paladino Il paladino che usa la protezione
     * @param bersaglio Il personaggio da proteggere
     * @return true se la protezione è stata applicata con successo
     */
    public boolean protezioneGiocatoreG(Guerriero guerriero, Personaggio bersaglio) {
        if (guerriero == null || bersaglio == null) {
            return false;
        }

        // Verifica se il guerriero ha abbastanza punti vita
        if (guerriero.getPuntiVita() < 20) {
            return false;
        }

         // delega al dominio: solo Guerriero può applicare la protezione
        boolean ok = guerriero.proteggi(bersaglio);
        if (!ok) return false;

        System.out.println(guerriero.getNomeP() + " protegge per un turno " + bersaglio.getNomeP());
        return true;
    }
  

       

    /**
     * Metodo per utilizzare la magia sacra del paladino
     * @param paladino Il paladino che usa la magia
     * @param bersaglio Il personaggio bersaglio
     * @param tipoMagiaSacra Il tipo di magia da utilizzare
     * @return true se la magia è stata lanciata con successo
     */
   @Override
public int attacca(Personaggio personaggio, Mostro mostro, Combattimento combattimento) {
    if (!(personaggio instanceof Paladino)) {
        return super.attacca(personaggio, mostro, combattimento);
    }
    Paladino paladino = (Paladino) personaggio;
    if (paladino == null || mostro == null) return 0;

   

    TipoMagiaSacra tipoMagiaSacra = scegliMagiaPerPaladino(paladino, mostro, combattimento);

    // verifica mana
    if (paladino.getPuntiMana() < tipoMagiaSacra.getCostoMana()) {
        return 0;
    }

    // Applica l'effetto: qui esempi negativi/offensivi contro il mostro
    switch (tipoMagiaSacra) {
        case AVVELENAMENTO:
        
            mostro.setPuntiVitaMostro(Math.max(0, mostro.getPuntiVitaMostro() ));
            System.out.println(paladino.getNomeP() + " avvelena " + mostro.getNomeMostro() );
            break;
        case MALATTIA:
            // esempio: riduce danno del mostro
            try { mostro.setDannoMostro(Math.max(0, mostro.getDannoMostro() - 2)); } catch (Exception ignored) {}
            System.out.println(paladino.getNomeP() + " indebolisce l'attacco di " + mostro.getNomeMostro());
            break;
        case CORROSIONE:
            try { mostro.setDifesaMostro(Math.max(0, mostro.getDifesaMostro() - 2)); } catch (Exception ignored) {}
            System.out.println(paladino.getNomeP() + " corrode l'armatura di " + mostro.getNomeMostro());
            break;
        case STORDIMENTO:
            // imposta uno stato sul mostro se esiste un campo/stato (fallback: setTipoPersonaIncontrata temporaneo)
            try { mostro.setTipoPersonaIncontrata(mostro.getTipoPersonaIncontrata() + " (stordito)"); } catch (Exception ignored) {}
            System.out.println(paladino.getNomeP() + " stordisce " + mostro.getNomeMostro());
            break;
        case RUBAVITA:
            int danno = 8;
            mostro.setPuntiVitaMostro(Math.max(0, mostro.getPuntiVitaMostro() - danno));
            try { paladino.setPuntiVita(Math.min(paladino.getPuntiVita() + danno / 2, 99999)); } catch (Exception ignored) {}
            System.out.println(paladino.getNomeP() + " ruba vita a " + mostro.getNomeMostro() + " (" + danno + " danno).");
            break;
        default:
            // fallback: danno normale
            int base = Math.max(1, paladino.getAttacco() - mostro.getDifesaMostro());
            mostro.setPuntiVitaMostro(Math.max(0, mostro.getPuntiVitaMostro() - base));
            System.out.println(paladino.getNomeP() + " colpisce " + mostro.getNomeMostro() + " per " + base);
            break;
    }

    paladino.setPuntiMana(Math.max(0, paladino.getPuntiMana() - tipoMagiaSacra.getCostoMana()));

    // gestione morte / XP (semplice)
    if (mostro.getPuntiVitaMostro() <= 0) {
        try { paladino.setEsperienza(paladino.getEsperienza() + 10); } catch (Exception ignored) {}
        System.out.println(mostro.getNomeMostro() + " è stato sconfitto da " + paladino.getNomeP());
    }

    return 1; // ritorna 1 = magia eseguita (potresti restituire danno effettivo se preferisci)
}

// Helper che sceglie la magia internamente (heuristica semplice)
private TipoMagiaSacra scegliMagiaPerPaladino(Paladino paladino, Mostro mostro, Combattimento combattimento) {
    // esempio di regole:
    // - se HP mostro bassi -> RUBAVITA per finire e curarsi
    // - se paladino ha poca vita -> RUBAVITA
    // - se mostro ha molta difesa -> CORROSIONE
    // - altrimenti AVVELENAMENTO
    try {
        if (paladino.getPuntiVita() < 15 && paladino.getPuntiMana() >= TipoMagiaSacra.RUBAVITA.getCostoMana()) {
            return TipoMagiaSacra.RUBAVITA;
        }
        if (mostro.getPuntiVitaMostro() <= 12 && paladino.getPuntiMana() >= TipoMagiaSacra.RUBAVITA.getCostoMana()) {
            return TipoMagiaSacra.RUBAVITA;
        }
        if (mostro.getDifesaMostro() > 5 && paladino.getPuntiMana() >= TipoMagiaSacra.CORROSIONE.getCostoMana()) {
            return TipoMagiaSacra.CORROSIONE;
        }
        if (paladino.getPuntiMana() >= TipoMagiaSacra.AVVELENAMENTO.getCostoMana()) {
            return TipoMagiaSacra.AVVELENAMENTO;
        }
    } catch (Exception ignored) {}
    // fallback
    return TipoMagiaSacra.AVVELENAMENTO;
}
    // Enum per i tipi di magia sacra disponibili
    public enum TipoMagiaSacra {
        RUBAVITA(8),
        CORROSIONE(12),
        AVVELENAMENTO(15),
        MALATTIA(10),
        STORDIMENTO(10);

        private final int costoMana;

        TipoMagiaSacra(int costoMana) {
            this.costoMana = costoMana;
        }

        public int getCostoMana() {
            return costoMana;
        }
    }
}