package ontology;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class KBAgent extends Agent{
	
	private Model model;
	
	protected void setup() {
		System.out.println("'" + getLocalName() + "' initiated.\t(" + this.getClass() + ")" );
		
	    model = ModelFactory.createDefaultModel();
		InputStream in = FileManager.get().open("file:res/td5.n3");
        model.read(in, null, "TURTLE");

		// register to Directory Facilitator (DF)
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("KnowledgeBase");
		sd.setName(getLocalName());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}

        // add request Behaviour
        addBehaviour(new RequestBehaviour());
    }

	private Model getModel(){
		return model;
	}
	
	private class RequestBehaviour extends Behaviour{

		@Override
		public void action() {
			MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage msg = receive(msgTemplate);
    		
            if (msg != null) {
            	String query = msg.getContent();
            	String result = runSelectQuery(query, getModel());
            	ACLMessage msgReply = msg.createReply();
            	if (result==null){
            		msgReply.setPerformative(ACLMessage.FAILURE);
            		msgReply.setContent("Error while executing the query");
            		send(msgReply);
            	}
            	else{
            		msgReply.setPerformative(ACLMessage.INFORM);
            		msgReply.setContent(result);
            		send(msgReply);
            	}
            }
			
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

	private static String runSelectQuery(String query, Model model) {

		try {
			QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ResultSet r = queryExecution.execSelect();
			ResultSetFormatter.outputAsCSV(baos,r);
			queryExecution.close();

			return baos.toString();
		} catch (Exception e) {
			return null;
		}
	}

}
