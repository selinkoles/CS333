import java.util.*;
import static java.lang.Math.min;

class Edge {
    public String from, to;
    public int flow;
    public int capacity;

    public Edge(String from, String to, int capacity) {
        this.from = from;
        this.to = to;
        this.capacity = capacity;
    }

    public int remainingCapacity() {
        return capacity-flow;
    }

    public void augmentEdge(int currentFlow) {
        flow += currentFlow;
    }

}

class Vertex {
    public String id;
    public ArrayList<Edge> edges;

    public Vertex(String id) {
        this.id = id;
        this.edges = new ArrayList<>();
    }
}

class Graph {
    public ArrayList<Vertex> vertices;
    public ArrayList<Edge> edges;

    public Graph() {
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    public void addVertex(String id) {
        vertices.add(new Vertex(id));
    }

    public void addEdge(String from, String to, int capacity) {
        Vertex sourceVertex = null;
        Vertex destinationVertex = null;
        for (Vertex vertex : vertices) {
            if (vertex.id.equals(from)) {
                sourceVertex = vertex;
            }
            if (vertex.id.equals(to)) {
                destinationVertex = vertex;
            }
        }
        if (sourceVertex == null) {
            sourceVertex = new Vertex(from);
            vertices.add(sourceVertex);
        }
        if (destinationVertex == null) {
            destinationVertex = new Vertex(to);
            vertices.add(destinationVertex);
        }
        Edge edge = new Edge(from, to, capacity);
        sourceVertex.edges.add(edge);
        destinationVertex.edges.add(edge);
        edges.add(edge);
    }
}

class FordFulkerson {

    public Graph networkGraph;
    public String source;
    public String sink;
    public HashMap<String, Boolean> vertexVisited = new HashMap<>();
    public int maxFlow = 0;
    private static final Integer INF = Integer.MAX_VALUE;

    public FordFulkerson(Graph networkGraph, String source, String sink) {
        this.networkGraph = networkGraph;
        this.source = source;
        this.sink = sink;
        for (Vertex vertex: networkGraph.vertices) {
            assert false;
            vertexVisited.put(vertex.id, false);
        }
    }

    public void solve() {
        for (int flow=dfs(source, INF); flow!=0; flow=dfs(source, INF)) {
            vertexVisited.replaceAll((k,v)->v=false);
            maxFlow += flow;
        }

        Set<String> ventureProjects = new HashSet<>();
        Queue<String> itrQueue = new ArrayDeque<>();

        itrQueue.offer("t");

        while (!itrQueue.isEmpty()) {
            String endNode = itrQueue.poll();
            int cap = 0;
            for (Edge edge: networkGraph.edges) {
                if (Objects.equals(edge.to, endNode) && !Objects.equals(edge.from, "S")) {
                    if (Objects.equals(endNode, "t") && edge.remainingCapacity()==0) {
                        ventureProjects.add(edge.from);
                        itrQueue.offer(edge.from);
                        cap = edge.capacity;
                    }
                    else if (Objects.equals(endNode, edge.to) && cap<=edge.flow) {
                        ventureProjects.add(edge.from);
                        itrQueue.offer(edge.from);
                        cap += edge.capacity;
                    }
                }
            }
        }

        System.out.print("Venture Projects: ");
        for (String proj: ventureProjects) {
            System.out.print(proj + " ");
        }

        System.out.println("\nMaximum profit: " + maxFlow);
    }

    private int dfs(String vertex, int flow) {
        if (Objects.equals(vertex, sink)) return flow;

        vertexVisited.put(vertex, true);

        ArrayList<Edge> edges = networkGraph.edges;
        for(Edge edge: edges) {
            if (edge.remainingCapacity() > 0 && !vertexVisited.get(edge.to)) {
                int bottleneckValue = dfs(edge.to, min(flow, edge.remainingCapacity()));


                if (bottleneckValue>0) {
                    edge.augmentEdge(bottleneckValue);
                    return bottleneckValue;
                }
            }
        }
        return 0;
    }

}

public class SELİN_KÖLEŞ_S021541 {
    public static Integer INF = Integer.MAX_VALUE;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in).useDelimiter("\n");

        // some datastructures declared and initialized will be used later
        ArrayList<String> prerequisites = new ArrayList<>();
        int projectProfit = 0;

        System.out.println("""
                > Enter the number of projects.
                > Names of projects and the outcomes in single line each.
                > Then define the prerequisites in parenthesis (p_i, p_j); meaning p_j --> p_i. e.g: (A,B) (C,E) ...
                > Later to start the program: Enter the word 'Decide'.""");

        int numOfProjects = sc.nextInt();

        String[] namesOfProjects;                                   // array to store the names of project
        int[] outcomesOfProjects;                                   // array to store outcomes of projects

        // scanner to get names of projects as an input and also checks if user gives correct number of inputs
        do {
            String inputProjects = sc.next();
            namesOfProjects = inputProjects.split(" ");
            if (namesOfProjects.length != numOfProjects) {
                System.err.println("The given number of projects are not equal to " + numOfProjects + ".");
            }
        } while (namesOfProjects.length != numOfProjects);

