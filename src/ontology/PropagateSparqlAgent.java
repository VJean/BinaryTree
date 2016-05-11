package ontology;

import jade.core.AID;
import jade.core.Agent;
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

    private class PropagateBehaviour extends OneShotBehaviour {
        private String convId;

        public PropagateBehaviour(int id, String content) {
            super();

            convId = String.valueOf(id);

            // send Request to KBAgent

            DFAgentDescription dfd = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("KnowledgeBase");
            dfd.addServices(sd);

//            try {
//                DFAgentDescription[] result = DFService.search(this.getAgent(), dfd);
//                if (result.length > 0) {
//                    AID kbAgent = result[0].getName();

                    ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                    msg.setContent(content);
                    msg.setConversationId(convId);
                    msg.addReceiver(new AID("KnowledgeBase",AID.ISLOCALNAME));

                    send(msg);
                //}
//            } catch (FIPAException e) {
//                e.printStackTrace();
//            }

        }

        @Override
        public void action() {
            MessageTemplate pattern = MessageTemplate.and(MatchPerformative(ACLMessage.INFORM),
                                                    MatchConversationId(this.convId));
            ACLMessage res = getAgent().receive(pattern);

            // wait for message
            if (res != null){
            	System.out.println(res.getSender().getLocalName() + " answered with INFORM");
            	System.out.println(res.getContent());
            	 // receive csv
//              ArrayList<String> split =  new ArrayList<String>(Arrays.asList(res.getContent().split("\\n")));
//              System.out.println(
//                      "===== convId : "+ convId + "\n"
//                      + split.size() + " résultats");
            }
            else
            	block();
            }

        private void handleInform(ACLMessage msg){

        }

        private void handleFailure(ACLMessage msg){

        }



    }

}
