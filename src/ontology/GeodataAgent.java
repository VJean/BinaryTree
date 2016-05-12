package ontology;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static com.hp.hpl.jena.enhanced.BuiltinPersonalities.model;

/**
 * Created by JeanV on 12/05/2016.
 */
public class GeodataAgent extends Agent {
    protected void setup() {
        System.out.println("'" + getLocalName() + "' initiated.\t(" + this.getClass() + ")" );

//        System.setProperty("http.proxyHost","proxyweb.utc.fr");
//        System.setProperty("http.proxyPort","3128");

        // register to Directory Facilitator (DF)
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("GeodataBase");
        sd.setName(getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        // add request Behaviour
        addBehaviour(new RequestBehavior());
    }

    private class RequestBehavior extends CyclicBehaviour {
        @Override
        public void action() {
            MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage msg = receive(msgTemplate);

            if (msg != null) {
                String query = msg.getContent();
                String result = runSelectQuery(query);
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
    }

    private static String runSelectQuery(String query) {
        try {
            //Query q = QueryFactory.create(query, Syntax.syntaxSPARQL);


            QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://linkedgeodata.org/sparql", query);
            ResultSet r = queryExecution.execSelect();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ResultSetFormatter.outputAsCSV(baos,r);
            queryExecution.close();

            return baos.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
