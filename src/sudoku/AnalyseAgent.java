package sudoku;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;


public class AnalyseAgent extends Agent {
	protected void setup() {
		System.out.println("Agent initiated : type:" + this.getClass() + " name:" + getLocalName());
		
		// add register Behaviour
		addBehaviour(new RegisterBehaviour());
	}
	
	private class RegisterBehaviour extends OneShotBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.setContent("register");
			
			msg.addReceiver(new AID("Simulation",AID.ISLOCALNAME));
			
			send(msg);
		}
		
	}
}
