package simulation.model;

import sim.engine.Stoppable;

import java.util.Random;

public class Food {
    private int items;
    public int x, y;

    public Food() {
        items = new Random().nextInt(Constants.MAX_FOOD) + 1;
    }

    public boolean isEmpty() {
        return items == 0;
    }

    @Override
    public String toString() {
        return "[reste:"+this.items+"]";
    }

    public void removeItem() {
        this.items--;
    }
}
