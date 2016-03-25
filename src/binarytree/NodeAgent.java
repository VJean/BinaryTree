package binarytree;

import com.fasterxml.jackson.core.JsonProcessingException;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


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
				
				TreeMsgContent treeMsg = TreeMsgContent.deserialize(msg);
				typeMsg = treeMsg.getType();
				valueMsg = treeMsg.getValue();
				
				switch(typeMsg){
					case "print":
						this.getAgent().addBehaviour(new PrintBehaviour(convId));
						break;
					case "insert":
//							AgentContainer cc = this.getAgent().getContainerController();
//							try {
//								String rootname= getLocalName() + valueMsg.toString();
//								cc.createNewAgent(rootname, "binarytree.NodeAgent", null);
//								root = rootname;
//							} catch (StaleProxyException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
						
						if(valueMsg == value){
							answer(message, ACLMessage.INFORM, value.toString() + " d�j� ins�r�");
						//TODO envoyer une requ�te d'insertion sur le fils gauche ou droit selon la valeur
						}else if(valueMsg <= value){
							
						}
						else{
							
						}
						this.getAgent().addBehaviour(new TreeAgent.InsertBehaviour(valueMsg, ++currentCid));
						break;
					case "inTree":
						if (root == null)
							answer(message, ACLMessage.INFORM, "cet arbre n'a pas encore de racine");
						else
							this.getAgent().addBehaviour(new TreeAgent.InTreeBehaviour(valueMsg, ++currentCid));
						break;
					default:
						answer(message, ACLMessage.FAILURE,"Requ�te mal form�e : '" + msg + "'");
						break;
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
	
	public class PrintBehaviour extends OneShotBehaviour {
		String convId;
		
		public PrintBehaviour(String convId) {
			super();
			
			this.convId = convId;
			
			TreeMsgContent reqcontent = new TreeMsgContent("print");
			ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
			req.addReceiver(new AID(root,AID.ISLOCALNAME));
			try {
				req.setContent(TreeMsgContent.serialize(reqcontent));
				send(req);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}