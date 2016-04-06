package sudoku;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.util.leap.HashMap;


public class AnalyseAgent extends Agent {
    protected void setup() {
		System.out.println("'" + getLocalName() + "' initiated.\t(" + this.getClass() + ")" );

        // register to Directory Facilitator (DF)
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("analyse");
        sd.setName(getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        // add register Behaviour
        addBehaviour(new RegisterBehaviour());
    }

    private class RegisterBehaviour extends OneShotBehaviour {

        @Override
        public void action() {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent("register");

            // search for the Simulation Agent
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("simulation");
			dfd.addServices(sd);

			try {

				DFAgentDescription[] result = DFService.search(this.getAgent(), dfd);
				if (result.length > 0) {

					msg.addReceiver(result[0].getName());

					send(msg);
				}

			} catch (FIPAException e) {
				System.out.print(getLocalName() + "could not register to simulation :\n\t" + e.getMessage());
			}
        }
    }

    /**
     * Comportement principal d'un AnalyseAgent :
     * <ol>
     *     <li>Réception des requêtes de l'environnement</li>
     *     <li>Traitement de la liste des case reçues</li>
     *     <li>Envoi des cases traitées par retour de message</li>
     * </ol>
     */
    private class AnalyseBehaviour extends Behaviour {
        @Override
        public void action() {

        }

        @Override
        public boolean done() {
            return false;
        }
    }

    private void handleCases(CaseGrille[] set){
    	Map<Integer, ArrayList<Integer>> possibleToIndex = (Map<Integer, ArrayList<Integer>>) new HashMap();
    	for(int i=0;i<set.length;i++){
    		if(set[i].getValeur() == 0){
    			uniqPossibleValue(set[i]);
    			// On met dans la map les index qui correspondent à chaque valeur des possibles
    			for(int j=0;j<set[i].getPossibles().size();j++){
    				int valuePossible = set[i].getPossibles().get(j);
    				// Si la valeur du possible existe déjà dans la map, on ajoute l'index correspondant
    				if(possibleToIndex.containsKey(valuePossible)){
    					possibleToIndex.get(valuePossible).add(i);
    				}
    				// Sinon, on ajoute une entrée dans la map avec le premier index à ajouter
    				else{
    					possibleToIndex.put(valuePossible, new ArrayList<Integer>(i));
    				}
    			}
    		}
    		else{
    			removePossibleValue(set, i);
    		}
    	}
    	uniqIndexToPossibleValue(set, possibleToIndex);
    }

    /**
     * Si une valeur n'est possible que pour une case, alors celle-ci prend cette valeur
     * @param possibleToIndex : valeurs possibles mappées à l'ensemble des index correspondants
     */
    private void uniqIndexToPossibleValue(CaseGrille[] set, Map<Integer, ArrayList<Integer>> possibleToIndex) {
    	for (Entry<Integer, ArrayList<Integer>> entry : possibleToIndex.entrySet()) {
			Integer value = entry.getKey();
			ArrayList<Integer> indexes = entry.getValue();
			
    		if(indexes.size() == 1){
    			set[indexes.get(0)].setValeur(value);
    		}
		}
	}

	/**
     * Supprime la valeur d'une case à la liste des possibles des autres cases non déterminées de son tableau
     * @param set : tableau des cases à modifier
     * @param i : index de la case à ne pas modifier (dont on utilise la valeur)
     */
    private void removePossibleValue(CaseGrille[] set, int i) {
    	int valueToRemove =  set[i].getValeur();
		for(int j=0;j<set.length;j++){
			if(j!=i && set[j].getValeur()==0){
				set[j].getPossibles().remove((Integer)valueToRemove);
			}
		}
	}

    /**
     * Donne à la valeur de la case l'unique valeur possible, si c'est le cas
     * @param caseG : case à modifier
     */
	private void uniqPossibleValue(CaseGrille caseG) {
		if(caseG.getPossibles().size() == 1){
			caseG.setValeur(caseG.getPossibles().get(0));	
			caseG.getPossibles().remove(0);
		}
	}
}
