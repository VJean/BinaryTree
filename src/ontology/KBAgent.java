package ontology;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class KBAgent extends Agent{
	
	private Model model;
	
	protected void setup() {
		System.out.println("'" + getLocalName() + "' initiated.\t(" + this.getClass() + ")" );
		
	    Model model = ModelFactory.createDefaultModel();
		InputStream in = FileManager.get().open("file:res/td5.n3");
        model.read(in, null, "TURTLE");
        
        // add request Behaviour
        addBehaviour(new RequestBehaviour());
    }
	public Model getModel(){
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
	public static String runSelectQuery(String query, Model model) {

        QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ResultSet r = queryExecution.execSelect();
        ResultSetFormatter.outputAsCSV(baos,r);
        queryExecution.close();
        
        return baos.toString();
    }

}
