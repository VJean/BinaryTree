package sudoku;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class EnvironmentAgent extends Agent {

    private int[][] initialGrid = {{5,0,0,0,0,4,0,0,8},
                            {0,1,0,9,0,7,0,0,0},
                            {0,9,2,8,5,0,7,0,6},
                            {7,0,0,3,0,1,0,0,4},
                            {0,0,0,0,0,0,0,0,0},
                            {6,0,0,2,0,8,0,0,1},
                            {1,0,8,0,3,2,4,9,0},
                            {0,0,0,1,0,6,0,5,0},
                            {3,0,0,7,0,0,0,0,2}};
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
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                grid[i][j]=new CaseGrille(initialGrid[i][j]);
            }
        }

        // add behaviours

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
            int baseLine = Math.floorDiv(index, 3) * 3; // no time to explain just accept it
            int baseColumn = index % 3; // same

            for (int i = baseLine; i < baseLine + 3; i++) {
                for (int j = baseColumn; j < baseColumn + 3; j++) {
                    result[i+j] = grid[i][j];
                }
            }
            return result;

        }

        return null;
    }


}
