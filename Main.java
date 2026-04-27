import java.util.Scanner;


public class Main {
    public static void main(String[] args){
        int[][] visibleMap = new int[][]{
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
        System.out.println("What method");
        String method = new Scanner(System.in).nextLine();
        Pathfind p = new Pathfind(visibleMap, method, 0,0,9,9);


    }
}
