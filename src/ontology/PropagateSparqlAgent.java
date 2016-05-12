package ontology;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

import static jade.lang.acl.MessageTemplate.MatchConversationId;
import static jade.lang.acl.MessageTemplate.MatchPerformative;

/**
 * Created by JeanV on 28/04/2016.
 */
public class PropagateSparqlAgent extends Agent {
    protected void setup() {
        System.out.println("'" + getLocalName() + "' initiated.\t(" + this.getClass() + ")" );

        addBehaviour(new HandleRequestsBehaviour());
    }

    private class HandleRequestsBehaviour extends CyclicBehaviour {
        int requestsCount = 0;

        @Override
        public void action() {
            MessageTemplate pattern = MatchPerformative(ACLMessage.REQUEST);
            ACLMessage req = getAgent().receive(pattern);

            if (req != null)
                getAgent().addBehaviour(new PropagateBehaviour(requestsCount++, req.getContent()));

        }
    }

    private class PropagateBehaviour extends Behaviour {
        private String convId;
        private String request;
        private boolean done = false;

        PropagateBehaviour(int id, String content) {
            super();

            convId = String.valueOf(id);
            request = content;

            // send Request to KBAgent
            // Nota : searching for agent in DFAgent doesn't seem to work...
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent(request);
            msg.setConversationId(convId);
            msg.addReceiver(new AID("KnowledgeBase",AID.ISLOCALNAME));

            send(msg);
        }

        @Override
        public void action() {
            MessageTemplate pattern = MessageTemplate.MatchConversationId(this.convId);
            ACLMessage res = receive(pattern);

            // wait for message
            if (res != null)
            {
                switch(res.getPerformative()) {
                    case ACLMessage.INFORM:
                        handleInform(res);
                        break;
                    case ACLMessage.FAILURE:
                        handleFailure(res);
                        break;
                }
                done = true;
            } else {
                block();
            }
        }

        @Override
        public boolean done() {
            return done;
        }

        private void handleInform(ACLMessage msg){
            System.out.println("------SparqlResult------\n"
                            + "---Request\n" + this.request
                            + "\n\n---Returned\n" + msg.getContent()
                            + "------------------------");
        }

        private void handleFailure(ACLMessage msg){
            System.out.println("Error !\n\t"+msg.getContent());
        }
    }

}
