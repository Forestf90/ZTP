import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * @author Michał Śliwa
 */
@WebServlet(name = "servlet136933")
public class Path extends HttpServlet {

    private static final long serialVersionUID = 8323012551755541446L;

    static String CONNECTION_STRING = "jdbc:sqlite:/home/forestf/Dokumenty/ztp-tasks/ztp//simple-gdata.db";
    static int K_NODE = 3;

    private ArrayList<Edge> graph;


    private void collectDataFromDB(String connectionString) {

        try {
            Connection conn = DriverManager.getConnection(connectionString);

            Statement s = conn.createStatement();
            ResultSet r = s.executeQuery("SELECT * from Gtable");

            while (r.next()) {
                int x = r.getInt("x");
                int y = r.getInt("y");
                float p = r.getFloat("p");

                this.graph.add(new Edge(x, y, p));
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private float getMinimalTransportCost() {
        float minCost = Float.MAX_VALUE;
        ArrayList<Integer> minPath; // minimalna sciezka - nigdy nie uzyta lecz miala zostac znaleziona

        ArrayList<ArrayList<Integer>> paths = getAllPaths(1, K_NODE);


        for (ArrayList<Integer> ar : paths) {

            float currentCost = calculateCost(ar);
            if (currentCost < minCost) {
                minCost = currentCost;
                minPath = ar;
            }
        }

        return minCost;
    }

    private ArrayList<ArrayList<Integer>> getAllPaths(int src, int des) {

        ArrayList<ArrayList<Integer>> paths = new ArrayList<>();

        ArrayList<Integer> currentPath = new ArrayList<>();
        currentPath.add(src);
        getAllPathsRec(currentPath, des, paths);

        return paths;
    }

    private void getAllPathsRec(ArrayList<Integer> currentPath, int des, ArrayList<ArrayList<Integer>> paths) {

        int last = currentPath.get(currentPath.size() - 1);

        if (last == des) {
            paths.add(currentPath);
            return;
        }
        ArrayList<Edge> neighbors = getNeighbors(last);

        for (Edge e : neighbors) {
            if (currentPath.contains(e.getY())) continue;
            ArrayList<Integer> newPath = new ArrayList<>(currentPath);
            newPath.add(e.getY());
            getAllPathsRec(newPath, des, paths);
        }

    }

    private ArrayList<Edge> getNeighbors(int x) {
        ArrayList<Edge> neighbors = new ArrayList<>();

        for (Edge e : graph) {
            if (e.getX() == x) neighbors.add(e);
        }
        return neighbors;
    }

    private float getEdgeCost(int x, int y) {
        for (Edge e : graph) {
            if (e.getY() == y && e.getX() == x) return e.getP();
        }
        return 0;
    }

    private float calculateCost(ArrayList<Integer> path) {

        float cost = 0;
        float preEdgeCost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            float edgeCost = getEdgeCost(path.get(i), path.get(i + 1));
            cost += 1 / edgeCost;
            if (i > 0) {
                cost += Math.abs(edgeCost - preEdgeCost);
            }
            preEdgeCost = edgeCost;
        }

        return cost;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.graph = new ArrayList<>();

        K_NODE = Integer.parseInt(request.getParameter("k"));
        CONNECTION_STRING = request.getParameter("dB");

        collectDataFromDB(CONNECTION_STRING);

        float result = getMinimalTransportCost();

        response.getWriter().print(String.format("%.3f", result));
    }


}

class Edge {

    private int x;
    private int y;
    private float p;

    public Edge(int x, int y, float p) {
        this.x = x;
        this.y = y;
        this.p = p;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public float getP() {
        return p;
    }
}

