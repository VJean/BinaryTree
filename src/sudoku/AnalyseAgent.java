package sudoku;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;


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
}
