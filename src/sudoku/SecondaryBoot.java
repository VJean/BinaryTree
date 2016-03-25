package sudoku;
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
			// create simulation agent
			AgentController simAgent = cc.createNewAgent("Simulation", "sudoku.SimulationAgent", null);
			simAgent.start();
			// create env agent
			AgentController envAgent = cc.createNewAgent("Environment", "sudoku.EnvironmentAgent", null);
			envAgent.start();
			// create 27 identical analyse agents
			for (int i = 0; i < 27; i++) {
				AgentController analAgent = cc.createNewAgent("Analyser"+i, "sudoku.AnalyseAgent", null);
				analAgent.start();
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
