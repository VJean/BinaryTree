package sudoku;

import com.fasterxml.jackson.core.JsonProcessingException;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class EnvironmentAgent extends Agent {

    private CaseGrille[][] grid = new CaseGrille[9][9];

    protected void setup() {
        System.out.println("'" + getLocalName() + "' initiated.\t(" + this.getClass() + ")" );

		// register to Directory Facilitator (DF)
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("environment");
		sd.setName(getLocalName());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		// init grid
		grid = loadFromFile();

        // add behaviours
		this.addBehaviour(new ReceiveCasesBehaviour());
		this.addBehaviour(new ReceiveRequestsBehaviour());
    }

	private CaseGrille[][] loadFromFile() {
		CaseGrille[][] result = new CaseGrille[9][9];

		File file = new File("res/sudoku_grid");
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);

			for(int i=0;i<9;i++){
				for(int j=0;j<9;j++){
					result[i][j]=new CaseGrille(scanner.nextInt());
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	private CaseGrille[] getCases(int index){
        if(index>=0 && index<=8){
            return grid[index];

        } else if(index>=9 && index<=17){
            index %= 9;
            CaseGrille result[] = new CaseGrille[9];
            for(int i=0;i<9;i++){
                result[i] = grid[i][index];
            }
            return result;

        } else if(index>=18 && index<=26){
            index %= 9;
            CaseGrille result[] = new CaseGrille[9];
            int baseLine = Math.floorDiv(index, 3) * 3;
            int baseColumn = index % 3 * 3;
			int resIndex = 0;
            for (int i = baseLine; i < baseLine + 3; i++) {
                for (int j = baseColumn; j < baseColumn + 3; j++) {
                    result[resIndex++] = grid[i][j];
                }
            }
            return result;
        }
        return null;
    }

	private class ReceiveCasesBehaviour extends Behaviour {
		@Override
		public void action() {
			MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			ACLMessage msg = receive(msgTemplate);

			if (msg != null) {
				CaseGrille[] received = new CaseGrille[9];
				int convId = Integer.parseInt(msg.getConversationId());

				try {
					received = CaseGrille.deserialize(msg.getContent());
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (received != null) {
					CaseGrille[] base = getCases(convId);
					// update the cases
					update(base, received);
				}
			}

		}

		private void update(CaseGrille[] oldSet, CaseGrille[] newSet) {
			for (int i = 0; i < 9; i++){
				CaseGrille oldCase = oldSet[i];
				CaseGrille newCase = newSet[i];


				if (oldCase.getValeur() == 0) {

					// affect new value
					if (newCase.getValeur() != oldCase.getValeur())
                        oldCase.setValeur(newCase.getValeur());
					// perform intersection
					oldCase.getPossibles().retainAll(newCase.getPossibles());
				}
			}
		}

		@Override
		public boolean done() {
			return false;
		}
	}
	
	private class PrintSudokuBehaviour extends Behaviour {
		@Override
		public void action() {
			MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage msg = receive(msgTemplate);

			if (msg != null && msg.getContent().equalsIgnoreCase("print")) {
				print();
			}

		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

	private class ReceiveRequestsBehaviour extends Behaviour {

		@Override
		public void action() {
			MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage msg = receive(msgTemplate);

			if (msg != null) {
				String content = msg.getContent();
				if (Pattern.matches("\\d+", content)) {
					Integer index = Integer.parseInt(msg.getContent());
					if (index < 27 && index >= 0) {
						ACLMessage forward = msg.createReply();
						// set conversationId with the requested row/column/square index.
						forward.setConversationId(index.toString());

						CaseGrille[] caseSet = getCases(index);

						try {
							forward.setContent(CaseGrille.serialize(caseSet));
							send(forward);
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
					}
				} else if (content.equalsIgnoreCase("status")) {
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.INFORM);
					if (isFinished())
						reply.setContent("finished");
					else
						reply.setContent("running");

					send(reply);

				} else if (content.equalsIgnoreCase("print")) {
					print();
				}
			}
		}

		@Override
		public boolean done() {
			return false;
		}
	}


	private void print() {
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				System.out.print(grid[i][j].getValeur() + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	private boolean isFinished() {
		boolean result = true;

		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				if (grid[i][j].getValeur() == 0)
					result = false;
			}
		}

		return result;
	}
}
