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

        this.addBehaviour(new RegistrationBehaviour());
        this.addBehaviour(new SimulationBehaviour(this, 100));
        this.addBehaviour(new StopSimulationBehaviour());
    }

    private class RegistrationBehaviour extends Behaviour {

        @Override
        public void action() {
            // wait for requests
            // get AID in msg content
            // store AID in analyseAgents
            MessageTemplate modele = MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE);
            ACLMessage message = receive(modele);
            if (message != null) {
                analyseAgents.add(message.getSender());
                System.out.println(getLocalName() + " registered " + message.getSender().getLocalName());
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

                    // get the environment status
                    ACLMessage isFinishedMsg = new ACLMessage(ACLMessage.REQUEST);
                    isFinishedMsg.addReceiver(envAgent);
                    isFinishedMsg.setContent("status");
                    send(isFinishedMsg);

                    // send the index
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
                if (message.getContent().equalsIgnoreCase("finished")) {
                    isStopped = true;
                    System.out.println("###################### Sudoku done.");

                } else {
                    System.out.println("###################### Sudoku still running.");

                }
            }
        }

        @Override
        public boolean done() {
            return false;
        }
    }

}
