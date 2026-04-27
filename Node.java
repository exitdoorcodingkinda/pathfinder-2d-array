public class Node {
    public Node parent;
    public boolean walkable;
    public int x;
    public int y;
    public double f;
    public double g;
    public double h;
    public boolean checked;

    public Node(Node parent, boolean walkable, int x, int y){
        this.parent = parent;
        this.walkable = walkable;
        this.x = x;
        this.y = y;
    }
}
