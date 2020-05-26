import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Stateless
public class Block implements IBlockRemote {


    /**
     * Metoda oblicza powierzchnie boczna figury
     *
     * @param pointsFloat lista wszystkich punktow
     * @return powierzchnia boczna figury
     */
    public float calculateLateralSurface(List<float[]> pointsFloat) {

        List<Point> points = new ArrayList<>();
        for(float[] f:pointsFloat){
            points.add(new Point(f[0], f[1], f[2]));
        }
        // aby utworzyc podstawe potrzeba conajmniej 3 punktow
        if (points.size() < 3) return 0;
        float height = calculateHeight(points);

        Vector<Point> hull = getHullPoints(points);
        float circuit = calculatePolygonCircuit(hull);
        return circuit * height;
    }

    /**
     * Metoda wykorzystuje algorytm Jarvisa do odszukania punktow otoczki
     *
     * @param points lista wszystkich punktow
     * @return lista punktow nalezacych do otoczki
     */
    public Vector<Point> getHullPoints(List<Point> points) {
        int n = points.size();
        Vector<Point> hull = new Vector<Point>();

        int l = 0;
        for (int i = 1; i < n; i++)
            if (points.get(i).getX() < points.get(l).getX())
                l = i;

        int q, p = l;
        do {
            hull.add(points.get(p));

            q = (p + 1) % n;
            for (int i = 0; i < n; i++) {
                if (clockOrientation(points.get(p), points.get(i), points.get(q)) == 2) q = i;
            }
            p = q;

        } while (p != l);

        return hull;
    }

    /**
     * Funkcja sprawdza czy polozenie punktow jest zgodne z zasada wskazowek zegara
     *
     * @param p pierwszy punkt tworzacy prosta
     * @param q drugi punkt tworzacy prosta
     * @param r trzeci punkt - sprawdzany
     * @return 0- punkty leza w jednej linni
     * 1-zgodne z wskazowkami zegara
     * 2- nie zgodne
     */
    public int clockOrientation(Point p, Point q, Point r) {
        float temp = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (temp == 0) return 0;
        return (temp > 0) ? 1 : 2;
    }

    /**
     * Metoda oblicza obwod podstawy
     *
     * @param points wektor zawierajacy punkty podstawy
     * @return obwod podstawy
     */
    private float calculatePolygonCircuit(Vector<Point> points) {
        float circuit = 0;

        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            circuit += Math.sqrt((p2.getY() - p1.getY()) * (p2.getY() - p1.getY())
                    + (p2.getX() - p1.getX()) * (p2.getX() - p1.getX()));
        }

        Point p1 = points.get(points.size() - 1);
        Point p2 = points.get(0);
        circuit += Math.sqrt((p2.getY() - p1.getY()) * (p2.getY() - p1.getY())
                + (p2.getX() - p1.getX()) * (p2.getX() - p1.getX()));

        return circuit;
    }

    /**
     * Metoda oblicza wysokosc figury
     *
     * @param points - lista punktow
     * @return srednia wartosc funkcji z =f(x,y)
     */
    private float calculateHeight(List<Point> points) {
        float sum = 0;
        for (Point p : points) {
            sum += p.getZ();
        }

        return sum / points.size();
    }
}


class Point {
    public Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;

    }

    private float x;
    private float y;
    private float z;

    public float getX() {
        return x;
    }


    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

}