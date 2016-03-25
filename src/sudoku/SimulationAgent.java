package sudoku;

import java.util.ArrayList;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class SimulationAgent extends Agent {

	private ArrayList<AID> analyseAgents = new ArrayList<AID>();
	private Boolean isStopped = false;
	
	protected void setup() {
		System.out.println("Agent initiated : type:" + this.getClass() + " name:" + getLocalName());
		// add registration behaviour
		this.addBehaviour(new RegistrationBehaviour());
		// add cyclic behaviour
		// 100ms: arbitrary period
		this.addBehaviour(new SimulationBehaviour(this, 100));
	}
	
	private class RegistrationBehaviour extends Behaviour {

		@Override
		public void action() {
			// wait for requests
			// get AID in msg content
			// store AID in analyseAgents
			MessageTemplate modele = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);	
			ACLMessage message = receive(modele);
			if (message != null) {
				if (message.getContent().equalsIgnoreCase("register"))
				{
					analyseAgents.add(message.getSender());
					System.out.println(getLocalName() + " registered agent " + message.getSender().getLocalName());
				}
			}
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return analyseAgents.size() == 27;
		}
	}
	
	private class SimulationBehaviour extends TickerBehaviour {

		public SimulationBehaviour(Agent a, long period) {
			super(a, period);
			
		}

		@Override
		protected void onTick() {
			// check that all 27 agents are registered
			if (analyseAgents.size() != 27 && !isStopped)
				return;
			
			for (int i = 0; i < 27; i++) {
				AID agent = analyseAgents.get(i);
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.addReplyTo(agent);
				msg.addReceiver(new AID("Environment",AID.ISLOCALNAME));
				msg.setContent(String.valueOf(i));
				send(msg);
			}
		}
	}
	
	private class StopSimulationBehaviour extends Behaviour {

		@Override
		public void action() {
			MessageTemplate modele = MessageTemplate.MatchPerformative(ACLMessage.INFORM);	
			ACLMessage message = receive(modele);
			if (message != null) {
				if (message.getContent().equalsIgnoreCase("stop"))
				{
					isStopped = true;
				}
			}
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}
	}

}