        // scanner to get outcomes of projects as an input and also checks if user gives correct number of inputs
        do {
            String inputOutcomes = sc.next();
            outcomesOfProjects = Arrays.stream(inputOutcomes.split(" ")).mapToInt(Integer::parseInt).toArray();
            if (outcomesOfProjects.length != numOfProjects) {
                System.err.println("The given number of outcomes are not equal to given number of projects.");
            }
        } while (outcomesOfProjects.length != numOfProjects);

        // hash-map to store the given outcomes of venture projects
        HashMap<String, Integer> outcomesMap = new HashMap<>();
        // storing the venture projects and their outcomes in outcomesMap<>
        for (int i = 0; i < namesOfProjects.length; i++) {
            outcomesMap.put(namesOfProjects[i], outcomesOfProjects[i]);
        }

        // scanner to get input for the prerequisites
        String[] str;
        String inputPreReq = sc.next();
        String[] inputPreReqArray = inputPreReq.split(" ");
        ArrayList<ArrayList<String>> preReq = new ArrayList<>();    // array to store the prerequisites
        ArrayList<ArrayList<String>> preReqCopy = new ArrayList<>();    // holds copy of ArrayList<ArrayList<String>> preReq

        // storing the input prerequisites in an 2D array
        for (String s : inputPreReqArray) {
            str = s.substring(1, 4).split(",");
            preReq.add(new ArrayList<>(Arrays.asList(str[0], str[1])));
            preReqCopy.add(new ArrayList<>(Arrays.asList(str[0], str[1])));
        }

        // a set which contains the projects which require some prerequisite
        Set<String> projectsNeedPreReq = new HashSet<>();
        // storing the projects need some prerequisite
        for (ArrayList<String> project : preReq) {
            projectsNeedPreReq.add(project.get(0));
        }

        // a set which contains the projects which do not need any prerequisite
        Set<String> seqProjects = new HashSet<>();
        // storing the projects which do not require any prerequisite
        for (String project : namesOfProjects) {
            if (!projectsNeedPreReq.contains(project)) {
                seqProjects.add(project);
            }
        }

        // hash-map to store the ultimate profit of venture projects
        HashMap<String, Integer> profitMap = new HashMap<>();
        /*
         * here we started to create our profit hashmap which store the ultimate profit for all projects
         * in first loop it stores the profit earned by the projects which don't need any sort of prerequisite
         * and if their profit is positive then we are removing their dependency on other project.
         * we are doing by simply excluding p_j from (p_i,p_j) in the preReq 2-D array
         */
        for (String seq : seqProjects) {
            profitMap.put(seq, outcomesMap.get(seq));
            if (profitMap.get(seq) > 0) {
                for (int i = 0; i < preReq.size(); i++) {
                    if (Objects.equals(preReq.get(i).get(1), seq)) {
                        preReq.remove(i--);
                    }
                }
            }
        }

        /*
         * here we are evaluating the profit of the projects which require some prerequisite
         * we are doing so by adding the outcome of the project + profit of their prerequisite
         * and also if their profit is positive then we are removing dependency of the project
         * as well as dependency of their prerequisite on other project, we are doing by simply
         * excluding p_j from (p_i,p_j) in the preReq 2-D array
         */
        for (String proj : projectsNeedPreReq) {
            for (ArrayList<String> p : preReq) {
                if (Objects.equals(p.get(0), proj)) {
                    prerequisites.add(p.get(1));
                }
            }

            for (String pre : prerequisites) {
                projectProfit += profitMap.get(pre);
            }
            projectProfit += outcomesMap.get(proj);
            profitMap.put(proj, projectProfit);

            if (profitMap.get(proj) > 0) {
                for (int i = 0; i < preReq.size(); i++) {
                    if (Objects.equals(preReq.get(i).get(1), proj)) {
                        preReq.remove(i--);
                    }
                }
                for (String pre : prerequisites) {
                    for (int i = 0; i < preReq.size(); i++) {
                        if (Objects.equals(preReq.get(i).get(1), pre)) {
                            preReq.remove(i--);
                        }
                    }
                }
            }
            projectProfit = 0;
            prerequisites.clear();
        }

        // we started to create our network flow graph
        Graph networkGraph = new Graph();

        networkGraph.addVertex("S");
        networkGraph.addVertex("t");

        for (String proj : namesOfProjects) {
            networkGraph.addVertex(proj);
        }

        for (String proj : seqProjects) {
            networkGraph.addEdge("S", proj, INF);
        }

        for (ArrayList<String> projArray : preReqCopy) {
            networkGraph.addEdge(projArray.get(1), projArray.get(0), INF);
        }

        for (String proj : namesOfProjects) {
            networkGraph.addEdge(proj, "t", profitMap.get(proj));
        }

        // we initialized the Ford Fulkerson solver which can evaluate
        FordFulkerson fordFulkersonSolver = new FordFulkerson(networkGraph, "S", "t");

        String startProgram;
        do {
            startProgram = sc.next();
            if (Objects.equals(startProgram, "Decide")) {
                fordFulkersonSolver.solve();
                break;
            }
            System.err.println("You entered wrong keyword. Please, Enter 'Decide' in the console.");
        } while (!Objects.equals(startProgram, "Decide"));

        sc.close();
    }
}

