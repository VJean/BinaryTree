package simulation.model;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Int2D;

public class AgentType implements Steppable {
    public int x, y;
	public Stoppable stoppable;

	@Override
	public void step(SimState state) {
		Beings beings = (Beings) state;
//		int f = friendsNum(beings) ;
//		if (f * 3 < 8) {
//			if (!move2(beings))
//				move2(beings);
//		}
	}

//  protected int friendsNum(Beings beings) {
//	return friendsNum(beings,x,y);
// }
//  protected int friendsNum(Beings beings,int l,int c) {
//		int nb = 0;
//	    for (int i = -1 ; i <= 1 ; i++) {
//	    for (int j = -1 ; j <= 1 ; j++) {
//	      if (i != 0 || j != 0) {
//	    	  Int2D flocation = new Int2D(beings.yard.stx(l + i),beings.yard.sty(c + j));
//	    	  Object ag = beings.yard.get(flocation.x,flocation.y);
//	          if (ag != null) {
//	        	  if (ag.getClass() == this.getClass())
//	        		  nb++;
//	          }
//	      }
//	    }
//	  }
//	  return nb;
//	 }
//
//  public boolean move(Beings beings) {
//	boolean done = false;
//	int n = beings.random.nextInt(Beings.NB_DIRECTIONS);
//	switch(n) {
//	case 0:
//		if (beings.isFree(x-1, y)
//	         && friendsNum(beings,x-1,y) >= LEVEL) {
//		 beings.yard.set(x, y, null);
//		 beings.yard.set(beings.yard.stx(x-1), y, this);
//		 x = beings.yard.stx(x-1);
//		 done = true;
//		}
//		break;
//	case 1:
//		if (beings.isFree(x+1, y) && friendsNum(beings,x+1,y) >= LEVEL) {
//		 beings.yard.set(x, y, null);
//		 beings.yard.set(beings.yard.stx(x+1), y, this);
//		 x = beings.yard.stx(x+1);
//		 done = true;
//	    }
//		break;
//	case 2:
//		if (beings.isFree(x, y-1) && friendsNum(beings,x,y-1) >= LEVEL) {
//			beings.yard.set(x, y, null);
//			beings.yard.set(x, beings.yard.sty(y-1), this);
//			y = beings.yard.sty(y-1);
//			done = true;
//		}
//		break;
//	case 3:
//		if (beings.isFree(x, y+1) && friendsNum(beings,x,y+1) >= LEVEL) {
//			beings.yard.set(x, y, null);
//			beings.yard.set(x, beings.yard.sty(y+1), this);
//			y = beings.yard.sty(y+1);
//			done = true;
//		}
//		break;
//	case 4:
//		if (beings.isFree(x-1, y-1) && friendsNum(beings,x-1,y-1) >= LEVEL) {
//			beings.yard.set(x, y, null);
//			beings.yard.set(beings.yard.stx(x-1), beings.yard.sty(y-1), this);
//			x = beings.yard.stx(x-1);
//			y = beings.yard.sty(y-1);
//			done = true;
//		}
//		break;
//	case 5:
//		if (beings.isFree(x+1, y-1) && friendsNum(beings,x+1,y-1) >= LEVEL) {
//			beings.yard.set(x, y, null);
//			beings.yard.set(beings.yard.stx(x+1), beings.yard.sty(y-1), this);
//			x = beings.yard.stx(x+1);
//			y = beings.yard.sty(y-1);
//			done = true;
//		}
//		break;
//	case 6:
//		if (beings.isFree(x+1, y+1) && friendsNum(beings,x+1,y+1) >= LEVEL) {
//			beings.yard.set(x, y, null);
//			beings.yard.set(beings.yard.stx(x+1), beings.yard.sty(y+1), this);
//			x = beings.yard.stx(x+1);
//			y = beings.yard.sty(y+1);
//			done = true;
//		}
//		break;
//	case 7:
//		if (beings.isFree(x-1, y+1) && friendsNum(beings,x-1,y+1) >= LEVEL) {
//			beings.yard.set(x, y, null);
//			beings.yard.set(beings.yard.stx(x-1), beings.yard.sty(y+1), this);
//			x = beings.yard.stx(x-1);
//			y = beings.yard.sty(y+1);
//			done = true;
//		}
//		break;
//	}
//	return done;
// }
//  public boolean move2(Beings beings) {
//		boolean done = false;
//			int n = beings.random.nextInt(Beings.NB_DIRECTIONS);
//			switch(n) {
//			case 0:
//				if (beings.isFree(x-1, y)) {
//				 beings.yard.set(x, y, null);
//				 beings.yard.set(beings.yard.stx(x-1), y, this);
//				 x = beings.yard.stx(x-1);
//				 done = true;
//				}
//				break;
//			case 1:
//				if (beings.isFree(x+1, y)) {
//				 beings.yard.set(x, y, null);
//				 beings.yard.set(beings.yard.stx(x+1), y, this);
//				 x = beings.yard.stx(x+1);
//				 done = true;
//			    }
//				break;
//			case 2:
//				if (beings.isFree(x, y-1)) {
//					beings.yard.set(x, y, null);
//					beings.yard.set(x, beings.yard.sty(y-1), this);
//					y = beings.yard.sty(y-1);
//					done = true;
//				}
//				break;
//			case 3:
//				if (beings.isFree(x, y+1)) {
//					beings.yard.set(x, y, null);
//					beings.yard.set(x, beings.yard.sty(y+1), this);
//					y = beings.yard.sty(y+1);
//					done = true;
//				}
//				break;
//			case 4:
//				if (beings.isFree(x-1, y-1)) {
//					beings.yard.set(x, y, null);
//					beings.yard.set(beings.yard.stx(x-1), beings.yard.sty(y-1), this);
//					x = beings.yard.stx(x-1);
//					y = beings.yard.sty(y-1);
//					done = true;
//				}
//				break;
//			case 5:
//				if (beings.isFree(x+1, y-1)) {
//					beings.yard.set(x, y, null);
//					beings.yard.set(beings.yard.stx(x+1), beings.yard.sty(y-1), this);
//					x = beings.yard.stx(x+1);
//					y = beings.yard.sty(y-1);
//					done = true;
//				}
//				break;
//			case 6:
//				if (beings.isFree(x+1, y+1)) {
//					beings.yard.set(x, y, null);
//					beings.yard.set(beings.yard.stx(x+1), beings.yard.sty(y+1), this);
//					x = beings.yard.stx(x+1);
//					y = beings.yard.sty(y+1);
//					done = true;
//				}
//				break;
//			case 7:
//				if (beings.isFree(x-1, y+1)) {
//					beings.yard.set(x, y, null);
//					beings.yard.set(beings.yard.stx(x-1), beings.yard.sty(y+1), this);
//					x = beings.yard.stx(x-1);
//					y = beings.yard.sty(y+1);
//					done = true;
//				}
//				break;
//			}
//		return done;
//	 }
//  public boolean tryMove(Beings beings,int f) {
//		boolean done = false;
//			int n = beings.random.nextInt(Beings.NB_DIRECTIONS);
//			switch(n) {
//			case 0:
//				if (beings.isFree(x-1, y) && friendsNum(beings,x-1,y) > f) {
//				 beings.yard.set(x, y, null);
//				 beings.yard.set(beings.yard.stx(x-1), y, this);
//				 x = beings.yard.stx(x-1);
//				 done = true;
//				}
//				break;
//			case 1:
//				if (beings.isFree(x+1, y) && friendsNum(beings,x+1,y) > f) {
//				 beings.yard.set(x, y, null);
//				 beings.yard.set(beings.yard.stx(x+1), y, this);
//				 x = beings.yard.stx(x+1);
//				 done = true;
//			    }
//				break;
//			case 2:
//				if (beings.isFree(x, y-1)  && friendsNum(beings,x,y-1) > f) {
//					beings.yard.set(x, y, null);
//					beings.yard.set(x, beings.yard.sty(y-1), this);
//					y = beings.yard.sty(y-1);
//					done = true;
//				}
//				break;
//			case 3:
//				if (beings.isFree(x, y+1)  && friendsNum(beings,x,y+1) > f) {
//					beings.yard.set(x, y, null);
//					beings.yard.set(x, beings.yard.sty(y+1), this);
//					y = beings.yard.sty(y+1);
//					done = true;
//				}
//				break;
//			case 4:
//				if (beings.isFree(x-1, y-1)  && friendsNum(beings,x-1,y-1) > f) {
//					beings.yard.set(x, y, null);
//					beings.yard.set(beings.yard.stx(x-1), beings.yard.sty(y-1), this);
//					x = beings.yard.stx(x-1);
//					y = beings.yard.sty(y-1);
//					done = true;
//				}
//				break;
//			case 5:
//				if (beings.isFree(x+1, y-1)  && friendsNum(beings,x+1,y-1) > f) {
//					beings.yard.set(x, y, null);
//					beings.yard.set(beings.yard.stx(x+1), beings.yard.sty(y-1), this);
//					x = beings.yard.stx(x+1);
//					y = beings.yard.sty(y-1);
//					done = true;
//				}
//				break;
//			case 6:
//				if (beings.isFree(x+1, y+1)  && friendsNum(beings,x+1,y+1) > f) {
//					beings.yard.set(x, y, null);
//					beings.yard.set(beings.yard.stx(x+1), beings.yard.sty(y+1), this);
//					x = beings.yard.stx(x+1);
//					y = beings.yard.sty(y+1);
//					done = true;
//				}
//				break;
//			case 7:
//				if (beings.isFree(x-1, y+1) && friendsNum(beings,x-1,y+1) > f) {
//					beings.yard.set(x, y, null);
//					beings.yard.set(beings.yard.stx(x-1), beings.yard.sty(y+1), this);
//					x = beings.yard.stx(x-1);
//					y = beings.yard.sty(y+1);
//					done = true;
//				}
//				break;
//			}
//		return done;
//	 }
}
