package map.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Graph {
    private int V; // No. of vertices in graph.

    private LinkedList<Integer>[] adj; // Adjacency List
    // representation

    ArrayList<ArrayList<Integer>> components = new ArrayList<>();

    public Graph(int v) {
        V = v;
        adj = new LinkedList[v + 1];

        for (int i = 1; i <= v; i++)
            adj[i] = new LinkedList();
    }

    /**
     * add edge to the graph
     * @param u - edge
     * @param w - edge
     */
    public void addEdge(int u, int w) {
        adj[u].add(w);
        adj[w].add(u); // Undirected Graph.
    }

    /**
     * dfs to check for the longest path possible in a connected component
     * @param v - the number of vertices
     * @param visited - a vector which checks which vertices were visited
     *                this vector resets every time the call is over
     *                it helps with the pseudo backtracking
     * @param al - the current path that we have
     *           it will become empty by the end of the run
     *           it helps with the pseudo backtracking
     * @param max - it remembers the path that had the most members
     */
    void DFSUtil(int v,boolean[] visited, ArrayList<Integer> al, ArrayList<Integer> max) {
        visited[v] = true;
        al.add(v);
        Iterator<Integer> it = adj[v].iterator();

        if(al.size() > max.size()) {
            max.clear();
            max.addAll(new ArrayList<>(al));
        }

        while (it.hasNext()) {
            int n = it.next();
            if (!visited[n]) {
                DFSUtil(n, visited, al, max);
            }
        }

        al.remove(al.size() - 1);
        visited[v] = false;

    }

    /**
     * finds all the connected components
     * @param v - number of vertices
     * @param visited - vector which checks whether the vertices were visited or not
     * @param al - vector which stores the members of the connected component
     */
    void DFSUtil(int v, boolean[] visited, ArrayList<Integer> al) {
        visited[v] = true;
        al.add(v);
        Iterator<Integer> it = adj[v].iterator();

        while (it.hasNext()) {
            int n = it.next();
            if (!visited[n]) {
                DFSUtil(n, visited, al);
            }
        }
    }

    /**
     * wrapper for DFSUtil
     */
    public void DFScomp() {
        boolean[] visited = new boolean[V + 1];

        for (int i = 1; i <= V; i++) {
            ArrayList<Integer> al = new ArrayList<>();
            if (!visited[i]) {
                DFSUtil(i, visited, al);
                components.add(al);
            }
        }
    }

    /**
     * wrapper for dfs
     * @return the longest path in the graph
     */
    public ArrayList<Integer> DFSLongest() {
        boolean[] visited = new boolean[V + 1];
        ArrayList<Integer> max = new ArrayList<>();

        for (int i = 1; i <= V; i++) {
            ArrayList<Integer> al = new ArrayList<>();
            DFSUtil(i, visited, al, max);
        }
        return max;
    }

    /**
     * @return the number of connected components that the graph has
     */
    public int ConnectedComponents() {
        return components.size();
    }
}