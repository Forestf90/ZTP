import javax.ejb.Remote;
import java.util.ArrayList;

@Remote
public interface IPlateRemote {

    /**
     * Dodanie elementu do listy
     * @param x wartosc dodawana do listy
     */
    void addX(double x);

    /**
     * Dodawanie elementu do listy
     * @param y wartosc dodawana do listy
     */
    void addY(double y);

    /**
     *
     * @return lista elemetow X
     */
    ArrayList<Double> getXList();

    /**
     *
     * @return lista elementow y
     */
    ArrayList<Double> getYList();
}
