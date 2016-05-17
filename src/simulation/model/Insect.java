package simulation.model;

import sim.engine.SimState;
import sim.field.grid.Grid2D;
import sim.util.Bag;
import sim.util.Int2D;

import java.util.Random;

public class Insect extends AgentType {
    private int distanceDeplacement;
    private int distancePerception;
    private int chargeMax;

    private int energie = Constants.MAX_ENERGY;
    private int charge = 0;

    public Insect() {
        // points à répartir
        int cap = Constants.CAPACITY_POINTS;
        // générateur aléatoire
        Random rand = new Random();

        chargeMax = rand.nextInt(Math.max(Constants.MAX_LOAD, cap)) + 1;
        cap = cap - chargeMax + 1;

        distanceDeplacement = rand.nextInt(cap) + 1;
        cap = cap - distanceDeplacement + 1;

        distancePerception = cap + 1;
    }

    @Override
    public void step(SimState state) {
        Beings beings = (Beings) state;
        // vie
        vie(beings);
        // perception
        Int2D target = perception(beings);
        
        //test
//        if(target!=null)
//        	System.out.println("x:"+this.x+" ; y:"+this.y+" ; target.x:"+target.x+" ; target.y:"+target.y);
//        else
//        	System.out.println("x:"+this.x+" ; y:"+this.y+" ; target:null");
        
        // deplacement
        if (target != null){
            move(target, beings);
        }
        else{
        	randMove(beings);
        }
    }

    private void randMove(Beings beings){
    	Random randMove = new Random();
        int moveDir;
    	moveDir = randMove.nextInt(2);
    	switch(moveDir){
    	case 0:
    		if(beings.isFree(x+1, y)){
    			beings.yard.setObjectLocation(this, beings.yard.stx(this.x + 1), y);
    			this.x = beings.yard.stx(x+1);
    		}
    		break;
    	case 1:
    		if(beings.isFree(x, y+1)){
    			beings.yard.setObjectLocation(this, x, beings.yard.stx(this.y + 1));
    			this.y = beings.yard.stx(y+1);
    		}
    		break;
    	}
    }
    
    private void move(Int2D target, Beings beings) {
    	int x1 = Math.max(this.x, target.x) - Math.min(this.x,target.x);
    	int x2 = Math.min(this.x, target.x) + Constants.GRID_SIZE - Math.max(this.x, target.x);
    	
    	int y1 = Math.max(this.y, target.y) - Math.min(this.y,target.y);
    	int y2 = Math.min(this.y, target.y) + Constants.GRID_SIZE - Math.max(this.y, target.y);
    	
    	if(beings.isFree(x+1, y) && ((x1<x2 && this.x<target.x) || (x1>x2 && this.x>target.x))){
    		beings.yard.setObjectLocation(this, beings.yard.stx(this.x + 1), y);
    		this.x = beings.yard.stx(this.x + 1);
    	}
    	else if(beings.isFree(x-1, y) && ((x1>x2 && this.x<target.x) || (x1<x2 && this.x>target.x))){
    		beings.yard.setObjectLocation(this, beings.yard.stx(this.x - 1), y);
    		this.x = beings.yard.stx(this.x - 1);
    	}
    	
    	if(beings.isFree(x, y+1) && ((y1<x2 && this.y<target.y) || (y1>y2 && this.y>target.y))){
    		beings.yard.setObjectLocation(this, x, beings.yard.stx(this.y + 1));
    		this.y = beings.yard.stx(this.y + 1);
    	}
    	else if(beings.isFree(x, y-1) && ((x1>x2 && this.y<target.y) || (y1<y2 && this.y>target.y))){
    		beings.yard.setObjectLocation(this, x, beings.yard.stx(this.y - 1));
    		this.y = beings.yard.stx(this.y - 1);
    	}
    }

    private void vie(Beings beings) {

    }

    private Int2D perception(Beings beings) {
        Bag neighbors = beings.yard.getMooreNeighbors(x,y,distancePerception, Grid2D.TOROIDAL, null,null,null);
        Int2D result = null;
        int deltaToResult = 0;

        if (neighbors != null)
        {
            for (Object neighbor : neighbors) {
                // filter the food objects
                if (neighbor instanceof Food){
                    Food food = (Food) neighbor;
                    if (result == null)
                    {
                        result = new Int2D(food.x, food.y);
                        deltaToResult = delta(new Int2D(this.x, this.y), result);
                    }
                    else {
                        int currentDelta = delta(new Int2D(this.x, this.y), new Int2D(food.x, food.y));
                        if (currentDelta < deltaToResult){
                            deltaToResult = currentDelta;
                            result = new Int2D(food.x, food.y);
                        }
                    }
                }
            }
        }

        return result;
    }

    private int delta(Int2D a, Int2D b){
        int xdelta = Math.min(Math.max(a.x, b.x) - Math.min(a.x, b.x),
                Math.min(a.x, b.x) + Constants.GRID_SIZE - Math.max(a.x, b.x));
        int ydelta = Math.min(Math.max(a.y, b.y) - Math.min(a.y, b.y),
                Math.min(a.y, b.y) + Constants.GRID_SIZE - Math.max(a.y, b.y));
        return Math.max(xdelta, ydelta);
    }

    @Override
    public String toString() {
        return "[vie:"+this.energie+", charge:"+this.charge+"]";
    }
}
