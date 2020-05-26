import javax.ejb.Remote;
import java.util.ArrayList;

@Remote
public interface ICostRemote {

    /**
     *
     * @param x tablica kosztow wierszy
     * @param y tablica kosztow kolumn
     * @return minimalny koszt pociecia
     */
    double calculateMinimalCutCost(ArrayList<Double>x, ArrayList<Double> y);
}
