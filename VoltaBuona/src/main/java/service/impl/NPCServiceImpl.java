package service.impl;

import java.util.Scanner;

import domain.Evento;
import domain.NPC;
import domain.Oggetto;
import domain.Personaggio;
import domain.Stanza;
import domain.Trappola;
import domain.Zaino;
import service.PersonaIncontrataService;

public class  NPCServiceImpl implements PersonaIncontrataService {

    private final Scanner scanner = new Scanner(System.in);
    private final NPC npc = null;


    public String parla(Personaggio personaggio, NPC npc) {

        if (npc.haInteragito()) {
            System.out.println("\nL'NPC " + npc.getNomeNPC() + " ti ha già parlato.");
            return null;
        }

        System.out.println(npc.proponiRebus());

        System.out.print("\nInserisci la tua risposta: ");
        String risposta = scanner.nextLine();

        boolean corretta = risolviRebus(npc, personaggio, risposta);

        if (corretta) {
            System.out.println("\n Risposta corretta! L’NPC ti ricompensa.");
            donaOggettoSingolo(npc, personaggio);
        } else {
            System.out.println("\n Risposta errata. L’NPC non ti dona nulla.");
        }

        npc.setHaInteragito(true);
        return risposta;
    }

    
    public boolean risolviRebus(NPC npc, Personaggio personaggio, String risposta) {
        return npc.verificaRisposta(risposta);
    }


    
    public void donaOggettoSingolo(NPC npc, Personaggio personaggio) {

        if (!npc.haOggettiDaDare()) {
            System.out.println("L’NPC non ha oggetti da darti.");
            return;
        }

        Oggetto dono = npc.daOggetto();
        System.out.println("L’NPC ti dona: " + dono.getNome());

        aggiungiOggettoAZaino(personaggio, dono);
     
    }
    @Override
    public void rimuoviEventoDaStanza(Stanza stanza, Evento evento){
        stanza.getListaEventiAttivi().remove(evento);
    };

    @Override
    public boolean attivaEvento(Personaggio personaggio, Evento e){
        if (e instanceof NPC npc) {
             Stanza stanza = ((NPC) e).getPosizioneCorrenteNPC();
            System.out.println("Incontri un NPC: " + npc.getNomeNPC());
            parla(personaggio, npc);
            rimuoviEventoDaStanza(stanza, e);
            // Interazione NPC consuma il turno
            return true;
        }
        return false;
    };

    @Override   
    public void eseguiEventiInStanza(Personaggio personaggio, Stanza stanza){   
        for (Evento e : stanza.getListaEventiAttivi()) {
            boolean termina = attivaEvento(personaggio, e);
            if (termina) return;
        }
    }

   
    private void aggiungiOggettoAZaino(Personaggio personaggio, Oggetto oggetto) {

        Zaino zaino = personaggio.getZaino();

        if (zaino.getListaOggetti().size() >= zaino.getCapienza()) {
            System.out.println("⚠ Il tuo zaino è pieno! Non puoi prendere " + oggetto.getNome());
            return;
        }

        zaino.getListaOggetti().add(oggetto);
        System.out.println("👉 Oggetto aggiunto allo zaino: " + oggetto.getNome());
    }




    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


}