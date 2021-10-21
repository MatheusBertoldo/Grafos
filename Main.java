import java.io.*;
import java.util.*;
enum Color {BLACK, GREY, WHITE;}
class Graph {
    private int V;
    private LinkedList<Integer>[] adj;
    private Color[] visited;
    public int[] distance;
    private Integer[] parentNode;
    public String path = "";
    public Graph(int v) {
        V = v;
        parentNode = new Integer[v];
        visited = new Color[v];
        distance = new int[v];
        adj = new LinkedList[v];
        for (int i = 0; i < v; ++i) adj[i] = new LinkedList();
    }
    int getVertices() {
        return this.V;
    }
    int getMax() {
        int max_degree = Integer.MIN_VALUE;
        for (int i = 0; i < adj.length; i++) {
            if (adj[i].size() > max_degree) max_degree = adj[i].size();
        }
        return max_degree;
    }
    int getMin() {
        int min_degree = Integer.MAX_VALUE;
        for (int i = 0; i < adj.length; i++) {
            if (adj[i].size() < min_degree) min_degree = adj[i].size();
        }
        return min_degree;
    }
    double getAverage() {
        int sum = 0;
        for(int i = 0; i < getVertices(); i++){
            sum += adj[i].size();
        }
        return (double) sum/getVertices();
    }
    void addEdge(int v, int w) {
        adj[v - 1].add(w - 1);
        adj[w - 1].add(v - 1);
    }
    void BFS(int root){
        Arrays.fill(visited, Color.WHITE); Arrays.fill(distance, -1); Arrays.fill(parentNode, null);
        visited[root] = Color.GREY;
        distance[root] = 0;
        parentNode[root] = null;
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()){
            root = queue.poll();
            System.out.print((root+1) + " ");
            for(Integer i : adj[root]){
                if(visited[i] == Color.WHITE){
                    visited[i] = Color.GREY;
                    distance[i] = distance[root] + 1;
                    parentNode[i] = root;
                    queue.add(i);
                }
            } visited[root] = Color.BLACK;
        }
    }
    void createPath(int start, int target){
        if(target == start) path += (start+1) + " ";
        else if(parentNode[target] == null) System.out.printf("Nenhum caminho existente.%n");
        else{
            createPath(start, parentNode[target]);
            path += (target+1) + " ";
        }
    }
}
public class Main {
    static Graph graph;
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static int read_File(File file) throws Exception {
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        int vertex_count = Integer.parseInt(fileReader.readLine()), edge_count = 0;
        graph = new Graph(vertex_count);
        do{
            String[] edges = fileReader.readLine().split(" ");
            graph.addEdge(Integer.parseInt(edges[0]), Integer.parseInt(edges[1]));
            edge_count++;
        }while(fileReader.ready());
        fileReader.close();
        return edge_count;
    }
    public static void main(String args[]) throws Exception{
        System.out.printf("Insira o caminho para o arquivo:%n");
        String filePath = reader.readLine();
        if(System.getProperty("os.name").toLowerCase().contains("win")) filePath.replace("\\", "\\\\");
        File file = new File(filePath);
        int edge_Count = read_File(file);

        File newFile = new File(file.getPath().replace(file.getName(), "output.txt"));
        while(true){
            if(newFile.createNewFile()){
                System.out.printf("Arquivo criado com êxito!%n");
                break;
            }
            else {
                System.out.printf("O arquivo %s já existe!%nDeletando...%n", newFile.getName());
                newFile.delete();
            }
        }

        FileWriter writer = new FileWriter(newFile);
        writer.write("Número de vértices: "+graph.getVertices()+"\nNúmero de arestas: "+edge_Count+"\nGrau máximo: "+graph.getMax()+"\nGrau mínimo: "+graph.getMin()+
                "\nGrau médio: "+graph.getAverage()+"\n");

        System.out.printf("Insira o vértice inicial para a busca:%n");
        int root = Integer.parseInt(reader.readLine()) - 1;
        graph.BFS(root);
        for(int i = 0; i < graph.getVertices(); i++){
            graph.createPath(root, i);
            writer.write("Caminho da raiz até o alvo "+(i+1)+": "+graph.path+" com distância de "+graph.distance[i]+"\n");
            graph.path = "";
        }
        writer.write("\n\nNão consegui usar nenhuma API para plottar o grafo :(");
        System.out.printf("%nSucesso!%n");
        writer.close();
        reader.close();
    }
}
