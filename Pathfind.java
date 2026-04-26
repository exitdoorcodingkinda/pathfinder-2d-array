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

            // initialize a new 2d array of nodes to have alongside our visible map, which is what displays the walls and pathing.
        Node[][] map = new Node[HEIGHT][WIDTH];
        for(int r = 0; r < HEIGHT; r++){
            for(int c = 0; c < WIDTH; c++){
                if(visibleMap[r][c] == 1){
                    map[r][c] = new Node(null, false, r, c); // set a node to be walkable or whatever, can maybe be used for weighting in the future
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
        // * Priority queue will sort stuff to the front depending on whatever, i use a comparator to put the Node with the smallest F at the front, the most likely to lead to the goal

        HashSet<Node> closedList = new HashSet<Node>(); // will store nodes that have already been analyzed
        openList.add(start); // adds the start to the openlist to being analyzing neighbors

        while(!openList.isEmpty()){ // while there are nodes to analyze for possible pathing
            Node current = openList.poll(); // take the node with the lowest F from openlist
            if(current == goal){ // if its equal to the goal node, which is already instantiated on the map, then make an arraylist of the nodes parents that connect to it
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
            int[][] neighbors = new int[][] {{0,1},{0,-1},{1,0},{-1,0}}; // up, left, right, down

            for(int[] list : neighbors){ // for each direction possible
                int currenty = current.y + list[1];
                int currentx = current.x + list[0]; // set the x and y to the currents x and y + the direction
                if(inBounds(currenty, currentx)){ // if the new coordinates are in the bounds of the visible/node map
                    Node neighbor = map[currenty][currentx]; // get the node at neighbor in the nodemap
                    if(neighbor == null){ // if the node is not instantiated yet then instantiate
                        neighbor = new Node(null, true, currentx, currenty);
                        neighbor.g = Integer.MAX_VALUE; // so that it is default unvisited, because it hasnt been visited yet
                        map[currenty][currentx] = neighbor; // set the location on the nodemap to the neighbors new node
                    }
                    if(!neighbor.walkable){
                        continue; // if the node is not walkable then skip this direction
                    }
                    if(closedList.contains(neighbor)){
                        continue; // if this node has already been evaluated as inefficient, skip it
                    }

                    int nextG = current.g+1; // the next G aka distance from the start which is just a counter
                    if(!openList.contains(neighbor)){ // if neighbor is not in the open list, that means we have not evaluated it yet
                        neighbor.g = nextG; // we set it to the nextg, cause it would come next and be 1 further from the start
                        neighbor.h = Math.abs(neighbor.x - goal.x) + Math.abs(neighbor.y - goal.y); // simple distance calculation to the end goal
                        neighbor.f = neighbor.g + neighbor.h; // f is the score of the distance from the start, and distance from the goal, lower is better
                        neighbor.parent = current; // set the neighbors parent to the current and add it to openlist, cus it could be the goal
                        openList.add(neighbor);
                    }
                    else if(nextG < neighbor.g){ // nextg is the cost to reach the neighbor from current node, if the new g for it is better (better path) then update it, else skip
                        neighbor.g =
                        neighbor.h = Math.abs(neighbor.x - goal.x) + Math.abs(neighbor.y - goal.y);
                        neighbor.f = neighbor.g + neighbor.h;
                        neighbor.parent = current;
                        openList.remove(neighbor);
                        openList.add(neighbor); // cus priority queue crap
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
