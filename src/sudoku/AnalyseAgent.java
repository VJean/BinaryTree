package sudoku;

import com.fasterxml.jackson.core.JsonProcessingException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import sun.util.resources.cldr.gl.CalendarData_gl_ES;

import java.io.IOException;


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
            MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage msg = receive(msgTemplate);

            if (msg != null) {
                CaseGrille[] cases;
                ACLMessage msgReply = msg.createReply();

                // deserialize message
                try {
                    cases = CaseGrille.deserialize(msg.getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                handleCases(cases);

                // answer to the request
                try {
                    msgReply.setPerformative(ACLMessage.INFORM);
                    msgReply.setContent(CaseGrille.serialize(cases));
                    send(msgReply);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public boolean done() {
            return false;
        }
    }

    private CaseGrille[] handleCases(CaseGrille[] set){
        return set;
    }
}
