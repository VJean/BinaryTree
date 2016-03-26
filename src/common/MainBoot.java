package common;

import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;

public class MainBoot {

	public static String MAIN_PROPERTIES_FILE = "properties/MainContainerProperties";

	public static void main(String[] args) {
		Runtime rt = Runtime.instance();
		Profile p = null;
		try{
			p = new ProfileImpl(MAIN_PROPERTIES_FILE);
			AgentContainer mc = rt.createMainContainer(p);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
