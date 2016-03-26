package sudoku;

import java.util.ArrayList;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class SimulationAgent extends Agent {

    private ArrayList<AID> analyseAgents = new ArrayList<AID>();
    private Boolean isStopped = false;

    protected void setup() {
        System.out.println("'" + getLocalName() + "' initiated.\t(" + this.getClass() + ")");

        // register to Directory Facilitator (DF)
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("simulation");
        sd.setName(getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

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
                if (message.getContent().equalsIgnoreCase("register")) {
                    analyseAgents.add(message.getSender());
                    System.out.println(getLocalName() + " registered " + message.getSender().getLocalName());
                }
            }
        }

        @Override
        public boolean done() {
            return analyseAgents.size() == 27;
        }
    }

    private class SimulationBehaviour extends TickerBehaviour {

        public SimulationBehaviour(Agent a, long period) {
            super(a, period);

        }

        @Override
        protected void onTick() {
            // check that all 27 agents are registered and simulation is running
            if (analyseAgents.size() != 27 || isStopped)
                return;

            // search for the Environment Agent
            DFAgentDescription dfd = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("environment");
            dfd.addServices(sd);

            try {
                DFAgentDescription[] result = DFService.search(this.getAgent(), dfd);
                if (result.length > 0) {
                    AID envAgent = result[0].getName();

                    for (int i = 0; i < 27; i++) {
                        AID agent = analyseAgents.get(i);
                        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                        msg.addReplyTo(agent);
                        msg.addReceiver(envAgent);
                        msg.setContent(String.valueOf(i));
                        send(msg);
                    }
                }

            } catch (FIPAException e) {
                e.printStackTrace();
            }
        }
    }

    private class StopSimulationBehaviour extends Behaviour {

        @Override
        public void action() {
            MessageTemplate modele = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage message = receive(modele);
            if (message != null) {
                if (message.getContent().equalsIgnoreCase("stop")) {
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
