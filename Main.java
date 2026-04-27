import java.util.Scanner;


public class Main {
    public static void main(String[] args){
        int[][] map = new int[][]{
                {0,1,0,0,0,1,0,0,0,0},
                {0,1,0,1,0,1,0,0,0,0},
                {0,0,0,1,0,1,0,0,0,0},
                {0,0,0,0,0,1,0,0,0,0},
                {1,1,0,1,0,1,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,1,1,1,0,1,0},
                {0,0,0,0,1,0,1,0,1,0},
                {0,0,0,0,1,0,1,0,1,0},
                {0,0,0,0,1,0,1,0,1,0}
        };
        Pathfind p = new Pathfind(map);
        p.printPathAStar(0,0,9,9,map);
        p.printPathFloodFill(0,0,9,9,map);


    }
}
