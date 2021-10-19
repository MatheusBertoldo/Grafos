import java.io.*;
import java.util.*;
class Graph {
    private int V;
    private LinkedList<Integer>[] adj;
    public Graph(int v) {
        V = v;
        adj = new LinkedList[v];
        for (int i=0; i<v; ++i) adj[i] = new LinkedList();
    }
    int getVertices(){
        return V;
    }
    int getMax(){
        int max_degree = Integer.MIN_VALUE;
        for(int i = 0; i < adj.length; i++){
            if(adj[i].size() > max_degree) max_degree = adj[i].size();
        }
        return max_degree;
    }
    int getMin(){
        int min_degree = Integer.MAX_VALUE;
        for(int i = 0; i < adj.length; i++){
            if(adj[i].size() < min_degree) min_degree = adj[i].size();
        }
        return min_degree;
    }
    double getAverage(){
        return (double) (getMax()+getMin())/2;
    }
    void addEdge(int v, int w) {
        adj[v-1].add(w-1);
        adj[w-1].add(v-1);
    }
    void DFSUtil(int v, boolean[] visited) {
        visited[v] = true;
        Iterator<Integer> i = adj[v].listIterator();
        while (i.hasNext()) {
            int n = i.next();
            if (!visited[n]) DFSUtil(n, visited);
        }
    }
    boolean isConnected() {
        boolean[] visited = new boolean[V];
        int i;
        for (i = 0; i < V; i++) visited[i] = false;
        for (i = 0; i < V; i++) {
            if (adj[i].size() != 0) break;
        }
        if (i == V) return true;
        DFSUtil(i, visited);
        for (i = 0; i < V; i++) {
            if (!visited[i] && adj[i].size() > 0) return false;
        }
        return true;
    }
    int isEulerian() {
        if (!isConnected()) return 0;
        int odd = 0;
        for (int i = 0; i < V; i++) {
            if (adj[i].size() % 2 != 0) odd++;
        }
        if (odd > 2) return 0;
        return (odd == 2)? 1 : 2;
    }
    void BFS(int s) {
        s = s-1;
        boolean visited[] = new boolean[V];
        LinkedList<Integer> queue = new LinkedList<Integer>();
        visited[s] = true;
        queue.add(s);
        while (queue.size() != 0) {
            s = queue.poll();
            System.out.print((s+1) + " ");
            Iterator<Integer> i = adj[s].listIterator();
            while (i.hasNext()) {
                int n = i.next();
                if (!visited[n]) {
                    visited[n] = true;
                    queue.add(n);
                }
            }
        }
    }
    void test() {
        int res = isEulerian();
        if (res == 0)
            System.out.println("O grafo não é Euleriano");
        else if (res == 1)
            System.out.println("O grafo é semi-Euleriano");
        else
            System.out.println("O grafo é Euleriano");
    }
}
public class Main{
    public static void main(String args[]) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Escreva o caminho para o arquivo: ");
        String filePath = reader.readLine();
        File graphFile = new File(filePath);
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(graphFile)));
        int vertex_count = Integer.parseInt(fileReader.readLine()), edge_count = 0;
        Graph graph = new Graph(vertex_count);
        do{
            String[] edges = fileReader.readLine().split(" ");
            graph.addEdge(Integer.parseInt(edges[0]), Integer.parseInt(edges[1]));
            edge_count++;
        } while(fileReader.ready());
        File newFile = new File(graphFile.getPath().replace(graphFile.getName(), "output.txt"));
        if(!newFile.createNewFile()){
            System.out.println("Arquivo já existe na pasta de origem, deletando e criando outro...");
            newFile.delete();
            newFile.createNewFile();
        }
        System.out.println("Arquivo criado com êxito!");
        FileWriter fileWriter = new FileWriter(newFile);
        fileWriter.write("Número de vértices: "+graph.getVertices()+"\nNúmero de arestas: "+edge_count+"\nGrau máximo: "+graph.getMax()+"\nGrau mínimo: "+graph.getMin()+
                "\nGrau médio: "+graph.getAverage()+"\n");
        fileWriter.close();
        System.out.print("Insira o vértice inicial da busca: ");
        int initial_vertex = Integer.parseInt(reader.readLine());
        graph.BFS(initial_vertex);
    }
}