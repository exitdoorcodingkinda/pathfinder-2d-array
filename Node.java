public class Node {
    private Node parent;
    private int x;
    private int y;
    private int g;
    private int h;
    private int f;

    public Node(){
        x=0;
        y=0;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    public void setParent(Node c){
        this.parent = c;
    }
    public Node getParent(){
        return this.parent;
    }

    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }

    public int getF(){
        f = g + h;
        return  g + h;
    }

    public int getG() { return g; }

    public void setH(int h){
        this.h = h;
    }
    public void setG(int g){
        this.g = g;
    }
}
