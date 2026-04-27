import java.util.*;

public class Pathfind {
    private final int WIDTH = 10;
    private final int HEIGHT = 10;
    private String method;


    public Pathfind(int[][] visibleMap, String method, int startx, int starty, int goalx, int goaly){
        Scanner sc = new Scanner(System.in);

        switch(method){
            case "astar":
                ArrayList<Node> astarpath = findPathAStar(new int[] {startx,starty}, new int[] {goalx,goaly}, visibleMap);

                for(int i = astarpath.size()-1; i >= 0; i--){
                    System.out.println(astarpath.get(i).x + ", " + astarpath.get(i).y);
                    visibleMap[astarpath.get(i).y][astarpath.get(i).x] = 2;
                    printScreen(visibleMap);
                    System.out.println("go to next iteration?");
                    String x = sc.nextLine();
                }
                break;
            case "floodfill":
                ArrayList<Node> floodpath = findPathFloodFill(new int[] {startx,starty} ,new int[] {goalx,goaly},visibleMap);

                for(int i = 0; i < floodpath.size(); i++){
                    System.out.println(floodpath.get(i).x + ", " + floodpath.get(i).y);
                    visibleMap[floodpath.get(i).y][floodpath.get(i).x] = 2;
                    printScreen(visibleMap);
                    System.out.println("go to next iteration?");
                    String x = sc.nextLine();
                }
                break;
        }





    }
    public void printPathFloodFill(){

    }





    public ArrayList<Node> findPathFloodFill(int[] startcoords, int[] goalcoords, int[][] map){
        Node[][] nodemap = new Node[map.length][map[0].length];

        for(int r = 0; r < HEIGHT; r++){
            for(int c = 0; c < WIDTH; c++){
                if(map[r][c] == 1){
                    Node wall = new Node(null, false, r, c);
                    wall.checked = true;
                    wall.g = -1;
                    nodemap[r][c] = wall; // set a node to be walkable or whatever, can maybe be used for weighting in the future
                }else{
                    Node nnode = new Node(null, true, r, c);
                    nnode.checked = false;
                    nnode.g = Integer.MAX_VALUE;
                    nodemap[r][c] = nnode;
                }
            } // fills the nodemap with already initialized, unwalkable ones, based on a int map passed through
        }

        Queue<Node> openlist = new LinkedList<>(); // linked list is amazing fast for insertions/deletions near the beginning (we'll be getting the item at index 0)

        nodemap[goalcoords[1]][goalcoords[0]] = new Node(null, true, goalcoords[0], goalcoords[1]); // creates the end goal node
        Node goal = nodemap[goalcoords[1]][goalcoords[0]] ;
        goal.g = 1;
        goal.checked = true;

        int[][] neighbors = new int[][] {{0,1},{0,-1},{1,0},{-1,0}};

        openlist.add(nodemap[goalcoords[1]][goalcoords[0]]);

        while (!openlist.isEmpty()){
            Node current = openlist.poll();
            for(int[] neighbor : neighbors){
                int neighbory = current.y + neighbor[1];
                int neighborx = current.x + neighbor[0];

                if(inBounds(neighbory, neighborx)){
                    if(nodemap[neighbory][neighborx] != null && (!nodemap[neighbory][neighborx].walkable || nodemap[neighbory][neighborx].checked)){
                        continue;
                    }
                    else{
                        Node newnode = new Node(null, true, neighborx, neighbory); // new node with no parent, it is walkable
                        newnode.g = current.g + 1; // set the g
                        newnode.checked = true; // it has been checked
                        nodemap[neighbory][neighborx] = newnode;
                        openlist.add(nodemap[neighbory][neighborx]);
                    }
                }

            }



        }

        ArrayList<Node> path = new ArrayList<>();
        Node start = nodemap[startcoords[1]][startcoords[0]]; // this will follow the path
        path.add(start);
        while(start.g != 1){
            for(int[] nbs : neighbors){
                int nbx = start.x + nbs[0];
                int nby = start.y + nbs[1];
                if(inBounds(nby,nbx)){
                    if(nodemap[nby][nbx] != null && nodemap[nby][nbx].walkable && nodemap[nby][nbx].g < start.g){
                        start = nodemap[nby][nbx];
                        path.add(nodemap[nby][nbx]);
                    }
                }

            }
        }

        return path;

    }

    public ArrayList<Node> findPathAStar(int[] startcoords, int[] goalcoords, int[][] map){

        Node[][] nodemap = new Node[HEIGHT][WIDTH];
        for(int r = 0; r < HEIGHT; r++){
            for(int c = 0; c < WIDTH; c++){
                if(map[r][c] == 1){
                    nodemap[r][c] = new Node(null, false, r, c); // set a node to be walkable or whatever, can maybe be used for weighting in the future
                }
            }
        }

        Node start = new Node(null, true, startcoords[0], startcoords[1]);
        Node goal = new Node(null, true, goalcoords[0], goalcoords[1]);

        nodemap[startcoords[1]][startcoords[0]] = start;
        nodemap[goalcoords[1]][goalcoords[0]] = goal;

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
                    Node neighbor = nodemap[currenty][currentx]; // get the node at neighbor in the nodemap
                    if(neighbor == null){ // if the node is not instantiated yet then instantiate
                        neighbor = new Node(null, true, currentx, currenty);
                        neighbor.g = Integer.MAX_VALUE; // so that it is default unvisited, because it hasnt been visited yet
                        nodemap[currenty][currentx] = neighbor; // set the location on the nodemap to the neighbors new node
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

    public void setMethod(String method){
        this.method = method;
    }

}
