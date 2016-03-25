package binarytree;

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
			AgentController tree = cc.createNewAgent("Tree", "binarytree.TreeAgent", null);
			
			tree.start();
			
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
