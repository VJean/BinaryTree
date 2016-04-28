package ontology;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class KBAgent extends Agent{
	protected void setup() {
		System.out.println("'" + getLocalName() + "' initiated.\t(" + this.getClass() + ")" );

        // add request Behaviour
        addBehaviour(new RequestBehaviour());
    }
	
	private class RequestBehaviour extends Behaviour{

		@Override
		public void action() {
			MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage msg = receive(msgTemplate);
            
            
			
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

}
