package binarytree;

import com.fasterxml.jackson.core.JsonProcessingException;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;


public class NodeAgent extends Agent{
	Integer value;
	String leftSon;
	String rightSon;
	
	protected void setup() {
		System.out.println("====>New binarytree.NodeAgent '"+getLocalName()+"' initiated");
		
		addBehaviour(new LaunchBehaviour());
	}
	
	private class LaunchBehaviour extends Behaviour{
		public LaunchBehaviour(){
			
		}

		@Override
		public void action() {
			MessageTemplate modele = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);	
			ACLMessage message = receive(modele);
			if (message != null) {
				String msg = message.getContent();
				String typeMsg;
				Integer valueMsg=0;
				String convId = message.getConversationId();

                TreeMsgContent treeMsg = null;
                try {
                    treeMsg = TreeMsgContent.deserialize(msg);
                    typeMsg = treeMsg.getType();
                    valueMsg = treeMsg.getValue();

                    switch(typeMsg){
                        case "print":
                            break;
                        case "insert":
                            break;
                        case "inTree":
                            break;
                        default:
                            answer(message, ACLMessage.FAILURE,"Requête mal formée : '" + msg + "'");
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
			}
		}
		private void answer(ACLMessage message, int perf, String msg) {
			System.out.println("requ�te "+message.getContent()+":");
			System.out.println("\t"+msg);
			
			ACLMessage fail = message.createReply();
			fail.setContent(msg);
			fail.setPerformative(perf);
			this.getAgent().send(fail);
		}

		@Override
		public boolean done() {
			
			return false;
		}
	}
}