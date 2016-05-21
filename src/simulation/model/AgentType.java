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
	}
}
