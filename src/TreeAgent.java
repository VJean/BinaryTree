import com.fasterxml.jackson.core.JsonProcessingException;

import jade.core.AID;
import jade.core.Agent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class TreeAgent extends Agent{
	String root;
	int currentCid = 0;
	
	protected void setup() {
		System.out.println("====>New TreeAgent '"+getLocalName()+"' initiated (...Je suis un arbre mais pas en bois)");
		
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
				
				if(msg.contains(",")){ // insert, inTree
					String[] split = msg.split(",");
					typeMsg = split[0];
					valueMsg = Integer.parseInt(split[1]);
				} else { // print
					typeMsg = msg;
				}
				
				switch(typeMsg){
					case "print":
						if (root == null)
							answer(message, ACLMessage.INFORM, "cet arbre n'a pas encore de racine");
						else
							this.getAgent().addBehaviour(new PrintBehaviour(++currentCid));
						break;
					case "insert":
						if (root == null){
						//	root = new NodeAgent();
							AgentContainer cc = this.getAgent().getContainerController();
							try {
								String rootname= getLocalName() + valueMsg.toString();
								cc.createNewAgent(rootname, "NodeAgent", null);
								root = rootname;
							} catch (StaleProxyException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						this.getAgent().addBehaviour(new InsertBehaviour(valueMsg, ++currentCid));
						break;
					case "inTree":
						if (root == null)
							answer(message, ACLMessage.INFORM, "cet arbre n'a pas encore de racine");
						else
							this.getAgent().addBehaviour(new InTreeBehaviour(valueMsg, ++currentCid));
						break;
					default:
						answer(message, ACLMessage.FAILURE,"Requête mal formée : '" + msg + "'");
						break;
				}
			}
		}

		private void answer(ACLMessage message, int perf, String msg) {
			System.out.println("requête "+message.getContent()+":");
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

	public class InTreeBehaviour extends OneShotBehaviour {
		Integer convId;
		
		public InTreeBehaviour(int valueMsg, int convId) {
			super();
			
			this.convId = convId;
			
			TreeMsgContent reqcontent = new TreeMsgContent("inTree", valueMsg);
			ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
			req.addReceiver(new AID(root,AID.ISLOCALNAME));
			req.setConversationId(this.convId.toString());
			try {
				req.setContent(TreeMsgContent.serialize(reqcontent));
				send(req);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void action() {
			// TODO Auto-generated method stub

		}

	}

	public class InsertBehaviour extends OneShotBehaviour {
		int convId;
		
		public InsertBehaviour(int valueMsg, int convId) {
			super();
			
			this.convId = convId;
			
			TreeMsgContent reqcontent = new TreeMsgContent("insert", valueMsg);
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

		@Override
		public void action() {
			// TODO Auto-generated method stub

		}

	}

	public class PrintBehaviour extends OneShotBehaviour {
		int convId;
		
		public PrintBehaviour(int convId) {
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

		@Override
		public void action() {
			// TODO Auto-generated method stub

		}
	}
}
