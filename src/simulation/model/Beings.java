package simulation.model;

import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.grid.ObjectGrid2D;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;

public class Beings extends SimState {
	public static int GRID_SIZE = 20; 
	public static int NUM_A = 100; 
	public static int NUM_B = 100; 
	public static int NB_DIRECTIONS = 8;
	public ObjectGrid2D yard = new ObjectGrid2D(GRID_SIZE,GRID_SIZE);
	public Beings(long seed) {
		super(seed);
	}
	public void start() {
		System.out.println("Simulation started");
		super.start();
	    yard.clear();
	    addAgentsA();
	    addAgentsB();
  }
  private void addAgentsA() {
	
	for(int  i  =  0;  i  <  NUM_A;  i++) {
      TypeA  a  =  new  TypeA();
      Int2D location = getFreeLocation();
      yard.set(location.x,location.y,a);
      a.x = location.x;
      a.y = location.y;
      schedule.scheduleRepeating(a);
    }  
  }
  private void addAgentsB() {
	  
	for(int  i  =  0;  i  <  NUM_B;  i++) {
      TypeB  b  =  new  TypeB();
      Int2D location = getFreeLocation();
      yard.set(location.x,location.y,b);
      b.x = location.x;
      b.y = location.y;
      schedule.scheduleRepeating(b);
    }  
  }
  public boolean free(int x,int y) {
	 int xx = yard.stx(x);
	 int yy = yard.sty(y);
	 return yard.get(xx,yy) == null;
  }
  private Int2D getFreeLocation() {
	  Int2D location = new Int2D(random.nextInt(yard.getWidth()),
	           random.nextInt(yard.getHeight()) );
	  Object ag;
	  while ((ag = yard.get(location.x,location.y)) != null) {
	   	  location = new Int2D(random.nextInt(yard.getWidth()),
	   	           random.nextInt(yard.getHeight()) );
	  }
	  return location;
  }
  public  int  getNumStudents()  {  return  NUM_A;  }
}
