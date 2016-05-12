package ontology;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;


public class SecondaryBoot {
	
	public static String SECONDARY_PROPERTIES_FILE = "properties/SecondaryContainerProperties";

	public static void main(String[] args) {
		Runtime rt = Runtime.instance();
		Profile p = null;
		try{
			p = new ProfileImpl(SECONDARY_PROPERTIES_FILE);
			ContainerController cc = rt.createAgentContainer(p);
			// create KB agent
			AgentController kbAgent = cc.createNewAgent("KnowledgeBase", "ontology.KBAgent", null);
			kbAgent.start();
			// create PropagateSparqlAgent agent
			AgentController propSparqlAgent = cc.createNewAgent("PropagateSparql", "ontology.PropagateSparqlAgent", null);
			propSparqlAgent.start();
			// create KB agent
			AgentController geoAgent = cc.createNewAgent("GeodataBase", "ontology.GeodataAgent", null);
			geoAgent.start();
			// create PropagateSparqlAgent agent
//			AgentController propGeoSparqlAgent = cc.createNewAgent("PropagateSparql", "ontology.PropagateGeoSparqlAgent", null);
//			propGeoSparqlAgent.start();

		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
