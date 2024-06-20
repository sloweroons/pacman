public class Pellet extends Item{
    boolean alive;

    Pellet(int x, int y) {
        super(x, y);
        alive = true;
    }

    public boolean isAlive() {
        return alive;
    }
}
