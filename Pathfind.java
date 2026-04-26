import java.util.*;

public class Pathfind {
    private final int WIDTH = 10;
    private final int HEIGHT = 10;
    public Pathfind(){
        Scanner sc = new Scanner(System.in);
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


        Node[][] map = new Node[HEIGHT][WIDTH];
        for(int r = 0; r < HEIGHT; r++){
            for(int c = 0; c < WIDTH; c++){
                if(visibleMap[r][c] == 1){
                    map[r][c] = new Node(null, false, r, c);
                }
            }
        }
        Node goal = new Node(null, true, 9, 9);
        goal.g = 0;
        goal.h = 0;
        goal.f = 0;
        map[9][9] = goal;
        Node start = new Node(null, true, 0, 0);
        start.g = 0;
        start.h = Math.abs(start.x - goal.x) + Math.abs(start.y - goal.y);
        map[0][0] = start;
        printScreen(visibleMap);
        ArrayList<Node> path = findPath(start,goal,map);
        for(int i = path.size() - 1; i >= 0; i--){
            System.out.println(path.get(i).x + ", " + path.get(i).y);
            visibleMap[path.get(i).y][path.get(i).x] = 2;
            printScreen(visibleMap);
            System.out.println("go to next iteration?");
            String x = sc.nextLine();
        }
    }

    public ArrayList<Node> findPath(Node start, Node goal, Node[][] map){
        PriorityQueue<Node> openList = new PriorityQueue<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.f - o2.f; // negative means 01 is better f value than o2
            }
        });

        HashSet<Node> closedList = new HashSet<Node>();
        openList.add(start);

        while(!openList.isEmpty()){
            Node current = openList.poll();
            if(current == goal){
                ArrayList<Node> path = new ArrayList<>();
                path.add(current);
                Node c = current.parent;
                while(c!=start){
                    path.add(c);
                    c = c.parent;
                }
                path.add(c);
                return path;
            }
            closedList.add(current);
            int[][] neighbors = new int[][] {{0,1},{0,-1},{1,0},{-1,0}};

            for(int[] list : neighbors){
                int currenty = current.y + list[1];
                int currentx = current.x + list[0];
                if(inBounds(currenty, currentx)){
                    Node neighbor = map[currenty][currentx];
                    if(neighbor == null){
                        neighbor = new Node(null, true, currentx, currenty);
                        neighbor.g = Integer.MAX_VALUE;
                        map[currenty][currentx] = neighbor;
                    }
                    if(neighbor.walkable == false){
                        continue;
                    }
                    if(closedList.contains(neighbor)){
                        continue;
                    }

                    int nextG = current.g+1;
                    if(!openList.contains(neighbor)){
                        neighbor.g = nextG;
                        neighbor.h = Math.abs(neighbor.x - goal.x) + Math.abs(neighbor.y - goal.y);
                        neighbor.f = neighbor.g + neighbor.h;
                        neighbor.parent = current;
                        openList.add(neighbor);
                    }
                    else if(nextG < neighbor.g){
                        neighbor.g = nextG;
                        neighbor.h = Math.abs(neighbor.x - goal.x) + Math.abs(neighbor.y - goal.y);
                        neighbor.f = neighbor.g + neighbor.h;
                        neighbor.parent = current;
                        openList.remove(neighbor);
                        openList.add(neighbor);
                    }
                }
            }
        }
        System.out.println("Path not found");
        return new ArrayList<>();
    }
    public boolean inBounds(int y, int x){
        return (y >= 0 && y < HEIGHT && x >= 0 && x < WIDTH );
    }

    public void printScreen(int[][] map){
        for(int r = 0; r < HEIGHT; r++){
            for(int c = 0; c < WIDTH; c++){
                System.out.print(map[r][c] + " ");
            }
            System.out.println();
        }
    }

}
